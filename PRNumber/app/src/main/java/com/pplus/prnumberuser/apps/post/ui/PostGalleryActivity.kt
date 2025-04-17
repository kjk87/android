package com.pplus.prnumberuser.apps.post.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.PPlusPermission
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.post.data.GalleryGridAdapter
import com.pplus.prnumberuser.core.network.model.dto.GalleryData
import com.pplus.prnumberuser.core.util.ImageUtils
import com.pplus.prnumberuser.databinding.ActivityPostGalleryBinding
import com.pplus.utils.part.apps.permission.Permission
import com.pplus.utils.part.apps.permission.PermissionListener
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView.TransformImageListener
import com.yalantis.ucrop.view.UCropView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

class PostGalleryActivity : BaseActivity() {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityPostGalleryBinding

    override fun getLayoutView(): View {
        binding = ActivityPostGalleryBinding.inflate(layoutInflater)
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
        binding.recyclerPostGallery.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_3)))
        binding.recyclerPostGallery.layoutManager = layoutManager
        mAdapter = GalleryGridAdapter(this)
        binding.recyclerPostGallery.adapter = mAdapter
        mAdapter!!.setMaxCount(mMaxCount)
        binding.layoutPostGalleryCrop.layoutParams.height = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * (3f / 4f)).toInt()
        mUCropViewList = ArrayList()
        mViewList = ArrayList()

        binding.viewPostGalleryBlocking.isClickable = true

        binding.textGalleryCancel.setOnClickListener {
            onBackPressed()
        }

        binding.textGalleryNext.setOnClickListener {
            if (mUCropViewList!!.size > 0) {
                crop(0)
            }
        }

        binding.imagePostGalleryCamera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val pPlusPermission = PPlusPermission(this)
                pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
                pPlusPermission.addPermission(Permission.PERMISSION_KEY.CAMERA)
                pPlusPermission.setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        goCamera()
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        LogUtil.e(LOG_TAG, "onPermissionDenied")
                        val intent = pPlusPermission.settingIntent
                        startActivity(intent)
                    }
                })
                pPlusPermission.checkPermission()
            } else {
                goCamera()
            }
        }

        val pPlusPermission = PPlusPermission(this)
        pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
        pPlusPermission.setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                galleryTask()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {}
        })
        pPlusPermission.checkPermission()
    }

    val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (StringUtils.isNotEmpty(mCurrentPhotoPath)) {
                mIsCamera = true
                galleryAddPic(Uri.fromFile(File(mCurrentPhotoPath)))
                val galleryData = GalleryData()
                galleryData.checked = true
                //                    galleryData.imageUrl = mCurrentPhotoPath
                galleryData.imageUri = File(mCurrentPhotoPath).toUri()
                mAdapter!!.add(galleryData)
            }
        }
    }

    val imageEditLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, data.getParcelableArrayListExtra(Const.CROPPED_IMAGE))
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }


    private var mCurrentPhotoPath: String? = null
    fun goCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    ImageUtils.setUpPhotoFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }

                mCurrentPhotoPath = photoFile?.absolutePath
                LogUtil.e(LOG_TAG, "mCurrentPhotoPath : "+mCurrentPhotoPath)

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
            val names = data.imageUri!!.path!!.split("/".toRegex()).toTypedArray()
            val fileName = names[names.size - 1]
            LogUtil.e(LOG_TAG, fileName)
            val albumF = filesDir
            val outputUri = Uri.fromFile(File(albumF, "temp_$fileName"))
            gestureCropImageView.setImageUri(data.imageUri!!, outputUri)
        } catch (e: Exception) {
        }
    }

    var transformImageListener: TransformImageListener = object : TransformImageListener {
        override fun onLoadComplete() {
            LogUtil.e("crop", "onLoadComplete")
            mVisiblePosition = mUCropViewList!!.size - 1
            mUCropViewList!![mVisiblePosition].animate().alpha(1f).setDuration(300).interpolator = AccelerateInterpolator()
            binding.viewPostGalleryBlocking.isClickable = false
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
            override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                mCroppedImageList!!.add(resultUri)
                if (position < mUCropViewList!!.size - 1) {
                    crop(position + 1)
                } else {
                    hideProgress()
                    val intent = Intent(this@PostGalleryActivity, PostImageEditActivity::class.java)
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

    private fun galleryAddPic(uri: Uri) {
        val mediaScanIntent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE") //        File f = new File(uri.getPath());
        //        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.data = uri
        this.sendBroadcast(mediaScanIntent)
    }

    fun galleryTask(){
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            val columns = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN
            val imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC")
            val infos = ArrayList<GalleryData>()
            var galleryData: GalleryData? = null
            if (imageCursor != null && imageCursor.moveToFirst()) {
                do {
                    galleryData = GalleryData()
                    val idColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val orientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
                    val mimeTypeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                    val sizeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                    galleryData.id = imageCursor.getLong(idColumnIndex)
                    //                    galleryData.imageUrl = imageCursor.getString(dataColumnIndex)
                    galleryData.imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, galleryData.id.toString())
                    galleryData.orientation = imageCursor.getInt(orientationColumnIndex)
                    try {
                        val type = imageCursor.getString(dataColumnIndex).substring(imageCursor.getString(dataColumnIndex).lastIndexOf(".") + 1)
                        galleryData.imageType = type
                    } catch (e: Exception) {
                        LogUtil.e(LOG_TAG, e.toString())
                    }
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

    internal inner class GalleryListSync : AsyncTask<Void?, Void?, ArrayList<GalleryData>>() {
        override fun onPreExecute() {
            super.onPreExecute() //            showProgress("gallery prg");
        }

        override fun doInBackground(vararg p0: Void?): ArrayList<GalleryData> {
            val columns = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN
            val imageCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC")
            val infos = ArrayList<GalleryData>()
            var galleryData: GalleryData? = null
            if (imageCursor != null && imageCursor.moveToFirst()) {
                do {
                    galleryData = GalleryData()
                    val idColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val orientationColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
                    val mimeTypeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                    val sizeIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                    galleryData.id = imageCursor.getLong(idColumnIndex)
//                    galleryData.imageUrl = imageCursor.getString(dataColumnIndex)
                    galleryData.imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, galleryData.id.toString())
                    galleryData.orientation = imageCursor.getInt(orientationColumnIndex)
                    try {
                        val type = imageCursor.getString(dataColumnIndex).substring(imageCursor.getString(dataColumnIndex).lastIndexOf(".") + 1)
                        galleryData.imageType = type
                    } catch (e: Exception) {
                        LogUtil.e(LOG_TAG, e.toString())
                    }
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
            return infos
        }

        override fun onPostExecute(result: ArrayList<GalleryData>) {
            super.onPostExecute(result)
            hideProgress()
            mAdapter!!.clear()
            mAdapter!!.addAll(result)
        }
    }

    internal inner class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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