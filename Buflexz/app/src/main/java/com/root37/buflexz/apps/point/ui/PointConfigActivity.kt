package com.root37.buflexz.apps.point.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.point.data.HistoryPointAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.HistoryPoint
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityPointConfigBinding
import retrofit2.Call

class PointConfigActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityPointConfigBinding

    override fun getLayoutView(): View {
        binding = ActivityPointConfigBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount = 0
    var mAdapter: HistoryPointAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    private var mDir = "DESC"

    override fun initializeView(savedInstanceState: Bundle?) {
        reloadSession()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerPointConfig.layoutManager = mLayoutManager
        mAdapter = HistoryPointAdapter()
        binding.recyclerPointConfig.adapter = mAdapter

        binding.recyclerPointConfig.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter?.listener = object : HistoryPointAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@PointConfigActivity, HistoryPointActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter?.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
            }
        }

        binding.textPointConfigSort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_past))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.value) {
                        1 -> {
                            binding.textPointConfigSort.setText(R.string.word_sort_recent)
                            mDir = "DESC"
                        }

                        2 -> {
                            binding.textPointConfigSort.setText(R.string.word_sort_past)
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
                binding.textPointConfigRetentionPoint.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString())
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
        ApiBuilder.create().getHistoryPointList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<HistoryPoint>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<HistoryPoint>>>?, response: NewResultResponse<ListResultResponse<HistoryPoint>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount > 0) {
                            binding.layoutPointConfigNotExist.visibility = View.GONE
                            binding.recyclerPointConfig.visibility = View.VISIBLE
                        } else {
                            binding.layoutPointConfigNotExist.visibility = View.VISIBLE
                            binding.recyclerPointConfig.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<HistoryPoint>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<HistoryPoint>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        reloadSession()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
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