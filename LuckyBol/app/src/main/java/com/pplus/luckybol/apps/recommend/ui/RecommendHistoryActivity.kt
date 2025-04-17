package com.pplus.luckybol.apps.recommend.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.recommend.data.RecommendHistoryAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityRecommendListBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class RecommendHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_lotto_recommend"
    }

    private lateinit var binding: ActivityRecommendListBinding

    override fun getLayoutView(): View {
        binding = ActivityRecommendListBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: RecommendHistoryAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerRecommendHistory.layoutManager = mLayoutManager
        mAdapter = RecommendHistoryAdapter(this)
        binding.recyclerRecommendHistory.adapter = mAdapter
//        recycler_recommend_history.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_20))

        binding.recyclerRecommendHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        getCount()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["recommendKey"] = LoginInfoManager.getInstance().user.recommendKey!!
        showProgress("")
        ApiBuilder.create().getUserCountByRecommendKey(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data!!

                binding.textRecommendHistoryCount.text = getString(R.string.format_recommend_history_count, FormatUtil.getMoneyType(mTotalCount.toString()))
                if (mTotalCount == 0) {
                    binding.layoutRecommendHistoryNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutRecommendHistoryNotExist.visibility = View.GONE
                }
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
        params["recommendKey"] = LoginInfoManager.getInstance().user.recommendKey!!
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getUserListByRecommendKey(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                mLockListView = false

                hideProgress()
                mAdapter?.addAll(response!!.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_recommend_history), ToolbarOption.ToolbarMenu.LEFT)
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
