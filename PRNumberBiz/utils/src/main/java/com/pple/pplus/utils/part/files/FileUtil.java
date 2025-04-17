package com.pple.pplus.utils.part.files;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.pple.pplus.utils.part.logs.LogUtil;

/**
 * Created by 안명훈 on 16. 6. 27..
 */
public class FileUtil {

    private static String LOG_TAG = FileUtil.class.getSimpleName();

    /* Checks if external storage is available for read and write */
    /**
     * 파일을 쓰기를 할 수 있는 권한 체크
     * */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    /**
     * 파일을 읽을수 있는 권한여부 체크
     * */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @return Text종류 파일에 대해 String으로 반환함.
     * */
    public static String getTextFileRead(File file){

        StringBuffer stringBuffer = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                stringBuffer.append(line).append("\n");
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            LogUtil.e(LOG_TAG , "checked to MimeType and not text type..");
        }

        return stringBuffer.toString();
    }

    /**
     * 파일의 종류를 판단
     * @return MimeType을 반환 합니다.
     * */
    public static MimeType getMimeType(Context context ,File file){
        Uri uri = Uri.fromFile(file);
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));

        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        MimeType mimeType = new MimeType();
        mimeType.setMimetypeString(type);

        return mimeType;
    }

    /**
     * MIME TYPE을 정의합니다.
     * <p>
     *     타입의 종류는 APPLICATION , AUDIO , IMAGE , TEXT , VIDEO , MULTIPART, NONE; 입니다 <br>
     *     추가가 필요시 enum부분에 추가후 소스코드 수정이 필요함
     * </p>
     * */
    public static class MimeType{

        private TYPE mimetype;
        private String mimetypeString;

        public TYPE getMimetype() {
            return mimetype;
        }

        public String getMimetypeString() {
            return mimetypeString;
        }

        public void setMimetypeString(String mimetypeString) {
            this.mimetypeString = mimetypeString;
            mimetype = convertToType(mimetypeString);

        }

        private TYPE convertToType(String mimetypeString){
            if(mimetypeString.startsWith("application")){
                return TYPE.APPLICATION;
            }else if(mimetypeString.startsWith("audio")){
                return TYPE.AUDIO;
            }else if(mimetypeString.startsWith("image")){
                return TYPE.IMAGE;
            }else if(mimetypeString.startsWith("text")){
                return TYPE.TEXT;
            }else if(mimetypeString.startsWith("video")){
                return TYPE.VIDEO;
            }else if(mimetypeString.startsWith("multipart")){
                return TYPE.MULTIPART;
            }
            return TYPE.NONE;
        }

        public enum TYPE {
            APPLICATION , AUDIO , IMAGE , TEXT , VIDEO , MULTIPART, NONE;
        }
    }
    /**
    MIME-Type	Description	File Extension
    application/acad	AutoCAD drawing files	dwg
    application/clariscad	ClarisCAD files	ccad
    application/dxf	DXF (AutoCAD)	dxf
    application/msaccess	Microsoft Access file	mdb
    application/msword	Microsoft Word file	doc
    application/octet-stream	Uninterpreted binary	bin
    application/pdf	PDF (Adobe Acrobat)	pdf
    application/postscript	Postscript, encapsulated Postscript,	ai, ps, eps
    Adobe Illustrator
    application/rtf	Rich Text Format file	rtf rtf
    application/vnd.ms-excel	Microsoft Excel file	xls
    application/vnd.ms-powerpoint	Microsoft PowerPoint file	ppt
    application/x-cdf	Channel Definition Format file	cdf
    application/x-csh	C-shell script	csh csh
    application/x-dvi	TeX	dvi dvi dvi
    application/x-javascript	Javascript source file	js
    application/x-latex	LaTeX source file	latex
    application/x-mif	FrameMaker MIF format	mif
    application/x-msexcel	Microsoft Excel file	xls
    application/x-mspowerpoint	Microsoft PowerPoint file	ppt
    application/x-tcl	TCL script	tcl
    application/x-tex	TeX source file	tex
    application/x-texinfo	Texinfo (emacs)	texinfo, texi
    application/x-troff	troff file	t, tr, roff t, tr, roff
    application/x-troff-man	troff with MAN macros	man
    application/x-troff-me	troff with ME macros	me
    application/x-troff-ms	troff with MS macros	ms
    application/x-wais-source	WAIS source file	src
    application/zip	ZIP archive	zip
    audio/basic	Basic audio (usually m-law)	au, snd
    audio/x-aiff	AIFF audio	aif, aiff, aifc
    audio/x-wav	Windows WAVE audio	wav
    image/gif	GIF image	gif
    image/ief	Image Exchange Format file	ief
    image/jpeg	JPEG image	jpeg, jpg jpe
    image/tiff	TIFF image	tiff, tif
    image/x-cmu-raster	CMU Raster image	ras
    image/x-portable-anymap	PBM Anymap image format	pnm
    image/x-portable-bitmap	PBM Bitmap image format	pbm
    image/x-portable-graymap	PBM Graymap image format	pgm
    image/x-portable-pixmap	PBM Pixmap image format	ppm
    image/x-rgb	RGB image format	rgb
    image/x-xbitmap	X Bitmap image	xbm
    image/x-xpixmap	X Pixmap image	xpm
    image/x-xwindowdump	X Windows Dump (xwd)	xwd
    multipart/x-gzip	GNU ZIP archive	gzip
    multipart/x-zip	PKZIP archive	zip
    text/css	Cascading style sheet	css
    text/html	HTML file	html, htm
    text/plain	Plain text	txt
    text/richtext	MIME Rich Text	rtx
    text/tab-separated- values	Text with tab-separated values	tsv
    text/xml	XML document	xml
    text/x-setext	Struct-Enhanced text	etx
    text/xsl	XSL style sheet	xsl
    video/mpeg	MPEG video	mpeg, mpg, mpe
    video/quicktime	QuickTime video	qt, mov
    video/x-msvideo	Microsoft Windows video	avi
    video/x-sgi-movie	SGI movie player format	movie
     */
}
