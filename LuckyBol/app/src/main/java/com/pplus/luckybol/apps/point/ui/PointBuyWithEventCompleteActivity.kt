package com.pplus.luckybol.apps.point.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.network.model.dto.PointBuy
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityPointBuyWithEventCompleteBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class PointBuyWithEventCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivityPointBuyWithEventCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityPointBuyWithEventCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mEvent: Event? = null
    var mPointBuy: PointBuy? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.EVENT)
        mPointBuy = intent.getParcelableExtra(Const.POINT_BUY)

        binding.textPointBuyWithEventCompleteJoinConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.textPointBuyWithEventCompletePoint.text = getString(R.string.format_save_point, FormatUtil.getMoneyType(mPointBuy!!.cash.toString()))

        getEvent()

    }

    private fun getGiftAll() {
        val params = java.util.HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
                                    response: NewResultResponse<EventGift>?) {
                if (response?.datas != null && response.datas!!.isNotEmpty()) {
                    val eventGift = response.datas!![0]
                    Glide.with(this@PointBuyWithEventCompleteActivity).load(eventGift.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imagePointBuyWithEventComplete)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventGift>?) {
            }
        }).build().call()
    }

    private fun getEvent() {
        val params = java.util.HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                hideProgress()
                mEvent = response!!.data
                if (mEvent != null) {

                    if (mEvent!!.primaryType == EventType.PrimaryType.randomluck.name) {
                        Glide.with(this@PointBuyWithEventCompleteActivity).load(mEvent!!.totalGiftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imagePointBuyWithEventComplete)
                    } else {
                        getGiftAll()
                    }

                    binding.textPointBuyWithEventCompleteEventName.text = mEvent!!.title

                    binding.layoutPointBuyWithEventCompleteJoinRateTotal.weightSum = mEvent!!.maxJoinCount!!.toFloat()

                    if (mEvent!!.joinCount!! < mEvent!!.maxJoinCount!!) {
                        binding.viewRandomPlayGraph.setBackgroundResource(R.drawable.img_play_graph_full)
                        binding.imagePointBuyWithEventCompleteGraph.setImageResource(R.drawable.img_play_graph_full_icon)
                        binding.textPointBuyWithEventCompleteJoinCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(mEvent!!.joinCount.toString()), FormatUtil.getMoneyType(mEvent!!.maxJoinCount.toString())))
                    } else {
                        var joinCount = mEvent!!.joinCount!!
                        if (joinCount >= mEvent!!.maxJoinCount!!) {
                            joinCount = mEvent!!.maxJoinCount!!
                        }
                        binding.viewRandomPlayGraph.setBackgroundResource(R.drawable.img_play_graph_full_red)
                        binding.imagePointBuyWithEventCompleteGraph.setImageResource(R.drawable.img_play_graph_full_icon_red)
                        binding.textPointBuyWithEventCompleteJoinCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(mEvent!!.maxJoinCount.toString())))
                    }
                    val layoutParams = binding.layoutPointBuyWithEventCompleteJoinRate.layoutParams
                    if (layoutParams is LinearLayout.LayoutParams) {
                        when (mEvent!!.winAnnounceType) {
                            EventType.WinAnnounceType.special.name -> {
                                if (mEvent!!.minJoinCount != null && mEvent!!.minJoinCount!! > 0) {
                                    if (mEvent!!.joinCount!! > mEvent!!.minJoinCount!!) {
                                        layoutParams.weight = mEvent!!.minJoinCount!!.toFloat()
                                    } else {
                                        layoutParams.weight = mEvent!!.joinCount!!.toFloat()
                                    }
                                } else {
                                    layoutParams.weight = 1f
                                }

                            }
                            EventType.WinAnnounceType.limit.name -> {
                                layoutParams.weight = mEvent!!.joinCount!!.toFloat()
                            }
                        }

                    }
                    binding.layoutPointBuyWithEventCompleteJoinRate.requestLayout()

                    if (StringUtils.isNotEmpty(mEvent!!.winAnnounceRandomDatetime)) {
                        binding.textPointBuyWithEventCompleteAnnounceTime.text = PplusCommonUtil.fromHtml(getString(R.string.html_announce_time_desc, mEvent!!.winAnnounceRandomDatetime!!.substring(0, 5)))
                    } else {
                        binding.textPointBuyWithEventCompleteAnnounceTime.text = getString(R.string.msg_announce_time_desc)
                    }

                    getJoinCount()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getJoinCount() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getMyJoinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.data != null) {
                    val myJoinCount = response.data

                    binding.textPointBuyWithEventCompleteJoinCountTotal.text = getString(R.string.format_point_buy_join_count, mPointBuy!!.count.toString(), myJoinCount.toString())
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }
}