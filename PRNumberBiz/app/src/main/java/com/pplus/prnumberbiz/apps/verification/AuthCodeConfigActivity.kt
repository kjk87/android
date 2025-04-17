package com.pplus.prnumberbiz.apps.verification

import android.os.Bundle
import com.pple.pplus.utils.part.logs.LogUtil
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.model.dto.Verification

class AuthCodeConfigActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_auth_code_config
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        configNumber()
        supportFragmentManager.addOnBackStackChangedListener {

            var fragment = supportFragmentManager.findFragmentById(R.id.auth_code_config_container)
            if ((fragment is AuthCodeConfigFragment)) {
                fragment.init()
                LogUtil.e(LOG_TAG, "VerificationNumberConfigFragment")
            }
        }
    }

    fun configNumber() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.auth_code_config_container, AuthCodeConfigFragment.newInstance(), AuthCodeConfigFragment::class.java.simpleName)
        ft.commitAllowingStateLoss()
    }

    fun confirmNumber() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.auth_code_config_container, CheckAuthCodeFragment.newInstance(), CheckAuthCodeFragment::class.java.simpleName)
//        ft.addToBackStack(VerificationNumberConfirmFragment::class.java.simpleName)
        ft.commitAllowingStateLoss()
    }

    fun inputNumber(authCode: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.auth_code_config_container, InputAuthCodeFragment.newInstance(authCode, null), InputAuthCodeFragment::class.java.simpleName)
//        ft.addToBackStack(VerificationNumberInputFragment::class.java.simpleName)
        ft.commitAllowingStateLoss()
    }

    fun inputNumber(verification: Verification) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.auth_code_config_container, InputAuthCodeFragment.newInstance("", verification), InputAuthCodeFragment::class.java.simpleName)
//        ft.addToBackStack(VerificationNumberInputFragment::class.java.simpleName)
        ft.commitAllowingStateLoss()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_verification_number_config), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }


    override fun onBackPressed() {
        if ((supportFragmentManager.findFragmentById(R.id.auth_code_config_container) is InputAuthCodeFragment)) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_back_verification_number1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_back_verification_number2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            configNumber()

                        }
                    }
                }
            }).builder().show(this)
        } else if ((supportFragmentManager.findFragmentById(R.id.auth_code_config_container) is CheckAuthCodeFragment)) {
            configNumber()
        } else {
            super.onBackPressed()
        }

    }
}
