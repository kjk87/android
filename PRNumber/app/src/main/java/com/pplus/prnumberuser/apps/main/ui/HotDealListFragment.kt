//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity2
//import com.pplus.prnumberuser.apps.main.data.MainHotDealAdapter
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import kotlinx.android.synthetic.main.fragment_hot_deal_list.*
//import kotlinx.android.synthetic.main.fragment_main_hot_deal.*
//import retrofit2.Call
//import java.util.*
//
//class HotDealListFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_hot_deal_list
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: MainHotDealAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var category: CategoryMajor? = null
//    private var mIsLast = false
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_hot_deal_list.layoutManager = mLayoutManager!!
//        mAdapter = MainHotDealAdapter()
//        recycler_hot_deal_list.adapter = mAdapter
//        recycler_hot_deal_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MainHotDealAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//
////                if (!PplusCommonUtil.loginCheck(activity!!)) {
////                    return
////                }
//
//                val intent = Intent(activity, GoodsDetailActivity2::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//        })
//        layout_hot_deal_loading?.visibility = View.VISIBLE
//        setData()
//    }
//
//    fun setData() {
//        mLocationData = LocationUtil.specifyLocationData
//        if (mLocationData != null) {
//            mPaging = 0
//            listCall(mPaging)
//        }
//    }
//
//    private fun listCall(page: Int) {
//
////        mAdapter!!.mIsRealTime = (parentFragment as MainHotDealFragment).check_main_coupon_sort.isChecked
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        if((parentFragment as MainHotDealFragment).mSort != "distance"){
//            params["sort"] = (parentFragment as MainHotDealFragment).mSort
//        }
////        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
////        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["latitude"] = mLocationData!!.latitude.toString()
//        params["longitude"] = mLocationData!!.longitude.toString()
//        params["isRealTime"] = (parentFragment as MainHotDealFragment).check_main_hot_deal_sort.isChecked.toString()
//        if(category!!.seqNo != -1L){
//            params["categoryMajorSeqNo"] = category!!.seqNo.toString()
//            params["isHotdeal"] = "false"
//            params["isPlus"] = "true"
//        }else{
//            params["isHotdeal"] = "true"
//            params["isPlus"] = "false"
//        }
//
//        params["page"] = page.toString()
//
////        showProgress("")
//        ApiBuilder.create().getGoodsLocation(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
////                hideProgress()
//
//                if (!isAdded) {
//                    return
//                }
//
//                layout_hot_deal_loading?.visibility = View.GONE
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                        if(mTotalCount == 0){
//                            layout_hot_deal_list_not_exist?.visibility = View.VISIBLE
//                        }else{
//                            layout_hot_deal_list_not_exist?.visibility = View.GONE
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
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                layout_hot_deal_loading?.visibility = View.GONE
//            }
//        }).build().call()
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
//        fun newInstance(category: CategoryMajor) =
//                HotDealListFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.DATA, category)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
