//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.content.ContentUris
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.os.SystemClock
//import android.provider.MediaStore
//import android.util.TypedValue
//import android.view.ContextThemeWrapper
//import android.view.Gravity
//import android.view.View
//import android.widget.TextView
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.data.PostWriteImagePagerAdapter
//import com.pplus.prnumberuser.apps.post.data.UploadStatus
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.custom.AttachmentTargetTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Attachment
//import com.pplus.prnumberuser.core.network.model.dto.No
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.request.params.ParamsAttachment
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.upload.DefaultUpload
//import com.pplus.prnumberuser.core.network.upload.PplusUploadListener
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_user_post_write.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.io.File
//import java.io.IOException
//import java.util.*
//import java.util.concurrent.ConcurrentHashMap
//import kotlin.collections.ArrayList
//
//class UserPostWriteActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_user_post_write
//    }
//
//    private var mPostMode: EnumData.MODE? = null
//    private var mPost: Post? = null
//    private var mImageAdapter: PostWriteImagePagerAdapter? = null
//    private var uploadDataMap: ConcurrentHashMap<String, UploadStatus>? = null
//    private var defaultUpload: DefaultUpload? = null
//    private var uploadCheckThread: UploadCheckThread? = null
//    private var mAttachImageList: ArrayList<String>? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPostMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
//        uploadDataMap = ConcurrentHashMap()
//
//        mImageAdapter = PostWriteImagePagerAdapter(this)
//
//        mImageAdapter!!.listener = object : PostWriteImagePagerAdapter.OnItemChangeListener{
//            override fun goGallery() {
//                goPostGallery()
//            }
//
//            override fun onChange() {
//                checkImage()
//            }
//        }
//
//        if (mPostMode == EnumData.MODE.UPDATE) {
//            mPost = intent.getParcelableExtra(Const.DATA)
//            if (mPost != null) {
//                edit_user_post_write_contents.setText(mPost!!.contents)
//
//                if (mPost!!.attachList!!.size > 0) {
//                    layout_user_post_write_image.visibility = View.VISIBLE
//                }
//                mImageAdapter!!.attachedList = mPost!!.attachList as ArrayList<Attachment>
//            } else {
//                layout_user_post_write_image.visibility = View.GONE
//            }
//        }
//
//        layout_user_post_write_reg_photo.setOnClickListener {
//            goPostGallery()
//        }
//
//        pager_user_post_write_image.adapter = mImageAdapter
//        pager_user_post_write_image.offscreenPageLimit = 10
//    }
//
//    fun checkImage() {
//        if (mImageAdapter!!.count > 0) {
//            layout_user_post_write_image.visibility = View.VISIBLE
//        } else {
//            layout_user_post_write_image.visibility = View.GONE
//        }
//    }
//
//    private fun goPostGallery() {
//
//        if (mImageAdapter!!.count < 10) {
//            val intent = Intent(this, PostGalleryActivity::class.java)
//            intent.putExtra(Const.COUNT, 10 - mImageAdapter!!.count)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            galleryLauncher.launch(intent)
//        } else {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                }
//            }).builder().show(this)
//        }
//
//    }
//
//    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            if (data != null) {
//                val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)!!
//                for (uri in dataList) {
//                    mImageAdapter!!.addData(uri.path!!)
//                    uploadFile(uri.path!!)
//                }
//
//                checkImage()
//            }
//        }
//    }
//
//    /**
//     * 게시글 등록/수정
//     */
//    fun update() {
//
//        if (isEmptyData()) {
//            return
//        }
//
//        if (uploadCheckThread == null || !uploadCheckThread!!.isAlive) {
//            uploadCheckThread = UploadCheckThread()
//            uploadCheckThread!!.setDaemon(true)
//            uploadCheckThread!!.start()
//        }
//
//    }
//
//    /**
//     * 등록 가능 여부 체크
//     *
//     * @return
//     */
//    private fun isEmptyData(): Boolean {
//
//
//        val contents = edit_user_post_write_contents.text.toString().trim()
//
//        if (StringUtils.isEmpty(contents) || contents.length < 10) {
//            ToastUtil.show(this, R.string.msg_input_real_impression_over_10)
//            return true
//        }
//
//        if ((mImageAdapter!!.attachedList == null || mImageAdapter!!.attachedList.size == 0) && (mImageAdapter!!.dataList == null || mImageAdapter!!.dataList.size == 0)) {
//            ToastUtil.show(this, R.string.msg_reg_photo)
//            return true
//        }
//        return false
//    }
//
//    private fun uploadFile(url: String) {
//
//        val paramsAttachment = ParamsAttachment()
//        paramsAttachment.targetType = AttachmentTargetTypeCode.postList
//
//        paramsAttachment.setFile(url)
//
//        uploadDataMap!![url] = UploadStatus.UPLOAD
//
//        if (defaultUpload == null) {
//            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {
//
//                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
//
//                    LogUtil.e(LOG_TAG, "tag = {} result = {}", tag, resultResponse.data.toString())
//
//                    if (mAttachImageList == null) {
//                        mAttachImageList = ArrayList()
//                    }
//                    mImageAdapter!!.insertAttach(tag, resultResponse.data)
////                    PplusCommonUtil.getFile(url).delete()
////                    PplusCommonUtil.deleteFromMediaScanner(url)
//
//
//                    mAttachImageList!!.add(resultResponse.data.url)
//                    uploadDataMap!![tag] = UploadStatus.SUCCESS_UPLOAD
//
//                    if (uploadCheckThread != null) {
//                        uploadCheckThread!!.interrupt()
//                    }
//
//                    if (checkFinish()) {
//                        delete()
//                    }
//                }
//
//                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {
//
//                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
//                    uploadDataMap!![tag] = UploadStatus.ERROR_UPLOAD
//                }
//
//            })
//        }
//
//        defaultUpload!!.request(url, paramsAttachment)
//
//    }
//
//    fun delete() {
//        val iterator = uploadDataMap!!.keys.iterator()
//        while (iterator.hasNext()) {
//            val key = iterator.next()
//            PplusCommonUtil.deleteFromMediaScanner(key)
//        }
//    }
//
//    fun checkFinish(): Boolean {
//        val iterator = uploadDataMap!!.keys.iterator()
//        var isFinish = true
//        var isError = false
//        while (iterator.hasNext()) {
//            val key = iterator.next()
//            when (uploadDataMap!![key]) {
//                UploadStatus.UPLOAD -> {
//                    isFinish = false
//                }
//                UploadStatus.ERROR_UPLOAD -> {
//                    isError = true
//                }
//            }
//            if (!isFinish) {
//                break
//            }
//        }
//
//        if (isError) {
//            hideProgress()
//        }
//
//        return isFinish
//    }
//
//
//    private fun getFile(name: String): File {
//
//        val output = File(name)
//        if (!output.exists()) {
//            try {
//                output.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//        return output
//    }
//
//    private fun deleteFromMediaScanner(filePath: String): Boolean {
//        var filePath = filePath
//
//        val fileUri = Uri.parse(filePath)
//        filePath = fileUri.path!!
//
//        val c = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='$filePath'", null, null)
//        if (c != null && c.moveToFirst()) {
//            val id = c.getInt(0)
//            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
//
//            return contentResolver.delete(uri, null, null) == 1
//        }
//        c?.close()
//        return false
//    }
//
//    /**
//     * 이미지 업로드가 정상적으로 이루졌는지 체크 후 포스트 등록 하는 쓰래드
//     */
//    inner class UploadCheckThread : Thread() {
//        init {
//
//            if (mPostMode == EnumData.MODE.WRITE) {
//                showProgress(getString(R.string.msg_registration_of_post))
//            } else {
//                showProgress(getString(R.string.msg_modification_of_post))
//            }
//        }
//
//        override fun run() {
//
//            super.run()
//
//            val isRun = true
//            val requestUrls = ArrayList<String>()
//            // 재시도 카운트
//            var retryCount = 0
//
//            while (isRun) {
//
//                // 업로드 진행중인 카운트
//                var uploadCnt = 0
//                // 에러 카운트
//                var errorCnt = 0
//                val retryCnt = 0
//                var successCnt = 0
//
//                requestUrls.clear()
//
//                var iterator = uploadDataMap!!.keys.iterator()
//                while (iterator.hasNext()) {
//
//                    val key = iterator.next()
//                    LogUtil.e(LOG_TAG, "uploadDataMap.get(key) = {}", uploadDataMap!![key])
//
//                    when (uploadDataMap!![key]) {
//                        UploadStatus.UPLOAD -> uploadCnt++
//                        UploadStatus.ERROR_UPLOAD -> {
//                            errorCnt++
//                            requestUrls.add(key)
//                        }
//                        UploadStatus.SUCCESS_UPLOAD -> successCnt++
//                    }
//                }
//
//                if (uploadCnt == 0 && errorCnt == 0 && retryCnt == 0) {
//                    // 정상적으로 모두 업로드가 되어있는 상태 이다.
//                    LogUtil.e(LOG_TAG, "모든 파일이 정상적으로 업로드 되었다.")
//                    break
//                } else {
//                    if (!checkSelfNetwork()) {
//                        return
//                    }
//                    if (retryCount > 3) {
//                        break
//                    }
//                    for (url in requestUrls) {
//                        LogUtil.e(LOG_TAG, "upload 요청 = {}", url)
//                        uploadFile(url)
//                    }
//                    while (isRun) {
//
//                        LogUtil.e(LOG_TAG, "업로드중인 파일 체크")
//                        // 첨부파일을 등록중 입니다.(%d/%d)
//                        showProgress(String.format(getString(R.string.format_post_photo_register), successCnt, uploadDataMap!!.size))
//                        SystemClock.sleep((2 * 1000).toLong())
//                        requestUrls.clear()
//
//                        iterator = uploadDataMap!!.keys.iterator()
//
//                        uploadCnt = 0
//                        successCnt = 0
//                        errorCnt = 0
//
//                        while (iterator.hasNext()) {
//
//                            val key = iterator.next()
//                            LogUtil.e(LOG_TAG, "업로드 상태 체크 = {}", uploadDataMap!![key])
//                            when (uploadDataMap!![key]) {
//
//                                UploadStatus.UPLOAD -> uploadCnt++
//                                UploadStatus.ERROR_UPLOAD -> errorCnt++
//                                UploadStatus.SUCCESS_UPLOAD -> successCnt++
//                            }
//                        }
//                        if (uploadCnt == 0) {
//                            break
//                        }
//                    }
//                    LogUtil.e(LOG_TAG, "업로드중인 파일 체크 종료")
//                }
//                retryCount++
//            }
//
//            if (retryCount > 3) {
//                runOnUiThread {
//                    showAlert(getString(R.string.msg_error_registration_of_photo))
//                    hideProgress()
//                }
//            } else {
//                runOnUiThread {
//                    if (mPostMode == EnumData.MODE.WRITE) {
//                        showProgress(getString(R.string.msg_registration_of_review))
//                    } else {
//                        showProgress(getString(R.string.msg_modification_of_review))
//                    }
//                    requestPost()
//                }
//            }
//
//            uploadCheckThread = null
//        }
//
//        private fun showProgress(message: String) {
//
//            runOnUiThread { this@UserPostWriteActivity.showProgress(message) }
//        }
//    }
//
//    /**
//     * 네트워크 체크
//     *
//     * @return
//     */
//    fun checkSelfNetwork(): Boolean {
//
//        if (!PplusCommonUtil.hasNetworkConnection()) {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_confirm))
//            builder.builder().show(this, true)
//            return false
//        }
//        return true
//    }
//
//    private fun requestPost() {
//
//        val contents = edit_user_post_write_contents.text.toString().trim()
//
//        if (StringUtils.isEmpty(contents)) {
//            ToastUtil.show(this, R.string.msg_input_review_contents)
//            return
//        }
//
//        if (contents.length < 10) {
//            ToastUtil.show(this, R.string.msg_input_review_over_10)
//            return
//        }
//
//        val params = Post()
//
//
//        params.contents = contents
//        params.type = EnumData.PostType.member.name
//
//        when (mPostMode) {
//
//            EnumData.MODE.WRITE -> {
//                if (LoginInfoManager.getInstance().user.boardSeqNo != null) {
//                    params.board = No(LoginInfoManager.getInstance().user.boardSeqNo)
//                }
//
//
//                if (mImageAdapter!!.getAttachList() != null && mImageAdapter!!.getAttachList().size> 0) {
//                    params.attachList = mImageAdapter!!.getAttachList()
//                }
//                showProgress("")
//
//                ApiBuilder.create().insertPost(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//                    override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//
//                        hideProgress()
//                        LogUtil.e(LOG_TAG, response.data.toString())
//                        setResult(RESULT_OK)
//                        finish()
//
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                        hideProgress()
//                    }
//                }).build().call()
//            }
//            EnumData.MODE.UPDATE -> {
//                params.no = mPost!!.no
//                val list = ArrayList<Attachment>()
//                if (mImageAdapter!!.attachedList != null && mImageAdapter!!.attachedList.size > 0) {
//                    list.addAll(mImageAdapter!!.attachedList)
//                }
//
//                if (mImageAdapter!!.getAttachList() != null && mImageAdapter!!.getAttachList().size > 0) {
//                    list.addAll(mImageAdapter!!.getAttachList())
//                }
//                if (list.size > 0) {
//                    params.attachList = list
//                }
//                showProgress("")
//                ApiBuilder.create().updatePost(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//                    override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//
//                        showAlert(R.string.msg_registered_review_post)
//                        hideProgress()
//                        setResult(RESULT_OK)
//                        finish()
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                        hideProgress()
//                    }
//                }).build().call()
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_reg_real_impression), ToolbarOption.ToolbarMenu.LEFT)
//        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
//        textRightTop.setText(R.string.word_reg)
//        textRightTop.setClickable(true)
//        textRightTop.setGravity(Gravity.CENTER)
//        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
//        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
//        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_48pt).toFloat())
//        textRightTop.setSingleLine()
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    if (isEmptyData()) {
//                        return@OnToolbarListener
//                    }
//
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(getString(R.string.word_notice_alert))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_real_impression), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                            when (event_alert) {
//                                AlertBuilder.EVENT_ALERT.RIGHT -> update()
//                            }
//                        }
//                    }).builder().show(this@UserPostWriteActivity)
//                }
//            }
//        }
//    }
//}
