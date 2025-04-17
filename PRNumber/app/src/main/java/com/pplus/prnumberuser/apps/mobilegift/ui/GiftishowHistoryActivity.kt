package com.pplus.prnumberuser.apps.mobilegift.ui

import android.content.Intent
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
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.mobilegift.data.GiftishowBuyHistoryAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.GiftishowBuy
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.ActivityMobileGiftHistoryBinding
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
                startActivityForResult(intent, Const.REQ_DETAIL)
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
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        if(mTotalCount == 0){
                            binding.layoutMobileGiftHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.layoutMobileGiftHistoryNotExist.visibility = View.GONE
                        }
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL -> if (resultCode == RESULT_OK) {
                mPage = 0
                listCall(mPage)
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_change_history), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}