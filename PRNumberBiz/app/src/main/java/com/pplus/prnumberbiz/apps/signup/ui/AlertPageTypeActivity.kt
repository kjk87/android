package com.pplus.prnumberbiz.apps.signup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.activity_alert_page_type.*

class AlertPageTypeActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }
    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_page_type
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val data = Intent()

        layout_alert_page_type_store.setOnClickListener {

            data.putExtra(Const.TYPE, EnumData.PageTypeCode.store.name)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        layout_alert_page_type_person.setOnClickListener {

            data.putExtra(Const.TYPE, EnumData.PageTypeCode.person.name)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        text_alert_page_type_cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

}
