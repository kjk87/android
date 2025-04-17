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
import com.pplus.prnumberuser.apps.common.mgmt.DeliveryAddressManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.data.PurchaseCartAdapter
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.ApiController
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityOrderPurchasePgBinding
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
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class OrderPurchasePgActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {
    override fun getPID(): String {
        return "Main_surrounding sale_product detail_buy"
    }

    private lateinit var binding: ActivityOrderPurchasePgBinding

    override fun getLayoutView(): View {
        binding = ActivityOrderPurchasePgBinding.inflate(layoutInflater)
        return binding.root
    }

    var mPayMethod = "card"
    var mInstallment = "00"
    var mMemo = ""

    //    var mKey = ""
    var mCartList: ArrayList<Cart>? = null
    var mPage: Page2? = null
    var mOrderPurchase = OrderPurchase()
    var mSalesType = 2
    var mMemoInput = false
    var mVisitCount = 1
    var mIsVisitNow:Boolean? = null
    var mDistance:Double? = null
    private var mPurchaseCartAdapter: PurchaseCartAdapter? = null
    private var mCardAdapter: CardSelectAdapter? = null
    private lateinit var mDeliveryAddress: DeliveryAddress

    override fun initializeView(savedInstanceState: Bundle?) {

        mSalesType = intent.getIntExtra(Const.TYPE, 2)
        mCartList = intent.getParcelableArrayListExtra(Const.DATA)
        mPage = intent.getParcelableExtra(Const.PAGE)

        when (mSalesType) {
            1 -> {
                binding.layoutOrderPurchasePgVisitCount.visibility = View.VISIBLE
                binding.layoutOrderPurchasePgVisitTime.visibility = View.VISIBLE
                binding.textOrderPurchasePgVisitNowDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_visit_now_desc, mPage!!.cookingTime.toString()))
                binding.textOrderPurchasePgJibunAddress.visibility = View.GONE
                binding.textOrderPurchasePgRoadAddress.visibility = View.GONE
                binding.editOrderPurchasePgDetailAddress.visibility = View.GONE
                binding.textOrderPurchasePgDisposable.visibility = View.GONE
                binding.textOrderPurchasePgMemo.visibility = View.GONE
                binding.editOrderPurchasePgMemo.visibility = View.VISIBLE
                binding.layoutOrderPurchasePgDeliveryFee.visibility = View.GONE
                binding.viewOrderPurchasePgDeliveryFeeBar.visibility = View.GONE
                binding.layoutOrderPurchasePgOutsideCard.visibility = View.GONE
                binding.viewOrderPurchasePgOutsideCardBar.visibility = View.GONE
                mMemoInput = true
            }
            2 -> {
                binding.layoutOrderPurchasePgVisitCount.visibility = View.GONE
                binding.layoutOrderPurchasePgVisitTime.visibility = View.GONE
                binding.textOrderPurchasePgJibunAddress.visibility = View.VISIBLE
                binding.textOrderPurchasePgRoadAddress.visibility = View.VISIBLE
                binding.editOrderPurchasePgDetailAddress.visibility = View.VISIBLE
                binding.textOrderPurchasePgDisposable.visibility = View.VISIBLE
                binding.textOrderPurchasePgMemo.visibility = View.VISIBLE
                binding.editOrderPurchasePgMemo.visibility = View.GONE
                binding.layoutOrderPurchasePgDeliveryFee.visibility = View.VISIBLE
                binding.viewOrderPurchasePgDeliveryFeeBar.visibility = View.VISIBLE
//                binding.layoutOrderPurchasePgOutsideCard.visibility = View.VISIBLE
//                binding.viewOrderPurchasePgOutsideCardBar.visibility = View.VISIBLE
                binding.layoutOrderPurchasePgOutsideCard.visibility = View.GONE
                binding.viewOrderPurchasePgOutsideCardBar.visibility = View.GONE
                mMemoInput = false
            }
            5 -> {
                binding.layoutOrderPurchasePgVisitCount.visibility = View.GONE
                binding.layoutOrderPurchasePgVisitTime.visibility = View.GONE
                binding.textOrderPurchasePgJibunAddress.visibility = View.GONE
                binding.textOrderPurchasePgRoadAddress.visibility = View.GONE
                binding.editOrderPurchasePgDetailAddress.visibility = View.GONE
                binding.textOrderPurchasePgDisposable.visibility = View.VISIBLE
                binding.textOrderPurchasePgMemo.visibility = View.GONE
                binding.editOrderPurchasePgMemo.visibility = View.VISIBLE
                binding.layoutOrderPurchasePgDeliveryFee.visibility = View.GONE
                binding.viewOrderPurchasePgDeliveryFeeBar.visibility = View.GONE
//                binding.layoutOrderPurchasePgOutsideCard.visibility = View.VISIBLE
//                binding.viewOrderPurchasePgOutsideCardBar.visibility = View.VISIBLE
                binding.layoutOrderPurchasePgOutsideCard.visibility = View.GONE
                binding.viewOrderPurchasePgOutsideCardBar.visibility = View.GONE
                mMemoInput = true
            }
        }

        binding.imageOrderPurchasePgVisitCountMinus.setOnClickListener {

            if (mVisitCount > 1) {
                mVisitCount--
            }
            binding.textOrderPurchasePgVisitCount.text = mVisitCount.toString()

            setTotalPrice()
        }

        binding.imageOrderPurchasePgVisitCountPlus.setOnClickListener {

            mVisitCount++
            binding.textOrderPurchasePgVisitCount.text = mVisitCount.toString()
            setTotalPrice()
        }

        mVisitCount = 1
        binding.textOrderPurchasePgVisitCount.text = mVisitCount.toString()

        binding.textOrderPurchasePgVisitNow.setOnClickListener {
            binding.textOrderPurchasePgVisitNow.isSelected = true
            binding.textOrderPurchasePgBookingVisitTime.isSelected = false

            binding.textOrderPurchasePgVisitNowDesc.visibility = View.VISIBLE
            binding.textOrderPurchasePgVisitTime.visibility = View.GONE
            mIsVisitNow = true
        }

        binding.textOrderPurchasePgBookingVisitTime.setOnClickListener {
            binding.textOrderPurchasePgVisitNow.isSelected = false
            binding.textOrderPurchasePgBookingVisitTime.isSelected = true

            binding.textOrderPurchasePgVisitNowDesc.visibility = View.GONE
            binding.textOrderPurchasePgVisitTime.visibility = View.VISIBLE
            mIsVisitNow = false
        }

        binding.textOrderPurchasePgVisitTime.setOnClickListener {
            val intent = Intent(this, VisitTimePickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            visitTimeLauncher.launch(intent)
        }

        binding.editOrderPurchasePgDetailAddress.setSingleLine()
        binding.editOrderPurchasePgPhone.setSingleLine()

        if(mSalesType == 2){
            mDeliveryAddress = DeliveryAddressManager.getInstance().deliveryAddressList!![0]
            binding.textOrderPurchasePgJibunAddress.text = mDeliveryAddress.jibunAddress
            binding.textOrderPurchasePgRoadAddress.text = "[${getString(R.string.word_road_name)}]${mDeliveryAddress.address}"

            if (StringUtils.isNotEmpty(mDeliveryAddress.addressDetail)) {
                binding.editOrderPurchasePgDetailAddress.setText(mDeliveryAddress.addressDetail)
            }
        }


        binding.editOrderPurchasePgPhone.setText(LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", ""))

        binding.textOrderPurchasePgDisposable.setOnClickListener {
            binding.textOrderPurchasePgDisposable.isSelected = !binding.textOrderPurchasePgDisposable.isSelected
        }

        binding.textOrderPurchasePgMemo.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setLeftText(getString(R.string.word_cancel))
            builder.setContents(getString(R.string.msg_order_req1), getString(R.string.msg_order_req2), getString(R.string.msg_order_req3), getString(R.string.msg_order_req4), getString(R.string.msg_order_req5))
            val contentsList = arrayListOf<String>()
            contentsList.add(getString(R.string.msg_order_req1))
            contentsList.add(getString(R.string.msg_order_req2))
            contentsList.add(getString(R.string.msg_order_req3))
            contentsList.add(getString(R.string.msg_order_req4))
            contentsList.add(getString(R.string.msg_order_req5))

            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> {

                            binding.textOrderPurchasePgMemo.text = contentsList[event_alert.value - 1]

                            when (event_alert.value) {
                                5 -> {
                                    binding.editOrderPurchasePgMemo.visibility = View.VISIBLE
                                    binding.editOrderPurchasePgMemo.requestFocus()
                                    mMemoInput = true
                                }
                                else -> {
                                    binding.editOrderPurchasePgMemo.visibility = View.GONE
                                    mMemoInput = false
                                }
                            }
                        }
                    }
                }
            }).builder().show(this)
        }

        binding.textOrderPurchasePgPay.setOnClickListener {
            val intent = Intent(this, AlertOrderPayActivity::class.java)
            intent.putExtra(Const.KEY, Const.ORDER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            payLauncher.launch(intent)
        }

        mOrderPurchase.pageSeqNo = mPage!!.seqNo
        mOrderPurchase.memberSeqNo = LoginInfoManager.getInstance().user.no

        mPurchaseCartAdapter = PurchaseCartAdapter()
        binding.recyclerOrderPurchasePgMenu.adapter = mPurchaseCartAdapter
        binding.recyclerOrderPurchasePgMenu.layoutManager = LinearLayoutManager(this)
        mPurchaseCartAdapter!!.setDataList(mCartList!!)


        binding.textOrderPurchasePgInstallment.setOnClickListener {
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

                            binding.textOrderPurchasePgInstallment.text = contents[event_alert.value - 1]
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
        binding.recyclerOrderPurchasePgCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerOrderPurchasePgCard.adapter = mCardAdapter

        binding.layoutOrderPurchasePgCard.setOnClickListener {
            binding.layoutOrderPurchasePgCard.isSelected = true
            binding.layoutOrderPurchasePgEasyPay.isSelected = false
            binding.layoutOrderPurchasePgOutsideCard.isSelected = false
            mPayMethod = "card"
            binding.recyclerOrderPurchasePgCard.visibility = View.GONE
            setTotalPrice()
        }

        binding.layoutOrderPurchasePgEasyPay.setOnClickListener {
            binding.layoutOrderPurchasePgCard.isSelected = false
            binding.layoutOrderPurchasePgEasyPay.isSelected = true
            binding.layoutOrderPurchasePgOutsideCard.isSelected = false
            mPayMethod = "ftlink"
            binding.recyclerOrderPurchasePgCard.visibility = View.VISIBLE
            setTotalPrice()
        }

        binding.layoutOrderPurchasePgOutsideCard.setOnClickListener {
            binding.layoutOrderPurchasePgCard.isSelected = false
            binding.layoutOrderPurchasePgEasyPay.isSelected = false
            binding.layoutOrderPurchasePgOutsideCard.isSelected = true
            mPayMethod = "outsideCard"
            binding.recyclerOrderPurchasePgCard.visibility = View.GONE
            setTotalPrice()
        }

        binding.layoutOrderPurchasePgCard.isSelected = true
        binding.layoutOrderPurchasePgEasyPay.isSelected = false
        binding.layoutOrderPurchasePgOutsideCard.isSelected = false
        mPayMethod = "card"
        binding.recyclerOrderPurchasePgCard.visibility = View.GONE
        cardListCall()

        if(mSalesType == 2){

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

            when(mPage!!.riderType) { //1:배달대행사, 2:자체배달, 3:모두사용
                2 -> {
                    mDistance = PplusCommonUtil.calDistance(mDeliveryAddress.latitude!!, mDeliveryAddress.longitude!!, mPage!!.latitude!!, mPage!!.longitude!!)/1000f
                    setTotalPrice()
                }
                else->{
                    val params = java.util.HashMap<String, String>()
                    params["sido"] = mDeliveryAddress.sido!!
                    params["sigungu"] = mDeliveryAddress.sigungu!!

                    if(mDeliveryAddress.dongli!!.endsWith("면")){
                        params["myeon"] = mDeliveryAddress.dongli!!
                    }else{
                        params["dongli"] = mDeliveryAddress.dongli!!
                    }

                    params["roadAddress"] = mDeliveryAddress.address!!
                    params["jibunAddress"] = mDeliveryAddress.jibunAddress!!
                    params["addressDetail"] = mDeliveryAddress.addressDetail!!
                    params["roadName"] = mDeliveryAddress.roadName!!
                    params["buildNo"] = mDeliveryAddress.buildNo!!
                    params["pageSeqNo"] = mPage!!.seqNo.toString()
                    params["price"] = "10000"
                    LogUtil.e(LOG_TAG, params.toString())
                    showProgress("")
                    ApiController.getCSApi().getRiderFee(params).enqueue(object : Callback<ResultRiderFee> {
                        override fun onResponse(call: Call<ResultRiderFee>, response: Response<ResultRiderFee>) {
                            hideProgress()
                            if(response.body() != null){
                                if(response.body()!!.result != null){
                                    val data = response.body()!!.result!!

                                    if(StringUtils.isNotEmpty(data.delivery_distance)){
                                        mDistance = data.delivery_distance!!.toDouble()/1000f
                                        LogUtil.e(LOG_TAG, mDistance.toString())
                                    }else{
                                        mDistance = 1.3
                                    }
                                }else{
                                    mDistance = 1.3
                                }
                                setTotalPrice()
                            }
                        }

                        override fun onFailure(call: Call<ResultRiderFee>, t: Throwable) {
                            hideProgress()
                        }
                    })
                }
            }
        }else{
            setTotalPrice()
        }

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

        when (mSalesType) {
            1->{//방문
                binding.textOrderPurchasePgSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_order_saved_point_desc, getString(R.string.word_visit_complete), FormatUtil.getMoneyType((price * 0.05).toInt().toString())))
            }
            2->{//배달
                binding.textOrderPurchasePgSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_order_saved_point_desc, getString(R.string.word_delivery_complete), FormatUtil.getMoneyType((price * 0.05).toInt().toString())))
            }
            5->{//포장
                binding.textOrderPurchasePgSavePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_order_saved_point_desc, getString(R.string.word_package_complete), FormatUtil.getMoneyType((price * 0.05).toInt().toString())))
            }
        }


        totalPrice += price

        if (mSalesType == 2) {
            if (mPage!!.riderFreePrice != null && mPage!!.riderFreePrice!! > 0) {
                if (totalPrice >= mPage!!.riderFreePrice!!) {
                    binding.textOrderPurchasePgDeliveryFee.text = getString(R.string.word_free_delivery_price)
                } else {
                    if(mPage!!.riderFee != null && mPage!!.riderFee!! > 0){

                        var riderFee = mPage!!.riderFee!!
                        var addRiderFeeDistance = mPage!!.addRiderFeeDistance
                        if(addRiderFeeDistance == null){
                            addRiderFeeDistance = 1.3f
                        }

                        var addRiderFee = mPage!!.addRiderFee
                        if(addRiderFee == null){
                            addRiderFee = 1000f
                        }

                        if(mDistance!!.toFloat() > addRiderFeeDistance){
//                            val totalAddRiderFee = Math.floor(mDistance!!/addRiderFeeDistance).toInt()*addRiderFee
                            mOrderPurchase.addRiderFee = addRiderFee
                            riderFee += addRiderFee.toInt()
                        }

                        totalPrice += riderFee
                        binding.textOrderPurchasePgDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(riderFee.toString()))

                    }else{
                        binding.textOrderPurchasePgDeliveryFee.text = getString(R.string.word_free_delivery_price)
                    }

                }
            }else{
                if(mPage!!.riderFee != null && mPage!!.riderFee!! > 0){

                    var riderFee = mPage!!.riderFee!!
                    var addRiderFeeDistance = mPage!!.addRiderFeeDistance
                    if(addRiderFeeDistance == null){
                        addRiderFeeDistance = 1.3f
                    }

                    var addRiderFee = mPage!!.addRiderFee
                    if(addRiderFee == null){
                        addRiderFee = 1000f
                    }

                    if(mDistance!!.toFloat() > addRiderFeeDistance){
//                        val totalAddRiderFee = Math.floor(mDistance!!/addRiderFeeDistance).toInt()*addRiderFee
                        mOrderPurchase.addRiderFee = addRiderFee
                        riderFee += addRiderFee.toInt()
                    }

                    totalPrice += riderFee
                    binding.textOrderPurchasePgDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(riderFee.toString()))
                }else{
                    binding.textOrderPurchasePgDeliveryFee.text = getString(R.string.word_free_delivery_price)
                }
            }
        }

        binding.textOrderPurchasePgTotalPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(totalPrice.toInt().toString())))

        checkInstallment()
    }

    private fun checkInstallment() {

        when (mPayMethod) {
            "ftlink" -> {
                val price = totalPrice
                if (price >= 50000) {
                    binding.layoutOrderPurchasePgInstallment.visibility = View.VISIBLE
                } else {
                    binding.layoutOrderPurchasePgInstallment.visibility = View.GONE
                }
            }
            else -> {
                binding.layoutOrderPurchasePgInstallment.visibility = View.GONE
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
        val intent = Intent(this, OrderPayCompleteActivity::class.java)
        intent.putExtra(Const.TYPE, mSalesType)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        payCompleteLauncher.launch(intent)
    }

    private fun buy() {
        mOrderPurchase.payMethod = mPayMethod
        mOrderPurchase.salesType = mSalesType
        when (mSalesType) {
            1 -> {
                mOrderPurchase.visitNumber = mVisitCount
                if(mIsVisitNow == null){
                    showAlert(R.string.msg_select_visit_time)
                    return
                }

                mOrderPurchase.isVisitNow = mIsVisitNow

                if(!mIsVisitNow!!){
                    val visitTime = binding.textOrderPurchasePgVisitTime.text.toString()
                    if(StringUtils.isEmpty(visitTime)){
                        showAlert(R.string.msg_select_visit_time)
                        return
                    }

                    val output = SimpleDateFormat("yyyy-MM-dd")
                    val dateStr = output.format(Date())
                    mOrderPurchase.visitTime = "${dateStr} ${visitTime}:00"
                }
            }
            2 -> {
                val addressDetail = binding.editOrderPurchasePgDetailAddress.text.toString().trim()
                if (StringUtils.isEmpty(addressDetail)) {
                    showAlert(R.string.msg_input_address_detail)
                    return
                }

                mOrderPurchase.address = mDeliveryAddress.address
                mOrderPurchase.addressDetail = addressDetail
                mOrderPurchase.disposableRequired = binding.textOrderPurchasePgDisposable.isSelected
            }
            5 -> {
                mOrderPurchase.disposableRequired = binding.textOrderPurchasePgDisposable.isSelected
            }
        }

        val phone = binding.editOrderPurchasePgPhone.text.toString().trim()
        if (StringUtils.isEmpty(phone)) {
            showAlert(R.string.hint_input_contact)
            return
        }

        if (!FormatUtil.isPhoneNumber(phone)) {
            showAlert(R.string.msg_invalid_phone_number)
            return
        }

        mOrderPurchase.phone = phone

        if (mMemoInput) {
            val memo = binding.editOrderPurchasePgMemo.text.toString().trim()
            if (StringUtils.isNotEmpty(memo)) {
                mOrderPurchase.memo = memo
            }
        } else {
            val memo = binding.textOrderPurchasePgMemo.text.toString().trim()
            if (StringUtils.isNotEmpty(memo)) {
                mOrderPurchase.memo = memo
            }
        }

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
                                        val intent = Intent(this@OrderPurchasePgActivity, VerificationMeActivity::class.java)
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
                                    val intent = Intent(this@OrderPurchasePgActivity, CardRegActivity::class.java)
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
            when (mSalesType) {
                1 -> {
                    if(mIsVisitNow == null){
                        showAlert(R.string.msg_select_visit_time)
                        return@registerForActivityResult
                    }

                    if(!mIsVisitNow!!){
                        val visitTime = binding.textOrderPurchasePgVisitTime.text.toString()
                        if(StringUtils.isEmpty(visitTime)){
                            showAlert(R.string.msg_select_visit_time)
                            return@registerForActivityResult
                        }
                    }
                }
                2 -> {
                    val addressDetail = binding.editOrderPurchasePgDetailAddress.text.toString().trim()
                    if (StringUtils.isEmpty(addressDetail)) {
                        showAlert(R.string.msg_input_address_detail)
                        return@registerForActivityResult
                    }
                }
                5 -> {
                    mOrderPurchase.disposableRequired = binding.textOrderPurchasePgDisposable.isSelected
                }
            }

            val phone = binding.editOrderPurchasePgPhone.text.toString().trim()
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

    val visitTimeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if(data != null){
                val visitTime = data.getStringExtra(Const.DATA)
                if(mPage!!.businessHoursType != 4){
                    val cal = Calendar.getInstance()
                    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                    val pageBusinessHoursList = mPage!!.pageBusinessHoursList!!.filter {
                        it.day == dayOfWeek
                    }
                    if(pageBusinessHoursList.isNotEmpty()){
                        LogUtil.e(LOG_TAG, "openTime : " + pageBusinessHoursList[0].openTime)
                        LogUtil.e(LOG_TAG, "closeTime : " + pageBusinessHoursList[0].closeTime)
                        val opens = pageBusinessHoursList[0].openTime!!.split(":")
                        val closes = pageBusinessHoursList[0].closeTime!!.split(":")
                        val visits = visitTime!!.split(" ")[1].split(":")
                        val openMin = opens[0].toInt()*60 + opens[1].toInt()
                        val closeMin = closes[0].toInt()*60 + closes[1].toInt()
                        val visitMin = visits[0].toInt()*60 + visits[1].toInt()

                        if(openMin > closeMin){
                            if(openMin <= visitMin || closeMin >= visitMin){
                                binding.textOrderPurchasePgVisitTime.text = visitTime.split(" ")[1]
                            }else{
                                showAlert(R.string.msg_visit_enable_in_open_time)
                            }
                        }else{
                            if(visitMin in openMin..closeMin){
                                binding.textOrderPurchasePgVisitTime.text = visitTime.split(" ")[1]
                            }else{
                                showAlert(R.string.msg_visit_enable_in_open_time)
                            }
                        }
                    }

                }else{
                    binding.textOrderPurchasePgVisitTime.text = visitTime!!.split(" ")[1]
                }


            }
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
