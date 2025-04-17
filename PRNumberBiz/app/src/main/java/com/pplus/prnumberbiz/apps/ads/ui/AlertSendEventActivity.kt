package com.pplus.prnumberbiz.apps.ads.ui

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_alert_send_event.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class AlertSendEventActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_send_event
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val goods = intent.getParcelableExtra<Goods>(Const.DATA)

        text_alert_send_event_goods_name.text = goods.name
        if (goods.originPrice != null && goods.originPrice!! > 0) {

            if (goods.originPrice!! <= goods.price!!) {
                text_alert_send_event_goods_origin_price.visibility = View.GONE
            } else {
                text_alert_send_event_goods_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(goods.originPrice.toString()))
                text_alert_send_event_goods_origin_price.visibility = View.VISIBLE
            }

        } else {
            text_alert_send_event_goods_origin_price.visibility = View.GONE
        }

        text_alert_send_event_goods_origin_price.paintFlags = text_alert_send_event_goods_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        text_alert_send_event_goods_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(goods.price.toString())))


        if (goods.count != null && goods.count != -1) {
            var soldCount = 0
            if (goods.soldCount != null) {
                soldCount = goods.soldCount!!
            }
            text_alert_send_event_goods_remain_count.visibility = View.VISIBLE
            text_alert_send_event_goods_remain_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_count, FormatUtil.getMoneyType((goods.count!! - soldCount).toString())))
        } else {
            text_alert_send_event_goods_remain_count.visibility = View.GONE
        }

        if (goods.attachments != null && goods.attachments!!.images != null && goods.attachments!!.images!!.isNotEmpty()) {
            val id = goods.attachments!!.images!![0]
            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
            Glide.with(this).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_alert_send_event_goods_image)

        } else {
            image_alert_send_event_goods_image.setImageResource(R.drawable.prnumber_default_img)
        }

        image_alert_send_event_close.setOnClickListener {
            finish()
        }

        text_alert_send_event_send.setOnClickListener {
            sendEvent(goods)
        }

    }

    private fun sendEvent(goods: Goods) {
        val params = HashMap<String, String>()

        params["seqNo"] = goods.seqNo.toString()
        ApiBuilder.create().goodsNews(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                showAlert(R.string.msg_sent_goods_news)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                showAlert(R.string.msg_alert_error_goods_news, 5)
                finish()
            }
        }).build().call()
    }
}
