//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.BuyShipTypeDetailAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.activity_buy_shipping_detail.*
//import retrofit2.Call
//import java.text.SimpleDateFormat
//
//class BuyShippingDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_buy_shipping_detail
//    }
//
//    var mBuyGoods: BuyGoods? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mBuyGoods = intent.getParcelableExtra(Const.DATA)
//
//        getBuyGoods()
//    }
//
//    private fun getBuyGoods() {
//        val params = HashMap<String, String>()
//        params["seqNo"] = mBuyGoods!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getOneBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoods>> {
//            override fun onResponse(call: Call<NewResultResponse<BuyGoods>>?, response: NewResultResponse<BuyGoods>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mBuyGoods = response.data
//                    setData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<BuyGoods>>?, t: Throwable?, response: NewResultResponse<BuyGoods>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setData() {
//
//        text_buy_shipping_detail_page_name.text = mBuyGoods!!.page!!.name
//        text_buy_shipping_detail_buy_cancel.visibility = View.GONE
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        text_buy_shipping_detail_confirm_buy.visibility = View.GONE
//        when (mBuyGoods!!.process) {
//            EnumData.BuyGoodsProcess.PAY.process -> {
//                text_buy_shipping_detail_buy_cancel.visibility = View.VISIBLE
//
//                when(mBuyGoods!!.orderProcess){
//                    EnumData.BuyGoodsOrderProcess.ready.process->{
//                        layout_buy_shipping_date_info.visibility = View.GONE
//                        text_buy_shipping_detail_buy_cancel.setText(R.string.word_buy_cancel)
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
//                        text_buy_shipping_detail_status.setText(R.string.word_pay_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.confirm.process->{
//                        layout_buy_shipping_date_info.visibility = View.GONE
//                        text_buy_shipping_detail_buy_cancel.setText(R.string.word_buy_cancel)
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
//                        text_buy_shipping_detail_status.setText(R.string.word_preparing_goods)
//                    }
//                    EnumData.BuyGoodsOrderProcess.shipping.process->{
//                        text_buy_shipping_detail_buy_cancel.setText(R.string.msg_inquiry_seller)
//                        text_buy_shipping_search_shipping.visibility = View.VISIBLE
//                        text_buy_shipping_detail_confirm_buy.visibility = View.VISIBLE
//                        text_buy_shipping_search_shipping.setOnClickListener {
//                            PplusCommonUtil.openChromeWebView(this, "https://m.search.naver.com/search.naver?query=${mBuyGoods!!.shippingCompany}+${mBuyGoods!!.transportNumber}")
//                        }
//
//                        if (StringUtils.isNotEmpty(mBuyGoods!!.deliveryStartDatetime)) {
//                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuyGoods!!.deliveryStartDatetime)
//                            date = output.format(d)
//                            layout_buy_shipping_date_info.visibility = View.VISIBLE
//                            text_buy_shipping_status_date.text = getString(R.string.format_shipping_start_date, date)
//                        }
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
//                        text_buy_shipping_detail_status.setText(R.string.word_shipping)
//                    }
//                    EnumData.BuyGoodsOrderProcess.shipping_complete.process->{
//                        text_buy_shipping_detail_confirm_buy.visibility = View.VISIBLE
//                        text_buy_shipping_detail_buy_cancel.setText(R.string.msg_inquiry_seller)
//
//                        if (StringUtils.isNotEmpty(mBuyGoods!!.deliveryCompleteDatetime)) {
//                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuyGoods!!.deliveryCompleteDatetime)
//                            date = output.format(d)
//                            layout_buy_shipping_date_info.visibility = View.VISIBLE
//                            text_buy_shipping_status_date.text = getString(R.string.format_shipping_complete_date, date)
//                        }
//
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                        text_buy_shipping_detail_status.setText(R.string.word_shipping_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.refund_wait.process->{
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
//                        text_buy_shipping_detail_status.setText(R.string.word_refund_request)
//                    }
//                    EnumData.BuyGoodsOrderProcess.refund.process->{
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                        text_buy_shipping_detail_status.setText(R.string.word_refund_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.exchange_wait.process->{
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
//                        text_buy_shipping_detail_status.setText(R.string.word_exchange_request)
//                    }
//                    EnumData.BuyGoodsOrderProcess.exchange.process->{
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                        text_buy_shipping_detail_status.setText(R.string.word_exchange_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.complete.process->{
//                        text_buy_shipping_detail_buy_cancel.visibility = View.GONE
//                        layout_buy_shipping_date_info.visibility = View.VISIBLE
//                        text_buy_shipping_status_date.text = getString(R.string.format_buy_complete_date, mBuyGoods!!.completeDatetime)
//                        text_buy_shipping_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                        text_buy_shipping_detail_status.setText(R.string.word_confirm_buy)
//                    }
//                }
//
//            }
//            EnumData.BuyGoodsProcess.CANCEL.process -> {
//                text_buy_shipping_detail_buy_cancel.visibility = View.GONE
//
//                if (StringUtils.isNotEmpty(mBuyGoods!!.cancelDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuyGoods!!.cancelDatetime)
//                    date = output.format(d)
//                    layout_buy_shipping_date_info.visibility = View.VISIBLE
//                    text_buy_shipping_status_date.text = getString(R.string.format_buy_cancel_date, date)
//                }
//            }
//        }
//
//        text_buy_shipping_detail_confirm_buy.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_buy_compete), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when(event_alert){
//                        AlertBuilder.EVENT_ALERT.RIGHT->{
//                            val params = HashMap<String, String>()
//                            params["seqNo"] = mBuyGoods!!.seqNo.toString()
//                            showProgress("")
//                            ApiBuilder.create().updateBuyComplete(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
//                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                    hideProgress()
//                                    getBuyGoods()
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                                    hideProgress()
//                                }
//                            }).build().call()
//                        }
//                    }
//                }
//            })
//            builder.builder().show(this)
//        }
//
//        recycler_buy_shipping_detail.layoutManager = LinearLayoutManager(this)
//        val adapter = BuyShipTypeDetailAdapter(mBuyGoods!!)
//        recycler_buy_shipping_detail.adapter = adapter
//
//        if (mBuyGoods!!.orderProcess != EnumData.BuyGoodsOrderProcess.complete.process && mBuyGoods!!.savedPoint != null && mBuyGoods!!.savedPoint!! > 0) {
//            text_buy_shipping_detail_point.visibility = View.VISIBLE
//            if (mBuyGoods!!.isPaymentPoint != null && mBuyGoods!!.isPaymentPoint!!) {
//                text_buy_shipping_detail_point.text = getString(R.string.format_saved, FormatUtil.getMoneyType(mBuyGoods!!.savedPoint.toString()))
//            } else {
//                text_buy_shipping_detail_point.text = getString(R.string.format_will_save, FormatUtil.getMoneyType(mBuyGoods!!.savedPoint.toString()))
//            }
//        } else {
//            text_buy_shipping_detail_point.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(mBuyGoods!!.payDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuyGoods!!.payDatetime)
//            date = output.format(d)
//        }
//        text_buy_shipping_detail_pay_date.text = date
//        text_buy_shipping_detail_date.text = date
//
//        if(mBuyGoods!!.deliveryFee != null && mBuyGoods!!.deliveryFee!! > 0){
//            text_buy_shipping_detail_delivery_fee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuyGoods!!.deliveryFee.toString()))
//        }else{
//            text_buy_shipping_detail_delivery_fee.text = getString(R.string.word_free_ship)
//        }
//
//        text_buy_shipping_detail_receiver_name.text = mBuyGoods!!.receiverName
//        text_buy_shipping_detail_receiver_tel.text = mBuyGoods!!.receiverTel
//        text_buy_shipping_detail_receiver_address.text = "(${mBuyGoods!!.receiverPostCode})${mBuyGoods!!.receiverAddress}"
//        if(StringUtils.isNotEmpty(mBuyGoods!!.deliveryMemo)){
//            layout_buy_shipping_detail_delivery_memo.visibility = View.VISIBLE
//            text_buy_shipping_detail_shipping_memo.text = mBuyGoods!!.deliveryMemo
//        }else{
//            layout_buy_shipping_detail_delivery_memo.visibility = View.GONE
//        }
//
//
//        text_buy_shipping_detail_buy_cancel.setOnClickListener {
//            when(mBuyGoods!!.orderProcess){
//                EnumData.BuyGoodsOrderProcess.ready.process, EnumData.BuyGoodsOrderProcess.confirm.process->{
//                    val intent = Intent(this, BuyCancelInfoActivity::class.java)
//                    intent.putExtra(Const.DATA, mBuyGoods!!.buy)
//                    startActivityForResult(intent, Const.REQ_BUY_CANCEL)
//                }
//                else->{
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(getString(R.string.word_notice_alert))
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exchange_refund1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exchange_refund2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.setLeftText(getString(R.string.msg_inquiry)).setRightText(getString(R.string.word_confirm))
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
//                                    val phone = mBuyGoods!!.page!!.phone
//
//                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}"))
//                                    startActivity(intent)
//                                }
//                            }
//                        }
//                    })
//                    builder.builder().show(this)
//                }
//            }
//
//        }
//
////        text_buy_shipping_detail_count.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(mBuy!!.buyGoodsList!![0].count.toString()))
//        text_buy_shipping_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuyGoods!!.price.toString()))
//
//        var payMethod = ""
//
//        when (mBuyGoods!!.buy!!.payMethod) {
//            "CARD", "card" -> {
//                payMethod = getString(R.string.word_credit_card)
//            }
//            "BANK", "bank" -> {
//                payMethod = getString(R.string.word_real_time_transfer)
//            }
//        }
//
//        text_buy_shipping_detail_pay_method.text = payMethod
//
//
//    }
//
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_BUY_CANCEL -> {
//                getBuyGoods()
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_history_detail), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
