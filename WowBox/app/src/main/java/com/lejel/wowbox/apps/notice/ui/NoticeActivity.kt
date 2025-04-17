package com.lejel.wowbox.apps.notice.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.notice.data.NoticeAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Notice
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityNoticeBinding
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
                        if(mTotalCount == 0){
                            binding.textNoticeNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textNoticeNotExist.visibility = View.GONE
                        }
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