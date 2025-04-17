//package com.pplus.prnumberbiz.apps.push.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Typeface
//import android.os.Bundle
//import android.util.TypedValue
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.TextView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil
//import com.pple.pplus.utils.part.format.FormatUtil
//import com.pple.pplus.utils.part.pref.PreferenceUtil
//import com.pple.pplus.utils.part.utils.StringUtils
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.ads.ui.SelectAdvertiseActivity
//import com.pplus.prnumberbiz.apps.billing.ui.CashBillingActivity
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
//import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberbiz.apps.coupon.data.CouponEnum
//import com.pplus.prnumberbiz.apps.customer.ui.SelectPlusActivity
//import com.pplus.prnumberbiz.core.code.common.EnumData
//import com.pplus.prnumberbiz.core.code.common.MoveType1Code
//import com.pplus.prnumberbiz.core.code.common.MoveType2Code
//import com.pplus.prnumberbiz.core.network.ApiBuilder
//import com.pplus.prnumberbiz.core.network.model.dto.*
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_push_advertise_send.*
//import kotlinx.android.synthetic.main.item_coupon2.*
//import kotlinx.android.synthetic.main.item_select_post.*
//import network.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.ArrayList
//
//class PushAdvertiseSendActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_push_advertise_send
//    }
//
//    var mPushPrice: Int? = null
//    var mAdvertise: Advertise? = null
//    private var mUserList: ArrayList<Fan>? = null
//    var mSendType = EnumData.SendType.reservation
//    var mAdsType: EnumData.AdsType? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        if (!PreferenceUtil.getDefaultPreference(this).getBoolean(Const.GUIDE_PUSH)) {
//            val intent = Intent(this, PushGuideActivity::class.java)
//            intent.putExtra(Const.KEY, Const.GUIDE_PUSH)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        mPushPrice = CountryConfigManager.getInstance().config.properties.pushPrice
//
//        mAdvertise = intent.getParcelableExtra<Advertise>(Const.ADVERTISE);
//        mAdsType = intent.getSerializableExtra(Const.TYPE) as EnumData.AdsType?
//
//        layout_push_ads_caution.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_caution2))
//            builder.addContents(AlertData.MessageData(getString(R.string.format_push_send_caution1, mPushPrice), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//            builder.addContents(AlertData.MessageData(getString(R.string.msg_push_caution2), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//            builder.setLeftText(getString(R.string.word_confirm))
//            builder.builder().show(this)
//        }
//
//        text_push_ads_receiver.setOnClickListener {
//            var intent = Intent(this, SelectPlusActivity::class.java)
//            if (mUserList != null && mUserList!!.size > 0) {
//                intent.putParcelableArrayListExtra(Const.CUSTOMER, mUserList)
//            }
//            intent.putExtra(Const.USER, EnumData.CustomerType.plus)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SEARCH)
//        }
//
//        layout_push_ads_none_select.setOnClickListener {
//            //광고 선택
//            intent = Intent(this, SelectAdvertiseActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CONTENTS)
//        }
//
//        text_push_ads_immediately.setOnClickListener {
//            if (mSendType == EnumData.SendType.reservation) {
//                setSelect(text_push_ads_immediately, text_push_ads_reservation)
//                text_push_ads_send.setText(R.string.word_push_send)
//                mSendType = EnumData.SendType.immediately
////                mIsEditing = true
//            }
//        }
//
//        text_push_ads_reservation.setOnClickListener {
//            if (mSendType == EnumData.SendType.immediately) {
//                mSendType = EnumData.SendType.reservation
//                setSelect(text_push_ads_reservation, text_push_ads_immediately)
//                text_push_ads_send.setText(R.string.word_next)
////                mIsEditing = true
//            }
//        }
//
//        text_push_ads_charge_cash.setOnClickListener {
//            var intent = Intent(this, CashBillingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        text_push_ads_send.setOnClickListener {
//            val msg = Msg()
//
//            msg.type = "push"
//            msg.isReserved = false
//            if (mAdvertise == null) {
//                showAlert(R.string.msg_select_advertise)
//                return@setOnClickListener
//            }
//
//            when (mAdsType) {
//                EnumData.AdsType.article -> {
//                    msg.subject = LoginInfoManager.getInstance().user.page!!.name!!
//                    msg.contents = mAdvertise!!.article?.contents
//                    msg.moveType1 = MoveType1Code.inner.name
//                    msg.moveType2 = MoveType2Code.adPost.name
//                    msg.moveTarget = No(mAdvertise!!.no)
//                }
//                EnumData.AdsType.coupon -> {
//                    msg.subject = LoginInfoManager.getInstance().user.page!!.name!!
//                    msg.contents = mAdvertise!!.template?.name
//                    msg.moveType1 = MoveType1Code.inner.name
//                    msg.moveType2 = MoveType2Code.adCoupon.name
//                    msg.moveTarget = No(mAdvertise!!.no)
//                }
//            }
//
//            if (mUserList == null || mUserList!!.size == 0) {
//                showAlert(R.string.msg_select_customer)
//                return@setOnClickListener
//            }
//
//            val useCash = mPushPrice!! * mUserList!!.size
//            val retentionCash = LoginInfoManager.getInstance().user.totalCash
//            if (useCash > retentionCash) {
//                showAlert(R.string.msg_charge_cash)
//                return@setOnClickListener
//            }
//
//            msg.totalPrice = useCash
//
//            val targetList = ArrayList<MsgTarget>()
//            for (fan in mUserList!!) {
//                val target = MsgTarget()
//                target.user = No(fan.getNo())
//                target.name = fan.getNickname()
//                targetList.add(target)
//            }
//
//            msg.targetList = targetList
//
//            when (mSendType) {
//
//                EnumData.SendType.reservation -> {
//                    msg.isReserved = true
//                    intent = Intent(this, PushReservationActivity::class.java)
//                    intent.putExtra(Const.KEY, EnumData.MsgType.push)
//                    intent.putExtra(Const.DATA, msg)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_RESERVATION)
//                }
//                EnumData.SendType.immediately -> sendPush(msg)
//            }
//        }
//
//        mSendType = EnumData.SendType.immediately
//        setSelect(text_push_ads_immediately, text_push_ads_reservation)
//        text_push_ads_send.setText(R.string.word_next)
//
//        if(mAdvertise != null && mAdsType != null){
//            setData()
//        }
//
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener{
//            override fun reload() {
//                text_push_ads_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
//            }
//        })
//    }
//
//    private fun sendPush(msg: Msg) {
//
//        val builder = AlertBuilder.Builder()
//        builder.setTitle(getString(R.string.word_notice_alert))
//        builder.addContents(AlertData.MessageData(getString(R.string.msg_push_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//        builder.addContents(AlertData.MessageData(getString(R.string.format_push_send_alert2, mUserList!!.size), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_push_send))
//        builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//            override fun onCancel() {
//
//            }
//
//            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                when (event_alert) {
//                    AlertBuilder.EVENT_ALERT.RIGHT -> {
//                        showProgress("")
//                        ApiBuilder.create().insertPushMsg(msg).setCallback(object : PplusCallback<NewResultResponse<Msg>> {
//
//                            override fun onResponse(call: Call<NewResultResponse<Msg>>, response: NewResultResponse<Msg>) {
//
//                                hideProgress()
//                                setResult(Activity.RESULT_OK)
//                                val builder = AlertBuilder.Builder()
//                                builder.setTitle(getString(R.string.word_notice_alert))
//                                if (msg.isReserved) {
//                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_reserved_push), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                } else {
//                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_send_push), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                }
//                                builder.addContents(AlertData.MessageData(getString(R.string.msg_success_send), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                                builder.setLeftText(getString(R.string.word_confirm))
//                                builder.setAutoCancel(false)
//                                builder.setBackgroundClickable(false)
//                                builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                                    override fun onCancel() {
//
//                                    }
//
//                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                                        when (event_alert) {
//                                            AlertBuilder.EVENT_ALERT.SINGLE -> PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener{
//                                                override fun reload() {
//                                                    resetData()
//                                                }
//                                            })
//                                        }
//                                    }
//                                }).builder().show(this@PushAdvertiseSendActivity)
//
//
//                            }
//
//                            override fun onFailure(call: Call<NewResultResponse<Msg>>, t: Throwable, response: NewResultResponse<Msg>) {
//
//                                hideProgress()
//                            }
//                        }).build().call()
//                    }
//                }
//            }
//        }).builder().show(this)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_CONTENTS -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        mAdvertise = data.getParcelableExtra<Advertise>(Const.ADVERTISE);
//                        mAdsType = data.getSerializableExtra(Const.TYPE) as EnumData.AdsType
//                        setData()
//                    }
//                }
//            }
//            Const.REQ_SEARCH -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        mUserList = data.getParcelableArrayListExtra(Const.CUSTOMER)
//                        if (mUserList != null && mUserList!!.size > 0) {
//                            if (mUserList!!.size == 1) {
//                                text_push_ads_receiver.setText("" + mUserList!!.get(0).nickname)
//                            } else {
//                                text_push_ads_receiver.text = getString(R.string.format_other, "" + mUserList!!.get(0).nickname, mUserList!!.size - 1)
//                            }
//
//                            text_push_ads_pre_use_cash.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mPushPrice!! * mUserList!!.size).toString()))
//                        }
//
//                    }
//                }
//            }
//            Const.REQ_RESERVATION -> {
//                resetData()
//            }
//            Const.REQ_CASH_CHANGE -> {
//
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener{
//                    override fun reload() {
//                        text_push_ads_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
//                    }
//                })
//            }
//        }
//    }
//
//    fun setData(){
//        layout_push_ads_none_select.visibility = View.GONE
//        when (mAdsType) {
//            EnumData.AdsType.article -> {
//                layout_push_ads_post.visibility = View.VISIBLE
//                layout_push_ads_coupon.visibility = View.GONE
//                setPost()
//            }
//            EnumData.AdsType.coupon -> {
//                layout_push_ads_post.visibility = View.GONE
//                layout_push_ads_coupon.visibility = View.VISIBLE
//                setCoupon()
//            }
//        }
//    }
//
//    fun setPost(){
//        Glide.with(this).load(mAdvertise!!.article?.attachList?.get(0)?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_select_post)
//
//        text_select_post_contents.text = mAdvertise!!.article?.contents
//    }
//
//    fun setCoupon(){
//        val mEvents = resources.getStringArray(R.array.coupon_event_type)
//        text_coupon_title.text = mAdvertise!!.template?.name
//
//        val eventType: String
//
//        if (StringUtils.isEmpty(mAdvertise!!.template?.type)) {
//            eventType = mEvents[0]
//        } else {
//            eventType = mEvents[EnumData.CouponType.valueOf(mAdvertise!!.template?.type!!).ordinal]
//        }
//        text_coupon_type.text = eventType
//        when (EnumData.CouponType.valueOf(mAdvertise!!.template?.type!!)) {
//
//            EnumData.CouponType.discount -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//            }
//            EnumData.CouponType.dayOfWeek -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_fbc000))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_fbc000))
//            }
//            EnumData.CouponType.visit -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_fbc000))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_fbc000))
//            }
//            EnumData.CouponType.consult -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_1cbeb1))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_1cbeb1))
//            }
//            EnumData.CouponType.expert -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_af3dff))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_af3dff))
//            }
//            EnumData.CouponType.time -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//
//            }
//            EnumData.CouponType.etc -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_6d6d6d))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_6d6d6d))
//            }
//            else -> {
//                text_coupon_type.setTextColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//                layout_coupon_down.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_ff4141))
//            }
//        }
//
//        if (mAdvertise!!.template?.discountType == CouponEnum.DiscountType.cost.name) {
//            text_coupon_discount_unit.text = getString(R.string.word_money_unit)
//            text_coupon_discount.text = FormatUtil.getMoneyType(mAdvertise!!.template?.discount)
//
//            layout_coupon_discount.visibility = View.VISIBLE
//            text_coupon_discount.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimensionPixelSize(R.dimen.textSize_100pt).toFloat());
//            text_coupon_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimensionPixelSize(R.dimen.textSize_40pt).toFloat());
//
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).topMargin = resources!!.getDimensionPixelSize(R.dimen.height_70)
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).bottomMargin = resources!!.getDimensionPixelSize(R.dimen.height_70)
//        } else if (mAdvertise!!.template?.discountType == CouponEnum.DiscountType.percent.name) {
//            text_coupon_discount_unit.text = "%"
//            text_coupon_discount.text = FormatUtil.getMoneyType(mAdvertise!!.template?.discount)
//
//            layout_coupon_discount.visibility = View.VISIBLE
//            text_coupon_discount.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimensionPixelSize(R.dimen.textSize_100pt).toFloat());
//            text_coupon_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimensionPixelSize(R.dimen.textSize_40pt).toFloat());
//
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).topMargin = resources!!.getDimensionPixelSize(R.dimen.height_70)
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).bottomMargin = resources!!.getDimensionPixelSize(R.dimen.height_70)
//        } else {
//            layout_coupon_discount.visibility = View.GONE
//            text_coupon_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources!!.getDimensionPixelSize(R.dimen.textSize_54pt).toFloat());
//
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).topMargin = resources!!.getDimensionPixelSize(R.dimen.height_100)
//            (layout_coupon_info.layoutParams as LinearLayout.LayoutParams).bottomMargin = resources!!.getDimensionPixelSize(R.dimen.height_100)
//        }
//
//        try {
//            val d1 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mAdvertise!!.template?.duration!!.start)
//            val d2 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mAdvertise!!.template?.duration!!.end)
//
//            val output = SimpleDateFormat("yyyy.MM.dd")
//
//            text_coupon_duration.text = output.format(d1) + "~" + output.format(d2)
//
//        } catch (e: Exception) {
//
//        }
//    }
//
//    private fun resetData() {
//
////        mIsEditing = false
//        mUserList = null
//        mAdvertise = null
//        layout_push_ads_none_select.visibility = View.VISIBLE
//        layout_push_ads_post.visibility = View.GONE
//        layout_push_ads_coupon.visibility = View.GONE
//        mSendType = EnumData.SendType.immediately
//        setSelect(text_push_ads_immediately, text_push_ads_reservation)
//        text_push_ads_send.setText(R.string.word_push_send)
//        text_push_ads_pre_use_cash.text = ""
//        text_push_ads_receiver.text = ""
//        text_push_ads_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
//    }
//
//    fun setSelect(view1: View, view2: View) {
//        view1.isSelected = true
//        view2.isSelected = false
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_send_ads_push), ToolbarOption.ToolbarMenu.LEFT)
//
//        val view = layoutInflater.inflate(R.layout.item_top_right, null)
//        val textView = view.findViewById<View>(R.id.text_top_right) as TextView
//        textView.setText(R.string.word_send_result)
//        textView.setTypeface(textView.typeface, Typeface.BOLD)
//        textView.setTextColor(ResourceUtil.getColor(this, R.color.white))
//        textView.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_8700ff))
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0)
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
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    val intent = Intent(this, PushResultActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//            }
//        }
//    }
//}
