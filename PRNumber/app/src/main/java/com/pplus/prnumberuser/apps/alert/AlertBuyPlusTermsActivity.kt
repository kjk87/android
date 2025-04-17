//package com.pplus.prnumberuser.apps.alert
//
//import android.app.Activity
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_alert_buy_plus_terms.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class AlertBuyPlusTermsActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_alert_buy_plus_terms
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        text_alert_buy_plus_terms_cancel.setOnClickListener {
//            setResult(Activity.RESULT_CANCELED)
//            finish()
//        }
//        text_alert_buy_plus_terms_confirm.setOnClickListener {
//
//            val params = HashMap<String, String>()
//            params["buyPlusTerms"] = "true"
//            showProgress("")
//            ApiBuilder.create().updateBuyPlusTerms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                    hideProgress()
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                    hideProgress()
//                    setResult(Activity.RESULT_CANCELED)
//                    finish()
//                }
//            }).build().call()
//        }
//
//    }
//}
