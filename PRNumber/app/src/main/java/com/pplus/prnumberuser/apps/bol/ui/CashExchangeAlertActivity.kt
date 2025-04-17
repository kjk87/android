package com.pplus.prnumberuser.apps.bol.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Exchange
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityCashExchangeAlertBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class CashExchangeAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCashExchangeAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityCashExchangeAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val exchange = intent.getParcelableExtra<Exchange>(Const.EXCHANGE)

        binding.textCashExchangeAlertAmount.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(exchange!!.bol.toString()))

//        val withDrawAmount = (exchange.bol!!*0.9).toInt()
        val withDrawAmount = exchange.bol!! - 1000

        binding.textCashExchangeAlertWithdrawAmount.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(withDrawAmount.toString()))

        binding.imageCashExchangeAlertClose.setOnClickListener {
            finish()
        }

        binding.textCashExchangeAlertConfirm.setOnClickListener {

            showProgress("")
            ApiBuilder.create().bolExchange(exchange).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    showAlert(R.string.msg_complete_request_cash_exchange)
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

    }

}
