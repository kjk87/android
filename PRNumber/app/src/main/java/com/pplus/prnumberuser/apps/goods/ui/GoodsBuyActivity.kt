//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Typeface
//import android.os.Bundle
//import android.view.View
//import android.widget.CompoundButton
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.gson.JsonParser
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.card.data.CardSelectAdapter
//import com.pplus.prnumberuser.apps.card.ui.CardRegActivity
//import com.pplus.prnumberuser.apps.card.ui.PayPasswordCheckActivity
//import com.pplus.prnumberuser.apps.card.ui.PayPasswordSetActivity
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.SelectedGoodsManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsBuyAdapter
//import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.*
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_goods_order.*
//import kr.co.bootpay.Bootpay
//import kr.co.bootpay.BootpayAnalytics
//import kr.co.bootpay.enums.Method
//import kr.co.bootpay.enums.PG
//import kr.co.bootpay.enums.UX
//import kr.co.bootpay.listener.*
//import kr.co.bootpay.model.BootExtra
//import kr.co.bootpay.model.BootUser
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.set
//
//class GoodsBuyActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
//    override fun getPID(): String {
//        return "Main_surrounding sale_product detail_buy"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_order
//    }
//
//    var mPayMethod = "card"
//    var mInstallment = "00"
//
//    var mCartList: ArrayList<Cart>? = null
//
//    //    var mKey = ""
//    var mBuyGoodsList: ArrayList<BuyGoods>? = null
//    var mBuy = Buy()
//    private var mOrderType = -1
//    private var mAdapter: CardSelectAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mOrderType = intent.getIntExtra(Const.TYPE, -1)
//        mBuyGoodsList = intent.getParcelableArrayListExtra(Const.BUY_GOODS)
//
//        layout_goods_order_buy_goods.visibility = View.GONE
//        layout_goods_order_buy_goods_list.visibility = View.VISIBLE
//
//        layout_goods_order_time.visibility = View.GONE
//        text_goods_order_immediately_desc.visibility = View.GONE
//        text_goods_order_reservation_time.visibility = View.GONE
//        text_goods_order_memo.visibility = View.GONE
//        layout_goods_order_page_address.visibility = View.GONE
//        layout_goods_order_address.visibility = View.GONE
//
//
//        text_goods_order_view_terms1.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS1)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_goods_order_view_terms2.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS2)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_goods_order_card.setOnClickListener {
//            text_goods_order_card.isSelected = true
//            text_goods_order_bank.isSelected = false
//
//            text_goods_order_card.typeface = Typeface.DEFAULT_BOLD
//            text_goods_order_bank.typeface = Typeface.DEFAULT
//
//            mPayMethod = "card"
//        }
//
//        text_goods_order_bank.setOnClickListener {
//            text_goods_order_bank.isSelected = true
//            text_goods_order_card.isSelected = false
//
//            text_goods_order_bank.typeface = Typeface.DEFAULT_BOLD
//            text_goods_order_card.typeface = Typeface.DEFAULT
//
//            mPayMethod = "bank"
//        }
//
//        edit_goods_order_name.setSingleLine()
//        edit_goods_order_phone.setSingleLine()
//
//        if (LoginInfoManager.getInstance().user.verification!!.media == "external" && StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.name)) {
//            edit_goods_order_name.setText(LoginInfoManager.getInstance().user.name)
//        }
//
//        edit_goods_order_phone.setText(LoginInfoManager.getInstance().user.mobile)
//
//        val contents = resources.getStringArray(R.array.report_installment_period)
//        text_goods_order_installment.setOnClickListener {
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
//                            text_goods_order_installment.text = contents[event_alert.value - 1]
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
//        text_goods_order_installment.text = contents[0]
//
//        text_goods_order_free_installment.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(this, getString(R.string.msg_goods_card_info_url) + "?timestamp=" + System.currentTimeMillis())
//        }
//
//        text_goods_order_pay.setOnClickListener {
//
//            if (!check_goods_order_terms1.isChecked || !check_goods_order_terms2.isChecked) {
//
//                ToastUtil.show(this, getString(R.string.msg_agree_terms))
//                return@setOnClickListener
//            }
//
//            mBuy.payMethod = "card"
//
//            val name = edit_goods_order_name.text.toString().trim()
//            if (StringUtils.isEmpty(name)) {
//                showAlert(R.string.hint_input_name)
//                return@setOnClickListener
//            }
//
//            val phone = edit_goods_order_phone.text.toString().trim()
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
//            if (mOrderType == EnumData.OrderType.delivery.type) {
//                val address = text_goods_order_delivery_address.text.toString().trim()
//                val addressDetail = edit_goods_order_delivery_detail_address.text.toString().trim()
//                if (StringUtils.isEmpty(address)) {
//                    showAlert(R.string.msg_input_address)
//                    return@setOnClickListener
//                }
//
//                if (StringUtils.isEmpty(addressDetail)) {
//                    showAlert(R.string.msg_input_detail_address)
//                    return@setOnClickListener
//                }
//
//                mBuy.clientAddress = "${address} $addressDetail"
//            }
//
//            if (StringUtils.isNotEmpty(bookDatetime)) {
//                val cal = Calendar.getInstance()
//
//                val year = cal.get(Calendar.YEAR)
//                var month = cal.get(Calendar.MONTH)
//                val day = cal.get(Calendar.DAY_OF_MONTH)
//
//                var hour = bookDatetime.split(":")[0]
//                val min = bookDatetime.split(":")[1]
//                if (hour.toInt() >= 24) {
//                    hour = DateFormatUtils.formatTime(hour.toInt() - 24)
//                    month += 1
//                    bookDatetime = "${hour}:${min}"
//                }
//
//                mBuy.bookDatetime = "${year}-${DateFormatUtils.formatTime(month + 1)}-${DateFormatUtils.formatTime(day)} ${bookDatetime}:00"
//            }
//
//            val memo = text_goods_order_memo.text.toString().trim()
//            if (StringUtils.isNotEmpty(memo)) {
//                mBuy.memo = memo
//            }
//
//            mBuy.buyerName = name
//            mBuy.buyerTel = phone
//
//            val buyGoodsList = ArrayList<BuyGoods>()
//            for (item in mBuyGoodsList!!) {
//
//                val buyGoods = BuyGoods()
//                buyGoods.goodsSeqNo = item.goods!!.seqNo
//                buyGoods.count = item.count
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
////            if((mBuyGoodsList!![0].goods!!.isHotdeal != null && mBuyGoodsList!![0].goods!!.isHotdeal!!) || (mBuyGoodsList!![0].goods!!.isPlus != null && mBuyGoodsList!![0].goods!!.isPlus!!)){
////                val intent = Intent(this, AlertGoodsBuyActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                startActivityForResult(intent, Const.REQ_BUY)
////            }else{
////                if (mBuyGoodsList!![0].goods!!.page!!.commissionPoint!!.woodongyi!!) {
////                    mBuy.pg = "LPNG"
////                    lpngPay()
////                } else {
////                    mBuy.pg = "DANAL"
////                    getOrderId()
////                }
////            }
//
////            if (mBuyGoodsList!![0].goods!!.page!!.commissionPoint!!.woodongyi!!) {
////                mBuy.pg = "LPNG"
////                lpngPay()
////            } else {
////                mBuy.pg = "DAOU"
////                getOrderId()
////            }
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
//                                val intent = Intent(this@GoodsBuyActivity, VerificationMeActivity::class.java)
//                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                startActivityForResult(intent, Const.REQ_VERIFICATION)
//                            }
//                        }
//                    }
//                }).builder().show(this)
//            } else {
//                if (mAdapter!!.mSelectData == null) {
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
//                                    val intent = Intent(this@GoodsBuyActivity, CardRegActivity::class.java)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivityForResult(intent, Const.REQ_REG)
//                                }
//                            }
//
//                        }
//                    }).builder().show(this)
//                } else {
//                    if ((mBuyGoodsList!![0].goods!!.isHotdeal != null && mBuyGoodsList!![0].goods!!.isHotdeal!!) || (mBuyGoodsList!![0].goods!!.isPlus != null && mBuyGoodsList!![0].goods!!.isPlus!!)) {
//                        val intent = Intent(this, AlertGoodsBuyActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_BUY)
//                    } else {
//
//                        val intent = Intent(this, PayPasswordCheckActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
//                    }
//                }
//            }
//        }
//
//        text_goods_order_card.typeface = Typeface.DEFAULT_BOLD
//        text_goods_order_bank.typeface = Typeface.DEFAULT
//        text_goods_order_card.isSelected = true
//        text_goods_order_bank.isSelected = false
//
//        mPayMethod = "card"
//        mBuy.memberSeqNo = LoginInfoManager.getInstance().user.no
//
//        if (mOrderType != -1) {
//            mBuy.orderType = mOrderType
//        }
//
//
//        if (!mBuyGoodsList!![0].goods!!.page!!.woodongyi!!) {
//            // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
//            if (Const.API_URL.startsWith("https://api")) {
//                BootpayAnalytics.init(this, getString(R.string.boot_pay_id))
//            } else if (Const.API_URL.startsWith("https://stage")) {
//                BootpayAnalytics.init(this, getString(R.string.boot_pay_id_stage))
//            } else {
//                BootpayAnalytics.init(this, getString(R.string.boot_pay_id_dev))
//            }
//        }
//
//        val adapter = GoodsBuyAdapter()
//        recycler_goods_order_buy_goods.adapter = adapter
//        adapter.setDataList(mBuyGoodsList!!)
//        recycler_goods_order_buy_goods.layoutManager = LinearLayoutManager(this)
//
//        check_goods_order_totalAgree.isChecked = true
//        check_goods_order_terms1.isChecked = true
//        check_goods_order_terms2.isChecked = true
//
//        check_goods_order_totalAgree.setOnCheckedChangeListener(this)
//        check_goods_order_terms1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_goods_order_terms2.isChecked) {
//                    isAll = true
//                }
//                check_goods_order_totalAgree.setOnCheckedChangeListener(null)
//                check_goods_order_totalAgree.isChecked = isAll
//                check_goods_order_totalAgree.setOnCheckedChangeListener(this@GoodsBuyActivity)
//            }
//        })
//
//        check_goods_order_terms2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_goods_order_terms1.isChecked) {
//                    isAll = true
//                }
//                check_goods_order_totalAgree.setOnCheckedChangeListener(null)
//                check_goods_order_totalAgree.isChecked = isAll
//                check_goods_order_totalAgree.setOnCheckedChangeListener(this@GoodsBuyActivity)
//            }
//        })
//
//        setTotalPrice()
//
//        mAdapter = CardSelectAdapter()
//        recycler_goods_buy_card.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recycler_goods_buy_card.adapter = mAdapter
//        cardListCall()
//    }
//
//    private fun cardListCall() {
//        showProgress("")
//        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
//            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
//                hideProgress()
//                if (response?.datas != null) {
//                    if (response.datas.isNotEmpty()) {
//                        mAdapter!!.mSelectData = response.datas[0]
//                    }
//                    mAdapter!!.setDataList(response.datas)
//                    mAdapter!!.add(Card())
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
//        check_goods_order_terms1.isChecked = isChecked
//        check_goods_order_terms2.isChecked = isChecked
//    }
//
//    var totalPrice = 0
//    private fun setTotalPrice() {
//        totalPrice = 0
//
//        for (i in 0 until mBuyGoodsList!!.size) {
//            val price = mBuyGoodsList!![i].goods!!.price!!.toInt() * mBuyGoodsList!![i].count!!
//            totalPrice += price
//        }
//
//        if(totalPrice >= 50000){
//            layout_goods_order_installment.visibility = View.VISIBLE
////            text_goods_order_free_installment.visibility = View.VISIBLE
//        }else{
//            layout_goods_order_installment.visibility = View.GONE
////            text_goods_order_free_installment.visibility = View.GONE
//        }
//
//        text_goods_order_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_purchase_price2, FormatUtil.getMoneyType(totalPrice.toString())))
//    }
//
////    var mPageManagement: PageManagement? = null
////    private fun getPageManagement() {
////        val params = HashMap<String, String>()
////        params["no"] = mBuyGoodsList!![0].goods!!.page!!.seqNo.toString()
////        showProgress("")
////        ApiBuilder.create().getPageManagement(params).setCallback(object : PplusCallback<NewResultResponse<PageManagement?>> {
////
////            override fun onResponse(call: Call<NewResultResponse<PageManagement?>>?, response: NewResultResponse<PageManagement?>?) {
////                hideProgress()
////                if (response!!.data != null) {
////                    mPageManagement = response.data!!
////
////                    setTotalPrice()
////                }
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<PageManagement?>>?, t: Throwable?, response: NewResultResponse<PageManagement?>?) {
////                hideProgress()
////            }
////        }).build().call()
////    }
//
//    private fun lpngPay() {
//
//        val intent = Intent(this, GoodsCardActivity::class.java)
//        intent.putExtra(Const.PRICE, totalPrice)
//        intent.putExtra(Const.INSTALLMENT, mInstallment)
//        intent.putExtra(Const.BUY, mBuy)
//        intent.putExtra(Const.GOODS, mBuyGoodsList!![0].goods)
//        startActivity(intent)
//    }
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
//        ApiBuilder.create().postBuyShop(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//                if (response != null && response.data != null) {
//
//                    if (response.data.seqNo != null) {
//                        ftLinkPay()
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
//        params.shopcode = mBuyGoodsList!![0].goods!!.page!!.shopCode
////        params.loginId = LoginInfoManager.getInstance().user.loginId
//        params.order_req_amt = totalPrice.toString()
//        params.order_hp = mBuy.buyerTel
//        params.order_name = mBuy.buyerName
//        params.order_goodsname = mBuy.title
//        params.req_installment = mInstallment
//        params.comp_memno = LoginInfoManager.getInstance().user.no.toString()
//        params.autokey = mAdapter!!.mSelectData!!.autoKey
//        params.req_cardcode = mAdapter!!.mSelectData!!.cardCode
//        params.comp_orderno = mBuy.orderId
//        showProgress("")
//        ApiBuilder.create().postBuyFTLinkPay(params).setCallback(object : PplusCallback<NewResultResponse<FTLink>> {
//            override fun onResponse(call: Call<NewResultResponse<FTLink>>?, response: NewResultResponse<FTLink>?) {
//                hideProgress()
//                orderComplete(mBuy.orderId!!)
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
//    private fun openPg(orderId: String) {
//        val builder = Bootpay.init(this)
////        if (StringUtils.isEmpty(mBuyGoodsList!![0].goods!!.page!!.distributorAgentCode) || mBuyGoodsList!![0].goods!!.page!!.distributorAgentCode != Const.GAL_COM_CODE) {
////            if (Const.API_URL.startsWith("https://api")) {
////                builder.setApplicationId(getString(R.string.boot_pay_id)) // 해당 프로젝트(안드로이드)의 application id 값
////            } else if (Const.API_URL.startsWith("http://52.79.84.26")) {
////                builder.setApplicationId(getString(R.string.boot_pay_id)) // 해당 프로젝트(안드로이드)의 application id 값
////            } else if (Const.API_URL.startsWith("https://stage")) {
////                builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
////            } else {
////                builder.setApplicationId(getString(R.string.boot_pay_id_dev)) // 해당 프로젝트(안드로이드)의 application id 값
////            }
////            builder.setPG(PG.INICIS) // 결제할 PG 사
////        }else{
////            builder.setApplicationId(getString(R.string.boot_pay_kicc_id)) // 해당 프로젝트(안드로이드)의 application id 값
////            builder.setPG(PG.EASYPAY) // 결제할 PG 사
////        }
//
//        if (Const.API_URL.startsWith("https://api")) {
//            builder.setApplicationId(getString(R.string.boot_pay_id)) // 해당 프로젝트(안드로이드)의 application id 값
//        } else if (Const.API_URL.startsWith("https://stage")) {
//            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
//        } else {
//            builder.setApplicationId(getString(R.string.boot_pay_id_dev)) // 해당 프로젝트(안드로이드)의 application id 값
//        }
//        builder.setPG(PG.DANAL) // 결제할 PG 사
//
////        builder.setApplicationId(getString(R.string.boot_pay_kicc_id)) // 해당 프로젝트(안드로이드)의 application id 값
////        builder.setPG(PG.EASYPAY) // 결제할 PG 사
//
//        val bootUser = BootUser()
//        bootUser.username = mBuy.buyerName
//        bootUser.phone = mBuy.buyerTel
//        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
//
//        builder.setBootUser(bootUser)
//                .setMethod(Method.CARD) // 결제수단
//                .setContext(this)
//                .setName(mBuy.title) // 결제할 상품명
//                .setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
//                .setBootExtra(bootExtra)
//                .setUX(UX.PG_DIALOG)
//
//
//        for (buyGoods in mBuyGoodsList!!) {
//            builder.addItem(buyGoods.goods!!.name, buyGoods.count!!, buyGoods.goods!!.seqNo.toString(), buyGoods.goods!!.price!!.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
//
//        }
//        builder.setPrice(totalPrice.toInt()) // 결제할 금액
//
//        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
//            override fun onConfirm(message: String?) {
//                mBuy.orderId = orderId
//
//                ApiBuilder.create().postBuyShop(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                    override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                        if (response != null && response.data != null) {
//
//                            if (response.data.seqNo != null) {
//                                Bootpay.confirm(message)
//                                return
//                            }
//                        }
//                        showAlert(R.string.msg_cancel_pg)
//                        Bootpay.removePaymentWindow()
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                        showAlert(R.string.msg_cancel_pg)
//                        Bootpay.removePaymentWindow()
//                    }
//                }).build().call()
//
//                LogUtil.e(LOG_TAG, "confirm : {}", message)
//            }
//        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
//            override fun onDone(message: String?) {
//                LogUtil.e(LOG_TAG, "done : {}", message)
//                val dataList = SelectedGoodsManager.getInstance(mBuyGoodsList!![0].goods!!.page!!.seqNo!!).load().buyGoodsList
//
//                for (i in 0 until mBuyGoodsList!!.size) {
//                    if (dataList != null && dataList.isNotEmpty()) {
//                        for (j in 0 until dataList.size) {
//                            if (dataList[j].goods!!.seqNo == mBuyGoodsList!![i].goods!!.seqNo) {
//                                dataList.removeAt(j)
//                                break
//                            }
//                        }
//                        SelectedGoodsManager.getInstance(mBuyGoodsList!![0].goods!!.page!!.seqNo!!).load().buyGoodsList = dataList
//                        SelectedGoodsManager.getInstance(mBuyGoodsList!![0].goods!!.page!!.seqNo!!).save()
//                    }
//
//                }
//                val parser = JsonParser()
//                val receiptId = parser.parse(message).asJsonObject.get("receipt_id").asString
//
//                showProgress(getString(R.string.msg_pay_ing))
//
//                val params = HashMap<String, String>()
//                params["receiptId"] = receiptId
//                ApiBuilder.create().checkBootPay(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                    override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                        if (response != null && response.data != null) {
//                            orderComplete(orderId)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                    }
//                }).build().call()
//
////                getBuy(orderId)
//
//            }
//        }).onReady(object : ReadyListener {
//            override fun onReady(message: String?) {
//                LogUtil.e(LOG_TAG, "ready : {}", message)
//            }
//        }).onCancel(object : CancelListener {
//            override fun onCancel(message: String?) {
//                LogUtil.e(LOG_TAG, "cancel : {}", message)
//                showAlert(R.string.msg_cancel_pg)
////                deleteBuy(buy.seqNo.toString())
//            }
//        }).onError(object : ErrorListener {
//            override fun onError(message: String?) {
//                LogUtil.e(LOG_TAG, "error : {}", message)
//                showAlert(R.string.msg_error_pg)
////                deleteBuy(buy.seqNo.toString())
//            }
//        }).onClose(object : CloseListener {
//            override fun onClose(message: String?) {
//                LogUtil.e(LOG_TAG, "close : {}", message)
//            }
//        }).request()
//    }
//
//    var retryCount = 1
//
////    private fun getBuy(orderId: String) {
////        if (retryCount > 40) {
////            hideProgress()
////            finish()
////            return
////        }
////        Thread.sleep(1500)
////        val params = HashMap<String, String>()
////        params["orderId"] = orderId
////        ApiBuilder.create().getOneBuy(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
////            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
////
////                if (response != null && response.data != null && response.data.process != null) {
////                    if (response.data.process!! > 0) {
////                        hideProgress()
////                        orderComplete(orderId)
////                    } else if (response.data.process!! < 0) {
////                        hideProgress()
////                        showAlert(R.string.msg_error_pg)
////                    } else {
////                        retryCount++
////                        getBuy(orderId)
////                    }
////                } else {
////                    hideProgress()
////                }
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
////                hideProgress()
////            }
////        }).build().call()
////    }
//
//    private fun orderComplete(orderId: String) {
//        val intent = Intent(this@GoodsBuyActivity, PayCompleteActivity::class.java)
//        intent.putExtra(Const.PAGE_SEQ_NO, mBuyGoodsList!![0].goods!!.page!!.seqNo)
//        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivityForResult(intent, Const.REQ_ORDER_FINISH)
//    }
//
//    fun deleteBuy(seqNo: String) {
//        val params = HashMap<String, String>()
//        params["seqNo"] = seqNo
//        ApiBuilder.create().deleteBuy(params).build().call()
//    }
//
//    var bookDatetime = ""
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
//            Const.REQ_SEARCH -> if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    val juso = data.getParcelableExtra<SearchAddressJuso>(Const.ADDRESS)
//                    text_goods_order_delivery_address.text = juso.roadAddr
//                    val zipCode = juso.zipNo
////                    callLatLon(juso.roadAddrPart1)
//                }
//            }
//            Const.REQ_TIME -> if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    bookDatetime = data.getStringExtra(Const.TIME)
//                    var hour = bookDatetime.split(":")[0]
//                    val min = bookDatetime.split(":")[1]
//                    if (hour.toInt() >= 24) {
//                        hour = DateFormatUtils.formatTime(hour.toInt() - 24)
//                        text_goods_order_reservation_time.text = getString(R.string.format_time_nextday, hour, min)
//                    } else {
//                        text_goods_order_reservation_time.text = getString(R.string.format_time, hour, min)
//                    }
//
//
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
//                        if (LoginInfoManager.getInstance().user.mobile != verifiedData.mobile) {
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
//                                                edit_goods_order_name.setText(LoginInfoManager.getInstance().user.name)
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
