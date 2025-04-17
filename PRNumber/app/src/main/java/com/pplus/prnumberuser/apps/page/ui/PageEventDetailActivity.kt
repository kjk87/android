package com.pplus.prnumberuser.apps.page.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.ui.EventResultActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPageEventDetailBinding
import com.pplus.prnumberuser.databinding.ItemPageEventGiftBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class PageEventDetailActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {
        return ""
    }

    lateinit var mEvent: Event

    private lateinit var binding: ActivityPageEventDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPageEventDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)!!
        getEvent()

        binding.textPageEventDetailJoin.setOnClickListener {
            if(mEvent.isPlus != null && mEvent.isPlus!! && mEvent.pageSeqNo != null && mEvent.agreement2 != null && mEvent.agreement2!! == 1){
                val params = HashMap<String, String>()
                params["pageSeqNo"] = mEvent.pageSeqNo.toString()
                showProgress("")
                ApiBuilder.create().getOnlyPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
                    override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
                        hideProgress()
                        if(response?.data == null || response.data.agreement == null || !response.data.agreement!!){
                            val intent = Intent(this@PageEventDetailActivity, Alert3rdPartyInfoTermsActivity::class.java)
                            intent.putExtra(Const.EVENT, mEvent)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_EVENT_AGREE)
                        }else{
                            joinEvent(mEvent)
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
                        hideProgress()
                    }
                }).build().call()
            }else{
                joinEvent(mEvent)
            }
        }
    }

    private fun getEvent() {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                mEvent = response!!.data
                if (mEvent != null) {
                    setData()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun setData(){
        if(StringUtils.isNotEmpty(mEvent.bannerImageUrl)){
            binding.imagePageEventDetailBanner.visibility = View.VISIBLE
            Glide.with(this).load(mEvent.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(binding.imagePageEventDetailBanner)
        }else{
            binding.imagePageEventDetailBanner.visibility = View.GONE
        }

        binding.textPageEventDetailContents.text = mEvent.contents

        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                if(response?.datas != null && response.datas.isNotEmpty()){
                    binding.layoutPageEventDetailGift.visibility = View.VISIBLE
                    val giftList = response.datas
                    binding.layoutPageEventDetailGiftList?.removeAllViews()
                    for(gift in giftList){
                        val pageEventGiftBinding = ItemPageEventGiftBinding.inflate(layoutInflater)
                        pageEventGiftBinding.textPageEventDetailGiftName.text = gift.title
                        pageEventGiftBinding.textPageEventDetailGiftCount.text = getString(R.string.format_count_unit4, FormatUtil.getMoneyType(gift.totalCount.toString()))
                        binding.layoutPageEventDetailGiftList.addView(pageEventGiftBinding.root)
                    }
                }else{
                    binding.layoutPageEventDetailGift.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?, t: Throwable?, response: NewResultResponse<EventGift>?) {

            }
        }).build().call()

        val start = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.duration!!.start)
        val end = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.duration!!.end)
        val announce = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.winAnnounceDate)
        val output = SimpleDateFormat("yy.MM.dd HH:mm")

        binding.textPageEventDetailTerm.text = "${output.format(start)} - ${output.format(end)}"
        binding.textPageEventDetailWinAnnounceDate.text = getString(R.string.format_win_announce_date, output.format(announce))
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response?.data != null) {
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

                            val intent = Intent(this@PageEventDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_RESULT)
                        }, delayTime)

                    } else {
                        val intent = Intent(this@PageEventDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_RESULT)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {

                hideProgress()

                if (response?.data != null && StringUtils.isNotEmpty(response.data.joinDate)) {
                    PplusCommonUtil.showEventAlert(this@PageEventDetailActivity, response.resultCode, event, response.data.joinDate, response.data.joinTerm)
                } else {
                    PplusCommonUtil.showEventAlert(this@PageEventDetailActivity, response?.resultCode!!, event)
                }
                getEvent()

            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_EVENT_AGREE -> {
                if (resultCode == Activity.RESULT_OK) {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    val event = data.getParcelableExtra<Event>(Const.DATA)
                    joinEvent(mEvent)
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_event_detail), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}