//package com.pplus.prnumberuser.apps.goods.ui
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
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
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
//class GoodsLikeFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_goods_like
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: GoodsLikeAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//    private var mSort = "goods_seq_no,${EnumData.GoodsSort.desc}"
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_goods_like.layoutManager = mLayoutManager
//        mAdapter = GoodsLikeAdapter()
//        recycler_goods_like.adapter = mAdapter
//        recycler_goods_like.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_200))
////        layout_buy_history_top.visibility = View.VISIBLE
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
//                if(mAdapter!!.getItem(position).goods!!.salesType == 3){
//                    val intent = Intent(activity, GoodsDetailShipTypeActivity::class.java)
//                    intent.putExtra(Const.DATA, mAdapter!!.getItem(position).goods)
//                    activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                }else{
//                    val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                    intent.putExtra(Const.DATA, mAdapter!!.getItem(position).goods)
//                    activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                }
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
//                            mSort = "goods_seq_no,${EnumData.GoodsSort.desc}"
//                            text_goods_like_sort.text = getString(R.string.word_sort_new_reg)
//                        }
//                        2 -> {
//                            mSort = "expire_datetime,${EnumData.GoodsSort.asc}"
//                            text_goods_like_sort.text = getString(R.string.word_sort_expired)
//                        }
//                    }
//                    mPaging = 0
//                    listCall(mPaging)
//
//                }
//            }).builder().show(activity)
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
//        params["salesType"] = "1"
//        params["sort"] = mSort
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsLikeBySalesType(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsLike>>> {
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
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                GoodsLikeFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
