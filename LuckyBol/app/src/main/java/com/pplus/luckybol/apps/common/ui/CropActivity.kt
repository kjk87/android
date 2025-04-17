package com.pplus.luckybol.apps.common.ui

import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.component.CropImageView
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.util.ImageUtils
import com.pplus.luckybol.core.util.imageresizer.ImageResizer
import com.pplus.luckybol.core.util.imageresizer.operations.ImageRotation
import com.pplus.luckybol.core.util.imageresizer.operations.ResizeMode
import com.pplus.luckybol.core.util.imageresizer.utils.ImageWriter
import com.pplus.luckybol.databinding.ActivityCropBinding
import com.pplus.utils.part.logs.LogUtil
import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * Created by dev on 2015-12-28.
 */
class CropActivity : BaseActivity(), View.OnClickListener, ImplToolbar {
    private var mSaveUri: Uri? = null
    private var mCroppedBitmap: Bitmap? = null
    private var mKey: String? = null
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityCropBinding

    override fun getLayoutView(): View {
        binding = ActivityCropBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_image_edit), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mSaveUri = intent.data
        mCroppedBitmap = bitmap

        mKey = intent.getStringExtra(Const.KEY)

        binding.cropImage.setHandleSizeInDp(10)
        binding.cropImage.setTouchPaddingInDp(8)
        binding.cropImage.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH)

        //        if(StringUtils.isNotEmpty(mKey) && mKey.equals(Const.REPRESENT)){
        //            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        //            rotate_layout.setVisibility(View.GONE);
        //            crop_fix_btn_rotation.setVisibility(View.VISIBLE);
        //        }else{
        //            cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        //            rotate_layout.setVisibility(View.VISIBLE);
        //            crop_fix_btn_rotation.setVisibility(View.GONE);
        //        }
        binding.cropImage.setCropMode(CropImageView.CropMode.RATIO_9_16)
        binding.cropRotateLayout.setVisibility(View.GONE)
        binding.cropFixBtnRotation.setVisibility(View.VISIBLE)
        try {
            binding.cropImage.post { binding.cropImage.imageBitmap = mCroppedBitmap }
        } catch (e: Exception) {
            LogUtil.e(LOG_TAG, e)
        }
        binding.cropFixBtnRotation.setOnClickListener(this)
        findViewById<View>(R.id.crop_btn_bottom_text).setOnClickListener(this)
        findViewById<View>(R.id.crop_btn_rotation).setOnClickListener(this)
        findViewById<View>(R.id.crop_btn_free).setOnClickListener(this)
        findViewById<View>(R.id.crop_btn_1_1).setOnClickListener(this)
        findViewById<View>(R.id.btn_crop_4_3).setOnClickListener(this)
        findViewById<View>(R.id.crop_btn_3_4).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.crop_btn_bottom_text -> {
                mCroppedBitmap = binding.cropImage.croppedBitmap
                onSaveClicked()
            }
            R.id.crop_fix_btn_rotation, R.id.crop_btn_rotation -> {
                var current_rotate_degrees = CropImageView.RotateDegrees.ROATATE_NORMAL
                current_rotate_degrees = if (current_rotate_degrees == CropImageView.RotateDegrees.ROATATE_NORMAL) {
                    CropImageView.RotateDegrees.ROTATE_90D
                } else if (current_rotate_degrees == CropImageView.RotateDegrees.ROTATE_90D) {
                    CropImageView.RotateDegrees.ROTATE_180D
                } else if (current_rotate_degrees == CropImageView.RotateDegrees.ROTATE_180D) {
                    CropImageView.RotateDegrees.ROTATE_270D
                } else {
                    CropImageView.RotateDegrees.ROATATE_NORMAL
                }
                binding.cropImage.rotateImage(current_rotate_degrees)
            }
            R.id.crop_btn_free -> binding.cropImage.setCropMode(CropImageView.CropMode.RATIO_FREE)
            R.id.crop_btn_1_1 -> binding.cropImage.setCropMode(CropImageView.CropMode.RATIO_1_1)
            R.id.btn_crop_4_3 -> binding.cropImage.setCropMode(CropImageView.CropMode.RATIO_4_3)
            R.id.crop_btn_3_4 -> binding.cropImage.setCropMode(CropImageView.CropMode.RATIO_3_4)
        }
    }

    var mSaving // Whether the "save" button is already clicked.
            = false

    private fun onSaveClicked() {
        if (mSaving) return
        mSaving = true

        // Return the cropped image directly or save it to the specified URI.
        val myExtras = intent.extras
        if (myExtras != null && (myExtras.getParcelable<Parcelable?>("data") != null || myExtras.getBoolean("return-data"))) {
            val output: File
            var currentPhotoPath: String? = ""
            try {
                output = ImageUtils.setUpPhotoFile(File(mSaveUri!!.path))
                currentPhotoPath = output.absolutePath
            } catch (e: Exception) {
                LogUtil.e("doPickImage", e.toString())
                return
            }
            val writed = writeBitmapToFile(mCroppedBitmap, output)
            val intent = Intent()
            if (writed) intent.data = Uri.fromFile(output)

            //			Bundle extras = new Bundle();
            //			extras.putParcelable("data", croppedImage);
            //			intent.putExtras(extrx`as);
            setResult(RESULT_OK, intent)
            finish()
        } //        else {
        //            final Bitmap b = mCroppedBitmap;
        //            saveOutput(b);
        //        }
    }

    private fun saveOutput(croppedImage: Bitmap) {
        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = contentResolver.openOutputStream(mSaveUri!!)
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            } catch (ex: IOException) { // TODO: report error to caller
                Log.e("Cropper", "Cannot open file: $mSaveUri", ex)
            } finally {
                finish()
            }
            val extras = Bundle()
            setResult(RESULT_OK, Intent(mSaveUri.toString()).putExtras(extras))
        } else {
            Log.e("Cropper", "not defined image url")
        }
        croppedImage.recycle()
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

    private fun writeBitmapToFile(bitmap: Bitmap?, file: File): Boolean {
        return ImageWriter.writeToFile(bitmap, file)
    }

    val bitmap: Bitmap?
        get() {
            val originFile = File(mSaveUri!!.path)
            val tempFile: File
            var imageRotation: ImageRotation? = null
            try {
                val exif = ExifInterface(mSaveUri!!.path!!)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> imageRotation = ImageRotation.CW_90
                    ExifInterface.ORIENTATION_ROTATE_180 -> imageRotation = ImageRotation.CW_180
                    ExifInterface.ORIENTATION_ROTATE_270 -> imageRotation = ImageRotation.CW_270
                }
            } catch (e: IOException) {
                LogUtil.e(LOG_TAG, e)
            }
            tempFile = File(cacheDir, originFile.name)
            if (!tempFile.exists()) {
                try {
                    tempFile.createNewFile()
                } catch (e: IOException) {
                    LogUtil.e(LOG_TAG, e)
                }
            }
            var bitmap: Bitmap? = null
            try {
                bitmap = if (imageRotation != null) {
                    when (imageRotation) {
                        ImageRotation.CW_90, ImageRotation.CW_180 -> ImageResizer.resize(originFile, 1280, 720, ResizeMode.FIT_TO_HEIGHT)
                        else -> ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH)
                    }
                } else {
                    ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH)
                }
                if (imageRotation != null) {
                    ImageResizer.saveToFile(bitmap, tempFile)
                    bitmap = ImageResizer.rotate(tempFile, imageRotation)
                }
                return bitmap
            } catch (error: OutOfMemoryError) {
                LogUtil.e(LOG_TAG, "OutOfMemoryError = {}", error)
            } catch (e: Exception) {
                LogUtil.e(LOG_TAG, "Exception = {}", e)
            } finally {
                LogUtil.e(LOG_TAG, "finally")
                if (tempFile != null) {
                    if (tempFile.exists()) {
                        tempFile.delete()
                        deleteFromMediaScanner(tempFile.absolutePath)
                    }
                }
            }
            return null
        }

    private fun deleteFromMediaScanner(filePath: String): Boolean {
        var filePath: String? = filePath
        val fileUri = Uri.parse(filePath)
        filePath = fileUri.path
        val c = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='$filePath'", null, null)
        if (c!!.moveToFirst()) {
            val id = c.getInt(0)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
            return contentResolver.delete(uri, null, null) == 1
        }
        return false
    }
}