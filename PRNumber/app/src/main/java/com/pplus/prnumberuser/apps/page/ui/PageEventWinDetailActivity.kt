package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.page.data.PageEventWinAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPageEventWinDetailBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class PageEventWinDetailActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {
        return ""
    }

    lateinit var eventGift:EventGift
    lateinit var mAdapter: PageEventWinAdapter
    private var mPaging = 0
    private var mTotalCount = 0
    private var mLockListView = true
    private var mLayoutManager: LinearLayoutManager? = null

    private lateinit var binding: ActivityPageEventWinDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityPageEventWinDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        eventGift = intent.getParcelableExtra(Const.DATA)!!
        mAdapter = PageEventWinAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerPageEventWinDetail.layoutManager = mLayoutManager
        binding.recyclerPageEventWinDetail.adapter = mAdapter
        binding.recyclerPageEventWinDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        getCount()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["giftSeqNo"] = eventGift.giftNo.toString()
        showProgress("")
        ApiBuilder.create().getEventWinCountByGiftSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data
                binding.textPageEventWinDetailGiftName.text = "${eventGift.title}(${getString(R.string.format_count_unit4, FormatUtil.getMoneyType(mTotalCount.toString()))})"
                mPaging = 1
                mAdapter.clear()
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["giftSeqNo"] = eventGift.giftNo.toString()
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventWinListByGiftSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {

                mLockListView = false

                hideProgress()
                mAdapter.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_winner), ToolbarOption.ToolbarMenu.LEFT)

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