package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_page_modify.*
import network.common.PplusCallback
import retrofit2.Call

class PageModifyActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_modify
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val page = LoginInfoManager.getInstance().user.page!!
        edit_page_modify_name.setText(page.name)
        edit_page_modify_catchphrase.setText(page.catchphrase)

        Glide.with(this).load(page.backgroundImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_page_modify_background)

        text_page_modify_complete.setOnClickListener {

            val name = edit_page_modify_name.text.toString().trim()

            if (StringUtils.isEmpty(name)) {
                showAlert(getString(R.string.msg_input_page_name))
                return@setOnClickListener
            }

            val catchphrase = edit_page_modify_catchphrase.text.toString().trim()

            if (StringUtils.isEmpty(catchphrase)) {
                showAlert(R.string.msg_input_catchphrase)
                return@setOnClickListener
            }

            LoginInfoManager.getInstance().user.page!!.name = name
            LoginInfoManager.getInstance().user.page!!.catchphrase = catchphrase

            ApiBuilder.create().updatePage(LoginInfoManager.getInstance().user.page).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                    hideProgress()
                    LoginInfoManager.getInstance().save()
                    showAlert(R.string.msg_saved)
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                    hideProgress()
                }
            }).build().call()
        }

        image_page_modify_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
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
    }
}
