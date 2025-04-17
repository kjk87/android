package com.lejel.wowbox.core.util;

import com.lejel.wowbox.core.util.imageresizer.utils.ImageWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Windows7-00 on 2016-11-04.
 */

public class ImageUtils{

    private final String TEMP_PREFIX = "tmp_";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static String JPEG_FILE_SUFFIX = "jpg";

    public static File setUpPhotoFile(File file) throws IOException{

        String mimeType = ImageWriter.getMimeType(file);

        if(mimeType != null) {
            String value[] = mimeType.split("/");
            JPEG_FILE_SUFFIX = value[1];
        }

        File f = createImageFile();
        return f;
    }

    public static File setUpPhotoFile() throws IOException{

        File f = createImageFile();
        return f;
    }

    private static File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = PplusCommonUtil.Companion.getAlbumDir();
        File imageF = File.createTempFile(imageFileName, "." + JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

}
