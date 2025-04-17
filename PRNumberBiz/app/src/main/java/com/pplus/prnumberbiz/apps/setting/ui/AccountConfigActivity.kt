package com.pplus.prnumberbiz.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData.MessageData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.signin.ui.ChangePWActivity
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.logOutAndRestart
import kotlinx.android.synthetic.main.activity_account_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class AccountConfigActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_account_config
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        text_account_numberChange.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(MessageData(getString(R.string.msg_verification_change_mobile_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.addContents(MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> {
                            val intent = Intent(this@AccountConfigActivity, VerificationMeActivity::class.java)
                            intent.putExtra(Const.KEY, Const.VERIFICATION)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_CHANGE_PHONE)
                        }
                    }
                }
            }).builder().show(this)
        }

        text_account_changePassword.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(MessageData(getString(R.string.msg_change_pw_verification_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> {
                            val intent = Intent(this@AccountConfigActivity, VerificationMeActivity::class.java)
                            intent.putExtra(Const.MOBILE_NUMBER, LoginInfoManager.getInstance().user.mobile)
                            intent.putExtra(Const.KEY, Const.VERIFICATION)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_FIND_PW)
                        }
                    }
                }
            }).builder().show(this)
        }

        text_account_secession.setOnClickListener {
            intent = Intent(this, SecessionActivity::class.java)
            startActivity(intent)
        }

        text_account_config_logout.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(MessageData(getString(R.string.msg_question_logout), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> logOutAndRestart()
                    }
                }
            }).builder().show(this@AccountConfigActivity)
        }
        setProfileData()
    }

    /**
     * UserInfo에서 Login과 Profile 정보를 가져와서 설정
     */
    private fun setProfileData() {
        if (LoginInfoManager.getInstance().user != null) {
            val user = LoginInfoManager.getInstance().user
            // ID, Pass
            if (user.accountType == SnsTypeCode.pplus.name) {
                layout_account_config_password.visibility = View.VISIBLE
                text_account_id.text = user.loginId
                text_password.text = Const.PASSWORD_DEFAULT
                // 패스워드는 Foucs Event 처리
                text_password.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
                    if (view === text_password) {
                        if (hasFocus) {
                            text_password.text = ""
                            text_password.setHint(R.string.msg_password_focus)
                            showKeyboard(text_password)
                        } else {
                            text_password.hint = ""
                            text_password.text = Const.PASSWORD_DEFAULT
                        }
                    }
                }
            } else {
                layout_account_config_password.visibility = View.GONE
                when (SnsTypeCode.valueOf(user.accountType!!)) {
                    SnsTypeCode.naver -> text_account_id.text = user.loginId + "(" + getString(R.string.word_account_naver) + ")"
                    SnsTypeCode.google -> text_account_id.text = user.loginId + "(" + getString(R.string.word_account_google) + ")"
                    SnsTypeCode.kakao -> text_account_id.text = user.loginId + "(" + getString(R.string.word_account_kakao) + ")"
                }
            }
            // 인증된 번호
            text_account_phoneNumber.text = user.mobile
            if (StringUtils.isNotEmpty(user.name) && user.name != "nonamed") {
                findViewById<View>(R.id.text_account_name_title).visibility = View.VISIBLE
                text_account_name.visibility = View.VISIBLE
                text_account_name.text = user.name
            } else {
                findViewById<View>(R.id.text_account_name_title).visibility = View.GONE
                text_account_name.visibility = View.GONE
            }
        }
    }

    fun changePassword(verification: Verification?) {
        val intent = Intent(this, ChangePWActivity::class.java)
        intent.putExtra(Const.VERIFICATION, verification)
        intent.putExtra(Const.LOGIN_ID, LoginInfoManager.getInstance().user.loginId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, Const.REQ_CHANGE_PW)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_FIND_PW -> if (data != null) {
                    val verification: Verification = data.getParcelableExtra(Const.VERIFICATION)
                    changePassword(verification)
                }
                Const.REQ_CHANGE_PHONE -> if (data != null) {
                    val verification: Verification = data.getParcelableExtra(Const.VERIFICATION)
                    val mobile = data.getStringExtra(Const.MOBILE_NUMBER)
                    changeMobile(verification, mobile)
                }
                Const.REQ_CHANGE_PW -> {
                }
            }
        }
    }

    private fun changeMobile(verification: Verification, mobile: String) {
        val params: MutableMap<String, String> = HashMap()
        params["mobile"] = mobile
        params["number"] = verification.number
        params["token"] = verification.token
        showProgress("")
        ApiBuilder.create().updateMobileByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any?>?> {
            override fun onResponse(call: Call<NewResultResponse<Any?>?>?, response: NewResultResponse<Any?>?) {
                hideProgress()
                LoginInfoManager.getInstance().user.mobile = mobile
                LoginInfoManager.getInstance().save()
                showAlert(R.string.msg_changed_mobile)
                text_account_phoneNumber.text = mobile
            }

            override fun onFailure(call: Call<NewResultResponse<Any?>?>?, t: Throwable?, response: NewResultResponse<Any?>?) {
                hideProgress()
                if (response != null && response.resultCode == 504) {
                    showAlert(R.string.msg_exist_mobile)
                } else {
                    showAlert(R.string.msg_can_not_use_mobile)
                }
            }

        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_config_account), ToolbarMenu.LEFT)
        //toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }

    private fun showKeyboard(v: View?) {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }
}