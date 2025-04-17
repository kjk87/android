package com.lejel.wowbox.apps.lottery.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.lottery.data.LotteryJoinListAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Lottery
import com.lejel.wowbox.core.network.model.dto.LotteryJoin
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityMyLotteryJoinListBinding
import retrofit2.Call

class MyLotteryJoinListActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMyLotteryJoinListBinding

    override fun getLayoutView(): View {
        binding = ActivityMyLotteryJoinListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: LotteryJoinListAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    var mLottery: Lottery? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = LinearLayoutManager(this)
        mAdapter = LotteryJoinListAdapter()
        binding.recyclerMyLotteryJoinList.adapter = mAdapter
        binding.recyclerMyLotteryJoinList.layoutManager = mLayoutManager

        binding.recyclerMyLotteryJoinList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        getJoinList(mPaging)
                    }
                }
            }
        })

        binding.textMyLotteryJoinNotExistJoin.setOnClickListener {
            val params = HashMap<String, String>()
            params["joinType"] = "advertise"
            showProgress("")
            ApiBuilder.create().lotteryJoin(params).setCallback(object : PplusCallback<NewResultResponse<LotteryJoin>> {
                override fun onResponse(call: Call<NewResultResponse<LotteryJoin>>?,
                                        response: NewResultResponse<LotteryJoin>?) {
                    hideProgress()

                    if(response?.result != null){
                        val lottoCount = PreferenceUtil.getDefaultPreference(this@MyLotteryJoinListActivity).get(Const.LOTTO_COUNT, 0)
                        PreferenceUtil.getDefaultPreference(this@MyLotteryJoinListActivity).put(Const.LOTTO_COUNT, lottoCount + 1)

                        PreferenceUtil.getDefaultPreference(this@MyLotteryJoinListActivity).put(Const.LOTTO_JOIN_TIME, System.currentTimeMillis())

                        val intent = Intent(this@MyLotteryJoinListActivity, LottoJoinResultActivity::class.java)
                        intent.putExtra(Const.DATA, response.result)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<LotteryJoin>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<LotteryJoin>?) {
                    hideProgress()
                }
            }).build().call()
        }

        getLottery()

    }

    private fun getLottery() {
        showProgress("")
        ApiBuilder.create().getLottery().setCallback(object : PplusCallback<NewResultResponse<Lottery>> {
            override fun onResponse(call: Call<NewResultResponse<Lottery>>?, response: NewResultResponse<Lottery>?) {
                hideProgress()
                if (response?.result != null) {
                    mLottery = response.result
                    binding.textMyLotteryJoinListCountTitle.text = getString(R.string.format_lotto_my_join_count_title, mLottery!!.lotteryRound.toString())

                    mPaging = 1
                    getJoinList(mPaging)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getJoinList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["lotteryRound"] = mLottery!!.lotteryRound.toString()
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLotteryJoinList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LotteryJoin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LotteryJoin>>>?, response: NewResultResponse<ListResultResponse<LotteryJoin>>?) {
                hideProgress()
                mLockListView = false
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()
                        mTotalCount = response.result!!.total!!
                        binding.textMyLotteryJoinListCount.text = getString(R.string.format_lotto_my_join_count, FormatUtil.getMoneyType(mTotalCount.toString()))

                        if(mTotalCount > 0){
                            binding.recyclerMyLotteryJoinList.visibility = View.VISIBLE
                            binding.layoutMyLotteryJoinNotExist.visibility = View.GONE
                        }else{
                            binding.recyclerMyLotteryJoinList.visibility = View.GONE
                            binding.layoutMyLotteryJoinNotExist.visibility = View.VISIBLE
                        }
                    }

                    if(response.result!!.list != null){
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LotteryJoin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LotteryJoin>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getJoinList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_join_history), ToolbarOption.ToolbarMenu.LEFT)
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