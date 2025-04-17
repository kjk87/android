package com.pplus.prnumberbiz.apps.main.ui

import android.app.Activity
import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.PageOpenBoundsCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_alert_secret_mode.*
import network.common.PplusCallback
import retrofit2.Call

class AlertSecretModeActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_secret_mode
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val page = LoginInfoManager.getInstance().user.page!!
        when (page.openBound) {
            PageOpenBoundsCode.everybody.name -> {
                image_alert_secret_mode.setImageResource(R.drawable.ic_main_popup_secret_off)
                text_alert_secret_mode1.setText(R.string.msg_secret_mode_off)
                text_alert_secret_mode2.setText(R.string.msg_secret_mode_off_description1)
                text_alert_secret_mode3.setText(R.string.msg_secret_mode_off_description2)
            }
            else -> {
                image_alert_secret_mode.setImageResource(R.drawable.ic_main_popup_secret_on)
                text_alert_secret_mode1.setText(R.string.msg_secret_mode_on)
                text_alert_secret_mode2.setText(R.string.msg_secret_mode_on_description1)
                text_alert_secret_mode3.setText(R.string.msg_secret_mode_on_description2)
            }
        }


        text_alert_secret_mode_cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        text_alert_secret_mode_confirm.setOnClickListener {
            when (page.openBound) {
                PageOpenBoundsCode.everybody.name -> {
                    page.openBound = PageOpenBoundsCode.nobody.name
                }
                else -> {
                    page.openBound = PageOpenBoundsCode.everybody.name
                }
            }
            update(page)
        }
    }

    private fun update(page: Page) {
        showProgress("")
        ApiBuilder.create().updatePage(page).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {

                hideProgress()
                LoginInfoManager.getInstance().user.page = page
                LoginInfoManager.getInstance().save()
                showAlert(R.string.msg_saved)
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                hideProgress()
            }
        }).build().call()
    }

}
