//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.main.data.MainSubscriptionAdapter
//import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.apps.subscription.ui.MySubscriptionActivity
//import com.pplus.prnumberuser.apps.subscription.ui.SubscriptionDetailActivity
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import kotlinx.android.synthetic.main.fragment_main_subscription_product.*
//import retrofit2.Call
//import java.util.*
//
//class MainSubscriptionProductFragment : BaseFragment<AppMainActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_subscription_product
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: MainSubscriptionAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var mIsLast = false
//
//    val mSignInReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            checkSignIn()
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(activity?.packageName + ".sigIn")
//        requireActivity().registerReceiver(mSignInReceiver, intentFilter)
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        requireActivity().unregisterReceiver(mSignInReceiver)
//    }
//
//    override fun init() {
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_main_subscription_product.layoutManager = mLayoutManager!!
//        mAdapter = MainSubscriptionAdapter()
//        recycler_main_subscription_product.adapter = mAdapter
//        recycler_main_subscription_product.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_48))
//
////        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
//        recycler_main_subscription_product.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        mAdapter!!.setOnItemClickListener(object : MainSubscriptionAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                val intent = Intent(activity, SubscriptionDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                signInLauncher.launch(intent)
//            }
//
//        })
//        layout_main_subscription_product_loading.visibility  = View.VISIBLE
//
//        text_main_subscription_product_retention.setOnClickListener {
//            val intent = Intent(activity, MySubscriptionActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        layout_main_subscription_find_store.setOnClickListener {
//            getParentActivity().setMainPageFragment()
//        }
//
//        text_subscription_product_search.setOnClickListener {
//            PplusCommonUtil.alertLocation(getParentActivity(), true, object : PplusCommonUtil.Companion.SuccessLocationListener {
//                override fun onSuccess() {
//                    if (!isAdded) {
//                        return
//                    }
//                    PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                        override fun onResult(address: String) {
//
//                            if (!isAdded) {
//                                return
//                            }
//
//                            text_subscription_product_address?.text = address
//                        }
//                    })
//
//                    mLocationData = LocationUtil.specifyLocationData
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            })
//        }
//
//        getParentActivity().mLocationListener = object : BaseActivity.LocationListener {
//            override fun onLocation(result : ActivityResult) {
//                PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//                    override fun onSuccess() {
//                        if (!isAdded) {
//                            return
//                        }
//                        PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                            override fun onResult(address: String) {
//
//                                if (!isAdded) {
//                                    return
//                                }
//
//                                text_subscription_product_address?.text = address
//                            }
//                        })
//
//                        mLocationData = LocationUtil.specifyLocationData
//                        mPaging = 0
//                        listCall(mPaging)
//                    }
//                })
//            }
//        }
//
//        text_subscription_product_address.setSingleLine()
//        text_subscription_product_address.setOnClickListener {
//            val intent = Intent(activity, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            getParentActivity().locationLauncher.launch(intent)
//        }
//
//        text_main_subscription_product_login.setOnClickListener {
//            val intent = Intent(activity, SnsLoginActivity::class.java)
//            signInLauncher.launch(intent)
//        }
//
//        text_main_subscription_product_point.setOnClickListener {
//            val intent = Intent(activity, BolConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        checkSignIn()
//
//        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        if (!isAdded) {
//                            return
//                        }
//
//                        text_subscription_product_address?.text = address
//                    }
//                })
//                mLocationData = LocationUtil.specifyLocationData
//                mPaging = 0
//                listCall(mPaging)
//            }
//        })
//    }
//
//    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        checkSignIn()
//    }
//
//    private fun checkSignIn(){
//        if (LoginInfoManager.getInstance().isMember) {
//            text_main_subscription_product_point.visibility = View.VISIBLE
//            text_main_subscription_product_login.visibility = View.GONE
//            setRetentionBol()
//        } else {
//            text_main_subscription_product_point.visibility = View.GONE
//            text_main_subscription_product_login.visibility = View.VISIBLE
//        }
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//                if (!isAdded) {
//                    return
//                }
//                text_main_subscription_product_point?.text = FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())
//            }
//        })
//        getMySubscriptionCount()
//    }
//
//    private fun getMySubscriptionCount(){
//        ApiBuilder.create().getSubscriptionDownloadCountByMemberSeqNoAndStatus().setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//                text_main_subscription_product_retention?.text = getString(R.string.format_retention_prepayment_voucher, response?.data.toString())
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
////        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        if (mLocationData != null) {
//            params["latitude"] = mLocationData!!.latitude.toString()
//            params["longitude"] = mLocationData!!.longitude.toString()
//        }
//        params["page"] = page.toString()
////        showProgress("")
//        ApiBuilder.create().getPlusSubscriptionTypeOnlyNormalOrderByDistance(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
//                if (!isAdded) {
//                    return
//                }
//                layout_main_subscription_product_loading?.visibility  =View.GONE
//                if (response != null) {
//
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.clear()
//                        if (mTotalCount == 0) {
//                            layout_main_subscription_product_not_exist?.visibility = View.VISIBLE
//                        } else {
//                            layout_main_subscription_product_not_exist?.visibility = View.GONE
//                        }
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
////                hideProgress()
//                layout_main_subscription_product_loading?.visibility  =View.GONE
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
//    override fun getPID(): String {
//        return "Main_page product"
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
//                MainSubscriptionProductFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
