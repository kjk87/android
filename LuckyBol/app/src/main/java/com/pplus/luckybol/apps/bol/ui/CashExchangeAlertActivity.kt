package com.pplus.luckybol.apps.bol.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Exchange
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityCashExchangeAlertBinding
import com.pplus.networks.common.PplusCallback
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

        binding.textCashExchangeAlertAmount.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(exchange!!.point.toString()))

        val withDrawAmount = (exchange.point!! - 1000).toInt()

        binding.textCashExchangeAlertWithdrawAmount.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(withDrawAmount.toString()))

        binding.imageCashExchangeAlertClose.setOnClickListener {
            finish()
        }

        binding.textCashExchangeAlertConfirm.setOnClickListener {

            showProgress("")
            ApiBuilder.create().cashExchange(exchange).setCallback(object : PplusCallback<NewResultResponse<Any>>{
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
