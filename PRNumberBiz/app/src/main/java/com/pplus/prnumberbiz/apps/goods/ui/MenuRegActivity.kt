package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.viewpager.widget.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
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
import com.pplus.prnumberbiz.apps.goods.data.GoodsImagePagerAdapter
import com.pplus.prnumberbiz.apps.goods.data.GoodsRegImagePagerAdapter
import com.pplus.prnumberbiz.apps.post.data.UploadStatus
import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.GoodsAttachments
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_menu_reg.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MenuRegActivity : BaseActivity() {
    override fun getPID(): String {

        val mode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE
        if(mode == EnumData.MODE.UPDATE){
            return "Main_product edit"
        }else{
            return "Main_make product"
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_menu_reg
    }

    var mMode = EnumData.MODE.WRITE
    var mGoods: Goods? = null
    var mImageId:String? = null
    var mCategoryList: List<PageGoodsCategory>? = null
    var mSelectCategory:PageGoodsCategory? = null

//    private var mImageAdapter: GoodsRegImagePagerAdapter? = null
    private var defaultUpload: DefaultUpload? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mMode = intent.getSerializableExtra(Const.MODE) as EnumData.MODE

        edit_menu_reg_name.setSingleLine()
        edit_menu_reg_price.setSingleLine()


        image_menu_reg_back.setOnClickListener {
            onBackPressed()
        }

        image_menu_reg_image.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
            builder.setLeftText(getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST ->{
                            when(event_alert.value){
                                1->{
                                    goPostGallery()
                                }
                                2->{
                                    mImageId = null
                                    setImage()
                                }
                            }

                        }
                    }
                }
            }).builder().show(this)

        }

        layout_menu_reg_add_image.setOnClickListener {
            goPostGallery()
        }

        if (mMode == EnumData.MODE.UPDATE) {

            text_menu_reg2.setText(R.string.msg_modify_goods)
            text_menu_reg.setText(R.string.word_modified)

            mGoods = intent.getParcelableExtra<Goods>(Const.DATA)
            if (mGoods != null) {

                if (StringUtils.isNotEmpty(mGoods!!.name)) {
                    edit_menu_reg_name.setText(mGoods!!.name)
                }

                if (mGoods!!.price != null) {
                    edit_menu_reg_price.setText(mGoods!!.price!!.toString())
                }

                if (mGoods!!.attachments != null && mGoods!!.attachments!!.images != null && mGoods!!.attachments!!.images!!.isNotEmpty()) {
                    mImageId = mGoods!!.attachments!!.images!![0]

                }

                text_menu_reg_category.text = mGoods!!.category!!.name

            }
        }

        text_menu_reg_category.setOnClickListener {
            if(mCategoryList != null){

                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_category_select))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                for (i in mCategoryList!!.indices) {

                    val messageData = AlertData.MessageData()
                    messageData.contents = mCategoryList!![i].goodsCategory!!.name
                    builder.addContents(messageData)
                }
//                builder.setContents(*mCategoryList!!)
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {

                            AlertBuilder.EVENT_ALERT.LIST -> if (event_alert == AlertBuilder.EVENT_ALERT.LIST) {
                                val value = event_alert.getValue()
                                LogUtil.e(LOG_TAG, "value = {}", event_alert.getValue())
                                mSelectCategory = mCategoryList!![value-1]
                                text_menu_reg_category.text = mSelectCategory!!.goodsCategory!!.name

                            }
                        }
                    }
                }).builder().show(this)
            }
        }

        text_menu_reg2.setOnClickListener {
            reg()
        }

        text_menu_reg.setOnClickListener {
            reg()
        }
        setImage()
        getCategory()
    }

    private fun getCategory() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["depth"] = "1"
        showProgress("")
        ApiBuilder.create().getPageGoodsCategory(params).setCallback(object : PplusCallback<NewResultResponse<PageGoodsCategory>> {
            override fun onResponse(call: Call<NewResultResponse<PageGoodsCategory>>?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
                if (response != null) {
                    mCategoryList = response.datas
                    if (mGoods != null) {
                        for(category in mCategoryList!!){
                            if(category.goodsCategory!!.seqNo == mGoods!!.category!!.seqNo){
                                mSelectCategory = category
                            }
                        }
                    }else{
                        mSelectCategory = mCategoryList!![0]
                        text_menu_reg_category.text = mSelectCategory!!.goodsCategory!!.name
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageGoodsCategory>>?, t: Throwable?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setImage(){
        if(StringUtils.isNotEmpty(mImageId)){
            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${mImageId}")
            Glide.with(this).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_menu_reg_image)
            image_menu_reg_image.visibility = View.VISIBLE
            layout_menu_reg_add_image.visibility = View.GONE
        }else{
            image_menu_reg_image.visibility = View.GONE
            layout_menu_reg_add_image.visibility = View.VISIBLE
        }
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
                    AlertBuilder.EVENT_ALERT.RIGHT -> update()
                }
            }
        }).builder().show(this)
    }

    private fun goPostGallery() {

        val intent = Intent(this, PostGalleryActivity::class.java)
        intent.putExtra(Const.COUNT, 1)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, Const.REQ_GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Const.REQ_GALLERY -> if (data != null) {
                    val dataList = data.getParcelableArrayListExtra<Uri>(Const.CROPPED_IMAGE)

                    showProgress("")
                    uploadFile(dataList[0].path)
                }
            }
        }
    }

    private fun uploadFile(url: String) {

        val paramsAttachment = ParamsAttachment()
        paramsAttachment.targetType = AttachmentTargetTypeCode.goods
        paramsAttachment.setFile(url)

        if (defaultUpload == null) {
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {
                    LogUtil.e(LOG_TAG, "tag = {}, no = {}, id = {}", tag, resultResponse.data.no, resultResponse.data.id)

                    hideProgress()
                    mImageId = resultResponse.data.id
                    PplusCommonUtil.deleteFromMediaScanner(url)
                    setImage()

                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString())
                }

            })
        }

        defaultUpload!!.request(url, paramsAttachment)

    }

    /**
     * 게시글 등록/수정
     */
    fun update() {

        if (isEmptyData()) {
            return
        }

        postGoods()

    }


    private fun isEmptyData(): Boolean {

        val name = edit_menu_reg_name.text.toString().trim()

        if (StringUtils.isEmpty(name)) {
            showAlert(R.string.msg_input_goods_name)
            return true
        }

        val price = edit_menu_reg_price.text.toString().trim()

        if (StringUtils.isEmpty(price)) {
            showAlert(R.string.msg_input_goods_sale_price)
            return true
        }

        if (price.toInt() < 1000) {
            showAlert(R.string.msg_input_over_1000)
            return true
        }
        return false
    }

    private fun postGoods() {
        val params = Goods()

        val page = LoginInfoManager.getInstance().user.page!!
        params.pageSeqNo = page.no

        val name = edit_menu_reg_name.text.toString().trim()

        params.name = name
        params.description = name


        val price = edit_menu_reg_price.text.toString().trim()
        params.price = price.toLong()
        params.originPrice = price.toLong()
        if (page.type == EnumData.PageTypeCode.store.name) {
            params.type = "0"
        }else{
            params.type = "1"
        }

        params.categorySeqNo = mSelectCategory!!.goodsCategory!!.seqNo


        when (mMode) {
            EnumData.MODE.WRITE -> {

                if (StringUtils.isNotEmpty(mImageId)) {
                    val attachments = GoodsAttachments()
                    attachments.images = arrayListOf(mImageId!!)
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
//                if (mGoods!!.category != null) {
//                    params.categorySeqNo = mGoods!!.category!!.seqNo
//                }

                if (StringUtils.isNotEmpty(mImageId)) {
                    val attachments = GoodsAttachments()
                    attachments.images = arrayListOf(mImageId!!)
                    params.attachments = attachments
                }


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
        val name = edit_menu_reg_name.text.toString().trim()

        if (StringUtils.isNotEmpty(name)) {
            return true
        }

        val sale_price = edit_menu_reg_price.text.toString().trim()

        if (StringUtils.isNotEmpty(sale_price)) {
            return true
        }

        if (StringUtils.isNotEmpty(mImageId)) {
            return true
        }

        return false
    }

}
