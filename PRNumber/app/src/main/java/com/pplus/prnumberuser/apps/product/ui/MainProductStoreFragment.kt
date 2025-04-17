package com.pplus.prnumberuser.apps.product.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
import com.pplus.prnumberuser.apps.product.data.ProductTicketAdapter
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainProductStoreBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class MainProductStoreFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentMainProductStoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainProductStoreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: ProductTicketAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var mIsLast = false
    private var mSort = "location"

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMainProductStore.layoutManager = mLayoutManager!!
        mAdapter = ProductTicketAdapter()
        binding.recyclerMainProductStore.adapter = mAdapter
//        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))

//        recycler_main_goods.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclerMainProductStore.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : ProductTicketAdapter.OnItemClickListener {
            override fun changeLike() {

            }

            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                activity?.startActivityForResult(intent, Const.REQ_DETAIL)
            }
        })

//        image_hot_deal_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        image_main_hotdeal_around.setOnClickListener {
//            val intent = Intent(activity, LocationAroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        binding.textMainProductStoreAddress.setSingleLine()
        binding.textMainProductStoreAddress.setOnClickListener {
            val intent = Intent(activity, LocationSelectActivity::class.java)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            intent.putExtra(Const.X, x)
            intent.putExtra(Const.Y, y)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            locationLauncher.launch(intent)
        }

//        image_main_product_store_filter.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_distance), getString(R.string.word_sort_reward), getString(R.string.word_sort_review))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            mSort = "location"
//                        }
//                        2 -> {
//                            mSort = "reward"
//                        }
//                        3 -> {
//                            mSort = "review"
//                        }
//                    }
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//            }).builder().show(activity)
//        }

        setData()
    }

    private fun setData() {
        mLocationData = LocationUtil.specifyLocationData
        if (mLocationData != null) {
            if (StringUtils.isEmpty(mLocationData!!.address)) {
                LogUtil.e(LOG_TAG, "callAddress")
                PplusCommonUtil.callAddress(mLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                    override fun onResult(address: String) {

                        mLocationData = LocationUtil.specifyLocationData
                        binding.textMainProductStoreAddress.text = mLocationData!!.address

                    }
                })
            } else {
                LogUtil.e(LOG_TAG, "address : {}", mLocationData!!.address)
                binding.textMainProductStoreAddress.text = mLocationData!!.address
            }

            mSort = "location"
            mPaging = 0
            listCall(mPaging)
        }
    }


    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["latitude"] = mLocationData!!.latitude.toString()
        params["longitude"] = mLocationData!!.longitude.toString()

        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getProductPriceListStoreTypeByPageAndDiscountDistanceDesc(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                    response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
//                        text_store_product_count?.text = getString(R.string.format_goods, FormatUtil.getMoneyType(mTotalCount.toString()))
//                        if(mTotalCount > 0){
//                            layout_store_product_not_exist?.visibility = View.GONE
//                        }else{
//                            layout_store_product_not_exist?.visibility = View.VISIBLE
//                        }

                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                mLockListView = false
            }

        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            setData()
        }
    }

    override fun getPID(): String {
        return "Main_surrounding sale"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                MainProductStoreFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
