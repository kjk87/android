//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_buy_cancel_info.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class BuyCancelInfoActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_buy_cancel_info
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val buy = intent.getParcelableExtra<Buy>(Const.DATA)
//
////        text_buy_cancel_desc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_buy_cancel_description1))
//
//        text_buy_not_cancel.setOnClickListener {
//            finish()
//        }
//        image_buy_cancel_info_close.setOnClickListener {
//            finish()
//        }
//
//        text_buy_cancel.setOnClickListener {
//
//            cancel(buy)
//
////            if(buy!!.buyGoodsList!!.size > 1){
////                cancel(buy.seqNo!!)
////            }else{
////                process(EnumData.BuyGoodsProcess.CANCEL.process, buy.buyGoodsList!![0])
////            }
//
//        }
//    }
//
//    private fun cancel(buy:Buy){
//        val params = HashMap<String, String>()
//        params["buySeqNo"] = buy.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().buyGoodsListCancel(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                val intent = Intent(this@BuyCancelInfoActivity, AlertCancelCompleteActivity::class.java)
//                startActivity(intent)
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//
//                if(response!!.resultCode == 704){
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(getString(R.string.word_notice_alert))
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_lpng_cancel1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_lpng_cancel2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.setLeftText(getString(R.string.msg_inquiry_cancel)).setRightText(getString(R.string.word_confirm))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                            when(event_alert){
//                                AlertBuilder.EVENT_ALERT.LEFT->{
//                                    val phone = buy.page!!.phone
//
//                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}"))
//                                    startActivity(intent)
//                                }
//                            }
//                        }
//                    })
//                    builder.builder().show(this@BuyCancelInfoActivity, true)
//                }
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_BUY_CANCEL -> {
//                setResult(resultCode)
//                finish()
//            }
//        }
//    }
//
//}
