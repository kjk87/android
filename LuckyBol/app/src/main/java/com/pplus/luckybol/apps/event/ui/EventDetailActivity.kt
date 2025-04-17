package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.BolChargeAlertActivity
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.apps.main.ui.PadActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResult
import com.pplus.luckybol.core.network.model.dto.No
import com.pplus.luckybol.core.network.model.request.params.ParamsJoinEvent
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityEventDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main(brand_event)_detail"
    }

    private lateinit var binding: ActivityEventDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEvent: Event? = null
    var mIsEnable = true

    override fun initializeView(savedInstanceState: Bundle?) {

        mEvent = intent.getParcelableExtra(Const.DATA)

        // JavaScript 허용
        binding.webviewEventDetail.settings.javaScriptEnabled = true
        // JavaScript의 window.open 허용
        binding.webviewEventDetail.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webviewEventDetail.settings.setAppCacheEnabled(false)
        binding.webviewEventDetail.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webviewEventDetail.settings.domStorageEnabled = true
        binding.webviewEventDetail.settings.loadsImagesAutomatically = true
        //        if(OsUtil.isLollipop()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.webviewEventDetail.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        binding.webviewEventDetail.addJavascriptInterface(AndroidBridge(), "EventScript")
        binding.webviewEventDetail.webViewClient = WebViewClient()
        binding.webviewEventDetail.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?,
                                   url: String?,
                                   message: String?,
                                   result: JsResult?): Boolean {
                showAlert(message)
                return false
            }

        }

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        // web client 를 chrome 으로 설정
        //        webview_event_detail.webChromeClient = WebChromeClient()

        //        text_event_detail_join.setOnClickListener {
        //
        //            if (!mIsEnable) {
        //                ToastUtil.show(this, R.string.msg_can_not_join_time_event)
        //                return@setOnClickListener
        //            }
        //
        //            if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
        //                val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent!!.winAnnounceDate).time
        //                if (System.currentTimeMillis() > winAnnounceDate) {
        //                    val intent = Intent(this, EventImpressionActivity::class.java)
        //                    intent.putExtra(Const.DATA, mEvent)
        //                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //                    startActivity(intent)
        //
        //                    return@setOnClickListener
        //                }
        //            }
        //
        //            if (mEvent!!.primaryType.equals(EventType.PrimaryType.insert.name)) {
        //                val intent = Intent(this, EventPadActivity::class.java)
        //                intent.putExtra(Const.NUMBER, mEvent!!.virtualNumber)
        //                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //                startActivityForResult(intent, Const.REQ_PAD)
        //                overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
        //            }
        ////            else if (mEvent!!.primaryType.equals(EventType.PrimaryType.join.name)) {
        ////                joinEvent(mEvent!!, null)
        ////            }
        //        }

        //        text_event_detail_join.visibility = View.GONE

        getEvent()
    }

    private fun getEvent() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                hideProgress()
                mEvent = response!!.data
                if (mEvent != null) {
                    setData()
                }


            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun setData() {
        binding.webviewEventDetail.loadUrl(mEvent!!.eventLink!!)

        var isWinAnnounce = false

        val currentMillis = System.currentTimeMillis()
        if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
            val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent!!.winAnnounceDate).time
            if (currentMillis > winAnnounceDate) {
                isWinAnnounce = true
                //                text_event_detail_join.text = getString(R.string.msg_confirm_event_win)
            }
        }

        if (!isWinAnnounce && mEvent!!.secondaryType.equals(EventType.SecondaryType.time.name) && mEvent!!.displayTimeList!!.isNotEmpty()) {

            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = sdf.format(Date(currentMillis)).split(":")
            val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
            var endDisplayMillis = 0L
            for (time in mEvent!!.displayTimeList!!) {
                val startSecond = (time.start.substring(0, 2).toInt() * 60 * 60) + (time.start.substring(2).toInt() * 60)
                val endSecond = (time.end.substring(0, 2).toInt() * 60 * 60) + (time.end.substring(2).toInt() * 60)

                if (currentSecond in startSecond..endSecond) {
                    endDisplayMillis = endSecond * 1000L
                    break
                }
            }

            if (endDisplayMillis > 0) {
                val remainMillis = endDisplayMillis - (currentSecond * 1000)
                if (remainMillis <= 0) {
                    mIsEnable = false
                    //                    text_event_detail_join.setBackgroundColor(ResourceUtil.getColor(this@EventDetailActivity, R.color.color_d3d3d3))
                }

            } else {
                mIsEnable = false
                //                text_event_detail_join.setBackgroundColor(ResourceUtil.getColor(this@EventDetailActivity, R.color.color_d3d3d3))
            }
        }
    }

    private inner class AndroidBridge {

        @JavascriptInterface
        fun sendMessage(properties: String?) {
            LogUtil.e(LOG_TAG, "sendMessage")

            if (mEvent!!.primaryType.equals(EventType.PrimaryType.insert.name)) {
                val intent = Intent(this@EventDetailActivity, PadActivity::class.java)
                //                val intent = Intent(this@EventDetailActivity, AppMainActivity::class.java)
                intent.putExtra(Const.KEY, Const.PAD)
                intent.putExtra(Const.NUMBER, mEvent!!.virtualNumber)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)

            } else {
                if (mEvent!!.reward != null && mEvent!!.reward!! < 0) {

                    if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(mEvent!!.reward!!)) {
                        val intent = Intent(this@EventDetailActivity, PlayAlertActivity::class.java)
                        intent.putExtra(Const.DATA, mEvent)
                        intent.putExtra(Const.PROPERTIES, properties)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        joinAlertLauncher.launch(intent)
                    } else {

                        val intent = Intent(this@EventDetailActivity, BolChargeAlertActivity::class.java)
                        intent.putExtra(Const.EVENT, mEvent)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        cashExchangeLauncher.launch(intent)
                    }
                } else {
                    if (StringUtils.isNotEmpty(properties)) {
                        joinEvent(mEvent!!, JsonParser.parseString(properties).asJsonObject)
                    } else {
                        joinEvent(mEvent!!)
                    }
                }
            }
        }
    }

    fun joinEvent(event: Event, properties: JsonObject) {
        val params = ParamsJoinEvent()
        params.event = No(event.no)
        params.properties = properties

        showProgress("")

        ApiBuilder.create().joinWithPropertiesEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(supportFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(this@EventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@EventDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {
                hideProgress()

                if(response?.data != null && StringUtils.isNotEmpty(response.data!!.joinDate)){
                    PplusCommonUtil.showEventAlert(this@EventDetailActivity, response.resultCode, event, response.data!!.joinDate, response.data!!.joinTerm, response.data!!.remainSecond, null)
                }else{
                    PplusCommonUtil.showEventAlert(this@EventDetailActivity, response?.resultCode!!, event, null)
                }
            }
        }).build().call()
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()

        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(supportFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(this@EventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@EventDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {
                hideProgress()


                if(response?.data != null && StringUtils.isNotEmpty(response.data!!.joinDate)){
                    PplusCommonUtil.showEventAlert(this@EventDetailActivity, response.resultCode, event, response.data!!.joinDate, response.data!!.joinTerm, response.data!!.remainSecond, null)
                }else{
                    PplusCommonUtil.showEventAlert(this@EventDetailActivity, response?.resultCode!!, event, null)
                }
            }
        }).build().call()
    }

    val cashExchangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
            }
        })
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val properties = result.data!!.getStringExtra(Const.PROPERTIES)
                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(mEvent!!.reward!!)) {
                    if (StringUtils.isNotEmpty(properties)) {
                        joinEvent(mEvent!!, JsonParser.parseString(properties).asJsonObject)
                    } else {
                        joinEvent(mEvent!!)
                    }
                } else {
                    //                            val intent = Intent(this, AlertBolLackActivity::class.java)
                    //                            intent.putExtra(Const.EVENT, mEvent!!)
                    //                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    //                            startActivity(intent)

                    val intent = Intent(this, BolChargeAlertActivity::class.java)
                    intent.putExtra(Const.EVENT, mEvent)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    cashExchangeLauncher.launch(intent)
                }
            }

        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_event), ToolbarOption.ToolbarMenu.LEFT)
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
