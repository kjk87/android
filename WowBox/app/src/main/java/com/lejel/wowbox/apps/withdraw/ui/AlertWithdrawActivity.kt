package com.lejel.wowbox.apps.withdraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertWithdrawBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class AlertWithdrawActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertWithdrawBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertWithdrawBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val withdraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Withdraw::class.java)!!
        val bankImage = intent.getStringExtra(Const.BANK_IMAGE)

        if(withdraw.request!! >= 3000000){
            withdraw.fee = withdraw.request!!*0.2f
        }else if(withdraw.request!! >= 2000000){
            withdraw.fee = withdraw.request!!*0.25f
        }else if(withdraw.request!! >= 1000000){
            withdraw.fee = withdraw.request!!*0.3f
        }else if(withdraw.request!! >= 500000){
            withdraw.fee = withdraw.request!!*0.35f
        }else if(withdraw.request!! >= 300000){
            withdraw.fee = withdraw.request!!*0.4f
        }else if(withdraw.request!! >= 150000){
            withdraw.fee = withdraw.request!!*0.5f
        }

        withdraw.withdraw = withdraw.request!!.toDouble() - withdraw.fee!!

        binding.textAlertWithdrawRequest.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(withdraw.request.toString()))
        binding.textAlertWithdrawWithdraw.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(withdraw.withdraw.toString()))

        binding.textAlertWithdrawConfirm.setOnClickListener {

            showProgress("")
            ApiBuilder.create().withdraw(withdraw).setCallback(object : PplusCallback<NewResultResponse<Withdraw>>{
                override fun onResponse(call: Call<NewResultResponse<Withdraw>>?, response: NewResultResponse<Withdraw>?) {
                    hideProgress()
                    setEvent("point_withdraw")
                    val intent = Intent(this@AlertWithdrawActivity, WithdrawReqCompleteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra(Const.DATA, withdraw)
                    intent.putExtra(Const.BANK_IMAGE, bankImage)
                    startActivity(intent)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Withdraw>>?, t: Throwable?, response: NewResultResponse<Withdraw>?) {
                    hideProgress()
                }
            }).build().call()
        }

        binding.textAlertWithdrawCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }


}
