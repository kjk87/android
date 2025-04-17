package com.pplus.prnumberbiz.apps

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_use_info.*

class UseInfoActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_menu_guide"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_use_info
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        webview_use_info.loadUrl("https://web.prnumber.com/info/index.html")
        webview_use_info.settings.javaScriptEnabled = true
        webview_use_info.setWebViewClient(object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        })

    }

    override fun onBackPressed() {

        if (webview_use_info.canGoBack()) {
            webview_use_info.goBack()
        } else {
            super.onBackPressed()
        }

    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_use_introduction), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
