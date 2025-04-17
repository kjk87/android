package com.pplus.luckybol.apps.event.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityEventLandingBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventLandingActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityEventLandingBinding

    override fun getLayoutView(): View {
        binding = ActivityEventLandingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var isCalled = false

    override fun initializeView(savedInstanceState: Bundle?) {
        val eventSeqNo = intent.getLongExtra(Const.NO, 0)
        val url = intent.getStringExtra(Const.URL)
        binding.webviewEventLanding.settings.javaScriptEnabled = true
        // JavaScript의 window.open 허용
        binding.webviewEventLanding.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewEventLanding.settings.setSupportMultipleWindows(true)
        binding.webviewEventLanding.settings.setAppCacheEnabled(false)
        binding.webviewEventLanding.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webviewEventLanding.webChromeClient = object : WebChromeClient() {

            override fun onJsAlert(view: WebView?,
                                   url: String?,
                                   message: String?,
                                   result: JsResult?): Boolean {
                showAlert(message)
                return false

            }


        }
        isCalled = false

        binding.webviewEventLanding.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                LogUtil.e(LOG_TAG, url)
                if (StringUtils.isNotEmpty(url) && url!!.contains("http://m.tammys.co.kr/member/join_ok.php")) {
                    //포인트지급
                    if(!isCalled){
                        isCalled = true
                        payPoint(eventSeqNo)
                    }

                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtil.e(LOG_TAG, url)
            }
        }

        binding.webviewEventLanding.loadUrl(url!!)
    }

    private fun payPoint(eventSeqNo: Long) {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = eventSeqNo.toString()
        ApiBuilder.create().eventPayPoint(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    override fun onBackPressed() {
        if(binding.webviewEventLanding.canGoBack()){
            binding.webviewEventLanding.goBack()
        }else{
            super.onBackPressed()
        }

    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    finish()
                }
                else -> {}
            }
        }
    }
}