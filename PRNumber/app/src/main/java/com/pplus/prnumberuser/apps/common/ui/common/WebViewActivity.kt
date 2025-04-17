package com.pplus.prnumberuser.apps.common.ui.common

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Terms
import com.pplus.prnumberuser.databinding.ActivityWebviewBinding
import com.pplus.prnumberuser.databinding.ItemDropboxCategoryBinding
import com.pplus.utils.part.logs.LogUtil
import java.util.*

/**
 * Created by ksh on 2016-08-30.
 */
class WebViewActivity : BaseActivity(), ImplToolbar {
    private var mUrl: String? = null
    private var isRightMenuClick = false // Right Menu를 클릭 여부

    //    private boolean isRightMenuSet = true; // Right Menu 활성화 여부
    private var mCurrentIndex = 0 // RIght Menu를 위한 Index
    private var mTermsList: ArrayList<Terms>? = null
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityWebviewBinding

    override fun getLayoutView(): View {
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mUrl = intent.getStringExtra(Const.WEBVIEW_URL)
        binding.svTerms.visibility = View.GONE
        mTermsList = intent.getParcelableArrayListExtra(Const.TERMS_LIST)
        if (!TextUtils.isEmpty(mUrl)) {
            binding.webviewLayout.webViewClient = WebClient()
            val set = binding.webviewLayout.settings //            if(OsUtil.isLollipop()) {
            //                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            //                CookieManager.getInstance().setAcceptCookie(true);
            //                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            //            }
            set.cacheMode = WebSettings.LOAD_NO_CACHE
            set.javaScriptEnabled = true
            set.loadWithOverviewMode = true //set.setUseWideViewPort(true);
            val chromeClient = webChromeClient()
            binding.webviewLayout.webChromeClient = chromeClient
            /**
             * 화면 포트에 맞도록 웹 설정을 변경합니다.
             * v 1.1.1 버전 웹 퍼블리셔 이슈로 인하여 해당 오류 사항을 수정하지않습니다.
             *
             * mWebView.getSettings().setUseWideViewPort(true);
             * mWebView.getSettings().setLoadWithOverviewMode(true);
             * mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
             *
             */
            val webViewInterface = WebViewInterface(this)
            binding.webviewLayout.addJavascriptInterface(webViewInterface, WebViewInterface.WEBVIEW_JS_INTERFACE_NAME)
            loadUrl()
        }
    }

    fun loadUrl() {
        binding.webviewLayout.loadUrl(mUrl + "?timestamp=" + System.currentTimeMillis())
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val title = intent.getStringExtra(Const.TITLE)
        toolbarOption.initializeDefaultToolbar(title, ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, R.drawable.ic_setting_down)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> onBackPressed()
                    ToolbarMenu.RIGHT -> {
                        binding.llTerms.removeAllViews()
                        if (!isRightMenuClick) {
                            binding.svTerms.visibility = View.VISIBLE
                            getToolbarOption().setRIghtImageRes(R.drawable.ic_setting_up)
                            if (mTermsList != null && mTermsList!!.size > 0) {
                                var i = 0
                                while (i < mTermsList!!.size) {
                                    if (i != mCurrentIndex) {
                                        val dropboxCategoryBinding = ItemDropboxCategoryBinding.inflate(layoutInflater)
                                        val tv = dropboxCategoryBinding.textDropboxCategoryName
                                        tv.text = mTermsList!![i].subject
                                        tv.tag = i
                                        tv.setTag(tv.id, mTermsList!![i].url)
                                        tv.setOnClickListener(menuClickListener)
                                        binding.llTerms.addView(dropboxCategoryBinding.root)
                                    }
                                    i++
                                }
                            }
                        } else {
                            binding.svTerms.visibility = View.GONE
                            getToolbarOption().setRIghtImageRes(R.drawable.ic_setting_down)
                        }
                        isRightMenuClick = !isRightMenuClick
                    }
                }
            }
        }
    }

    var menuClickListener = View.OnClickListener { view ->
        isRightMenuClick = !isRightMenuClick // 이미지 변경
        getToolbarOption().setRIghtImageRes(R.drawable.ic_setting_up)
        binding.svTerms.visibility = View.GONE // Webview 처리
        val i = view.tag as Int
        mCurrentIndex = i
        binding.svTerms.visibility = View.GONE

        // Text 변경
        if (mTermsList != null && mTermsList!!.size > 0 && mTermsList!!.size > i) {
            setTitle(mTermsList!![i].subject)
        }
        mUrl = view.getTag(view.id) as String
        LogUtil.e(LOG_TAG, mUrl)
        loadUrl()
    }

    internal inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return if (url != null && url.startsWith("http")) {
                view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                true
            } else {
                false
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            LogUtil.d(TAG, "onPageStarted url = {}", url)
            showProgress("")
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            LogUtil.d(TAG, "onPageFinished url = {}", url)
            hideProgress()
        }
    }

    inner class webChromeClient : WebChromeClient() {
        override fun onCreateWindow(view: WebView, dialog: Boolean, userGesture: Boolean, resultMsg: Message): Boolean { // TODO Auto-generated method stub
            return super.onCreateWindow(view, dialog, userGesture, resultMsg)
        }

        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.setContents(message)
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> result.confirm()
                        EVENT_ALERT.LEFT -> {
                        }
                    }
                }
            }).builder().show(this@WebViewActivity)
            return true
        }

        override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.setContents(getString(R.string.msg_question_logout))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> result.confirm()
                        EVENT_ALERT.LEFT -> result.cancel()
                    }
                }
            }).builder().show(this@WebViewActivity)
            return true
        }
    }

    companion object {
        private val TAG = WebViewActivity::class.java.simpleName
    }
}