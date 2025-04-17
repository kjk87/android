package com.pplus.luckybol.apps.point.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.igaworks.adpopcorn.IgawAdpopcorn
import com.igaworks.adpopcorn.interfaces.IAdPOPcornEventListener
import com.igaworks.adpopcorn.style.ApStyleManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.point.data.PointHistoryAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.PointHistory
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityPointHistoryBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class PointHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_pointlist"
    }

    private lateinit var binding: ActivityPointHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityPointHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSort = "seqNo,desc"
    private var mAdapter: PointHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textPointHistorySortPast.setOnClickListener {
            mSort = "seqNo,asc"
            binding.textPointHistorySortPast.isSelected = true
            binding.textPointHistorySortRecent.isSelected = false

            mPage = 0
            listCall(mPage)
        }

        binding.textPointHistorySortRecent.setOnClickListener {
            mSort = "seqNo,desc"

            binding.textPointHistorySortPast.isSelected = false
            binding.textPointHistorySortRecent.isSelected = true
            mPage = 0
            listCall(mPage)
        }

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerPointHistory.layoutManager = mLayoutManager
        mAdapter = PointHistoryAdapter()
        binding.recyclerPointHistory.adapter = mAdapter
//        recycler_point_history.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))

        binding.recyclerPointHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

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

        mAdapter!!.setOnItemClickListener(object : PointHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@PointHistoryActivity, PointHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        binding.textPointHistorySortPast.isSelected = false
        binding.textPointHistorySortRecent.isSelected = true

        val pintHistory = intent.getParcelableExtra<PointHistory>(Const.DATA)
        if(pintHistory != null){
            val intent = Intent(this@PointHistoryActivity, PointHistoryDetailActivity::class.java)
            intent.putExtra(Const.DATA, pintHistory)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        IgawAdpopcorn.setEventListener(this, object : IAdPOPcornEventListener {

            override fun OnAgreePrivacy() {

            }

            override fun OnClosedOfferWallPage() {
                if (LoginInfoManager.getInstance().isMember) {
                    getRetentionBol()
                    mPage = 0
                    listCall(mPage)
                }
            }
        })

        getRetentionBol()
        mPage = 0
        listCall(mPage)
    }

    private fun getRetentionBol(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textPointHistoryRetentionPoint.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString()))
            }
        })
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        params["sort"] = mSort
        params["page"] = page.toString()
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getPointHistoryList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<PointHistory>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<PointHistory>>>?,
                                    response: NewResultResponse<SubResultResponse<PointHistory>>?) {
                hideProgress()
                mLockListView = false
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        mAdapter!!.clear()
                        if(mTotalCount == 0){
                            binding.textPointHistoryNotExist.visibility = View.VISIBLE
                        }else{
                            binding.textPointHistoryNotExist.visibility = View.GONE
                        }
                    }


                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<PointHistory>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<PointHistory>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_config), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_free_charge_station))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    IgawAdpopcorn.setUserId(this@PointHistoryActivity, LoginInfoManager.getInstance().user.no.toString())
                    val optionMap = HashMap<String, Any>()
                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_TITLE_TEXT, getString(R.string.word_bol_charge_station))
                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#fc5c57"))
                    optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
                    optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
                    ApStyleManager.setCustomOfferwallStyle(optionMap)
                    IgawAdpopcorn.openOfferWall(this@PointHistoryActivity)
                }
            }
        }
    }
}
