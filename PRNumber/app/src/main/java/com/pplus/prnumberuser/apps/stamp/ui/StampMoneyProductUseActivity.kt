package com.pplus.prnumberuser.apps.stamp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import com.google.gson.JsonParser
import com.onetwocm.echossserviceprovider.EchossServiceProvider
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.subscription.ui.AlertSubscriptionUseQrActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityStampMoneyProductBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import org.json.JSONObject
import retrofit2.Call
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

class StampMoneyProductUseActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityStampMoneyProductBinding

    override fun getLayoutView(): View {
        binding = ActivityStampMoneyProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var scheme: String? = null
    private var merchantCode: String? = null
    private var userCode: String? = null
    private var licenseId: String? = null
    private var authKey: String? = null
    private var extData: JSONObject? = null

    private var webview: WebView? = null
    private var mPage:Page2? = null
    private var mSubscriptionDownload:SubscriptionDownload? = null
    private var mUsePrice = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        mSubscriptionDownload = intent.getParcelableExtra(Const.SUBSCRIPTION_DOWNLOAD)
        mUsePrice = intent.getIntExtra(Const.PRICE, 0)

        binding.textStampMoneyProductUsePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_use_money_product, FormatUtil.getMoneyType(mUsePrice.toString())))
        val remainPrice = mSubscriptionDownload!!.havePrice!! - mSubscriptionDownload!!.usePrice!!
        binding.textStampMoneyProductRemainPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use, FormatUtil.getMoneyType((remainPrice - mUsePrice).toString())))

        if(Const.API_URL.startsWith("https://api")){
            licenseId = "pe52e0d0bff664079bd127db3872d0b5c"
        }else{
            licenseId = "d902bce6a20ff4efca14217cb9415c5b3"
        }

        userCode = LoginInfoManager.getInstance().user.no.toString()//"U0001"
        scheme = "prnumberstamp"
        merchantCode = mPage!!.echossId
        authKey = ""
        extData = null

        val esp = EchossServiceProvider()
        esp.setLanguage(esp.LANGUAGE_CODE_TYPE.KOREAN)
        esp.setBackgroundColor("#000000")
        esp.setBackgroundOpacity("0.0")
        esp.setDescription("")
        esp.setLoadingYn("N")
        esp.setIconYn("N")
        showProgress("")
        esp.getEchossServiceURL(this, esp.REGION_CODE_TYPE.KOREA, licenseId, userCode, scheme, merchantCode, authKey, extData, object : EchossServiceProvider.OnDataListener{
            override fun onSuccess(serviceURL: String?) {
                hideProgress()
                webview = WebView(this@StampMoneyProductUseActivity)
                webview!!.webViewClient = WebViewWebClient()
                webview!!.setBackgroundColor(Color.TRANSPARENT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true)
                }

                val set = webview!!.settings
                set.javaScriptEnabled = true

                binding.layoutStampSubscription.addView(webview, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
                webview!!.alpha = 0f
                showProgress("")
                webview!!.loadUrl(serviceURL!!)
            }

            override fun onError(errorCode: String?, errorMessage: String?) {
                LogUtil.e(LOG_TAG, "[" + errorCode + "] " + errorMessage)
                hideProgress()
                showAlert(errorMessage)
            }
        })
    }

    inner class WebViewWebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(webview: WebView?, request: WebResourceRequest?): Boolean {
            var url = request!!.url.toString()
            LogUtil.e(LOG_TAG, "url1 : " + url)

            try {
                url = URLDecoder.decode(url, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                LogUtil.e("echoss", "base64 decode error", e)
            }
            if (url.startsWith(scheme!!)) {
                val removedPrefixUrl = url.replaceFirst(scheme + "://".toRegex(), "").lowercase(Locale.getDefault())
                if (removedPrefixUrl.startsWith("echoss/initsuccess")) {
                    LogUtil.e("echoss", "success initialize")
                    hideProgress()
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/initerror")) {
                    hideProgress()
                    val arr = getSplitStringsFrom(url)
                    if (arr.size < 2) return true
                    LogUtil.e("echoss", "[" + arr[0] + "] " + arr[1])
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/onbeforestamp")) {
                    LogUtil.e("echoss", "stamp recognized")
                    showProgress("")
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/onstamp")) {
                    hideProgress()
                    val arr = getSplitStringsFrom(url)
                    if (arr.size < 1) return true
                    LogUtil.e("echoss", arr[0])
                    val jsonElement = JsonParser.parseString(arr[0])
                    val token = jsonElement.asJsonObject.get("token").asString
                    use(token)
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/onerror")) {
                    hideProgress()
                    val arr = getSplitStringsFrom(url)
                    if (arr.size < 2) return true
                    LogUtil.e("echoss", "[" + arr[0] + "] " + arr[1])
                    showAlert(arr[1])
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/close")) {
//                    (webview?.parent as RelativeLayout).removeView(webview)
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(webview, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url) //view.clearHistory();
            view.clearCache(true)
        }
    }

    private fun use(token:String){
        val params = HashMap<String, String>()
        params["subscriptionDownloadSeqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["usePrice"] = mUsePrice.toString()
        params["token"] = token

        showProgress("")
        ApiBuilder.create().subscriptionUseWithStamp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_money_product_use_complete)
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if(response?.resultCode == 510){
                    showAlert(R.string.msg_can_not_use_money_product)
                }else if(response?.resultCode == 662){
                    showAlert(R.string.msg_exceed_use_price)
                }
            }
        }).build().call()
    }

    private fun getSplitStringsFrom(url: String): Array<String> {
        val index = url.indexOf("?") + 1
        val data = url.substring(index)
        return data.split("&").toTypedArray()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_qr)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val qrIntent = Intent(this@StampMoneyProductUseActivity, AlertSubscriptionUseQrActivity::class.java)
                        qrIntent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, mSubscriptionDownload)
                        qrIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(qrIntent)
                        overridePendingTransition(R.anim.view_up, R.anim.fix)

                    }
                }
            }
        }
    }
}