package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.viewpager.widget.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.common.DatePickerActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsRegImagePagerAdapter
import com.pplus.prnumberbiz.apps.menu.ui.MenuConfigActivity
import com.pplus.prnumberbiz.apps.post.data.UploadStatus
import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.GoodsAttachments
import com.pplus.prnumberbiz.core.network.model.dto.PageManagement
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_goods_reg2.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GoodsRegActivity2 : BaseActivity() {
    override fun getPID(): String {

        val mode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        if (mode == EnumData.MODE.UPDATE) {
            return "Main_product edit"
        } else {
            return "Main_make product"
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_reg2
    }

    var mMode = EnumData.MODE.WRITE
    var mGoods: Goods? = null
    var mExpireDate = ""
    var mStartTime = ""
    var mEndTime = ""
    var mType = EnumData.GoodsType.hotdeal.name

    private var mImageAdapter: GoodsRegImagePagerAdapter? = null
    private var defaultUpload: DefaultUpload? = null
    private var uploadCheckThread: UploadCheckThread? = null
    private lateinit var mUploadDataMap: ConcurrentHashMap<String, UploadStatus>
    private lateinit var mUploadUrlMap: ConcurrentHashMap<String, String>

    override fun initializeView(savedInstanceState: Bundle?) {
        mMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        mType = intent.getStringExtra(Const.TYPE)

        edit_goods_reg_name.setSingleLine()
        edit_goods_reg_origin_price.setSingleLine()
        edit_goods_reg_sale_price.setSingleLine()

        mUploadDataMap = ConcurrentHashMap()
        mUploadUrlMap = ConcurrentHashMap()

        mImageAdapter = GoodsRegImagePagerAdapter(this)
        pager_goods_reg_image.adapter = mImageAdapter
        pager_goods_reg_image.offscreenPageLimit = 10
        pager_goods_reg_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                indicator_goods_reg.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        image_goods_reg_back.setOnClickListener {
            onBackPressed()
        }

        layout_goods_reg_add_image.setOnClickListener {
            goPostGallery()
        }

        layout_goods_reg_add_image2.setOnClickListener {
            goPostGallery()
        }

        edit_goods_reg_desc.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (edit_goods_reg_desc.length() > 0) {
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        text_goods_reg_discount_rate.text = PplusCommonUtil.fromHtml(getString(R.string.html_discount, "0"))


        if (mType == EnumData.GoodsType.hotdeal.name) {
//            layout_goods_reg_prlink_reward.visibility = View.VISIBLE
            layout_goods_reg_expire_date.visibility = View.GONE
            text_goods_reg_title.setText(R.string.word_reg_hot_deal)
            text_goods_reg_caution.setText(R.string.msg_hot_deal_reg_caution)
            text_goods_reg_option_title.setText(R.string.word_reg_hot_deal_option_title)
        } else {
//            layout_goods_reg_prlink_reward.visibility = View.GONE
            layout_goods_reg_expire_date.visibility = View.VISIBLE
            text_goods_reg_title.setText(R.string.word_reg_plus_goods)
            text_goods_reg_caution.setText(R.string.msg_plus_goods_reg_caution)
            text_goods_reg_option_title.setText(R.string.word_reg_plus_goods_option_title)
        }

        if (mMode == EnumData.MODE.UPDATE) {

            text_goods_reg2.setText(R.string.msg_modify_goods)
            text_goods_reg.setText(R.string.word_modified)

            mGoods = intent.getParcelableExtra<Goods>(Const.DATA)
            if (mGoods != null) {

                if (StringUtils.isNotEmpty(mGoods!!.name)) {
                    edit_goods_reg_name.setText(mGoods!!.name)
                }

                if (mGoods!!.price != null) {
                    edit_goods_reg_sale_price.setText(mGoods!!.price!!.toString())
                }

                if (StringUtils.isNotEmpty(mGoods!!.description)) {
                    edit_goods_reg_desc.setText(mGoods!!.description)
                }

                edit_goods_reg_origin_price.setText(mGoods!!.originPrice.toString())

                if (mGoods!!.count != null && mGoods!!.count != -1) {
                    text_goods_reg_count.text = mGoods!!.count.toString()
                }

                if (StringUtils.isNotEmpty(mGoods!!.expireDatetime)) {

                    mExpireDate = mGoods!!.expireDatetime!!

                    text_goods_reg_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mGoods!!.expireDatetime)) + " " + getString(R.string.word_until)
                }
                if (StringUtils.isNotEmpty(mGoods!!.startTime) && StringUtils.isNotEmpty(mGoods!!.endTime)) {

                    mStartTime = mGoods!!.startTime!!
                    mEndTime = mGoods!!.endTime!!

                    text_goods_reg_use_time.text = mGoods!!.startTime + "~" + mGoods!!.endTime
                }

                if (mGoods!!.rewardPrReviewLink != null) {
                    edit_goods_reg_reward.setText(mGoods!!.rewardPrReviewLink.toString())
                }

//                if (mGoods!!.rewardPrLink != null) {
//                    edit_goods_reg_prlink_reward.setText(mGoods!!.rewardPrLink.toString())
//                }

                if (mGoods!!.attachments != null && mGoods!!.attachments!!.images != null && mGoods!!.attachments!!.images!!.isNotEmpty()) {
                    mImageAdapter!!.mDataList = mGoods!!.attachments!!.images!! as ArrayList<String>
                    mImageAdapter!!.notifyDataSetChanged()

                    indicator_goods_reg.visibility = View.VISIBLE
                    indicator_goods_reg.removeAllViews()
                    indicator_goods_reg.build(LinearLayout.HORIZONTAL, mGoods!!.attachments!!.images!!.size)
                }

                val percent = 100 - (mGoods!!.price!!.toFloat() / mGoods!!.originPrice.toString().toFloat() * 100).toInt()
                text_goods_reg_discount_rate.text = PplusCommonUtil.fromHtml(getString(R.string.html_discount, percent.toString()))
            }
        } else {

            getGoodsCount()

            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, 1)
            val date = "${cal.get(Calendar.YEAR)}-${DateFormatUtils.formatTime(cal.get(Calendar.MONTH) + 1)}-${DateFormatUtils.formatTime(cal.get(Calendar.DAY_OF_MONTH))}"
            mExpireDate = date + " 23:59:59"

            text_goods_reg_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mExpireDate)) + " " + getString(R.string.word_until)

            val params = HashMap<String, String>()
            params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
            showProgress("")
            ApiBuilder.create().getPageManagement(params).setCallback(object : PplusCallback<NewResultResponse<PageManagement>> {
                override fun onResponse(call: Call<NewResultResponse<PageManagement>>?, response: NewResultResponse<PageManagement>?) {
                    hideProgress()
                    if (response != null) {
                        val openTimeList = response.data.opentimeList
                        if (openTimeList != null && openTimeList.isNotEmpty()) {

                            if(StringUtils.isNotEmpty(openTimeList[0].startTime) && StringUtils.isNotEmpty(openTimeList[0].endTime)){
                                mStartTime = openTimeList[0].startTime!!
                                mEndTime = openTimeList[0].endTime!!
                                text_goods_reg_use_time.text = mStartTime + "~" + mEndTime
                            }

                        }

                    }
                }

                override fun onFailure(call: Call<NewResultResponse<PageManagement>>?, t: Throwable?, response: NewResultResponse<PageManagement>?) {
                    hideProgress()
                }
            }).build().call()
        }

        layout_goods_reg_expire_date.setOnClickListener {

            val intent = Intent(this, DatePickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_EXPIRE_DATE)
        }

        mImageAdapter!!.setOnItemChangeListener(object : GoodsRegImagePagerAdapter.OnItemChangeListener {
            override fun onChange() {
                checkImage()
            }
        })
        text_goods_reg2.setOnClickListener {
            reg()
        }

        text_goods_reg.setOnClickListener {
            reg()
        }

        edit_goods_reg_origin_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val sale_price = edit_goods_reg_sale_price.text.toString().trim()
                    if (StringUtils.isNotEmpty(sale_price)) {
                        if (s.toString().toInt() > sale_price.toInt()) {
                            text_goods_reg_discount_rate.visibility = View.VISIBLE
                            LogUtil.e(LOG_TAG, "{}", sale_price.toFloat() / s.toString().toFloat())
                            val percent = 100 - ((sale_price.toFloat() / s.toString().toFloat()) * 100f).toInt()
                            text_goods_reg_discount_rate.text = PplusCommonUtil.fromHtml(getString(R.string.html_discount, percent.toString()))
                        } else {
                            text_goods_reg_discount_rate.visibility = View.INVISIBLE
                        }
                    } else {
                        text_goods_reg_discount_rate.visibility = View.INVISIBLE
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_goods_reg_sale_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val origin_price = edit_goods_reg_origin_price.text.toString().trim()
                    if (StringUtils.isNotEmpty(origin_price)) {
                        if (s.toString().toInt() < origin_price.toInt()) {
                            text_goods_reg_discount_rate.visibility = View.VISIBLE
                            val percent = 100 - ((s.toString().toFloat() / origin_price.toFloat()) * 100f).toInt()
                            text_goods_reg_discount_rate.text = PplusCommonUtil.fromHtml(getString(R.string.html_discount, percent.toString()))
                        } else {
                            text_goods_reg_discount_rate.visibility = View.INVISIBLE
                        }
                    } else {
                        text_goods_reg_discount_rate.visibility = View.INVISIBLE
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        layout_goods_reg_count.setOnClickListener {
            val res = arrayOfNulls<String>(100)
            for (i in 0 until res.size) {
                res[i] = (i + 1).toString()
            }
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_select))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setContents(*res)
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST -> if (event_alert == AlertBuilder.EVENT_ALERT.LIST) {
                            val value = event_alert.getValue()
                            text_goods_reg_count.text = value.toString()
                        }
                    }
                }
            }).builder().show(this)
        }

        layout_goods_reg_use_time.setOnClickListener {
            val intent = Intent(this, UseTimePickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_TIME)
        }

        checkImage()

        getPageManagement()
    }

    private fun getGoodsCount() {

        val params = HashMap<String, String>()

        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
        params["status"] = EnumData.GoodsStatus.ing.status.toString()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["isHotdeal"] = "false"
        params["isPlus"] = "false"
        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params["type"] = "0"
        } else {
            params["type"] = "1"
        }

        params["page"] = "0"
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                if (response != null) {

                    val goodsCount = response.data.totalElements!!

                    if(goodsCount > 0){
                        val intent = Intent(this@GoodsRegActivity2, MenuConfigActivity::class.java)
                        intent.putExtra(Const.KEY, Const.SELECT)
                        intent.putExtra(Const.TYPE, mType)
                        startActivityForResult(intent, Const.REQ_SELECT)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
            }
        }).build().call()
    }

    var mPageManagement: PageManagement? = null
    private fun getPageManagement() {
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPageManagement(params).setCallback(object : PplusCallback<NewResultResponse<PageManagement>> {


            override fun onResponse(call: Call<NewResultResponse<PageManagement>>?, response: NewResultResponse<PageManagement>?) {
                hideProgress()
                mPageManagement = response!!.data
            }

            override fun onFailure(call: Call<NewResultResponse<PageManagement>>?, t: Throwable?, response: NewResultResponse<PageManagement>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun reg() {
        if (isEmptyData()) {
            return
        }

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        when (mMode) {
            EnumData.MODE.WRITE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_goods_reg), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
            EnumData.MODE.UPDATE -> {
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_goods_modify), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            }
        }

        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {

                        val price = edit_goods_reg_sale_price.text.toString().trim()

                        if(mPageManagement != null && mPageManagement!!.deliveryMinPrice != null && mPageManagement!!.deliveryMinPrice!! > 0){
                            if (price.toInt() < mPageManagement!!.deliveryMinPrice!!.toInt()) {
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_min_delivery_price), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                builder.setOnAlertResultListener(object : OnAlertResultListener {
                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                update()
                                            }
                                        }
                                    }
                                }).builder().show(this@GoodsRegActivity2)
                            }else{
                                update()
                            }
                        }else{
                            update()
                        }

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
                        mImageAdapter!!.add("")
                        mUploadDataMap[(index + i).toString()] = UploadStatus.UPLOAD
                        mUploadUrlMap[(index + i).toString()] = dataList[i].path
                    }

                    showProgress("")
                    for (i in 0 until dataList.size) {
                        uploadFile((index + i).toString(), dataList[i].path)
                    }

                    checkImage()
                }
                Const.REQ_EXPIRE_DATE -> if (data != null) {

                    val expireDate = data.getStringExtra(Const.DATA)
                    mExpireDate = expireDate + " 23:59:59"
                    text_goods_reg_expire_date.text = expireDate.replace("-", ".") + " " + getString(R.string.word_until)

                }
                Const.REQ_TIME -> {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            val start = data.getStringExtra(Const.START)
                            val end = data.getStringExtra(Const.END)
                            mStartTime = start
                            mEndTime = end
                            text_goods_reg_use_time.text = start + "~" + end

                        }
                    }
                }
                Const.REQ_SELECT->{
                    if(data != null){
                        val goods = data.getParcelableExtra<Goods>(Const.GOODS)

                        if (StringUtils.isNotEmpty(goods!!.name)) {
                            edit_goods_reg_name.setText(goods.name)
                        }

                        if (StringUtils.isNotEmpty(goods.description)) {
                            edit_goods_reg_desc.setText(goods.description)
                        }

                        edit_goods_reg_origin_price.setText(goods.originPrice.toString())


                        if (goods.attachments != null && goods.attachments!!.images != null && goods.attachments!!.images!!.isNotEmpty()) {
                            mImageAdapter!!.mDataList = goods.attachments!!.images!! as ArrayList<String>
                            mImageAdapter!!.notifyDataSetChanged()

                            indicator_goods_reg.visibility = View.VISIBLE
                            indicator_goods_reg.removeAllViews()
                            indicator_goods_reg.build(LinearLayout.HORIZONTAL, goods.attachments!!.images!!.size)
                        }
                        checkImage()
                    }
                }
            }
        }
    }

    fun checkImage() {
        if (mImageAdapter!!.count > 0) {
            pager_goods_reg_image.visibility = View.VISIBLE
            indicator_goods_reg.visibility = View.VISIBLE
            indicator_goods_reg.removeAllViews()
            indicator_goods_reg.build(LinearLayout.HORIZONTAL, mImageAdapter!!.count)
            indicator_goods_reg.setCurrentItem(pager_goods_reg_image.currentItem)
            layout_goods_reg_add_image.visibility = View.GONE
            layout_goods_reg_add_image2.visibility = View.VISIBLE
        } else {
            pager_goods_reg_image.visibility = View.GONE
            indicator_goods_reg.visibility = View.GONE
            layout_goods_reg_add_image.visibility = View.VISIBLE
            layout_goods_reg_add_image2.visibility = View.GONE
        }
    }

    private fun uploadFile(key: String, url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.goods
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

            runOnUiThread { this@GoodsRegActivity2.showProgress(message) }
        }
    }

    private fun isEmptyData(): Boolean {

        val name = edit_goods_reg_name.text.toString().trim()

        if (StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_goods_name)
            return true
        }

        val origin_price = edit_goods_reg_origin_price.text.toString().trim()

        if (StringUtils.isEmpty(origin_price)) {
            showAlert(R.string.msg_input_goods_origin_price)
            return true
        }

        val price = edit_goods_reg_sale_price.text.toString().trim()

        if (StringUtils.isEmpty(price)) {
            showAlert(R.string.msg_input_goods_sale_price)
            return true
        }

        if (origin_price.toInt() < price.toInt()) {
            showAlert(R.string.msg_sale_price_over)
            return true
        }

        if (price.toInt() < 1000) {
            showAlert(R.string.msg_input_over_1000)
            return true
        }

        val contents = edit_goods_reg_desc.text.toString().trim()

        if (StringUtils.isEmpty(contents)) {
            showAlert(R.string.mgs_input_goods_contents)
            return true
        }

        if (contents.length < 10) {
            showAlert(getString(R.string.format_msg_goods_input_over, 10))
            return true
        }

        if ((mImageAdapter!!.getDataList() == null || mImageAdapter!!.getDataList()!!.isEmpty())) {
            showAlert(R.string.msg_reg_photo)
            return true
        }

        val count = text_goods_reg_count.text.toString().trim()

        if (mType == EnumData.GoodsType.hotdeal.name) {

            if (StringUtils.isEmpty(count)) {
                showAlert(R.string.msg_input_count)
                return true
            }
        }

        if (StringUtils.isNotEmpty(text_goods_reg_count.text.toString().trim())) {

            if (mGoods != null && mGoods!!.count!! > count.toInt()) {
                showAlert(R.string.msg_can_not_set_count_small_current)
                return true
            }
        }

        if (mType == EnumData.GoodsType.plus.name) {
            if (StringUtils.isEmpty(mExpireDate)) {
                showAlert(R.string.msg_select_goods_expire_date)
                return true
            }

            if (StringUtils.isNotEmpty(mExpireDate)) {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mExpireDate)
                if (System.currentTimeMillis() >= d.time) {
                    showAlert(R.string.msg_can_not_set_date_before_current)
                    return true
                }
            }
        }


        if (StringUtils.isEmpty(mStartTime) || StringUtils.isEmpty(mEndTime)) {
            showAlert(R.string.msg_select_use_time)
            return true
        }

//        if (mType == EnumData.GoodsType.hotdeal.name) {
//            val prLinkReward = edit_goods_reg_prlink_reward.text.toString().trim()
//
//            if (StringUtils.isEmpty(prLinkReward)) {
//                showAlert(R.string.msg_input_prlink_reward)
//                return true
//            }
//
//            if(prLinkReward.toInt() >= price.toInt()){
//                showAlert(R.string.msg_under_prlink_reward_price)
//                return true
//            }
//        }


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
        val params = Goods()

        params.pageSeqNo = LoginInfoManager.getInstance().user.page!!.no

        val name = edit_goods_reg_name.text.toString().trim()

        params.name = name

        val origin_price = edit_goods_reg_origin_price.text.toString().trim()
        params.originPrice = origin_price.toLong()


        val price = edit_goods_reg_sale_price.text.toString().trim()

        params.price = price.toLong()

        val desc = edit_goods_reg_desc.text.toString().trim()
        params.description = desc

        params.startTime = mStartTime
        params.endTime = mEndTime


        var count = -1

        if (StringUtils.isNotEmpty(text_goods_reg_count.text.toString().trim())) {
            count = text_goods_reg_count.text.toString().trim().toInt()
            params.count = count
        }



        if (mType == EnumData.GoodsType.hotdeal.name) {

//            val prLinkReward = edit_goods_reg_prlink_reward.text.toString().trim()
//            params.rewardPrLink = prLinkReward.toLong()

            params.isHotdeal = true
        } else if (mType == EnumData.GoodsType.plus.name) {
            params.isPlus = true
            if (StringUtils.isNotEmpty(mExpireDate)) {

                params.expireDatetime = mExpireDate
            }
        }

        if (StringUtils.isNotEmpty(edit_goods_reg_reward.text.toString().trim())) {
            val reward = edit_goods_reg_reward.text.toString().trim().toLong()
            params.rewardPrReviewLink = reward
        }

        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params.type = "0"
        } else {
            params.type = "1"
        }

        when (mMode) {
            EnumData.MODE.WRITE -> {

                if (mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.size > 0) {
                    val attachments = GoodsAttachments()
                    attachments.images = mImageAdapter!!.getDataList()
                    params.attachments = attachments
                }

                showProgress(getString(R.string.msg_registration_of_goods))

                ApiBuilder.create().postGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        showAlert(R.string.msg_registed_goods)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
            EnumData.MODE.UPDATE -> {

                params.seqNo = mGoods!!.seqNo
                if (mGoods!!.category != null) {
                    params.categorySeqNo = mGoods!!.category!!.seqNo
                }

                val attachments = GoodsAttachments()
                attachments.images = mImageAdapter!!.getDataList()
                params.attachments = attachments

                showProgress(getString(R.string.msg_modification_of_goods))
                ApiBuilder.create().putGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                        hideProgress()
                        showAlert(R.string.msg_modified_goods)
                        val data = Intent()
                        data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
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
        val name = edit_goods_reg_name.text.toString().trim()

        if (StringUtils.isNotEmpty(name)) {
            return true
        }

        val origin_price = edit_goods_reg_origin_price.text.toString().trim()

        if (StringUtils.isNotEmpty(origin_price)) {
            return true
        }

        val sale_price = edit_goods_reg_sale_price.text.toString().trim()

        if (StringUtils.isNotEmpty(sale_price)) {
            return true
        }

        val contents = edit_goods_reg_desc.text.toString().trim()

        if (StringUtils.isNotEmpty(contents)) {
            return true
        }

        if ((mImageAdapter!!.getDataList() != null && mImageAdapter!!.getDataList()!!.isNotEmpty())) {
            return true
        }

//        if (StringUtils.isNotEmpty(edit_goods_reg_count.text.toString().trim())) {
//            return true
//        }

//        if (StringUtils.isNotEmpty(mExpireDate)) {
//            return true
//        }

        return false
    }

}
