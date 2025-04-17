package com.root37.buflexz.apps.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.terms.ui.AddTermsAgreeActivity
import com.root37.buflexz.apps.join.JoinActivity
import com.root37.buflexz.apps.my.ui.WithdrawalCompleteActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.dto.Nation
import com.root37.buflexz.core.network.model.dto.Terms
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.GoogleUtil
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLoginBinding
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
        GoogleUtil.init(this, mGoogleSignListener)
        binding.layoutLogin.setOnClickListener {
            showProgress("")
            GoogleUtil.launch(mGoogleLauncher)
        }
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

    private fun login(platformKey:String, email:String){
        val params = HashMap<String, String>()
        params["platformKey"] = platformKey
        params["device"] = PplusCommonUtil.getDeviceID()
        showProgress("")
        ApiBuilder.create().loginByPlatform(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.result != null) {
                    if(response.result!!.status == "active"){
                        LoginInfoManager.getInstance().member = response.result
                        LoginInfoManager.getInstance().save()
                        getNotSignedTermsList()
                    }else if(response.result!!.status == "waitingLeave"){
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

    private fun getNotSignedTermsList(){
        showProgress("")
        ApiBuilder.create().getNotSignedList().setCallback(object :PplusCallback<NewResultResponse<ListResultResponse<Terms>>>{
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if(response?.result != null && response.result!!.list != null){
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
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            mGoogleSignListener.handleSignInResult(task.getResult(ApiException::class.java))
        } else {
            hideProgress()
        }
    }
}