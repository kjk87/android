package com.pplus.prnumberuser.apps.bol.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.data.BolAdapter
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Bol
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.databinding.ActivityBolConfigBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class BolConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_pointlist"
    }

    private lateinit var binding: ActivityBolConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityBolConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSort = "new"
    private var mAdapter: BolAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textBolConfigSortPast.setOnClickListener {
            mSort = "old"
            binding.textBolConfigSortPast.isSelected = true
            binding.textBolConfigSortRecent.isSelected = false

            getCount()
        }

        binding.textBolConfigSortRecent.setOnClickListener {
            mSort = "new"

            binding.textBolConfigSortPast.isSelected = false
            binding.textBolConfigSortRecent.isSelected = true
            getCount()
        }

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBolConfig.layoutManager = mLayoutManager
        mAdapter = BolAdapter()
        binding.recyclerBolConfig.adapter = mAdapter
//        recycler_bol_config.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))

        binding.recyclerBolConfig.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : BolAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@BolConfigActivity, BolHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

//        layout_bol_config_cash_exchange.setOnClickListener {
//            val intent = Intent(this, CashExchangeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }
//
//        layout_bol_config_lotto.setOnClickListener {
//            val intent = Intent(this, LottoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }
//
//        layout_bol_config_play.setOnClickListener {
//            val intent = Intent(this, PlayActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }
//
//        layout_bol_config_get_bol.setOnClickListener {
//            val intent = Intent(this, GetBolActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }



        binding.textBolConfigSortPast.isSelected = false
        binding.textBolConfigSortRecent.isSelected = true

        val bol = intent.getParcelableExtra<Bol>(Const.DATA)
        if(bol != null){
            val intent = Intent(this@BolConfigActivity, BolHistoryDetailActivity::class.java)
            intent.putExtra(Const.DATA, bol)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        getRetentionBol()
        getCount()
    }

    private fun getRetentionBol(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textBolConfigRetentionBol.text = getString(R.string.format_cash_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
            }
        })
    }

    private fun getCount() {
        val params = HashMap<String, String>()

        ApiBuilder.create().getBolHistoryCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {

                if (isFinishing) {
                    return
                }

                mTotalCount = response.data
                if (mTotalCount > 0) {
                    binding.textBolConfigNotExist.visibility = View.GONE
                } else {
                    binding.textBolConfigNotExist.visibility = View.VISIBLE
                }
                mPage = 1
                mAdapter!!.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable,
                                   response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        params["align"] = mSort
        params["pg"] = "" + page
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getBolHistoryListWithTargetList(params).setCallback(object : PplusCallback<NewResultResponse<Bol>> {

            override fun onResponse(call: Call<NewResultResponse<Bol>>,
                                    response: NewResultResponse<Bol>) {

                hideProgress()
                if (isFinishing) {
                    return
                }
                mLockListView = false

                mAdapter!!.addAll(response.datas)

            }

            override fun onFailure(call: Call<NewResultResponse<Bol>>,
                                   t: Throwable,
                                   response: NewResultResponse<Bol>) {

                hideProgress()
            }
        }).build().call()
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getRetentionBol()
        getCount()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_config), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_free_charge_station))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
