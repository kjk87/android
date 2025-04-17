package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_alert_page_link.*
import network.common.PplusCallback
import retrofit2.Call

class AlertPageLinkActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_page_link
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        val isLink = intent.getBooleanExtra(Const.IS_LINK, false)

        val page = LoginInfoManager.getInstance().user.page!!

        if(isLink){
            text_alert_page_link_title.setText(R.string.word_link_hompage)
            text_alert_page_link_desc.setText(R.string.msg_link_url_desc)
            image_alert_page_link.setImageResource(R.drawable.img_popup_url_info)
            edit_alert_page_link_url.visibility = View.VISIBLE

            if (StringUtils.isNotEmpty(page.homepageLink)) {
                edit_alert_page_link_url.setText(page.homepageLink)
            }
        }else{
            text_alert_page_link_title.setText(R.string.word_link_page)
            text_alert_page_link_desc.setText(R.string.msg_link_page_desc)
            image_alert_page_link.setImageResource(R.drawable.img_popup_prpage_info)
            edit_alert_page_link_url.visibility = View.GONE


        }

        image_alert_page_link_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        text_alert_page_link_save.setOnClickListener {
            page.isLink = isLink


            if(isLink){

                val url = edit_alert_page_link_url.text.toString().trim()

                if (StringUtils.isEmpty(url)) {
                    showAlert(R.string.msg_input_link_url)
                    return@setOnClickListener
                }

                if (!URLUtil.isValidUrl(url)) {
                    showAlert(R.string.msg_invalid_url)
                    return@setOnClickListener
                }

                page.homepageLink = url

            }

            showProgress("")
            ApiBuilder.create().updatePage(page).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                    LoginInfoManager.getInstance().user.page = response!!.data
                    LoginInfoManager.getInstance().save()
                    hideProgress()
                    showAlert(R.string.msg_saved)
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                    hideProgress()
                }
            }).build().call()
        }
    }

}
