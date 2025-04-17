//package com.pplus.prnumberuser.apps.mobilegift.ui
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.MobileGiftPurchase
//import com.pplus.prnumberuser.core.network.model.dto.No
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_mobile_gift_buy.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MobileGiftBuyActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_mobile_gift_buy
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val purchase = intent.getParcelableExtra<MobileGiftPurchase>(Const.DATA)
//        val isMe = intent.getBooleanExtra(Const.IS_ME, false)
//
//        text_mobile_gift_buy_goods_name.text = purchase!!.mobileGift.name
//        text_mobile_gift_buy_company_name.text = purchase.mobileGift.companyName
//        text_mobile_gift_buy_count.text = getString(R.string.format_count_unit, purchase.countPerTarget.toString())
//        text_mobile_gift_buy_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(purchase.totalCost.toString())))
//
//        image_mobile_gift_buy_close.setOnClickListener {
//            onBackPressed()
//        }
//
//        if (isMe) {
//            text_mobile_gift_buy_title.setText(R.string.word_goods_buy)
//            layout_mobile_gift_buy_receiver.visibility = View.GONE
//            layout_mobile_gift_buy_msg.visibility = View.GONE
//            text_mobile_gift_buy_question.setText(R.string.msg_question_buy)
//            text_mobile_gift_buy.setText(R.string.msg_buy)
//        } else {
//            text_mobile_gift_buy_title.setText(R.string.msg_gift)
//            layout_mobile_gift_buy_receiver.visibility = View.VISIBLE
//            text_mobile_gift_buy_receiver_name.text = purchase.targetList[0].name
//            layout_mobile_gift_buy_msg.visibility = View.VISIBLE
//            text_mobile_gift_buy_msg_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, 0, 30))
//            edit_mobile_gift_buy_msg.addTextChangedListener(object : TextWatcher{
//                override fun afterTextChanged(s: Editable?) {
//                    text_mobile_gift_buy_msg_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, s!!.length, 30))
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                }
//            })
//
//            text_mobile_gift_buy_question.setText(R.string.msg_question_gift)
//            text_mobile_gift_buy.setText(R.string.msg_gift)
//        }
//
//        text_mobile_gift_buy.setOnClickListener {
//
//            if (purchase.totalCost > LoginInfoManager.getInstance().user.totalBol) {
//                showAlert(R.string.msg_not_enough_bol)
//                return@setOnClickListener
//            }
//
//            val msg = edit_mobile_gift_buy_msg.text.toString().trim()
//            if(StringUtils.isNotEmpty(msg)){
//                purchase.msg = msg
//            }
//            showProgress("")
//            ApiBuilder.create().prepareOrder(purchase).setCallback(object : PplusCallback<NewResultResponse<No>>{
//                override fun onResponse(call: Call<NewResultResponse<No>>?, response: NewResultResponse<No>?) {
//                    hideProgress()
//                    completeOrder(response!!.data.no.toString())
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<No>>?, t: Throwable?, response: NewResultResponse<No>?) {
//                    hideProgress()
//                    if (response!!.resultCode == 623) {
//                        showAlert(R.string.msg_not_enough_bol)
//                    }
//                }
//            }).build().call()
//        }
//    }
//
//    private fun completeOrder(no: String) {
//        val params: MutableMap<String, String> = HashMap()
//        params["no"] = no
//        showProgress("")
//        ApiBuilder.create().completeOrder(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                showAlert(R.string.msg_complete_mobile_gift)
//                finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                cancelOrder(no)
//            }
//
//        }).build().call()
//    }
//
//    private fun cancelOrder(no: String) {
//        val params: MutableMap<String, String> = HashMap()
//        params["no"] = no
//        showProgress("")
//        ApiBuilder.create().cancelPrepareOrder(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//}
