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
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.GalleryData
import com.root37.buflexz.core.util.ImageUtils
import com.root37.buflexz.core.util.ToastUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.apps.gallery.data.GalleryGridAdapter
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityGalleryBinding
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView.TransformImageListener
import com.yalantis.ucrop.view.UCropView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class GalleryActivity : BaseActivity() {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityGalleryBinding

    override fun getLayoutView(): View {
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdapter: GalleryGridAdapter? = null
    private var mUCropViewList: MutableList<UCropView>? = null
    private var mViewList: MutableList<View>? = null
    private var mCroppedImageList: ArrayList<Uri>? = null
    private var mVisiblePosition = -1
    private var mIsCamera = false
    private var mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT
    override fun initializeView(savedInstanceState: Bundle?) {
        mMaxCount = intent.getIntExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT)
        val layoutManager = GridLayoutManager(this, 4)
        binding.recyclerGallery.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_3)))
        binding.recyclerGallery.layoutManager = layoutManager
        mAdapter = GalleryGridAdapter()
        binding.recyclerGallery.adapter = mAdapter
        mAdapter!!.setMaxCount(mMaxCount)
        binding.layoutGalleryCrop.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f)).toInt()
        mUCropViewList = ArrayList()
        mViewList = ArrayList()

        binding.viewGalleryBlocking.isClickable = true

        binding.textGalleryCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.textGalleryNext.setOnClickListener {
            if (mUCropViewList!!.size > 0) {
                crop(0)
            }
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

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.forEach {
            if (!it.value) {
                ToastUtil.show(this, getString(R.string.msg_failed_granted_permission))
                return@registerForActivityResult
            }
        }

        goCamera()
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

    val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                mIsCamera = true
                val galleryData = GalleryData()
                galleryData.checked = true //                    galleryData.imageUrl = mCurrentPhotoPath
                galleryData.imageUri = File(mCurrentPhotoPath).toUri()
                galleryData.isCamera = true
                mAdapter!!.add(galleryData)
            }
        }
    }

    val imageEditLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, PplusCommonUtil.getParcelableArrayListExtra(data, Const.CROPPED_IMAGE, Uri::class.java))
                setResult(RESULT_OK, data)
                finish()
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
                    val photoURI: Uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    fun changeImage(position: Int) {

        //        mUCropViewList.get(mVisiblePosition).animate().alpha(0);
        mVisiblePosition = position
        mUCropViewList!![position].animate().alpha(1f).setDuration(300).interpolator = AccelerateInterpolator()
        binding.layoutPostCrop.bringChildToFront(mViewList!![mVisiblePosition])
    }

    fun removeImage(position: Int) {
        binding.layoutPostCrop.removeView(mViewList!![position])
        mUCropViewList!!.removeAt(position)
        mViewList!!.removeAt(position)
        mVisiblePosition = mUCropViewList!!.size - 1
        if (mVisiblePosition >= 0) {
            binding.layoutPostCrop.bringChildToFront(mViewList!![mVisiblePosition])
            mUCropViewList!![mVisiblePosition].animate().alpha(1f).setDuration(300).interpolator = AccelerateInterpolator()
        }
    }

    fun addImage(data: GalleryData) {
        try {
            mAdapter!!.isBlocking = true

            //            if(mUCropViewList.size() > 0) {
            //                mUCropViewList.get(mVisiblePosition).animate().alpha(0);
            //            }
            val view = layoutInflater.inflate(R.layout.layout_ucrop, RelativeLayout(this))
            val uCropView = view.findViewById<View>(R.id.ucrop_view) as UCropView
            val gestureCropImageView = uCropView.cropImageView
            gestureCropImageView.setBackgroundColor(ResourceUtil.getColor(this, R.color.black))
            val mOverlayView = uCropView.overlayView
            gestureCropImageView.isScaleEnabled = true
            gestureCropImageView.isRotateEnabled = false
            gestureCropImageView.targetAspectRatio = 4f / 3f
            view.findViewById<View>(R.id.image_rotate).setOnClickListener { gestureCropImageView.postRotate(90f) }
            mUCropViewList!!.add(uCropView)
            mViewList!!.add(view)
            binding.layoutPostCrop.addView(view)
            gestureCropImageView.setTransformImageListener(transformImageListener)

            if (data.isCamera != null && data.isCamera!!) {
                gestureCropImageView.setImageUri(data.imageUri!!, data.imageUri)
            } else {
                val extensions = contentResolver.getType(data.imageUri!!)!!.split("/".toRegex()).toTypedArray()
                val extension = extensions[extensions.size - 1]

                val names = data.imageUri!!.path!!.split("/".toRegex()).toTypedArray()
                val fileName = names[names.size - 1]
                LogUtil.e(LOG_TAG, fileName)
                val albumF = filesDir
                val outputUri = Uri.fromFile(File(albumF, "temp_$fileName.$extension"))
                gestureCropImageView.setImageUri(data.imageUri!!, outputUri)
            }


        } catch (e: Exception) {
            LogUtil.e(LOG_TAG, e.toString())
        }
    }

    var transformImageListener: TransformImageListener = object : TransformImageListener {
        override fun onLoadComplete() {
            LogUtil.e("crop", "onLoadComplete")
            mVisiblePosition = mUCropViewList!!.size - 1
            mUCropViewList!![mVisiblePosition].animate().alpha(1f).setDuration(300).interpolator = AccelerateInterpolator()
            binding.viewGalleryBlocking.isClickable = false
            mAdapter!!.isBlocking = false
        }

        override fun onLoadFailure(e: Exception) {}
        override fun onRotate(currentAngle: Float) {}
        override fun onScale(currentScale: Float) {}
    }

    private fun crop(position: Int) {
        showProgress(getString(R.string.msg_editing_image))
        if (position == 0) {
            mCroppedImageList = ArrayList()
        }
        mUCropViewList!![position].cropImageView.cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, object : BitmapCropCallback {
            override fun onBitmapCropped(resultUri: Uri,
                                         offsetX: Int,
                                         offsetY: Int,
                                         imageWidth: Int,
                                         imageHeight: Int) {
                mCroppedImageList!!.add(resultUri)
                if (position < mUCropViewList!!.size - 1) {
                    crop(position + 1)
                } else {
                    hideProgress()
                    val intent = Intent(this@GalleryActivity, ImageEditActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList)
                    imageEditLauncher.launch(intent)
                }
            }

            override fun onCropFailure(t: Throwable) {
                hideProgress()
            }
        })
    }

    fun galleryTask() {
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            val columns = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE)
            val orderBy = MediaStore.Images.Media.DATE_ADDED
            val imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC")
            val infos = ArrayList<GalleryData>()
            var galleryData: GalleryData? = null
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
                    val imagePath = File(galleryData.imageUri!!.path).absolutePath
                    val folderList = imagePath.split("/".toRegex()).toTypedArray()
                    galleryData.folder = folderList[folderList.size - 2]
                    if (mAdapter!!.selectGalleryList.contains(galleryData)) {
                        galleryData.checked = true
                    }
                    if (mIsCamera && imagePath.contains(mCurrentPhotoPath!!)) {
                        mIsCamera = false
                        galleryData.checked = true
                    }
                    infos.add(galleryData)
                } while (imageCursor.moveToNext())
                imageCursor.close()
            }

            CoroutineScope(Dispatchers.Main + job).launch {
                hideProgress()
                mAdapter!!.clear()
                mAdapter!!.addAll(infos)
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