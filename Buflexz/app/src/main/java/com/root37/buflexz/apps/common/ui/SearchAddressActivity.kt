package com.root37.buflexz.apps.common.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pplus.utils.part.logs.LogUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.Address
import com.root37.buflexz.databinding.ActivitySearchAddressBinding
import org.json.JSONObject

class SearchAddressActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivitySearchAddressBinding

    override fun getLayoutView(): View {
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.webviewSearchAddress.settings.javaScriptEnabled = true
        binding.webviewSearchAddress.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewSearchAddress.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewSearchAddress.settings.loadWithOverviewMode = true
        binding.webviewSearchAddress.settings.useWideViewPort = true
        binding.webviewSearchAddress.settings.allowContentAccess = true
        binding.webviewSearchAddress.settings.domStorageEnabled = true
        binding.webviewSearchAddress.settings.allowFileAccess = true
        binding.webviewSearchAddress.settings.setSupportMultipleWindows(true)
        binding.webviewSearchAddress.settings.mixedContentMode = 0
        binding.webviewSearchAddress.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webviewSearchAddress.addJavascriptInterface(AndroidBridge(), "Address")
        binding.webviewSearchAddress.webChromeClient = object : WebChromeClient() {

        }
        binding.webviewSearchAddress.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtil.e(LOG_TAG, url)
            }

        }

        binding.webviewSearchAddress.loadUrl("${Const.API_URL}common/address")
    }

    private inner class AndroidBridge {

        private val handler = Handler(Looper.myLooper()!!)

        @JavascriptInterface
        fun select(params: String) {
            LogUtil.e(LOG_TAG, params)

            handler.post{
                val jsonObject = JSONObject(params)
                val address = Address()
                address.addr = jsonObject.optString("addr")
                address.roadAddress = jsonObject.optString("roadAddress")
                address.jibunAddress = jsonObject.optString("jibunAddress")
                address.postno = jsonObject.optString("postno")
                val data = Intent()
                data.putExtra(Const.DATA, address)
                setResult(RESULT_OK, data)
                finish()
            }

        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_search_address), ToolbarOption.ToolbarMenu.LEFT)
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