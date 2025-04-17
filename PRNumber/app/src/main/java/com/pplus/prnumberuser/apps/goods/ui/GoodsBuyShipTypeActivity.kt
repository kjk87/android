//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.CompoundButton
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.card.data.CardSelectAdapter
//import com.pplus.prnumberuser.apps.card.ui.CardConfigActivity
//import com.pplus.prnumberuser.apps.card.ui.CardRegActivity
//import com.pplus.prnumberuser.apps.card.ui.PayPasswordCheckActivity
//import com.pplus.prnumberuser.apps.card.ui.PayPasswordSetActivity
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsBuyShipTypeAdapter
//import com.pplus.prnumberuser.apps.shippingsite.ui.ShippingSiteConfigActivity
//import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_goods_buy_ship_type.*
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.logs.LogUtil
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//
//class GoodsBuyShipTypeActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
//    override fun getPID(): String {
//        return "Main_surrounding sale_product detail_buy"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_buy_ship_type
//    }
//
//    var mPayMethod = "card"
//    var mInstallment = "00"
//
//    //    var mKey = ""
//    var mBuyGoodsList: ArrayList<BuyGoods>? = null
//    var mBuy = Buy()
//    private var mOrderType = -1
//    private var mCardSelectAdapter: CardSelectAdapter? = null
//    private var mSelectShippingSite:ShippingSite? = null
//    private var mGoodsBuyShipTypeAdapter:GoodsBuyShipTypeAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mOrderType = intent.getIntExtra(Const.TYPE, -1)
//        mBuyGoodsList = intent.getParcelableArrayListExtra(Const.BUY_GOODS)
//
//
//
//        text_goods_buy_ship_type_view_terms1.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS1)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_goods_buy_ship_type_view_terms2.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS2)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_goods_buy_ship_type_site_config.setOnClickListener {
//            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH_ADDRESS)
//        }
//
//        text_goods_buy_ship_type_not_exist_site.setOnClickListener {
//            val intent = Intent(this, ShippingSiteConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH_ADDRESS)
//        }
//
//        text_goods_buy_ship_type_card_config.setOnClickListener {
//            val intent = Intent(this, CardConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_CARD_CONFIG)
//        }
//
//        edit_goods_buy_ship_type_name.setSingleLine()
//        edit_goods_buy_ship_type_phone.setSingleLine()
//
//        if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
//            edit_goods_buy_ship_type_name.setText(LoginInfoManager.getInstance().user.name)
//        }
//
//        edit_goods_buy_ship_type_phone.setText(LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE+"##", ""))
//
//        val contents = resources.getStringArray(R.array.report_installment_period)
//        text_goods_buy_ship_type_installment.setOnClickListener {
//
//            val builder = AlertBuilder.Builder()
//            builder.setContents(*contents)
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO)
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.RADIO -> {
//
//                            text_goods_buy_ship_type_installment.text = contents[event_alert.value - 1]
//                            if (event_alert.value == 1) {
//                                mInstallment = "00"
//                            } else if (event_alert.value < 10) {
//                                mInstallment = "0${event_alert.value}"
//                            } else {
//                                mInstallment = event_alert.value.toString()
//                            }
//                        }
//                    }
//                }
//            }).builder().show(this)
//        }
//
//        mInstallment = "00"
//        text_goods_buy_ship_type_installment.text = contents[0]
//
//        text_goods_buy_ship_type_free_installment.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(this, getString(R.string.msg_goods_card_info_url) + "?timestamp=" + System.currentTimeMillis())
//        }
//
//        text_goods_buy_ship_type_pay.setOnClickListener {
//
//            if (!check_goods_buy_ship_type_terms1.isChecked || !check_goods_buy_ship_type_terms2.isChecked) {
//
//                ToastUtil.show(this, getString(R.string.msg_agree_terms))
//                return@setOnClickListener
//            }
//
//            mBuy.payMethod = "card"
//
//            val name = edit_goods_buy_ship_type_name.text.toString().trim()
//            if (StringUtils.isEmpty(name)) {
//                showAlert(R.string.hint_input_name)
//                return@setOnClickListener
//            }
//
//            val phone = edit_goods_buy_ship_type_phone.text.toString().trim()
//            if (StringUtils.isEmpty(phone)) {
//                showAlert(R.string.hint_input_contact)
//                return@setOnClickListener
//            }
//
//            if (!FormatUtil.isPhoneNumber(phone)) {
//                showAlert(R.string.msg_invalid_phone_number)
//                return@setOnClickListener
//            }
//
//            val memo = text_goods_buy_ship_type_memo.text.toString().trim()
//
//            mBuy.buyerName = LoginInfoManager.getInstance().user.name
//            mBuy.buyerTel = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE+"##", "")
//
//            if(mSelectShippingSite == null){
//                showAlert(R.string.msg_select_shipping_site)
//                return@setOnClickListener
//            }
//
//            val buyGoodsList = ArrayList<BuyGoods>()
//            for (item in mBuyGoodsList!!) {
//
//                val buyGoods = BuyGoods()
//                buyGoods.goodsSeqNo = item.goods!!.seqNo
//                buyGoods.goodsPriceSeqNo = item.goodsPriceData!!.seqNo
//                buyGoods.count = item.count
//                buyGoods.deliveryFee  = item.deliveryFee
//                if (StringUtils.isNotEmpty(memo)) {
//                    buyGoods.deliveryMemo = memo
//                }
//                buyGoods.receiverName = name
//                buyGoods.receiverTel = phone
//                buyGoods.receiverAddress = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
//                buyGoods.receiverPostCode = mSelectShippingSite!!.postCode
//                buyGoods.buyGoodsOptionSelectList = item.buyGoodsOptionSelectList
//                buyGoodsList.add(buyGoods)
//            }
//
//            mBuy.buyGoodsSelectList = buyGoodsList
//            if (mBuyGoodsList!!.size > 1) {
//                mBuy.title = getString(R.string.format_other2, mBuyGoodsList!![0].goods!!.name, mBuyGoodsList!!.size - 1)
//            } else {
//                mBuy.title = mBuyGoodsList!![0].goods!!.name
//            }
//
//            if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//                    override fun onCancel() {}
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val intent = Intent(this@GoodsBuyShipTypeActivity, VerificationMeActivity::class.java)
//                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivityForResult(intent, Const.REQ_VERIFICATION)
//                            }
//                        }
//                    }
//                }).builder().show(this)
//            } else {
//                if (mCardSelectAdapter!!.mSelectData == null) {
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(getString(R.string.word_notice_alert))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_exist_card), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
//                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                            when (event_alert) {
//                                AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                    val intent = Intent(this@GoodsBuyShipTypeActivity, CardRegActivity::class.java)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivityForResult(intent, Const.REQ_REG)
//                                }
//                            }
//
//                        }
//                    }).builder().show(this)
//                } else {
////                    if ((mBuyGoodsList!![0].goods!!.isHotdeal != null && mBuyGoodsList!![0].goods!!.isHotdeal!!) || (mBuyGoodsList!![0].goods!!.isPlus != null && mBuyGoodsList!![0].goods!!.isPlus!!)) {
////                        val intent = Intent(this, AlertGoodsBuyActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        startActivityForResult(intent, Const.REQ_BUY)
////                    } else {
////
////                        val intent = Intent(this, PayPasswordCheckActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
////                    }
//
//                    val intent = Intent(this, PayPasswordCheckActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
//                }
//            }
//        }
//
//        mPayMethod = "card"
//        mBuy.memberSeqNo = LoginInfoManager.getInstance().user.no
//
//        if (mOrderType != -1) {
//            mBuy.orderType = mOrderType
//        }
//
//        mGoodsBuyShipTypeAdapter = GoodsBuyShipTypeAdapter(mBuyGoodsList!![0])
//        recycler_goods_buy_ship_type_buy_goods.adapter = mGoodsBuyShipTypeAdapter
////        adapter.setDataList(mBuyGoodsList!!)
//        recycler_goods_buy_ship_type_buy_goods.layoutManager = LinearLayoutManager(this)
//
//        check_goods_buy_ship_type_totalAgree.isChecked = true
//        check_goods_buy_ship_type_terms1.isChecked = true
//        check_goods_buy_ship_type_terms2.isChecked = true
//
//        check_goods_buy_ship_type_totalAgree.setOnCheckedChangeListener(this)
//        check_goods_buy_ship_type_terms1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_goods_buy_ship_type_terms2.isChecked) {
//                    isAll = true
//                }
//                check_goods_buy_ship_type_totalAgree.setOnCheckedChangeListener(null)
//                check_goods_buy_ship_type_totalAgree.isChecked = isAll
//                check_goods_buy_ship_type_totalAgree.setOnCheckedChangeListener(this@GoodsBuyShipTypeActivity)
//            }
//        })
//
//        check_goods_buy_ship_type_terms2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_goods_buy_ship_type_terms1.isChecked) {
//                    isAll = true
//                }
//                check_goods_buy_ship_type_totalAgree.setOnCheckedChangeListener(null)
//                check_goods_buy_ship_type_totalAgree.isChecked = isAll
//                check_goods_buy_ship_type_totalAgree.setOnCheckedChangeListener(this@GoodsBuyShipTypeActivity)
//            }
//        })
//
//        setTotalPrice()
//
//        mCardSelectAdapter = CardSelectAdapter()
//        recycler_goods_buy_card.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recycler_goods_buy_card.adapter = mCardSelectAdapter
//        cardListCall()
//        getShippingSiteList()
//    }
//
//    private fun getShippingSiteList(){
//        ApiBuilder.create().shippingSiteList.setCallback(object : PplusCallback<NewResultResponse<ShippingSite>>{
//            override fun onResponse(call: Call<NewResultResponse<ShippingSite>>?, response: NewResultResponse<ShippingSite>?) {
//                if(response?.datas != null){
//                    if(response.datas.isNotEmpty()){
//                        text_goods_buy_ship_type_not_exist_site.visibility = View.GONE
//                        layout_goods_buy_ship_type_exist_site.visibility = View.VISIBLE
//                        mSelectShippingSite = response.datas[0]
//                        text_goods_buy_ship_type_site_name.text = mSelectShippingSite!!.siteName
//                        text_goods_buy_ship_type_site_address.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
//                        checkIslandsRegion()
//                    }else{
//                        text_goods_buy_ship_type_not_exist_site.visibility = View.VISIBLE
//                        layout_goods_buy_ship_type_exist_site.visibility = View.GONE
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<ShippingSite>>?, t: Throwable?, response: NewResultResponse<ShippingSite>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun checkIslandsRegion(){
//        if(mSelectShippingSite != null && mBuyGoodsList!![0].goods!!.deliveryAddFee != null && mBuyGoodsList!![0].goods!!.deliveryAddFee!! > 0){
//            val params = HashMap<String, String>()
//            params["postCode"] = mSelectShippingSite!!.postCode!!
//            showProgress("")
//            ApiBuilder.create().checkIslandsRegion(params).setCallback(object : PplusCallback<NewResultResponse<Boolean>> {
//                override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
//                    hideProgress()
//                    if(response?.data != null && response.data){
//                        if(mBuyGoodsList!![0].deliveryFee == null){
//                            mBuyGoodsList!![0].deliveryFee = 0
//                        }
//                        mBuyGoodsList!![0].deliveryFee = mBuyGoodsList!![0].deliveryFee!! + mBuyGoodsList!![0].goods!!.deliveryAddFee!!
//                        if(mGoodsBuyShipTypeAdapter != null){
//                            mGoodsBuyShipTypeAdapter!!.notifyDataSetChanged()
//                        }
//                        setTotalPrice()
//                    }
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
//                    hideProgress()
//                }
//            }).build().call()
//        }
//    }
//
//    private fun cardListCall() {
//        showProgress("")
//        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
//            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
//                hideProgress()
//                if (response?.datas != null) {
//                    if (response.datas.isNotEmpty()) {
//                        mCardSelectAdapter!!.mSelectData = response.datas[0]
//                    }
//                    mCardSelectAdapter!!.setDataList(response.datas)
//                    mCardSelectAdapter!!.add(Card())
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Card>>?, t: Throwable?, response: NewResultResponse<Card>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//        check_goods_buy_ship_type_terms1.isChecked = isChecked
//        check_goods_buy_ship_type_terms2.isChecked = isChecked
//    }
//
//    var totalPrice = 0
//    private fun setTotalPrice() {
//        totalPrice = 0
//
//        for (i in 0 until mBuyGoodsList!!.size) {
//            var price = 0
//            if(mBuyGoodsList!![i].buyGoodsOptionSelectList != null && mBuyGoodsList!![i].buyGoodsOptionSelectList!!.isNotEmpty()){
//                for(buyGoodsOption in mBuyGoodsList!![i].buyGoodsOptionSelectList!!){
//                    price = (mBuyGoodsList!![i].goodsPriceData!!.price!!.toInt() + buyGoodsOption.price!!)*buyGoodsOption.amount!!
//                    totalPrice += price
//                }
//            }else{
//                price = mBuyGoodsList!![i].goodsPriceData!!.price!!.toInt() * mBuyGoodsList!![i].count!!
//                totalPrice += price
//            }
//
//            val deliveryMinPrice = mBuyGoodsList!![i].goods!!.deliveryMinPrice
//
//            LogUtil.e(LOG_TAG, "deliveryMinPrice : {}", deliveryMinPrice)
//
//            if(mBuyGoodsList!![i].deliveryFee != null && mBuyGoodsList!![i].deliveryFee!! > 0){
//                if(deliveryMinPrice == null || deliveryMinPrice <= 0){
//                    totalPrice += mBuyGoodsList!![i].deliveryFee!!
//                }else{
//                    if(deliveryMinPrice > 0 && price < deliveryMinPrice){
//                        totalPrice += mBuyGoodsList!![i].deliveryFee!!
//                    }
//                }
//            }
//        }
//
//        if(totalPrice >= 50000){
//            layout_goods_buy_ship_type_installment.visibility = View.VISIBLE
////            text_goods_buy_ship_type_free_installment.visibility = View.VISIBLE
//        }else{
//            layout_goods_buy_ship_type_installment.visibility = View.GONE
////            text_goods_buy_ship_type_free_installment.visibility = View.GONE
//        }
//
//        text_goods_buy_ship_type_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_purchase_price2, FormatUtil.getMoneyType(totalPrice.toString())))
//    }
//
//
//    private fun getOrderId() {
//        showProgress("")
//        ApiBuilder.create().buyOrderId.setCallback(object : PplusCallback<NewResultResponse<String>> {
//            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//
//                hideProgress()
//                if (response != null) {
////                    val regex = Regex("^[0-9]{22}\\z")
//                    val orderId = response.data
//                    buy(orderId)
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
//        mBuy.orderId = orderId
//        mBuy.installment = mInstallment
//        showProgress("")
//        ApiBuilder.create().postBuyShip(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//                if (response != null && response.data != null) {
//
//                    if (response.data.seqNo != null) {
//                        ftLinkPay()
//                        return
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//                showAlert(R.string.msg_cancel_pg)
//            }
//        }).build().call()
//    }
//
//    private fun ftLinkPay() {
//        val params = FTLink()
//        params.shopcode = mBuyGoodsList!![0].goodsPriceData!!.page!!.shopCode
////        params.loginId = LoginInfoManager.getInstance().user.loginId
//        params.order_req_amt = totalPrice.toString()
//        params.order_hp = mBuy.buyerTel
//        params.order_name = mBuy.buyerName
//        params.order_goodsname = mBuy.title
//        params.req_installment = mInstallment
//        params.comp_memno = LoginInfoManager.getInstance().user.no.toString()
//        params.autokey = mCardSelectAdapter!!.mSelectData!!.autoKey
//        params.req_cardcode = mCardSelectAdapter!!.mSelectData!!.cardCode
//        params.comp_orderno = mBuy.orderId
//        showProgress("")
//        ApiBuilder.create().postBuyFTLinkPay(params).setCallback(object : PplusCallback<NewResultResponse<FTLink>> {
//            override fun onResponse(call: Call<NewResultResponse<FTLink>>?, response: NewResultResponse<FTLink>?) {
//                hideProgress()
//                orderComplete()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<FTLink>>?, t: Throwable?, response: NewResultResponse<FTLink>?) {
//                hideProgress()
//                if (response != null && response.data != null && StringUtils.isNotEmpty(response.data.errMessage)) {
//                    showAlert(response.data.errMessage)
//                }
//            }
//        }).build().call()
//    }
//
//    private fun orderComplete() {
////        val intent = Intent(this, ShipTypePayCompleteActivity::class.java)
////        intent.putExtra(Const.PAGE_SEQ_NO, mBuyGoodsList!![0].goodsPriceData!!.pageSeqNo)
////        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////        startActivityForResult(intent, Const.REQ_ORDER_FINISH)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_BUY ->{
//                if(resultCode == Activity.RESULT_OK){
//                    val intent = Intent(this, PayPasswordCheckActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
//                }
//            }
//            Const.REQ_CHECK_PASSWORD ->{
//                if(resultCode == Activity.RESULT_OK){
//                    mBuy.pg = "DAOU"
//                    getOrderId()
//                }
//            }
//            Const.REQ_SEARCH_ADDRESS->{
//                if(resultCode == Activity.RESULT_OK && data != null){
//                    mSelectShippingSite = data.getParcelableExtra(Const.SHIPPING_SITE)
//                    if(mSelectShippingSite != null){
//                        text_goods_buy_ship_type_not_exist_site.visibility = View.GONE
//                        layout_goods_buy_ship_type_exist_site.visibility = View.VISIBLE
//                        text_goods_buy_ship_type_site_name.text = mSelectShippingSite!!.siteName
//                        text_goods_buy_ship_type_site_address.text = mSelectShippingSite!!.address + " " + mSelectShippingSite!!.addressDetail
//
//                        checkIslandsRegion()
//                    }else{
//                        text_goods_buy_ship_type_not_exist_site.visibility = View.VISIBLE
//                        layout_goods_buy_ship_type_exist_site.visibility = View.GONE
//                    }
//                }
//            }
//            Const.REQ_ORDER_FINISH -> {
//                setResult(resultCode)
//                finish()
//            }
//            Const.REQ_REG -> {
//                if (resultCode == Activity.RESULT_OK) {
//
//                    if(LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!){
//                        val intent = Intent(this, PayPasswordSetActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_CHANGE_PASSWORD)
//                    }
//                    cardListCall()
//                }
//            }
//            Const.REQ_CARD_CONFIG->{
//                cardListCall()
//            }
//            Const.REQ_CHANGE_PASSWORD->{
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                    override fun reload() {
//                    }
//                })
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val verifiedData = data.getParcelableExtra<User>(Const.DATA)
//                        if (LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE+"##", "") != verifiedData.mobile) {
//                            showAlert(R.string.msg_incorrect_joined_mobile_number)
//                        } else {
//                            val user = LoginInfoManager.getInstance().user
//                            user.name = verifiedData.name
//                            user.birthday = verifiedData.birthday
//                            user.mobile = verifiedData.mobile
//                            user.gender = verifiedData.gender
//                            user.verification = verifiedData.verification
//                            showProgress("")
//                            ApiBuilder.create().updateUser(user).setCallback(object : PplusCallback<NewResultResponse<User>> {
//
//                                override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
//                                    hideProgress()
//                                    showProgress("")
//                                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                                        override fun reload() {
//                                            hideProgress()
//                                            if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
//                                                edit_goods_buy_ship_type_name.setText(LoginInfoManager.getInstance().user.name)
//                                            }
//                                            showAlert(R.string.msg_verified)
//                                        }
//                                    })
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
//                                    hideProgress()
//                                }
//                            }).build().call()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_buy), ToolbarOption.ToolbarMenu.LEFT)
//
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
