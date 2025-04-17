package com.lejel.wowbox.apps.invite.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.invite.data.InviteMiningAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.BuffInviteMining
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityInviteMiningListBinding
import retrofit2.Call

class InviteMiningListActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityInviteMiningListBinding

    override fun getLayoutView(): View {
        binding = ActivityInviteMiningListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    private lateinit var mAdapter: InviteMiningAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerInviteMining.layoutManager = mLayoutManager
        mAdapter = InviteMiningAdapter()
        binding.recyclerInviteMining.adapter = mAdapter

        binding.recyclerInviteMining.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        if(LoginInfoManager.getInstance().member!!.inviteCount == null){
            LoginInfoManager.getInstance().member!!.inviteCount = 0
        }

        binding.textInviteMiningInviteCount.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.inviteCount.toString())

        getSumCoin()
    }

    private fun getSumCoin(){

        showProgress("")
        ApiBuilder.create().getBuffInviteMiningTotalCoin().setCallback(object : PplusCallback<NewResultResponse<Double>>{
            override fun onResponse(call: Call<NewResultResponse<Double>>?, response: NewResultResponse<Double>?) {
                hideProgress()
                if(response?.result != null){
                    binding.textInviteMiningBuffAmount.text = FormatUtil.getMoneyTypeFloat(response.result.toString())
                }else{
                    binding.textInviteMiningBuffAmount.text = "0"
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
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getBuffInviteMiningList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<BuffInviteMining>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<BuffInviteMining>>>?, response: NewResultResponse<ListResultResponse<BuffInviteMining>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<BuffInviteMining>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<BuffInviteMining>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_invite_mining_history), ToolbarOption.ToolbarMenu.LEFT)
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