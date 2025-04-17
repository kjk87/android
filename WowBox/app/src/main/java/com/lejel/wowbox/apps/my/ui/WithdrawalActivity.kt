package com.lejel.wowbox.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWithdrawalBinding
import retrofit2.Call

class WithdrawalActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawalBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textWithdrawalRetentionPoint.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
                binding.textWithdrawalRetentionCash.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
                binding.textWithdrawalRetentionBall.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString())
            }
        })

//        getBuffCoinBalance()

        binding.textWithdrawalReq.setOnClickListener {

            if(LoginInfoManager.getInstance().member!!.verifiedMobile == null || !LoginInfoManager.getInstance().member!!.verifiedMobile!!){

                val builder = AlertBuilder.Builder()
                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_verify_mobile), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                builder.setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.SINGLE -> {
                                val intent = Intent(this@WithdrawalActivity, UpdateMobileNumberActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                            else -> {

                            }
                        }
                    }
                }).builder().show(this)
                return@setOnClickListener
            }
            val intent = Intent(this, WithdrawalAuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

//    private fun getBuffCoinBalance() {
//        ApiBuilder.create().getBuffCoinBalance().setCallback(object : PplusCallback<NewResultResponse<Map<String, Any>>> {
//            override fun onResponse(call: Call<NewResultResponse<Map<String, Any>>>?, response: NewResultResponse<Map<String, Any>>?) {
//                if (response?.result != null) {
//                    LogUtil.e(LOG_TAG, response.result!!.toString())
//                    val buffCoin = response.result!!["buff"]
//                    val usdt = response.result!!["usdt"]
//                    val totalUsdt = response.result!!["totalUsdt"]
//                    binding.textWithdrawalRetentionBuffCoin.text = FormatUtil.getCoinType(buffCoin.toString())
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?, t: Throwable?, response: NewResultResponse<Map<String, Any>>?) {
//            }
//        }).build().call()
//    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_withdrawal), ToolbarOption.ToolbarMenu.LEFT)
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