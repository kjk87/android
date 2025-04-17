package com.pplus.prnumberbiz.apps.sms

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.ui.SmsFragment

class SmsSendActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sms_send
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.sms_send_container, SmsFragment.newInstance(), SmsFragment::class.java.simpleName)
        ft.commitNow()

    }
}
