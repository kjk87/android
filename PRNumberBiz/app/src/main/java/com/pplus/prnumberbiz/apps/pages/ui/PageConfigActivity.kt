package com.pplus.prnumberbiz.apps.pages.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity

class PageConfigActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_page_config
    }

    var mIsEditing = true

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.page_config_container, PageConfigFragment.newInstance(), PageConfigFragment::class.java.simpleName)
        ft.replace(R.id.page_config_container, PageConfigFragment2.newInstance(), PageConfigFragment2::class.java.simpleName)
        ft.commit()

    }


    override fun onBackPressed() {
        if (mIsEditing) {
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
}
