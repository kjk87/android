package com.root37.buflexz.apps.my.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.databinding.ActivityWithdrawalAuthBinding

class WithdrawalAuthActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawalAuthBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawalAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertBuilder.Builder()
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_cancel_auth), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
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
                }).builder().show(this@WithdrawalAuthActivity)
            }
        })

        auth()
    }

    private fun auth() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.withdrawal_container, WithdrawalAuthFragment.newInstance(), WithdrawalAuthFragment::class.java.simpleName)
        ft.commit()
    }

    fun confirm(reason: String, authNumber: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.withdrawal_container, WithdrawalAuthConfirmFragment.newInstance(reason, authNumber), WithdrawalAuthFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_withdrawal), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}