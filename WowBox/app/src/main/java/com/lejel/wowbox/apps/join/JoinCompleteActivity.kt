package com.lejel.wowbox.apps.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.login.WithdrawalCancelAuthActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityJoinCompleteBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class JoinCompleteActivity : BaseActivity() {

    private lateinit var binding: ActivityJoinCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityJoinCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        val referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response = referrerClient.installReferrer

                        LogUtil.e(LOG_TAG, "InstallReferrerResponse.OK : " + response.installReferrer)

                        val referrer = response.installReferrer
                        if (StringUtils.isNotEmpty(referrer)) {
                            val referrers = referrer.split("&").toTypedArray()
                            for (referrerValue in referrers) {
                                val keyValue = referrerValue.split("=").toTypedArray()
                                if (keyValue[0] == "recommendKey") {
                                    val recommendKey = keyValue[1]
                                    if (StringUtils.isNotEmpty(recommendKey)) {
                                        binding.editInputRecommender.setText(recommendKey)
                                        LogUtil.e("recommendKey", "recommendKey : {}", recommendKey)
                                    }
                                }
                            }
                        }

                        referrerClient.endConnection()
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { // API not available on the current Play Store app
                        LogUtil.e(LOG_TAG, "NOT_SUPPORTED")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { // Connection could not be established
                        LogUtil.e(LOG_TAG, "SERVICE_UNAVAILABLE")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                LogUtil.e(LOG_TAG, "onInstallReferrerServiceDisconnected") // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

        binding.editInputRecommender.setSingleLine()

        val userKey = intent.getStringExtra(Const.USER_KEY)!!
        loginByUserKey(userKey)

    }

    private fun loginByUserKey(userKey: String) {
        val params = HashMap<String, String>()
        params["userKey"] = userKey
        params["device"] = PplusCommonUtil.getDeviceID()
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                hideProgress()
                if (response?.result != null) {
                    if (response.result!!.status == "active") {
                        LoginInfoManager.getInstance().member = response.result
                        LoginInfoManager.getInstance().save()

                        binding.textJoinComplete.setOnClickListener {
                            val recommendKey = binding.editInputRecommender.text.toString().trim()
                            if(StringUtils.isNotEmpty(recommendKey)){
                                inputRecommender(recommendKey)
                            }else{
                                setResult(RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun inputRecommender(recommendKey:String){
        val params = HashMap<String, String>()
        params["recommendeeKey"] = recommendKey
        showProgress("")
        ApiBuilder.create().inputRecommender(params).setCallback(object  : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                setResult(RESULT_OK)
                finish()

            }
        }).build().call()
    }

    val joinCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }
}