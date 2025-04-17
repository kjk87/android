package com.pplus.prnumberbiz.apps.bol.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.bol.data.PointAdapter
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Bol
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_point_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class PointConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_mypage_luckybol"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_point_config
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSort = "new"
    private var mAdapter: PointAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        text_point_config_sort_past.setOnClickListener {
            mSort = "old"
            text_point_config_sort_past.isSelected = true
            text_point_config_sort_recent.isSelected = false

            getCount()
        }

        text_point_config_sort_recent.setOnClickListener {
            mSort = "new"

            text_point_config_sort_past.isSelected = false
            text_point_config_sort_recent.isSelected = true
            getCount()
        }

        mLayoutManager = LinearLayoutManager(this)
        recycler_point_config.layoutManager = mLayoutManager
        mAdapter = PointAdapter(this)
        recycler_point_config.adapter = mAdapter
//        recycler_point_config.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))

        recycler_point_config.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PointAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@PointConfigActivity, PointHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                text_point_config_retention_point.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
            }
        })

        text_point_config_sort_past.isSelected = false
        text_point_config_sort_recent.isSelected = true

        val bol = intent.getParcelableExtra<Bol>(Const.DATA)
        if(bol != null){

        }

        getCount()
    }

    private fun getCount() {
        val params = HashMap<String, String>()

        ApiBuilder.create().getBolHistoryCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                mTotalCount = response.data
                if(mTotalCount > 0){
                    text_point_config_not_exist.visibility = View.GONE
                }else{
                    text_point_config_not_exist.visibility = View.VISIBLE
                }
                mPage = 1
                mAdapter!!.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

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

            override fun onResponse(call: Call<NewResultResponse<Bol>>, response: NewResultResponse<Bol>) {

                hideProgress()
                mLockListView = false

                mAdapter!!.addAll(response.datas)

            }

            override fun onFailure(call: Call<NewResultResponse<Bol>>, t: Throwable, response: NewResultResponse<Bol>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
