package com.pplus.prnumberuser.apps.product.ui

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
import com.pplus.prnumberuser.core.network.model.dto.Purchase
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityBuyCancelInfoBinding
import retrofit2.Call

class PurchaseCancelInfoActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityBuyCancelInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityBuyCancelInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val purchase = intent.getParcelableExtra<Purchase>(Const.DATA)

//        text_buy_cancel_desc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_buy_cancel_description1))

        binding.textBuyNotCancel.setOnClickListener {
            finish()
        }
        binding.imageBuyCancelInfoClose.setOnClickListener {
            finish()
        }

        binding.textBuyCancel.setOnClickListener {

            cancel(purchase!!)

        }
    }

    private fun cancel(purchase:Purchase){
        val params = HashMap<String, String>()
        params["purchaseSeqNo"] = purchase.seqNo.toString()
        params["memo"] = ""
        params["type"] = "user"
        showProgress("")
        ApiBuilder.create().cancelPurchase(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@PurchaseCancelInfoActivity, AlertCancelCompleteActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_BUY_CANCEL -> {
                setResult(resultCode)
                finish()
            }
        }
    }

}
