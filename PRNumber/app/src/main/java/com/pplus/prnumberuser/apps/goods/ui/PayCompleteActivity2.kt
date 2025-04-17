//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_pay_complete2.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PayCompleteActivity2 : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_pay_complete2
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val orderId = intent.getStringExtra(Const.ID)
//
//
//
//        text_pay_complete_history2.setOnClickListener {
////            val intent = Intent(this, MainActivity::class.java)
//            val intent = Intent(this, AppMainActivity::class.java)
//            intent.putExtra(Const.KEY, Const.GOODS_HISTORY)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_pay_complete_main2.setOnClickListener {
//            setResult(Activity.RESULT_OK)
//            finish()
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
//                    val buy = response.data
//                    var rewardLuckybol = 0L
//                    for (i in 0 until buy.buyGoodsList!!.size) {
//                        val goods = buy.buyGoodsList!![i].goods
//                        if (goods!!.rewardLuckybol != null) {
//                            rewardLuckybol = +goods.rewardLuckybol!!
//                        }
//
//                    }
//
//                    if (rewardLuckybol > 0) {
//                        view_pay_complete_reward_bar.visibility = View.VISIBLE
//                        text_pay_complete_reward_bol.visibility = View.VISIBLE
//                        text_pay_complete_reward_bol.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point2, FormatUtil.getMoneyType(rewardLuckybol.toString())))
//                    } else {
//                        view_pay_complete_reward_bar.visibility = View.GONE
//                        text_pay_complete_reward_bol.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//
//            }
//        }).build().call()
//    }
//
//    override fun onBackPressed() {
//
//    }
//}
