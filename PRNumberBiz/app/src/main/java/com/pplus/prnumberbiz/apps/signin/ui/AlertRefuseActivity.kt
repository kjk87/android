package com.pplus.prnumberbiz.apps.signin.ui

import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.setting.ui.InquiryActivity
import kotlinx.android.synthetic.main.activity_alert_refuse.*

class AlertRefuseActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_refuse
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val page = LoginInfoManager.getInstance().user.page!!

        text_alert_refuse_reason.text = page.reason

        image_alert_refuse_close.setOnClickListener {
            finish()
        }

        text_alert_refuse_inquiry.setOnClickListener {
            val intent = Intent(this, InquiryActivity::class.java)
            intent.putExtra(Const.KEY, Const.OTHER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
