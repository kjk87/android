package com.root37.buflexz.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityWithdrawalBinding
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

                binding.textWithdrawalRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
                binding.textWithdrawalRetentionBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString()))
            }
        })

        getBuffCoinBalance()

        binding.textWithdrawalReq.setOnClickListener {
            val intent = Intent(this, WithdrawalAuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun getBuffCoinBalance() {
        ApiBuilder.create().getBuffCoinBalance().setCallback(object : PplusCallback<NewResultResponse<Map<String, Any>>> {
            override fun onResponse(call: Call<NewResultResponse<Map<String, Any>>>?, response: NewResultResponse<Map<String, Any>>?) {
                if (response?.result != null) {
                    LogUtil.e(LOG_TAG, response.result!!.toString())
                    val buffCoin = response.result!!["buff"]
                    val krw = response.result!!["krw"]
                    val totalKrw = response.result!!["totalKrw"]
                    binding.textWithdrawalRetentionBuffCoin.text = FormatUtil.getCoinType(buffCoin.toString())

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?, t: Throwable?, response: NewResultResponse<Map<String, Any>>?) {
            }
        }).build().call()
    }

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