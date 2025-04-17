//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyDetailActivity
//import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_coupon_pay_complete.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class CouponPayCompleteActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_coupon_pay_complete
//    }
//
//    var mBuy: Buy? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val orderId = intent.getStringExtra(Const.ID)
//
//
//
//        layout_coupon_pay_complete_use.setOnClickListener {
//            if (mBuy == null) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, BuyDetailActivity::class.java)
//            intent.putExtra(Const.DATA, mBuy)
//            startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//
//        }
//
//        layout_coupon_pay_complete_after_use.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_after_use_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//
//            builder.setLeftText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//                override fun onCancel() {
////                    val intent = Intent(this@PayCompleteActivity, MainActivity::class.java)
//                    val intent = Intent(this@CouponPayCompleteActivity, AppMainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
////                    val intent = Intent(this@PayCompleteActivity, MainActivity::class.java)
//                    val intent = Intent(this@CouponPayCompleteActivity, AppMainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//            }).builder().show(this)
//
//        }
//
//
//        val params = HashMap<String, String>()
//
//        params["orderId"] = orderId
//        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                if (response != null && response.data != null) {
//
//                    mBuy = response.data
//
//
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//
//            }
//        }).build().call()
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_GOODS_DETAIL -> {
////                val intent = Intent(this, MainActivity::class.java)
//                val intent = Intent(this, AppMainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//        }
//    }
//
//    override fun onBackPressed() {
//
//    }
//}
