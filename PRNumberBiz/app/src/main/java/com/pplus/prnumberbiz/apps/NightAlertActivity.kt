package com.pplus.prnumberbiz.apps

import android.os.Bundle
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sms_alert.*

class NightAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sms_alert
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        layout_sms_alert_confirm.setOnClickListener {

            val check = check_sms_alert_not_retry.isChecked
            PreferenceUtil.getDefaultPreference(this).put(Const.NIGHT_ADS, check)
            finish()
        }

    }

}
