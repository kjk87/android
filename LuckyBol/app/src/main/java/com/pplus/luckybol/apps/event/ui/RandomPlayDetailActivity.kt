//package com.pplus.luckybol.apps.event.ui
//
//import android.content.Context
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.event.data.RandomPlayHeaderGiftAdapter
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.Event
//import com.pplus.luckybol.core.network.model.dto.EventGift
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.databinding.ActivityRandomPlayDetailBinding
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
//class RandomPlayDetailActivity : BaseActivity(), ImplToolbar {
//    private lateinit var binding: ActivityRandomPlayDetailBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityRandomPlayDetailBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun getPID(): String? {
//        return ""
//    }
//
//    var mEvent: Event? = null
//    var mAdapter: RandomPlayHeaderGiftAdapter? = null
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mEvent = intent.getParcelableExtra(Const.DATA)
//        mAdapter = RandomPlayHeaderGiftAdapter()
//
//
//
//        val gridLayoutManager = GridLayoutManager(this, 2)
//        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (mAdapter!!.getItemViewType(position)) {
//                    mAdapter!!.TYPE_HEADER -> 2
//                    mAdapter!!.TYPE_ITEM -> 1
//                    else -> 1
//                }
//            }
//        })
//
//        binding.recyclerRandomPlayDetail.layoutManager = gridLayoutManager
//        binding.recyclerRandomPlayDetail.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_140))
//        binding.recyclerRandomPlayDetail.adapter = mAdapter!!
//
//        getEvent()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//    private fun getEvent() {
//        val params = HashMap<String, String>()
//        params["no"] = mEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?,
//                                    response: NewResultResponse<Event>?) {
//                hideProgress()
//                mEvent = response!!.data
//                if (mEvent != null) {
//                    mAdapter!!.mEvent = mEvent
//                }
//                getGiftAll()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Event>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun getGiftAll() {
//        val params = HashMap<String, String>()
//        params["no"] = mEvent!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
//            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
//                                    response: NewResultResponse<EventGift>?) {
//                hideProgress()
//                if (response?.datas != null) {
//                    mAdapter!!.addAll(response.datas)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<EventGift>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_random_play), ToolbarOption.ToolbarMenu.LEFT)
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