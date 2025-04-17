package com.pplus.luckybol.apps.event.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventImpressionHeaderAdapter
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityEventImpressionBinding
import com.pplus.networks.common.PplusCallback
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

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)

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
                val eventWin = mAdapter!!.getItem(position)
                eventWin.event = mEvent

                if(LoginInfoManager.getInstance().isMember && eventWin.user!!.no == LoginInfoManager.getInstance().user.no){
                    val builder = AlertBuilder.Builder()
                    builder.setLeftText(getString(R.string.word_cancel))

                    builder.setContents(getString(R.string.word_modified))

                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert.getValue()) {
                                1 -> {
                                    val intent = Intent(this@EventImpressionActivity, EventWinImpressionActivity::class.java)
                                    intent.putExtra(Const.EVENT_WIN, eventWin)
                                    resultLauncher.launch(intent)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mEvent = intent?.getParcelableExtra(Const.DATA)
        if(mEvent != null){
            getEvent()
        }
    }

    private fun getEvent(){
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>>{
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                mEvent = response!!.data

                ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
                    override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                        if (response?.datas != null && response.datas!!.isNotEmpty()) {

                            mAdapter!!.mGiftList = (response.datas as MutableList<EventGift>?)!!
                            mAdapter!!.mEvent = mEvent

                            if(LoginInfoManager.getInstance().isMember){
                                existResult()
                            }

                            getCount()
                        }

                    }

                    override fun onFailure(call: Call<NewResultResponse<EventGift>>?, t: Throwable?, response: NewResultResponse<EventGift>?) {

                    }
                }).build().call()

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun existResult() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().existsEventResult(params).setCallback(object : PplusCallback<NewResultResponse<EventExist>> {
            override fun onResponse(call: Call<NewResultResponse<EventExist>>?, response: NewResultResponse<EventExist>?) {
                hideProgress()
                val eventExist = response!!.data!!
                if (eventExist.join!!) {
                    if (eventExist.win!!) {
                        getWinAll()
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
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getWinAll(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
                hideProgress()
                if (response.datas != null && response.datas!!.isNotEmpty()) {

                    var eventWin: EventWin? = null
//                    val eventWinList = response.datas.filter{ it -> StringUtils.isEmpty(it.impression)}
//                    if(eventWinList.isNotEmpty()){
//                        eventWin = eventWinList[0]
//                    }
                    mAdapter!!.mMyWinList = (response.datas as MutableList<EventWin>?)!!
                    mAdapter!!.notifyItemChanged(0)
                    for(data in response.datas!!){

                        if(StringUtils.isEmpty(data.impression)){
                            eventWin = data
                            break
                        }
                    }

                    if(eventWin == null){
                        return
                    }

                    eventWin.event = mEvent!!

                    val loading = EventLoadingView()
                    loading.setText(getString(R.string.msg_checking_event_result))
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

                        }
                        val eventResult = EventResult()
                        eventResult.win = eventWin
                        val intent = Intent(this@EventImpressionActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, eventResult)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }, 2000)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getWinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data!!

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getWinList(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {

                mLockListView = false

                hideProgress()
                mAdapter?.addAll(response.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {

                hideProgress()
            }
        }).build().call()
    }

    val eventWinDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(LoginInfoManager.getInstance().isMember){
            existResult()
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getCount()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            getEvent()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_status2), ToolbarOption.ToolbarMenu.LEFT)
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
