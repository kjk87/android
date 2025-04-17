package com.pplus.prnumberuser.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData.MessageData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.signin.ui.ChangePWActivity
import com.pplus.prnumberuser.apps.signin.ui.FindPWActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityAccountConfigBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class AccountConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAccountConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityAccountConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAccountNumberChange.setOnClickListener {
            if (LoginInfoManager.getInstance().user.verification!!.media == "external") {
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
                                changeMobileLauncher.launch(intent)
                            }
                        }
                    }
                }).builder().show(this)
            } else {
                intent = Intent(this@AccountConfigActivity, VerificationActivity::class.java)
                intent.putExtra(Const.KEY, Const.MOBILE_NUMBER)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                changeMobileLauncher.launch(intent)
            }
        }

        binding.textAccountChangePassword.setOnClickListener {
            if (LoginInfoManager.getInstance().user.verification!!.media == "external") {
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
                                findPwLauncher.launch(intent)
                            }
                        }
                    }
                }).builder().show(this)
            } else {
                intent = Intent(this, FindPWActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        binding.textAccountSecession.setOnClickListener {
            intent = Intent(this, SecessionActivity::class.java)
            startActivity(intent)
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
                binding.layoutAccountConfigPassword.visibility = View.VISIBLE
                binding.textAccountId.text = user.loginId
                binding.textPassword.text = Const.PASSWORD_DEFAULT
                // 패스워드는 Foucs Event 처리
                binding.textPassword.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
                    if (view === binding.textPassword) {
                        if (hasFocus) {
                            binding.textPassword.text = ""
                            binding.textPassword.setHint(R.string.msg_password_focus)
                            showKeyboard(binding.textPassword)
                        } else {
                            binding.textPassword.hint = ""
                            binding.textPassword.text = Const.PASSWORD_DEFAULT
                        }
                    }
                }
            } else {
                binding.layoutAccountConfigPassword.visibility = View.GONE

                when (SnsTypeCode.valueOf(user.accountType!!)) {
                    SnsTypeCode.facebook -> binding.textAccountId.text = user.loginId + "(" + getString(R.string.word_account_facebook) + ")"
                    SnsTypeCode.naver -> binding.textAccountId.text = user.loginId + "(" + getString(R.string.word_account_naver) + ")"
                    SnsTypeCode.google -> binding.textAccountId.text = user.loginId + "(" + getString(R.string.word_account_google) + ")"
                    SnsTypeCode.kakao -> binding.textAccountId.text = user.loginId + "(" + getString(R.string.word_account_kakao) + ")"
                }
            }
            // 인증된 번호
            binding.textAccountPhoneNumber.text = user.mobile
            if (LoginInfoManager.getInstance().user.page != null && StringUtils.isNotEmpty(user.name)) {
                binding.textAccountNameTitle.visibility = View.VISIBLE
                binding.textAccountName.visibility = View.VISIBLE
                binding.textAccountName.text = user.name
            } else {
                binding.textAccountNameTitle.visibility = View.GONE
                binding.textAccountName.visibility = View.GONE
            }
        }
    }

    fun changePassword(verification: Verification?) {
        LogUtil.e(LOG_TAG, "changePassword")
        LogUtil.e(LOG_TAG, "LOGIN ID : {}", LoginInfoManager.getInstance().user.loginId)
        val intent = Intent(this, ChangePWActivity::class.java)
        intent.putExtra(Const.VERIFICATION, verification)
        intent.putExtra(Const.LOGIN_ID, LoginInfoManager.getInstance().user.loginId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    val changeMobileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(data != null){
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobile = data.getStringExtra(Const.MOBILE_NUMBER)
                changeMobile(verification!!, mobile!!)
            }
        }
    }

    val findPwLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(data != null){
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobileNumber = data.getStringExtra(Const.MOBILE_NUMBER)
                if(LoginInfoManager.getInstance().user.mobile != mobileNumber){
                    showAlert(R.string.msg_incorrect_joined_mobile_number)
                }else{
                    changePassword(verification)
                }
            }
        }
    }

    private fun changeMobile(verification: Verification, mobile: String) {
        val params = HashMap<String, String>()
        params["mobile"] = mobile
        params["number"] = verification.number
        params["token"] = verification.token
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().updateMobileByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                LoginInfoManager.getInstance().user.mobile = mobile
                LoginInfoManager.getInstance().save()
                showAlert(R.string.msg_changed_mobile)
                binding.textAccountPhoneNumber.text = mobile
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
                if (response.resultCode == 504) {
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
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun showKeyboard(v: View?) {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }
}