package com.pplus.luckybol.apps.wallet.ui

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.WalletSecureUtil
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityWalletBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call


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
        binding.webviewWallet.settings.setAppCacheEnabled(true)
        binding.webviewWallet.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewWallet.settings.loadWithOverviewMode = true
        binding.webviewWallet.settings.useWideViewPort = true
        binding.webviewWallet.settings.allowContentAccess = true
        binding.webviewWallet.settings.domStorageEnabled = true
        val dir = cacheDir
        if (!dir.exists()) {
            dir.mkdirs()
        }
        binding.webviewWallet.settings.setAppCachePath(dir.path)
        binding.webviewWallet.settings.allowFileAccess = true
        binding.webviewWallet.settings.setSupportMultipleWindows(true)
        binding.webviewWallet.settings.mixedContentMode = 0
        binding.webviewWallet.setLayerType(View.LAYER_TYPE_HARDWARE, null)



        checkUser()


    }

    private fun checkUser() {
        showProgress("")
        ApiBuilder.create().walletDuplicateUser().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                hideProgress()

                if (response?.data != null && response.data == "SUCCESS") {
                    signUp()
                } else {
                    val user = LoginInfoManager.getInstance().user!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("id", user.loginId!!.replace(user.appType + "##", ""))
                    jsonObject.addProperty("password", PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
                    jsonObject.addProperty("key", user.no)
                    jsonObject.addProperty("reqTime", System.currentTimeMillis())
                    val url: String
                    if (Const.API_URL.startsWith("https://api")) {
                        url = "https://lockup.buffwallet.net/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                    } else {
                        url = "http://175.126.82.89:30080/#/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                    }
                    LogUtil.e(LOG_TAG, url)
                    binding.webviewWallet.loadUrl(url)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun signUp() {
        showProgress("")
        val params = HashMap<String, String>()
        params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
        ApiBuilder.create().walletSignUp(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                hideProgress()

                if (response?.data != null && response.data == "SUCCESS") {
                    val user = LoginInfoManager.getInstance().user!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("id", user.loginId!!.replace(user.appType + "##", ""))
                    jsonObject.addProperty("password", PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
                    jsonObject.addProperty("key", user.no)
                    jsonObject.addProperty("reqTime", System.currentTimeMillis())
                    val url: String
                    if (Const.API_URL.startsWith("https://api")) {
                        url = "https://lockup.buffwallet.net/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                    } else {
                        url = "http://175.126.82.89:30080/#/bunny/login?data=" + WalletSecureUtil.urlEncode(jsonObject.toString())
                    }
                    LogUtil.e(LOG_TAG, url)
                    binding.webviewWallet.loadUrl(url)
                } else {
                    showAlert(R.string.msg_error_wallet)
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_wallet), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}