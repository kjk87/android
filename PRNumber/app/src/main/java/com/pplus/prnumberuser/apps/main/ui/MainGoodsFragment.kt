//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.net.Uri
//import android.os.Bundle
//import android.support.annotation.DimenRes
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.component.RecyclerScaleScrollListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity
//import com.pplus.prnumberuser.apps.main.data.MainGoodsAdapter
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.dto.Page2
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_goods.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainGoodsFragment : BaseFragment<AppMainActivity2>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_goods
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: MainGoodsAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var mIsLast = false
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_main_goods.layoutManager = mLayoutManager!!
//        mAdapter = MainGoodsAdapter(activity!!)
//        recycler_main_goods.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
////        recycler_main_goods.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
//        recycler_main_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MainGoodsAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//
//                val intent = Intent(activity, GoodsDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position).goods)
//                activity!!.startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//            }
//        })
//
//        text_main_goods_address.setSingleLine()
//        text_main_goods_address.setOnClickListener {
//            val intent = Intent(activity, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity!!.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//        }
//
//        image_main_goods_banner.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_seller2_url))
//        }
//
//        layout_main_goods_reg.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW)
//            val bizIntent = activity?.packageManager?.getLaunchIntentForPackage("com.pplus.prnumberbiz")
//            if (bizIntent != null) {
//                try {
//                    intent.data = Uri.parse("prnumberbiz://appLink?moveType1=inner&moveType2=goodsReg")
//                    startActivity(intent)
//                } catch (e: Exception) {
//                    bizIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(bizIntent)
//                }
//
//            } else {
//                // Bring user to the market or let them choose an app?
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                intent.data = Uri.parse("market://details?id=com.pplus.prnumberbiz")
//                startActivity(intent)
//            }
//        }
//
//        if (LoginInfoManager.getInstance().user.page != null) {
//            image_main_goods_banner.visibility = View.GONE
//            layout_main_goods_reg.visibility = View.VISIBLE
//            recycler_main_goods.addOnScrollListener(RecyclerScaleScrollListener(layout_main_goods_reg))
//        } else {
//            image_main_goods_banner.visibility = View.VISIBLE
//            layout_main_goods_reg.visibility = View.GONE
//        }
//
//        setData()
//    }
//
//    private fun setData() {
//        mLocationData = LocationUtil.getSpecifyLocationData()
//        if (mLocationData != null) {
//            if (StringUtils.isEmpty(mLocationData!!.address)) {
//                LogUtil.e(LOG_TAG, "callAddress")
//                PplusCommonUtil.callAddress(mLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        mLocationData = LocationUtil.getSpecifyLocationData()
//                        text_main_goods_address?.text = mLocationData!!.address
//
//                    }
//                })
//            } else {
//                LogUtil.e(LOG_TAG, "address : {}", mLocationData!!.address)
//                text_main_goods_address?.text = mLocationData!!.address
//            }
//
//            mPaging = 0
//            listCall(mPaging)
//        }
//    }
//
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//        params["longitude"] = mLocationData!!.longitude.toString()
//        params["latitude"] = mLocationData!!.latitude.toString()
//        params["page"] = page.toString()
//        ApiBuilder.create().getPageLisWithGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
//                hideProgress()
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
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
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
//        return "Main_surrounding sale"
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
//                MainGoodsFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
