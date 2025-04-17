//package com.pplus.prnumberuser.apps.category.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.data.GoodsAdapter
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity2
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import kotlinx.android.synthetic.main.fragment_category_goods.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class CategoryGoodsFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_category_goods
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mSortType = "${EnumData.GoodsSort.distance.name},${EnumData.GoodsSort.asc}"
//    private var mAdapter: GoodsAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var category: Category? = null
//
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_category_goods.layoutManager = mLayoutManager
//        mAdapter = GoodsAdapter(activity!!)
//        recycler_category_goods.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
//        recycler_category_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                activity?.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
//        text_category_goods_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_distance), getString(R.string.word_sort_recent))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            text_category_goods_sort.setText(R.string.word_sort_distance)
//                            mSortType = "${EnumData.GoodsSort.distance.name},${EnumData.GoodsSort.asc}"
//                        }
//                        2 -> {
//                            text_category_goods_sort.setText(R.string.word_sort_recent)
//                            mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
//                        }
//                    }
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }).builder().show(activity)
//        }
//
//        text_category_goods_sort.setText(R.string.word_sort_distance)
//
//        setData()
//    }
//
//    private fun setData() {
//        mLocationData = LocationUtil.getSpecifyLocationData()
//        if (mLocationData != null) {
//
//            mPaging = 0
//            listCall(mPaging)
//        }
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
////        params["sort"] = mSortType
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["latitude"] = mLocationData!!.latitude.toString()
//        params["longitude"] = mLocationData!!.longitude.toString()
//
//        if(category!!.depth == 1){
//            params["pageParentCategorySeqNo"] = category!!.no.toString()
//        }else{
//            params["pageCategorySeqNo"] = category!!.no.toString()
//        }
//
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getGoodsLocation(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                hideProgress()
//
//                if (response != null) {
//
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
////                        text_goods_list_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        if (mTotalCount == 0) {
//                            layout_category_goods_not_exist.visibility = View.VISIBLE
//                        } else {
//                            layout_category_goods_not_exist.visibility = View.GONE
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
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position == 0) {
//                outRect.set(0, mTopOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
//
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                if (resultCode == RESULT_OK) {
//                    setData()
//                }
//            }
//            Const.REQ_SEARCH -> {
//                setData()
//            }
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
//            category = it.getParcelable(Const.DATA)
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(category: Category) =
//                CategoryGoodsFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.DATA, category)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
