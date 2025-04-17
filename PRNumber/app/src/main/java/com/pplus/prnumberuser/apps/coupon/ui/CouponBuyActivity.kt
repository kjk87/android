//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.CompoundButton
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
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
//import com.pplus.prnumberuser.apps.goods.ui.GoodsCardActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsOrderTermsActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_coupon_buy.*
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
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.set
//
//class CouponBuyActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_coupon_buy
//    }
//
//    var mInstallment = "00"
//
//
//    var mBuy = Buy()
//    var mCoupon: Goods? = null
//    var mCount = 1
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mCoupon = intent.getParcelableExtra(Const.COUPON)
//        mCount = intent.getIntExtra(Const.COUNT, 0)
//
//        if (StringUtils.isNotEmpty(mCoupon!!.page!!.thumbnail)) {
//            Glide.with(this).load(mCoupon!!.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_coupon_buy_page_image)
//        } else {
//            image_coupon_buy_page_image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        text_coupon_buy_name.text = mCoupon!!.name
//
//        if (StringUtils.isNotEmpty(mCoupon!!.expireDatetime)) {
//            text_coupon_buy_expire_date.visibility = View.VISIBLE
//            text_coupon_buy_expire_date.text = getString(R.string.word_expire_date2) + ": " + SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCoupon!!.expireDatetime)) + " " + getString(R.string.word_until)
//        } else {
//            text_coupon_buy_expire_date.visibility = View.GONE
//        }
//
//        if (mCoupon!!.allWeeks != null) {
//            text_coupon_buy_use_dayofweek.visibility = View.VISIBLE
//            if (mCoupon!!.allWeeks!!) {
//                text_coupon_buy_use_dayofweek.text = getString(R.string.word_use_enable_dayofweek) + ": " + getString(R.string.word_every_dayofweek)
//            } else {
//                val dayOfWeek = mCoupon!!.dayOfWeeks!!.replace(",", "/").replace("0", getString(R.string.word_mon)).replace("1", getString(R.string.word_tue))
//                        .replace("2", getString(R.string.word_wed)).replace("3", getString(R.string.word_thu)).replace("4", getString(R.string.word_fri))
//                        .replace("5", getString(R.string.word_sat)).replace("6", getString(R.string.word_sun))
//
//                text_coupon_buy_use_dayofweek.text = getString(R.string.word_use_enable_dayofweek) + ": " + dayOfWeek
//            }
//        } else {
//            text_coupon_buy_use_dayofweek.visibility = View.GONE
//        }
//
//        if (mCoupon!!.allDays != null) {
//            text_coupon_buy_use_time.visibility = View.VISIBLE
//            if (mCoupon!!.allDays!!) {
//                text_coupon_buy_use_time.text = getString(R.string.word_use_enable_time) + ": " + getString(R.string.word_every_time_use)
//            } else {
//                text_coupon_buy_use_time.text = getString(R.string.word_use_enable_time) + ": " + mCoupon!!.startTime + "~" + mCoupon!!.endTime
//            }
//        }
//
//        if (StringUtils.isNotEmpty(mCoupon!!.serviceCondition)) {
//            text_coupon_buy_use_condition.visibility = View.VISIBLE
//            text_coupon_buy_use_condition.setSingleLine()
//            text_coupon_buy_use_condition.text = getString(R.string.word_use_condition) + ": " + mCoupon!!.serviceCondition
//        } else {
//            text_coupon_buy_use_condition.visibility = View.GONE
//        }
//
//        text_coupon_buy_count.text = getString(R.string.word_count) + ": " + mCount
//
//        text_coupon_buy_view_terms1.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS1)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_coupon_buy_view_terms2.setOnClickListener {
//            val intent = Intent(this, GoodsOrderTermsActivity::class.java)
//            intent.putExtra(Const.KEY, Const.TERMS2)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        edit_coupon_buy_name.setSingleLine()
//        edit_coupon_buy_phone.setSingleLine()
//        edit_coupon_buy_phone.setText(LoginInfoManager.getInstance().user.mobile)
//
//        val contents = resources.getStringArray(R.array.report_installment_period)
//        text_coupon_buy_installment.setOnClickListener {
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
//                            text_coupon_buy_installment.text = contents[event_alert.value - 1]
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
//        text_coupon_buy_installment.text = contents[0]
//
//        text_coupon_buy_free_installment.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(this, getString(R.string.msg_goods_card_info_url) + "?timestamp=" + System.currentTimeMillis())
//        }
//
//        text_coupon_buy_pay.setOnClickListener {
//
//            if (!check_coupon_buy_terms1.isChecked || !check_coupon_buy_terms2.isChecked) {
//
//                ToastUtil.show(this, getString(R.string.msg_agree_terms))
//                return@setOnClickListener
//            }
//
//            mBuy.payMethod = "card"
//
//            val name = edit_coupon_buy_name.text.toString().trim()
//            if (StringUtils.isEmpty(name)) {
//                showAlert(R.string.hint_input_name)
//                return@setOnClickListener
//            }
//
//            val phone = edit_coupon_buy_phone.text.toString().trim()
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
//            mBuy.buyerName = name
//            mBuy.buyerTel = phone
//
//            val buyGoodsList = ArrayList<BuyGoods>()
//            val buyGoods = BuyGoods()
//            buyGoods.goodsSeqNo = mCoupon!!.seqNo
//            buyGoods.count = mCount
//            buyGoodsList.add(buyGoods)
//
//            mBuy.buyGoodsSelectList = buyGoodsList
//            mBuy.title = mCoupon!!.name
//
//            if (mCoupon!!.page!!.woodongyi!!) {
//                mBuy.pg = "LPNG"
//                lpngPay()
//            } else {
//                mBuy.pg = "inicis"
//                getOrderId()
//            }
//
////            mBuy.pg = "kicc"
//
//
//        }
//
//        mBuy.memberSeqNo = LoginInfoManager.getInstance().user.no
//
//
//        if (!mCoupon!!.page!!.woodongyi!!) {
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
//        check_coupon_buy_totalAgree.setOnCheckedChangeListener(this)
//        check_coupon_buy_terms1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_coupon_buy_terms2.isChecked) {
//                    isAll = true
//                }
//                check_coupon_buy_totalAgree.setOnCheckedChangeListener(null)
//                check_coupon_buy_totalAgree.isChecked = isAll
//                check_coupon_buy_totalAgree.setOnCheckedChangeListener(this@CouponBuyActivity)
//            }
//        })
//
//        check_coupon_buy_terms2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//
//                var isAll = false
//                if (isChecked && check_coupon_buy_terms1.isChecked) {
//                    isAll = true
//                }
//                check_coupon_buy_totalAgree.setOnCheckedChangeListener(null)
//                check_coupon_buy_totalAgree.isChecked = isAll
//                check_coupon_buy_totalAgree.setOnCheckedChangeListener(this@CouponBuyActivity)
//            }
//        })
//
//        setTotalPrice()
//    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//        check_coupon_buy_terms1.isChecked = isChecked
//        check_coupon_buy_terms2.isChecked = isChecked
//    }
//
//    var totalPrice = 0
//    private fun setTotalPrice() {
//        totalPrice = mCoupon!!.price!!.toInt() * mCount
//
//        text_coupon_buy_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_purchase_price2, FormatUtil.getMoneyType(totalPrice.toString())))
//    }
//
//
//    private fun lpngPay() {
//
//        val intent = Intent(this, GoodsCardActivity::class.java)
//        intent.putExtra(Const.PRICE, totalPrice)
//        intent.putExtra(Const.INSTALLMENT, mInstallment)
//        intent.putExtra(Const.BUY, mBuy)
//        intent.putExtra(Const.GOODS, mCoupon)
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
//                    if (orderId.matches(regex)) {
//                        openPg(response.data)
//                    }
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
//        if (Const.API_URL.startsWith("https://api")) {
//            builder.setApplicationId(getString(R.string.boot_pay_id)) // 해당 프로젝트(안드로이드)의 application id 값
//        } else if (Const.API_URL.startsWith("https://stage")) {
//            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
//        } else {
//            builder.setApplicationId(getString(R.string.boot_pay_id_dev)) // 해당 프로젝트(안드로이드)의 application id 값
//        }
//        builder.setPG(PG.INICIS) // 결제할 PG 사
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
//        builder.addItem(mCoupon!!.name, mCount, mCoupon!!.seqNo.toString(), mCoupon!!.price!!.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
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
//
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
//    private fun orderComplete(orderId: String) {
//        val intent = Intent(this@CouponBuyActivity, CouponPayCompleteActivity::class.java)
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
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
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
