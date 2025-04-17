package com.pplus.luckybol.core.network.model.request.params;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.core.code.custom.AttachmentTargetTypeCode;
import com.pplus.luckybol.core.network.model.request.BaseParams;
import com.pplus.luckybol.core.util.imageresizer.ImageResizer;
import com.pplus.luckybol.core.util.imageresizer.operations.ImageRotation;
import com.pplus.luckybol.core.util.imageresizer.operations.ResizeMode;

import java.io.File;
import java.io.IOException;

/**
 * Created by j2n on 2016. 9. 29..
 */

public class ParamsAttachment extends BaseParams{

    @Override
    public boolean equals(Object o){

        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ParamsAttachment that = (ParamsAttachment) o;

        if(targetType != that.targetType) return false;
        return file != null ? file.equals(that.file) : that.file == null;

    }

    @Override
    public int hashCode(){

        int result = targetType != null ? targetType.hashCode() : 0;
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    private AttachmentTargetTypeCode targetType = AttachmentTargetTypeCode.postList;
    private Long targetNo;
    private boolean resize = true;
    private File file;

    public AttachmentTargetTypeCode getTargetType(){

        return targetType;
    }

    public void setTargetType(AttachmentTargetTypeCode targetType){

        this.targetType = targetType;
    }

    public Long getTargetNo(){

        return targetNo;
    }

    public void setTargetNo(Long targetNo){

        this.targetNo = targetNo;
    }

    public File getFile(){

        return file;
    }

    public void setFile(String path){

        file = new File(path);
    }

    public boolean isResize(){

        return resize;
    }

    public void setResize(boolean resize){

        this.resize = resize;
    }

    public void buildFile(){

        String path = file.getPath();

        File originFile = new File(path);

        File tempFile;

        ImageRotation imageRotation = null;
        try {
            ExifInterface exif = new ExifInterface(path);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    //                    rotate = 90;
                    imageRotation = ImageRotation.CW_90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    //                    rotate = 180;
                    imageRotation = ImageRotation.CW_180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    //                    rotate = 270;
                    imageRotation = ImageRotation.CW_270;
                    break;
            }

        } catch (IOException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        tempFile = new File(LuckyBolApplication.getContext().getCacheDir(), originFile.getName());

        if(!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                LogUtil.e(LOG_TAG, e.toString());
            }
        }

        Bitmap bitmap = null;

        if(isResize()){
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
                ImageResizer.saveToFile(bitmap, tempFile);

            } catch (OutOfMemoryError error) {
                LogUtil.e(LOG_TAG, "error = {}", error);
            } finally {
                if(bitmap != null){
                    bitmap.recycle();
                }

            }
        }

        if(imageRotation != null) {
            try {
                bitmap = ImageResizer.rotate(tempFile, imageRotation);
                ImageResizer.saveToFile(bitmap, tempFile);
            } catch (OutOfMemoryError error) {
                LogUtil.e(LOG_TAG, "error = {}", error);
            } finally {
                if(bitmap != null){
                    bitmap.recycle();
                }

            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tempFile.getPath(), options);

        if(bitmap != null) {
            bitmap.recycle();
        }

        // 최종 파일 변경
        this.file = tempFile;
    }
}
