package com.pplus.luckybol.apps.buff.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.RegImagePagerAdapter
import com.pplus.luckybol.apps.post.data.UploadStatus
import com.pplus.luckybol.apps.post.ui.PostGalleryActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.code.custom.AttachmentTargetTypeCode
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Attachment
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.dto.BuffPostImage
import com.pplus.luckybol.core.network.model.request.params.ParamsAttachment
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.upload.PplusUploadListener
import com.pplus.luckybol.core.network.upload.S3Upload
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityBuffPostRegBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.concurrent.ConcurrentHashMap

class BuffPostRegActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityBuffPostRegBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffPostRegBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPostMode: EnumData.MODE? = null
    private var mBuffPost: BuffPost? = null
    private var mBuff: Buff? = null

    private var mImageAdapter: RegImagePagerAdapter? = null
    private var s3Upload: S3Upload? = null
    private var uploadCheckThread: UploadCheckThread? = null
    private lateinit var mUploadDataMap: ConcurrentHashMap<String, UploadStatus>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuff = intent.getParcelableExtra(Const.BUFF)
        mPostMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        mUploadDataMap = ConcurrentHashMap()
        mUploadUrlMap = ConcurrentHashMap()

        mImageAdapter = RegImagePagerAdapter(this)
        binding.pagerBuffPostRegImage.adapter = mImageAdapter
        binding.pagerBuffPostRegImage.offscreenPageLimit = 10

        if (mPostMode == EnumData.MODE.UPDATE) {
            mBuffPost = intent.getParcelableExtra(Const.DATA)
            if (mBuffPost != null) {
                binding.editBuffPostRegContents.setText(mBuffPost!!.content)

                if (mBuffPost!!.imageList != null && mBuffPost!!.imageList!!.isNotEmpty()) {

                    for(buffPostImage in mBuffPost!!.imageList!!){
                        mImageAdapter!!.add(buffPostImage.image!!)
                    }
                    mImageAdapter!!.notifyDataSetChanged()
                }
            } else {
                binding.layoutBuffPostRegImage.visibility = View.GONE
            }
            checkImage()
        }

        binding.layoutBuffPostRegRegPhoto.setOnClickListener {
            goPostGallery()
        }

        binding.editBuffPostRegContents.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (binding.editBuffPostRegContents.length() > 0) {
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        mImageAdapter!!.setOnItemChangeListener(object : RegImagePagerAdapter.OnItemChangeListener {
            override fun onChange() {
                checkImage()
            }
        })
    }

    fun checkImage() {
        if (mImageAdapter!!.count > 0) {
            binding.layoutBuffPostRegImage.visibility = View.VISIBLE
        } else {
            binding.layoutBuffPostRegImage.visibility = View.GONE
        }
    }

    fun setImageRefresh(pos: Int) {
        binding.pagerBuffPostRegImage.currentItem = 0
    }

    fun goPostGallery() {

        if (mImageAdapter!!.count < 10) {
            val intent = Intent(this, PostGalleryActivity::class.java)
            intent.putExtra(Const.COUNT, 10 - mImageAdapter!!.count)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            galleryLauncher.launch(intent)
        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                }
            }).builder().show(this)
        }

    }

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)!!

                val index = mImageAdapter!!.count
                for (i in 0 until dataList.size) {
                    mImageAdapter!!.add("")
                    mUploadDataMap[(index + i).toString()] = UploadStatus.UPLOAD
                    mUploadUrlMap[(index + i).toString()] = dataList[i].path!!
                }

                showProgress("")
                for (i in 0 until dataList.size) {
                    uploadFile((index + i).toString(), dataList[i].path!!)
                }

                checkImage()
            }
        }
    }

    /**
     * 게시글 등록/수정
     */
    fun update() {

        if (isEmptyData()) {
            return
        }

        if (uploadCheckThread == null || !uploadCheckThread!!.isAlive) {
            uploadCheckThread = UploadCheckThread()
            uploadCheckThread!!.setDaemon(true)
            uploadCheckThread!!.start()
        }

    }

    /**
     * 등록 가능 여부 체크
     *
     * @return
     */
    private fun isEmptyData(): Boolean {

        val contents = binding.editBuffPostRegContents.text.toString().trim()

        if (StringUtils.isEmpty(contents)) {
            ToastUtil.show(this, R.string.msg_input_buff_post_contents)
            return true
        }

        if (contents.length < 10) {
            ToastUtil.show(this, R.string.msg_input_over_10)
            return true
        }

        if (mImageAdapter!!.getDataList() == null || mImageAdapter!!.getDataList()!!.isEmpty()) {
            ToastUtil.show(this, R.string.msg_reg_event_image)
            return true
        }
        return false
    }

    private fun isEditingData(): Boolean {

        val contents = binding.editBuffPostRegContents.text.toString().trim()

        if (StringUtils.isNotEmpty(contents)) {
            return true
        }

        return false
    }

    private fun uploadFile(key: String, url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.postList
        paramsAttachment.setFile(url)

        if (s3Upload == null) {
            s3Upload = S3Upload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
                    LogUtil.e(LOG_TAG, "tag = {}, url = {}, id = {}", tag, resultResponse.data!!.url, resultResponse.data!!.id)
                    mImageAdapter!!.insertAttach(tag, resultResponse.data!!)
                    mUploadDataMap[tag] = UploadStatus.SUCCESS_UPLOAD

                    if (checkFinish()) {
                        hideProgress()
                        delete(url)
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    mUploadDataMap[tag] = UploadStatus.ERROR_UPLOAD
                }

            })
        }

        s3Upload!!.request(key, paramsAttachment)

    }

    fun delete(url: String) {
        val iterator = mUploadUrlMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            PplusCommonUtil.deleteFromMediaScanner(url)
        }
    }

    fun checkFinish(): Boolean {
        val iterator = mUploadDataMap.keys.iterator()
        var isFinish = true
        var isError = false
        while (iterator.hasNext()) {
            val key = iterator.next()
            when (mUploadDataMap[key]) {
                UploadStatus.UPLOAD -> {
                    isFinish = false
                }
                UploadStatus.ERROR_UPLOAD -> {
                    isError = true
                }
                else -> {}
            }
            if (!isFinish) {
                break
            }
        }

        if (isError) {
            hideProgress()
        }

        return isFinish
    }

    /**
     * 이미지 업로드가 정상적으로 이루졌는지 체크 후 포스트 등록 하는 쓰래드
     */
    inner class UploadCheckThread : Thread() {
        init {

            if (mPostMode == EnumData.MODE.WRITE) {
                showProgress(getString(R.string.msg_registration_of_post))
            } else {
                showProgress(getString(R.string.msg_modification_of_post))
            }
        }

        override fun run() {

            super.run()

            val isRun = true
            val requestKeys = ArrayList<String>()
            // 재시도 카운트
            var retryCount = 0

            while (isRun) {

                // 업로드 진행중인 카운트
                var uploadCnt = 0
                // 에러 카운트
                var errorCnt = 0
                val retryCnt = 0
                var successCnt = 0

                requestKeys.clear()

                var iterator = mUploadDataMap.keys.iterator()
                while (iterator.hasNext()) {

                    val key = iterator.next()
                    LogUtil.e(LOG_TAG, "mUploadDataMap.get(key) = {}", mUploadDataMap[key])

                    when (mUploadDataMap[key]) {
                        UploadStatus.UPLOAD -> uploadCnt++
                        UploadStatus.ERROR_UPLOAD -> {
                            errorCnt++
                            requestKeys.add(key)
                        }
                        UploadStatus.SUCCESS_UPLOAD -> successCnt++
                        else -> {}
                    }
                }

                if (uploadCnt == 0 && errorCnt == 0 && retryCnt == 0) {
                    // 정상적으로 모두 업로드가 되어있는 상태 이다.
                    LogUtil.e(LOG_TAG, "모든 파일이 정상적으로 업로드 되었다.")
                    break
                } else {
                    if (!checkSelfNetwork()) {
                        return
                    }
                    if (retryCount > 3) {
                        break
                    }
                    for (key in requestKeys) {
                        LogUtil.e(LOG_TAG, "upload 요청 = {}", mUploadUrlMap[key])
                        uploadFile(key, mUploadUrlMap[key]!!)
                    }
                    while (isRun) {

                        LogUtil.e(LOG_TAG, "업로드중인 파일 체크")
                        // 첨부파일을 등록중 입니다.(%d/%d)
                        showProgress(String.format(getString(R.string.format_post_photo_register), successCnt, mUploadDataMap.size))
                        SystemClock.sleep((2 * 1000).toLong())
                        requestKeys.clear()

                        iterator = mUploadDataMap.keys.iterator()

                        uploadCnt = 0
                        successCnt = 0
                        errorCnt = 0

                        while (iterator.hasNext()) {

                            val key = iterator.next()
                            LogUtil.e(LOG_TAG, "업로드 상태 체크 = {}", mUploadDataMap[key])
                            when (mUploadDataMap[key]) {

                                UploadStatus.UPLOAD -> uploadCnt++
                                UploadStatus.ERROR_UPLOAD -> errorCnt++
                                UploadStatus.SUCCESS_UPLOAD -> successCnt++
                                else -> {}
                            }
                        }
                        if (uploadCnt == 0) {
                            break
                        }
                    }
                    LogUtil.e(LOG_TAG, "업로드중인 파일 체크 종료")
                }
                retryCount++
            }

            if (retryCount > 3) {
                runOnUiThread {
                    showAlert(getString(R.string.msg_error_registration_of_photo))
                    hideProgress()
                }
            } else {
                runOnUiThread {

                    requestPost()
                }
            }

            uploadCheckThread = null
        }

        private fun showProgress(message: String) {

            runOnUiThread { this@BuffPostRegActivity.showProgress(message) }
        }
    }

    /**
     * 네트워크 체크
     *
     * @return
     */
    fun checkSelfNetwork(): Boolean {

        if (!PplusCommonUtil.hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.builder().show(this, true)
            return false
        }
        return true
    }

    private fun requestPost() {

        val contents = binding.editBuffPostRegContents.text.toString().trim()

        val params = BuffPost()
        if (mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.size > 0) {
            val imageList = arrayListOf<BuffPostImage>()
            var array = 0
            for(image in mImageAdapter!!.getDataList()!!){
                array++
                val buffPostImage = BuffPostImage()
                buffPostImage.array = array
                buffPostImage.image = image
                imageList.add(buffPostImage)
            }
            params.imageList = imageList
        }

        when (mPostMode) {

            EnumData.MODE.WRITE -> {

                params.memberSeqNo = LoginInfoManager.getInstance().user.no
                params.content = contents
                params.buffSeqNo = mBuff!!.seqNo


                showProgress("")
                ApiBuilder.create().insertBuffPost(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_registered_buff_post)
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            EnumData.MODE.UPDATE -> {
                params.seqNo = mBuffPost!!.seqNo
                params.buffSeqNo = mBuff!!.seqNo
                params.memberSeqNo = mBuffPost!!.memberSeqNo
                params.content = contents

                showProgress("")
                ApiBuilder.create().updateBuffPost(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_modified_buff_post)
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            else -> {}
        }
    }

    override fun onBackPressed() {
        if (isEditingData()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_back), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> finish()
                        else -> {}
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buff_post_write), ToolbarOption.ToolbarMenu.LEFT)
        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
        textRightTop.setText(R.string.word_reg)
        textRightTop.setClickable(true)
        textRightTop.setGravity(Gravity.CENTER)
        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_48pt).toFloat())
        textRightTop.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    if (isEmptyData()) {
                        return@OnToolbarListener
                    }

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_buff_post_write), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> update()
                                else -> {}
                            }
                        }
                    }).builder().show(this@BuffPostRegActivity)
                }
            }
        }
    }
}
