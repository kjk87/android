//package com.pplus.luckybol.apps.point.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Paint
//import android.os.Bundle
//import android.view.View
//import android.widget.RelativeLayout
//import androidx.core.widget.NestedScrollView
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.google.android.material.appbar.AppBarLayout
//import com.google.gson.JsonParser
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.card.data.CardSelectAdapter
//import com.pplus.luckybol.apps.card.ui.CardRegActivity
//import com.pplus.luckybol.apps.card.ui.PayPasswordCheckActivity
//import com.pplus.luckybol.apps.card.ui.PayPasswordSetActivity
//import com.pplus.luckybol.apps.common.builder.AlertBuilder
//import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
//import com.pplus.luckybol.apps.common.builder.data.AlertData
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
//import com.pplus.luckybol.core.code.common.EventType
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.*
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.core.util.ToastUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_point_buy_with_event.*
//import kr.co.bootpay.Bootpay
//import kr.co.bootpay.BootpayAnalytics
//import kr.co.bootpay.enums.Method
//import kr.co.bootpay.enums.PG
//import kr.co.bootpay.listener.*
//import kr.co.bootpay.model.BootExtra
//import kr.co.bootpay.model.BootUser
//import retrofit2.Call
//
//class PointBuyWithEventActivity : BaseActivity() {
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_point_buy_with_event
//    }
//
//    override fun getPID(): String? {
//        return ""
//    }
//
//    var mCount = 1
//    var mMyJoinCount = 0
//    var mPayMethod = "card"
//    var mPointBuy = PointBuy()
//    var mEvent: Event? = null
//    var mInstallment = "00"
//    private var mAdapter: CardSelectAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mEvent = intent.getParcelableExtra(Const.EVENT)
//
//
//        image_point_buy_with_event_back.setOnClickListener {
//            onBackPressed()
//        }
//
//        layout_point_buy_with_event_count_minus.setOnClickListener {
//            if (mCount > 1) {
//                mCount--
//                text_point_buy_with_event_count.text = mCount.toString()
//                setTotalCount()
//            }
//        }
//
//        app_bar_point_buy_with_event.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                LogUtil.e(LOG_TAG, "verticalOffset : "+verticalOffset)
//                image_point_buy_with_event_point_popup.visibility = View.GONE
//            }
//        })
//
//        scroll_point_buy_with_event.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
//            override fun onScrollChange(v: NestedScrollView?,
//                                        scrollX: Int,
//                                        scrollY: Int,
//                                        oldScrollX: Int,
//                                        oldScrollY: Int) {
//                image_point_buy_with_event_point_popup.visibility = View.GONE
//                LogUtil.e(LOG_TAG, "scroll : "+scrollY)
//            }
//        })
//
//        layout_point_buy_with_event_count_plus.setOnClickListener {
//
//            val possibleCount = mEvent!!.maxJoinCount!! - mEvent!!.joinCount!!
//
//            if (mCount >= possibleCount) {
//                return@setOnClickListener
//            }
//
//            if (mEvent!!.buyLimitCount != null && mEvent!!.buyLimitCount!! > 0) {
//                val myPossibleCount = mEvent!!.buyLimitCount!! - mMyJoinCount
//                if (mCount >= myPossibleCount) {
//                    ToastUtil.show(this, R.string.msg_exceed_join_limit_count)
//                    return@setOnClickListener
//                }
//            }
//
//            mCount++
//            text_point_buy_with_event_count.text = mCount.toString()
//            setTotalCount()
//        }
//
//        mCount = 1
//        text_point_buy_with_event_count.text = mCount.toString()
//        setTotalCount()
//
//        val contents = resources.getStringArray(R.array.report_installment_period)
//        text_point_buy_with_event_installment.setOnClickListener {
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
//                            text_point_buy_with_event_installment.text = contents[event_alert.value - 1]
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
//        text_point_buy_with_event_installment.text = contents[0]
//
//        layout_point_buy_with_event_caution_title.setOnClickListener {
//            if (layout_point_buy_with_event_caution.visibility == View.VISIBLE) {
//                image_point_buy_with_event_arrow.setImageResource(R.drawable.ic_arrow_down)
//                layout_point_buy_with_event_caution.visibility = View.GONE
//            } else {
//                image_point_buy_with_event_arrow.setImageResource(R.drawable.ic_arrow_up)
//                layout_point_buy_with_event_caution.visibility = View.VISIBLE
//            }
//        }
//
//        text_point_buy_with_event_point_guide.setOnClickListener {
//
//            text_point_buy_with_event_point_guide.post {
//                val location = IntArray(2)
//                text_point_buy_with_event_point_guide.getLocationOnScreen(location)
//                val x = location[0] + text_point_buy_with_event_point_guide.width / 2 - resources.getDimensionPixelSize(R.dimen.width_256)
//                val y = location[1] - text_point_buy_with_event_point_guide.height - resources.getDimensionPixelSize(R.dimen.height_254)
//                (image_point_buy_with_event_point_popup.layoutParams as RelativeLayout.LayoutParams).marginStart = x
//                (image_point_buy_with_event_point_popup.layoutParams as RelativeLayout.LayoutParams).topMargin = y
//                image_point_buy_with_event_point_popup.requestLayout()
//            }
//
//            if (image_point_buy_with_event_point_popup.visibility == View.VISIBLE) {
//                image_point_buy_with_event_point_popup.visibility = View.GONE
//            } else {
//                image_point_buy_with_event_point_popup.post {
//                    image_point_buy_with_event_point_popup.visibility = View.VISIBLE
//                }
//            }
//        }
//
//        text_point_buy_with_event_card.setOnClickListener {
//            text_point_buy_with_event_card.isSelected = true
//            text_point_buy_with_event_easy_pay.isSelected = false
//            mPayMethod = "card"
//            layout_point_buy_with_event_easy_pay.visibility = View.GONE
//        }
//
//        text_point_buy_with_event_easy_pay.setOnClickListener {
//            text_point_buy_with_event_card.isSelected = false
//            text_point_buy_with_event_easy_pay.isSelected = true
//            mPayMethod = "ftlink"
//            layout_point_buy_with_event_easy_pay.visibility = View.VISIBLE
//        }
//
//        mAdapter = CardSelectAdapter()
//        recycler_point_buy_with_event_easy_pay_card.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recycler_point_buy_with_event_easy_pay_card.adapter = mAdapter
//        cardListCall()
//
//        text_point_buy_with_event_card.isSelected = true
//        text_point_buy_with_event_easy_pay.isSelected = false
//        mPayMethod = "card"
//
//        text_point_buy_with_event_pay.setOnClickListener {
//            val possibleCount = mEvent!!.maxJoinCount!! - mEvent!!.joinCount!!
//
//            if (mCount > possibleCount) {
//                showAlert(R.string.msg_over_joinable_count)
//                return@setOnClickListener
//            }
//
//            if (mEvent!!.buyLimitCount != null && mEvent!!.buyLimitCount!! > 0) {
//                val myPossibleCount = mEvent!!.buyLimitCount!! - mMyJoinCount
//                LogUtil.e(LOG_TAG, "myPossibleCount : {}", myPossibleCount)
//                if (mCount > myPossibleCount) {
//                    showAlert(R.string.msg_over_joinable_count)
//                    return@setOnClickListener
//                }
//            }
//
//            mPointBuy = PointBuy()
//            mPointBuy.payMethod = mPayMethod
//            mPointBuy.memberSeqNo = LoginInfoManager.getInstance().user.no
//            mPointBuy.cash = mEvent!!.rewardPlay!! * mCount
//            mPointBuy.eventSeqNo = mEvent!!.no
//            mPointBuy.count = mCount
//
//            if (mPayMethod == "card") {
//                if (Const.API_URL.startsWith("https://api")) {
//                    BootpayAnalytics.init(this, getString(R.string.boot_pay_cash_id))
//                } else {
//                    BootpayAnalytics.init(this, getString(R.string.boot_pay_id_stage))
//                }
//
//                mPointBuy.pg = "danal"
//                getOrderId()
//            } else {
//
//                if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(getString(R.string.word_notice_alert))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                    builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//                        override fun onCancel() {}
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                            when (event_alert) {
//                                AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                    val intent = Intent(this@PointBuyWithEventActivity, VerificationMeActivity::class.java)
//                                    intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivityForResult(intent, Const.REQ_VERIFICATION)
//                                }
//                            }
//                        }
//                    }).builder().show(this)
//                    return@setOnClickListener
//                }
//
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
//                                    val intent = Intent(this@PointBuyWithEventActivity, CardRegActivity::class.java)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    startActivityForResult(intent, Const.REQ_REG)
//                                }
//                            }
//
//                        }
//                    }).builder().show(this)
//                } else {
//
//                    mPointBuy.installment = mInstallment
//                    mPointBuy.cardCode = mAdapter!!.mSelectData!!.cardCode
//                    mPointBuy.autoKey = mAdapter!!.mSelectData!!.autoKey
//                    mPointBuy.pg = "dau"
//
//                    val intent = Intent(this, PayPasswordCheckActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_CHECK_PASSWORD)
//                }
//            }
//        }
//
//        getEvent()
//    }
//
//    private fun getEvent() {
//        val params = java.util.HashMap<String, String>()
//        params["no"] = mEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?,
//                                    response: NewResultResponse<Event>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mEvent = response.data
//
//                    if (mEvent!!.primaryType == EventType.PrimaryType.randomluck.name) {
//                        text_point_buy_with_event_gift_name.visibility = View.GONE
//                        text_point_buy_with_event_gift_price.visibility = View.GONE
//                        Glide.with(this@PointBuyWithEventActivity).load(mEvent!!.totalGiftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(image_point_buy_with_event_gift)
//                        text_point_buy_with_event_gift_url.visibility = View.GONE
//                    } else {
//                        text_point_buy_with_event_gift_name.visibility = View.VISIBLE
//                        text_point_buy_with_event_gift_price.visibility = View.VISIBLE
//                        text_point_buy_with_event_gift_url.visibility = View.VISIBLE
//                        getGiftAll()
//                    }
//
//                    text_point_buy_with_event_join_price?.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_price, FormatUtil.getMoneyType(mEvent!!.rewardPlay.toString())))
//                    text_point_buy_with_event_join_point?.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_point, FormatUtil.getMoneyType(mEvent!!.rewardPlay.toString())))
//
//                    getMyJoinCount()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Event>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun setTotalCount() {
//        val totalPrice = mEvent!!.rewardPlay!! * mCount
//
//        text_point_buy_with_event_pay.text = getString(R.string.format_pay, FormatUtil.getMoneyType(totalPrice.toString()))
//        text_point_buy_with_event_save_point.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(totalPrice.toString()))
//        text_point_buy_with_event_pay_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((totalPrice.toString())))
//        if (totalPrice >= 50000 && mPayMethod == "ftlink") {
//            layout_point_buy_with_event_installment.visibility = View.VISIBLE
//        } else {
//            layout_point_buy_with_event_installment.visibility = View.GONE
//        }
//    }
//
//    private fun cardListCall() {
//        showProgress("")
//        ApiBuilder.create().cardList.setCallback(object : PplusCallback<NewResultResponse<Card>> {
//            override fun onResponse(call: Call<NewResultResponse<Card>>?,
//                                    response: NewResultResponse<Card>?) {
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
//            override fun onFailure(call: Call<NewResultResponse<Card>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Card>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getOrderId() {
//        ApiBuilder.create().postOrderId().setCallback(object : PplusCallback<NewResultResponse<String>> {
//            override fun onResponse(call: Call<NewResultResponse<String>>?,
//                                    response: NewResultResponse<String>?) {
//
//                hideProgress()
//                if (response?.data != null) {
//                    if (mPointBuy.payMethod == "card") {
//                        openPg(response.data)
//                    } else {
//                        mPointBuy.orderId = response.data
//                        insertPointBuy()
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<String>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<String>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun openPg(orderId: String) {
//        val builder = Bootpay.init(this)
//        if (Const.API_URL.startsWith("https://api")) {
//            builder.setApplicationId(getString(R.string.boot_pay_cash_id)) // 해당 프로젝트(안드로이드)의 application id 값
//        } else {
//            builder.setApplicationId(getString(R.string.boot_pay_id_stage)) // 해당 프로젝트(안드로이드)의 application id 값
//        }
//
//        val bootUser = BootUser()
//        bootUser.username = LoginInfoManager.getInstance().user.name
//        bootUser.phone = LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "")
//        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
//
//        builder.setBootUser(bootUser)
//            .setMethod(Method.CARD) // 결제수단
//            .setPG(PG.DANAL)
//            .setContext(this) // 결제수단
//            .setName(getString(R.string.word_point)) // 결제할 상품명
//            .setOrderId(orderId) //고유 주문번호로, 생성하신 값을 보내주셔야 합니다.
//        //.setAccountExpireAt("2018-09-22") // 가상계좌 입금기간 제한 ( yyyy-mm-dd 포멧으로 입력해주세요. 가상계좌만 적용됩니다. 오늘 날짜보다 더 뒤(미래)여야 합니다 )
//        builder.addItem(getString(R.string.word_point), 1, "point_${mPointBuy.cash}", mPointBuy.cash!!) // 주문정보에 담길 상품정보, 통계를 위해 사용
//        builder.setPrice(mPointBuy.cash!!.toInt())
//
//        builder.onConfirm(object : ConfirmListener { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
//            override fun onConfirm(message: String?) {
//                mPointBuy.orderId = orderId
//
//                Bootpay.confirm(message)
//                LogUtil.e(LOG_TAG, "confirm : {}", message)
//            }
//        }).onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
//            override fun onDone(message: String?) {
//                LogUtil.e(LOG_TAG, "done : {}", message)
//
//                val receiptId = JsonParser.parseString(message).asJsonObject.get("receipt_id").asString
//                mPointBuy.receiptId = receiptId
//                insertPointBuy()
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
//                //                deleteBuy(buy.seqNo.toString())
//            }
//        }).onError(object : ErrorListener {
//            override fun onError(message: String?) {
//                LogUtil.e(LOG_TAG, "error : {}", message)
//                showAlert(R.string.msg_error_pg)
//                //                deleteBuy(buy.seqNo.toString())
//            }
//        }).onClose(object : CloseListener {
//            override fun onClose(message: String?) {
//                LogUtil.e(LOG_TAG, "close : {}", message)
//            }
//        }).request()
//    }
//
//    private fun insertPointBuy() {
//        showProgress("")
//        ApiBuilder.create().insertPointBuy(mPointBuy).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?,
//                                    response: NewResultResponse<Any>?) {
//                hideProgress()
//                complete()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Any>?) {
//                hideProgress()
//                if (response?.resultCode == 662) {
//                    showAlert(R.string.msg_exceed_join_limit_count)
//                } else {
//                    showAlert(R.string.msg_fail_pay)
//                }
//            }
//        }).build().call()
//
//    }
//
//    private fun complete() {
//        val intent = Intent(this, PointBuyWithEventCompleteActivity::class.java)
//        intent.putExtra(Const.EVENT, mEvent)
//        intent.putExtra(Const.POINT_BUY, mPointBuy)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivityForResult(intent, Const.REQ_COMPLETE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_COMPLETE -> {
//                setResult(RESULT_OK)
//                finish()
//            }
//            Const.REQ_CHECK_PASSWORD -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    getOrderId()
//                }
//            }
//            Const.REQ_REG -> {
//                if (resultCode == Activity.RESULT_OK) {
//
//                    if (LoginInfoManager.getInstance().user.setPayPassword == null || !LoginInfoManager.getInstance().user.setPayPassword!!) {
//                        val intent = Intent(this, PayPasswordSetActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_CHANGE_PASSWORD)
//                    }
//                    cardListCall()
//                }
//            }
//            Const.REQ_CHANGE_PASSWORD -> {
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                    override fun reload() {
//                    }
//                })
//            }
//            Const.REQ_VERIFICATION -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        val verifiedData = data.getParcelableExtra<User>(Const.DATA)
//                        if (LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "") != verifiedData.mobile) {
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
//                                override fun onResponse(call: Call<NewResultResponse<User>>,
//                                                        response: NewResultResponse<User>) {
//                                    hideProgress()
//                                    showProgress("")
//                                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                                        override fun reload() {
//                                            hideProgress()
//                                            showAlert(R.string.msg_verified)
//                                        }
//                                    })
//                                }
//
//                                override fun onFailure(call: Call<NewResultResponse<User>>,
//                                                       t: Throwable,
//                                                       response: NewResultResponse<User>) {
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
//    private fun getGiftAll() {
//        val params = java.util.HashMap<String, String>()
//        params["no"] = mEvent!!.no.toString()
//        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
//            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
//                                    response: NewResultResponse<EventGift>?) {
//                if (isFinishing) {
//                    return
//                }
//
//                if (response?.datas != null && response.datas.isNotEmpty()) {
//                    val eventGift = response.datas[0]
//                    Glide.with(this@PointBuyWithEventActivity).load(eventGift.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(image_point_buy_with_event_gift)
//                    text_point_buy_with_event_gift_name?.text = eventGift.title
//                    text_point_buy_with_event_gift_price?.text = PplusCommonUtil.fromHtml(getString(R.string.html_normal_price, FormatUtil.getMoneyType(eventGift.price.toString())))
//                    text_point_buy_with_event_gift_price?.paintFlags = text_point_buy_with_event_gift_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//
//                    if (StringUtils.isNotEmpty(eventGift.giftLink)) {
//                        text_point_buy_with_event_gift_url.visibility = View.VISIBLE
//                        text_point_buy_with_event_gift_url.setOnClickListener {
//                            PplusCommonUtil.openChromeWebView(this@PointBuyWithEventActivity, eventGift.giftLink!!)
//                        }
//                    } else {
//                        text_point_buy_with_event_gift_url.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<EventGift>?) {
//            }
//        }).build().call()
//    }
//
//    private fun getMyJoinCount() {
//        val params = HashMap<String, String>()
//        params["no"] = mEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getMyJoinCountAndBuyType(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                    response: NewResultResponse<Int>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mMyJoinCount = response.data
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Int>?) {
//                hideProgress()
//            }
//        }).build().call()
//
//    }
//}