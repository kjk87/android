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
import kotlinx.android.synthetic.main.activity_buy_history_detail.*
import kotlinx.android.synthetic.main.item_order_detail_menu.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class BuyHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_buy_history_detail
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
                    text_buy_history_detail_date.text = mBuy!!.regDatetime

                    layout_buy_history_detail_goods_list.removeAllViews()

                    if (mBuy!!.buyGoodsList != null) {
                        for (buyGoods in mBuy!!.buyGoodsList!!) {
                            val view = layoutInflater.inflate(R.layout.item_order_detail_menu, null)

                            if (buyGoods.goods!!.attachments != null && buyGoods.goods!!.attachments!!.images != null && buyGoods.goods!!.attachments!!.images!!.isNotEmpty()) {
                                view.layout_order_detail_menu.visibility = View.VISIBLE
                                val imageNo = buyGoods.goods!!.attachments!!.images!![0]
                                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${imageNo}")
                                Glide.with(this@BuyHistoryDetailActivity).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(view.image_order_detail_menu)
                            } else {
                                view.layout_order_detail_menu.visibility = View.GONE
                            }

                            view.text_order_detail_menu_name.text = buyGoods.goods!!.name
                            view.text_order_detail_menu_count.text = getString(R.string.format_count_unit, buyGoods.count.toString())
                            layout_buy_history_detail_goods_list.addView(view)
                        }

                        val buyGoods = mBuy!!.buyGoodsList!![0]

                        when (buyGoods.process) {
                            1 -> {//결제완료
                                text_buy_history_detail_status.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_737373))
                                text_buy_history_detail_status.setText(R.string.word_pay_complete)
                                text_buy_history_detail_status_date.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_737373))
                                text_buy_history_detail_date_title.setText(R.string.word_pay_date)
                                text_buy_history_detail_status_date.text = buyGoods.payDatetime
                                text_buy_history_detail_desc.visibility = View.GONE
                                text_buy_history_detail_cancel.visibility = View.VISIBLE
                                text_buy_history_detail_cancel.setOnClickListener {
                                    val builder = AlertBuilder.Builder()
                                    builder.setTitle(getString(R.string.word_notice_alert))
                                    builder.addContents(AlertData.MessageData(getString(R.string.html_money_unit2, FormatUtil.getMoneyType(mBuy!!.price.toString())), AlertBuilder.MESSAGE_TYPE.HTML, 1))
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_question_pay_cancel), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                                        override fun onCancel() {

                                        }

                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                    cancel(mBuy!!)
                                                }
                                            }
                                        }
                                    }).builder().show(this@BuyHistoryDetailActivity)
                                }
                            }
                            2 -> {//취소
                                text_buy_history_detail_status.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_ff4646))
                                text_buy_history_detail_status.setText(R.string.word_buy_cancel)
                                text_buy_history_detail_status_date.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_ff4646))
                                text_buy_history_detail_date_title.setText(R.string.word_order_cancel_date)
                                text_buy_history_detail_status_date.text = buyGoods.cancelDatetime
                                text_buy_history_detail_desc.setText(R.string.msg_buy_canceled_history)
                                text_buy_history_detail_desc.visibility = View.VISIBLE
                                text_buy_history_detail_cancel.visibility = View.GONE
                            }
                            3 -> {//사용완료
                                text_buy_history_detail_status.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_b7b7b7))
                                text_buy_history_detail_status.setText(R.string.word_use_complete)
                                text_buy_history_detail_status_date.setTextColor(ResourceUtil.getColor(this@BuyHistoryDetailActivity, R.color.color_b7b7b7))
                                text_buy_history_detail_date_title.setText(R.string.word_use_complete_date)
                                text_buy_history_detail_status_date.text = buyGoods.useDatetime
                                text_buy_history_detail_desc.setText(R.string.msg_use_complete_history)
                                text_buy_history_detail_desc.visibility = View.VISIBLE
                                text_buy_history_detail_cancel.visibility = View.GONE
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(mBuy!!.memo)) {
                        layout_buy_history_detail_memo.visibility = View.VISIBLE
                        text_buy_history_detail_memo.text = mBuy!!.memo
                    }

                    if (StringUtils.isNotEmpty(mBuy!!.buyerTel)) {
                        text_buy_history_detail_client_phone.text = FormatUtil.getPhoneNumber(maskString(mBuy!!.buyerTel, 3, 7, "X"))
                        text_buy_history_detail_call.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${mBuy!!.buyerTel}")
                            startActivity(intent)
                        }
                    }
                    text_buy_history_detail_client_name.text = mBuy!!.buyerName

                    text_buy_history_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuy!!.price.toString()))


                }

            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun maskString(strText: String?, start: Int, end: Int, maskChar: String): String? {
        var start = start
        var end = end
        if (strText == null || strText == "") return ""
        if (start < 0) start = 0
        if (end > strText.length) end = strText.length
        if (start > end) throw Exception("End index cannot be greater than start index")
        val maskLength = end - start
        if (maskLength == 0) return strText
        val sbMaskString = StringBuilder(maskLength)
        for (i in 0 until maskLength) {
            sbMaskString.append(maskChar)
        }
        return (strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength))
    }

    private fun cancel(buy:Buy){
        val params = HashMap<String, String>()
        params["buySeqNo"] = buy.seqNo.toString()
        showProgress("")
        ApiBuilder.create().buyGoodsListCancel(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_pay_canceled)
                getBuy()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()

                if(response!!.resultCode == 704){
                    showAlert(R.string.msg_can_not_pay_cancel)
                }
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
