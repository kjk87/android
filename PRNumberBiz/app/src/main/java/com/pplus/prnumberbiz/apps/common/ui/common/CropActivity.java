package com.pplus.prnumberbiz.apps.common.ui.common;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.view.View;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.util.ImageUtils;
import com.pplus.prnumberbiz.core.util.imageresizer.ImageResizer;
import com.pplus.prnumberbiz.core.util.imageresizer.operations.ImageRotation;
import com.pplus.prnumberbiz.core.util.imageresizer.operations.ResizeMode;
import com.pplus.prnumberbiz.core.util.imageresizer.utils.ImageWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dev on 2015-12-28.
 */
public class CropActivity extends BaseActivity implements View.OnClickListener, ImplToolbar{

    private CropImageView cropImageView;
    private Intent i;
    private Uri mSaveUri = null;
    private Bitmap mCroppedBitmap;
    private String mKey;
    private View rotate_layout, crop_fix_btn_rotation;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_crop;
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_image_edit), ToolbarOption.ToolbarMenu.LEFT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };

    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        mSaveUri = getIntent().getData();
        mCroppedBitmap = getBitmap();

        rotate_layout = findViewById(R.id.crop_rotate_layout);
        crop_fix_btn_rotation = findViewById(R.id.crop_fix_btn_rotation);
        mKey = getIntent().getStringExtra(Const.KEY);

        cropImageView = (CropImageView) findViewById(R.id.crop_image);
        cropImageView.setHandleSizeInDp(10);
        cropImageView.setTouchPaddingInDp(8);
        cropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);

//        if(StringUtils.isNotEmpty(mKey) && mKey.equals(Const.REPRESENT)){
//            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
//            rotate_layout.setVisibility(View.GONE);
//            crop_fix_btn_rotation.setVisibility(View.VISIBLE);
//        }else{
//            cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
//            rotate_layout.setVisibility(View.VISIBLE);
//            crop_fix_btn_rotation.setVisibility(View.GONE);
//        }

        cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
        rotate_layout.setVisibility(View.GONE);
        crop_fix_btn_rotation.setVisibility(View.VISIBLE);

        try {
            cropImageView.post(new Runnable(){

                @Override
                public void run(){

                    cropImageView.setImageBitmap(mCroppedBitmap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        crop_fix_btn_rotation.setOnClickListener(this);
        findViewById(R.id.crop_btn_bottom_text).setOnClickListener(this);
        findViewById(R.id.crop_btn_rotation).setOnClickListener(this);
        findViewById(R.id.crop_btn_free).setOnClickListener(this);
        findViewById(R.id.crop_btn_1_1).setOnClickListener(this);
        findViewById(R.id.btn_crop_4_3).setOnClickListener(this);
        findViewById(R.id.crop_btn_3_4).setOnClickListener(this);

    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.crop_btn_bottom_text:
                mCroppedBitmap = cropImageView.getCroppedBitmap();
                onSaveClicked();
                break;
            case R.id.crop_fix_btn_rotation:
            case R.id.crop_btn_rotation:
                CropImageView.RotateDegrees current_rotate_degrees = CropImageView.RotateDegrees.ROATATE_NORMAL;
                if(current_rotate_degrees == CropImageView.RotateDegrees.ROATATE_NORMAL) {
                    current_rotate_degrees = CropImageView.RotateDegrees.ROTATE_90D;
                } else if(current_rotate_degrees == CropImageView.RotateDegrees.ROTATE_90D) {
                    current_rotate_degrees = CropImageView.RotateDegrees.ROTATE_180D;
                } else if(current_rotate_degrees == CropImageView.RotateDegrees.ROTATE_180D) {
                    current_rotate_degrees = CropImageView.RotateDegrees.ROTATE_270D;
                } else {
                    current_rotate_degrees = CropImageView.RotateDegrees.ROATATE_NORMAL;
                }
                cropImageView.rotateImage(current_rotate_degrees);
                break;
            case R.id.crop_btn_free:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                break;
            case R.id.crop_btn_1_1:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                break;
            case R.id.btn_crop_4_3:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            case R.id.crop_btn_3_4:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                break;
        }
    }

    boolean mSaving;  // Whether the "save" button is already clicked.

    private void onSaveClicked(){

        if(mSaving) return;

        mSaving = true;

        // Return the cropped image directly or save it to the specified URI.
        Bundle myExtras = getIntent().getExtras();
        if(myExtras != null && (myExtras.getParcelable("data") != null || myExtras.getBoolean("return-data"))) {

            File output;
            String currentPhotoPath = "";

            try {
                output = ImageUtils.setUpPhotoFile(new File(mSaveUri.getPath()));
                currentPhotoPath = output.getAbsolutePath();
            } catch (Exception e) {
                LogUtil.e("doPickImage", e.toString());
                return;
            }

            boolean writed = writeBitmapToFile(mCroppedBitmap, output);

            Intent intent = new Intent();

            if(writed) intent.setData(Uri.fromFile(output));

            //			Bundle extras = new Bundle();
            //			extras.putParcelable("data", croppedImage);
            //			intent.putExtras(extrx`as);
            setResult(RESULT_OK, intent);
            finish();
        }
//        else {
        //            final Bitmap b = mCroppedBitmap;
        //            saveOutput(b);
        //        }
    }

    private void saveOutput(Bitmap croppedImage){

        if(mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if(outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                }
            } catch (IOException ex) {
                // TODO: report error to caller
                android.util.Log.e("Cropper", "Cannot open file: " + mSaveUri, ex);
            } finally {
                finish();
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString()).putExtras(extras));
        } else {
            android.util.Log.e("Cropper", "not defined image url");
        }
        croppedImage.recycle();
        finish();
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

    private boolean writeBitmapToFile(Bitmap bitmap, File file){

        return ImageWriter.writeToFile(bitmap, file);
    }

    public Bitmap getBitmap(){

        File originFile = new File(mSaveUri.getPath());

        File tempFile;

        ImageRotation imageRotation = null;
        try {
            ExifInterface exif = new ExifInterface(mSaveUri.getPath());

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageRotation = ImageRotation.CW_90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageRotation = ImageRotation.CW_180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageRotation = ImageRotation.CW_270;
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        tempFile = new File(getCacheDir(), originFile.getName());

        if(!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap bitmap = null;

        try {
            if(imageRotation != null) {
                switch (imageRotation) {
                    case CW_90:
                    case CW_180:
                        bitmap = ImageResizer.resize(originFile, 1280, 720, ResizeMode.FIT_TO_HEIGHT);
                        break;
                    default:
                        bitmap = ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH);
                        break;
                }
            } else {
                bitmap = ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH);
            }

            if(imageRotation != null) {

                ImageResizer.saveToFile(bitmap, tempFile);
                bitmap = ImageResizer.rotate(tempFile, imageRotation);
            }

            return bitmap;

        } catch (OutOfMemoryError error) {
            LogUtil.e(LOG_TAG, "OutOfMemoryError = {}", error);
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "Exception = {}", e);
        } finally {
            LogUtil.e(LOG_TAG, "finally");
            if(tempFile != null) {
                if(tempFile.exists()) {
                    tempFile.delete();
                    deleteFromMediaScanner(tempFile.getAbsolutePath());
                }
            }
        }
        return null;
    }

    private boolean deleteFromMediaScanner(String filePath){

        Uri fileUri = Uri.parse(filePath);
        filePath = fileUri.getPath();

        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='" + filePath + "'", null, null);
        if(c.moveToFirst()) {
            int id = c.getInt(0);
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            return getContentResolver().delete(uri, null, null) == 1;
        }
        return false;
    }
}
