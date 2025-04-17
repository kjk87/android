package com.pplus.prnumberbiz.apps.sale.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_sale_order_detail.*
import kotlinx.android.synthetic.main.item_order_detail_menu.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleOrderDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_order_detail
    }

    var mBuy: Buy? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuy = intent.getParcelableExtra(Const.DATA)
        getBuy()
    }

    private fun getBuy() {
        val params = HashMap<String, String>()
        params["seqNo"] = mBuy!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
                hideProgress()

                if (response != null) {
                    mBuy = response.data
                    text_sale_order_detail_date.text = mBuy!!.regDatetime

                    layout_sale_order_detail_goods_list.removeAllViews()


                    if(mBuy!!.buyGoodsList != null ){
                        for (buyGoods in mBuy!!.buyGoodsList!!) {
                            val view = layoutInflater.inflate(R.layout.item_order_detail_menu, null)

                            if(buyGoods.goods!!.attachments != null && buyGoods.goods!!.attachments!!.images != null && buyGoods.goods!!.attachments!!.images!!.isNotEmpty()){
                                view.layout_order_detail_menu.visibility = View.VISIBLE
                                val imageNo = buyGoods.goods!!.attachments!!.images!![0]
                                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${imageNo}")
                                Glide.with(this@SaleOrderDetailActivity).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(view.image_order_detail_menu)
                            }else{
                                view.layout_order_detail_menu.visibility = View.GONE
                            }

                            view.text_order_detail_menu_name.text = buyGoods.goods!!.name
                            view.text_order_detail_menu_count.text = getString(R.string.format_count_unit, buyGoods.count.toString())
                            layout_sale_order_detail_goods_list.addView(view)
                        }
                    }

                    if (StringUtils.isNotEmpty(mBuy!!.memo)) {
                        layout_sale_order_detail_memo.visibility = View.VISIBLE
                        text_sale_order_detail_memo.text = mBuy!!.memo
                    }

                    text_sale_order_detail_client_name.text = mBuy!!.buyerName
                    text_sale_order_detail_client_phone.text = mBuy!!.buyerTel
                    text_sale_order_detail_call.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${mBuy!!.buyerTel}")
                        startActivity(intent)
                    }

                    text_sale_order_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuy!!.price.toString()))

                    when (mBuy!!.orderType) {
                        0 -> {//매장
                            text_sale_order_detail_type.setText(R.string.word_order_store)
                            text_sale_order_detail_type.setTextColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_ff696a))
                            layout_sale_order_detail_book_time.visibility = View.GONE
                            layout_sale_order_detail_delivery_address.visibility = View.GONE
                            layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_ff696a))
                        }
                        1 -> {//포장
                            text_sale_order_detail_type.setText(R.string.word_order_packing)
                            text_sale_order_detail_type.setTextColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_a26df3))
                            layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_a26df3))
                            layout_sale_order_detail_delivery_address.visibility = View.GONE
                            if (StringUtils.isNotEmpty(mBuy!!.bookDatetime)) {
                                layout_sale_order_detail_book_time.visibility = View.VISIBLE
                                text_sale_order_detail_book_time.text = mBuy!!.bookDatetime
                            } else {
                                layout_sale_order_detail_book_time.visibility = View.GONE
                            }

                        }
                        2 -> {//배달
                            text_sale_order_detail_type.setText(R.string.word_order_delivery)
                            text_sale_order_detail_type.setTextColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_3ec082))
                            layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_3ec082))
                            layout_sale_order_detail_book_time.visibility = View.GONE
                            layout_sale_order_detail_delivery_address.visibility = View.VISIBLE
                            text_sale_order_detail_delivery_address.text = mBuy!!.clientAddress
                        }
                    }

                    layout_sale_order_detail_confirm.setOnClickListener {
                        when (mBuy!!.orderProcess) {
                            0 -> {
                                updateOrderProcess(1)
                            }
                            1 -> {
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_order_complete), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                builder.setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                updateOrderProcess(2)
                                            }
                                        }
                                    }
                                }).builder().show(this@SaleOrderDetailActivity)

                            }
                        }
                    }

                    setOrderProcess()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setOrderProcess() {
        when (mBuy!!.orderProcess) {
            0 -> {
                layout_sale_order_detail_cancel.visibility = View.VISIBLE

                layout_sale_order_detail_cancel.setOnClickListener {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_order_cancel), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    updateOrderProcess(3)
                                }
                            }
                        }
                    }).builder().show(this@SaleOrderDetailActivity)
                }
            }
            1 -> {
                layout_sale_order_detail_cancel.visibility = View.VISIBLE

                layout_sale_order_detail_cancel.setOnClickListener {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_order_cancel), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    updateOrderProcess(3)
                                }
                            }
                        }
                    }).builder().show(this@SaleOrderDetailActivity)
                }
                layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_579ffb))
                when (mBuy!!.orderType) {
                    0, 1 -> {
                        text_sale_order_detail_confirm.setText(R.string.word_order_complete)
                    }
                    2 -> {
                        text_sale_order_detail_confirm.setText(R.string.word_delivery_complete)
                    }
                }
            }
            2 -> {
                layout_sale_order_detail_cancel.visibility = View.GONE
                layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_737373))
                text_sale_order_detail_confirm.setText(R.string.msg_completed_order)

            }
            3 -> {
                layout_sale_order_detail_cancel.visibility = View.GONE
                layout_sale_order_detail_confirm.setBackgroundColor(ResourceUtil.getColor(this@SaleOrderDetailActivity, R.color.color_737373))
                text_sale_order_detail_confirm.setText(R.string.msg_completed_order_cancel)
            }
        }
    }

    private fun updateOrderProcess(process: Int) {
        val params = HashMap<String, String>()
        params["seqNo"] = mBuy!!.seqNo.toString()
        params["orderProcess"] = process.toString()
        showProgress("")
        ApiBuilder.create().updateOrderProcess(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mBuy!!.orderProcess = process
                setOrderProcess()

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history_detail), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
