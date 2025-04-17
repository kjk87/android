package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import android.view.View
import android.webkit.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.data.EventDetailImageAdapter
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.main.ui.PadActivity
import com.pplus.prnumberuser.apps.my.ui.MemberAddressSaveActivity
import com.pplus.prnumberuser.apps.page.ui.Alert3rdPartyInfoTermsActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.ParamsJoinEvent
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.*
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import org.json.JSONObject
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
    var mEventDetailList:List<EventDetail>? = null
    var mContentsArray: SparseArray<View>? = null
    var mEventDetailImageAdapter: EventDetailImageAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mEvent = intent.getParcelableExtra(Const.DATA)

        getEvent()
    }

    val eventAgreeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {

                val event = data.getParcelableExtra<Event>(Const.DATA)!!
                val properties = data.getStringExtra(Const.PROPERTIES)

                if (StringUtils.isNotEmpty(properties)) {
                    joinEvent(event, JsonParser.parseString(properties).asJsonObject)
                } else {
                    joinEvent(event)
                }

            }

        }
    }

    private fun getEvent() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params)
            .setCallback(object : PplusCallback<NewResultResponse<Event>> {
                override fun onResponse(
                    call: Call<NewResultResponse<Event>>?,
                    response: NewResultResponse<Event>?
                ) {
                    hideProgress()
                    mEvent = response!!.data
                    if (mEvent != null) {

                        when (mEvent!!.detailType) {
                            2 -> {
                                binding.webviewEventDetail.visibility = View.VISIBLE
                                binding.layoutEventDetail.visibility = View.GONE
                                // JavaScript 허용
                                binding.webviewEventDetail.settings.javaScriptEnabled = true
                                // JavaScript의 window.open 허용
                                binding.webviewEventDetail.settings.javaScriptCanOpenWindowsAutomatically =
                                    true
                                binding.webviewEventDetail.settings.setAppCacheEnabled(false)
                                binding.webviewEventDetail.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                                binding.webviewEventDetail.addJavascriptInterface(
                                    AndroidBridge(),
                                    "EventScript"
                                )
                                binding.webviewEventDetail.webChromeClient = object : WebChromeClient() {
                                }
                                binding.webviewEventDetail.loadUrl(mEvent!!.eventLink!!)
                            }
                            3 -> {
                                binding.webviewEventDetail.visibility = View.GONE
                                binding.layoutEventDetail.visibility = View.VISIBLE

                                if(StringUtils.isNotEmpty(mEvent!!.personalTitle)){
                                    binding.layoutEventDetailTerms.visibility = View.VISIBLE
                                    binding.textEventDetailTermsView.setOnClickListener {
                                        val intent = Intent(this@EventDetailActivity, EventDetailPersonalTermsActivity::class.java)
                                        mEvent!!.personalContents = ""
                                        intent.putExtra(Const.DATA, mEvent)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                    }
                                }else{
                                    binding.layoutEventDetailTerms.visibility = View.GONE
                                }
                                getEventDetailImageList()
                            }
                        }

                        setData()
                    }
                }

                override fun onFailure(
                    call: Call<NewResultResponse<Event>>?,
                    t: Throwable?,
                    response: NewResultResponse<Event>?
                ) {
                    hideProgress()
                }
            }).build().call()
    }

    class CustomBitmapImageViewTarget(view: ImageView?) : DrawableImageViewTarget(view) {

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            super.onResourceReady(resource, transition)
            val rate = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS.toFloat()/resource.intrinsicWidth.toFloat()
            val changedHeight = resource.intrinsicHeight*rate
            view.layoutParams.height = changedHeight.toInt()
        }
    }

    fun getEventDetailImageList() {
        binding.layoutEventDetailImage.removeAllViews()
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEventDetailImageList(params)
            .setCallback(object : PplusCallback<NewResultResponse<EventDetailImage>> {
                override fun onResponse(
                    call: Call<NewResultResponse<EventDetailImage>>?,
                    response: NewResultResponse<EventDetailImage>?
                ) {
                    hideProgress()
                    if(response?.datas != null && response.datas!!.isNotEmpty()){

                        for(item in response.datas!!){
                            val imageBinding = ItemGoodsImageDetailBinding.inflate(layoutInflater)
                            Glide.with(this@EventDetailActivity).load(item.image).into(CustomBitmapImageViewTarget(imageBinding.imageGoodsImageDetail))
                            binding.layoutEventDetailImage.addView(imageBinding.root)
                        }
                    }

                    getEventDetailList()
                }

                override fun onFailure(
                    call: Call<NewResultResponse<EventDetailImage>>?,
                    t: Throwable?,
                    response: NewResultResponse<EventDetailImage>?
                ) {
                    hideProgress()
                    getEventDetailList()
                }
            }).build().call()
    }

    fun getEventDetailList() {

        binding.textEventDetailTitle.text = mEvent!!.detailTitle
        binding.textEventDetailExplain.text = mEvent!!.detailExplain

        if(mEvent!!.useDetailItem != null && mEvent!!.useDetailItem!!){
            binding.layoutEventDetailContents.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params["eventSeqNo"] = mEvent!!.no.toString()
            showProgress("")
            ApiBuilder.create().getEventDetailList(params)
                .setCallback(object : PplusCallback<NewResultResponse<EventDetail>> {
                    override fun onResponse(
                        call: Call<NewResultResponse<EventDetail>>?,
                        response: NewResultResponse<EventDetail>?
                    ) {
                        hideProgress()
                        if(response?.datas != null && response.datas!!.isNotEmpty()){
                            mEventDetailList = response.datas
                            mContentsArray = SparseArray()
                            var i = 0
                            binding.layoutEventDetailContents.removeAllViews()
                            for (eventDetail in mEventDetailList!!){
                                val titleBinding = ItemEventDetailTitleBinding.inflate(layoutInflater)
                                titleBinding.textEventDetailTitle.text = eventDetail.question
                                if(eventDetail.compulsory != null && eventDetail.compulsory!!){
                                    titleBinding.textEventDetailCompulsory.visibility = View.VISIBLE
                                }else{
                                    titleBinding.textEventDetailCompulsory.visibility = View.GONE
                                }
                                when(eventDetail.type){
                                    1->{//단답형
                                        val type1Binding = ItemEventDetailType1Binding.inflate(layoutInflater)
                                        type1Binding.editEventDetailType1Contents.setSingleLine()
                                        if(StringUtils.isNotEmpty(eventDetail.guide)){
                                            type1Binding.editEventDetailType1Contents.hint = eventDetail.guide
                                        }
                                        titleBinding.layoutEventDetailTitleContents.addView(type1Binding.root)
                                        eventDetail.data = type1Binding.editEventDetailType1Contents
                                    }
                                    2->{//장문형
                                        val type2Binding = ItemEventDetailType2Binding.inflate(layoutInflater)
                                        if(StringUtils.isNotEmpty(eventDetail.guide)){
                                            type2Binding.editEventDetailType2Contents.hint = eventDetail.guide
                                        }
                                        titleBinding.layoutEventDetailTitleContents.addView(type2Binding.root)
                                        eventDetail.data = type2Binding.editEventDetailType2Contents
                                    }
                                    else -> {
                                        val params = HashMap<String, String>()
                                        params["eventSeqNo"] = mEvent!!.no.toString()
                                        params["eventDetailSeqNo"] = eventDetail.seqNo.toString()
                                        showProgress("")
                                        ApiBuilder.create().getEventDetailItemList(params)
                                            .setCallback(object : PplusCallback<NewResultResponse<EventDetailItem>> {
                                                override fun onResponse(
                                                    call: Call<NewResultResponse<EventDetailItem>>?,
                                                    response: NewResultResponse<EventDetailItem>?
                                                ) {
                                                    hideProgress()
                                                    if(response?.datas != null && response.datas!!.isNotEmpty()){
                                                        val itemList = response.datas
                                                        when(eventDetail.type){
                                                            3->{//라디오
                                                                val radioList = ArrayList<TextView>()
                                                                for(item in itemList){

                                                                    val type3Binding = ItemEventDetailType3Binding.inflate(layoutInflater)
                                                                    type3Binding.textEventDetailType3Radio.text = item.item
                                                                    radioList.add(type3Binding.textEventDetailType3Radio)

                                                                    type3Binding.textEventDetailType3Radio.setOnClickListener {
                                                                        for(radio in radioList){
                                                                            radio.isSelected = false
                                                                        }
                                                                        type3Binding.textEventDetailType3Radio.isSelected = true
                                                                    }

                                                                    titleBinding.layoutEventDetailTitleContents.addView(type3Binding.root)
                                                                }

                                                                eventDetail.data = radioList
                                                            }
                                                            4->{//드랍박스

                                                                val contents = arrayOfNulls<String>(itemList.size)
                                                                for (i in itemList.indices) {
                                                                    contents[i] = itemList[i].item
                                                                }
                                                                val type4Binding = ItemEventDetailType4Binding.inflate(layoutInflater)
                                                                if(StringUtils.isNotEmpty(eventDetail.guide)){
                                                                    type4Binding.textEventDetailType4Dropbox.hint = eventDetail.guide
                                                                }

                                                                type4Binding.layoutEventDetailType4Dropbox.setOnClickListener {
                                                                    val builder = AlertBuilder.Builder()
                                                                    builder.setTitle(getString(R.string.word_select))
                                                                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                                                                    builder.setContents(*contents)
                                                                    builder.setLeftText(getString(R.string.word_cancel))
                                                                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                                                                        override fun onCancel() {

                                                                        }

                                                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                                                            when (event_alert) {

                                                                                AlertBuilder.EVENT_ALERT.LIST -> {
                                                                                    type4Binding.textEventDetailType4Dropbox.text = contents[event_alert.value-1]
                                                                                }
                                                                            }
                                                                        }
                                                                    }).builder().show(this@EventDetailActivity)
                                                                }
                                                                eventDetail.data = type4Binding.textEventDetailType4Dropbox
                                                                titleBinding.layoutEventDetailTitleContents.addView(type4Binding.root)
                                                            }
                                                            5->{//체크박스
                                                                val checkList = ArrayList<TextView>()
                                                                for(item in itemList){
                                                                    val type5Binding = ItemEventDetailType5Binding.inflate(layoutInflater)
                                                                    type5Binding.textEventDetailType5Check.text = item.item
                                                                    checkList.add(type5Binding.textEventDetailType5Check)

                                                                    type5Binding.textEventDetailType5Check.setOnClickListener {

                                                                        type5Binding.textEventDetailType5Check.isSelected = !(type5Binding.textEventDetailType5Check.isSelected)
                                                                    }

                                                                    titleBinding.layoutEventDetailTitleContents.addView(type5Binding.root)
                                                                }

                                                                eventDetail.data = checkList
                                                            }
                                                        }
                                                    }

                                                }

                                                override fun onFailure(
                                                    call: Call<NewResultResponse<EventDetailItem>>?,
                                                    t: Throwable?,
                                                    response: NewResultResponse<EventDetailItem>?
                                                ) {
                                                    hideProgress()
                                                }
                                            }).build().call()
                                    }
                                }
                                i++

                                binding.layoutEventDetailContents.addView(titleBinding.root)
                            }
                            binding.textEventDetailJoin.setOnClickListener {
                                joinWithEventDetail()
                            }
                        }else{
                            binding.layoutEventDetailContents.visibility = View.GONE
                            binding.textEventDetailJoin.setOnClickListener {
                                joinNotEventDetail()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<NewResultResponse<EventDetail>>?,
                        t: Throwable?,
                        response: NewResultResponse<EventDetail>?
                    ) {
                        hideProgress()
                    }
                }).build().call()
        }else{
            binding.layoutEventDetailContents.visibility = View.GONE

            binding.textEventDetailJoin.setOnClickListener {
                joinNotEventDetail()
            }
        }
    }

    val keys = arrayOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")

    private fun joinWithEventDetail(){

        if (!PplusCommonUtil.loginCheck(this@EventDetailActivity, null)) {
            return
        }

        if(mEventDetailList == null){
            LogUtil.e(LOG_TAG, "mEventDetailList is null")
            return
        }

        var i = 0
        val propertiesMap= HashMap<String, String>()
        for (eventDetail in mEventDetailList!!){
            if(eventDetail.data == null){
                LogUtil.e(LOG_TAG, "eventDetail.data is null")
                return
            }
            when(eventDetail.type) {
                1 -> {//단답형
                    val value = (eventDetail.data as EditText).text.toString().trim()
                    if(eventDetail.compulsory != null && eventDetail.compulsory!! && StringUtils.isEmpty(value)){
                        showAlert(getString(R.string.msg_input_compulsory))
                        return
                    }
                    propertiesMap[keys[i]] = value
                }
                2 -> {//장문형
                    val value = (eventDetail.data as EditText).text.toString().trim()
                    if(eventDetail.compulsory != null && eventDetail.compulsory!! && StringUtils.isEmpty(value)){
                        showAlert(getString(R.string.msg_input_compulsory))
                        return
                    }
                    propertiesMap[keys[i]] = value
                }
                3 -> {//라디오
                    val radioList = (eventDetail.data as ArrayList<TextView>)
                    var value = ""
                    for(radio in radioList){
                        if(radio.isSelected){
                            value = radio.text.toString()
                            break
                        }
                    }
                    if(eventDetail.compulsory != null && eventDetail.compulsory!! && StringUtils.isEmpty(value)){
                        showAlert(getString(R.string.msg_input_compulsory))
                        return
                    }
                    propertiesMap[keys[i]] = value
                }
                4 -> {//드랍박스
                    val value = (eventDetail.data as TextView).text.toString().trim()
                    if(eventDetail.compulsory != null && eventDetail.compulsory!! && StringUtils.isEmpty(value)){
                        showAlert(getString(R.string.msg_input_compulsory))
                        return
                    }
                    propertiesMap[keys[i]] = value
                }
                5 -> {//체크박스
                    val checkList = (eventDetail.data as ArrayList<TextView>)
                    val checkedList = arrayListOf<String>()

                    for(radio in checkList){
                        if(radio.isSelected){
                            checkedList.add(radio.text.toString())
                        }
                    }

                    var value = ""
                    var j = 0
                    for(checked in checkedList){
                        value += checked
                        if(j < checkedList.size-1){
                            value += ","
                        }
                        j++
                    }

                    if(eventDetail.compulsory != null && eventDetail.compulsory!! && StringUtils.isEmpty(value)){
                        showAlert(getString(R.string.msg_input_compulsory))
                        return
                    }
                    propertiesMap[keys[i]] = value
                }
            }
            i++
        }

        val properties = JSONObject(propertiesMap as Map<*, *>).toString()


        if (mEvent!!.useDetailItem != null && mEvent!!.useDetailItem!! && LoginInfoManager.getInstance().user.verification!!.media != "external") {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(
                AlertData.MessageData(
                    getString(R.string.msg_verification_me_for_event1),
                    AlertBuilder.MESSAGE_TYPE.TEXT,
                    3
                )
            )
            builder.addContents(
                AlertData.MessageData(
                    getString(R.string.msg_move_verification),
                    AlertBuilder.MESSAGE_TYPE.TEXT,
                    2
                )
            )
            builder.setLeftText(getString(R.string.word_cancel))
                .setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val intent = Intent(this@EventDetailActivity, VerificationMeActivity::class.java)
                            intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            verificationLauncher.launch(intent)
                        }
                    }
                }
            }).builder().show(this@EventDetailActivity)
            return
        }

        if (mEvent!!.deliveryInfo != null && mEvent!!.deliveryInfo!!) {
            showProgress("")
            ApiBuilder.create().memberAddress.setCallback(object :
                PplusCallback<NewResultResponse<MemberAddress>> {
                override fun onResponse(
                    call: Call<NewResultResponse<MemberAddress>>?,
                    response: NewResultResponse<MemberAddress>?
                ) {
                    hideProgress()

                    val memberAddress = response?.data
                    if (memberAddress == null) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_need_member_address1),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                3
                            )
                        )
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_need_member_address2),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                2
                            )
                        )
                        builder.setLeftText(getString(R.string.word_cancel))
                            .setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object :
                            OnAlertResultListener {
                            override fun onCancel() {}
                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@EventDetailActivity, MemberAddressSaveActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                    }
                                }
                            }
                        }).builder().show(this@EventDetailActivity)
                        return
                    } else {
                        checkPlusAndJoin(properties)
                    }
                }

                override fun onFailure(
                    call: Call<NewResultResponse<MemberAddress>>?,
                    t: Throwable?,
                    response: NewResultResponse<MemberAddress>?
                ) {
                    hideProgress()
                }
            }).build().call()
        } else {
            checkPlusAndJoin(properties)
        }
    }

    private fun joinNotEventDetail(){

        if (!PplusCommonUtil.loginCheck(this@EventDetailActivity, null)) {
            return
        }

        if (mEvent!!.deliveryInfo != null && mEvent!!.deliveryInfo!!) {
            showProgress("")
            ApiBuilder.create().memberAddress.setCallback(object :
                PplusCallback<NewResultResponse<MemberAddress>> {
                override fun onResponse(
                    call: Call<NewResultResponse<MemberAddress>>?,
                    response: NewResultResponse<MemberAddress>?
                ) {
                    hideProgress()

                    val memberAddress = response?.data
                    if (memberAddress == null) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_need_member_address1),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                3
                            )
                        )
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_need_member_address2),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                2
                            )
                        )
                        builder.setLeftText(getString(R.string.word_cancel))
                            .setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object :
                            OnAlertResultListener {
                            override fun onCancel() {}
                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@EventDetailActivity, MemberAddressSaveActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        startActivity(intent)
                                    }
                                }
                            }
                        }).builder().show(this@EventDetailActivity)
                        return
                    } else {
                        checkPlusAndJoin("")
                    }
                }

                override fun onFailure(
                    call: Call<NewResultResponse<MemberAddress>>?,
                    t: Throwable?,
                    response: NewResultResponse<MemberAddress>?
                ) {
                    hideProgress()
                }
            }).build().call()
        } else {
            checkPlusAndJoin("")
        }
    }

    fun setData() {

        var isWinAnnounce = false

        val currentMillis = System.currentTimeMillis()
        if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
            val winAnnounceDate =
                DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent!!.winAnnounceDate).time
            if (currentMillis > winAnnounceDate) {
                isWinAnnounce = true
//                text_event_detail_join.text = getString(R.string.msg_confirm_event_win)
            }
        }

        if (!isWinAnnounce && mEvent!!.secondaryType.equals(EventType.SecondaryType.time.name) && mEvent!!.displayTimeList!!.isNotEmpty()) {

            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = sdf.format(Date(currentMillis)).split(":")
            val currentSecond =
                (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
            var endDisplayMillis = 0L
            for (time in mEvent!!.displayTimeList!!) {
                val startSecond =
                    (time.start.substring(0, 2).toInt() * 60 * 60) + (time.start.substring(2)
                        .toInt() * 60)
                val endSecond =
                    (time.end.substring(0, 2).toInt() * 60 * 60) + (time.end.substring(2)
                        .toInt() * 60)

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

        private val handler = Handler()

        @JavascriptInterface
        fun sendMessage(properties: String?) {

            handler.post {
                LogUtil.e(LOG_TAG, "sendMessage")

                if (!PplusCommonUtil.loginCheck(this@EventDetailActivity, null)) {
                    return@post
                }

                if (mEvent!!.primaryType != EventType.PrimaryType.number.name) {

                    if (mEvent!!.isDb != null && mEvent!!.isDb!! && LoginInfoManager.getInstance().user.verification!!.media != "external") {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_verification_me_for_event1),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                3
                            )
                        )
                        builder.addContents(
                            AlertData.MessageData(
                                getString(R.string.msg_move_verification),
                                AlertBuilder.MESSAGE_TYPE.TEXT,
                                2
                            )
                        )
                        builder.setLeftText(getString(R.string.word_cancel))
                            .setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {
                            override fun onCancel() {}
                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@EventDetailActivity, VerificationMeActivity::class.java)
                                        intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        verificationLauncher.launch(intent)
                                    }
                                }
                            }
                        }).builder().show(this@EventDetailActivity)
                        return@post
                    }

                    if (mEvent!!.deliveryInfo != null && mEvent!!.deliveryInfo!!) {
                        showProgress("")
                        ApiBuilder.create().memberAddress.setCallback(object :
                            PplusCallback<NewResultResponse<MemberAddress>> {
                            override fun onResponse(
                                call: Call<NewResultResponse<MemberAddress>>?,
                                response: NewResultResponse<MemberAddress>?
                            ) {
                                hideProgress()

                                val memberAddress = response?.data
                                if (memberAddress == null) {
                                    val builder = AlertBuilder.Builder()
                                    builder.setTitle(getString(R.string.word_notice_alert))
                                    builder.addContents(
                                        AlertData.MessageData(
                                            getString(R.string.msg_need_member_address1),
                                            AlertBuilder.MESSAGE_TYPE.TEXT,
                                            3
                                        )
                                    )
                                    builder.addContents(
                                        AlertData.MessageData(
                                            getString(R.string.msg_need_member_address2),
                                            AlertBuilder.MESSAGE_TYPE.TEXT,
                                            2
                                        )
                                    )
                                    builder.setLeftText(getString(R.string.word_cancel))
                                        .setRightText(getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object :
                                        OnAlertResultListener {
                                        override fun onCancel() {}
                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                                    val intent = Intent(this@EventDetailActivity, MemberAddressSaveActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                    }).builder().show(this@EventDetailActivity)
                                    return
                                } else {
                                    checkPlusAndJoin(properties)
                                }
                            }

                            override fun onFailure(
                                call: Call<NewResultResponse<MemberAddress>>?,
                                t: Throwable?,
                                response: NewResultResponse<MemberAddress>?
                            ) {
                                hideProgress()
                            }
                        }).build().call()
                    } else {
                        checkPlusAndJoin(properties)
                    }

                } else {
                    val intent = Intent(this@EventDetailActivity, PadActivity::class.java)
//                val intent = Intent(this@EventDetailActivity, AppMainActivity::class.java)
                    intent.putExtra(Const.KEY, Const.PAD)
                    intent.putExtra(Const.NUMBER, mEvent!!.virtualNumber)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
                }
            }
        }
    }

    fun checkPlusAndJoin(properties: String?) {

        if(StringUtils.isNotEmpty(mEvent!!.personalTitle)){
            val check = binding.checkEventDetailTermsAgree.isChecked
            if(!check){
                showAlert(R.string.msg_agree_terms)
                return
            }
        }

        if (mEvent!!.isPlus != null && mEvent!!.isPlus!! && mEvent!!.pageSeqNo != null && mEvent!!.agreement2 != null && mEvent!!.agreement2!! == 1) {

            if (LoginInfoManager.getInstance().user.verification!!.media != "external") {
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(
                    AlertData.MessageData(
                        getString(R.string.msg_verification_me_for_event1),
                        AlertBuilder.MESSAGE_TYPE.TEXT,
                        3
                    )
                )
                builder.addContents(
                    AlertData.MessageData(
                        getString(R.string.msg_move_verification),
                        AlertBuilder.MESSAGE_TYPE.TEXT,
                        2
                    )
                )
                builder.setLeftText(getString(R.string.word_cancel))
                    .setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {}
                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(this@EventDetailActivity, VerificationMeActivity::class.java)
                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                verificationLauncher.launch(intent)
                            }
                        }
                    }
                }).builder().show(this)
                return
            }

            val params = HashMap<String, String>()
            params["pageSeqNo"] = mEvent!!.pageSeqNo.toString()
            showProgress("")
            ApiBuilder.create().getOnlyPlus(params)
                .setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                    override fun onResponse(
                        call: Call<NewResultResponse<Plus>>?,
                        response: NewResultResponse<Plus>?
                    ) {
                        hideProgress()
                        if (response?.data == null || response.data.agreement == null || !response.data.agreement!!) {

                            val intent = Intent(this@EventDetailActivity, Alert3rdPartyInfoTermsActivity::class.java)
                            intent.putExtra(Const.EVENT, mEvent!!)
                            intent.putExtra(Const.PROPERTIES, properties)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            eventAgreeLauncher.launch(intent)
                        } else {

                            if (StringUtils.isNotEmpty(properties)) {
                                joinEvent(mEvent!!, JsonParser.parseString(properties).asJsonObject)
                            } else {
                                joinEvent(mEvent!!)
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<NewResultResponse<Plus>>?,
                        t: Throwable?,
                        response: NewResultResponse<Plus>?
                    ) {
                        hideProgress()
                    }
                }).build().call()
        } else {

            if (StringUtils.isNotEmpty(properties)) {
                joinEvent(mEvent!!, JsonParser.parseString(properties).asJsonObject)
            } else {
                joinEvent(mEvent!!)
            }
        }
    }

    fun joinEvent(event: Event, properties: JsonObject) {
        val params = ParamsJoinEvent()
        params.event = No(event.no)
        params.properties = properties

        showProgress("")

        ApiBuilder.create().joinWithPropertiesEvent(params)
            .setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
                override fun onResponse(
                    call: Call<NewResultResponse<EventResult>>?,
                    response: NewResultResponse<EventResult>?
                ) {

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
                            val intent =
                                Intent(this@EventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<NewResultResponse<EventResult>>?,
                    t: Throwable?,
                    response: NewResultResponse<EventResult>?
                ) {
                    hideProgress()

                    if (response != null) {
                        if (response.resultCode == 502) {
                            showAlert(getString(R.string.msg_input_info))
                        } else {
                            PplusCommonUtil.showEventAlert(
                                this@EventDetailActivity,
                                response.resultCode,
                                event
                            )
                        }

                    }
                }
            }).build().call()
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()

        showProgress("")

        ApiBuilder.create().joinEvent(params)
            .setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
                override fun onResponse(
                    call: Call<NewResultResponse<EventResult>>?,
                    response: NewResultResponse<EventResult>?
                ) {

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

                override fun onFailure(
                    call: Call<NewResultResponse<EventResult>>?,
                    t: Throwable?,
                    response: NewResultResponse<EventResult>?
                ) {
                    hideProgress()

                    if (response != null) {
                        PplusCommonUtil.showEventAlert(
                            this@EventDetailActivity,
                            response.resultCode,
                            event
                        )
                    }
                }
            }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(
            getString(R.string.word_event),
            ToolbarOption.ToolbarMenu.LEFT
        )
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_event_top_share)
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

                        if (mEvent!!.detailType != null && StringUtils.isNotEmpty(mEvent!!.code)) {

                            var text = ""
                            if (mEvent!!.detailType == 3) {
                                var url = ""
                                if (Const.API_URL.startsWith("https://api")) {
                                    url = getString(R.string.format_event_url, mEvent!!.code.toString())
                                } else {
                                    url = getString(R.string.format_stage_event_url, mEvent!!.code.toString())
                                }

                                text = "${mEvent!!.title}\n$url"
                            } else {
                                text = "${getString(R.string.msg_invite_description)}\n${getString(R.string.msg_invite_url)}"
                            }

                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT, text)
                            val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
                            startActivity(chooserIntent)
                        }

                    }
                }
            }
        }
    }

}
