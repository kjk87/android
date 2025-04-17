package com.pplus.prnumberuser.apps.subscription.ui

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
import com.pplus.prnumberuser.apps.subscription.data.SubscriptionLogAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionLog
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivitySubscriptionDownloadDetailBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import org.json.JSONObject
import retrofit2.Call
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

class SubscriptionDownloadDetailActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivitySubscriptionDownloadDetailBinding

    override fun getLayoutView(): View {
        binding = ActivitySubscriptionDownloadDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mSubscriptionDownload: SubscriptionDownload? = null

    private var scheme: String? = null
    private var merchantCode: String? = null
    private var userCode: String? = null
    private var licenseId: String? = null
    private var authKey: String? = null
    private var extData: JSONObject? = null

    private var webview: WebView? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mSubscriptionDownload = intent.getParcelableExtra(Const.DATA)
        binding.recyclerSubscriptionDownloadLog.layoutManager = GridLayoutManager(this, 5)
        binding.recyclerSubscriptionDownloadLog.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_30))

        getSubscriptionDownload()
    }

    private fun getSubscriptionDownload(){
        val params = HashMap<String, String>()
        params["seqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["sort"] = "seqNo,desc"
        showProgress("")
        ApiBuilder.create().getSubscriptionDownloadBySeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionDownload>> {
            override fun onResponse(call: Call<NewResultResponse<SubscriptionDownload>>?, response: NewResultResponse<SubscriptionDownload>?) {
                hideProgress()
                if (response?.data != null) {
                    mSubscriptionDownload = response.data!!
                    setSubscription(mSubscriptionDownload!!.productPrice!!)
                    binding.textSubscriptionDownloadDetailTimes.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_remain_count, FormatUtil.getMoneyType(mSubscriptionDownload!!.haveCount.toString()), FormatUtil.getMoneyType((mSubscriptionDownload!!.haveCount!! - mSubscriptionDownload!!.useCount!!).toString())))

                    val currentDate = LocalDate.now()
                    val expireDate = LocalDate.parse(mSubscriptionDownload!!.expireDate)
                    val remainDay = ChronoUnit.DAYS.between(currentDate, expireDate)
                    binding.textSubscriptionDownloadDetailExpireDate.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_remain_day, FormatUtil.getMoneyType(mSubscriptionDownload!!.expireDate), FormatUtil.getMoneyType(remainDay.toString())))
                    binding.textSubscriptionDownloadDetailContents.text = mSubscriptionDownload!!.useCondition
                    binding.layoutSubscriptionDownloadStamp2.visibility = View.GONE
                    binding.textSubscriptionDownloadDetailUse.visibility = View.GONE
                    when(mSubscriptionDownload!!.status){ // 1:사용중, 2:사용완료, 3:기간만료
                        1->{
                            binding.textSubscriptionDownloadDetailStatus.visibility = View.GONE

                            binding.layoutSubscriptionDownloadDetailComplete.visibility = View.GONE

                            val page = mSubscriptionDownload!!.productPrice!!.page!!
                            if(StringUtils.isNotEmpty(page.echossId)){
                                binding.layoutSubscriptionDownloadStamp2.visibility = View.VISIBLE

                                if(Const.API_URL.startsWith("https://api")){
                                    licenseId = "pe52e0d0bff664079bd127db3872d0b5c"
                                }else{
                                    licenseId = "d902bce6a20ff4efca14217cb9415c5b3"
                                }

                                userCode = LoginInfoManager.getInstance().user.no.toString()//"U0001"
                                scheme = "prnumberstamp"
                                merchantCode = page.echossId
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
                                esp.getEchossServiceURL(this@SubscriptionDownloadDetailActivity, esp.REGION_CODE_TYPE.KOREA, licenseId, userCode, scheme, merchantCode, authKey, extData, object : EchossServiceProvider.OnDataListener{
                                    override fun onSuccess(serviceURL: String?) {
                                        hideProgress()
                                        webview = WebView(this@SubscriptionDownloadDetailActivity)
                                        webview!!.webViewClient = WebViewWebClient()
                                        webview!!.setBackgroundColor(Color.TRANSPARENT)

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            WebView.setWebContentsDebuggingEnabled(true)
                                        }

                                        val set = webview!!.settings
                                        set.javaScriptEnabled = true

                                        binding.layoutSubscriptionDownloadStamp.addView(webview, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_1920)))
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
                            }else{
                                binding.textSubscriptionDownloadDetailUse.visibility = View.VISIBLE
                                binding.textSubscriptionDownloadDetailUse.setOnClickListener {
                                    val intent = Intent(this@SubscriptionDownloadDetailActivity, CheckUseSubscriptionAuthCodeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    intent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, mSubscriptionDownload)
                                    defaultLauncher.launch(intent)
                                }
                            }
                        }
                        2->{
                            binding.textSubscriptionDownloadDetailStatus.visibility = View.VISIBLE
                            binding.textSubscriptionDownloadDetailStatus.setText(R.string.word_use_complete)
                            binding.layoutSubscriptionDownloadDetailComplete.visibility = View.VISIBLE
                            val output = SimpleDateFormat("yyyy-MM-dd")
                            binding.textSubscriptionDownloadDetailCompleteDate.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mSubscriptionDownload!!.completeDatetime))
                        }
                        3->{
                            binding.textSubscriptionDownloadDetailStatus.visibility = View.VISIBLE
                            binding.textSubscriptionDownloadDetailStatus.setText(R.string.word_expire)
                            binding.layoutSubscriptionDownloadDetailComplete.visibility = View.GONE

//                            val output = SimpleDateFormat("yyyy.MM.dd")
//                            holder.text_date.text = output.format(item.expireDate)
                        }
                    }



                    getSubscriptionLogList()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubscriptionDownload>>?, t: Throwable?, response: NewResultResponse<SubscriptionDownload>?) {
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
                    useSubscription(token)
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

    private fun useSubscription(token:String){
        val params = HashMap<String, String>()
        params["subscriptionDownloadSeqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["useCount"] = "1"
        params["token"] = token
        showProgress("")
        ApiBuilder.create().subscriptionUseWithStamp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_subscription_use_complete)
                getSubscriptionDownload()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if(response?.resultCode == 510){
                    showAlert(R.string.msg_can_not_use_subscription)
                }else if(response?.resultCode == 662){
                    showAlert(R.string.msg_exceed_use_count)
                }
                getSubscriptionDownload()
            }
        }).build().call()
    }

    private fun getSplitStringsFrom(url: String): Array<String> {
        val index = url.indexOf("?") + 1
        val data = url.substring(index)
        return data.split("&").toTypedArray()
    }

    private fun setSubscription(item: ProductPrice){

        if(item.isSubscription != null && item.isSubscription!!){
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_4694fb_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.white))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_999999))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_count, mSubscriptionDownload!!.haveCount.toString()))
        }else{
            binding.itemMainSubscription.layoutMainSubscriptionEnd.setBackgroundResource(R.drawable.bg_ffcf5c_right_radius_30)
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.setTextColor(ResourceUtil.getColor(this, R.color.color_4a3606))
            binding.itemMainSubscription.textMainSubscriptionDesc.text = getString(R.string.word_remain_money_manage_type)
        }

        binding.itemMainSubscription.textMainSubscriptionName.text = mSubscriptionDownload!!.name
        binding.textSubscriptionDownloadDetailPageName.text = item.page!!.name

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
            } else {
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.VISIBLE
            }

        } else {
            binding.itemMainSubscription.textMainSubscriptionOriginPrice.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.VISIBLE
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.text = PplusCommonUtil.fromHtml(getString(R.string.html_percent_unit2, item.discountRatio!!.toInt().toString()))
        } else {
            binding.itemMainSubscription.textMainSubscriptionDiscountRatio.visibility = View.GONE
        }
        binding.itemMainSubscription.textMainSubscriptionSalePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
    }

    private fun getSubscriptionLogList(){
        val params = HashMap<String, String>()
        params["subscriptionDownSeqNo"] = mSubscriptionDownload!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getSubscriptionLogListBySubscriptionDownloadSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionLog>> {
            override fun onResponse(call: Call<NewResultResponse<SubscriptionLog>>?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
                if(response?.datas != null){
                    val logList = response.datas!!
                    val adapter = SubscriptionLogAdapter()
                    binding.recyclerSubscriptionDownloadLog.adapter = adapter
                    val remainCount = mSubscriptionDownload!!.haveCount!! - logList.size
                    for(i in 0 until remainCount){
                        logList.add(SubscriptionLog())
                    }
                    adapter.setDataList(logList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubscriptionLog>>?, t: Throwable?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
            }
        }).build().call()
    }
    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getSubscriptionDownload()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_retention_prepayment_voucher), ToolbarOption.ToolbarMenu.LEFT)
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
                        val qrIntent = Intent(this@SubscriptionDownloadDetailActivity, AlertSubscriptionUseQrActivity::class.java)
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