//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.data.PlusGoodsAdapter
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity2
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.SortCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import kotlinx.android.synthetic.main.fragment_main_goods_plus.*
//import retrofit2.Call
//import java.util.*
//
//class MainGoodsPlusFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_goods_plus
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mSortCode: SortCode = SortCode.distance
//    private var mAdapter: PlusGoodsAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var mIsLast = false
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_main_goods_plus.layoutManager = mLayoutManager!!
//        mAdapter = PlusGoodsAdapter(activity!!, true)
//        recycler_main_goods_plus.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
////        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
//        recycler_main_goods_plus.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
////        text_main_goods_plus_not_exist_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_not_exist_plus_deal))
////        text_main_goods_plus_around.setOnClickListener {
////            val intent = Intent(activity, AroundPageActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            activity!!.startActivityForResult(intent, Const.REQ_PAGE)
////        }
//
////        text_main_goods_plus_category.setOnClickListener {
////            val intent = Intent(activity, SearchActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            activity!!.startActivityForResult(intent, Const.REQ_PAGE)
////        }
//
////        image_main_goods_plus_view_plus.setOnClickListener {
////            val intent = Intent(activity, MyPlusActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            activity!!.startActivityForResult(intent, Const.REQ_PLUS)
////        }
//
//        text_main_goods_plus_around_page.setOnClickListener {
////            val intent = Intent(activity, MainActivity::class.java)
//            val intent = Intent(activity, AppMainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
////        image_main_goods_plus_my.setOnClickListener {
////            val intent = Intent(activity, MyInfoActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            activity!!.startActivityForResult(intent, Const.REQ_PLUS)
////        }
//
////        recycler_main_goods_plus_plus.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
////        mPlusAdapter = MainPlusAdapter(activity!!)
////        recycler_main_goods_plus_plus.adapter = mPlusAdapter
////
////        GravitySnapHelper(Gravity.START, false, object : GravitySnapHelper.SnapListener {
////            override fun onSnap(position: Int) {
////
////            }
////        }).attachToRecyclerView(recycler_main_goods_plus_plus)
////        mPlusAdapter!!.setOnItemClickListener(object : MainPlusAdapter.OnItemClickListener {
////            override fun onItemClick(position: Int, view: View) {
////                val location = IntArray(2)
////                view.getLocationOnScreen(location)
////                val x = location[0] + view.width / 2
////                val y = location[1] + view.height / 2
////
////                val params = HashMap<String, String>()
////                params["no"] = mPlusAdapter!!.getItem(position).no!!.toString()
////                showProgress("")
////                ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
////                    override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
////                        hideProgress()
////                        PplusCommonUtil.goPage(activity!!, response.data, x, y)
////                    }
////
////                    override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
////                        hideProgress()
////                    }
////                }).build().call()
////            }
////        })
////        plusListCall()
//        mPaging = 0
//        listCall(mPaging)
//    }
//
////    private var mPlusAdapter: MainPlusAdapter? = null
////    private fun plusListCall() {
////
////        val params = HashMap<String, String>()
////        params["pg"] = "1"
////
////        ApiBuilder.create().getPlusList(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
////
////            override fun onResponse(call: Call<NewResultResponse<Plus>>?, response: NewResultResponse<Plus>?) {
////                if (!isAdded) {
////                    return
////                }
////                mPlusAdapter!!.clear()
////                if (response != null && response.datas != null && response.datas.size > 0) {
////                    layout_main_goods_plus_plus.visibility = View.VISIBLE
////                    LogUtil.e(LOG_TAG, "plus count : {}", response.datas.size)
////                    mPlusAdapter!!.addAll(response.datas)
////                } else {
////                    layout_main_goods_plus_plus.visibility = View.GONE
////                }
////            }
////
////            override fun onFailure(call: Call<NewResultResponse<Plus>>?, t: Throwable?, response: NewResultResponse<Plus>?) {
////            }
////        }).build().call()
////
////    }
//
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["isPlus"] = "true"
//        params["sort"] = "${EnumData.GoodsSort.news_datetime.name},${EnumData.BuyGoodsSort.desc.name}"
//        params["page"] = page.toString()
////        showProgress("")
//        ApiBuilder.create().getGoodsPlus(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
//                if (!isAdded) {
//                    return
//                }
//
////                hideProgress()
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                        if (mTotalCount == 0) {
//                            layout_main_goods_plus_not_exist?.visibility = View.VISIBLE
//                        } else {
//                            layout_main_goods_plus_not_exist?.visibility = View.GONE
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
////                hideProgress()
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
//            Const.REQ_GOODS_DETAIL -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }
//            Const.REQ_PAGE, Const.REQ_PLUS -> {
//                mPaging = 0
//                listCall(mPaging)
////                plusListCall()
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_plus product"
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
//                MainGoodsPlusFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
