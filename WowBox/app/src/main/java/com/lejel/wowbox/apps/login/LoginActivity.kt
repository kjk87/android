package com.lejel.wowbox.apps.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.terms.ui.AddTermsAgreeActivity
import com.lejel.wowbox.apps.join.JoinActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Config
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.GoogleUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLoginBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class LoginActivity : BaseActivity() {

    override fun getPID(): String {
        return "Login"
    }

    private lateinit var binding: ActivityLoginBinding

    override fun getLayoutView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        GoogleUtil.init(this, mGoogleSignListener)
        binding.layoutLogin.setOnClickListener {
            setEvent(FirebaseAnalytics.Event.LOGIN)
            showProgress("")
            GoogleUtil.launch(mGoogleLauncher)
        }

        binding.layoutDemoLogin.setOnClickListener {
            login("109562419473057930391", "j2nroot@gmail.com")
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
                    if (version == PplusCommonUtil.getAppVersion(this@LoginActivity)) {
                        binding.layoutDemoLogin.visibility = View.VISIBLE
                    } else {
                        binding.layoutDemoLogin.visibility = View.GONE
                    }
                } else {
                    binding.layoutDemoLogin.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Config>>?, t: Throwable?, response: NewResultResponse<Config>?) {
                hideProgress()
                binding.layoutDemoLogin.visibility = View.GONE
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
                login(id, email!!)
            }
        }
    }

    private fun login(platformKey: String, email: String) {
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
                        getNotSignedTermsList()
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
                                        val intent = Intent(this@LoginActivity, WithdrawalCancelAuthActivity::class.java)
                                        intent.putExtra(Const.DATA, response.result)
                                        startActivity(intent)
                                    }

                                    else -> {

                                    }
                                }
                            }
                        }).builder().show(this@LoginActivity)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.code == 404) {
                    val intent = Intent(this@LoginActivity, JoinActivity::class.java)
                    intent.putExtra(Const.PLATFORM_KEY, platformKey)
                    intent.putExtra(Const.PLATFORM_EMAIL, email)
                    intent.putExtra(Const.TYPE, "google")
                    joinLauncher.launch(intent)

                }
            }
        }).build().call()
    }

    private fun getNotSignedTermsList() {
        showProgress("")
        ApiBuilder.create().getNotSignedList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    for (terms in response.result!!.list!!) {
                        if (terms.compulsory!!) {
                            val intent = Intent(this@LoginActivity, AddTermsAgreeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            termsAgreeLauncher.launch(intent)
                            return
                        }
                    }
                }
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val termsAgreeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getNotSignedTermsList()
    }

    val joinLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val platformKey = result.data!!.getStringExtra(Const.PLATFORM_KEY)!!
            val platformEmail = result.data!!.getStringExtra(Const.PLATFORM_EMAIL)!!
            login(platformKey, platformEmail)
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