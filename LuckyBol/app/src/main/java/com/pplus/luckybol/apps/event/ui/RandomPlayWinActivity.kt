package com.pplus.luckybol.apps.event.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.EventLoadingView
import com.pplus.luckybol.apps.event.data.RandomPlayHeaderWinAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityRandomPlayWinBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class RandomPlayWinActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityRandomPlayWinBinding

    override fun getLayoutView(): View {
        binding = ActivityRandomPlayWinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter:RandomPlayHeaderWinAdapter? = null
    var mEvent: Event? = null
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)

        mLayoutManager = GridLayoutManager(this, 2)
        mLayoutManager!!.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter!!.getItemViewType(position)) {
                    mAdapter!!.TYPE_HEADER -> 2
                    mAdapter!!.TYPE_ITEM -> 1
                    else -> 1
                }
            }
        })
        binding.recyclerRandomPlayWin.layoutManager = mLayoutManager
        mAdapter = RandomPlayHeaderWinAdapter()
        binding.recyclerRandomPlayWin.adapter = mAdapter!!
        binding.recyclerRandomPlayWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
        getEvent()
    }

    private fun getEvent(){
        val params = HashMap<String, String>()
        params["no"] = mEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                mEvent = response!!.data

                ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
                    override fun onResponse(call: Call<NewResultResponse<EventGift>>?, response: NewResultResponse<EventGift>?) {
                        if (response!!.datas != null && response.datas!!.isNotEmpty()) {
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

                    val handler = Handler()
                    handler.postDelayed({
                        try{
                            loading.dismiss()
                        }catch (e : Exception){

                        }
                        val eventResult = EventResult()
                        eventResult.win = eventWin
                        val intent = Intent(this@RandomPlayWinActivity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, eventResult)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
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

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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