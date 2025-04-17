//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.webkit.CookieManager
//import android.webkit.JavascriptInterface
//import android.webkit.WebSettings
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_goods_pg.*
//
//class GoodsPgActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_pg
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        val url = intent.getStringExtra(Const.URL)
//
////        web_view_pg.setWebChromeClient(ChromeClient())
////        sampleWebView.setWebViewClient(SampleWebView())
//        web_view_pg.addJavascriptInterface(AndroidBridge(), "GoodsPG")
//        web_view_pg.settings.javaScriptEnabled = true
//        web_view_pg.settings.setSavePassword(false)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            web_view_pg.settings.mixedContentMode =WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//            val cookieManager = CookieManager.getInstance()
//            cookieManager.setAcceptCookie(true)
//            cookieManager.setAcceptThirdPartyCookies(web_view_pg, true)
//        }
//        web_view_pg.loadUrl(url)
//    }
//
//    private inner class AndroidBridge {
//
//        @JavascriptInterface
//        fun success(orderId: String) {
//            val handler = Handler()
//
//            handler.post{
//                val data = Intent()
//                data.putExtra(Const.ID, orderId)
//                setResult(Activity.RESULT_OK, data)
//                finish()
//            }
//        }
//
//        @JavascriptInterface
//        fun fail(orderId: String) {
//            val handler = Handler()
//
//            handler.post{
//                setResult(Activity.RESULT_CANCELED)
//                finish()
//            }
//        }
//    }
//}
