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
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_sale_goods_detail.*
import kotlinx.android.synthetic.main.item_order_detail_menu.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleGoodsDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_goods_detail
    }

    var mBuyGoods: BuyGoods? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuyGoods = intent.getParcelableExtra(Const.DATA)
        getBuyGoods()
    }

    private fun getBuyGoods() {
        val params = HashMap<String, String>()
        params["seqNo"] = mBuyGoods!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getOneBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoods>> {
            override fun onResponse(call: Call<NewResultResponse<BuyGoods>>?, response: NewResultResponse<BuyGoods>?) {
                hideProgress()

                if (response != null) {
                    mBuyGoods = response.data
                    text_sale_goods_detail_reg_date.text = mBuyGoods!!.regDatetime

                    layout_sale_goods_detail_goods_list.removeAllViews()
                    val view = layoutInflater.inflate(R.layout.item_order_detail_menu, null)

                    if(mBuyGoods!!.goods != null && mBuyGoods!!.goods!!.attachments != null && mBuyGoods!!.goods!!.attachments!!.images != null && mBuyGoods!!.goods!!.attachments!!.images!!.isNotEmpty()){
                        val imageNo = mBuyGoods!!.goods!!.attachments!!.images!![0]
                        val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${imageNo}")
                        Glide.with(this@SaleGoodsDetailActivity).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(view.image_order_detail_menu)
                    }

                    view.text_order_detail_menu_name.text = mBuyGoods!!.goods!!.name
                    view.text_order_detail_menu_count.text = getString(R.string.format_count_unit, mBuyGoods!!.count.toString())
                    layout_sale_goods_detail_goods_list.addView(view)

                    if (StringUtils.isNotEmpty(mBuyGoods!!.memo)) {
                        layout_sale_goods_detail_memo.visibility = View.VISIBLE
                        text_sale_goods_detail_memo.text = mBuyGoods!!.memo
                    }

                    text_sale_goods_detail_client_name.text = mBuyGoods!!.buy!!.buyerName
                    text_sale_goods_detail_client_phone.text = mBuyGoods!!.buy!!.buyerTel

                    text_sale_goods_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuyGoods!!.price.toString()))
                    text_sale_goods_detail_call.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${mBuyGoods!!.buy!!.buyerTel}")
                        startActivity(intent)
                    }

                    when (mBuyGoods!!.process) {
                        1 -> {//결제완료
                            text_sale_goods_detail_type.setTextColor(ResourceUtil.getColor(this@SaleGoodsDetailActivity, R.color.color_737373))
                            text_sale_goods_detail_type.setText(R.string.word_pay_complete)
                            text_sale_goods_detail_date_title.setText(R.string.word_pay_date)
                            layout_sale_goods_detail_date.visibility = View.GONE
                            text_sale_goods_detail_desc.setText(R.string.msg_buy_complete_history)
                        }
                        2 -> {//취소
                            text_sale_goods_detail_type.setTextColor(ResourceUtil.getColor(this@SaleGoodsDetailActivity, R.color.color_ff4646))
                            text_sale_goods_detail_type.setText(R.string.word_purchase_cancel)
                            layout_sale_goods_detail_date.visibility = View.VISIBLE
                            text_sale_goods_detail_date_title.setText(R.string.word_order_cancel_date)
                            text_sale_goods_detail_date.text = mBuyGoods!!.cancelDatetime
                            text_sale_goods_detail_date.setTextColor(ResourceUtil.getColor(this@SaleGoodsDetailActivity, R.color.color_ff4646))
                            text_sale_goods_detail_desc.setText(R.string.msg_buy_canceled_history)
                        }
                        3 -> {//사용완료
                            text_sale_goods_detail_type.setTextColor(ResourceUtil.getColor(this@SaleGoodsDetailActivity, R.color.color_b7b7b7))
                            text_sale_goods_detail_type.setText(R.string.word_use_complete)
                            layout_sale_goods_detail_date.visibility = View.VISIBLE
                            text_sale_goods_detail_date_title.setText(R.string.word_use_complete_date)
                            text_sale_goods_detail_date.text = mBuyGoods!!.useDatetime
                            text_sale_goods_detail_date.setTextColor(ResourceUtil.getColor(this@SaleGoodsDetailActivity, R.color.color_b7b7b7))
                            text_sale_goods_detail_desc.setText(R.string.msg_use_complete_history)
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
