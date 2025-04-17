package com.pplus.prnumberuser.apps.menu.ui

import android.app.Activity
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
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.post.data.UploadStatus
import com.pplus.prnumberuser.apps.post.ui.PostGalleryActivity
import com.pplus.prnumberuser.apps.product.data.RegImagePagerAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.upload.PplusUploadListener
import com.pplus.prnumberuser.core.network.upload.S3Upload
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityProductReviewRegBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class OrderMenuReviewRegActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityProductReviewRegBinding

    override fun getLayoutView(): View {
        binding = ActivityProductReviewRegBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPostMode: EnumData.MODE? = null
    private var mOrderMenuReview: OrderMenuReview? = null

    //    private var mProductPrice: ProductPrice? = null
    private var mOrderPurchase: OrderPurchase? = null

    private var mImageAdapter: RegImagePagerAdapter? = null
    private var s3Upload: S3Upload? = null
    private var uploadCheckThread: UploadCheckThread? = null
    private lateinit var mUploadDataMap: ConcurrentHashMap<String, UploadStatus>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>

    override fun initializeView(savedInstanceState: Bundle?) {

        mPostMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        mOrderPurchase = intent.getParcelableExtra(Const.ORDER_PURCHASE)

        mUploadDataMap = ConcurrentHashMap()
        mUploadUrlMap = ConcurrentHashMap()

        mImageAdapter = RegImagePagerAdapter(this)
        binding.pagerProductReviewRegImage.adapter = mImageAdapter
        binding.pagerProductReviewRegImage.offscreenPageLimit = 10

        if (mPostMode == EnumData.MODE.UPDATE) {
            mOrderMenuReview = intent.getParcelableExtra(Const.DATA)
            if (mOrderMenuReview != null) {
                binding.editProductReviewRegContents.setText(mOrderMenuReview!!.review)

                if (mOrderMenuReview!!.imageList != null && mOrderMenuReview!!.imageList!!.isNotEmpty()) {

                    for (reviewImage in mOrderMenuReview!!.imageList!!) {
                        mImageAdapter!!.add(reviewImage.image!!)
                    }
                    mImageAdapter!!.notifyDataSetChanged()
                }

                binding.ratingProductReviewReg.rating = mOrderMenuReview!!.eval!!.toFloat()
            } else {
                binding.layoutProductReviewRegImage.visibility = View.GONE
            }
            checkImage()
        }

        binding.ratingProductReviewReg.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (rating <= 1.0) {
                ToastUtil.show(this, R.string.word_very_dissatisfaction)
            } else if (rating <= 2.0) {
                ToastUtil.show(this, R.string.word_dissatisfaction)
            } else if (rating <= 3.0) {
                ToastUtil.show(this, R.string.word_normal)
            } else if (rating <= 4.0) {
                ToastUtil.show(this, R.string.word_satisfied)
            } else if (rating <= 5.0) {
                ToastUtil.show(this, R.string.word_very_satisfied)
            }
        }

        binding.layoutProductReviewRegRegPhoto.setOnClickListener {
            goPostGallery()
        }

        binding.editProductReviewRegContents.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (binding.editProductReviewRegContents.length() > 0) {
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
            binding.layoutProductReviewRegImage.visibility = View.VISIBLE
        } else {
            binding.layoutProductReviewRegImage.visibility = View.GONE
        }
    }

    fun setImageRefresh(pos: Int) {
        binding.pagerProductReviewRegImage.currentItem = 0
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
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)

                val index = mImageAdapter!!.count
                for (i in 0 until dataList!!.size) {
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

        val eval = binding.ratingProductReviewReg.rating.toInt()

        if (eval < 1) {
            ToastUtil.show(this, R.string.msg_select_eval)
            return true
        }

        val contents = binding.editProductReviewRegContents.text.toString().trim()

        if (StringUtils.isEmpty(contents)) {
            ToastUtil.show(this, R.string.msg_input_review_contents)
            return true
        }

        if (contents.length < 10) {
            ToastUtil.show(this, R.string.msg_input_review_over_10)
            return true
        }
        return false
    }

    private fun isEditingData(): Boolean {

        val contents = binding.editProductReviewRegContents.text.toString().trim()

        if (StringUtils.isNotEmpty(contents)) {
            return true
        }

        val eval = binding.ratingProductReviewReg.rating.toInt()
        if (eval > 0) {
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
                    hideProgress()
                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                    mUploadDataMap[tag] = UploadStatus.ERROR_UPLOAD
                }

            })
        }

        s3Upload!!.request(key, paramsAttachment)

    }

    fun delete(url:String) {
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
                showProgress(getString(R.string.msg_registration_of_review))
            } else {
                showProgress(getString(R.string.msg_modification_of_review))
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

                    requestPost()
                }
            }

            uploadCheckThread = null
        }

        private fun showProgress(message: String) {

            runOnUiThread { this@OrderMenuReviewRegActivity.showProgress(message) }
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

        val contents = binding.editProductReviewRegContents.text.toString().trim()

        val params = OrderMenuReview()
        if (mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.size > 0) {
            val imageList = arrayListOf<ReviewImage>()
            var array = 0
            for (image in mImageAdapter!!.getDataList()!!) {
                array++
                val productReviewImage = ReviewImage()
                productReviewImage.array = array
                productReviewImage.image = image
                imageList.add(productReviewImage)
            }
            params.imageList = imageList
        }
        params.eval = binding.ratingProductReviewReg.rating.toInt()

        when (mPostMode) {

            EnumData.MODE.WRITE -> {

                params.memberSeqNo = LoginInfoManager.getInstance().user.no
                params.pageSeqNo = mOrderPurchase!!.pageSeqNo
                params.orderPurchaseSeqNo = mOrderPurchase!!.seqNo
                params.review = contents


                showProgress("")
                ApiBuilder.create().insertReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_registered_review_post)
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            EnumData.MODE.UPDATE -> {
                params.seqNo = mOrderMenuReview!!.seqNo
                params.memberSeqNo = mOrderMenuReview!!.memberSeqNo
                params.pageSeqNo = mOrderMenuReview!!.pageSeqNo
                params.orderPurchaseSeqNo = mOrderMenuReview!!.orderPurchaseSeqNo
                params.review = contents

                showProgress("")
                ApiBuilder.create().updateReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_modified_review)
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
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
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_review_write), ToolbarOption.ToolbarMenu.LEFT)
        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
        textRightTop.setText(R.string.word_reg)
        textRightTop.setClickable(true)
        textRightTop.setGravity(Gravity.CENTER)
        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb))
        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_48pt).toFloat())
        textRightTop.setSingleLine()
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        if (isEmptyData()) {
                            return
                        }

                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_question_review_write), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> update()
                                }
                            }
                        }).builder().show(this@OrderMenuReviewRegActivity)
                    }
                }
            }
        }
    }
}
