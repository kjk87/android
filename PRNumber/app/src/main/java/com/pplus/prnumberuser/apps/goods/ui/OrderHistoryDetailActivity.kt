//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_buy_history_detail.*
//import kotlinx.android.synthetic.main.item_buy_menu.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//
//class OrderHistoryDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_buy_history_detail
//    }
//
//    var mBuy: Buy? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mBuy = intent.getParcelableExtra(Const.DATA)
//
//        getBuy()
//    }
//
//    private fun getBuy(){
//        val params = HashMap<String, String>()
//        params["seqNo"] = mBuy!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//                if (response!!.data != null) {
//                    mBuy = response.data
//                    setData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setData() {
//        if(mBuy!!.buyGoodsList!![0].goods!!.page != null){
//            text_buy_history_detail_page_name.visibility = View.VISIBLE
//            text_buy_history_detail_page_name.text = mBuy!!.buyGoodsList!![0].goods!!.page!!.name
//            text_buy_history_detail_page_name.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//                PplusCommonUtil.goPage(this, mBuy!!.buyGoodsList!![0].goods!!.page!!, x, y)
//            }
//        }else{
//            text_buy_history_detail_page_name.visibility = View.GONE
//        }
//
//        layout_buy_history_detail_address.visibility = View.GONE
//        when(mBuy!!.orderType){
//            0->{//매장
//                text_buy_history_detail_order_type.setTextColor(ResourceUtil.getColor(this, R.color.color_ff696a))
//                text_buy_history_detail_order_type.setText(R.string.word_order_store)
//            }
//            1->{//포장
//                text_buy_history_detail_order_type.setTextColor(ResourceUtil.getColor(this, R.color.color_a26df3))
//                text_buy_history_detail_order_type.setText(R.string.word_order_packing)
//            }
//            2->{//배달
//                text_buy_history_detail_order_type.setTextColor(ResourceUtil.getColor(this, R.color.color_3ec082))
//                text_buy_history_detail_order_type.setText(R.string.word_order_delivery)
//                layout_buy_history_detail_address.visibility = View.VISIBLE
//                text_buy_history_detail_address.text = mBuy!!.clientAddress
//            }
//        }
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//
//        if(mBuy!!.buyGoodsList!![0].goods!!.isPlus!! || mBuy!!.buyGoodsList!![0].goods!!.isHotdeal!!){
//
//            if (StringUtils.isNotEmpty(mBuy!!.buyGoodsList!![0].useDatetime)) {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.buyGoodsList!![0].useDatetime)
//                date = output.format(d)
//                text_buy_history_detail_order_date.text = date
//            }
//
//        }else{
//            if (StringUtils.isNotEmpty(mBuy!!.regDatetime)) {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.regDatetime)
//                date = output.format(d)
//                text_buy_history_detail_order_date.text = date
//            }
//        }
//
//        if(mBuy!!.deliveryFee != null && mBuy!!.deliveryFee!! > 0){
//            layout_buy_history_detail_delivery_fee.visibility = View.VISIBLE
//            text_buy_history_detail_delivery_fee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuy!!.deliveryFee.toString()))
//        }
//
//        layout_buy_history_detail_cancel_date.visibility = View.GONE
//        layout_buy_history_detail_confirm_date.visibility = View.GONE
//        layout_buy_history_detail_complete_date.visibility = View.GONE
//
//        when (mBuy!!.orderProcess) {
//            EnumData.OrderProcess.ready.process -> {//접수대기
//                text_buy_history_detail_order_process.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
//                text_buy_history_detail_order_process.setText(R.string.word_order_ready)
//            }
//            EnumData.OrderProcess.ing.process -> {//처리중
//                text_buy_history_detail_order_process.setTextColor(ResourceUtil.getColor(this, R.color.color_737373))
//                text_buy_history_detail_order_process.setText(R.string.word_order_ing)
//
//                layout_buy_history_detail_confirm_date.visibility = View.VISIBLE
//                if (StringUtils.isNotEmpty(mBuy!!.confirmDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.confirmDatetime)
//                    date = output.format(d)
//                    text_buy_history_detail_confirm_date.text = date
//                }
//            }
//            EnumData.OrderProcess.complete.process -> {//완료
//                text_buy_history_detail_order_process.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                when(mBuy!!.orderType){
//                    2->{
//                        text_buy_history_detail_order_process.setText(R.string.word_delivery_complete)
//                    }
//                    else->{
//                        text_buy_history_detail_order_process.setText(R.string.word_order_complete)
//                    }
//                }
//
//                layout_buy_history_detail_complete_date.visibility = View.VISIBLE
//                if (StringUtils.isNotEmpty(mBuy!!.completeDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.completeDatetime)
//                    date = output.format(d)
//                    text_buy_history_detail_complete_date.text = date
//                }
//
//            }
//            EnumData.OrderProcess.cancel.process -> {//취소
//
//                if (StringUtils.isNotEmpty(mBuy!!.cancelDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.cancelDatetime)
//                    date = output.format(d)
//                    text_buy_history_detail_cancel_date.text = date
//                }
//
//                layout_buy_history_detail_cancel_date.visibility = View.VISIBLE
//                text_buy_history_detail_order_process.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//                text_buy_history_detail_order_process.setText(R.string.word_cancel)
//            }
//            EnumData.OrderProcess.shipping.process -> {//배송중
//                text_buy_history_detail_order_process.setTextColor(ResourceUtil.getColor(this, R.color.color_b7b7b7))
//            }
//        }
//
//        if(StringUtils.isEmpty(mBuy!!.bookDatetime)){
//            layout_buy_history_detail_reservation_date.visibility = View.GONE
//        }else{
//            layout_buy_history_detail_reservation_date.visibility = View.VISIBLE
//            val output = SimpleDateFormat("HH:mm")
//
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.bookDatetime)
//            text_buy_history_detail_reservation_date.text = output.format(d)
//
//        }
//
//        val buyGoodsList = mBuy!!.buyGoodsList
//
//        layout_buy_history_detail_menu_list.removeAllViews()
//        for(buyGoods in buyGoodsList!!){
//            val layout_menu = layoutInflater.inflate(R.layout.item_buy_menu, null)
//            val text_menu_name = layout_menu.text_buy_menu_name
//            val text_menu_price = layout_menu.text_buy_menu_price
//            text_menu_name.text = "${buyGoods.goods!!.name} X${buyGoods.count.toString()}"
//            text_menu_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(buyGoods.price.toString()))
//            layout_buy_history_detail_menu_list.addView(layout_menu)
//        }
//
//        text_buy_history_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuy!!.price.toString()))
//
//
//        var payMethod = ""
//        when (mBuy!!.payMethod) {
//            "CARD", "card" -> {
//                payMethod = getString(R.string.word_credit_card)
//            }
//            "BANK", "bank" -> {
//                payMethod = getString(R.string.word_real_time_transfer)
//            }
//        }
//
//        text_buy_history_detail_pay_method.text = payMethod
//
//        text_buy_history_detail_buyer.text = mBuy!!.buyerName
//        text_buy_history_detail_buyer_contact.text = mBuy!!.buyerTel
//
//        if(StringUtils.isEmpty(mBuy!!.memo)){
//            layout_buy_history_detail_memo.visibility = View.GONE
//        }else{
//            layout_buy_history_detail_memo.visibility = View.VISIBLE
//            text_buy_history_detail_memo.text = mBuy!!.memo
//        }
//
//
//
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_order_detail), ToolbarOption.ToolbarMenu.LEFT)
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
