//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailShipTypeActivity
//import com.pplus.prnumberuser.apps.main.data.MainHotDealShipTypeAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.CategoryFirst
//import com.pplus.prnumberuser.core.network.model.dto.GoodsPrice
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import kotlinx.android.synthetic.main.fragment_hot_deal_ship_type_list.*
//import retrofit2.Call
//import java.util.*
//
//class ShipTypeListFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_hot_deal_ship_type_list
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mAdapter: MainHotDealShipTypeAdapter? = null
//    private var mLayoutManager: GridLayoutManager? = null
//    private var category: CategoryFirst? = null
//    private var mIsLast = false
//    private var mSort = "seqNo,desc"
//
//    override fun init() {
//
//        mLayoutManager = GridLayoutManager(activity, 2)
//        recycler_hot_deal_ship_type_list.layoutManager = mLayoutManager!!
//        mAdapter = MainHotDealShipTypeAdapter()
//        recycler_hot_deal_ship_type_list.adapter = mAdapter
//        recycler_hot_deal_ship_type_list.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60))
//        recycler_hot_deal_ship_type_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MainHotDealShipTypeAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//
////                if (!PplusCommonUtil.loginCheck(activity!!)) {
////                    return
////                }
//
//                val intent = Intent(activity, GoodsDetailShipTypeActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//        })
//
//        text_hot_deal_ship_type_list_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_discount), getString(R.string.word_sort_bol))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            mSort = "seqNo,desc"
//                            text_hot_deal_ship_type_list_sort.text = getString(R.string.word_sort_recent)
//                        }
//                        2 -> {
//                            mSort = "discount_ratio,desc"
//                            text_hot_deal_ship_type_list_sort.text = getString(R.string.word_sort_discount)
//                        }
//                        3 -> {
//                            mSort = "price,desc"
//                            text_hot_deal_ship_type_list_sort.text = getString(R.string.word_sort_bol)
//                        }
//                    }
//                    mPaging = 0
//                    listCall(mPaging)
//
//                }
//            }).builder().show(activity)
//        }
//
//        layout_hot_deal_ship_type_loading?.visibility = View.VISIBLE
//        mSort = "seqNo,desc"
//        text_hot_deal_ship_type_list_sort.text = getString(R.string.word_sort_recent)
//        setData()
//    }
//
//    fun setData() {
//        mPaging = 0
//        listCall(mPaging)
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//        if(category!!.seqNo != -1L){
//            params["first"] = category!!.seqNo.toString()
//        }
//
//        params["page"] = page.toString()
//        params["sort"] = mSort
////        showProgress("")
//        ApiBuilder.create().getGoodsPriceShipType(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsPrice>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsPrice>>>?,
//                                    response: NewResultResponse<SubResultResponse<GoodsPrice>>?) {
//                if (!isAdded) {
//                    return
//                }
//
//                layout_hot_deal_ship_type_loading?.visibility = View.GONE
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        text_hot_deal_ship_type_list_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                        mAdapter!!.clear()
//                        if(mTotalCount == 0){
//                            layout_hot_deal_ship_type_list_not_exist?.visibility = View.VISIBLE
//                        }else{
//                            layout_hot_deal_ship_type_list_not_exist?.visibility = View.GONE
//                        }
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsPrice>>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<SubResultResponse<GoodsPrice>>?) {
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                layout_hot_deal_ship_type_loading?.visibility = View.GONE
//            }
//
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position <= 1) {
//                outRect.set(0, mItemOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
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
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(category: CategoryFirst) =
//                ShipTypeListFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.DATA, category)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
