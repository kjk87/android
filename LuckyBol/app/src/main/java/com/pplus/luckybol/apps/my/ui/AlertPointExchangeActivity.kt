package com.pplus.luckybol.apps.my.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityAlertPointExchangeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
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

        binding.textAlertPointExchangeRetentionBol.setSingleLine()

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textAlertPointExchangeRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })

        binding.editAlertPointExchange.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isNotEmpty()) {
                    val amount = p0.toString().toLong()

                    if(amount == 0L){
                        binding.editAlertPointExchange.setText("")
                        return
                    }

                    if(amount > LoginInfoManager.getInstance().user.totalBol){
                        binding.editAlertPointExchange.setText(LoginInfoManager.getInstance().user.totalBol.toString())
                    }

                }
            }
        })

        binding.imagePointExchangeClose.setOnClickListener {
            finish()
        }

        binding.textAlertPointConfirm.setOnClickListener {
            val pointStr = binding.editAlertPointExchange.text.toString()

            if(StringUtils.isEmpty(pointStr) || pointStr.toInt() == 0){
                showAlert(R.string.msg_input_exchange_point)
                return@setOnClickListener
            }

            if(pointStr.toInt() > LoginInfoManager.getInstance().user.totalBol){
                showAlert(R.string.msg_not_enough_bol)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["point"] = pointStr
            showProgress("")

            ApiBuilder.create().exchangePointByBol(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    hideProgress()
                    showAlert(R.string.msg_exchanged_point)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Any>?) {
                    hideProgress()
                    if (response!!.resultCode == 517) {
                        showAlert(R.string.msg_not_enough_bol)
                    }
                }
            }).build().call()
        }
    }


    override fun onPause() {
        super.onPause()
    }
}
