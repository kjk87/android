//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.coupon.ui.AlertCouponDownloadCompleteActivity
//import com.pplus.prnumberuser.apps.coupon.ui.CouponDetailActivity
//import com.pplus.prnumberuser.apps.main.data.CouponAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.fragment_coupon.*
//import kotlinx.android.synthetic.main.fragment_main_hot_deal.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class CouponFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_coupon
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: CouponAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var category: Category? = null
//    private var mIsLast = false
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_coupon.layoutManager = mLayoutManager!!
//        mAdapter = CouponAdapter()
//        recycler_coupon.adapter = mAdapter
//        recycler_coupon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : CouponAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//
//                if (!PplusCommonUtil.loginCheck(activity!!)) {
//                    return
//                }
//
//                val intent = Intent(activity, CouponDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//            }
//
//            override fun onCheckDownload(position: Int) {
//                if (!PplusCommonUtil.loginCheck(activity!!)) {
//                    return
//                }
//
//                checkLike(mAdapter!!.getItem(position))
//            }
//        })
//
//        setData()
//    }
//
//    private fun checkLike(goods: Goods) {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        params["goodsSeqNo"] = goods.seqNo.toString()
//        params["pageSeqNo"] = goods.page!!.seqNo.toString()
//
////        showProgress("")
//        ApiBuilder.create().getGoodsLikeOne(params).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
////                hideProgress()
//
//                if (response != null) {
//                    if (response.data != null && response.data.status == 1) {
//                        ToastUtil.show(activity, R.string.msg_already_download_coupon)
//                    }else{
//                        postLike(goods)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun postLike(goods: Goods) {
//
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = goods.seqNo
//        goodsLike.pageSeqNo = goods.page!!.seqNo
////        showProgress("")
//        ApiBuilder.create().postGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<GoodsLike>> {
//            override fun onResponse(call: Call<NewResultResponse<GoodsLike>>?, response: NewResultResponse<GoodsLike>?) {
////                hideProgress()
//                val intent = Intent(activity, AlertCouponDownloadCompleteActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//                (parentFragment as MainCouponFragment).getGoodsLikeCount()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<GoodsLike>>?, t: Throwable?, response: NewResultResponse<GoodsLike>?) {
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    fun setData() {
//        mLocationData = LocationUtil.getSpecifyLocationData()
//        if (mLocationData != null) {
//            mPaging = 0
//            listCall(mPaging)
//        }
//    }
//
//    private fun listCall(page: Int) {
//
//        mAdapter!!.mIsRealTime = (parentFragment as MainCouponFragment).check_main_coupon_sort.isChecked
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        if((parentFragment as MainCouponFragment).mSort != "distance"){
//            params["sort"] = (parentFragment as MainCouponFragment).mSort
//        }
//        params["expired"] = "false" //null-All, true-유효기간지난것, false-유효기간남은것
//        params["status"] = EnumData.GoodsStatus.ing.status.toString()
//        params["latitude"] = mLocationData!!.latitude.toString()
//        params["longitude"] = mLocationData!!.longitude.toString()
//        params["isHotdeal"] = "false"
//        params["isPlus"] = "false"
//        params["isCoupon"] = "true"
//        params["represent"] = "true"
//        params["isRealTime"] = (parentFragment as MainCouponFragment).check_main_coupon_sort.isChecked.toString()
//        if(category!!.no != -1L){
//            params["pageParentCategorySeqNo"] = category!!.no.toString()
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
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
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
//        fun newInstance(category: Category) =
//                CouponFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.DATA, category)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
