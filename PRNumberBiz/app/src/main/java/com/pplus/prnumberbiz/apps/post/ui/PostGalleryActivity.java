package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
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
import com.pplus.prnumberbiz.apps.post.data.GalleryGridAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class PostGalleryActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_post_gallery;
    }

    private GalleryGridAdapter mAdapter;
    private List<UCropView> mUCropViewList;
    private List<View> mViewList;
    private ArrayList<Uri> mCroppedImageList;
    private View mBlockingView;
    private RelativeLayout layout_post_crop;
    private int mVisiblePosition = -1;
    private boolean mIsCamera = false;
    private int mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT;

    @Override
    public void initializeView(Bundle savedInstanceState){


        mMaxCount = getIntent().getIntExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT);

        RecyclerView recyclerGallery = (RecyclerView) findViewById(R.id.recycler_post_gallery);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerGallery.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.width_3)));
        recyclerGallery.setLayoutManager(layoutManager);

        mAdapter = new GalleryGridAdapter(this);
        recyclerGallery.setAdapter(mAdapter);
        mAdapter.setMaxCount(mMaxCount);

        findViewById(R.id.layout_post_gallery_crop).getLayoutParams().height = (int) (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f));

        mUCropViewList = new ArrayList<>();
        mViewList = new ArrayList<>();

        layout_post_crop = (RelativeLayout) findViewById(R.id.layout_post_crop);


        mBlockingView = findViewById(R.id.view_post_gallery_blocking);
        mBlockingView.setClickable(true);

        findViewById(R.id.text_gallery_cancel).setOnClickListener(this);
        findViewById(R.id.text_gallery_next).setOnClickListener(this);
        findViewById(R.id.image_post_gallery_camera).setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.text_gallery_cancel:
                onBackPressed();
                break;
            case R.id.text_gallery_next:
                if(mUCropViewList.size() > 0) {
                    crop(0);
                }else{
                    showAlert(R.string.msg_select_reg_photo);
                }
                break;
            case R.id.image_post_gallery_camera:
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

    private String mCurrentPhotoPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        LogUtil.e(LOG_TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case Const.REQ_CAMERA:
                    if(StringUtils.isNotEmpty(mCurrentPhotoPath)){
                        galleryAddPic(Uri.fromFile(new File(mCurrentPhotoPath)));
                        GalleryData galleryData = new GalleryData();
                        galleryData.setChecked(true);
                        galleryData.setImageUrl(mCurrentPhotoPath);
                        mAdapter.add(galleryData);

//                        mCurrentPhotoPath = null;
//                        MediaScannerConnection.scanFile(getApplicationContext(), new String[] { mCurrentPhotoPath }, null, new MediaScannerConnection.OnScanCompletedListener() {
//
//                            @Override
//                            public void onScanCompleted(String path, Uri uri) {
//                                // TODO Auto-generated method stub
//                                LogUtil.e(LOG_TAG, "onScanCompleted : {}", path);
//
//                            }
//                        });
                    }
                    break;
                case Const.REQ_GALLERY:
                    if(data != null){
                        data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, data.getParcelableArrayListExtra(Const.CROPPED_IMAGE));
                        setResult(RESULT_OK, data);
                        finish();
                    }
                    break;
            }
        }else{
            switch (requestCode) {
                case Const.REQ_CAMERA:
                    mIsCamera = false;
//                    mCurrentPhotoPath = null;
                    break;
                case Const.REQ_GALLERY:
                    new AsyncTask<Void, Void, Void>(){

                        @Override
                        protected Void doInBackground(Void... voids){
                            for(Uri uri : mCroppedImageList) {
                                PplusCommonUtil.Companion.deleteFromMediaScanner(uri.getPath());
                            }

                            return null;
                        }
                    }.execute();

                    break;
            }
        }
    }

    public void goCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = ImageUtils.setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            mIsCamera = true;
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

    @Override
    protected void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        LogUtil.e(LOG_TAG, "onSaveInstanceState");
//        outState.putParcelableArrayList("select_list", mAdapter.getSelectGalleryList());
//        outState.putBoolean("camera", mIsCamera);
//        outState.putString("photopath", mCurrentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.e(LOG_TAG, "onRestoreInstanceState");
        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey("photopath")) {
//                mCurrentPhotoPath = savedInstanceState.getString("photopath");
//
//            }
//            if (savedInstanceState.containsKey("camera")) {
//                mIsCamera = savedInstanceState.getBoolean("camera");
//            }
//            if (savedInstanceState.containsKey("select_list")) {
//                if(mAdapter != null) mAdapter.setSelectGalleryList(savedInstanceState.<GalleryData>getParcelableArrayList("select_list"));
//            }
        }
    }

    public void changeImage(int position){

//        mUCropViewList.get(mVisiblePosition).animate().alpha(0);
        mVisiblePosition = position;
        mUCropViewList.get(position).animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
        layout_post_crop.bringChildToFront(mViewList.get(mVisiblePosition));
    }

    public void removeImage(int position){

        layout_post_crop.removeView(mViewList.get(position));
        mUCropViewList.remove(position);
        mViewList.remove(position);
        mVisiblePosition = mUCropViewList.size() - 1;
        if(mVisiblePosition >= 0) {
            layout_post_crop.bringChildToFront(mViewList.get(mVisiblePosition));
            mUCropViewList.get(mVisiblePosition).animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
        }
    }

    public void addImage(GalleryData data){

        try {
            mAdapter.setBlocking(true);

//            if(mUCropViewList.size() > 0) {
//                mUCropViewList.get(mVisiblePosition).animate().alpha(0);
//            }

            View view = getLayoutInflater().inflate(R.layout.layout_ucrop, new RelativeLayout(this));
            final UCropView uCropView = (UCropView) view.findViewById(R.id.ucrop_view);

            final GestureCropImageView gestureCropImageView = uCropView.getCropImageView();
            gestureCropImageView.setBackgroundColor(ResourceUtil.getColor(this, R.color.black));
            OverlayView mOverlayView = uCropView.getOverlayView();
            gestureCropImageView.setScaleEnabled(true);
            gestureCropImageView.setRotateEnabled(false);
            gestureCropImageView.setTargetAspectRatio(4f / 3f);

            view.findViewById(R.id.image_rotate).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    gestureCropImageView.postRotate(90);
                }
            });

            mUCropViewList.add(uCropView);
            mViewList.add(view);
            layout_post_crop.addView(view);

            gestureCropImageView.setTransformImageListener(transformImageListener);

            Uri inputUri = Uri.fromFile(new File(data.getImageUrl()));
            String[] names = inputUri.getPath().split("/");
            String fileName = names[names.length-1];
            LogUtil.e(LOG_TAG, fileName);
            File albumF = PplusCommonUtil.Companion.getAlbumDir();
            Uri outputUri = Uri.fromFile(new File(albumF, "temp_" + fileName));
            gestureCropImageView.setImageUri(inputUri, outputUri);
        } catch (Exception e) {

        }


    }

    TransformImageView.TransformImageListener transformImageListener = new TransformImageView.TransformImageListener(){

        @Override
        public void onLoadComplete(){

            LogUtil.e("crop", "onLoadComplete");

            mVisiblePosition = mUCropViewList.size() - 1;
            mUCropViewList.get(mVisiblePosition).animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
            mBlockingView.setClickable(false);
            mAdapter.setBlocking(false);

        }

        @Override
        public void onLoadFailure(@NonNull Exception e){

        }

        @Override
        public void onRotate(float currentAngle){

        }

        @Override
        public void onScale(float currentScale){

        }
    };

    private void crop(final int position){
        showProgress(getString(R.string.msg_editing_image));
        if(position == 0) {
            mCroppedImageList = new ArrayList<>();
        }
        mUCropViewList.get(position).getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback(){

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight){

                mCroppedImageList.add(resultUri);
                galleryAddPic(resultUri);
                if(position < mUCropViewList.size() - 1) {
                    crop(position + 1);
                } else {
                    hideProgress();

                    Intent intent = new Intent(PostGalleryActivity.this, PostImageEditActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList);
                    startActivityForResult(intent, Const.REQ_GALLERY);
                }

            }

            @Override
            public void onCropFailure(@NonNull Throwable t){
                hideProgress();
            }
        });
    }

    private void galleryAddPic(Uri uri){

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//        File f = new File(uri.getPath());
//        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
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

//                    if(mAdapter.getSelectGalleryList().contains(galleryData)){
//                        galleryData.setChecked(true);
//                    }
//
//                    try{
//
//                        if(mIsCamera && StringUtils.isNotEmpty(mCurrentPhotoPath) && galleryData.getImageUrl().contains(mCurrentPhotoPath)){
//                            mIsCamera = false;
//                            galleryData.setChecked(true);
//                        }
//                    }catch (Exception e){
//
//                    }

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
            mAdapter.clear();
            mAdapter.addAll(result);
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
