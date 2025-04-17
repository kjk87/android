package com.root37.buflexz.apps.gallery.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.GalleryData
import com.root37.buflexz.core.util.ImageUtils
import com.root37.buflexz.core.util.PplusCommonUtil.Companion.getAlbumDir
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivitySelectOneBinding
import com.root37.buflexz.databinding.LayoutUcropBinding
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.apps.gallery.data.GalleryOneAdapter
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.GestureCropImageView
import com.yalantis.ucrop.view.TransformImageView.TransformImageListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OneGalleryActivity() : BaseActivity() {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivitySelectOneBinding

    override fun getLayoutView(): View {
        binding = ActivitySelectOneBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mGestureCropImageView: GestureCropImageView? = null
    private var mOneAdapter: GalleryOneAdapter? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        val layoutManager = GridLayoutManager(this, 4)
        binding.recyclerSelectGallery.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_3)))
        binding.recyclerSelectGallery.layoutManager = layoutManager
        mOneAdapter = GalleryOneAdapter(this)
        binding.recyclerSelectGallery.adapter = mOneAdapter

        binding.layoutGalleryCrop.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f)).toInt()

        val uCropBinding = LayoutUcropBinding.inflate(layoutInflater)
        val uCropView = uCropBinding.ucropView
        mGestureCropImageView = uCropView.cropImageView
        mGestureCropImageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        mGestureCropImageView!!.setBackgroundColor(ResourceUtil.getColor(this, R.color.black))
        val mOverlayView = uCropView.overlayView
        mOverlayView.layoutParams.width = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS
        mGestureCropImageView!!.isScaleEnabled = true
        mGestureCropImageView!!.isRotateEnabled = false
        mGestureCropImageView!!.targetAspectRatio = 1f


        uCropBinding.imageRotate.setOnClickListener {
            mGestureCropImageView!!.postRotate(90f)
        }
        binding.layoutCrop.addView(uCropBinding.root)
        binding.viewCropBlocking.isClickable = true
        mGestureCropImageView!!.setTransformImageListener(object : TransformImageListener {
            override fun onLoadComplete() {
                LogUtil.e("crop", "onLoadComplete")
                binding.viewCropBlocking.isClickable = false
                uCropView.animate().alpha(1f).setDuration(300).interpolator = AccelerateInterpolator()
            }

            override fun onLoadFailure(e: Exception) {
                LogUtil.e("crop", "onLoadFailure")
            }

            override fun onRotate(currentAngle: Float) {}
            override fun onScale(currentScale: Float) {}
        })
        binding.textGalleryCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.textGalleryComplete.setOnClickListener {
            showProgress(getString(R.string.msg_editing_image))
            mGestureCropImageView!!.cropAndSaveImage(Bitmap.CompressFormat.PNG, 90, object : BitmapCropCallback {
                override fun onBitmapCropped(resultUri: Uri,
                                             offsetX: Int,
                                             offsetY: Int,
                                             imageWidth: Int,
                                             imageHeight: Int) {
                    val data = Intent()
                    data.data = resultUri
                    setResult(RESULT_OK, data)
                    finish()
                    hideProgress()
                }

                override fun onCropFailure(t: Throwable) {
                    LogUtil.e(LOG_TAG, t.toString())
                    hideProgress()
                }
            })
        }

        binding.imageGalleryCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                cameraPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            } else {
                goCamera()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)

            // 권한이 열려있는지 확인
            if (permission == PackageManager.PERMISSION_DENIED) {
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            } else {
                galleryTask()
            }
        }else{
            val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

            // 권한이 열려있는지 확인
            if (permission == PackageManager.PERMISSION_DENIED) {
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            } else {
                galleryTask()
            }
        }


    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.forEach {
            if (!it.value) {
                ToastUtil.show(this, getString(R.string.msg_failed_granted_permission))
                finish()
            }
        }

        galleryTask()
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.forEach {
            if (!it.value) {
                ToastUtil.show(this, getString(R.string.msg_failed_granted_permission))
                return@registerForActivityResult
            }
        }

        goCamera()
    }

    val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                val galleryData = GalleryData()
                galleryData.checked = true //                    galleryData.imageUrl = mCurrentPhotoPath
                galleryData.imageUri = File(mCurrentPhotoPath).toUri()
                galleryData.isCamera = true
                mOneAdapter!!.add(galleryData)
                setSelect(galleryData)
            }
        }
    }

    private var mCurrentPhotoPath: String? = null
    fun goCamera() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent -> // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also { // Create the File where the photo should go
                val photoFile: File? = try {
                    ImageUtils.setUpPhotoFile()
                } catch (ex: IOException) { // Error occurred while creating the File
                    null
                }

                mCurrentPhotoPath = photoFile?.absolutePath
                LogUtil.e(LOG_TAG, "mCurrentPhotoPath : " + mCurrentPhotoPath)

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    fun setSelect(data: GalleryData) {
        try {
            LogUtil.e(LOG_TAG, data.imageUri!!.path!!)
            if (data.isCamera != null && data.isCamera!!) {
                mGestureCropImageView!!.setImageUri(data.imageUri!!, data.imageUri)
            } else {
                val extensions = contentResolver.getType(data.imageUri!!)!!.split("/".toRegex()).toTypedArray()
                val extension = extensions[extensions.size - 1]
                val names = data.imageUri!!.path!!.split("/".toRegex()).toTypedArray()
                val fileName = names[names.size - 1]
                LogUtil.e(LOG_TAG, fileName)
                val albumF = getAlbumDir()
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val outputUri = Uri.fromFile(File(albumF, "temp_" + timeStamp + "_" + fileName + "." + extension))
                LogUtil.e(LOG_TAG, outputUri.path)
                mGestureCropImageView!!.setImageUri(data.imageUri!!, outputUri)
            }
        } catch (e: Exception) {
            LogUtil.e(LOG_TAG, e.toString())
        }
    }

    fun galleryTask() {
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            val columns = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE)
            val orderBy = MediaStore.Images.Media.DATE_ADDED
            val imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC")
            var galleryData: GalleryData? = null
            val infos = ArrayList<GalleryData>()
            if (imageCursor != null && imageCursor.moveToFirst()) {
                do {
                    galleryData = GalleryData()
                    val idColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID) //                    val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val orientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
                    val mimeTypeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                    val sizeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                    galleryData.id = imageCursor.getLong(idColumnIndex) //                    galleryData.imageUrl = imageCursor.getString(dataColumnIndex)
                    galleryData.imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, galleryData.id.toString())
                    galleryData.orientation = imageCursor.getInt(orientationColumnIndex)
                    galleryData.mimeType = imageCursor.getString(mimeTypeIndex)
                    galleryData.size = imageCursor.getInt(sizeIndex)

                    val folderList = File(galleryData.imageUri!!.path).absolutePath.split("/".toRegex()).toTypedArray()
                    galleryData.folder = folderList.get(folderList.size - 2)
                    infos.add(galleryData)
                } while (imageCursor.moveToNext())
                imageCursor.close()
            }

            CoroutineScope(Dispatchers.Main + job).launch {
                hideProgress()
                mOneAdapter!!.clear()
                mOneAdapter!!.addAll(infos)
            }
        }
    }

    internal inner class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {
            val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (itemPosition % 4 == 3) {
                outRect.right = 0
            } else {
                outRect.right = space
            }
            outRect.bottom = space // Add top margin only for the first item to avoid double space between items
        }
    }
}