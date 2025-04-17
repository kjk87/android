package com.pplus.prnumberbiz.apps.sale.ui

import android.os.Bundle
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoodsTypePrice
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_alert_sale_order.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class AlertSaleOrderActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_sale_order
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val start = intent.getStringExtra(Const.START)
        val end = intent.getStringExtra(Const.END)

        image_alert_sale_order_close.setOnClickListener {
            finish()
        }

        getTotalPrice(start, end)
    }

    private fun getTotalPrice(start: String, end: String) {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["type"] = "0"
        params["startDuration"] = start
        params["endDuration"] = end
        showProgress("")
        ApiBuilder.create().getPriceGoodsType(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoodsTypePrice>> {
            override fun onResponse(call: Call<NewResultResponse<BuyGoodsTypePrice>>?, response: NewResultResponse<BuyGoodsTypePrice>?) {
                hideProgress()
                if (response != null) {
                    val data = response.data

                    text_alert_sale_order_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(data.totalPrice!!.toInt().toString()))
                    text_alert_sale_order_normal_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(data.generalPrice!!.toInt().toString()))
                    text_alert_sale_order_hotdeal_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(data.hotdealPrice!!.toInt().toString()))
                    text_alert_sale_order_plus_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(data.plusPrice!!.toInt().toString()))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuyGoodsTypePrice>>?, t: Throwable?, response: NewResultResponse<BuyGoodsTypePrice>?) {
                hideProgress()
            }
        }).build().call()
    }

}
