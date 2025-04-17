//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.Editable
//import android.text.InputType
//import android.text.TextWatcher
//import android.view.View
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.coupon.ui.CouponPayCompleteActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.Lpng
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_goods_card.*
//import retrofit2.Call
//
//class GoodsCardActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_card
//    }
//
//    var mBuy: Buy? = null
//    var mLpng: Lpng? = null
//    var mPrice = 0
//    var mGoods:Goods? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mLpng = Lpng()
//        val installment = intent.getStringExtra(Const.INSTALLMENT)
//        mLpng!!.req_installment = installment
//        mBuy = intent.getParcelableExtra(Const.BUY)
//        mPrice = intent.getIntExtra(Const.PRICE, 0)
//        mGoods = intent.getParcelableExtra(Const.GOODS)
//
//        text_goods_card_page_name.text = mGoods!!.page!!.name
//        text_goods_card_goods_name.text = mBuy!!.title
//
//        if(installment == "00"){
//            text_goods_card_installment.setText(R.string.word_one_pay)
//        }else{
//            text_goods_card_installment.text = getString(R.string.format_installment_period, installment)
//        }
//
//        text_goods_card_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(mPrice.toString())))
//
//        text_goods_card_personal_card.setOnClickListener {
//            text_goods_card_personal_card.isSelected = true
//            text_goods_card_corporate_card.isSelected = false
//            layout_goods_card_birthday.visibility = View.VISIBLE
//            layout_goods_card_biz_no.visibility = View.GONE
//        }
//
//        text_goods_card_corporate_card.setOnClickListener {
//            text_goods_card_personal_card.isSelected = false
//            text_goods_card_corporate_card.isSelected = true
//            layout_goods_card_birthday.visibility = View.GONE
//            layout_goods_card_biz_no.visibility = View.VISIBLE
//        }
//
//        text_goods_card_cancel.setOnClickListener {
//            finish()
//        }
//
//        edit_goods_card_no1.setSingleLine()
//        edit_goods_card_no2.setSingleLine()
//        edit_goods_card_no3.setSingleLine()
//        edit_goods_card_no4.setSingleLine()
//
//        edit_goods_card_validity_month.setSingleLine()
//        edit_goods_card_validity_year.setSingleLine()
//        edit_goods_card_password.setSingleLine()
//        edit_goods_card_birthday.setSingleLine()
//        edit_goods_card_biz_no.setSingleLine()
//
//        edit_goods_card_no3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
//        edit_goods_card_no4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
//        edit_goods_card_validity_month.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
//        edit_goods_card_validity_year.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
//        edit_goods_card_password.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
//
//        edit_goods_card_no1.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 4){
//                    edit_goods_card_no2.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        edit_goods_card_no2.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 4){
//                    edit_goods_card_no3.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        edit_goods_card_no3.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 4){
//                    edit_goods_card_no4.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        edit_goods_card_no4.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 4){
//                    edit_goods_card_validity_month.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        edit_goods_card_validity_month.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 2){
//                    edit_goods_card_validity_year.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        edit_goods_card_validity_year.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                if(s?.length == 2){
//                    edit_goods_card_password.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })
//
//        text_goods_card_pay.setOnClickListener {
//
//            val cardNo1 = edit_goods_card_no1.text.toString()
//            val cardNo2 = edit_goods_card_no2.text.toString()
//            val cardNo3 = edit_goods_card_no3.text.toString()
//            val cardNo4 = edit_goods_card_no4.text.toString()
//
//            if (StringUtils.isEmpty(cardNo1) || StringUtils.isEmpty(cardNo2) || StringUtils.isEmpty(cardNo3) || StringUtils.isEmpty(cardNo4)) {
//                showAlert(R.string.msg_input_card_no)
//                return@setOnClickListener
//            }
//
//            if (cardNo1.length < 4 || cardNo2.length < 4 || cardNo3.length < 4 || cardNo4.length < 4) {
//                showAlert(R.string.msg_input_valid_card_no)
//                return@setOnClickListener
//            }
//
//            mLpng!!.req_cardNo = cardNo1 + cardNo2 + cardNo3 + cardNo4
//
//            val mm = edit_goods_card_validity_month.text.toString()
//            val yy = edit_goods_card_validity_year.text.toString()
//            mLpng!!.req_cardMonth = mm
//            mLpng!!.req_cardYear = "20$yy"
//
//            val pwd = edit_goods_card_password.text.toString()
//            mLpng!!.req_cardPwd = pwd
//
//            if (text_goods_card_personal_card.isSelected) {
//                val birthday = edit_goods_card_birthday.text.toString()
//                if (StringUtils.isEmpty(birthday) || birthday.length < 6) {
//                    showAlert(R.string.msg_input_card_birthday2)
//                    return@setOnClickListener
//                }
//                mLpng!!.req_identity = birthday
//
//            } else if (text_goods_card_corporate_card.isSelected) {
//                val bizNo = edit_goods_card_biz_no.text.toString()
//                if (StringUtils.isEmpty(bizNo) || bizNo.length < 10) {
//                    showAlert(R.string.msg_input_card_biz_no2)
//                    return@setOnClickListener
//                }
//                mLpng!!.req_identity = bizNo
//            }
//
//            mLpng!!.shopcode = mGoods!!.page!!.shopCode
//            mLpng!!.order_req_amt = mPrice.toString()
//            mLpng!!.order_hp = mBuy!!.buyerTel
//            mLpng!!.order_name = mBuy!!.buyerName
//            mLpng!!.comp_memno = mGoods!!.page!!.seqNo.toString()
////            mLpng!!.loginId = LoginInfoManager.getInstance().user.loginId
//            mLpng!!.order_goodsname = mBuy!!.title
//            getOrderId()
//        }
//
//        text_goods_card_personal_card.isSelected = true
//        text_goods_card_corporate_card.isSelected = false
//        layout_goods_card_birthday.visibility = View.VISIBLE
//        layout_goods_card_biz_no.visibility = View.GONE
//
//    }
//
//    private fun getOrderId() {
//        showProgress("")
//        ApiBuilder.create().lpngOrderId.setCallback(object : PplusCallback<NewResultResponse<String>> {
//            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//
//                hideProgress()
//                if (response != null) {
//                    buy(response.data)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun buy(orderId: String) {
//        mBuy!!.orderId = orderId
//        mBuy!!.pg = "LPNG"
//        if (mGoods!!.isPlus!! || mGoods!!.isHotdeal!!) {
//            showProgress("")
//
//            ApiBuilder.create().postBuyHot(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    if (response != null && response.data != null) {
//
//                        if (response.data.seqNo != null) {
//                            lpngPay(orderId)
//                            return
//                        }
//                    }
//                    showAlert(R.string.msg_cancel_pg)
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    showAlert(R.string.msg_cancel_pg)
//                }
//            }).build().call()
//        } else if (mGoods!!.type == "0") {
//            showProgress("")
//            ApiBuilder.create().postBuy(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    if (response != null && response.data != null) {
//
//                        if (response.data.seqNo != null) {
//                            lpngPay(orderId)
//                            return
//                        }
//                    }
//                    showAlert(R.string.msg_cancel_pg)
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    showAlert(R.string.msg_cancel_pg)
//                }
//            }).build().call()
//        }else {
//            showProgress("")
//            ApiBuilder.create().postBuyShop(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    if (response != null && response.data != null) {
//
//                        if (response.data.seqNo != null) {
//                            lpngPay(orderId)
//                            return
//                        }
//                    }
//                    showAlert(R.string.msg_cancel_pg)
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                    hideProgress()
//                    showAlert(R.string.msg_cancel_pg)
//                }
//            }).build().call()
//        }
//    }
//
//    private fun lpngPay(orderId: String) {
//        mLpng!!.comp_orderno = orderId
//        showProgress("")
//        ApiBuilder.create().postBuyLpng(mLpng).setCallback(object : PplusCallback<NewResultResponse<Lpng>> {
//            override fun onResponse(call: Call<NewResultResponse<Lpng>>?, response: NewResultResponse<Lpng>?) {
//                hideProgress()
//                orderComplete(orderId)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Lpng>>?, t: Throwable?, response: NewResultResponse<Lpng>?) {
//                hideProgress()
//                if(response!= null && response.data != null && StringUtils.isNotEmpty(response.data.errMessage)){
//                    showAlert(response.data.errMessage)
//                }
//            }
//        }).build().call()
//    }
//
//    private fun orderComplete(orderId: String) {
//        if(mGoods!!.isCoupon != null && mGoods!!.isCoupon!!){
//            val intent = Intent(this, CouponPayCompleteActivity::class.java)
//            intent.putExtra(Const.ID, orderId)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_ORDER_FINISH)
//        }else{
//            val intent = Intent(this, PayCompleteActivity::class.java)
//            intent.putExtra(Const.ID, orderId)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_ORDER_FINISH)
//        }
//
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_card_pay), ToolbarOption.ToolbarMenu.LEFT)
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
