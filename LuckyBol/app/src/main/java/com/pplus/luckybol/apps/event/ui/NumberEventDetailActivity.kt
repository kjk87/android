package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResult
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityNumberEventDetailBinding
import com.pplus.luckybol.databinding.ItemWinCodeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList


class NumberEventDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityNumberEventDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityNumberEventDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var viewWinCodeList = ArrayList<ItemWinCodeBinding>()
    var numberSb = StringBuilder()
    override fun initializeView(savedInstanceState: Bundle?) {
        val event = intent.getParcelableExtra<Event>(Const.DATA)!!

        Glide.with(this).load(event.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.img_number_event_default).error(R.drawable.img_number_event_default)).into(binding.imageNumberEvent)

        viewWinCodeList = ArrayList()
        for(i in 0 until event.winCode!!.length){
            val itemWinCodeBinding = ItemWinCodeBinding.inflate(layoutInflater, LinearLayout(this), false)
            viewWinCodeList.add(itemWinCodeBinding)

            binding.layoutNumberEventWinCode.addView(itemWinCodeBinding.root)

            if(i < event.winCode!!.length-1){
                (itemWinCodeBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_24)
            }
        }

        numberSb = StringBuilder()
        binding.viewNumberEventPad.setOnKeyClickListener { key, view ->
            if (key.number == "#") {//삭제
                if (numberSb.isNotEmpty()) {
                    numberSb.setLength(numberSb.length - 1)

                    for(i in 0 until viewWinCodeList.size){
                        viewWinCodeList[i].textWinCode.text = ""
                    }

                    for(i in 0 until numberSb.length){
                        viewWinCodeList[i].textWinCode.text = numberSb[i].toString()
                    }

                    if(numberSb.length < event.winCode!!.length){
                        binding.textNumberEventDetailJoin.setBackgroundResource(R.drawable.btn_gray)
                        binding.textNumberEventDetailJoin.isEnabled = false
                    }else{
                        binding.textNumberEventDetailJoin.setBackgroundResource(R.drawable.bg_fc5c57_radius_66)
                        binding.textNumberEventDetailJoin.isEnabled = true
                    }
                }
            } else if (key.number != "*") {

                if(numberSb.length < event.winCode!!.length){
                    numberSb.append(key.number)

                    for(i in 0 until numberSb.length){
                        viewWinCodeList[i].textWinCode.text = numberSb[i].toString()
                    }

                    if(numberSb.length < event.winCode!!.length){
                        binding.textNumberEventDetailJoin.setBackgroundResource(R.drawable.btn_gray)
                        binding.textNumberEventDetailJoin.isEnabled = false
                    }else{
                        binding.textNumberEventDetailJoin.setBackgroundResource(R.drawable.bg_fc5c57_radius_66)
                        binding.textNumberEventDetailJoin.isEnabled = true
                    }
                }

            } else if (key.number == "*") {

            }
        }

        binding.textNumberEventDetailJoin.setBackgroundResource(R.drawable.btn_gray)
        binding.textNumberEventDetailJoin.isEnabled = false

        binding.textNumberEventDetailJoin.setOnClickListener {

            val winCode = numberSb.toString().trim()

            if(StringUtils.isEmpty(winCode)){
                showAlert(R.string.msg_input_number_event)
                return@setOnClickListener
            }

            if(winCode.length < event.winCode!!.length){
                showAlert(R.string.format_msg_input_over_number, event.winCode!!.length)
                return@setOnClickListener
            }

            joinEvent(event, winCode)
        }

    }

    fun joinEvent(event: Event, winCode:String) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        params["winCode"] = winCode
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                setEvent("numberEventJoin")
                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        val delayTime = 2000L
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

                            val intent = Intent(this@NumberEventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@NumberEventDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response != null) {
                    PplusCommonUtil.showEventAlert(this@NumberEventDetailActivity, response.resultCode, event, null)
                }

            }
        }).build().call()
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_match_number_event), ToolbarOption.ToolbarMenu.LEFT)
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
