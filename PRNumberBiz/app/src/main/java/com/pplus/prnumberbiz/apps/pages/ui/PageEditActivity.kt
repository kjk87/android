package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.permission.Permission
import com.pple.pplus.utils.part.apps.permission.PermissionListener
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity
import com.pplus.prnumberbiz.apps.menu.ui.MenuConfigActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.activity_page_edit.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class PageEditActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_edit
    }

    var mPage: Page? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        image_page_edit_back.setOnClickListener {
            onBackPressed()
        }
        image_page_edit_camera.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)

            var contents: Array<String>? = null
            contents = arrayOf(getString(R.string.word_select_default_image), getString(R.string.word_select_album))

            builder.setContents(*contents)
            builder.setLeftText(getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                            1 -> {
                                val intent = Intent(this@PageEditActivity, TemplateActivity::class.java)
                                startActivityForResult(intent, Const.REQ_BACKGROUND_DEFAULT_IMAGE)
                            }
                            2 -> {

                                val pPlusPermission = PPlusPermission(this@PageEditActivity)
                                pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
                                pPlusPermission.setPermissionListener(object : PermissionListener {

                                    override fun onPermissionGranted() {

                                        val intent = Intent(this@PageEditActivity, PhotoTakerActivity::class.java)
                                        intent.putExtra("mode", "picture")
                                        intent.putExtra("crop", true)
                                        startActivityForResult(intent, Const.REQ_BACKGROUND_IMAGE)
                                    }

                                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

                                    }
                                })
                                pPlusPermission.checkPermission()
                            }
                        }
                    }
                }
            }).builder().show(this)
        }
        image_page_edit_modify.setOnClickListener {
            val intent = Intent(this, PageModifyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
            overridePendingTransition(R.anim.fix, R.anim.fix)
        }

        text_page_edit_store_info.setOnClickListener {
            val intent = Intent(this, PageConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        text_page_edit_sns.setOnClickListener {
            val intent = Intent(this, SnsSyncActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SYNC_SNS)
        }
        text_page_edit_operation_info.setOnClickListener {
            val intent = Intent(this, OperationInfoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }
        text_page_edit_menu_config.setOnClickListener {
            val intent = Intent(this, MenuConfigActivity::class.java)
            intent.putExtra(Const.KEY, Const.REG)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        setData()
    }

    private fun setData() {
        mPage = LoginInfoManager.getInstance().user.page

//        if (mPage!!.type == EnumData.PageTypeCode.person.name && !mPage!!.isSeller!!) {
////        if (!mPage!!.isSeller!!) {
//            layout_page_edit_operation_info.visibility = View.GONE
////            layout_page_edit_menu_config.visibility = View.GONE
//            text_page_edit_store_info.text = getString(R.string.word_pr_info)
//            text_page_edit_store_info.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_edit_pr, 0, 0)
//        } else {
//            layout_page_edit_operation_info.visibility = View.VISIBLE
////            layout_page_edit_menu_config.visibility = View.VISIBLE
//            text_page_edit_store_info.text = getString(R.string.word_store_info)
//            text_page_edit_store_info.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_edit_store, 0, 0)
//        }

        layout_page_edit_operation_info.visibility = View.VISIBLE
//            layout_page_edit_menu_config.visibility = View.VISIBLE
        text_page_edit_store_info.text = getString(R.string.word_store_info)
        text_page_edit_store_info.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_edit_store, 0, 0)

        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
            text_page_edit_menu_config.setText(R.string.word_menu_config)
        } else {
            text_page_edit_menu_config.setText(R.string.word_goods_config)
        }

        Glide.with(this).load(mPage!!.backgroundImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_page_edit_background)
        text_page_edit_name.text = mPage!!.name
        text_page_edit_catchphrase.text = mPage!!.catchphrase

        if (mPage!!.usePrnumber!! && mPage!!.numberList != null && mPage!!.numberList!!.isNotEmpty()) {
            text_page_edit_number.text = PplusNumberUtil.getPrNumberFormat(mPage!!.numberList!![0].number)
        }

        getSnsLink()
    }

    private fun getSnsLink() {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage!!.no!!

        ApiBuilder.create().getSnsLinkAll(params).setCallback(object : PplusCallback<NewResultResponse<Sns>> {

            override fun onResponse(call: Call<NewResultResponse<Sns>>, response: NewResultResponse<Sns>) {

                image_page_edit_facebook.visibility = View.GONE
                image_page_edit_twitter.visibility = View.GONE
                image_page_edit_kakao.visibility = View.GONE
                image_page_edit_instagram.visibility = View.GONE
                image_page_edit_blog.visibility = View.GONE
                image_page_edit_homepage.visibility = View.GONE
                image_page_edit_kakaochannel.visibility = View.GONE
                image_page_edit_youtube.visibility = View.GONE

                val snsList = response.datas

                if (snsList != null && !snsList.isEmpty()) {
                    for (sns in snsList) {
                        if (StringUtils.isNotEmpty(sns.url)) {
                            when (sns.type) {

                                SnsTypeCode.twitter.name -> {
                                    image_page_edit_twitter.visibility = View.VISIBLE
                                    image_page_edit_twitter.tag = sns
                                    image_page_edit_twitter.setOnClickListener(onSnsClickListener)
                                }
//                                SnsTypeCode.facebook -> {
//                                    image_biz_main_facebook.visibility = View.VISIBLE
//                                    image_biz_main_facebook.tag = sns
//                                    image_biz_main_facebook.setOnClickListener(onSnsClickListener)
//                                }
                                SnsTypeCode.instagram.name -> {
                                    image_page_edit_instagram.visibility = View.VISIBLE
                                    image_page_edit_instagram.tag = sns
                                    image_page_edit_instagram.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.kakao.name -> {
                                    image_page_edit_kakao.visibility = View.VISIBLE
                                    image_page_edit_kakao.tag = sns
                                    image_page_edit_kakao.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.blog.name -> {
                                    image_page_edit_blog.visibility = View.VISIBLE
                                    image_page_edit_blog.isSelected = true
                                    image_page_edit_blog.tag = sns
                                    image_page_edit_blog.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.homepage.name -> {
                                    image_page_edit_homepage.visibility = View.VISIBLE
                                    image_page_edit_homepage.tag = sns
                                    image_page_edit_homepage.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.kakaochannel.name -> {
                                    image_page_edit_kakaochannel.visibility = View.VISIBLE
                                    image_page_edit_kakaochannel.tag = sns
                                    image_page_edit_kakaochannel.setOnClickListener(onSnsClickListener)
                                }
                                SnsTypeCode.youtube.name -> {
                                    image_page_edit_youtube.visibility = View.VISIBLE
                                    image_page_edit_youtube.tag = sns
                                    image_page_edit_youtube.setOnClickListener(onSnsClickListener)
                                }
                            }
                        }
                    }
                }

//                mAdapter!!.setSNS(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Sns>>, t: Throwable, response: NewResultResponse<Sns>) {

            }
        }).build().call()
    }

    private val onSnsClickListener = View.OnClickListener { v ->
        val sns = v.tag as Sns
        snsEvent(sns)
    }

    private fun snsEvent(sns: Sns) {
        // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) {
            // 계정으로 이동
            startActivity(PplusCommonUtil.getOpenSnsIntent(this, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_BACKGROUND_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val selectImage = data.data
                        LogUtil.e(LOG_TAG, "file : {}", selectImage!!.path)
                        apiPageBackgroundUpload(selectImage.path!!)
                    }
                }
            }
            Const.REQ_BACKGROUND_DEFAULT_IMAGE -> {
                if (data != null) {
                    val attachment = data.getParcelableExtra<Attachment>(Const.DATA)
                    LoginInfoManager.getInstance().user.page!!.backgroundImage = ImgUrl(attachment.no, attachment.url)
                    LoginInfoManager.getInstance().save()
                    mPage = LoginInfoManager.getInstance().user.page
                    updateBackground("" + attachment.no!!)
                }
            }

            Const.REQ_SET_PAGE -> {
                setData()
            }
            Const.REQ_SYNC_SNS -> {
                getSnsLink()
            }
        }
    }

    private var defaultUpload: DefaultUpload? = null
    private fun apiPageBackgroundUpload(filepath: String) {

        val attachment = ParamsAttachment()
        attachment.targetType = AttachmentTargetTypeCode.pageBackground
        attachment.setFile(filepath)
        attachment.targetNo = mPage!!.no

        if (defaultUpload == null) {
            defaultUpload = DefaultUpload(object : PplusUploadListener<Attachment> {

                override fun onResult(tag: String, resultResponse: NewResultResponse<Attachment>) {

                    hideProgress()
                    val url = resultResponse.data.url
                    val no = resultResponse.data.no

                    LoginInfoManager.getInstance().user.page!!.backgroundImage = ImgUrl(no, url)
                    LoginInfoManager.getInstance().save()
                    mPage = LoginInfoManager.getInstance().user.page
                    updateBackground("" + no!!)
                    PplusCommonUtil.deleteFromMediaScanner(filepath)
                }

                override fun onFailure(tag: String, resultResponse: NewResultResponse<*>) {

                    LogUtil.e(LOG_TAG, "onFailure")
                    hideProgress()
                    PplusCommonUtil.deleteFromMediaScanner(filepath)
                }
            })
        }

        showProgress("")
        defaultUpload!!.request(filepath, attachment)
    }

    private fun updateBackground(no: String?) {

        val params = HashMap<String, String>()
        params["no"] = "" + mPage!!.no!!
        if (StringUtils.isNotEmpty(no)) {
            params["backgroundImage.no"] = no!!
        }
        showProgress("")
        ApiBuilder.create().updatePageBackgroundImage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()

                setData()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
            }
        }).build().call()
    }
}
