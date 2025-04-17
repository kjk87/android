package com.root37.buflexz.apps.luckydraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.luckydraw.data.LuckyDrawWinAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.dto.LuckyDrawWin
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLuckyDrawWinBinding
import retrofit2.Call

class LuckyDrawWinActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawWinBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawWinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mAdapter: LuckyDrawWinAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1

    private lateinit var mLuckyDraw: LuckyDraw

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!

        binding.textLuckyDrawWinDrawTitle.text = mLuckyDraw.title
        binding.textLuckyDrawWinTotalPrice.text = FormatUtil.getMoneyTypeFloat(mLuckyDraw.totalPrice.toString())

        mLayoutManager = LinearLayoutManager(this)
        mAdapter = LuckyDrawWinAdapter()
        mAdapter!!.launcher = defaultLauncher
        binding.recyclerLuckyDrawWin.adapter = mAdapter
        binding.recyclerLuckyDrawWin.layoutManager = mLayoutManager

        binding.recyclerLuckyDrawWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : LuckyDrawWinAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                if (LoginInfoManager.getInstance().isMember() && item.userKey == LoginInfoManager.getInstance().member!!.userKey) {
                    val builder = AlertBuilder.Builder()
                    builder.setContents(getString(R.string.word_modify_win_impression), getString(R.string.word_cancel))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert.value) {
                                1 -> {
                                    val intent = Intent(this@LuckyDrawWinActivity, LuckyDrawWinImpressionActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    defaultLauncher.launch(intent)
                                }
                            }

                        }
                    }).builder().show(this@LuckyDrawWinActivity)
                }
            }
        }

        mPaging = 1
        getList(mPaging)
    }

    private fun getList(page: Int) {
        val params = HashMap<String, String>()
        params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLuckyDrawWinList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
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

                if(LoginInfoManager.getInstance().isMember()){
                    getMyWinList()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getMyWinList() {
        val params = HashMap<String, String>()
        params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getLuckyDrawMyWinList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    for(win in response.result!!.list!!){
                        if(StringUtils.isEmpty(win.impression)){
                            val intent = Intent(this@LuckyDrawWinActivity, AlertMyLuckyDrawWinActivity::class.java)
                            intent.putExtra(Const.DATA, win)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                            break
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_view_winner), ToolbarOption.ToolbarMenu.LEFT)
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