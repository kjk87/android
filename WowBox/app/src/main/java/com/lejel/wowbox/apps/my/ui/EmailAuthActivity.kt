package com.lejel.wowbox.apps.my.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityEmailAuthBinding
import com.lejel.wowbox.databinding.ActivityWalletMakeBinding

class EmailAuthActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityEmailAuthBinding

    override fun getLayoutView(): View {
        binding = ActivityEmailAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertBuilder.Builder()
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_close_make_buff_wallet), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                setResult(RESULT_CANCELED)
                                finish()
                            }

                            else -> {

                            }
                        }
                    }
                }).builder().show(this@EmailAuthActivity)
            }
        })

        auth()
    }

    private fun auth() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.email_auth_container, EmailAuthFragment.newInstance(), EmailAuthFragment::class.java.simpleName)
        ft.commit()
    }

    fun confirm(authEmail: String, authNumber: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.email_auth_container, EmailAuthConfirmFragment.newInstance(authEmail, authNumber), EmailAuthConfirmFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_make_buff_wallet), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}