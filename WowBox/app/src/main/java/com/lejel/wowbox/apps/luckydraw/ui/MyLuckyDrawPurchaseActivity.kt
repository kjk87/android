package com.lejel.wowbox.apps.luckydraw.ui

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
import com.lejel.wowbox.apps.luckydraw.data.LuckyDrawPurchaseAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDrawPurchase
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityMyLuckyDrawPurchaseBinding
import retrofit2.Call

class MyLuckyDrawPurchaseActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMyLuckyDrawPurchaseBinding

    override fun getLayoutView(): View {
        binding = ActivityMyLuckyDrawPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: LuckyDrawPurchaseAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = LinearLayoutManager(this)
        mAdapter = LuckyDrawPurchaseAdapter()
        binding.recyclerMyLuckyDrawPurchase.adapter = mAdapter
        binding.recyclerMyLuckyDrawPurchase.layoutManager = mLayoutManager

        binding.recyclerMyLuckyDrawPurchase.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLuckyDrawPurchaseList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawPurchase>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutMyLuckyDrawPurchaseNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMyLuckyDrawPurchaseNotExist.visibility = View.GONE
                        }

                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawPurchase>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lucky_draw_purchase_history), ToolbarOption.ToolbarMenu.LEFT)
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