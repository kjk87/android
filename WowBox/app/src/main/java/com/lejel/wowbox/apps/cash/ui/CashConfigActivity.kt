package com.lejel.wowbox.apps.cash.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.cash.data.HistoryCashAdapter
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.HistoryCash
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityCashConfigBinding
import retrofit2.Call

class CashConfigActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityCashConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityCashConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: HistoryCashAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "DESC"

    override fun initializeView(savedInstanceState: Bundle?) {
        reloadSession()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerCashConfig.layoutManager = mLayoutManager
        mAdapter = HistoryCashAdapter()
        binding.recyclerCashConfig.adapter = mAdapter

        binding.recyclerCashConfig.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter?.listener = object : HistoryCashAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@CashConfigActivity, HistoryCashActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter?.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.textCashConfigSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_past))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            binding.textCashConfigSort.setText(R.string.word_sort_recent)
                            mDir = "DESC"
                        }

                        2 -> {
                            binding.textCashConfigSort.setText(R.string.word_sort_past)
                            mDir = "ASC"
                        }
                    }
                    mPaging = 1
                    getList(mPaging)

                }
            }).builder().show(this)
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textCashConfigRetentionCash.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.cash!!.toInt().toString())
            }
        })
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        params["order[][column]"] = "seqNo"
        params["order[][dir]"] = mDir
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getHistoryCashList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<HistoryCash>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<HistoryCash>>>?, response: NewResultResponse<ListResultResponse<HistoryCash>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount > 0) {
                            binding.layoutCashConfigNotExist.visibility = View.GONE
                            binding.recyclerCashConfig.visibility = View.VISIBLE
                        } else {
                            binding.layoutCashConfigNotExist.visibility = View.VISIBLE
                            binding.recyclerCashConfig.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<HistoryCash>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<HistoryCash>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        reloadSession()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_rp_config), ToolbarOption.ToolbarMenu.LEFT)
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