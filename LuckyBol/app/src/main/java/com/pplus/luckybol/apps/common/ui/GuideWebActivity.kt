package com.pplus.luckybol.apps.common.ui

import android.os.Bundle
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityGuideWebBinding

class GuideWebActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGuideWebBinding

    override fun getLayoutView(): View {
        binding = ActivityGuideWebBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.webviewGuide.settings.javaScriptEnabled = true
        // JavaScript의 window.open 허용
        binding.webviewGuide.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewGuide.settings.setAppCacheEnabled(false)
        binding.webviewGuide.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webviewGuide.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                showAlert(message)
                return false
            }
        }

        binding.webviewGuide.loadUrl("http://web.prnumber.com/guide/")
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_bol_use_method), ToolbarOption.ToolbarMenu.LEFT)
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
