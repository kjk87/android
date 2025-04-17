package com.pplus.prnumberbiz.apps.goods.ui

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
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.activity_goods_sale_history_detail.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.HashMap

class GoodsSaleHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_product detail_order list_detail"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_sale_history_detail
    }


    override fun initializeView(savedInstanceState: Bundle?) {
        val buyGoods = intent.getParcelableExtra<BuyGoods>(Const.DATA)

        val params = HashMap<String, String>()
        params["seqNo"] = buyGoods.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOneBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoods>> {

            override fun onResponse(call: Call<NewResultResponse<BuyGoods>>?, response: NewResultResponse<BuyGoods>?) {
                hideProgress()
                if (response != null) {
                    val data = response.data
                    if (data.goods != null) {
                        text_goods_sale_history_detail_goods_name.text = data.goods!!.name
                        if (data.goods!!.attachments != null && data.goods!!.attachments!!.images != null && data.goods!!.attachments!!.images!!.isNotEmpty()) {
                            val id = data.goods!!.attachments!!.images!![0]

                            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                            Glide.with(this@GoodsSaleHistoryDetailActivity).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_goods_sale_history_detail_goods_image)
                        } else {
                            image_goods_sale_history_detail_goods_image.setImageResource(R.drawable.prnumber_default_img)
                        }
                    }

                    text_goods_sale_history_detail_count.text = getString(R.string.format_count, FormatUtil.getMoneyType(data.count.toString()))

                    var payMethod = ""

                    if (data.buy != null) {
                        when (data.buy!!.payMethod) {
                            "CARD", "card" -> {
                                payMethod = getString(R.string.word_credit_card)
                            }
                            "BANK", "bank" -> {
                                payMethod = getString(R.string.word_real_time_transfer)
                            }
                        }
                        text_goods_sale_history_detail_pay_method.text = payMethod
                        text_goods_sale_history_detail_buyer.text = data.buy!!.buyerName

                        text_goods_sale_history_detail_call.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${data.buy!!.buyerTel}")
                            startActivity(intent)
                        }

                        if(data.buy!!.buyerTel!!.length > 8){
                            text_goods_sale_history_detail_buyer_phone.text = FormatUtil.getPhoneNumber(data.buy!!.buyerTel).replaceRange(4, 8, "****")
                        }else{
                            text_goods_sale_history_detail_buyer_phone.text = data.buy!!.buyerTel!!.replaceRange(0, data.buy!!.buyerTel!!.length-1, "****")
                        }

                    }

                    var date: String? = null
                    when (data.process) {
                        EnumData.BuyGoodsProcess.PAY.process -> {
                            text_goods_sale_history_detail_date_title.setText(R.string.word_pay_date)
                            date = data.payDatetime
                            if (data.buy != null) {
                                text_goods_sale_history_detail_buyer_phone.text = FormatUtil.getPhoneNumber(data.buy!!.buyerTel)
                            }

                            text_goods_sale_history_detail_call.visibility = View.VISIBLE
                        }
                        EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
                            text_goods_sale_history_detail_date_title.setText(R.string.word_cancel_date)
                            date = data.cancelDatetime
                            text_goods_sale_history_detail_call.visibility = View.GONE
                        }
                        EnumData.BuyGoodsProcess.USE.process -> {
                            text_goods_sale_history_detail_date_title.setText(R.string.word_use_date2)
                            date = data.useDatetime
                            text_goods_sale_history_detail_call.visibility = View.GONE
                        }
                        else -> {
                            text_goods_sale_history_detail_date_title.setText(R.string.word_pay_date)
                            date = data.payDatetime
                            if (data.buy != null) {
                                text_goods_sale_history_detail_buyer_phone.text = FormatUtil.getPhoneNumber(data.buy!!.buyerTel)
                            }
                            text_goods_sale_history_detail_call.visibility = View.VISIBLE
                        }
                    }

                    try {
                        if (StringUtils.isNotEmpty(date)) {
                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date)

                            val output = SimpleDateFormat("yyyy.MM.dd")
                            text_goods_sale_history_detail_pay_date.text = output.format(d)
                        }

                    } catch (e: Exception) {

                    }

                    text_goods_sale_history_detail_total_price.text = FormatUtil.getMoneyType(data.price.toString())

                    when (data.process) {
                        EnumData.BuyGoodsProcess.PAY.process -> {
                            text_goods_sale_history_process.setTextColor(ResourceUtil.getColor(this@GoodsSaleHistoryDetailActivity, R.color.color_579ffb))
                            text_goods_sale_history_process.setText(R.string.word_pay_complete)
                        }
                        EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
                            text_goods_sale_history_process.setTextColor(ResourceUtil.getColor(this@GoodsSaleHistoryDetailActivity, R.color.color_ff4646))
                            text_goods_sale_history_process.setText(R.string.word_buy_cancel)
                        }
                        EnumData.BuyGoodsProcess.USE.process -> {
                            text_goods_sale_history_process.setTextColor(ResourceUtil.getColor(this@GoodsSaleHistoryDetailActivity, R.color.color_b7b7b7))
                            text_goods_sale_history_process.setText(R.string.word_use_complete)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuyGoods>>?, t: Throwable?, response: NewResultResponse<BuyGoods>?) {
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
