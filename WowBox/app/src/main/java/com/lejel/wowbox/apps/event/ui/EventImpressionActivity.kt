package com.lejel.wowbox.apps.event.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.data.EventImpressionHeaderAdapter
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxLoadingView
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.*
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityEventImpressionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EventImpressionActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_mypage_prizewinner"
    }

    private lateinit var binding: ActivityEventImpressionBinding

    override fun getLayoutView(): View {
        binding = ActivityEventImpressionBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: EventImpressionHeaderAdapter? = null
    var mEvent: Event? = null
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mPage = 1
            listCall(mPage)
        }

        mEvent = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Event::class.java)

        mAdapter = EventImpressionHeaderAdapter(this)
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerEventImpression.layoutManager = mLayoutManager
        binding.recyclerEventImpression.adapter = mAdapter
        binding.recyclerEventImpression.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_100))
        binding.recyclerEventImpression.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : EventImpressionHeaderAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(mAdapter!!.mDataList == null || mAdapter!!.mDataList!!.isEmpty()){
                    return
                }
                val eventWin = mAdapter!!.getItem(position)

                if(LoginInfoManager.getInstance().isMember() && eventWin.userKey == LoginInfoManager.getInstance().member!!.userKey && eventWin.eventGift!!.giftType != "point" && eventWin.eventGift!!.giftType != "ball"){
                    val builder = AlertBuilder.Builder()
                    builder.setRightText(getString(R.string.word_cancel))

                    builder.setContents(getString(R.string.word_modified))

                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert.value) {
                                1 -> {
                                    val intent = Intent(this@EventImpressionActivity, EventWinImpressionActivity::class.java)
                                    intent.putExtra(Const.EVENT_WIN, eventWin)
                                    intent.putExtra(Const.EVENT, mEvent)
                                    resultLauncher?.launch(intent)
                                }
                            }
                        }
                    }).builder().show(this@EventImpressionActivity)
                }

            }
        })

        getEvent()
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mEvent = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Event::class.java)
        if(mEvent != null){
            getEvent()
        }
    }

    private fun getEvent(){
        showProgress("")
        ApiBuilder.create().getEvent(mEvent!!.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<Event>>{
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                if(response?.result != null){
                    mEvent = response.result

                    val params = HashMap<String, String>()
                    params["eventSeqNo"] = mEvent!!.seqNo.toString()

                    ApiBuilder.create().getEventGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EventGift>>> {
                        override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EventGift>>>?, response: NewResultResponse<ListResultResponse<EventGift>>?) {
                            if(response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()){
                                mAdapter!!.mGiftList = response.result!!.list as MutableList
                                mAdapter!!.mEvent = mEvent

                                if(LoginInfoManager.getInstance().isMember()){
                                    existResult()
                                }

                                mPage = 1
                                listCall(mPage)
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EventGift>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EventGift>>?) {

                        }

                    }).build().call()
                }



            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun existResult() {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().existsEventResult(params).setCallback(object : PplusCallback<NewResultResponse<EventExist>> {
            override fun onResponse(call: Call<NewResultResponse<EventExist>>?, response: NewResultResponse<EventExist>?) {
                hideProgress()
                if(response?.result != null){
                    val eventExist = response.result!!
                    if (eventExist.join!!) {
                        if (eventExist.win!!) {
                            getWinAll()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventExist>>?, t: Throwable?, response: NewResultResponse<EventExist>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getWinAll() {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getWinAll(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EventWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {

                    var eventWin: EventWin? = null
                    //                    val eventWinList = response.datas.filter{ it -> StringUtils.isEmpty(it.impression)}
                    //                    if(eventWinList.isNotEmpty()){
                    //                        eventWin = eventWinList[0]
                    //                    }
                    mAdapter!!.mMyWinList = response.result!!.list as MutableList
                    mAdapter!!.notifyItemChanged(0)
                    for(data in response.result!!.list!!){

                        if(StringUtils.isEmpty(data.impression)){
                            eventWin = data
                            break
                        }
                    }

                    if(eventWin == null){
                        return
                    }

                    val loading = LuckyBoxLoadingView()
//                    loading.setText(getString(R.string.msg_checking_event_result))
                    loading.isCancelable = false
                    try{
                        loading.show(supportFragmentManager, "")
                    }catch (e : Exception){

                    }

                    val handler = Handler(Looper.myLooper()!!)
                    handler.postDelayed({
                        try{
                            loading.dismiss()
                        }catch (e : Exception){
                            LogUtil.e(LOG_TAG, e.toString())
                        }

                        val intent = Intent(this@EventImpressionActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_WIN, eventWin)
                        intent.putExtra(Const.EVENT, mEvent)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher?.launch(intent)
                    }, 2000)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent!!.seqNo.toString()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventWinList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EventWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val eventWinDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(LoginInfoManager.getInstance().isMember()){
            existResult()
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            getEvent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resultLauncher = null
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_status2), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}
