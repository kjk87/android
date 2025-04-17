package com.lejel.wowbox.apps.gallery.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.gallery.data.GalleryOnlyAdapter
import com.lejel.wowbox.core.network.model.dto.GalleryData
import com.lejel.wowbox.core.util.ImageUtils
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityGalleryOnlyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class GalleryOnlyActivity : BaseActivity() {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityGalleryOnlyBinding

    override fun getLayoutView(): View {
        binding = ActivityGalleryOnlyBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdapter: GalleryOnlyAdapter? = null
    private var mIsCamera = false
    private var mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT
    override fun initializeView(savedInstanceState: Bundle?) {
        mMaxCount = intent.getIntExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT)
        val layoutManager = GridLayoutManager(this, 4)
        binding.recyclerOnlyGallery.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.width_3)))
        binding.recyclerOnlyGallery.layoutManager = layoutManager
        mAdapter = GalleryOnlyAdapter()
        binding.recyclerOnlyGallery.adapter = mAdapter
        mAdapter!!.setMaxCount(mMaxCount)

        binding.textGalleryvCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.textGalleryOnlyComplete.setOnClickListener {
            val selectList = mAdapter!!.selectGalleryList

            val imageList = arrayListOf<Uri>()

            val job = Job()
            showProgress("")
            CoroutineScope(Dispatchers.IO + job).launch {

                for(galleryData in selectList){

                    val extensions = contentResolver.getType(galleryData.imageUri!!)!!.split("/".toRegex()).toTypedArray()
                    val extension = extensions[extensions.size - 1]
                    val names = galleryData.imageUri!!.path!!.split("/".toRegex()).toTypedArray()
                    val fileName = names[names.size - 1]
                    LogUtil.e(LOG_TAG, fileName)
                    val albumF = PplusCommonUtil.getAlbumDir()
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val file = File(albumF, "temp_" + timeStamp + "_" + fileName + "." + extension)

                    try {
                        val bitmap: Bitmap
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, galleryData.imageUri!!))
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, galleryData.imageUri!!)
                        }
                        if (bitmap != null) {
                            val bytes = ByteArrayOutputStream()
                            if (extension == "png") {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
                            } else {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                            }

                            val bitmapData = bytes.toByteArray() //write the bytes in file //write the bytes in file
                            val fos = FileOutputStream(file)
                            fos.write(bitmapData)
                            fos.flush()
                            fos.close()
                            val outputFileUri = Uri.fromFile(file)
                            imageList.add(outputFileUri)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                CoroutineScope(Dispatchers.Main + job).launch {
                    hideProgress()
                    val data = Intent()
                    data.putParcelableArrayListExtra(Const.IMAGE_LIST, imageList)
                    setResult(RESULT_OK, data)
                    finish()
                }
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