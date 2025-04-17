//package com.pplus.prnumberuser.apps.page.ui
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.PlusGoodsAdapter
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity2
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_event_goods.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class EventGoodsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_event_goods
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: PlusGoodsAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//    private var mPage: Page? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mPage = intent.getParcelableExtra(Const.PAGE)
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_event_goods.layoutManager = mLayoutManager!!
//        mAdapter = PlusGoodsAdapter(this, false)
//        recycler_event_goods.adapter = mAdapter
//
//        recycler_event_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : PlusGoodsAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(this@EventGoodsActivity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
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
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//
//        params["pageSeqNo"] = mPage!!.no.toString()
//        if (mPage!!.type == EnumData.PageTypeCode.store.name) {
//            params["type"] = "0"
//        } else {
//            params["type"] = "1"
//        }
//        params["isPlus"] = "true"
//        params["isHotdeal"] = "false"
//        params["sort"] = "${EnumData.GoodsSort.news_datetime.name},${EnumData.BuyGoodsSort.desc.name}"
//        params["page"] = page.toString()
//        showProgress("")
//        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//
//                        text_event_goods_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_event_goods_count, FormatUtil.getMoneyType(mTotalCount.toString())))
////                        if (mTotalCount == 0) {
////                            layout_main_goods_plus_not_exist?.visibility = View.VISIBLE
////                        } else {
////                            layout_main_goods_plus_not_exist?.visibility = View.GONE
////                        }
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
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_event_goods), ToolbarOption.ToolbarMenu.LEFT)
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
