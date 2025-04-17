package com.lejel.wowbox.apps.point.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertPointExchangeBinding
import com.lejel.wowbox.databinding.ActivityAlertWithdrawBinding
import retrofit2.Call

class AlertPointExchangeActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertPointExchangeBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertPointExchangeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val point = intent.getIntExtra(Const.POINT, 0)


        binding.textAlertPointExchangePoint.text = FormatUtil.getMoneyType(point.toString())

        binding.textAlertPointExchangeConfirm.setOnClickListener {

            showProgress("")
            val params = HashMap<String, String>()
            params["point"] = point.toString()
            ApiBuilder.create().pointMallExchange(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                    setEvent("point_mallExchange")

                    hideProgress()
                    showAlert(R.string.msg_complete_point_exchange)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

        binding.textAlertPointExchangeCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }


}
