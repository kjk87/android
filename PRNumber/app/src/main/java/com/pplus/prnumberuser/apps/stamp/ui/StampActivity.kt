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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonParser
import com.onetwocm.echossserviceprovider.EchossServiceProvider
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.my.ui.AlertSaveQrActivity
import com.pplus.prnumberuser.apps.page.data.HashTagValueAdapter
import com.pplus.prnumberuser.apps.page.ui.AlertFirstBenefitCompleteActivity
import com.pplus.prnumberuser.apps.page.ui.AlertFirstBenefitQrActivity
import com.pplus.prnumberuser.apps.page.ui.AlertPageCashBackCompleteActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.VisitorPointGiveHistory
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityStampBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import org.json.JSONObject
import retrofit2.Call
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

class StampActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityStampBinding

    override fun getLayoutView(): View {
        binding = ActivityStampBinding.inflate(layoutInflater)
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
    private var mPage:Page? = null
    private var mType:String? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        mType = intent.getStringExtra(Const.TYPE)

        when(mType){
            "visit" -> {
                binding.textStampDesc.text = getString(R.string.msg_stamp_visit_point_desc)
                binding.layoutStampVisitPoint.visibility = View.VISIBLE
                binding.layoutStampSnsPoint.visibility = View.GONE
                binding.layoutStampBenefit.visibility = View.GONE

                binding.textStampPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(mPage!!.visitPoint.toString())))

                var minPrice = 6000
                if (mPage!!.visitMinPrice != null && mPage!!.visitMinPrice!! > 0) {
                    minPrice = mPage!!.visitMinPrice!!
                }

                binding.textStampPointMinPriceDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_visit_save_desc, mPage!!.name, FormatUtil.getMoneyType(minPrice.toString())))
            }
            "sns" -> {
                binding.textStampDesc.text = getString(R.string.msg_stamp_visit_point_desc)
                binding.layoutStampVisitPoint.visibility = View.GONE
                binding.layoutStampSnsPoint.visibility = View.VISIBLE
                binding.layoutStampBenefit.visibility = View.GONE

                binding.textStampSnsPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(mPage!!.snsPoint.toString())))
                binding.textStampSnsDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_sns_save_desc, mPage!!.name))

                val keywordList = ArrayList<String>()
                if (StringUtils.isNotEmpty(mPage!!.hashtag)) {
                    keywordList.addAll(mPage!!.hashtag!!.split(",").toMutableList())
                }

                val layoutManager = GridLayoutManager(this, 30)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                    override fun getSpanSize(position: Int): Int {

                        val keyWord = keywordList[position]

                        return if (keyWord.length < 3) {
                            7
                        } else if (keyWord.length < 4) {
                            8
                        } else if (keyWord.length < 5) {
                            9
                        } else if (keyWord.length < 6) {
                            10
                        } else if (keyWord.length < 7) {
                            12
                        } else if (keyWord.length < 8) {
                            13
                        } else if (keyWord.length < 10) {
                            14
                        } else {
                            20
                        }
                    }
                }

                binding.recyclerStampSnsHashtag.layoutManager = layoutManager
                val adapter = HashTagValueAdapter()
                adapter.setDataList(keywordList)
                binding.recyclerStampSnsHashtag.adapter = adapter
            }
            "benefit"->{
                binding.textStampDesc.text = getString(R.string.msg_stamp_first_benefit_desc)

                binding.layoutStampVisitPoint.visibility = View.GONE
                binding.layoutStampSnsPoint.visibility = View.GONE
                binding.layoutStampBenefit.visibility = View.VISIBLE

                binding.textStampBenefitDesc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_first_benefit_desc, FormatUtil.getMoneyType(mPage!!.name)))
                binding.textStampBenefitDesc2.text = mPage!!.benefit
            }
        }

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
        initStamp()
    }

    fun initStamp(){
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
                webview = WebView(this@StampActivity)
                webview!!.webViewClient = WebViewWebClient()
                webview!!.setBackgroundColor(Color.TRANSPARENT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true)
                }

                val set = webview!!.settings
                set.javaScriptEnabled = true

                binding.layoutStamp.addView(webview, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))
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
//                    (webview?.parent as RelativeLayout).removeView(webview)
                    val jsonElement = JsonParser.parseString(arr[0])
                    val token = jsonElement.asJsonObject.get("token").asString
                    givePoint(token)
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

    private fun givePoint(token:String){
        val params = VisitorPointGiveHistory()
        params.pageSeqNo = mPage!!.no
        params.senderSeqNo = mPage!!.user!!.no
        params.receiverSeqNo = LoginInfoManager.getInstance().user.no
        params.type = mType
        params.token = token
        when (mType) {
            "visit" -> {
                params.price = mPage!!.visitPoint
            }
            "sns" -> {
                params.price = mPage!!.snsPoint
            }
            "benefit" -> {
                params.price = 0
            }
        }

        params.isPayment = false
//        params.authCode = authCode

        showProgress("")
        ApiBuilder.create().postVisitorGivePointWithStamp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                when (mType) {
                    "benefit" -> {
                        val intent = Intent(this@StampActivity, AlertFirstBenefitCompleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                    else -> {
                        val intent = Intent(this@StampActivity, AlertPageCashBackCompleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(Const.CASH, params.price)
                        startActivity(intent)
                    }
                }
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.resultCode == 504) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    when (mType) {
                        "sns" -> {
                            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_already_sns_point_saved, mPage!!.name), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                        }
                        "benefit" -> {
                            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_already_first_benefit_received, mPage!!.name), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                        }
                    }

                    builder.builder().show(this@StampActivity)
                } else {
                    if (mType == "benefit") {
                        showAlert(R.string.msg_failed_receive_first_benefit)
                    } else {
                        showAlert(R.string.msg_failed_save_point)
                    }
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
        val type = intent.getStringExtra(Const.TYPE)
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
                        val type = intent.getStringExtra(Const.TYPE)
                        if (type == "benefit") {
                            val intent = Intent(this@StampActivity, AlertFirstBenefitQrActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                        } else {
                            val intent = Intent(this@StampActivity, AlertSaveQrActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                        }

                    }
                }
            }
        }
    }
}