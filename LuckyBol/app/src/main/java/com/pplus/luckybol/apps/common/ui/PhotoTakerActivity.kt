package com.pplus.luckybol.apps.common.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.util.ImageFilePath
import com.pplus.luckybol.core.util.ImageUtils
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.deleteFromMediaScanner
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.getAlbumDir
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import java.io.*

class PhotoTakerActivity : AppCompatActivity() {
    @JvmField
    var LOG_TAG = this.javaClass.simpleName
    private val return_data = true
    private val fix_ratio = false
    private var crop = false
    private val faceDetection = true
    private var mDirectory: File? = null

    //    private OnNotFoundCropIntentListener mNotFoundCropIntentListener;
    private var mCropUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(FILE_PATH)
            if (!StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                setResultData() //                galleryAddPic();
                //                File f = new File(mCurrentPhotoPath);
                //                Uri contentUri = Uri.fromFile(f);
                //                doCropImage(contentUri);
            }
            return
        }
        val mode = intent.getStringExtra("mode")
        crop = intent.getBooleanExtra("crop", false)
        mDirectory = getAlbumDir()
        if (mode == "camera") {
            doImageCapture()
        } else if (mode == "picture") {
            doPickImage()
        } else {
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(FILE_PATH, mCurrentPhotoPath)
        super.onSaveInstanceState(outState)
        LogUtil.e("yklee", "PhotoTakerActivity onSaveInstanceState")
    }

    private fun setResultData() {
        LogUtil.e("path", mCurrentPhotoPath)
        galleryAddPic()
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        val intent = Intent()
        intent.data = contentUri
        intent.putExtra(Const.PATH, mCurrentPhotoPath)
        setResult(RESULT_OK, intent)
        finish()
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

    private fun getBitmap(uri: Uri): Bitmap? {
        val `in`: InputStream? = null
        try {
            return MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } catch (e: Exception) {
            LogUtil.e("Cropper", "file $uri not found")
        }
        return null
    }

    private fun writeBitmapToFile(bitmap: Bitmap?, file: File): Boolean {
        return try {
            if (bitmap != null) {
                var fops: FileOutputStream? = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fops)
                fops!!.flush()
                fops.close()
                fops = null
                true
            } else {
                false
            }
        } catch (e: FileNotFoundException) {
            LogUtil.e(LOG_TAG, e)
            false
        } catch (e: IOException) {
            LogUtil.e(LOG_TAG, e)
            false
        }
    }

    fun doCropImage(uri: Uri?) {

        //        if(!uri.getScheme().trim().equalsIgnoreCase("file")) {
        //            //파일이 아닌경우 편집이 안됨
        //            return false;
        //        }
        val intent = Intent(this, CropActivity::class.java) //편집 후 출력경로
        intent.type = "image/*" //원본데이터
        intent.data = uri
        intent.putExtra("return-data", true)
        cropImageLauncher.launch(intent)
    }

    val cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            galleryAddPic()
            setResult(RESULT_OK, result.data)
            finish()
        } else {
            cancelResult()
        }

    }

    private var mCurrentPhotoPath: String? = null
    fun doImageCapture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var f: File? = null
        try {
            f = ImageUtils.setUpPhotoFile()
            mCurrentPhotoPath = f.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), f))
            cameraLauncher.launch(intent)
        } catch (e: IOException) {
            LogUtil.e(LOG_TAG, e)
            f = null
            mCurrentPhotoPath = null
        }
    }

    val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (mCurrentPhotoPath != null) {
                galleryAddPic()
                val f = File(mCurrentPhotoPath)
                val contentUri = Uri.fromFile(f)
                if (crop) {
                    doCropImage(contentUri)
                } else {
                    setResultData()
                }
            } else {
                cancelResult()
            }
        }
    }

    private fun galleryAddPic() {
        if (mCurrentPhotoPath != null) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(mCurrentPhotoPath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            this.sendBroadcast(mediaScanIntent)
        }
    }

    fun getRealImagePath(uri: Uri?): String? {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri!!, proj, null, null, null)
        val index = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        return if (cursor.moveToFirst()) {
            val path = cursor.getString(index)
            cursor.close()
            path
        } else {
            cursor.close()
            null
        }
    }

    fun doPickImage(): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            doPickImageLauncher.launch(intent)
            true
        } catch (e: Exception) {
            LogUtil.e("doPickImage", e.toString())
            false
        }
    }

    val doPickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val dataUri = result.data!!.data
            if (dataUri != null) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                mCurrentPhotoPath = ImageFilePath.getPath(this, dataUri)
                LogUtil.e("PHOTOTAKER", mCurrentPhotoPath)
                if (crop) {
                    doCropImage(Uri.fromFile(File(mCurrentPhotoPath)))
                } else {
                    setResultData()
                }
            }
        } else {
            cancelResult()
        }

    }

    private fun cancelResult() {
        if (StringUtils.isNotEmpty(mCurrentPhotoPath)) {
            deleteFromMediaScanner(mCurrentPhotoPath!!)
        }
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun getFile(name: String): File {
        val output = File(name)
        if (!output.exists()) {
            try {
                output.createNewFile()
            } catch (e: IOException) {
                LogUtil.e(LOG_TAG, e)
            }
        }
        return output
    }

    companion object {
        private const val FILE_PATH = "file_path"
    }
}