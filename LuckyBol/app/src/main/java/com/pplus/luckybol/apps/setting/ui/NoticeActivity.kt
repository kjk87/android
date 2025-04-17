package com.pplus.luckybol.apps.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.setting.data.NoticeAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Notice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityNoticeBinding
import retrofit2.Call
import java.util.*

/**
 * 공지사항
 */
class NoticeActivity : BaseActivity(), ImplToolbar {
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: NoticeAdapter? = null
    private var mLockListView = true
    private var mTotalCount = 0
    private var mPage = 0
    override fun getPID(): String? {
        return "Main_mypage_notice"
    }

    private lateinit var binding: ActivityNoticeBinding

    override fun getLayoutView(): View {
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = NoticeAdapter()
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerNotice.layoutManager = mLayoutManager
        binding.recyclerNotice.adapter = mAdapter
        mAdapter!!.setOnItemClickListener (object : NoticeAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if (mAdapter!!.getItem(position) != null) { // Webview 상세로 이동
                    val intent = Intent(this@NoticeActivity, NoticeDetailActivity::class.java)
                    intent.putExtra(Const.NOTICE, mAdapter!!.getItem(position))
                    startActivity(intent)
                }
            }
        })
        binding.recyclerNotice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
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
        getCount()
    }

    private fun getCount() {
            val params: MutableMap<String, String> = HashMap()
            params["appKey"] = packageName
            ApiBuilder.create().getNoticeCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    if(response?.data != null){
                        mTotalCount = response.data!!
                        mPage = 1
                        listCall(mPage)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                }
            }).build().call()
        }

    private fun listCall(page: Int) {
        val params: MutableMap<String, String> = HashMap()
        params["appKey"] = packageName
        params["pg"] = "" + page
        mLockListView = true
        ApiBuilder.create().getNoticeList(params).setCallback(object : PplusCallback<NewResultResponse<Notice>> {
            override fun onResponse(call: Call<NewResultResponse<Notice>>?, response: NewResultResponse<Notice>?) {
                mLockListView = false
                if(response?.datas != null){
                    mAdapter!!.addAll(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Notice>>?, t: Throwable?, response: NewResultResponse<Notice>?) {
                mLockListView = false
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    else -> {}
                }
            }
        }
    }
}