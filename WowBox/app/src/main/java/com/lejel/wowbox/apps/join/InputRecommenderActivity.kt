package com.lejel.wowbox.apps.join

import android.os.Bundle
import android.view.View
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityInputRecommenderBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


class InputRecommenderActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityInputRecommenderBinding

    override fun getLayoutView(): View {
        binding = ActivityInputRecommenderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

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

        binding.textInputRecommenderReg.setOnClickListener {

            val recommendeeKey = binding.editInputRecommender.text.toString().trim()
            if(StringUtils.isEmpty(recommendeeKey)){
                showAlert(R.string.msg_input_recommend_key)
                return@setOnClickListener
            }
            inputRecommender(recommendeeKey)
        }
    }

    private fun inputRecommender(recommendeeKey:String){
        val params = HashMap<String, String>()
        params["recommendeeKey"] = recommendeeKey
        showProgress("")
        ApiBuilder.create().inputRecommender(params).setCallback(object  : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}