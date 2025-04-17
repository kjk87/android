package com.root37.buflexz.apps.notice.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.notice.data.NoticeAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Notice
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.ActivityNoticeBinding
import retrofit2.Call
import java.util.Locale

class NoticeActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityNoticeBinding

    override fun getLayoutView(): View {
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: NoticeAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        mAdapter = NoticeAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerNotice.adapter = mAdapter
        binding.recyclerNotice.layoutManager = mLayoutManager

        binding.recyclerNotice.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getList(mPaging)
                    }
                }
            }
        })

        mAdapter!!.listener = object : NoticeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                val intent = Intent(this@NoticeActivity, NoticeDetailActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        val country = Locale.getDefault().country
        params["nation"] = country.lowercase()
        params["aos"] = "1"

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getNoticeList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Notice>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Notice>>>?, response: NewResultResponse<ListResultResponse<Notice>>?) {
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

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Notice>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Notice>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarOption.ToolbarMenu.LEFT)
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