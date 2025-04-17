package com.pplus.prnumberuser.apps.page.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.page.data.PageAttendanceLogAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPageAttendanceBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import org.json.JSONObject
import retrofit2.Call
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

class PageAttendanceActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {
        return ""
    }

    var mPage: Page? = null
    var mPageAttendance: PageAttendance? = null

    private var scheme: String? = null
    private var merchantCode: String? = null
    private var userCode: String? = null
    private var licenseId: String? = null
    private var authKey: String? = null
    private var extData: JSONObject? = null

    private var webview: WebView? = null

    private lateinit var binding: ActivityPageAttendanceBinding

    override fun getLayoutView(): View {
        binding = ActivityPageAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.DATA)
        binding.recyclerPageAttendanceLog.layoutManager = GridLayoutManager(this, 5)
        binding.recyclerPageAttendanceLog.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_30))
        getPage()
    }

    private fun getPage() {
        val params = HashMap<String, String>()
        params["no"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                hideProgress()
                if (response?.data != null) {
                    mPage = response.data!!
                    binding.textPageAttendanceGoPage.setOnClickListener {
                        val location = IntArray(2)
                        it.getLocationOnScreen(location)
                        val x = location[0] + it.width / 2
                        val y = location[1] + it.height / 2

                        PplusCommonUtil.goPage(this@PageAttendanceActivity, mPage!!, x, y)
                    }

                    binding.textPageAttendancePageName.text = mPage!!.name
                    binding.textPageAttendanceCatchphrase.text = mPage!!.catchphrase
                    binding.textPageAttendancePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(mPage!!.visitPoint.toString())))
                    Glide.with(this@PageAttendanceActivity).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageAttendanceThumbnail)

                    getPageAttendance()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPageAttendance() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.no.toString()
        showProgress("")
        ApiBuilder.create().pageAttendanceSaveAndGet(params).setCallback(object : PplusCallback<NewResultResponse<PageAttendance>> {
            override fun onResponse(call: Call<NewResultResponse<PageAttendance>>?, response: NewResultResponse<PageAttendance>?) {
                hideProgress()
                if (response?.data != null) {
                    mPageAttendance = response.data!!


                    binding.textPageAttendanceTitle.text = getString(R.string.format_word_visit_save_title, mPageAttendance!!.totalCount.toString())

                    when (mPageAttendance!!.status) { // 1:사용중, 2:사용완료, 3:기간만료
                        1 -> {

                            if (StringUtils.isNotEmpty(mPage!!.echossId)) {

                                if (Const.API_URL.startsWith("https://api")) {
                                    licenseId = "pe52e0d0bff664079bd127db3872d0b5c"
                                } else {
                                    licenseId = "d902bce6a20ff4efca14217cb9415c5b3"
                                }

                                userCode = LoginInfoManager.getInstance().user.no.toString() //"U0001"
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
                                esp.getEchossServiceURL(this@PageAttendanceActivity, esp.REGION_CODE_TYPE.KOREA, licenseId, userCode, scheme, merchantCode, authKey, extData, object : EchossServiceProvider.OnDataListener {
                                    override fun onSuccess(serviceURL: String?) {
                                        hideProgress()
                                        webview = WebView(this@PageAttendanceActivity)
                                        webview!!.webViewClient = WebViewWebClient()
                                        webview!!.setBackgroundColor(Color.TRANSPARENT)

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            WebView.setWebContentsDebuggingEnabled(true)
                                        }

                                        val set = webview!!.settings
                                        set.javaScriptEnabled = true

                                        binding.layoutPageAttendanceStamp.addView(webview, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_1920)))
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
                        }
                    }

                    if(mPageAttendance!!.totalCount!! > 1){
                        binding.recyclerPageAttendanceLog.visibility = View.VISIBLE
                        getPageAttendanceLogList()
                    }else{
                        binding.recyclerPageAttendanceLog.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageAttendance>>?, t: Throwable?, response: NewResultResponse<PageAttendance>?) {
                hideProgress()
            }
        }).build().call()
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
                    (webview?.parent as RelativeLayout).removeView(webview)
                    val jsonElement = JsonParser.parseString(arr[0])
                    val token = jsonElement.asJsonObject.get("token").asString
                    attendance(token)
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/onerror")) {
                    hideProgress()
                    val arr = getSplitStringsFrom(url)
                    if (arr.size < 2) return true
                    LogUtil.e("echoss", "[" + arr[0] + "] " + arr[1])
                    showAlert(arr[1])
                    return true
                } else if (removedPrefixUrl.startsWith("echoss/close")) { //                    (webview?.parent as RelativeLayout).removeView(webview)
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

    private fun attendance(token: String) {
        val params = HashMap<String, String>()
        params["pageAttendanceSeqNo"] = mPageAttendance!!.seqNo.toString()
        params["pageSeqNo"] = mPageAttendance!!.pageSeqNo.toString()
        params["token"] = token
        showProgress("")
        ApiBuilder.create().pageAttendanceWithStamp(params).setCallback(object : PplusCallback<NewResultResponse<PageAttendance>> {
            override fun onResponse(call: Call<NewResultResponse<PageAttendance>>?, response: NewResultResponse<PageAttendance>?) {
                hideProgress()
                if (response?.data != null) {
                    val pageAttendance = response.data!!
                    if (pageAttendance.status == 1) {
                        showAlert(R.string.msg_saved)
                    } else if (pageAttendance.status == 2) {
                        val intent = Intent(this@PageAttendanceActivity, AlertPageCashBackCompleteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(Const.CASH, mPage!!.visitPoint)
                        startActivity(intent)
                    }
                }

                getPageAttendance()
            }

            override fun onFailure(call: Call<NewResultResponse<PageAttendance>>?, t: Throwable?, response: NewResultResponse<PageAttendance>?) {
                hideProgress()
                showAlert(R.string.msg_failed_save_point)
                getPageAttendance()
            }
        }).build().call()
    }

    private fun getSplitStringsFrom(url: String): Array<String> {
        val index = url.indexOf("?") + 1
        val data = url.substring(index)
        return data.split("&").toTypedArray()
    }


    private fun getPageAttendanceLogList() {
        val params = HashMap<String, String>()
        params["pageAttendanceSeqNo"] = mPageAttendance!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getPageAttendanceLogList(params).setCallback(object : PplusCallback<NewResultResponse<PageAttendanceLog>> {
            override fun onResponse(call: Call<NewResultResponse<PageAttendanceLog>>?, response: NewResultResponse<PageAttendanceLog>?) {
                hideProgress()
                if (response?.datas != null) {
                    val logList = response.datas!!
                    val adapter = PageAttendanceLogAdapter()
                    binding.recyclerPageAttendanceLog.adapter = adapter
                    val remainCount = mPageAttendance!!.totalCount!! - logList.size
                    for (i in 0 until remainCount) {
                        logList.add(PageAttendanceLog())
                    }
                    adapter.setDataList(logList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageAttendanceLog>>?, t: Throwable?, response: NewResultResponse<PageAttendanceLog>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getPageAttendance()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_around_cash_back), ToolbarOption.ToolbarMenu.LEFT)
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
                        val intent = Intent(this@PageAttendanceActivity, AlertPageAttendanceQrActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(Const.PAGE_ATTENDANCE, mPageAttendance)
                        defaultLauncher.launch(intent)
                        overridePendingTransition(R.anim.view_up, R.anim.fix)

                    }
                }
            }
        }
    }
}