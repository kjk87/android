package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.event.data.BannerAdapter
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventBanner
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityEventMoveDetetailBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

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

//        Glide.with(this).load(mEvent!!.detailImage?.url).apply(RequestOptions().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(object : SimpleTarget<Drawable>() {
//            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                val screenWidth = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS
//                val width = resource.intrinsicWidth
//                val height = resource.intrinsicHeight
//                val rate = screenWidth.toFloat().div(width.toFloat())
//                LogUtil.e(LOG_TAG, "rate : {}", rate)
//                val imageHeight = (height * rate)
//
//                LogUtil.e(LOG_TAG, "height : {}", imageHeight)
//                image_event_move_detail.layoutParams.height = imageHeight.toInt()
//                image_event_move_detail.setImageDrawable(resource)
//            }
//        })
        binding.recyclerEventMoveDetail.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
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
                mAdapter!!.addAll(response!!.datas)
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
                            startActivityForResult(intent, Const.REQ_RESULT)
                        }, delayTime)

                    }else{
                        val intent = Intent(this@EventMoveDetailActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivityForResult(intent, Const.REQ_RESULT)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (StringUtils.isNotEmpty(mMoveTargetString)) {

                        PplusCommonUtil.openChromeWebView(this@EventMoveDetailActivity, mMoveTargetString!!)

//                        val intent = Intent(Intent.ACTION_VIEW)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        intent.data = Uri.parse(mMoveTargetString)
//                        startActivity(intent)
                        mMoveTargetString = ""
                    }
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
