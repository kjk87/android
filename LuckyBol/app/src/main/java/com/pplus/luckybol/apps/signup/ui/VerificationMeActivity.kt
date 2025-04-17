package com.pplus.luckybol.apps.signup.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData.MessageData
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityVerificationMeBinding

class VerificationMeActivity : BaseActivity() {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityVerificationMeBinding

    override fun getLayoutView(): View {
        binding = ActivityVerificationMeBinding.inflate(layoutInflater)
        return binding.root
    }

    var key: String? = null
    var mobileNumber: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        key = intent.getStringExtra(Const.KEY)
        mobileNumber = intent.getStringExtra(Const.MOBILE_NUMBER)

        verification("")
    }

    fun verification(params: String?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.verification_me_container, VerificationMeStep2Fragment.newInstance(params), VerificationMeStep2Fragment::class.java.simpleName)
        ft.commit()
    }

    override fun onBackPressed() {
        if (key == Const.MOBILE_NUMBER) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(MessageData(getString(R.string.msg_question_change_mobile_stop), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> finish()
                        else -> {}
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }
    }
}