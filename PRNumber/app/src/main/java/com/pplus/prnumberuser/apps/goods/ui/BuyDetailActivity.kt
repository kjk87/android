//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.view.View
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.BuyGoodsAdapter
//import com.pplus.prnumberuser.apps.goods.data.CouponBuyGoodsAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_buy_detail.*
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.apps.product.ui.CheckAuthCodeActivity
//import retrofit2.Call
//import java.text.SimpleDateFormat
//
//class BuyDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_buy_detail
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
//    private fun getBuy() {
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
//
//        if(mBuy!!.page != null){
//            if (StringUtils.isNotEmpty(mBuy!!.page!!.thumbnail)) {
//                Glide.with(this).load(mBuy!!.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(image_buy_detail_page_image)
//            } else {
//                image_buy_detail_page_image.setImageResource(R.drawable.img_page_profile_circle_default)
//            }
//
//            text_ot_deal_history_detail_page_name.text = mBuy!!.page!!.name
//        }
//
//
//        text_buy_detail_use.setOnClickListener {
//            val intent = Intent(this, CheckAuthCodeActivity::class.java)
//            intent.putExtra(Const.DATA, mBuy)
//            startActivityForResult(intent, Const.REQ_USE)
//        }
//
//        when (mBuy!!.buyGoodsList!![0].process) {
//            EnumData.BuyGoodsProcess.PAY.process -> {
//                val goods = mBuy!!.buyGoodsList!![0].goods
//                    if(goods!!.isHotdeal!! || goods.isPlus!!){
//                    text_buy_detail_buy_cancel.visibility = View.GONE
//                }else{
//                    text_buy_detail_buy_cancel.visibility = View.VISIBLE
//                }
//
////                text_buy_detail_buy_cancel.visibility = View.VISIBLE
//
//                text_buy_detail_use.visibility = View.VISIBLE
//                text_buy_detail_use_date.visibility = View.GONE
//            }
//            EnumData.BuyGoodsProcess.USE.process -> {
//                text_buy_detail_buy_cancel.visibility = View.GONE
//                text_buy_detail_use.visibility = View.GONE
//                text_buy_detail_use_date.visibility = View.VISIBLE
//
//                var date = ""
//                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//                if (StringUtils.isNotEmpty(mBuy!!.buyGoodsList!![0].useDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.buyGoodsList!![0].useDatetime)
//                    date = output.format(d)
//                    text_buy_detail_use_date.text = getString(R.string.format_use_date2, date)
//                } else {
//                    text_buy_detail_use_date.visibility = View.GONE
//                }
//            }
//            EnumData.BuyGoodsProcess.CANCEL.process -> {
//                text_buy_detail_buy_cancel.visibility = View.GONE
//                text_buy_detail_use.visibility = View.GONE
//                text_buy_detail_use_date.visibility = View.VISIBLE
//
//                var date = ""
//                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//                if (StringUtils.isNotEmpty(mBuy!!.buyGoodsList!![0].cancelDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.buyGoodsList!![0].cancelDatetime)
//                    date = output.format(d)
//                    text_buy_detail_use_date.text = getString(R.string.format_cancel_date2, date)
//                } else {
//                    text_buy_detail_use_date.visibility = View.GONE
//                }
//            }
//            EnumData.BuyGoodsProcess.EXPIRE.process -> {
//                val goods = mBuy!!.buyGoodsList!![0].goods
//                if(!goods!!.isHotdeal!! && !goods.isPlus!!){
//                    text_buy_detail_buy_cancel.visibility = View.VISIBLE
//                }else{
//                    text_buy_detail_buy_cancel.visibility = View.GONE
//                }
//
//                text_buy_detail_use.visibility = View.GONE
//                text_buy_detail_use_date.visibility = View.VISIBLE
//
//                var date = ""
//                val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//                if (StringUtils.isNotEmpty(mBuy!!.buyGoodsList!![0].expireDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.buyGoodsList!![0].expireDatetime)
//                    date = output.format(d)
//                    text_buy_detail_use_date.text = getString(R.string.format_expire_date4, date)
//                } else {
//                    text_buy_detail_use_date.visibility = View.GONE
//                }
//            }
//        }
//
//
//        if (mBuy!!.process != EnumData.BuyGoodsProcess.CANCEL.process && mBuy!!.process != EnumData.BuyGoodsProcess.BIZ_CANCEL.process && mBuy!!.process != EnumData.BuyGoodsProcess.EXPIRE.process && mBuy!!.buyGoodsList!![0].process != EnumData.BuyGoodsProcess.EXPIRE.process && mBuy!!.savedPoint != null && mBuy!!.savedPoint!! > 0) {
//            text_buy_detail_point.visibility = View.VISIBLE
//            if (mBuy!!.isPaymentPoint != null && mBuy!!.isPaymentPoint!!) {
//                text_buy_detail_point.text = getString(R.string.format_saved, FormatUtil.getMoneyType(mBuy!!.savedPoint.toString()))
//            } else {
//                text_buy_detail_point.text = getString(R.string.format_will_save, FormatUtil.getMoneyType(mBuy!!.savedPoint.toString()))
//            }
//        } else {
//            text_buy_detail_point.visibility = View.GONE
//        }
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        if (StringUtils.isNotEmpty(mBuy!!.buyGoodsList!![0].payDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBuy!!.buyGoodsList!![0].payDatetime)
//            date = output.format(d)
//        }
//        text_buy_detail_pay_date.text = date
//
//        text_buy_detail_buyer_name.text = mBuy!!.buyerName
//        text_buy_detail_buyer_contact.text = mBuy!!.buyerTel
//
//        text_buy_detail_buy_cancel.setOnClickListener {
//            val intent = Intent(this, BuyCancelInfoActivity::class.java)
//            intent.putExtra(Const.DATA, mBuy)
//            startActivityForResult(intent, Const.REQ_BUY_CANCEL)
//        }
//
////        text_buy_detail_count.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(mBuy!!.buyGoodsList!![0].count.toString()))
//        text_buy_detail_total_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mBuy!!.price.toString()))
//
//        var payMethod = ""
//
//        when (mBuy!!.payMethod) {
//            "CARD", "card" -> {
//                payMethod = getString(R.string.word_credit_card)
//            }
//            "BANK", "bank" -> {
//                payMethod = getString(R.string.word_real_time_transfer)
//            }
//        }
//
//        text_buy_detail_pay_method.text = payMethod
//
//        recycler_hot_deal_buy_goods.layoutManager = LinearLayoutManager(this)
//        if (mBuy!!.type == "2") {
//            val adapter = CouponBuyGoodsAdapter(this, mBuy!!)
//            recycler_hot_deal_buy_goods.adapter = adapter
//            adapter.listener = object : CouponBuyGoodsAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//
//                }
//
//                override fun onRefresh() {
//                    getBuy()
//                }
//            }
//
//            adapter.setDataList(mBuy!!.buyGoodsList!!.toMutableList())
//        } else {
//            val adapter = BuyGoodsAdapter(this, mBuy!!)
//            recycler_hot_deal_buy_goods.adapter = adapter
//            adapter.listener = object : BuyGoodsAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//
//                }
//
//                override fun onRefresh() {
//                    getBuy()
//                }
//            }
//
//            adapter.setDataList(mBuy!!.buyGoodsList!!.toMutableList())
//        }
//
//
//    }
//
//    private fun use(buySeqNo: Long) {
//        val params = HashMap<String, String>()
//        params["buySeqNo"] = buySeqNo.toString()
//        showProgress("")
//        ApiBuilder.create().buyGoodsListUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                showAlert(R.string.msg_use_complete)
//                setResult(Activity.RESULT_OK)
//                getBuy()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_BUY_CANCEL -> {
//                getBuy()
//            }
//            Const.REQ_USE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    use(mBuy!!.seqNo!!)
//                }
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
