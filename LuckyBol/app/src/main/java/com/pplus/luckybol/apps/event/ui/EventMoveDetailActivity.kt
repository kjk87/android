package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.luckybol.apps.event.data.BannerAdapter
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventBanner
import com.pplus.luckybol.core.network.model.dto.EventResult
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityEventMoveDetetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventMoveDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventMoveDetetailBinding

    override fun getLayoutView(): View {
        binding = ActivityEventMoveDetetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEvent: Event? = null
    var mAdapter: BannerAdapter? = null
    var mMoveTargetString: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mEvent = intent.getParcelableExtra(Const.DATA)

        Glide.with(this).load(mEvent!!.detailImage?.url).apply(RequestOptions().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val screenWidth = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS
                val width = resource.intrinsicWidth
                val height = resource.intrinsicHeight
                val rate = screenWidth.toFloat().div(width.toFloat())
                LogUtil.e(LOG_TAG, "rate : {}", rate)
                val imageHeight = (height * rate)

                LogUtil.e(LOG_TAG, "height : {}", imageHeight)
                binding.imageEventMoveDetail.layoutParams.height = imageHeight.toInt()
                binding.imageEventMoveDetail.setImageDrawable(resource)
            }
        })
        binding.recyclerEventMoveDetail.layoutManager = LinearLayoutManager(this)
        mAdapter = BannerAdapter(this)
        binding.recyclerEventMoveDetail.adapter = mAdapter
        binding.recyclerEventMoveDetail.isNestedScrollingEnabled = false
        binding.recyclerEventMoveDetail.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_20))
        mAdapter!!.setOnItemClickListener(object : BannerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                mMoveTargetString = mAdapter!!.getItem(position).moveTargetString
                joinEvent(mEvent!!, mAdapter!!.getItem(position))
            }
        })
        getBannerAll()
    }

    private fun getBannerAll() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getBannerAll(params).setCallback(object : PplusCallback<NewResultResponse<EventBanner>> {
            override fun onResponse(call: Call<NewResultResponse<EventBanner>>?, response: NewResultResponse<EventBanner>?) {
                hideProgress()
                mAdapter!!.clear()
                mAdapter!!.addAll(response!!.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<EventBanner>>?, t: Throwable?, response: NewResultResponse<EventBanner>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun joinEvent(event: Event, banner: EventBanner) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        params["bannerNo"] = banner.bannerNo.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

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

                            val intent = Intent(this@EventMoveDetailActivity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }, delayTime)

                    }else{
                        val intent = Intent(this@EventMoveDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {
                hideProgress()
                if (response != null) {
                    if (response.resultCode == 504) {
                        if (StringUtils.isNotEmpty(mMoveTargetString)) {

                            PplusCommonUtil.openChromeWebView(this@EventMoveDetailActivity, mMoveTargetString!!)

                            mMoveTargetString = ""
                        }
                    }else if (response.resultCode == 516) {
                        showAlert(R.string.msg_can_not_join_time_event)
                    }
                }
            }
        }).build().call()
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (StringUtils.isNotEmpty(mMoveTargetString)) {

                PplusCommonUtil.openChromeWebView(this@EventMoveDetailActivity, mMoveTargetString!!)
                mMoveTargetString = ""
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
