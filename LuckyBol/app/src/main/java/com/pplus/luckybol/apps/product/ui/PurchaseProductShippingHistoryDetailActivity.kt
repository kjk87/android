package com.pplus.luckybol.apps.product.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.data.PurchaseProductShipAdapter
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.PurchaseProduct
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityPurchaseProductShippingHistoryDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat

class PurchaseProductShippingHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPurchaseProductShippingHistoryDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPurchaseProductShippingHistoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPurchaseProduct: PurchaseProduct? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPurchaseProduct = intent.getParcelableExtra(Const.DATA)

//        text_purchase_product_shipping_history_detail_desc.text = PplusCommonUtil.fromHtml(getString(R.string.html_purchase_point_desc))
        getPurchaseProduct()
    }

    private fun getPurchaseProduct() {
        val params = HashMap<String, String>()
        params["seqNo"] = mPurchaseProduct!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPurchaseProduct(params).setCallback(object : PplusCallback<NewResultResponse<PurchaseProduct>> {
            override fun onResponse(call: Call<NewResultResponse<PurchaseProduct>>?, response: NewResultResponse<PurchaseProduct>?) {
                hideProgress()
                if (response?.data != null) {
                    mPurchaseProduct = response.data
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PurchaseProduct>>?, t: Throwable?, response: NewResultResponse<PurchaseProduct>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData() {

        binding.textPurchaseProductShippingHistoryDetailPageName.text = mPurchaseProduct!!.page!!.name
        binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.GONE

        var date = ""
        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        binding.textPurchaseProductShippingHistoryDetailConfirmBuy.visibility = View.GONE


        when (mPurchaseProduct!!.status) {
            EnumData.PurchaseProductStatus.PAY.status -> {
                binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.VISIBLE
                binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.GONE
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_pay_complete)
//                text_purchase_product_shipping_history_detail_status.setText(R.string.word_shipping_ready)
                when(mPurchaseProduct!!.deliveryStatus){
                    EnumData.DeliveryStatus.READY_ING.status->{
                        binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.VISIBLE
                        binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
                        binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_preparing_goods)
//                        binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_shipping_ready)
                    }
                    EnumData.DeliveryStatus.ING.status->{
                        binding.textPurchaseProductShippingHistoryDetailBuyCancel.setText(R.string.msg_inquiry_seller)
                        binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.GONE
                        binding.textPurchaseProductShippingHistoryDetailSearchShipping.visibility = View.VISIBLE
                        binding.textPurchaseProductShippingHistoryDetailConfirmBuy.visibility = View.VISIBLE
                        binding.textPurchaseProductShippingHistoryDetailSearchShipping.setOnClickListener {
                            PplusCommonUtil.openChromeWebView(this, "https://m.search.naver.com/search.naver?query=${mPurchaseProduct!!.purchaseDelivery!!.shippingCompany}+${mPurchaseProduct!!.purchaseDelivery!!.transportNumber}")
                        }

                        if (StringUtils.isNotEmpty(mPurchaseProduct!!.purchaseDelivery!!.deliveryStartDatetime)) {
                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.purchaseDelivery!!.deliveryStartDatetime)
                            date = output.format(d)
                            binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                            binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_shipping_start_date, date)
                        }
                        binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
                        binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_shipping)
                    }
                    EnumData.DeliveryStatus.COMPLETE.status->{
                        binding.textPurchaseProductShippingHistoryDetailConfirmBuy.visibility = View.VISIBLE
                        binding.textPurchaseProductShippingHistoryDetailBuyCancel.setText(R.string.msg_inquiry_seller)
                        binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.GONE

                        if (StringUtils.isNotEmpty(mPurchaseProduct!!.purchaseDelivery!!.deliveryCompleteDatetime)) {
                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.purchaseDelivery!!.deliveryCompleteDatetime)
                            date = output.format(d)
                            binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                            binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_shipping_complete_date, date)
                        }

                        binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                        binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_shipping_complete)
                    }
                }
            }
            EnumData.PurchaseProductStatus.CANCEL_REQ.status -> {
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_cancel_request)
            }
            EnumData.PurchaseProductStatus.CANCEL_COMPLETE.status -> {
                binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.GONE

                if (StringUtils.isNotEmpty(mPurchaseProduct!!.cancelDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.cancelDatetime)
                    date = output.format(d)
                    binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                    binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_buy_cancel_date, date)
                }
            }
            EnumData.PurchaseProductStatus.REFUND_REQ.status -> {
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_refund_request)
            }
            EnumData.PurchaseProductStatus.REFUND_COMPLETE.status -> {
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_refund_complete)

                if (StringUtils.isNotEmpty(mPurchaseProduct!!.refundDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.refundDatetime)
                    date = output.format(d)
                    binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                    binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_buy_refund_date, date)
                }
            }
            EnumData.PurchaseProductStatus.EXCHANGE_REQ.status -> {
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4646))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_exchange_request)

            }
            EnumData.PurchaseProductStatus.EXCHANGE_COMPLETE.status -> {
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_exchange_complete)

                if (StringUtils.isNotEmpty(mPurchaseProduct!!.exchangeDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.exchangeDatetime)
                    date = output.format(d)
                    binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                    binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_buy_exchange_date, date)
                }
            }
            EnumData.PurchaseProductStatus.COMPLETE.status -> {
                binding.textPurchaseProductShippingHistoryDetailBuyCancel.visibility = View.GONE
                binding.layoutPurchaseProductShippingHistoryDetailDateInfo.visibility = View.VISIBLE
                binding.textPurchaseProductShippingHistoryDetailStatusDate.text = getString(R.string.format_buy_complete_date, mPurchaseProduct!!.changeStatusDatetime)
                binding.textPurchaseProductShippingHistoryDetailStatus.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
                binding.textPurchaseProductShippingHistoryDetailStatus.setText(R.string.word_confirm_buy)

//                if (StringUtils.isNotEmpty(mPurchaseProduct!!.purchaseDelivery!!.deliveryCompleteDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.purchaseDelivery!!.deliveryCompleteDatetime)
//                    date = output.format(d)
//                    layout_purchase_product_shipping_history_detail_date_info.visibility = View.VISIBLE
//                    text_purchase_product_shipping_history_detail_status_date.text = getString(R.string.format_shipping_complete_date, date)
//                }
//
//                text_purchase_product_shipping_history_detail_status.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                text_purchase_product_shipping_history_detail_status.setText(R.string.word_shipping_complete)
            }
        }

        binding.textPurchaseProductShippingHistoryDetailConfirmBuy.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_buy_compete), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when(event_alert){
                        AlertBuilder.EVENT_ALERT.RIGHT->{
                            val params = HashMap<String, String>()
                            params["seqNo"] = mPurchaseProduct!!.seqNo.toString()
                            showProgress("")
                            ApiBuilder.create().updatePurchaseProductComplete(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    getPurchaseProduct()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            })
            builder.builder().show(this)
        }

        binding.recyclerPurchaseProductShippingHistoryDetail.layoutManager = LinearLayoutManager(this)
        val adapter = PurchaseProductShipAdapter(mPurchaseProduct!!)
        binding.recyclerPurchaseProductShippingHistoryDetail.adapter = adapter

        if (mPurchaseProduct!!.status != EnumData.PurchaseProductStatus.COMPLETE.status && mPurchaseProduct!!.savedPoint != null && mPurchaseProduct!!.savedPoint!! > 0) {
            binding.textPurchaseProductShippingHistoryDetailPoint.visibility = View.VISIBLE
            if (mPurchaseProduct!!.isPaymentPoint != null && mPurchaseProduct!!.isPaymentPoint!!) {
                binding.textPurchaseProductShippingHistoryDetailPoint.text = getString(R.string.format_saved, FormatUtil.getMoneyTypeFloat(mPurchaseProduct!!.savedPoint.toString()))
            } else {
                binding.textPurchaseProductShippingHistoryDetailPoint.text = getString(R.string.format_will_save, FormatUtil.getMoneyTypeFloat(mPurchaseProduct!!.savedPoint.toString()))
            }
        } else {
            binding.textPurchaseProductShippingHistoryDetailPoint.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(mPurchaseProduct!!.payDatetime)) {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mPurchaseProduct!!.payDatetime)
            date = output.format(d)
        }
        binding.textPurchaseProductShippingHistoryDetailPayDate.text = date
        binding.textPurchaseProductShippingHistoryDetailDate.text = date

        if(mPurchaseProduct!!.purchaseDelivery!!.deliveryFee == null){
            mPurchaseProduct!!.purchaseDelivery!!.deliveryFee = 0f
        }

        if(mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee1 == null){
            mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee1 = 0f
        }

        if(mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee2 == null){
            mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee2 = 0f
        }

        val deliveryFee = mPurchaseProduct!!.purchaseDelivery!!.deliveryFee!! + mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee1!! + mPurchaseProduct!!.purchaseDelivery!!.deliveryAddFee2!!

        if(deliveryFee > 0){
            binding.textPurchaseProductShippingHistoryDetailDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(deliveryFee.toInt().toString()))
        }else{
            binding.textPurchaseProductShippingHistoryDetailDeliveryFee.text = getString(R.string.word_free_ship)
        }

        binding.textPurchaseProductShippingHistoryDetailReceiverName.text = mPurchaseProduct!!.purchaseDelivery!!.receiverName
        binding.textPurchaseProductShippingHistoryDetailReceiverTel.text = mPurchaseProduct!!.purchaseDelivery!!.receiverTel
        binding.textPurchaseProductShippingHistoryDetailReceiverAddress.text = "(${mPurchaseProduct!!.purchaseDelivery!!.receiverPostCode})${mPurchaseProduct!!.purchaseDelivery!!.receiverAddress} ${mPurchaseProduct!!.purchaseDelivery!!.receiverAddressDetail}"
        if(StringUtils.isNotEmpty(mPurchaseProduct!!.purchaseDelivery!!.deliveryMemo)){
            binding.layoutPurchaseProductShippingHistoryDetailDeliveryMemo.visibility = View.VISIBLE
            binding.textPurchaseProductShippingHistoryDetailShippingMemo.text = mPurchaseProduct!!.purchaseDelivery!!.deliveryMemo
        }else{
            binding.layoutPurchaseProductShippingHistoryDetailDeliveryMemo.visibility = View.GONE
        }


        binding.textPurchaseProductShippingHistoryDetailBuyCancel.setOnClickListener {

            if(mPurchaseProduct!!.status == EnumData.PurchaseProductStatus.PAY.status){
                if(mPurchaseProduct!!.deliveryStatus == EnumData.DeliveryStatus.READY_ING.status || mPurchaseProduct!!.deliveryStatus == EnumData.DeliveryStatus.READY_BEFORE.status || mPurchaseProduct!!.deliveryStatus == EnumData.DeliveryStatus.READY_ING.status){
                    val intent = Intent(this, PurchaseCancelInfoActivity::class.java)
                    intent.putExtra(Const.DATA, mPurchaseProduct!!.purchase)
                    buyCancelLauncher.launch(intent)
                }else{
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exchange_refund1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_exchange_refund2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.msg_inquiry)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when(event_alert){
                                AlertBuilder.EVENT_ALERT.LEFT->{
                                    val phone = mPurchaseProduct!!.page!!.phone

                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}"))
                                    startActivity(intent)
                                }
                                else -> {}
                            }
                        }
                    })
                    builder.builder().show(this)
                }

            }

        }

//        text_purchase_product_shipping_history_detail_count.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(mBuy!!.buyGoodsList!![0].count.toString()))
        binding.textPurchaseProductShippingHistoryDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mPurchaseProduct!!.price.toString()))

        var payMethod = ""

        when (mPurchaseProduct!!.purchase!!.payMethod) {
            "CARD", "card" -> {
                payMethod = getString(R.string.word_credit_card)
            }
            "BANK", "bank" -> {
                payMethod = getString(R.string.word_real_time_transfer)
            }
            "POINT", "point"->{
                payMethod = getString(R.string.word_pay_point)
//                text_purchase_product_shipping_history_detail_confirm_buy.visibility = View.GONE
//                text_purchase_product_shipping_history_detail_buy_cancel.visibility = View.GONE
            }
        }

        binding.textPurchaseProductShippingHistoryDetailPayMethod.text = payMethod

    }

    val buyCancelLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPurchaseProduct()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_history_detail), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
