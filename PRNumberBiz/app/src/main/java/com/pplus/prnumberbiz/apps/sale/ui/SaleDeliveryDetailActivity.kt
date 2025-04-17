package com.pplus.prnumberbiz.apps.sale.ui

import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.model.dto.Delivery
import kotlinx.android.synthetic.main.activity_sale_delivery_detail.*
import kotlinx.android.synthetic.main.item_order_detail_menu.view.*

class SaleDeliveryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_delivery_detail
    }

    var mDelivery: Delivery? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mDelivery = intent.getParcelableExtra(Const.DATA)


        when (mDelivery!!.payment) {
            getString(R.string.word_pre_pay) -> {
                text_sale_delivery_detail_type.setText(R.string.word_pre_pay)
            }
            getString(R.string.word_card) -> {
                text_sale_delivery_detail_type.setText(R.string.word_card_pay)
            }
            getString(R.string.word_cash) -> {
                text_sale_delivery_detail_type.setText(R.string.word_cash_pay)
            }
        }


        text_sale_delivery_detail_date.text = mDelivery!!.regDatetime

        layout_sale_delivery_detail_goods_list.removeAllViews()
        for (deliveryGoods in mDelivery!!.deliveryGoodsList!!) {

            val view = layoutInflater.inflate(R.layout.item_order_detail_menu, null)


            view.image_order_detail_menu.visibility = View.GONE

            view.text_order_detail_menu_name.text = deliveryGoods.name
            view.text_order_detail_menu_count.text = getString(R.string.format_count_unit, deliveryGoods.count.toString())
            layout_sale_delivery_detail_goods_list.addView(view)
        }

        text_sale_order_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mDelivery!!.totalPrice.toString()))

        if (StringUtils.isNotEmpty(mDelivery!!.clientMemo)) {
            layout_sale_delivery_detail_memo.visibility = View.VISIBLE
            text_sale_delivery_detail_memo.text = mDelivery!!.clientMemo
        }
        text_sale_delivery_detail_delivery_address.text = mDelivery!!.clientAddress
        text_sale_delivery_detail_contact.text = mDelivery!!.clientTel

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
