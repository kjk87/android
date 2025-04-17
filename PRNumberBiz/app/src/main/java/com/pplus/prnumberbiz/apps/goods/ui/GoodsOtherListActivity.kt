//package com.pplus.prnumberbiz.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberbiz.apps.goods.data.GoodsOtherAdapter
//import com.pplus.prnumberbiz.core.code.common.EnumData
//import com.pplus.prnumberbiz.core.network.ApiBuilder
//import com.pplus.prnumberbiz.core.network.model.dto.Goods
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
//import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
//import kotlinx.android.synthetic.main.activity_goods_other_list.*
//import network.common.PplusCallback
//import retrofit2.Call
//import java.util.HashMap
//
//class GoodsOtherListActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private var mAdapter: GoodsOtherAdapter? = null
//
//    private var mPage: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_other_list
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_goods_other_list.layoutManager = mLayoutManager
//        mAdapter = GoodsOtherAdapter(this)
//        recycler_goods_other_list.adapter = mAdapter
//
//        recycler_goods_other_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : GoodsOtherAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(this@GoodsOtherListActivity, GoodsDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.putExtra(Const.OTHER, true)
//                startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//        })
//
//        mPage = 0
//        listCall(mPage)
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["sort"] = mSortType
//        params["expired"] = "0" //expired ( 1: 기한 완료,  0  : 기한 남은것, null : 전체)
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//
//                if (response != null) {
//
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                        if (mTotalCount == 0) {
//                            layout_goods_other_list_not_exist.visibility = View.VISIBLE
//                        } else {
//                            layout_goods_other_list_not_exist.visibility = View.GONE
//                        }
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_REG, Const.REQ_DETAIL -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    mPage = 0
//                    listCall(mPage)
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_view_real_time_deal), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
