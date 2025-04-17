//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Typeface
//import android.os.Bundle
//import android.view.View
//import com.google.gson.JsonObject
//import com.google.gson.JsonParser
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.menu.ui.ReservationTimePickerActivity
//import com.pplus.prnumberuser.apps.shippingsite.ui.SearchAddressActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.ApiController
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
//import retrofit2.Callback
//import retrofit2.Response
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.set
//
//class HotdealBuyActivity : BaseActivity(), ImplToolbar {
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
//    //    var mKey = ""
//    var mGoods: Goods? = null
//    var mCount = 0
//    var mBuy = Buy()
//    private var mOrderType = -1
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
////        mKey = intent.getStringExtra(Const.KEY)
//        mOrderType = intent.getIntExtra(Const.TYPE, -1)
//        mGoods = intent.getParcelableExtra(Const.GOODS)
//        mCount = intent.getIntExtra(Const.COUNT, 0)
//
//        layout_goods_order_buy_goods.visibility = View.VISIBLE
//        layout_goods_order_buy_goods_list.visibility = View.GONE
//
//        when (mGoods!!.type) {
//            "0" -> {
//                text_goods_order_reservation_time.visibility = View.GONE
//                text_goods_order_immediately_desc.visibility = View.GONE
//                text_goods_order_memo.visibility = View.VISIBLE
//                text_goods_order_page_address.text = "${mGoods!!.page!!.roadAddress} ${mGoods!!.page!!.roadDetailAddress}"
//                when (mOrderType) {
//                    EnumData.OrderType.store.type -> {
//                        layout_goods_order_time.visibility = View.GONE
//                        layout_goods_order_page_address.visibility = View.VISIBLE
//                        layout_goods_order_address.visibility = View.GONE
//
//                        layout_goods_order_immediately.isSelected = true
//                        layout_goods_order_reservation.isSelected = false
//                        text_goods_order_immediately_desc.visibility = View.VISIBLE
//                        text_goods_order_reservation_time.visibility = View.GONE
//                    }
//                    EnumData.OrderType.packing.type -> {
//                        layout_goods_order_time.visibility = View.GONE
//                        layout_goods_order_page_address.visibility = View.VISIBLE
//                        layout_goods_order_address.visibility = View.GONE
//
//                        layout_goods_order_immediately.isSelected = true
//                        layout_goods_order_reservation.isSelected = false
//                        text_goods_order_immediately_desc.visibility = View.VISIBLE
//                        text_goods_order_reservation_time.visibility = View.GONE
//
//                    }
//                    EnumData.OrderType.delivery.type -> {
//                        layout_goods_order_time.visibility = View.GONE
//                        layout_goods_order_page_address.visibility = View.GONE
//                        layout_goods_order_address.visibility = View.VISIBLE
//                    }
//                }
//
//            }
//            "1" -> {
//                layout_goods_order_time.visibility = View.GONE
//                text_goods_order_immediately_desc.visibility = View.GONE
//                text_goods_order_reservation_time.visibility = View.GONE
//                text_goods_order_memo.visibility = View.GONE
//                layout_goods_order_page_address.visibility = View.GONE
//                layout_goods_order_address.visibility = View.GONE
//
//            }
//        }
//
//        layout_goods_order_immediately.setOnClickListener {
//            layout_goods_order_immediately.isSelected = true
//            layout_goods_order_reservation.isSelected = false
//            text_goods_order_immediately_desc.visibility = View.VISIBLE
//            text_goods_order_reservation_time.visibility = View.GONE
//        }
//
//        layout_goods_order_reservation.setOnClickListener {
//            layout_goods_order_immediately.isSelected = false
//            layout_goods_order_reservation.isSelected = true
//            text_goods_order_immediately_desc.visibility = View.GONE
//            text_goods_order_reservation_time.visibility = View.VISIBLE
//        }
//
//        text_goods_order_reservation_time.setOnClickListener {
//            val intent = Intent(this, ReservationTimePickerActivity::class.java)
//            intent.putExtra(Const.START, mGoods!!.startTime)
//            intent.putExtra(Const.END, mGoods!!.endTime)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_TIME)
//        }
//
//        text_goods_order_delivery_address.setOnClickListener {
//            val intent = Intent(this, SearchAddressActivity::class.java)
//            val page = Page()
//            page.no = mGoods!!.page!!.seqNo
//            intent.putExtra(Const.PAGE, page)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH)
//        }
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
//            PplusCommonUtil.openChromeWebView(this, getString(R.string.msg_goods_card_info_url)+"?timestamp=" + System.currentTimeMillis())
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
//            if (mGoods!!.page!!.woodongyi!!) {
//                mBuy.pg = "LPNG"
//                lpngPay()
//            } else {
//                mBuy.pg = "inicis"
//                getOrderId()
//            }
//
//
//
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
//        val cart = Cart()
//        cart.memberSeqNo = LoginInfoManager.getInstance().user.no
//        cart.goodsSeqNo = mGoods!!.seqNo
//        cart.goods = mGoods
//        cart.count = mCount
//        mBuy.buyGoods = cart
//        mBuy.title = mGoods!!.name
//        if (mOrderType != -1) {
//            mBuy.orderType = mOrderType
//        }
//
//
//        if (!mGoods!!.page!!.woodongyi!!) {
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
//
//
//        getPageManagement()
//    }
//
//    var mPageManagement: PageManagement? = null
//    private fun getPageManagement() {
//        val params = HashMap<String, String>()
//        params["no"] = mGoods!!.page!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getPageManagement(params).setCallback(object : PplusCallback<NewResultResponse<PageManagement?>> {
//
//            override fun onResponse(call: Call<NewResultResponse<PageManagement?>>?, response: NewResultResponse<PageManagement?>?) {
//                hideProgress()
//                if (response!!.data != null) {
//                    mPageManagement = response.data!!
//
//                    text_goods_order_title.text = mGoods!!.name
//                    text_goods_order_count.text = getString(R.string.format_order_count, FormatUtil.getMoneyType(mCount.toString()))
//
//                    var deliveryFee = 0
//
//                    if (mOrderType == EnumData.OrderType.delivery.type) {
//                        text_goods_order_delivery_fee.visibility = View.VISIBLE
//                        if (mPageManagement!!.deliveryFee != null && mPageManagement!!.deliveryFee!! > 0) {
//                            deliveryFee = mPageManagement!!.deliveryFee!!.toInt()
//                            text_goods_order_delivery_fee.text = PplusCommonUtil.fromHtml(getString(R.string.html_delivery_fee, FormatUtil.getMoneyType(mPageManagement!!.deliveryFee.toString())))
//                        } else {
//                            text_goods_order_delivery_fee.text = getString(R.string.word_free_delivery)
//                        }
//                    } else {
//                        text_goods_order_delivery_fee.visibility = View.GONE
//                    }
//
//                    val price = mGoods!!.price!! * mCount + deliveryFee
//
//                    if (mGoods!!.page!!.woodongyi!!) {
//                        if (price >= 50000) {
//                            layout_goods_order_installment.visibility = View.VISIBLE
//                        } else {
//                            layout_goods_order_installment.visibility = View.GONE
//                        }
//                    }else{
//                        layout_goods_order_installment.visibility = View.GONE
//                    }
//
//                    if (mGoods!!.isPlus!!) {
//                        if (StringUtils.isNotEmpty(mGoods!!.expireDatetime)) {
//                            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mGoods!!.expireDatetime)
//                            val output = SimpleDateFormat(getString(R.string.word_format_date))
//                            text_goods_order_expire_date.text = getString(R.string.format_expire_date2, output.format(d))
//                        }
//                    } else if (mGoods!!.isHotdeal!! || mGoods!!.type == "1") {
//                        text_goods_order_expire_date.text = getString(R.string.format_expire_date2, getString(R.string.word_buy_after_30))
//                    }
//
//                    if (StringUtils.isNotEmpty(mGoods!!.startTime) && StringUtils.isNotEmpty(mGoods!!.endTime)) {
//                        text_goods_order_use_time.visibility = View.VISIBLE
//                        text_goods_order_use_time.text = getString(R.string.format_use_time, mGoods!!.startTime + " ~ " + mGoods!!.endTime)
//                    } else {
//                        text_goods_order_use_time.visibility = View.GONE
//                    }
//
//                    text_goods_order_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_pay_price, FormatUtil.getMoneyType(price.toString())))
//
//                    if (mGoods!!.rewardLuckybol != null && mGoods!!.rewardLuckybol!! > 0) {
//                        text_goods_order_reward.visibility = View.VISIBLE
//                        text_goods_order_reward.text = PplusCommonUtil.fromHtml(getString(R.string.html_reward_point3, FormatUtil.getMoneyType(mGoods!!.rewardLuckybol!!.toString())))
//
//                    } else {
//                        text_goods_order_reward.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<PageManagement?>>?, t: Throwable?, response: NewResultResponse<PageManagement?>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun lpngPay() {
//
//        var price = mCount * mGoods!!.price!!.toInt()
//        if (mOrderType == EnumData.OrderType.delivery.type && mPageManagement!!.deliveryFee != null && mPageManagement!!.deliveryFee!! > 0) {
//            price += mPageManagement!!.deliveryFee!!.toInt()
//        }
//
//        val intent = Intent(this, GoodsCardActivity::class.java)
//        intent.putExtra(Const.PRICE, price)
//        intent.putExtra(Const.INSTALLMENT, mInstallment)
//        intent.putExtra(Const.PAGE_MANAGE, mPageManagement)
//        intent.putExtra(Const.BUY, mBuy)
//        intent.putExtra(Const.GOODS, mGoods)
//        startActivity(intent)
//    }
//
//    private fun getOrderId() {
//        val params = HashMap<String, String>()
//        showProgress("")
//        ApiBuilder.create().buyOrderId.setCallback(object : PplusCallback<NewResultResponse<String>> {
//            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//
//                hideProgress()
//                if (response != null) {
//                    val regex = Regex("^[0-9]{22}\\z")
//                    val orderId = response.data
//                    if(orderId.matches(regex)) {
//                        openPg(response.data)
//                    }
//
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun openPg(orderId: String) {
//        val builder = Bootpay.init(this)
//
////        if (StringUtils.isEmpty(mGoods!!.page!!.distributorAgentCode) || mGoods!!.page!!.distributorAgentCode != Const.GAL_COM_CODE) {
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
//        builder.setPG(PG.INICIS) // 결제할 PG 사
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
//                //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
////        when (mKey) {
////            Const.CART -> {
////                var price = 0L
////                for (cart in mCartList!!) {
////                    builder.addItem(cart.goods!!.name, cart.count!!, cart.goods!!.seqNo.toString(), cart.goods!!.price!!.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
////
////                    price = +price + (cart.goods!!.price!! * cart.count!!)
////                }
////                builder.setPrice(price.toInt()) // 결제할 금액
////            }
////            Const.GOODS -> {
////                builder.addItem(mGoods!!.name, mCount, mGoods!!.seqNo.toString(), mGoods!!.price!!.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
////
////
////                var price = mCount * mGoods!!.price!!.toInt()
////                if (mOrderType == EnumData.OrderType.delivery.type && mPageManagement!!.deliveryFee != null && mPageManagement!!.deliveryFee!! > 0) {
////                    price += mPageManagement!!.deliveryFee!!
////                }
////
////                builder.setPrice(price) // 결제할 금액
////            }
////        }
//        builder.addItem(mGoods!!.name, mCount, mGoods!!.seqNo.toString(), mGoods!!.price!!.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
//
//        var price = mCount * mGoods!!.price!!.toInt()
//        if (mOrderType == EnumData.OrderType.delivery.type && mPageManagement!!.deliveryFee != null && mPageManagement!!.deliveryFee!! > 0) {
//            price += mPageManagement!!.deliveryFee!!.toInt()
//        }
//        builder.setPrice(price) // 결제할 금액
//
//        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
//            override fun onConfirm(message: String?) {
//                mBuy.orderId = orderId
//
//                if (mGoods!!.isPlus!! || mGoods!!.isHotdeal!!) {
//                    ApiBuilder.create().postBuyHot(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                        override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                            hideProgress()
//                            if (response != null && response.data != null) {
//
//                                if (response.data.seqNo != null) {
//                                    Bootpay.confirm(message)
//                                    return
//                                }
//                            }
//                            showAlert(R.string.msg_cancel_pg)
//                            Bootpay.removePaymentWindow()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                            hideProgress()
//                            showAlert(R.string.msg_cancel_pg)
//                            Bootpay.removePaymentWindow()
//                        }
//                    }).build().call()
//                } else if (mGoods!!.type == "1") {
//                    ApiBuilder.create().postBuyShop(mBuy).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//                        override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                            hideProgress()
//                            if (response != null && response.data != null) {
//
//                                if (response.data.seqNo != null) {
//                                    Bootpay.confirm(message)
//                                    return
//                                }
//                            }
//                            showAlert(R.string.msg_cancel_pg)
//                            Bootpay.removePaymentWindow()
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                            hideProgress()
//                            showAlert(R.string.msg_cancel_pg)
//                            Bootpay.removePaymentWindow()
//                        }
//                    }).build().call()
//                }
//
//                LogUtil.e(LOG_TAG, "confirm : {}", message)
//            }
//        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
//            override fun onDone(message: String?) {
//                LogUtil.e(LOG_TAG, "done : {}", message)
//
//                val parser = JsonParser()
//                val receiptId = parser.parse(message).asJsonObject.get("receipt_id").asString
////                val regex = Regex("^[a-z0-9]{24}\\z")
//
//                showProgress(getString(R.string.msg_pay_ing))
//                val params = HashMap<String, String>()
//                params["receiptId"] = receiptId
//                ApiBuilder.create().checkBootPay(params).setCallback(object : PplusCallback<NewResultResponse<Buy>>{
//                    override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                        if(response != null && response.data != null){
//                            orderComplete(orderId)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                        hideProgress()
//                    }
//                }).build().call()
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
//    private fun getBuy(orderId: String) {
//        if (retryCount > 40) {
//            hideProgress()
//            finish()
//            return
//        }
//        Thread.sleep(1500)
//        val params = HashMap<String, String>()
//        params["orderId"] = orderId
//        ApiBuilder.create().getOneBuy(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//
//                if (response != null && response.data != null && response.data.process != null) {
//                    if (response.data.process!! > 0) {
//                        hideProgress()
//                        orderComplete(orderId)
//                    } else if (response.data.process!! < 0) {
//                        hideProgress()
//                        showAlert(R.string.msg_error_pg)
//                    } else {
//                        retryCount++
//                        getBuy(orderId)
//                    }
//                } else {
//                    hideProgress()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun orderComplete(orderId: String) {
//        val intent = Intent(this@HotdealBuyActivity, PayCompleteActivity::class.java)
//        intent.putExtra(Const.ID, orderId)
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
//            Const.REQ_SEARCH -> if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    val juso = data.getParcelableExtra<Juso>(Const.ADDRESS)
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
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_info), ToolbarOption.ToolbarMenu.LEFT)
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
