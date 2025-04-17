package com.pplus.prnumberbiz.apps.pages.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_alert_page_set_guide.*

class AlertPageSetGuideActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_page_set_guide
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        text_alert_page_set_guide_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_page_set_guide_title))
        image_alert_page_set_guide_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        text_alert_page_set_guide_reg.setOnClickListener {
            val intent = Intent(this, PageConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }
    }

    override fun onBackPressed() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SET_PAGE -> {
                setResult(resultCode)
                finish()
            }
        }
    }
}
