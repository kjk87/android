package com.pplus.prnumberuser.apps.menu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.card.data.CardSelectAdapter
import com.pplus.prnumberuser.apps.card.ui.CardRegActivity
import com.pplus.prnumberuser.apps.card.ui.PayPasswordCheckActivity
import com.pplus.prnumberuser.apps.card.ui.PayPasswordSetActivity
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.PurchaseCartAdapter
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityTicketPurchasePgBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import kr.co.bootpay.Bootpay
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.listener.*
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap


class TicketPurchasePgActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
    override fun getPID(): String {
        return "Main_surrounding sale_product detail_buy"
    }

    private lateinit var binding: ActivityTicketPurchasePgBinding

    override fun getLayoutView(): View {
        binding = ActivityTicketPurchasePgBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPayMethod = "card"
    var mInstallment = "00"
    var mMemo = ""

    //    var mKey = ""
    var mCartList: ArrayList<Cart>? = null
    var mPage: Page2? = null
    var mOrderPurchase = OrderPurchase()
    private var mPurchaseCartAdapter: PurchaseCartAdapter? = null
    private var mCardAdapter: CardSelectAdapter? = null
    private lateinit var mDeliveryAddress: DeliveryAddress

    override fun initializeView(savedInstanceState: Bundle?) {

        mCartList = intent.getParcelableArrayListExtra(Const.DATA)
        mPage = intent.getParcelableExtra(Const.PAGE)

        binding.textTicketPurchasePgCaution.text = PplusCommonUtil.fromHtml(getString(R.string.html_ticket_purchase_caution))

        binding.editTicketPurchasePgPhone.setSingleLine()
        binding.editTicketPurchasePgPhone.setText(LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", ""))

        binding.textTicketPurchasePgPay.setOnClickListener {
            val intent = Intent(this, AlertOrderPayActivity::class.java)
            intent.putExtra(Const.KEY, Const.TICKET)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            payLauncher.launch(intent)
        }

        mOrderPurchase.pageSeqNo = mPage!!.seqNo
        mOrderPurchase.memberSeqNo = LoginInfoManager.getInstance().user.no

        mPurchaseCartAdapter = PurchaseCartAdapter()
        binding.recyclerTicketPurchasePgMenu.adapter = mPurchaseCartAdapter
        binding.recyclerTicketPurchasePgMenu.layoutManager = LinearLayoutManager(this)
        mPurchaseCartAdapter!!.setDataList(mCartList!!)


        binding.textTicketPurchasePgInstallment.setOnClickListener {
            val contents = resources.getStringArray(R.array.report_installment_period)
            val builder = AlertBuilder.Builder()
            builder.setContents(*contents)
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO)
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RADIO -> {

                            binding.textTicketPurchasePgInstallment.text = contents[event_alert.value - 1]
                            if (event_alert.value == 1) {
                                mInstallment = "00"
                            } else if (event_alert.value < 10) {
                                mInstallment = "0${event_alert.value}"
                            } else {
                                mInstallment = event_alert.value.toString()
                            }
                        }
                    }
                }
            }).builder().show(this)
        }

        binding.textPurchaseProductShipTerms.text = getString(R.string.msg_product_purchase_agree_desc)


        mCardAdapter = CardSelectAdapter()
        binding.recyclerTicketPurchasePgCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerTicketPurchasePgCard.adapter = mCardAdapter

        binding.layoutTicketPurchasePgCard.setOnClickListener {
            binding.layoutTicketPurchasePgCard.isSelected = true
            binding.layoutTicketPurchasePgEasyPay.isSelected = false
            mPayMethod = "card"
            binding.recyclerTicketPurchasePgCard.visibility = View.GONE
            setTotalPrice()
        }

        binding.layoutTicketPurchasePgEasyPay.setOnClickListener {
            binding.layoutTicketPurchasePgCard.isSelected = false
            binding.layoutTicketPurchasePgEasyPay.isSelected = true
            mPayMethod = "ftlink"
            binding.recyclerTicketPurchasePgCard.visibility = View.VISIBLE
            setTotalPrice()
        }

        binding.layoutTicketPurchasePgCard.isSelected = true
        binding.layoutTicketPurchasePgEasyPay.isSelected = false
        mPayMethod = "card"
        binding.recyclerTicketPurchasePgCard.visibility = View.GONE
        cardListCall()

        setTotalPrice()

    }

    private fun cardListCall() {
        showProgress("")
        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
                hideProgress()
                if (response?.datas != null) {
                    if (response.datas.isNotEmpty()) {
                        mCardAdapter!!.mSelectData = response.datas[0]
                    }
                    mCardAdapter!!.setDataList(response.datas)
                    mCardAdapter!!.add(Card())
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?, t: Throwable?, response: NewResultResponse<Card>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        //        check_purchase_product_ship_terms1.isChecked = isChecked
        //        check_purchase_product_ship_terms2.isChecked = isChecked
    }

    var totalPrice = 0f
    private fun setTotalPrice() {
        totalPrice = 0f

        var price = 0f
        for (cart in mCartList!!) {

            price += cart.orderMenu!!.price!!
            var optionPrice = 0f
            for (cartOption in cart.cartOptionList!!) {

                if (cartOption.menuOptionDetail!!.price == null) {
                    cartOption.menuOptionDetail!!.price = 0f
                }

                optionPrice += cartOption.menuOptionDetail!!.price!!
            }
            price += optionPrice

            price *= cart.amount!!

        }

        binding.textTicketPurchasePgSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_ticket_saved_point_desc, FormatUtil.getMoneyType((price * 0.05).toInt().toString())))
        totalPrice += price

        binding.textTicketPurchasePgTotalPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(totalPrice.toInt().toString())))

        checkInstallment()
    }

    private fun checkInstallment() {

        when (mPayMethod) {
            "ftlink" -> {
                val price = totalPrice
                if (price >= 50000) {
                    binding.layoutTicketPurchasePgInstallment.visibility = View.VISIBLE
                } else {
                    binding.layoutTicketPurchasePgInstallment.visibility = View.GONE
                }
            }
            else -> {
                binding.layoutTicketPurchasePgInstallment.visibility = View.GONE
            }
        }

    }

    private fun orderId() {
        showProgress("")
        ApiBuilder.create().postOrderId().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {

                hideProgress()
                if (response != null) { //                    val regex = Regex("^[0-9]{22}\\z")
                    val orderId = response.data

                    when (mPayMethod) {
                        "card" -> {
                            openPg(orderId)
                        } //                        "ftlink", "outsideCard" -> {
                        else -> {
                            orderPurchase(orderId, null)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun openPg(orderId: String) {
        val builder = Bootpay.init(this)
        if (Const.API_URL.startsWith("https://api")) {
            builder.setApplicationId(getString(R.string.boot_pay_cash_id)) // 해당 프로젝트(안드로이드)의 application id 값
        } else {
            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
        }

        val bootUser = BootUser()
        bootUser.username = LoginInfoManager.getInstance().user.name
        bootUser.phone = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "")
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))

        builder.setBootUser(bootUser).setMethod(Method.CARD) // 결제수단
            .setPG(PG.DANAL) // 결제수단
            .setContext(this).setName(mOrderPurchase.title) // 결제할 상품명
            .setBootExtra(bootExtra).setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
        //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
        builder.addItem(mOrderPurchase.title, mOrderPurchase.amount!!, orderId, totalPrice.toInt()) // 주문정보에 담길 상품정보, 통계를 위해 사용
        builder.setPrice(totalPrice.toInt())

        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
            override fun onConfirm(message: String?) {
                orderPurchase(orderId, message)
                LogUtil.e(LOG_TAG, "confirm : {}", message)
            }
        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
            override fun onDone(message: String?) {
                LogUtil.e(LOG_TAG, "done : {}", message)

                val receiptId = JsonParser.parseString(message).asJsonObject.get("receipt_id").asString

                verify(mOrderPurchase.orderId!!, receiptId)

            }
        }).onReady(object : ReadyListener {
            override fun onReady(message: String?) {
                LogUtil.e(LOG_TAG, "ready : {}", message)
            }
        }).onCancel(object : CancelListener {
            override fun onCancel(message: String?) {
                LogUtil.e(LOG_TAG, "cancel : {}", message)
                showAlert(R.string.msg_cancel_pg) //                deleteBuy(buy.seqNo.toString())
            }
        }).onError(object : ErrorListener {
            override fun onError(message: String?) {
                LogUtil.e(LOG_TAG, "error : {}", message)
                showAlert(R.string.msg_error_pg) //                deleteBuy(buy.seqNo.toString())
            }
        }).onClose(object : CloseListener {
            override fun onClose(message: String?) {
                LogUtil.e(LOG_TAG, "close : {}", message)
            }
        }).request()
    }

    private fun orderPurchase(orderId: String, message: String?) {
        mOrderPurchase.orderId = orderId
        when (mPayMethod) {
            "card" -> {
                mOrderPurchase.pg = "DANAL"
            }
            "ftlink" -> {
                mOrderPurchase.pg = "FTLINK"
            }
        }

        //        mOrderPurchase.installment = mInstallment
        showProgress("")
        ApiBuilder.create().orderPurchase(mOrderPurchase).setCallback(object : PplusCallback<NewResultResponse<OrderPurchase>> {
            override fun onResponse(call: Call<NewResultResponse<OrderPurchase>>?, response: NewResultResponse<OrderPurchase>?) {
                hideProgress()
                if (response?.data != null && response.data.seqNo != null) {
                    when (mPayMethod) {
                        "card" -> {

                            if (totalPrice == response.data.price) {
                                Bootpay.confirm(message)
                            } else {
                                Bootpay.removePaymentWindow()
                                showAlert(R.string.msg_error_pg)
                            }

                        }
                        "ftlink" -> {
                            ftLinkPay()
                        }
                        else -> {
                            orderComplete()
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<OrderPurchase>>?, t: Throwable?, response: NewResultResponse<OrderPurchase>?) {
                hideProgress()
                if (mPayMethod == "card") {
                    Bootpay.removePaymentWindow()
                }
                if (response?.resultCode == 510) {
                    showAlert(R.string.msg_expired_product)
                } else if (response?.resultCode == 516) {
                    showAlert(R.string.msg_can_not_purchase_time)
                } else {
                    showAlert(R.string.msg_cancel_pg)
                }

            }
        }).build().call()
    }

    private fun verify(orderId: String, receiptId: String) {
        val params = HashMap<String, String>()
        params["orderId"] = orderId
        params["receiptId"] = receiptId

        showProgress("")
        ApiBuilder.create().orderPurchaseVerifyBootPay(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                orderComplete()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_error_pg)
            }
        }).build().call()
    }

    private fun ftLinkPay() {
        val params = FTLink()
        params.shopcode = mPage!!.shopCode //        params.loginId = LoginInfoManager.getInstance().user.loginId
        params.order_req_amt = totalPrice.toInt().toString()
        params.order_hp = mOrderPurchase.phone
        params.order_name = LoginInfoManager.getInstance().user.nickname
        params.order_goodsname = mOrderPurchase.title
        params.req_installment = mInstallment
        params.comp_memno = LoginInfoManager.getInstance().user.no.toString()
        params.autokey = mCardAdapter!!.mSelectData!!.autoKey
        params.req_cardcode = mCardAdapter!!.mSelectData!!.cardCode
        params.comp_orderno = mOrderPurchase.orderId
        showProgress("")
        ApiBuilder.create().orderPurchaseFTLinkPay(params).setCallback(object : PplusCallback<NewResultResponse<FTLink>> {
            override fun onResponse(call: Call<NewResultResponse<FTLink>>?, response: NewResultResponse<FTLink>?) {
                hideProgress()
                orderComplete()
            }

            override fun onFailure(call: Call<NewResultResponse<FTLink>>?, t: Throwable?, response: NewResultResponse<FTLink>?) {
                hideProgress()
                if (response != null && response.data != null && StringUtils.isNotEmpty(response.data.errMessage)) {
                    showAlert(response.data.errMessage)
                }
            }
        }).build().call()
    }

    private fun orderComplete() {
        val intent = Intent(this, TicketPayCompleteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        payCompleteLauncher.launch(intent)
    }

    private fun buy() {
        mOrderPurchase.payMethod = mPayMethod
        mOrderPurchase.salesType = 6

        val phone = binding.editTicketPurchasePgPhone.text.toString().trim()
        if (StringUtils.isEmpty(phone)) {
            showAlert(R.string.hint_input_contact)
            return
        }

        if (!FormatUtil.isPhoneNumber(phone)) {
            showAlert(R.string.msg_invalid_phone_number)
            return
        }

        mOrderPurchase.phone = phone

        val orderPurchaseMenuList = arrayListOf<OrderPurchaseMenu>()
        val title = StringBuilder()
        var amount = 0
        for ((i, cart) in mCartList!!.withIndex()) {

            if (i != 0) {
                title.append(", ")
            }
            title.append(cart.orderMenu!!.title)

            val orderPurchaseMenu = OrderPurchaseMenu()
            orderPurchaseMenu.orderMenuSeqNo = cart.orderMenuSeqNo
            orderPurchaseMenu.amount = cart.amount
            orderPurchaseMenu.title = cart.orderMenu!!.title
            amount += cart.amount!!

            val orderPurchaseMenuOptionList = arrayListOf<OrderPurchaseMenuOption>()
            for (cartOption in cart.cartOptionList!!) {
                val orderPurchaseMenuOption = OrderPurchaseMenuOption()
                orderPurchaseMenuOption.menuOptionDetailSeqNo = cartOption.menuOptionDetailSeqNo
                orderPurchaseMenuOption.type = cartOption.type
                orderPurchaseMenuOption.price = cartOption.menuOptionDetail!!.price
                orderPurchaseMenuOption.title = cartOption.menuOptionDetail!!.title
                orderPurchaseMenuOptionList.add(orderPurchaseMenuOption)
            }

            orderPurchaseMenu.orderPurchaseMenuOptionList = orderPurchaseMenuOptionList
            orderPurchaseMenuList.add(orderPurchaseMenu)
        }

        mOrderPurchase.orderPurchaseMenuList = orderPurchaseMenuList
        mOrderPurchase.title = title.toString()
        mOrderPurchase.amount = amount

        mOrderPurchase.appType = Const.APP_TYPE

        when (mPayMethod) {
            "card" -> {
                orderId()
            }
            "ftlink" -> {
                if (mCardAdapter!!.mSelectData == null) {

                    if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {}
                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@TicketPurchasePgActivity, VerificationMeActivity::class.java)
                                        intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        verificationLauncher.launch(intent)
                                    }
                                }
                            }
                        }).builder().show(this)

                        return
                    }

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_not_exist_card), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {
                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val intent = Intent(this@TicketPurchasePgActivity, CardRegActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    cardRegLauncher.launch(intent)
                                }
                            }

                        }
                    }).builder().show(this)
                } else {
                    val intent = Intent(this, PayPasswordCheckActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    checkPasswordLauncher.launch(intent)
                }
            }
            "outsideCard" -> {
                orderId()
            }
        }
    }

    val payLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val phone = binding.editTicketPurchasePgPhone.text.toString().trim()
            if (StringUtils.isEmpty(phone)) {
                showAlert(R.string.hint_input_contact)
                return@registerForActivityResult
            }

            if (!FormatUtil.isPhoneNumber(phone)) {
                showAlert(R.string.msg_invalid_phone_number)
                return@registerForActivityResult
            }

            buy()
        }
    }

    val cardRegLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!) {
                val intent = Intent(this, PayPasswordSetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                changePasswordLauncher.launch(intent)
            }
            cardListCall()
        }
    }

    val checkPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            orderId()
        }
    }

    val payCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(result.resultCode)
        finish()
    }

    val changePasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_order), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
