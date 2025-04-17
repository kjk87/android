package com.lejel.wowbox.core.util.imageresizer.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.webkit.MimeTypeMap;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageWriter {

    public static boolean writeToFile(Bitmap image, File file) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        String mimeType = getMimeType(file);

        if (!StringUtils.isEmpty(mimeType) && mimeType.indexOf("png") != -1) {
            image.setHasAlpha(true);
            image.compress(CompressFormat.PNG, 100, bytes);
        } else {
            image.compress(CompressFormat.JPEG, 100, bytes);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes.toByteArray());

        } catch (IOException e) {
            LogUtil.e("ImageWriter", e.toString());
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtil.e("ImageWriter", e.toString());
                }
            }
        }

        return true;
    }

    public static String getMimeType(File file) {

        return getMimeType(file.getPath());
    }

    public static String getMimeType(String url) {

        String type = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        if (StringUtils.isEmpty(type)) {
            return null;
        }

        return type;
    }
}
