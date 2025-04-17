package com.pplus.prnumberbiz.apps.push.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.NightAlertActivity
import com.pplus.prnumberbiz.apps.cash.ui.CashConfigActivity2
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.coupon.ui.SelectCouponActivity
import com.pplus.prnumberbiz.apps.customer.ui.SelectPlusActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.MoveType1Code
import com.pplus.prnumberbiz.core.code.common.MoveType2Code
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_push_send.*
import kotlinx.android.synthetic.main.item_coupon_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class PushSendActivity : BaseActivity() {
    override fun getPID(): String {
        return "Main_menu_push"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_push_send
    }

    var mPushPrice: Int? = null
    var mCoupon: Goods? = null
    private var mUserList: ArrayList<Fan>? = null
    var mSendType = EnumData.SendType.reservation
    var mIsEditing = false

    override fun initializeView(savedInstanceState: Bundle?) {

        if (!PreferenceUtil.getDefaultPreference(this).getBoolean(Const.NIGHT_ADS)) {
            val intent = Intent(this, NightAlertActivity::class.java)
            startActivity(intent)
        }

        text_push_title.setOnClickListener {
            onBackPressed()
        }

        text_push_result.setOnClickListener {
            val intent = Intent(this, PushResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        mPushPrice = CountryConfigManager.getInstance().config.properties.pushPrice
        text_push_cost.text = getString(R.string.format_push_cost_description, mPushPrice.toString())

        text_push_receiver.setOnClickListener {
            val intent = Intent(this, SelectPlusActivity::class.java)
            if (mUserList != null && mUserList!!.size > 0) {
                intent.putParcelableArrayListExtra(Const.CUSTOMER, mUserList)
            }
            intent.putExtra(Const.USER, EnumData.CustomerType.plus)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SEARCH)
        }

        layout_push_post_select.setOnClickListener {
            //광고 선택
            intent = Intent(this, SelectCouponActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_CONTENTS)
        }

        text_push_immediately.setOnClickListener {
            if (mSendType == EnumData.SendType.reservation) {
                setSelect(text_push_immediately, text_push_reservation)
                text_push_send.setText(R.string.msg_push_send)
                mSendType = EnumData.SendType.immediately
                mIsEditing = true
            }
        }

        text_push_reservation.setOnClickListener {
            if (mSendType == EnumData.SendType.immediately) {
                mSendType = EnumData.SendType.reservation
                setSelect(text_push_reservation, text_push_immediately)
                text_push_send.setText(R.string.word_next)
                mIsEditing = true
            }
        }

        text_push_charge_cash.setOnClickListener {
            val intent = Intent(this, CashConfigActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
        }

        text_push_send.setOnClickListener {
            val msg = Msg()

            msg.type = "push"
            msg.isReserved = false
            if (mCoupon == null) {
                showAlert(R.string.msg_select_send_post)
                return@setOnClickListener
            }

            msg.subject = LoginInfoManager.getInstance().user.page!!.name!!
            msg.moveType1 = MoveType1Code.inner.name

            msg.contents = mCoupon!!.name
            msg.moveType2 = MoveType2Code.couponDetail.name
            msg.moveTarget = No(mCoupon!!.seqNo)

            if (mUserList == null || mUserList!!.size == 0) {
                showAlert(R.string.msg_select_customer)
                return@setOnClickListener
            }

            val useCash = mPushPrice!! * mUserList!!.size
            msg.totalPrice = useCash

            val targetList = ArrayList<MsgTarget>()
            for (fan in mUserList!!) {
                val target = MsgTarget()
                target.user = No(fan.no)
                target.name = fan.nickname
                targetList.add(target)
            }

            msg.targetList = targetList

            when (mSendType) {

                EnumData.SendType.reservation -> {
                    msg.isReserved = true
                    intent = Intent(this, PushReservationActivity::class.java)
                    intent.putExtra(Const.KEY, EnumData.MsgType.push)
                    intent.putExtra(Const.DATA, msg)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESERVATION)
                }
                EnumData.SendType.immediately -> sendPush(msg)
            }
        }

        mSendType = EnumData.SendType.immediately
        setSelect(text_push_immediately, text_push_reservation)
        text_push_send.setText(R.string.word_next)

        mCoupon = intent.getParcelableExtra(Const.DATA)
        if (mCoupon != null) {
            mIsEditing = true
            setData()
        }

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                text_push_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
            }
        })
        getPlusCount()
    }

    private fun getPlusCount() {

        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {

                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_push_receiver.hint = getString(R.string.format_hint_plus_count, FormatUtil.getMoneyType(group.count.toString()))
                        break
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()

    }

    private fun sendPush(msg: Msg) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
//        builder.addContents(AlertData.MessageData(getString(R.string.msg_push_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.addContents(AlertData.MessageData(getString(R.string.format_push_send_alert2, mUserList!!.size), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_push_send))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        showProgress("")
                        ApiBuilder.create().insertPushMsg(msg).setCallback(object : PplusCallback<NewResultResponse<Msg>> {

                            override fun onResponse(call: Call<NewResultResponse<Msg>>, response: NewResultResponse<Msg>) {

                                hideProgress()
                                setResult(Activity.RESULT_OK)
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                if (msg.isReserved) {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_reserved_push), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                } else {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_send_push), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                }
                                builder.addContents(AlertData.MessageData(getString(R.string.msg_success_send), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                builder.setLeftText(getString(R.string.word_confirm))
                                builder.setAutoCancel(false)
                                builder.setBackgroundClickable(false)
                                builder.setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.SINGLE -> PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                                                override fun reload() {
                                                    resetData()
                                                }
                                            })
                                        }
                                    }
                                }).builder().show(this@PushSendActivity)


                            }

                            override fun onFailure(call: Call<NewResultResponse<Msg>>, t: Throwable, response: NewResultResponse<Msg>) {

                                hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_CONTENTS -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        mIsEditing = true
                        mCoupon = data.getParcelableExtra(Const.COUPON);
                        setData()
                    }
                }
            }
            Const.REQ_SEARCH -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        mIsEditing = true
                        mUserList = data.getParcelableArrayListExtra(Const.CUSTOMER)
                        if (mUserList != null && mUserList!!.size > 0) {
                            if (mUserList!!.size == 1) {
                                text_push_receiver.text = mUserList!![0].nickname
                            } else {
                                text_push_receiver.text = getString(R.string.format_other, "" + mUserList!![0].nickname, mUserList!!.size - 1)
                            }

                            text_push_pre_use_cash.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType((mPushPrice!! * mUserList!!.size).toString()))
                        }

                    }
                }
            }
            Const.REQ_RESERVATION -> {
                resetData()
            }
            Const.REQ_CASH_CHANGE -> {

                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        text_push_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
                    }
                })
            }
        }
    }

    fun setData() {
        layout_push_none_select.visibility = View.GONE
        layout_push_coupon.visibility = View.VISIBLE
        setCoupon()

    }

    private fun setCoupon() {
        val item = mCoupon!!

        layout_coupon_top.visibility = View.GONE
        view_coupon_sold_status.visibility = View.GONE
        text_coupon_sold_status.visibility = View.GONE

        if(item.represent != null && item.represent!!){
            text_coupon_sold_status.visibility = View.VISIBLE
            text_coupon_sold_status.setText(R.string.word_represent_coupon)
            text_coupon_sold_status.setBackgroundResource(R.drawable.bg_coupon_representation)
        }else{
            text_coupon_sold_status.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(item.expireDatetime)) {
            text_coupon_expire_date.visibility = View.VISIBLE
            text_coupon_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.expireDatetime)) + " " + getString(R.string.word_until)
        }else{
            text_coupon_expire_date.visibility = View.GONE
        }

        text_coupon_name.text = item.name
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                text_coupon_origin_price.visibility = View.GONE
            } else {
                text_coupon_origin_price.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                text_coupon_origin_price.visibility = View.VISIBLE
            }

        } else {
            text_coupon_origin_price.visibility = View.GONE
        }

        text_coupon_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
            Glide.with(this).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(image_coupon_page_image)
        } else {
            image_coupon_page_image.setImageResource(R.drawable.img_page_profile_circle_default)
        }
    }

    private fun resetData() {
        mIsEditing = false
        mUserList = null
        mCoupon = null
        layout_push_none_select.visibility = View.VISIBLE
        layout_push_coupon.visibility = View.GONE
        mSendType = EnumData.SendType.immediately
        setSelect(text_push_immediately, text_push_reservation)
        text_push_send.setText(R.string.word_push_send)
        text_push_pre_use_cash.text = ""
        text_push_receiver.text = ""
        text_push_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
    }

    fun setSelect(view1: View, view2: View) {
        view1.isSelected = true
        view2.isSelected = false
    }

    override fun onBackPressed() {

        if (mIsEditing) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_question_back), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> finish()
                    }
                }
            }).builder().show(this)
        } else {
            super.onBackPressed()
        }

    }
}
