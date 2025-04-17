package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.JsonParser
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.card.data.CardSelectAdapter
import com.pplus.luckybol.apps.card.ui.CardRegActivity
import com.pplus.luckybol.apps.card.ui.PayPasswordCheckActivity
import com.pplus.luckybol.apps.card.ui.PayPasswordSetActivity
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityEventBuyBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import kr.co.bootpay.Bootpay
import kr.co.bootpay.BootpayAnalytics
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.listener.*
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser
import retrofit2.Call

class EventBuyActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityEventBuyBinding

    override fun getLayoutView(): View {
        binding = ActivityEventBuyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mCount = 1
    var mMyJoinCount = 0
    var mPayMethod = "card"
    var mEventBuy = EventBuy()
    var mEvent: Event? = null
    var mInstallment = "00"
    private var mAdapter: CardSelectAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.EVENT)

        when(mEvent!!.primaryType){
            EventType.PrimaryType.randomluck.name->{
                setTitle(getString(R.string.word_event_buy_title))
                binding.textEventBuyPointDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_event_buy_point_desc))
                binding.textEventBuyCaution1.text = getString(R.string.msg_point_buy_caution_desc1)
                binding.textEventBuyCaution2.text = getString(R.string.msg_point_buy_caution_desc2)
                binding.textEventBuyCaution3.text = getString(R.string.msg_point_buy_caution_desc3)
            }
            EventType.PrimaryType.benefit.name->{
                setTitle(getString(R.string.word_event_buy_title2))
                binding.textEventBuyPointDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_event_buy_point_desc2))
                binding.textEventBuyCaution1.text = getString(R.string.msg_point_buy_caution_desc1_2)
                binding.textEventBuyCaution2.text = getString(R.string.msg_point_buy_caution_desc2_2)
                binding.textEventBuyCaution3.text = getString(R.string.msg_point_buy_caution_desc3_2)
            }
        }

        //        image_event_buy_back.setOnClickListener {
        //            onBackPressed()
        //        }

        binding.layoutEventBuyCountMinus.setOnClickListener {
            if (mCount > 1) {
                mCount--
                binding.textEventBuyCount.text = mCount.toString()
                setTotalCount()
            }
        }

        binding.appBarEventBuy.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                LogUtil.e(LOG_TAG, "verticalOffset : " + verticalOffset)
                binding.imageEventBuyPointPopup.visibility = View.GONE
            }
        })

        binding.scrollEventBuy.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?,
                                        scrollX: Int,
                                        scrollY: Int,
                                        oldScrollX: Int,
                                        oldScrollY: Int) {
                binding.imageEventBuyPointPopup.visibility = View.GONE
                LogUtil.e(LOG_TAG, "scroll : " + scrollY)
            }
        })

        binding.layoutEventBuyCountPlus.setOnClickListener {



            if (mCount >= 50) {
                return@setOnClickListener
            }

            if(mEvent!!.maxJoinCount != null && mEvent!!.maxJoinCount!! > 0){
                val possibleCount = mEvent!!.maxJoinCount!! - mEvent!!.joinCount!!

                if (mCount >= possibleCount) {
                    return@setOnClickListener
                }
            }

            if (mEvent!!.buyType != "always" && mEvent!!.buyLimitCount != null && mEvent!!.buyLimitCount!! > 0) {
                val myPossibleCount = mEvent!!.buyLimitCount!! - mMyJoinCount
                if (mCount >= myPossibleCount) {
                    ToastUtil.show(this, R.string.msg_exceed_join_limit_count)
                    return@setOnClickListener
                }
            }

            mCount++
            binding.textEventBuyCount.text = mCount.toString()
            setTotalCount()
        }

        mCount = 1
        binding.textEventBuyCount.text = mCount.toString()
        setTotalCount()

        val contents = resources.getStringArray(R.array.report_installment_period)
        binding.textEventBuyInstallment.setOnClickListener {

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

                            binding.textEventBuyInstallment.text = contents[event_alert.value - 1]
                            if (event_alert.value == 1) {
                                mInstallment = "00"
                            } else if (event_alert.value < 10) {
                                mInstallment = "0${event_alert.value}"
                            } else {
                                mInstallment = event_alert.value.toString()
                            }
                        }
                        else -> {}
                    }
                }
            }).builder().show(this)
        }

        mInstallment = "00"
        binding.textEventBuyInstallment.text = contents[0]

        binding.layoutEventBuyCautionTitle.setOnClickListener {
            if (binding.layoutEventBuyCaution.visibility == View.VISIBLE) {
                binding.imageEventBuyArrow.setImageResource(R.drawable.ic_arrow_down)
                binding.layoutEventBuyCaution.visibility = View.GONE
            } else {
                binding.imageEventBuyArrow.setImageResource(R.drawable.ic_arrow_up)
                binding.layoutEventBuyCaution.visibility = View.VISIBLE
            }
        }

        //        text_event_buy_point_guide.setOnClickListener {
        //
        //            text_event_buy_point_guide.post {
        //                val location = IntArray(2)
        //                text_event_buy_point_guide.getLocationOnScreen(location)
        //                val x = location[0] + text_event_buy_point_guide.width / 2 - resources.getDimensionPixelSize(R.dimen.width_256)
        //                val y = location[1] - text_event_buy_point_guide.height - resources.getDimensionPixelSize(R.dimen.height_254)
        //                (image_event_buy_point_popup.layoutParams as RelativeLayout.LayoutParams).marginStart = x
        //                (image_event_buy_point_popup.layoutParams as RelativeLayout.LayoutParams).topMargin = y
        //                image_event_buy_point_popup.requestLayout()
        //            }
        //
        //            if (image_event_buy_point_popup.visibility == View.VISIBLE) {
        //                image_event_buy_point_popup.visibility = View.GONE
        //            } else {
        //                image_event_buy_point_popup.post {
        //                    image_event_buy_point_popup.visibility = View.VISIBLE
        //                }
        //            }
        //        }

        binding.textEventBuyCard.setOnClickListener {
            binding.textEventBuyCard.isSelected = true
            binding.textEventBuyEasyPay.isSelected = false
            mPayMethod = "card"
            binding.layoutEventBuyEasyPay.visibility = View.GONE
        }

        binding.textEventBuyEasyPay.setOnClickListener {
            binding.textEventBuyCard.isSelected = false
            binding.textEventBuyEasyPay.isSelected = true
            mPayMethod = "ftlink"
            binding.layoutEventBuyEasyPay.visibility = View.VISIBLE
        }

        mAdapter = CardSelectAdapter()
        binding.recyclerEventBuyEasyPayCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerEventBuyEasyPayCard.adapter = mAdapter
        cardListCall()

        binding.textEventBuyCard.isSelected = true
        binding.textEventBuyEasyPay.isSelected = false
        mPayMethod = "card"

        binding.textEventBuyPay.setOnClickListener {

            if(mEvent!!.maxJoinCount != null && mEvent!!.maxJoinCount!! > 0){
                val possibleCount = mEvent!!.maxJoinCount!! - mEvent!!.joinCount!!

                if (mCount > possibleCount) {
                    showAlert(R.string.msg_over_joinable_count)
                    return@setOnClickListener
                }
            }

            if (mEvent!!.buyType != "always" && mEvent!!.buyLimitCount != null && mEvent!!.buyLimitCount!! > 0) {
                val myPossibleCount = mEvent!!.buyLimitCount!! - mMyJoinCount
                LogUtil.e(LOG_TAG, "myPossibleCount : {}", myPossibleCount)
                if (mCount > myPossibleCount) {
                    showAlert(R.string.msg_over_joinable_count)
                    return@setOnClickListener
                }
            }

            mEventBuy = EventBuy()
            mEventBuy.memberSeqNo = LoginInfoManager.getInstance().user.no
            mEventBuy.totalPrice = mEvent!!.rewardPlay!!.toInt() * mCount

            var useBol = 0
            if (StringUtils.isNotEmpty(binding.editEventBuyUseBol.text.toString().trim())) {
                useBol = binding.editEventBuyUseBol.text.toString().trim().toInt()
            }

            if (useBol > 0) {
                mEventBuy.pgPrice = mEventBuy.totalPrice!! - useBol
                mEventBuy.bolPrice = useBol
            } else {
                mEventBuy.pgPrice = mEventBuy.totalPrice
                mEventBuy.bolPrice = 0
            }

            if(mEventBuy.pgPrice!! > 0 && mEventBuy.pgPrice!! < 1000){
                showAlert(R.string.msg_alert_pay_over_1000)
                return@setOnClickListener
            }

            if (mEventBuy.pgPrice!! <= 0) {
                mPayMethod = "point"
            }

            mEventBuy.payMethod = mPayMethod
            mEventBuy.eventSeqNo = mEvent!!.no
            mEventBuy.count = mCount

            if (mPayMethod == "card") {
                if (Const.API_URL.startsWith("https://api")) {
                    BootpayAnalytics.init(this, getString(R.string.boot_pay_cash_id))
                } else {
                    BootpayAnalytics.init(this, getString(R.string.boot_pay_id_stage))
                }

                mEventBuy.pg = "danal"
                getOrderId()
            } else if (mPayMethod == "point") {
                mEventBuy.pg = "point"
                getOrderId()
            } else {

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
                                    val intent = Intent(this@EventBuyActivity, VerificationMeActivity::class.java)
                                    intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    verificationLauncher.launch(intent)
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(this)
                    return@setOnClickListener
                }

                if (mAdapter!!.mSelectData == null) {
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
                                    val intent = Intent(this@EventBuyActivity, CardRegActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    regLauncher.launch(intent)
                                }
                                else -> {}
                            }

                        }
                    }).builder().show(this)
                } else {

                    mEventBuy.installment = mInstallment
                    mEventBuy.cardCode = mAdapter!!.mSelectData!!.cardCode
                    mEventBuy.autoKey = mAdapter!!.mSelectData!!.autoKey
                    mEventBuy.pg = "dau"

                    val intent = Intent(this, PayPasswordCheckActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    checkPasswordLauncher.launch(intent)
                }
            }
        }

        binding.editEventBuyUseBol.setSingleLine()
        binding.editEventBuyUseBol.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    val amount = p0.toString().toInt()

                    if (amount <= 0) {
                        binding.editEventBuyUseBol.setText("")
                        return
                    }

                    val totalPrice = mEvent!!.rewardPlay!! * mCount

                    if (totalPrice > LoginInfoManager.getInstance().user.totalBol) {
                        if (amount > LoginInfoManager.getInstance().user.totalBol) {
                            binding.editEventBuyUseBol.setText(LoginInfoManager.getInstance().user.totalBol.toString())
                        }
                    } else {
                        if (amount > totalPrice) {
                            binding.editEventBuyUseBol.setText(totalPrice.toString())
                        }

                    }
                }

                setTotalCount()
            }
        })

        binding.textEventBuyTotalUseBol.setOnClickListener {
            val totalPrice = mEvent!!.rewardPlay!! * mCount
            if (totalPrice > LoginInfoManager.getInstance().user.totalBol) {
                binding.editEventBuyUseBol.setText(LoginInfoManager.getInstance().user.totalBol.toString())
            } else {
                binding.editEventBuyUseBol.setText(totalPrice.toString())
            }
        }

        setRetentionBol()
        getEvent()
    }

    private fun getEvent() {
        val params = java.util.HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                hideProgress()
                if (response?.data != null) {
                    mEvent = response.data
//                    setTitle(mEvent!!.title)
                    if (mEvent!!.primaryType == EventType.PrimaryType.randomluck.name) {
                        binding.appBarEventBuy.visibility = View.GONE
                        binding.textEventBuyGiftName.visibility = View.GONE
                        binding.textEventBuyGiftPrice.visibility = View.GONE
                        //                        Glide.with(this@EventBuyActivity).load(mEvent!!.totalGiftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(image_event_buy_gift)
                        binding.textEventBuyGiftUrl.visibility = View.GONE
                    } else {
                        binding.appBarEventBuy.visibility = View.VISIBLE
                        binding.textEventBuyGiftName.visibility = View.VISIBLE
                        binding.textEventBuyGiftPrice.visibility = View.VISIBLE
                        binding.textEventBuyGiftUrl.visibility = View.VISIBLE
                        getGiftAll()
                    }

                    binding.textEventBuyJoinPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(mEvent!!.rewardPlay.toString()))
                    binding.textEventBuyJoinPoint.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(mEvent!!.earnedPoint.toString()))

                    getMyJoinCount()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setTotalCount() {
        val totalPrice = mEvent!!.rewardPlay!! * mCount

        binding.textEventBuyJoinPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat((mEvent!!.rewardPlay!!* mCount).toString()))
        binding.textEventBuyJoinPoint.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat((mEvent!!.earnedPoint!!* mCount).toString()))
        var useBol = 0
        if (StringUtils.isNotEmpty(binding.editEventBuyUseBol.text.toString().trim())) {
            useBol = binding.editEventBuyUseBol.text.toString().trim().toInt()
        }

        val pgPrice = totalPrice - useBol
        binding.textEventBuyTotalPayPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(pgPrice.toString()))
        binding.textEventBuySavePoint.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyType(pgPrice.toString()))
        binding.textEventBuyPayPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((pgPrice.toString())))
        if (pgPrice >= 50000 && mPayMethod == "ftlink") {
            binding.layoutEventBuyInstallment.visibility = View.VISIBLE
        } else {
            binding.layoutEventBuyInstallment.visibility = View.GONE
        }

        if(pgPrice <= 0){
            mPayMethod = "point"
            binding.layoutEventBuyCard.visibility = View.GONE
        }else{
            binding.layoutEventBuyCard.visibility = View.VISIBLE
            if(binding.textEventBuyCard.isSelected){
                mPayMethod = "card"
            }else{
                mPayMethod = "ftlink"
            }
        }
    }

    private fun cardListCall() {
        showProgress("")
        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
            override fun onResponse(call: Call<NewResultResponse<Card>>?,
                                    response: NewResultResponse<Card>?) {
                hideProgress()
                if (response?.datas != null) {
                    if (response.datas!!.isNotEmpty()) {
                        mAdapter!!.mSelectData = response.datas!![0]
                    }
                    mAdapter!!.setDataList(response.datas!! as MutableList<Card>)
                    mAdapter!!.add(Card())
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Card>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getOrderId() {
        showProgress("")
        ApiBuilder.create().postOrderId().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {

                hideProgress()
                if (response?.data != null) {
                    if (mEventBuy.payMethod == "card") {
                        openPg(response.data!!)
                    } else {
                        mEventBuy.orderId = response.data
//                        insertEventBuy()
                        insertEventBuyList()
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
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

        var pgName = ""
        if(mEvent!!.primaryType == "randomluck"){
            pgName = getString(R.string.word_random_play)
        }else{
            pgName = getString(R.string.word_life_benefit)
        }

        builder.setBootUser(bootUser).setMethod(Method.CARD) // 결제수단
            .setPG(PG.DANAL).setContext(this) // 결제수단
            .setName(getString(R.string.word_play_join)) // 결제할 상품명
            .setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
        //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
        builder.addItem(pgName, 1, mEvent!!.no.toString(), mEventBuy.pgPrice!!) // 주문정보에 담길 상품정보, 통계를 위해 사용
        builder.setPrice(mEventBuy.pgPrice!!)

        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
            override fun onConfirm(message: String?) {
                mEventBuy.orderId = orderId

                Bootpay.confirm(message)
                LogUtil.e(LOG_TAG, "confirm : {}", message)
            }
        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
            override fun onDone(message: String?) {
                LogUtil.e(LOG_TAG, "done : {}", message)

                val receiptId = JsonParser.parseString(message).asJsonObject.get("receipt_id").asString
                mEventBuy.receiptId = receiptId
                insertEventBuyList()
//                insertEventBuy()

            }
        }).onReady(object : ReadyListener {
            override fun onReady(message: String?) {
                LogUtil.e(LOG_TAG, "ready : {}", message)
            }
        }).onCancel(object : CancelListener {
            override fun onCancel(message: String?) {
                LogUtil.e(LOG_TAG, "cancel : {}", message)
                showAlert(R.string.msg_cancel_pg)
                //                deleteBuy(buy.seqNo.toString())
            }
        }).onError(object : ErrorListener {
            override fun onError(message: String?) {
                LogUtil.e(LOG_TAG, "error : {}", message)
                showAlert(R.string.msg_error_pg)
                //                deleteBuy(buy.seqNo.toString())
            }
        }).onClose(object : CloseListener {
            override fun onClose(message: String?) {
                LogUtil.e(LOG_TAG, "close : {}", message)
            }
        }).request()
    }

    private fun insertEventBuyList() {
        showProgress("")
        ApiBuilder.create().eventBuyList(mEventBuy).setCallback(object : PplusCallback<NewResultResponse<EventResultJpa>> {
            override fun onResponse(call: Call<NewResultResponse<EventResultJpa>>?,
                                    response: NewResultResponse<EventResultJpa>?) {
                hideProgress()

                if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                    val list = response?.datas

                    if (list != null && list.isNotEmpty()){
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        val delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(supportFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            if(list.size == 1){
                                if (list[0].win != null) {
                                    val intent = Intent(this@EventBuyActivity, EventBuyResultActivity::class.java)
                                    intent.putExtra(Const.EVENT_WIN, list[0].win)
                                    intent.putExtra(Const.EVENT, mEvent)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    resultLauncher.launch(intent)
                                }else{
                                    val intent = Intent(this@EventBuyActivity, EventLoseAlertActivity::class.java)
                                    intent.putExtra(Const.EVENT_BUY, mEventBuy)
                                    intent.putExtra(Const.EVENT, mEvent)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    resultLauncher.launch(intent)
                                }
                            }else{
                                val intent = Intent(this@EventBuyActivity, AlertEventResultListActivity::class.java)
                                intent.putParcelableArrayListExtra(Const.DATA, list as ArrayList)
                                intent.putExtra(Const.EVENT, mEvent)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                resultLauncher.launch(intent)
                            }
                            
                        }, delayTime)
                    }
                } else {
                    complete()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResultJpa>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResultJpa>?) {
                hideProgress()
                if (response?.resultCode == 662) {
                    showAlert(R.string.msg_exceed_join_limit_count)
                } else {
                    showAlert(R.string.msg_fail_pay)
                }
            }
        }).build().call()
    }

    private fun insertEventBuy() {
        showProgress("")
        ApiBuilder.create().eventBuy(mEventBuy).setCallback(object : PplusCallback<NewResultResponse<EventWinJpa>> {
            override fun onResponse(call: Call<NewResultResponse<EventWinJpa>>?,
                                    response: NewResultResponse<EventWinJpa>?) {
                hideProgress()

                if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {

                    val mLoading = EventLoadingView()
                    mLoading.isCancelable = false
                    mLoading.setText(getString(R.string.msg_checking_event_result))
                    val delayTime = 2000L
                    mLoading.isCancelable = false
                    try {
                        mLoading.show(supportFragmentManager, "")
                    } catch (e: Exception) {

                    }

                    val handler = Handler()
                    handler.postDelayed(Runnable {

                        try {
                            mLoading.dismiss()
                        } catch (e: Exception) {

                        }


                        if (response?.data != null) {
                            val intent = Intent(this@EventBuyActivity, EventBuyResultActivity::class.java)
                            intent.putExtra(Const.EVENT_WIN, response.data)
                            intent.putExtra(Const.EVENT, mEvent)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }else{
                            val intent = Intent(this@EventBuyActivity, EventLoseAlertActivity::class.java)
                            intent.putExtra(Const.EVENT_BUY, mEventBuy)
                            intent.putExtra(Const.EVENT, mEvent)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }

                    }, delayTime)
                } else {
                    complete()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventWinJpa>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventWinJpa>?) {
                hideProgress()
                if (response?.resultCode == 662) {
                    showAlert(R.string.msg_exceed_join_limit_count)
                } else {
                    showAlert(R.string.msg_fail_pay)
                }
            }
        }).build().call()
    }

    private fun setRetentionBol() {

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textEventBuyRetentionBol.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user!!.totalBol.toString()))
            }
        })
    }

    private fun complete() {
        setEvent("randomluckJoin")
        val intent = Intent(this, EventBuyCompleteActivity::class.java)
        intent.putExtra(Const.EVENT, mEvent)
        intent.putExtra(Const.EVENT_BUY, mEventBuy)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        resultLauncher.launch(intent)
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }

    val checkPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            getOrderId()
        }
    }

    val regLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            if (LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!) {
                val intent = Intent(this, PayPasswordSetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                changePasswordLauncher.launch(intent)
            }
            cardListCall()
        }
    }

    val changePasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
            }
        })
    }

    private fun getGiftAll() {
        val params = java.util.HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
                                    response: NewResultResponse<EventGift>?) {
                if (isFinishing) {
                    return
                }

                if (response?.datas != null && response.datas!!.isNotEmpty()) {
                    val eventGift = response.datas!![0]
                    Glide.with(this@EventBuyActivity).load(eventGift.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageEventBuyGift)
                    binding.textEventBuyGiftName.text = eventGift.title
                    binding.textEventBuyGiftPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_normal_price, FormatUtil.getMoneyType(eventGift.price.toString())))
                    binding.textEventBuyGiftPrice.paintFlags = binding.textEventBuyGiftPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    if (StringUtils.isNotEmpty(eventGift.giftLink)) {
                        binding.textEventBuyGiftUrl.visibility = View.VISIBLE
                        binding.textEventBuyGiftUrl.setOnClickListener {
                            PplusCommonUtil.openChromeWebView(this@EventBuyActivity, eventGift.giftLink!!)
                        }
                    } else {
                        binding.textEventBuyGiftUrl.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventGift>?) {
            }
        }).build().call()
    }

    private fun getMyJoinCount() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getMyJoinCountAndBuyType(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.data != null) {
                    mMyJoinCount = response.data!!
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_event_buy_title), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}