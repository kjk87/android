package com.pplus.prnumberbiz.apps.post.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.LinearLayout
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.post.data.PostRegImagePagerAdapter
import com.pplus.prnumberbiz.apps.post.data.UploadStatus
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.dto.No
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_post_write.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PostRegActivity : BaseActivity() {
    override fun getPID(): String {

        val mode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        if (mode == EnumData.MODE.UPDATE) {
            return "post edit"
        } else {
            return "Main_make product"
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_post_write
    }

    var mMode = EnumData.MODE.WRITE
    var mPost: Post? = null

    private var mImageAdapter: PostRegImagePagerAdapter? = null
    private var defaultUpload: DefaultUpload? = null
    private var uploadCheckThread: UploadCheckThread? = null
    private lateinit var mUploadDataMap: ConcurrentHashMap<String, UploadStatus>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>

    override fun initializeView(savedInstanceState: Bundle?) {
        mMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE


        mUploadDataMap = ConcurrentHashMap()
        mUploadUrlMap = ConcurrentHashMap()

        mImageAdapter = PostRegImagePagerAdapter(this)
        pager_post_image.adapter = mImageAdapter
        pager_post_image.offscreenPageLimit = 10
        pager_post_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                indicator_post_image.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        image_post_reg_back.setOnClickListener {
            onBackPressed()
        }

        text_post_reg.setOnClickListener {
            reg()
        }
        text_post_reg2.setOnClickListener {
            reg()
        }

        layout_post_add_image.setOnClickListener {
            goPostGallery()
        }

        layout_post_add_image2.setOnClickListener {
            goPostGallery()
        }
        edit_post_title.setSingleLine()
        edit_post_contents.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (edit_post_contents.length() > 0) {
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        if (mMode == EnumData.MODE.UPDATE) {

            mPost = intent.getParcelableExtra(Const.DATA)
            if (mPost != null) {

                if (StringUtils.isNotEmpty(mPost!!.subject)) {
                    edit_post_title.setText(mPost!!.subject)
                }

                if (StringUtils.isNotEmpty(mPost!!.contents)) {
                    edit_post_contents.setText(mPost!!.contents)
                }

                if (StringUtils.isNotEmpty(mPost!!.articleUrl)) {
                    edit_post_url.setText(mPost!!.articleUrl)
                }

                if (mPost!!.attachList != null && mPost!!.attachList!!.isNotEmpty()) {
                    mImageAdapter!!.mDataList = mPost!!.attachList as ArrayList<Attachment>
                    mImageAdapter!!.notifyDataSetChanged()

                    indicator_post_image.visibility = View.VISIBLE
                    indicator_post_image.removeAllViews()
                    indicator_post_image.build(LinearLayout.HORIZONTAL, mPost!!.attachList!!.size)
                }
            }
        }

        mImageAdapter!!.setOnItemChangeListener(object : PostRegImagePagerAdapter.OnItemChangeListener {
            override fun onChange() {
                checkImage()
            }
        })

        checkImage()

    }


    private fun reg() {
        if (isEmptyData()) {
            return
        }

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        when (mMode) {
            EnumData.MODE.WRITE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_post_write), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
            EnumData.MODE.UPDATE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_post_modify), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
        }

        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        update()

                    }
                }
            }
        }).builder().show(this)
    }

    private fun goPostGallery() {

        if (mImageAdapter!!.count < 10) {
            val intent = Intent(this, PostGalleryActivity::class.java)
            intent.putExtra(Const.COUNT, Const.IMAGE_UPLOAD_MAX_COUNT - mImageAdapter!!.count)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_GALLERY)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Const.REQ_GALLERY -> if (data != null) {
                    val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)

                    val index = mImageAdapter!!.count
                    for (i in 0 until dataList.size) {
                        mImageAdapter!!.add(Attachment())
                        mUploadDataMap[(index + i).toString()] = UploadStatus.UPLOAD
                        mUploadUrlMap[(index + i).toString()] = dataList[i].path
                    }

                    showProgress("")
                    for (i in 0 until dataList.size) {
                        uploadFile((index + i).toString(), dataList[i].path)
                    }

                    checkImage()
                }
            }
        }
    }

    fun checkImage() {
        if (mImageAdapter!!.count > 0) {
            pager_post_image.visibility = View.VISIBLE
            indicator_post_image.visibility = View.VISIBLE
            indicator_post_image.removeAllViews()
            indicator_post_image.build(LinearLayout.HORIZONTAL, mImageAdapter!!.count)
            indicator_post_image.setCurrentItem(pager_post_image.currentItem)
            layout_post_add_image.visibility = View.GONE
            layout_post_add_image2.visibility = View.VISIBLE
        } else {
            pager_post_image.visibility = View.GONE
            indicator_post_image.visibility = View.GONE
            layout_post_add_image.visibility = View.VISIBLE
            layout_post_add_image2.visibility = View.GONE
        }
    }

    private fun uploadFile(key: String, url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.postList
        paramsAttachment.setFile(url)

        if (defaultUpload == null) {
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
                    LogUtil.e(LOG_TAG, "tag = {}, no = {}, id = {}", tag, resultResponse.data.no, resultResponse.data.id)
                    mImageAdapter!!.insertAttach(tag, resultResponse.data)
                    mUploadDataMap[tag] = UploadStatus.SUCCESS_UPLOAD

                    if (checkFinish()) {
                        hideProgress()
                        delete()
                    }
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    mUploadDataMap[tag] = UploadStatus.ERROR_UPLOAD
                }

            })
        }

        defaultUpload!!.request(key, paramsAttachment)

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
     * 이미지 업로드가 정상적으로 이루졌는지 체크 후 포스트 등록 하는 쓰래드
     */
    inner class UploadCheckThread : Thread() {
        init {

            if (mMode == EnumData.MODE.WRITE) {
                showProgress(getString(R.string.msg_registration_of_goods))
            } else {
                showProgress(getString(R.string.msg_modification_of_goods))
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

                    postGoods()
                }
            }

            uploadCheckThread = null
        }

        private fun showProgress(message: String) {

            runOnUiThread { this@PostRegActivity.showProgress(message) }
        }
    }

    private fun isEmptyData(): Boolean {


        if ((mImageAdapter!!.getDataList() == null || mImageAdapter!!.getDataList()!!.isEmpty())) {
            showAlert(R.string.msg_reg_photo)
            return true
        }

        val title = edit_post_title.text.toString().trim()

        if (StringUtils.isEmpty(title)) {
            showAlert(R.string.msg_input_title)
            return true
        }

        val contents = edit_post_contents.text.toString().trim()

        if (StringUtils.isEmpty(contents)) {
            showAlert(R.string.msg_input_contents)
            return true
        }

        if (contents.length < 10) {
            showAlert(getString(R.string.format_msg_goods_input_over, 10))
            return true
        }

        val url = edit_post_url.text.toString().trim()
        if (StringUtils.isNotEmpty(url) && !URLUtil.isValidUrl(url)) {
            showAlert(R.string.msg_invalid_url)
            return true
        }
        return false
    }

    fun delete() {
        val iterator = mUploadUrlMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            PplusCommonUtil.deleteFromMediaScanner(mUploadUrlMap[key]!!)
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

    private fun postGoods() {
        val params = Post()
        params.board = No(LoginInfoManager.getInstance().user.page!!.prBoard!!.no)

        val title = edit_post_title.text.toString().trim()
        params.subject = title

        val contents = edit_post_contents.text.toString()

        if (StringUtils.isEmpty(contents)) {
            ToastUtil.show(this, R.string.msg_input_event_contents)
            return
        }

        val url = edit_post_url.text.toString().trim()
        if(StringUtils.isNotEmpty(url)){
            params.articleUrl = url
        }


        params.contents = contents
        params.type = "pr"

        if (mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.size > 0) {
            params.attachList = mImageAdapter!!.getDataList()
        }


        when (mMode) {
            EnumData.MODE.WRITE -> {

                showProgress(getString(R.string.msg_registration_of_goods))

                ApiBuilder.create().insertPost(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
                    override fun onResponse(call: Call<NewResultResponse<Post>>?, response: NewResultResponse<Post>?) {
                        hideProgress()

                        if (mMode == EnumData.MODE.WRITE) {
                            showAlert(R.string.msg_registed_post)
                        } else {
                            showAlert(R.string.msg_modified_post)
                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Post>>?, t: Throwable?, response: NewResultResponse<Post>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            EnumData.MODE.UPDATE -> {
                params.no = mPost!!.no

                showProgress(getString(R.string.msg_modification_of_goods))

                ApiBuilder.create().updatePost(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {

                    override fun onResponse(call: Call<NewResultResponse<Post>>?, response: NewResultResponse<Post>?) {
                        hideProgress()
                        val data = Intent()
                        data.putExtra(Const.DATA, mPost)
                        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Post>>?, t: Throwable?, response: NewResultResponse<Post>?) {
                        hideProgress()
                    }
                }).build().call()

            }
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
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }

    }

    private fun isEditingData(): Boolean {

        val title = edit_post_title.text.toString().trim()

        if (StringUtils.isNotEmpty(title)) {
            return true
        }

        val contents = edit_post_contents.text.toString().trim()

        if (StringUtils.isNotEmpty(contents)) {
            return true
        }

        if ((mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.isNotEmpty())) {
            return true
        }

        return false
    }
}
