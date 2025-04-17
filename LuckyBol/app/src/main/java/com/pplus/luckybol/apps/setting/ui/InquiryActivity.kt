package com.pplus.luckybol.apps.setting.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData.MessageData
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData.InquiryType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.No
import com.pplus.luckybol.core.network.model.dto.Post
import com.pplus.luckybol.core.network.model.dto.PostProperties
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.fromHtml
import com.pplus.luckybol.databinding.ActivityInquiryBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

/**
 * Created by ksh on 2016-08-30.
 * 문의하기
 */
class InquiryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityInquiryBinding

    override fun getLayoutView(): View {
        binding = ActivityInquiryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mIsDirect = false
    private var mInquiryType: InquiryType? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textInquiryDescription.text = fromHtml(getString(R.string.html_msg_inquiry_description))
        binding.editInquirySubject.setSingleLine()
        binding.editInquiryMailId.setSingleLine()
        binding.textInquirySelection.setOnClickListener{
            showEmailPopup()
        }
        binding.editInquiryEmail.visibility = View.GONE
        mInquiryType = InquiryType.inquiry
        binding.textInquiryType.setText(R.string.msg_inquiry)
        binding.textInquiryType.setOnClickListener{
            showTypePopup()
        }
    }

    private fun insert() {
        val subject = binding.editInquirySubject.text.toString().trim()
        if (StringUtils.isEmpty(subject)) {
            showAlert(R.string.msg_input_title)
            return
        }
        val contents = binding.editInquiryContents.text.toString().trim()
        if (StringUtils.isEmpty(contents)) {
            showAlert(R.string.msg_input_contents)
            return
        }
        var email: String? = null
        if (mIsDirect) {
            email = binding.editInquiryEmail.text.toString().trim()
            if (email.trim().length == 0) {
                showAlert(R.string.msg_input_email_id)
                return
            }
        } else {
            email = binding.editInquiryMailId.text.toString()
            if (email.trim().length == 0) {
                showAlert(R.string.msg_input_email_id)
                return
            }
            email = email + "@" + binding.textInquirySelection.text.toString()
        }
        if (!FormatUtil.isEmailAddress(email)) {
            showAlert(R.string.msg_valid_email)
            return
        }
        val params = Post()
        when (mInquiryType) {
            InquiryType.inquiry -> params.board = No(CountryConfigManager.getInstance().config.properties.inquiryBoard)
            InquiryType.suggest -> params.board = No(CountryConfigManager.getInstance().config.properties.suggestBoard)
            else -> {}
        }
        params.subject = subject
        params.contents = contents
        params.type = mInquiryType!!.name
        params.appType = Const.APP_TYPE
        val properties = PostProperties()
        properties.email = email
        params.properties = properties
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(MessageData(getString(R.string.msg_question_inquiry), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {}
            override fun onResult(event_alert: EVENT_ALERT) {
                when (event_alert) {
                    EVENT_ALERT.RIGHT -> {
                        val handler = Handler(Looper.myLooper()!!)
                        handler.post{
                            insertInquiry(params)
                        }

                    }
                    else -> {}
                }
            }
        }).builder().show(this)
    }

    private fun insertInquiry(params:Post){
        showProgress("")
        ApiBuilder.create().insertPost(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
            override fun onResponse(call: Call<NewResultResponse<Post>>?,
                                    response: NewResultResponse<Post>?) {
                hideProgress()
                showAlert(R.string.msg_complete_regist)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Post>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Post>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun showTypePopup() {
        val contents = arrayOf(getString(R.string.msg_inquiry), getString(R.string.msg_suggestion))
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_select))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
        builder.setContents(*contents)
        builder.setLeftText(getString(R.string.word_cancel))
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {}
            override fun onResult(event_alert: EVENT_ALERT) {
                when (event_alert) {
                    EVENT_ALERT.LIST -> when (event_alert.getValue()) {
                        1 -> {
                            mInquiryType = InquiryType.inquiry
                            binding.textInquiryType.setText(R.string.msg_inquiry)
                        }
                        2 -> {
                            mInquiryType = InquiryType.suggest
                            binding.textInquiryType.setText(R.string.msg_suggestion)
                        }
                    }
                    else -> {}
                }
            }
        }).builder().show(this)
    }

    /**
     * 이메일 선택 팝업
     */
    private fun showEmailPopup() {
        val mailAddress = resources.getStringArray(R.array.mail_address)
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_select))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
        builder.setContents(*mailAddress)
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {}
            override fun onResult(event_alert: EVENT_ALERT) {
                when (event_alert) {
                    EVENT_ALERT.LIST -> if (event_alert == EVENT_ALERT.LIST) {
                        val value = event_alert.getValue()
                        LogUtil.e(LOG_TAG, "value = {}", event_alert.getValue())
                        if (event_alert.getValue() == mailAddress.size) {
                            // 직접 입력
                            mIsDirect = true
                            binding.layoutInquirySelectMail.visibility = View.GONE
                            binding.editInquiryEmail.visibility = View.VISIBLE
                        } else {
                            // 이메일 선택 완료
                            binding.layoutInquirySelectMail.visibility = View.VISIBLE
                            binding.editInquiryEmail.visibility = View.GONE
                            binding.textInquirySelection.text = mailAddress[event_alert.getValue() - 1]
                            mIsDirect = false
                        }
                    }
                    else -> {}
                }
            }
        }).builder().show(this)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_inquiry), ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, getString(R.string.word_complete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarMenu.RIGHT -> if (tag == 1) {
                    insert()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.editInquiryContents.text.toString().trim().length > 0) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(MessageData(getString(R.string.msg_alert_back_button1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(MessageData(getString(R.string.msg_alert_back_button2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> finish()
                        else -> {}
                    }
                }
            }).builder().show(this)
        } else {
            finish()
        }
    }
}