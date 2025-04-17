package com.pplus.luckybol.apps.mobilegift.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.mobilegift.data.GiftishowBuyHistoryAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.GiftishowBuy
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.ActivityMobileGiftHistoryBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*

class GiftishowHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return "Main_bolpoint_pointshop_buylist"
    }

    private lateinit var binding: ActivityMobileGiftHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityMobileGiftHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mAdapter: GiftishowBuyHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerMobileGiftHistory.layoutManager = mLayoutManager
        mAdapter = GiftishowBuyHistoryAdapter()
        binding.recyclerMobileGiftHistory.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : GiftishowBuyHistoryAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GiftishowHistoryActivity, GiftishowHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                detailLauncher.launch(intent)
            }
        })
        binding.recyclerMobileGiftHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mPage = 0
        listCall(mPage)
    }


    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["sort"] = "seqNo,desc"
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getGiftishowBuyList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GiftishowBuy>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GiftishowBuy>>>?,
                                    response: NewResultResponse<SubResultResponse<GiftishowBuy>>?) {
                hideProgress()
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        if(mTotalCount == 0){
                            binding.layoutMobileGiftHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutMobileGiftHistoryNotExist.visibility = View.GONE
                        }
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GiftishowBuy>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<GiftishowBuy>>?) {
                mLockListView = false
                hideProgress()
            }

        }).build().call()
    }

    val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            mPage = 0
            listCall(mPage)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_change_history), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}