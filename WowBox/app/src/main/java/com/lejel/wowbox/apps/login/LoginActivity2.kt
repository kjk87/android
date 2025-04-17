package com.lejel.wowbox.apps.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.join.JoinActivity
import com.lejel.wowbox.apps.verify.ui.SelectVerifyTypeActivity
import com.lejel.wowbox.apps.verify.ui.VerifySmsActivity
import com.lejel.wowbox.apps.verify.ui.VerifyWhatsAppActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Config
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.GoogleUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLogin2Binding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class LoginActivity2 : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLogin2Binding

    override fun getLayoutView(): View {
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.editLoginMobileNumber.setSingleLine()

        binding.textLogin.setOnClickListener {
            val mobileNumber = binding.editLoginMobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.hint_input_mobile_number)
                return@setOnClickListener
            }

            if (!FormatUtil.isPhoneNumber(mobileNumber)) {
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }

            checkJoinedMobile(mobileNumber)
        }

        binding.textLoginJoin.setOnClickListener {
            val intent = Intent(this@LoginActivity2, JoinActivity::class.java)
            intent.putExtra(Const.TYPE, "mobile")
            joinLauncher.launch(intent)
        }

        GoogleUtil.init(this, mGoogleSignListener)
        binding.layoutGoogleLogin.setOnClickListener {
            setEvent(FirebaseAnalytics.Event.LOGIN)
            showProgress("")
            GoogleUtil.launch(mGoogleLauncher)
        }

        binding.textDemoLogin.setOnClickListener {
            loginByPlatform("109562419473057930391", "j2nroot@gmail.com")
        }

        getDemoLoginConfig()
    }
    private fun getDemoLoginConfig() {
        showProgress("")
        ApiBuilder.create().getConfig("demoLogin").setCallback(object : PplusCallback<NewResultResponse<Config>> {
            override fun onResponse(call: Call<NewResultResponse<Config>>?, response: NewResultResponse<Config>?) {
                hideProgress()
                if (response?.result != null) {
                    val version = response.result!!.config!!
                    if (version == PplusCommonUtil.getAppVersion(this@LoginActivity2)) {
                        binding.textDemoLogin.visibility = View.VISIBLE
                    } else {
                        binding.textDemoLogin.visibility = View.GONE
                    }
                } else {
                    binding.textDemoLogin.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Config>>?, t: Throwable?, response: NewResultResponse<Config>?) {
                hideProgress()
                binding.textDemoLogin.visibility = View.GONE
            }
        }).build().call()
    }

    private fun checkJoinedMobile(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        showProgress("")
        ApiBuilder.create().checkJoinedMobile(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@LoginActivity2, SelectVerifyTypeActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                selectVerifyTypeLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()

                if(response?.code == 590){
                    showAlert(R.string.msg_already_joined_google)
                }else if(response?.code == 591){
                    showAlert(R.string.msg_already_joined_apple)
                }else{
                    val intent = Intent(this@LoginActivity2, JoinActivity::class.java)
                    intent.putExtra(Const.PLATFORM_KEY, mobileNumber)
                    intent.putExtra(Const.TYPE, "mobile")
                    joinLauncher.launch(intent)
                }
            }
        }).build().call()
    }

    private fun sendWhatsApp(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "login"
        showProgress("")
        ApiBuilder.create().sendWhatsapp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@LoginActivity2, VerifyWhatsAppActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "login")
                mobileLoginLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@LoginActivity2, JoinActivity::class.java)
                intent.putExtra(Const.PLATFORM_KEY, mobileNumber)
                intent.putExtra(Const.TYPE, "mobile")
                joinLauncher.launch(intent)
            }
        }).build().call()
    }

    private fun sendSms(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "login"
        showProgress("")
        ApiBuilder.create().sendSms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@LoginActivity2, VerifySmsActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "login")
                mobileLoginLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@LoginActivity2, JoinActivity::class.java)
                intent.putExtra(Const.PLATFORM_KEY, mobileNumber)
                intent.putExtra(Const.TYPE, "mobile")
                joinLauncher.launch(intent)
            }
        }).build().call()
    }

    private fun loginByPlatform(platformKey: String, email: String) {
        val params = HashMap<String, String>()
        params["platformKey"] = platformKey
        params["device"] = PplusCommonUtil.getDeviceID()
        showProgress("")
        ApiBuilder.create().loginByPlatform(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.result != null) {
                    if (response.result!!.status == "active") {
                        LoginInfoManager.getInstance().member = response.result
                        LoginInfoManager.getInstance().save()

                        setResult(RESULT_OK)
                        finish()

                    //                        getNotSignedTermsList()


                    } else if (response.result!!.status == "waitingLeave") {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.msg_alert_waiting_leave))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_waiting_leave_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_withdrawal_cancel))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@LoginActivity2, WithdrawalCancelAuthActivity::class.java)
                                        intent.putExtra(Const.DATA, response.result)
                                        startActivity(intent)
                                    }

                                    else -> {

                                    }
                                }
                            }
                        }).builder().show(this@LoginActivity2)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.code == 404) {
                    val intent = Intent(this@LoginActivity2, JoinActivity::class.java)
                    intent.putExtra(Const.PLATFORM_KEY, platformKey)
                    intent.putExtra(Const.PLATFORM_EMAIL, email)
                    intent.putExtra(Const.TYPE, "google")
                    joinLauncher.launch(intent)

                }
            }
        }).build().call()
    }

    var mGoogleSignListener = object : GoogleUtil.GoogleSignListener {
        override fun handleSignInResult(account: GoogleSignInAccount?) {
            hideProgress()
            if (account != null) {
                val id = account.id
                val email = account.email

                GoogleUtil.signOut()

                LogUtil.e("id", "" + id!!)
                LogUtil.e("id", "" + email)
                val params = HashMap<String, String>()
                params["platformKey"] = id
                params["device"] = PplusCommonUtil.getDeviceID()
                loginByPlatform(id, email!!)
            }
        }
    }

    val selectVerifyTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mobileNumber = result.data!!.getStringExtra(Const.MOOBILE_NUMBER)!!
            val verifyType = result.data!!.getStringExtra(Const.VERIFY_TYPE)!!
            when (verifyType) {
                "whatsapp" -> {
                    sendWhatsApp(mobileNumber)
                }

                else -> {
                    sendSms(mobileNumber)
                }
            }
        }
    }

    val mobileLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    val joinLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    val mGoogleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        LogUtil.e(LOG_TAG, result.toString())

        if (result.resultCode == Activity.RESULT_OK) {
            setEvent("google_login_success")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            mGoogleSignListener.handleSignInResult(task.getResult(ApiException::class.java))
        } else {
            setEvent("google_login_fail")
            hideProgress()
        }
    }

}