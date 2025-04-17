package com.lejel.wowbox.apps.partner.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.partner.data.ChildProfitPartnerAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.ProfitPartner
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityBonusProfitDetailBinding
import retrofit2.Call
import java.text.SimpleDateFormat

class BonusProfitDetailActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBonusProfitDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityBonusProfitDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: ChildProfitPartnerAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    lateinit var mProfitPartner: ProfitPartner

    override fun initializeView(savedInstanceState: Bundle?) {
        mProfitPartner = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, ProfitPartner::class.java)!!

        val calMonth = SimpleDateFormat(getString(R.string.word_date_format)).format(SimpleDateFormat("yyyy-MM-dd").parse(mProfitPartner.calculateMonth))
        binding.textBonusProfitDetailTitle.text = getString(R.string.format_mater_profit, calMonth)
        binding.textBonusProfitDetailBonusProfit.text = FormatUtil.getMoneyTypeFloat(mProfitPartner.bonusProfit.toString())
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBonusProfitDetail.layoutManager = mLayoutManager
        mAdapter = ChildProfitPartnerAdapter()
        binding.recyclerBonusProfitDetail.adapter = mAdapter

        binding.recyclerBonusProfitDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.textBonusProfitDetailChildCount.setOnClickListener {
            val intent = Intent(this, ChildPartnerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        getChildCount()
    }

    private fun getChildCount() {
        showProgress("")
        ApiBuilder.create().getChildPartnerCount().setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.result != null) {
                    binding.textBonusProfitDetailChildCount.text = response.result.toString()
                } else {
                    binding.textBonusProfitDetailChildCount.text = "0"
                }

                getTotalBonusProfit()
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getTotalBonusProfit() {
        showProgress("")
        ApiBuilder.create().getTotalBonusProfit().setCallback(object : PplusCallback<NewResultResponse<Double>> {
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()
                if (response?.result != null) {
                    binding.textBonusProfitDetailTotalBonusProfit.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                } else {
                    binding.textBonusProfitDetailTotalBonusProfit.text = "0"
                }

                mPaging = 1
                getList(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Double>>?, t: Throwable?, response: NewResultResponse<Double>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["order[][column]"] = "seqNo"
        params["order[][dir]"] = "DESC"
        params["calculateMonth"] = mProfitPartner.calculateMonth!!
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getChildProfitPartnerList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<ProfitPartner>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<ProfitPartner>>>?, response: NewResultResponse<ListResultResponse<ProfitPartner>>?) {
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

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<ProfitPartner>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<ProfitPartner>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_master_profit_detail), ToolbarOption.ToolbarMenu.LEFT)
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