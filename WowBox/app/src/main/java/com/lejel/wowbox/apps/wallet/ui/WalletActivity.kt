package com.lejel.wowbox.apps.wallet.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.WalletSecureUtil
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.WalletRes
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityWalletBinding
import retrofit2.Call
import java.util.Date


class WalletActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityWalletBinding

    override fun getLayoutView(): View {
        binding = ActivityWalletBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.webviewWallet.webViewClient = object : WebViewClient() {


        }

        binding.webviewWallet.settings.javaScriptEnabled = true
        binding.webviewWallet.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewWallet.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webviewWallet.settings.loadWithOverviewMode = true
        binding.webviewWallet.settings.useWideViewPort = true
        binding.webviewWallet.settings.allowContentAccess = true
        binding.webviewWallet.settings.domStorageEnabled = true
        binding.webviewWallet.settings.allowFileAccess = true
        binding.webviewWallet.settings.setSupportMultipleWindows(true)
        binding.webviewWallet.settings.mixedContentMode = 0
        binding.webviewWallet.setLayerType(View.LAYER_TYPE_HARDWARE, null)



        checkUser()


    }

    private fun checkUser() {
        val params = HashMap<String, String>()
        params["email"] = LoginInfoManager.getInstance().member!!.authEmail!!
        showProgress("")
        ApiBuilder.create().walletDuplicateUser(params).setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
            override fun onResponse(call: Call<NewResultResponse<WalletRes>>?, response: NewResultResponse<WalletRes>?) {
                hideProgress()
                if (response?.result != null) {
                    if (response.result!!.result == "SUCCESS") { //미가입
                        signUp()
                    } else {
                        val member = LoginInfoManager.getInstance().member!!
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("id", member.authEmail!!)
                        jsonObject.addProperty("password", member.userKey!!)
                        jsonObject.addProperty("key", member.userKey)
                        jsonObject.addProperty("reqTime", PplusCommonUtil.setServerTimeZone(Date()).time)
                        val url = "https://glockup.buffwallet.net/#/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                        LogUtil.e(LOG_TAG, url)
                        binding.webviewWallet.loadUrl(url)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun signUp() {
        showProgress("")
        ApiBuilder.create().walletSignUp().setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
            override fun onResponse(call: Call<NewResultResponse<WalletRes>>?,
                                    response: NewResultResponse<WalletRes>?) {
                hideProgress()

                if (response?.result != null && response.result!!.result == "SUCCESS") {
                    val member = LoginInfoManager.getInstance().member!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("id", member.authEmail!!)
                    jsonObject.addProperty("password", member.userKey!!)
                    jsonObject.addProperty("key", member.userKey)
                    jsonObject.addProperty("reqTime", PplusCommonUtil.setServerTimeZone(Date()).time)
                    val url = "https://glockup.buffwallet.net/#/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                    LogUtil.e(LOG_TAG, url)
                    binding.webviewWallet.loadUrl(url)
                } else {
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<WalletRes>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buff_coin), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}