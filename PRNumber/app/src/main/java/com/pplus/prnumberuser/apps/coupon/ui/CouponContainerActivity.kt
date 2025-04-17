//package com.pplus.prnumberuser.apps.coupon.ui
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsLikeAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_goods_like.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class CouponContainerActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.fragment_goods_like
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: GoodsLikeAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//    private var mSort = "goodsSeqNo,${EnumData.GoodsSort.desc}"
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_goods_like.layoutManager = mLayoutManager
//        recycler_goods_like.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_150))
//        mAdapter = GoodsLikeAdapter()
//        recycler_goods_like.adapter = mAdapter
//        recycler_goods_like.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : GoodsLikeAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(this@CouponContainerActivity, CouponDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position).goods)
//                startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//        })
//
//        mAdapter!!.setOnItemDeleteListener(object : GoodsLikeAdapter.OnItemDeleteListener {
//            override fun onItemDelete() {
//                mPaging = 0
//                listCall(mPaging)
//            }
//        })
//
//        text_goods_like_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_new_reg), getString(R.string.word_sort_expired))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            mSort = "goodsSeqNo,${EnumData.GoodsSort.desc}"
//                            text_goods_like_sort.text = getString(R.string.word_sort_new_reg)
//                        }
//                        2 -> {
//                            mSort = "expireDatetime,${EnumData.GoodsSort.asc}"
//                            text_goods_like_sort.text = getString(R.string.word_sort_expired)
//                        }
//                    }
//                    mPaging = 0
//                    listCall(mPaging)
//
//                }
//            }).builder().show(this)
//        }
//
//        mPaging = 0
//        listCall(mPaging)
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["sort"] = mSort
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsLike(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsLike>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsLike>>>?, response: NewResultResponse<SubResultResponse<GoodsLike>>?) {
//                hideProgress()
//
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        text_goods_like_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if(mTotalCount > 0){
//                            layout_goods_like_not_exist.visibility = View.GONE
//                        }else{
//                            layout_goods_like_not_exist.visibility = View.VISIBLE
//                        }
//
//                        mAdapter!!.clear()
//                    }
//
//                    mLockListView = false
//
//                    val dataList = response.data.content!!
//                    mAdapter!!.addAll(dataList)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsLike>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<GoodsLike>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon_container), ToolbarOption.ToolbarMenu.LEFT)
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
//
//    }
//}
