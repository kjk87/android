package com.pplus.prnumberuser.apps.menu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.goods.ui.AlertCancelCompleteActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityAlertCancelOrderBinding
import retrofit2.Call

class AlertCancelOrderActivity : BaseActivity() {

    private lateinit var binding: ActivityAlertCancelOrderBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertCancelOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val orderPurchase = intent.getParcelableExtra<OrderPurchase>(Const.DATA)


        when(orderPurchase!!.salesType){
            6->{
                binding.textAlertCancelOrderMsg.setText(R.string.msg_alert_cancel_ticket)
            }
            else->{
                binding.textAlertCancelOrderMsg.setText(R.string.msg_alert_cancel_order)
            }
        }

        binding.textAlertCancelOrderCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.textAlertCancelOrderConfirm.setOnClickListener {
            cancel(orderPurchase)
        }
    }
    private fun cancel(orderPurchase: OrderPurchase){
        val params = HashMap<String, String>()
        params["orderPurchaseSeqNo"] = orderPurchase.seqNo.toString()
        params["memo"] = ""
        showProgress("")
        ApiBuilder.create().cancelOrderPurchaseUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@AlertCancelOrderActivity, AlertCancelCompleteActivity::class.java)
                startActivity(intent)
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.resultCode == 516) {
                    showAlert(R.string.msg_impossible_cancel_time)
                    finish()
                }

            }
        }).build().call()
    }
}