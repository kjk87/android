package com.pplus.prnumberbiz.apps.main.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.NightAlertActivity
import com.pplus.prnumberbiz.apps.cash.ui.CashChargeActivity
import com.pplus.prnumberbiz.apps.cash.ui.CashChargeAlertActivity
import com.pplus.prnumberbiz.apps.cash.ui.CashConfigActivity2
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerActivity
import com.pplus.prnumberbiz.apps.customer.ui.SmsLockerActivity
import com.pplus.prnumberbiz.apps.goods.ui.SelectGoodsActivity
import com.pplus.prnumberbiz.apps.outgoingnumber.OutGoingNumberConfigActivity
import com.pplus.prnumberbiz.apps.push.ui.PushReservationActivity
import com.pplus.prnumberbiz.apps.sms.SmsResultActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_sms.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SmsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SmsFragment : BaseFragment<BaseActivity>() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sms
    }

    override fun initializeView(container: View?) {

    }


    private var mCustomerList: ArrayList<Customer>? = null
    private var mSmsCash: Int = 0
    private var isAds = true

    private var firstText: String? = null
    private var lastText: String? = null
    private var mSendType: EnumData.SendType? = null
    private var mSmsType: EnumData.SmsType? = null
    private var mMaxText: String? = null
    private var mSenderNo = LoginInfoManager.getInstance().user.mobile

    override fun init() {

        if (!PreferenceUtil.getDefaultPreference(activity).getBoolean(Const.NIGHT_ADS)) {
            val intent = Intent(activity, NightAlertActivity::class.java)
            startActivity(intent)
        }

        text_sms_title.setOnClickListener {
            activity?.finish()
        }

        text_sms_send_type.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_send_type))
            builder.setContents(getString(R.string.word_immediately_sent), getString(R.string.word_reservation_sent))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            mSendType = EnumData.SendType.immediately
                            text_sms_send_type.setText(R.string.word_immediately_sent)
                            text_sms_send.setText(R.string.msg_send_sms)
                        }

                        2 -> {
                            mSendType = EnumData.SendType.reservation
                            text_sms_send_type.setText(R.string.word_reservation_sent)
                            text_sms_send.setText(R.string.word_next)
                        }
                    }
                }
            }).builder().show(activity)
        }

        text_sms_send_select_sender.setOnClickListener {
            authedNumberAll()
        }

        text_sms_send_select_customer.setOnClickListener {
            val intent = Intent(activity, SelectCustomerActivity::class.java)
            intent.putExtra(Const.USER, EnumData.CustomerType.customer)
            if (mCustomerList != null && mCustomerList!!.isNotEmpty()) {
                intent.putParcelableArrayListExtra(Const.CUSTOMER, mCustomerList)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SEARCH)
        }

        text_sms_send_select_contents.setOnClickListener {
            ToastUtil.show(activity, R.string.msg_preparing_service)
//            val intent = Intent(activity, SelectGoodsActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CONTENTS)
        }

        edit_sms_send_contents.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (edit_sms_send_contents.length() > 0) {
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        edit_sms_send_contents.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {

                val contents = editable.toString()
                var bytes = 0
                if (isAds) {
                    bytes = firstText!!.toByteArray().size + contents.toByteArray().size + lastText!!.toByteArray().size
                } else {
                    bytes = contents.toByteArray().size
                }

                if (bytes > 2000) {
                    edit_sms_send_contents.setText(mMaxText)
                } else {
                    mMaxText = contents
                }

                setBytes(bytes)
            }
        })

        text_sms_send_save.setOnClickListener {
            val savedMsg = SavedMsg()
            savedMsg.user = No(LoginInfoManager.getInstance().user.no)
            if (StringUtils.isEmpty(edit_sms_send_contents.text.toString().trim())) {
                showAlert(R.string.msg_input_contents)
                return@setOnClickListener
            }

            val savedContents = StringBuilder()
            savedContents.append(edit_sms_send_contents.text.toString().trim())
            savedMsg.priority = 1
            val savedMsgProperties = SavedMsgProperties()
            savedMsgProperties.msg = savedContents.toString()
            savedMsgProperties.isAdvertise = isAds
            savedMsg.properties = savedMsgProperties

            showProgress("")
            ApiBuilder.create().insertSavedMsg(savedMsg).setCallback(object : PplusCallback<NewResultResponse<SavedMsg>> {

                override fun onResponse(call: Call<NewResultResponse<SavedMsg>>, response: NewResultResponse<SavedMsg>) {

                    hideProgress()
                    showAlert(R.string.msg_saved_msg)
                }

                override fun onFailure(call: Call<NewResultResponse<SavedMsg>>, t: Throwable, response: NewResultResponse<SavedMsg>) {

                    hideProgress()
                }
            }).build().call()
        }

        text_sms_send_locker.setOnClickListener {
            val intent = Intent(activity, SmsLockerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity?.startActivityForResult(intent, Const.REQ_SMS_LOCKER)
        }

//        text_sms_send_charge_cash.setOnClickListener {
//            val intent = Intent(activity, CashConfigActivity2::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

        text_sms_send.setOnClickListener {
            val msg = Msg()

            msg.type = mSmsType!!.name.toLowerCase()
            msg.isReserved = false

            if (StringUtils.isEmpty(edit_sms_send_contents.text.toString().trim())) {
                showAlert(R.string.msg_input_contents)
                return@setOnClickListener
            }

            val contents = StringBuilder()
            if (isAds) {
                contents.append(firstText)
            }
            contents.append(edit_sms_send_contents.text.toString().trim())
            if (isAds) {
                contents.append(lastText)
            }

            msg.contents = contents.toString()

            if (mCustomerList == null || mCustomerList!!.isEmpty()) {
                showAlert(R.string.msg_select_customer)
                return@setOnClickListener
            }

            val useCash = mSmsCash * mCustomerList!!.size
            val retentionCash = LoginInfoManager.getInstance().user.totalCash
            if (useCash > retentionCash) {
                val alertBuilder = AlertBuilder.Builder()
                alertBuilder.setTitle(getString(R.string.word_notice_alert))
                alertBuilder.addContents(AlertData.MessageData(getString(R.string.msg_ads_setting_cash_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                alertBuilder.addContents(AlertData.MessageData(getString(R.string.msg_ads_setting_cash_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                alertBuilder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                alertBuilder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                if (LoginInfoManager.getInstance().user.page!!.agent != null) {
                                    val intent = Intent(activity, CashChargeAlertActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                } else {
//                                    val intent = Intent(activity, CashBillingActivity::class.java)
                                    val intent = Intent(activity, CashChargeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
                                }
                            }
                        }
                    }
                })
                return@setOnClickListener
            }
            msg.totalPrice = useCash
            val targetList = ArrayList<MsgTarget>()
            for (customer in mCustomerList!!) {
                val target = MsgTarget()
                target.mobile = customer.getMobile()
                target.name = customer.getName()
                targetList.add(target)
            }

            msg.targetList = targetList
            val properties = MsgProperties()
            properties.senderNo = mSenderNo
            properties.isAdvertise = isAds
            msg.properties = properties

            when (mSendType) {

                EnumData.SendType.reservation -> {
                    msg.isReserved = true
                    val intent = Intent(activity, PushReservationActivity::class.java)
                    intent.putExtra(Const.KEY, EnumData.MsgType.sms)
                    intent.putExtra(Const.DATA, msg)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_RESERVATION)
                }
                EnumData.SendType.immediately -> sendSms(msg)
            }
        }

        text_sms_result.setOnClickListener {
            val intent = Intent(activity, SmsResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        text_sms_send_reg_send_number.setOnClickListener {
            val intent = Intent(activity, OutGoingNumberConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        text_sms_detail.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.msg_send_lms))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_sms_send_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_sms_send_description3), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.addContents(AlertData.MessageData(getString(R.string.format_msg_sms_send_description4, CountryConfigManager.getInstance().config.properties.adLmsPrice), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_sms_send_description5), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.builder().show(activity)
        }

//        text_sms_cost.text = getString(R.string.format_push_cost_description, CountryConfigManager.getInstance().config.properties.adLmsPrice.toString())
        resetData()

        val goods = parentActivity.intent.getParcelableExtra<Goods>(Const.DATA)
        if (goods != null) {
            val url = getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${goods.seqNo}")
            edit_sms_send_contents.append("\n\n" + url)
        }
    }

    private fun sendSms(msg: Msg) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_sms_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.addContents(AlertData.MessageData(getString(R.string.format_sms_send_alert2, mCustomerList!!.size), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_sms_send))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        showProgress("")
                        ApiBuilder.create().insertSmsMsg(msg).setCallback(object : PplusCallback<NewResultResponse<Msg>> {

                            override fun onResponse(call: Call<NewResultResponse<Msg>>, response: NewResultResponse<Msg>) {

                                hideProgress()
                                if (!isAdded) {
                                    return
                                }
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                if (msg.isReserved) {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_reserved_sms), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                                } else {
                                    builder.addContents(AlertData.MessageData(getString(R.string.msg_success_send_sms), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
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
                                            AlertBuilder.EVENT_ALERT.SINGLE ->
                                                resetData()
                                        }
                                    }
                                }).builder().show(activity)


                            }

                            override fun onFailure(call: Call<NewResultResponse<Msg>>, t: Throwable, response: NewResultResponse<Msg>) {

                                hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(activity)
    }

    fun resetData() {
        mCustomerList = null
        text_sms_send_ads.text = getString(R.string.word_sms_ads2) + LoginInfoManager.getInstance().user.page!!.name!!
        text_sms_send_deny_reception.text = getString(R.string.format_sms_free_deny_reception, LoginInfoManager.getInstance().user.page!!.no.toString())
        firstText = text_sms_send_ads.text.toString() + "\n"
        lastText = "\n" + "\n" + text_sms_send_deny_reception.text.toString()
        visibleAds(isAds)
        mSmsCash = CountryConfigManager.getInstance().config.properties.smsPrice!!
        mSmsType = EnumData.SmsType.LMS
        text_sms_send_select_sender.text = mSenderNo
        mSendType = EnumData.SendType.immediately
        text_sms_send_type.setText(R.string.word_immediately_sent)
        text_sms_send.setText(R.string.msg_send_sms)

        text_sms_send.setText(R.string.msg_send_sms)

        text_sms_send_pre_use_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, "0"))
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

//                text_sms_send_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
            }
        })
        text_sms_send_select_customer.text = ""
        text_sms_send_select_customer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_arrow, 0)
        edit_sms_send_contents.setText("")
    }

    private fun visibleAds(visible: Boolean) {

//        text_sms_send_check_ads.isSelected = visible
        val contents = edit_sms_send_contents.text.toString()
        if (visible) {
            text_sms_send_ads.visibility = View.VISIBLE
            text_sms_send_deny_reception.visibility = View.VISIBLE

            setBytes(firstText!!.toByteArray().size + contents.toByteArray().size + lastText!!.toByteArray().size)
        } else {
            text_sms_send_ads.visibility = View.GONE
            text_sms_send_deny_reception.visibility = View.GONE
            setBytes(contents.toByteArray().size)
        }

    }

    private fun setBytes(bytes: Int) {

//        if (bytes <= 90) {
//            if (isAds) {
//                mSmsCash = CountryConfigManager.getInstance().config.properties.adSmsPrice!!
//            } else {
//                mSmsCash = CountryConfigManager.getInstance().config.properties.smsPrice!!
//            }
//
//            mSmsType = EnumData.SmsType.SMS
//            text_sms_send_byte.text = getString(R.string.format_sms_byte, bytes, 90)
//        } else {
//            if (isAds) {
//                mSmsCash = CountryConfigManager.getInstance().config.properties.adLmsPrice!!
//            } else {
//                mSmsCash = CountryConfigManager.getInstance().config.properties.lmsPrice!!
//            }
//
//            mSmsType = EnumData.SmsType.LMS
//            text_sms_send_byte.text = getString(R.string.format_sms_byte, bytes, 2000)
//        }

        if (isAds) {
            mSmsCash = CountryConfigManager.getInstance().config.properties.adLmsPrice!!
        } else {
            mSmsCash = CountryConfigManager.getInstance().config.properties.lmsPrice!!
        }

        mSmsType = EnumData.SmsType.LMS
        text_sms_send_byte.text = getString(R.string.format_sms_byte, bytes, 2000)

        setPreUseCash()
    }

    private fun setPreUseCash() {

        if (mCustomerList != null && mCustomerList!!.isNotEmpty()) {
            text_sms_send_pre_use_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType((mSmsCash * mCustomerList!!.size).toString())))
        }
    }

    fun setSelect(view1: View, view2: View) {
        view1.isSelected = true
        view2.isSelected = false
    }

    private fun authedNumberAll() {

        showProgress("")
        ApiBuilder.create().authedNumberAll.setCallback(object : PplusCallback<NewResultResponse<OutgoingNumber>> {

            override fun onResponse(call: Call<NewResultResponse<OutgoingNumber>>, response: NewResultResponse<OutgoingNumber>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                val dataList = response.datas

                if (dataList != null && dataList.size > 0) {
                    val numberList = arrayListOf<String>()
                    for (data in dataList) {
                        numberList.add(PhoneNumberUtils.formatNumber(data.mobile, Locale.getDefault().country))
                    }

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_select_outgoing_number))
                    builder.setArrayContents(numberList).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO)
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RADIO -> {
                                    mSenderNo = numberList[event_alert.getValue() - 1]?.replace("-", "")
                                    text_sms_send_select_sender.text = mSenderNo
                                }
                            }


                        }
                    }).builder().show(activity)
                } else {
                    showAlert(R.string.msg_not_exist_outgoing_number_list)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<OutgoingNumber>>, t: Throwable, response: NewResultResponse<OutgoingNumber>) {

            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_SEARCH -> {
                    if (data != null) {
                        mCustomerList = data.getParcelableArrayListExtra(Const.CUSTOMER)
                        if (mCustomerList != null && mCustomerList!!.isNotEmpty()) {
                            if (mCustomerList!!.size == 1) {
                                text_sms_send_select_customer.text = mCustomerList!![0].name
                            } else {
                                text_sms_send_select_customer.text = getString(R.string.format_other, mCustomerList!![0].name, mCustomerList!!.size - 1)
                            }

                            text_sms_send_select_customer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_input_arrow_blue, 0)

                            setPreUseCash()
                        }
                    }
                }
                Const.REQ_CONTENTS -> {
                    if (data != null) {
                        val goods = data.getParcelableExtra<Goods>(Const.GOODS)
                        if (goods != null) {
                            val url = getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${goods.seqNo}")
                            edit_sms_send_contents.append("\n\n" + url)
                        }

                    }
                }
                Const.REQ_RESERVATION -> {
                    resetData()
                }
                Const.REQ_SMS_LOCKER -> {
                    if (data != null) {
                        val msg = data.getParcelableExtra<SavedMsg>(Const.MSG)
                        edit_sms_send_contents.setText(msg.properties.msg)
                    }
                }
                Const.REQ_CASH_CHANGE -> {
                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

                        override fun reload() {

//                            text_sms_send_retention_cash.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalCash)))
                        }
                    })
                }
            }
        }

    }

    override fun getPID(): String {
        return ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this mapFragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of mapFragment SmsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                SmsFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
