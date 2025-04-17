//package com.pplus.prnumberuser.apps.event.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_number_event_detail.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//import android.R.attr.maxLength
//import android.app.Activity
//import android.text.InputFilter
//import android.view.View
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_win_code.view.*
//import java.lang.StringBuilder
//import kotlin.collections.ArrayList
//
//
//class NumberEventDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_number_event_detail
//    }
//
//    var viewWinCodeList = ArrayList<View>()
//    var numberSb = StringBuilder()
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val event = intent.getParcelableExtra<Event>(Const.DATA)
//
//        Glide.with(this).load(event!!.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.img_number_event_default).error(R.drawable.img_number_event_default)).into(image_number_event)
//
//        viewWinCodeList = ArrayList<View>()
//        for(i in 0 until event.winCode!!.length){
//            val view = layoutInflater.inflate(R.layout.item_win_code, null)
//            viewWinCodeList.add(view)
//
//            layout_number_event_win_code.addView(view)
//
//            if(i < event.winCode!!.length-1){
//                (view.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_24)
//            }
//        }
//
//        numberSb = StringBuilder()
//        view_number_event_pad.setOnKeyClickListener { key, view ->
//            if (key.number == "#") {//삭제
//                if (numberSb.isNotEmpty()) {
//                    numberSb.setLength(numberSb.length - 1)
//
//                    for(i in 0 until viewWinCodeList.size){
//                        viewWinCodeList[i].text_win_code.text = ""
//                    }
//
//                    for(i in 0 until numberSb.length){
//                        viewWinCodeList[i].text_win_code.text = numberSb[i].toString()
//                    }
//
//                    if(numberSb.length < event.winCode!!.length){
//                        text_number_event_detail_join.setBackgroundResource(R.drawable.btn_gray)
//                        text_number_event_detail_join.isEnabled = false
//                    }else{
//                        text_number_event_detail_join.setBackgroundResource(R.drawable.btn_blue)
//                        text_number_event_detail_join.isEnabled = true
//                    }
//                }
//            } else if (key.number != "*") {
//
//                if(numberSb.length < event.winCode!!.length){
//                    numberSb.append(key.number)
//
//                    for(i in 0 until numberSb.length){
//                        viewWinCodeList[i].text_win_code.text = numberSb[i].toString()
//                    }
//
//                    if(numberSb.length < event.winCode!!.length){
//                        text_number_event_detail_join.setBackgroundResource(R.drawable.btn_gray)
//                        text_number_event_detail_join.isEnabled = false
//                    }else{
//                        text_number_event_detail_join.setBackgroundResource(R.drawable.btn_blue)
//                        text_number_event_detail_join.isEnabled = true
//                    }
//                }
//
//            } else if (key.number == "*") {
//
//            }
//        }
//
//        text_number_event_detail_join.setBackgroundResource(R.drawable.btn_gray)
//        text_number_event_detail_join.isEnabled = false
//
//        text_number_event_detail_join.setOnClickListener {
//
//            val winCode = numberSb.toString().trim()
//
//            if(StringUtils.isEmpty(winCode)){
//                showAlert(R.string.msg_input_number_event)
//                return@setOnClickListener
//            }
//
//            if(winCode.length < event.winCode!!.length){
//                showAlert(R.string.format_msg_input_over_number, event.winCode!!.length)
//                return@setOnClickListener
//            }
//
//            joinEvent(event, winCode)
//        }
//
//    }
//
//    fun joinEvent(event: Event, winCode:String) {
//        val params = HashMap<String, String>()
//        params["no"] = event.no.toString()
//        params["winCode"] = winCode
//        showProgress("")
//
//        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
//            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//
//                if (response!!.data != null) {
//                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                        val mLoading = EventLoadingView()
//                        mLoading.isCancelable = false
//                        mLoading.setText(getString(R.string.msg_checking_event_result))
//                        val delayTime = 2000L
//                        mLoading.isCancelable = false
//                        try {
//                            mLoading.show(supportFragmentManager, "")
//                        } catch (e: Exception) {
//
//                        }
//
//                        val handler = Handler()
//                        handler.postDelayed(Runnable {
//
//                            try {
//                                mLoading.dismiss()
//                            } catch (e: Exception) {
//
//                            }
//
//                            val intent = Intent(this@NumberEventDetailActivity, EventResultActivity::class.java)
//                            intent.putExtra(Const.EVENT_RESULT, response.data)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivityForResult(intent, Const.REQ_RESULT)
//                        }, delayTime)
//
//                    } else {
//                        val intent = Intent(this@NumberEventDetailActivity, EventResultActivity::class.java)
//                        intent.putExtra(Const.EVENT_RESULT, response.data)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_RESULT)
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//
//                if (response != null) {
//                    PplusCommonUtil.showEventAlert(this@NumberEventDetailActivity, response.resultCode, event)
//                }
//
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            Const.REQ_RESULT->{
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_match_number_event), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
