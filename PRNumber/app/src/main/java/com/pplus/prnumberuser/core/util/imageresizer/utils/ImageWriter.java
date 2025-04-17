package com.pplus.prnumberuser.core.util.imageresizer.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.webkit.MimeTypeMap;

import com.pplus.utils.part.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageWriter {

	public static boolean writeToFile(Bitmap image, File file) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		image.setHasAlpha(true);
		image.compress(CompressFormat.PNG, 100, bytes);

		try {
    		FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes.toByteArray());
			fos.close();
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	
    	return true;
	}

	public static String getMimeType(File file){

		return getMimeType(file.getPath());
	}

	public static String getMimeType(String url){

		String type = null;

		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if(extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}

		if(StringUtils.isEmpty(type)) {
			return null;
		}

		return type;
	}
}
