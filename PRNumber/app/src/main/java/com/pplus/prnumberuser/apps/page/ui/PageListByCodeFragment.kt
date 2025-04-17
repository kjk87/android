//package com.pplus.prnumberuser.apps.page.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.BusProvider
//import com.pplus.prnumberuser.BusProviderData
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.page.data.PageAdapter
//import com.pplus.prnumberuser.core.code.common.SortCode
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.squareup.otto.Subscribe
//import kotlinx.android.synthetic.main.fragment_page_list.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PageListByCodeFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_page_list
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mSortCode = SortCode.distance
//    private var mAdapter: PageAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_page_list.layoutManager = mLayoutManager
//        mAdapter = PageAdapter(activity!!)
//        recycler_page_list.adapter = mAdapter
//
//        recycler_page_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : PageAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//                val location = IntArray(2)
//                view.getLocationOnScreen(location)
//                val x = location[0] + view.width / 2
//                val y = location[1] + view.height / 2
//                PplusCommonUtil.goPage(activity!!, mAdapter!!.getItem(position), x, y)
//            }
//        })
//
//        text_page_list_sort_distance.setOnClickListener {
//            if(mSortCode != SortCode.distance){
//                mSortCode = SortCode.distance
//                text_page_list_sort_distance.isSelected = true
//                text_page_list_sort_plus.isSelected = false
//                getCount()
//            }
//
//        }
//
//        text_page_list_sort_plus.setOnClickListener {
//            if(mSortCode != SortCode.plusCount){
//                mSortCode = SortCode.plusCount
//                text_page_list_sort_distance.isSelected = false
//                text_page_list_sort_plus.isSelected = true
//                getCount()
//            }
//
//        }
//
////        layout_page_list_address.visibility = View.GONE
////
////        text_store_page_address.setOnClickListener {
////            val intent = Intent(activity, LocationSelectActivity::class.java)
////            val location = IntArray(2)
////            it.getLocationOnScreen(location)
////            val x = location[0] + it.width / 2
////            val y = location[1] + it.height / 2
////            intent.putExtra(Const.X, x)
////            intent.putExtra(Const.Y, y)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity!!.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
////        }
//
//        mSortCode = SortCode.distance
//        text_page_list_sort_distance.isSelected = true
//        text_page_list_sort_plus.isSelected = false
//
//        setData()
//    }
//
//    private fun setData() {
//        mLocationData = LocationUtil.getSpecifyLocationData()
//        getCount()
////        if (mLocationData != null) {
////
//////            PplusCommonUtil.callAddress(LocationUtil.getSpecifyLocationData(), object : PplusCommonUtil.Companion.OnAddressCallListener {
//////
//////                override fun onResult(address: String) {
//////
//////                    if (!isAdded) {
//////                        return
//////                    }
//////
//////                    text_store_page_address?.text = address
//////                }
//////            })
////
////
////        }
//
//    }
//
//    private fun getCount() {
//
//        val params = HashMap<String, String>()
////        if(mIsOnly){
////            params["only"] = Const.GAL_COM_CODE
////        }else{
////            params["exclude"] = Const.GAL_COM_CODE
////        }
//
//        ApiBuilder.create().getPageCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                mTotalCount = response.data
//
////                if (mTotalCount == 0) {
////                    layout_category_page_not_exist.visibility = View.VISIBLE
////                } else {
////                    layout_category_page_not_exist.visibility = View.GONE
////                }
//
//                mPaging = 1
//                mAdapter!!.clear()
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        val params = HashMap<String, String>()
//        params["align"] = mSortCode.name
//        if (mLocationData != null) {
//            params["latitude"] = mLocationData!!.latitude.toString()
//            params["longitude"] = mLocationData!!.longitude.toString()
//        }
//        params["pg"] = page.toString()
////        if(mIsOnly){
////            params["only"] = Const.GAL_COM_CODE
////        }else{
////            params["exclude"] = Const.GAL_COM_CODE
////        }
//
////        if(!StringUtils.isNotEmpty(mType) && page == 1){
////            showProgress("")
////        }
//        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getPageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                hideProgress()
////                if(!StringUtils.isNotEmpty(mType)){
////                    hideProgress()
////                }
//
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                mLockListView = false
//                hideProgress()
////                if(!StringUtils.isNotEmpty(mType)){
////                    hideProgress()
////                }
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
//    @Subscribe
//    fun setPlus(data: BusProviderData) {
//        if (data.type == BusProviderData.BUS_MAIN && data.subData is Page) {
//            mAdapter!!.setBusPlus(data)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        BusProvider.getInstance().unregister(this)
//    }
//
//    private var mIsOnly = true
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        BusProvider.getInstance().register(this)
//        arguments?.let {
//            mIsOnly = it.getBoolean(Const.ONLY)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(isOnly: Boolean) =
//                PageListByCodeFragment().apply {
//                    arguments = Bundle().apply {
//                        putBoolean(Const.ONLY, isOnly)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
