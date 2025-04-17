package com.lejel.wowbox.apps.event.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.data.MyWinHistoryAdapter
import com.lejel.wowbox.apps.event.data.TotalWinHistoryAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.ActivityMyWinHistoryBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*

class MyWinHistoryActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_mypage_Winning history_my"
    }

    private lateinit var binding: ActivityMyWinHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityMyWinHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: MyWinHistoryAdapter? = null
    var mTotalAdapter: TotalWinHistoryAdapter? = null
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun initializeView(savedInstanceState: Bundle?) {

        val tab = intent.getStringExtra(Const.TAB)

        mTotalAdapter = TotalWinHistoryAdapter()
        mAdapter = MyWinHistoryAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerWinHistory.layoutManager = mLayoutManager
        binding.recyclerWinHistory.adapter = mAdapter
        binding.recyclerWinHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        if (binding.textWinHistoryTotal.isSelected) {
                            listCall(mPage)
                        } else {
                            userListCall(mPage)
                        }
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : MyWinHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = mAdapter!!.getItem(position).event

                val intent = Intent(this@MyWinHistoryActivity, EventImpressionActivity::class.java)
                intent.putExtra(Const.DATA, event)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                resultLauncher.launch(intent)
            }
        })

        mTotalAdapter!!.setOnItemClickListener(object : TotalWinHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = mTotalAdapter!!.getItem(position)

                val intent = Intent(this@MyWinHistoryActivity, EventImpressionActivity::class.java)
                intent.putExtra(Const.DATA, event)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                resultLauncher.launch(intent)
            }
        })

        binding.layoutWinHistoryTotal.setOnClickListener {
            setSelect(binding.layoutWinHistoryTotal, binding.layoutWinHistoryMy)
            setBold(binding.textWinHistoryTotal, binding.textWinHistoryMy)
            binding.recyclerWinHistory.adapter = mTotalAdapter
            mPage = 1
            mTotalAdapter?.clear()
            mAdapter?.clear()
            listCall(mPage)
        }

        binding.layoutWinHistoryMy.setOnClickListener {
            setSelect(binding.layoutWinHistoryMy, binding.layoutWinHistoryTotal)
            setBold(binding.textWinHistoryMy, binding.textWinHistoryTotal)
            binding.recyclerWinHistory.adapter = mAdapter
            mPage = 1
            mTotalAdapter?.clear()
            mAdapter?.clear()
            userListCall(mPage)
        }

        setSelect(binding.layoutWinHistoryMy, binding.layoutWinHistoryTotal)
        setBold(binding.textWinHistoryMy, binding.textWinHistoryTotal)
        mPage = 1
        mTotalAdapter?.clear()
        mAdapter?.clear()
        userListCall(mPage)

    }

    private fun setSelect(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun setBold(view1: TextView, view2: TextView) {

        view1.typeface = Typeface.DEFAULT_BOLD
        view2.typeface = Typeface.DEFAULT
    }

    private fun userListCall(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getUserWinList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EventWin>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                mLockListView = false
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter?.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount > 0) {
                            binding.textWinHistoryNotExist.visibility = View.GONE
                        } else {
                            binding.textWinHistoryNotExist.visibility = View.VISIBLE
                        }

                    }
                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter?.addAll(dataList)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
                mLockListView = false
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getWinAnnounceList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Event>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Event>>>?, response: NewResultResponse<ListResultResponse<Event>>?) {
                mLockListView = false
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mTotalAdapter?.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount > 0) {
                            binding.textWinHistoryNotExist.visibility = View.GONE
                        } else {
                            binding.textWinHistoryNotExist.visibility = View.VISIBLE
                        }

                    }
                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mTotalAdapter?.addAll(dataList)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Event>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Event>>?) {
                mLockListView = false
                hideProgress()
            }
        }).build().call()
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (binding.textWinHistoryTotal.isSelected) {
            mPage = 1
            mTotalAdapter?.clear()
            mAdapter?.clear()
            listCall(mPage)
        } else {
            mPage = 1
            mTotalAdapter?.clear()
            mAdapter?.clear()
            userListCall(mPage)
        }
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}
