package com.pplus.luckybol.apps.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.PopupMange
import com.pplus.luckybol.databinding.ActivityMainBannerDetailBinding


class MainBannerDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMainBannerDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityMainBannerDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val popup = intent.getParcelableExtra<PopupMange>(Const.DATA)!!

        setTitle(popup.title)

        binding.webviewMainBannerDetail.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(view: WebView?,
                                        isDialog: Boolean,
                                        isUserGesture: Boolean,
                                        resultMsg: Message?): Boolean {
                val result = view!!.hitTestResult
                val data = result.extra
                if (data!!.startsWith("https://coupa.ng")) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
                    startActivity(browserIntent)
                    return true
                }

                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }
        }
        binding.webviewMainBannerDetail.webViewClient = object : WebViewClient() {

            //            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            //                LogUtil.e(LOG_TAG, "host : {}", Uri.parse(url).host)
            //                if (Uri.parse(url).host == "m.cafe.naver.com") {
            //                    // This is my web site, so do not override; let my WebView load the page
            //                    return false
            //                }
            //                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            //                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            //                    startActivity(this)
            //                }
            //                return true
            //            }


        }

        binding.webviewMainBannerDetail.settings.javaScriptEnabled = true
        binding.webviewMainBannerDetail.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewMainBannerDetail.settings.setAppCacheEnabled(true)
        binding.webviewMainBannerDetail.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webviewMainBannerDetail.settings.loadWithOverviewMode = true
        binding.webviewMainBannerDetail.settings.useWideViewPort = true
        binding.webviewMainBannerDetail.settings.allowContentAccess = true
        binding.webviewMainBannerDetail.settings.domStorageEnabled = true
        val dir = cacheDir
        if (!dir.exists()) {
            dir.mkdirs()
        }
        binding.webviewMainBannerDetail.settings.setAppCachePath(dir.path)
        binding.webviewMainBannerDetail.settings.allowFileAccess = true
        binding.webviewMainBannerDetail.settings.setSupportMultipleWindows(true)
        binding.webviewMainBannerDetail.settings.mixedContentMode = 0
        binding.webviewMainBannerDetail.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webviewMainBannerDetail.loadUrl(popup.moveTarget!!)


    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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