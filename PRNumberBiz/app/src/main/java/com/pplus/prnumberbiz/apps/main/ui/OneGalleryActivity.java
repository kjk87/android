package com.pplus.prnumberbiz.apps.main.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pple.pplus.utils.part.apps.permission.Permission;
import com.pple.pplus.utils.part.apps.permission.PermissionListener;
import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.info.DeviceUtil;
import com.pple.pplus.utils.part.info.OsUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.main.data.GalleryOneAdapter;
import com.pplus.prnumberbiz.core.network.model.dto.GalleryData;
import com.pplus.prnumberbiz.core.util.ImageUtils;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OneGalleryActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_select_one;
    }

    private GestureCropImageView mGestureCropImageView;

    private GalleryOneAdapter mOneAdapter;
    private View mBlockingView;
    private RelativeLayout layout_crop;

    @Override
    public void initializeView(Bundle savedInstanceState){

        RecyclerView recyclerGallery = (RecyclerView) findViewById(R.id.recycler_select_gallery);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerGallery.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.width_3)));
        recyclerGallery.setLayoutManager(layoutManager);

        mOneAdapter = new GalleryOneAdapter(this);
        recyclerGallery.setAdapter(mOneAdapter);

        findViewById(R.id.layout_gallery_crop).getLayoutParams().height = (int) (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f));

        View view = getLayoutInflater().inflate(R.layout.layout_ucrop, new RelativeLayout(this));
        final UCropView uCropView = (UCropView) view.findViewById(R.id.ucrop_view);

        mGestureCropImageView = uCropView.getCropImageView();
        mGestureCropImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mGestureCropImageView.setBackgroundColor(ResourceUtil.getColor(this, R.color.black));
        OverlayView mOverlayView = uCropView.getOverlayView();
        mOverlayView.getLayoutParams().width = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS;
        mGestureCropImageView.setScaleEnabled(true);
        mGestureCropImageView.setRotateEnabled(false);
        mGestureCropImageView.setTargetAspectRatio(1f);

        view.findViewById(R.id.image_rotate).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                mGestureCropImageView.postRotate(90);
            }
        });
        layout_crop = (RelativeLayout) findViewById(R.id.layout_crop);
        layout_crop.addView(view);

        mBlockingView = findViewById(R.id.view_crop_blocking);
        mBlockingView.setClickable(true);
        mGestureCropImageView.setTransformImageListener(new TransformImageView.TransformImageListener(){

            @Override
            public void onLoadComplete(){

                LogUtil.e("crop", "onLoadComplete");
                mBlockingView.setClickable(false);
                uCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
            }

            @Override
            public void onLoadFailure(@NonNull Exception e){

                LogUtil.e("crop", "onLoadFailure");
            }

            @Override
            public void onRotate(float currentAngle){

            }

            @Override
            public void onScale(float currentScale){

            }
        });

        findViewById(R.id.text_gallery_cancel).setOnClickListener(this);
        findViewById(R.id.text_gallery_complete).setOnClickListener(this);
        findViewById(R.id.image_gallery_camera).setOnClickListener(this);
        PPlusPermission pPlusPermission = new PPlusPermission(this);
        pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE);
        pPlusPermission.setPermissionListener(new PermissionListener(){

            @Override
            public void onPermissionGranted(){

                GalleryListSync galleryListSync = new GalleryListSync();
                galleryListSync.execute();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions){

            }
        });
        pPlusPermission.checkPermission();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_gallery_cancel:
                onBackPressed();
                break;
            case R.id.text_gallery_complete:
                showProgress(getString(R.string.msg_editing_image));
                mGestureCropImageView.cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback(){

                    @Override
                    public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight){

                        Intent data = new Intent();
                        data.setData(resultUri);
                        setResult(RESULT_OK, data);
                        finish();
                        hideProgress();
                    }

                    @Override
                    public void onCropFailure(@NonNull Throwable t){

                        hideProgress();
                    }
                });
                break;
            case R.id.image_gallery_camera:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final PPlusPermission pPlusPermission = new PPlusPermission(this);
                    pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE);
                    pPlusPermission.addPermission(Permission.PERMISSION_KEY.CAMERA);
                    pPlusPermission.setPermissionListener(new PermissionListener(){

                        @Override
                        public void onPermissionGranted(){

                            goCamera();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions){

                            LogUtil.e(LOG_TAG, "onPermissionDenied");
                            Intent intent = pPlusPermission.getSettingIntent();
                            startActivityForResult(intent, Const.REQ_CAMERA_PERMISSION);
                        }
                    });
                    pPlusPermission.checkPermission();
                } else {
                    goCamera();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case Const.REQ_CAMERA:
                    if(StringUtils.isNotEmpty(mCurrentPhotoPath)){
                        galleryAddPic(Uri.fromFile(new File(mCurrentPhotoPath)));
                        GalleryData galleryData = new GalleryData();
                        galleryData.setChecked(true);
                        galleryData.setImageUrl(mCurrentPhotoPath);
                        mOneAdapter.add(galleryData);
                        setSelect(galleryData);
                    }
                    break;
            }
        }
    }

    private void galleryAddPic(Uri uri){

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        //        File f = new File(uri.getPath());
        //        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }

    private String mCurrentPhotoPath;
    public void goCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = ImageUtils.setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            if(OsUtil.isLollipop()){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), f));
            }else{
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            }
            startActivityForResult(intent, Const.REQ_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
        }
    }

    public void setSelect(GalleryData data){

        try {

            Uri inputUri = Uri.fromFile(new File(data.getImageUrl()));
            String[] names = inputUri.getPath().split("/");
            String fileName = names[names.length - 1];
            LogUtil.e(LOG_TAG, fileName);
            File albumF = PplusCommonUtil.Companion.getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Uri outputUri = Uri.fromFile(new File(albumF, "temp_" + timeStamp + "_" + fileName));
            mGestureCropImageView.setImageUri(inputUri, outputUri);
        } catch (Exception e) {

        }
    }

    class GalleryListSync extends AsyncTask<Void, Void, ArrayList<GalleryData>>{

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            //            showProgress("gallery prg");
        }

        @Override
        protected ArrayList<GalleryData> doInBackground(Void... params){

            final String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE};
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
            ArrayList<GalleryData> infos = new ArrayList<>();
            GalleryData galleryData = null;

            if(imageCursor != null && imageCursor.moveToFirst()) {
                do {
                    galleryData = new GalleryData();
                    int idColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                    int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    int orientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
                    int mimeTypeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                    int sizeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                    galleryData.setId(imageCursor.getLong(idColumnIndex));
                    galleryData.setImageUrl(imageCursor.getString(dataColumnIndex));
                    String type = imageCursor.getString(dataColumnIndex).substring(imageCursor.getString(dataColumnIndex).lastIndexOf(".") + 1);
                    galleryData.setOrientation(imageCursor.getInt(orientationColumnIndex));
                    galleryData.setImageType(type);
                    galleryData.setMimeType(imageCursor.getString(mimeTypeIndex));
                    galleryData.setSize(imageCursor.getInt(sizeIndex));
                    String[] folderList = galleryData.getImageUrl().split("/");
                    galleryData.setFolder(folderList[folderList.length - 2]);

                    infos.add(galleryData);

                } while (imageCursor.moveToNext());

                imageCursor.close();
            }

            return infos;
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryData> result){

            super.onPostExecute(result);
            hideProgress();
            mOneAdapter.clear();
            mOneAdapter.addAll(result);
        }

    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpacesItemDecoration(int space){

            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            if(itemPosition % 4 == 3) {
                outRect.right = 0;
            } else {
                outRect.right = space;
            }

            outRect.bottom = space;
            // Add top margin only for the first item to avoid double space between items
        }
    }
}
