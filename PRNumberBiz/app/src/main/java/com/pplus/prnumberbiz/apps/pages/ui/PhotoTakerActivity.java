package com.pplus.prnumberbiz.apps.pages.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.common.CropActivity;
import com.pplus.prnumberbiz.core.util.ImageFilePath;
import com.pplus.prnumberbiz.core.util.ImageUtils;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoTakerActivity extends Activity{

    private static final String FILE_PATH = "file_path";

    private boolean return_data = true;
    private boolean fix_ratio = false;
    private boolean crop = false;
    private boolean faceDetection = true;

    private File mDirectory;
    //    private OnNotFoundCropIntentListener mNotFoundCropIntentListener;
    private Uri mCropUri;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(FILE_PATH);
            if(!StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                setResultData();
//                galleryAddPic();
//                File f = new File(mCurrentPhotoPath);
//                Uri contentUri = Uri.fromFile(f);
//                doCropImage(contentUri);
            }
            return;
        }

        String mode = getIntent().getStringExtra("mode");
        crop = getIntent().getBooleanExtra("crop", false);
        mDirectory = PplusCommonUtil.Companion.getAlbumDir();

        if(mode.equals("camera")) {
            doImageCapture();
        } else if(mode.equals("picture")) {
            doPickImage();
        } else {
            finish();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putString(FILE_PATH, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
        LogUtil.e("yklee", "PhotoTakerActivity onSaveInstanceState");
    }

    private void setResultData(){

        LogUtil.e("path", mCurrentPhotoPath);
        galleryAddPic();
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Intent intent = new Intent();
        intent.setData(contentUri);
        intent.putExtra(Const.PATH, mCurrentPhotoPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    //    public void setNotFoundCropIntentListener(
    //            OnNotFoundCropIntentListener listener) {
    //        mNotFoundCropIntentListener = listener;
    //    }

    //    private MediaUriFinder.MediaScannedListener mScanner = new MediaUriFinder.MediaScannedListener() {
    //
    //        @Override
    //        public boolean OnScanned(Uri uri) {
    //            /*
    //             * Start Crop Activity with URI that we get once scanned if not
    //			 * found Support Crop Activity then run OnNotFoundCropIntent()
    //			 */
    //            if (!doCropImage(uri) && mNotFoundCropIntentListener != null)
    //                mNotFoundCropIntentListener.OnNotFoundCropIntent(
    //                        mDirectory.getAbsolutePath(), mCropUri);
    //            return false;
    //        }
    //    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Const.REQ_PICK_FROM_FILE:
                    Uri dataUri = data.getData();

                    if(dataUri != null) {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

//                        Cursor cursor = getContentResolver().query(dataUri, filePathColumn, null, null, null);
//                        if(cursor == null){
//                            return;
//                        }
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex);
//                        cursor.close();
//                        LogUtil.e("LOG", "path : {}", picturePath);
                        mCurrentPhotoPath = ImageFilePath.getPath(this, dataUri);
                        LogUtil.e("PHOTOTAKER", mCurrentPhotoPath);
                        if(crop) {
                            doCropImage(Uri.fromFile(new File(mCurrentPhotoPath)));
                        }else{
                            setResultData();
                        }

                        //                        if(dataUri.getScheme().trim().equalsIgnoreCase("content")) {
                        //
                        //                        } else if(dataUri.getScheme().trim().equalsIgnoreCase("file")) {
                        //                            //                            MediaUriFinder.create(this, dataUri.getPath(), mScanner);
                        //                        }
                    }
                    break;
                case Const.REQ_CROP_IMAGE:
                    LogUtil.e("data", "data : {}", data.getData().getPath());
                    galleryAddPic();
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                case Const.REQ_CAMERA:

                    if(mCurrentPhotoPath != null) {
                        galleryAddPic();
                        File f = new File(mCurrentPhotoPath);
                        Uri contentUri = Uri.fromFile(f);
                        if(crop) {
                            doCropImage(contentUri);
                        }else{
                            setResultData();
                        }

                    }
                    break;
            }// end Switch case
        } else {
            if(StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                PplusCommonUtil.Companion.deleteFromMediaScanner(mCurrentPhotoPath);
            }
            setResult(RESULT_CANCELED);
            finish();

        }
    }

    private Bitmap getBitmap(Uri uri){

        InputStream in = null;
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            return bm;
        } catch (Exception e) {
            LogUtil.e("Cropper", "file " + uri.toString() + " not found");
        }
        return null;
    }

    private boolean writeBitmapToFile(Bitmap bitmap, File file){

        try {
            if(bitmap != null) {
                FileOutputStream fops = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fops);
                fops.flush();
                fops.close();
                fops = null;
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void doCropImage(Uri uri){

        //        if(!uri.getScheme().trim().equalsIgnoreCase("file")) {
        //            //파일이 아닌경우 편집이 안됨
        //            return false;
        //        }

        Intent intent = new Intent(this, CropActivity.class);
        //편집 후 출력경로
        intent.setType("image/*");
        //원본데이터
        intent.setData(uri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Const.REQ_CROP_IMAGE);
    }

    private String mCurrentPhotoPath;

    public void doImageCapture(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = ImageUtils.setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), f));
            startActivityForResult(intent, Const.REQ_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
    }

    private void galleryAddPic(){

        if(mCurrentPhotoPath != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }

    }

    public String getRealImagePath(Uri uri){

        String[] proj = {MediaStore.Video.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

        if(cursor.moveToFirst()) {
            String path = cursor.getString(index);
            cursor.close();
            return path;
        } else {
            cursor.close();
            return null;
        }
    }

    public boolean doPickImage(){

        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Const.REQ_PICK_FROM_FILE);
            return true;
        } catch (Exception e) {
            LogUtil.e("doPickImage", e.toString());
            return false;
        }
    }

    private File getFile(String name){

        File output = new File(name);
        if(!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    @Override
    protected void finalize() throws Throwable{

        mCropUri = null;
        //        mNotFoundCropIntentListener = null;
        super.finalize();
    }

    //    public static interface OnNotFoundCropIntentListener {
    //        public boolean OnNotFoundCropIntent(String path, Uri uri);
    //    }
}
