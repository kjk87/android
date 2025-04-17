//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_goods_list.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class GoodsListActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private var mAdapter: GoodsAdapter? = null
//
//    private var mPaging: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
//    private var mPage: Page? = null
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_goods_list
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPage = intent.getParcelableExtra(Const.PAGE)
//
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
//        recycler_goods_list.layoutManager = mLayoutManager
//        mAdapter = GoodsAdapter(this)
//        recycler_goods_list.adapter = mAdapter
//
//        recycler_goods_list.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : GoodsAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(this@GoodsListActivity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
//        text_goods_list_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_past), getString(R.string.word_sort_high_price), getString(R.string.word_sort_low_price))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            text_goods_list_sort.setText(R.string.word_sort_recent)
//                            mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
//                        }
//                        2 -> {
//                            text_goods_list_sort.setText(R.string.word_sort_past)
//                            mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.asc}"
//                        }
//                        3 -> {
//                            text_goods_list_sort.setText(R.string.word_sort_high_price)
//                            mSortType = "${EnumData.GoodsSort.price.name},${EnumData.GoodsSort.desc}"
//                        }
//                        4 -> {
//                            text_goods_list_sort.setText(R.string.word_sort_low_price)
//                            mSortType = "${EnumData.GoodsSort.price.name},${EnumData.GoodsSort.asc}"
//                        }
//                    }
//                }
//            }).builder().show(this)
//        }
//
//        mPaging = 0
//        listCall(mPaging)
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["sort"] = mSortType
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["pageSeqNo"] = mPage!!.no.toString()
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
//                        text_goods_list_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if (mTotalCount == 0) {
//                            layout_goods_list_not_exist.visibility = View.VISIBLE
//                        } else {
//                            layout_goods_list_not_exist.visibility = View.GONE
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
//            Const.REQ_GOODS_DETAIL -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pre_pay_sale_goods), ToolbarOption.ToolbarMenu.LEFT)
//
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
