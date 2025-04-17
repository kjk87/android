package com.pplus.prnumberuser.apps.main.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.apps.main.data.MainPageWithPrepaymentAdapter
import com.pplus.prnumberuser.apps.prepayment.ui.MyPrepaymentPublishActivity
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.network.prnumber.IPRNumberRequest
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMainPageWithPrepaymentBinding
import retrofit2.Call
import java.util.*

class MainPageWithPrepaymentFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentMainPageWithPrepaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainPageWithPrepaymentBinding.inflate(inflater, container, false)
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
    private var mTab = 0
    private var mAdapter: MainPageWithPrepaymentAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var mIsLast = false


    override fun init() {

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclermainPageWithPrepaymentProduct.layoutManager = mLayoutManager!!
        mAdapter = MainPageWithPrepaymentAdapter()
        mAdapter!!.signInLauncher = signInLauncher
        binding.recyclermainPageWithPrepaymentProduct.adapter = mAdapter
        binding.recyclermainPageWithPrepaymentProduct.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_66))

//        recycler_main_goods_plus.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.layout_main_floating))
        binding.recyclermainPageWithPrepaymentProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : MainPageWithPrepaymentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

            }
        })
        binding.layoutMainPageWithPrepaymentLoading.visibility  = View.VISIBLE

        binding.textMainPageWithPrepaymentSearch.setOnClickListener {
            PplusCommonUtil.alertLocation(getParentActivity(), true, object : PplusCommonUtil.Companion.SuccessLocationListener {
                override fun onSuccess() {
                    PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                        override fun onResult(address: String) {

                            if (!isAdded) {
                                return
                            }

                            binding.textMainPageWithPrepaymentAddress.text = address
                        }
                    })

                    mLocationData = LocationUtil.specifyLocationData
                    locationLog()
                    mPaging = 0
                    listCall(mPaging)
                }
            })
        }

        getParentActivity().mLocationListener = object : BaseActivity.LocationListener {
            override fun onLocation(result : ActivityResult) {
                PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
                    override fun onSuccess() {
                        if (!isAdded) {
                            return
                        }
                        PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                            override fun onResult(address: String) {

                                if (!isAdded) {
                                    return
                                }

                                binding.textMainPageWithPrepaymentAddress.text = address
                            }
                        })

                        mLocationData = LocationUtil.specifyLocationData
                        locationLog()
                        mPaging = 0
                        listCall(mPaging)
                    }
                })
            }
        }

        binding.textMainPageWithPrepaymentAddress.setSingleLine()
        binding.textMainPageWithPrepaymentAddress.setOnClickListener {
            val intent = Intent(activity, LocationSelectActivity::class.java)
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            intent.putExtra(Const.X, x)
            intent.putExtra(Const.Y, y)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            getParentActivity().locationLauncher.launch(intent)
        }

        checkSignIn()

        binding.textMainPageWithPrepaymentTabTotal.setOnClickListener {

            binding.textMainPageWithPrepaymentTabTotal.isSelected = true
            binding.textMainPageWithPrepaymentTabVisit.isSelected = false
            mTab = 0
            mPaging = 0
            listCall(mPaging)
        }

        binding.textMainPageWithPrepaymentTabVisit.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }

            binding.textMainPageWithPrepaymentTabTotal.isSelected = false
            binding.textMainPageWithPrepaymentTabVisit.isSelected = true

            mTab = 1
            mPaging = 0
            listCall(mPaging)
        }

        binding.layoutMainPageWithPrepaymentRetentionCount.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(requireActivity(), MyPrepaymentPublishActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            myPrepaymentPublishLauncher.launch(intent)
        }

        binding.textMainPageWithPrepaymentTabTotal.isSelected = true
        binding.textMainPageWithPrepaymentTabVisit.isSelected = false

        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
            override fun onSuccess() {
                if (!isAdded) {
                    return
                }
                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {

                    override fun onResult(address: String) {

                        if (!isAdded) {
                            return
                        }

                        binding.textMainPageWithPrepaymentAddress.text = address
                    }
                })
                mLocationData = LocationUtil.specifyLocationData

                locationLog()

                mTab = 0
                mPaging = 0
                listCall(mPaging)
            }
        })
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkSignIn()
    }

    val myPrepaymentPublishLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getRetentionCount()
    }

    private fun checkSignIn(){
        if (LoginInfoManager.getInstance().isMember) {
            binding.layoutMainPageWithPrepaymentRetentionCount.visibility = View.VISIBLE
            getRetentionCount()
        } else {
            binding.layoutMainPageWithPrepaymentRetentionCount.visibility = View.GONE
        }
    }

    private fun getRetentionCount(){
        ApiBuilder.create().getPrepaymentRetentionCount().setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                binding.textMainPageWithPrepaymentRetentionCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_retention_count, response?.data.toString()))
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    private fun locationLog(){
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        params["platform"] = "aos"
        params["serviceLog"] = "금액권 목록제공을 위한 현재위치조회"
        ApiBuilder.create().locationServiceLogSave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        if (mLocationData != null) {
            params["latitude"] = mLocationData!!.latitude.toString()
            params["longitude"] = mLocationData!!.longitude.toString()
        }
        params["page"] = page.toString()
        var api : IPRNumberRequest<SubResultResponse<Page2>>
        if(mTab == 0){
            api = ApiBuilder.create().getPageListWithPrepayment(params)
        }else{
            api = ApiBuilder.create().getPageListWithPrepaymentExistVisitLog(params)
        }
        api.setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMainPageWithPrepaymentLoading.visibility  =View.GONE
                if (response != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutMainPageWithPrepaymentNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutMainPageWithPrepaymentNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
//                hideProgress()
                binding.layoutMainPageWithPrepaymentLoading.visibility  =View.GONE
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

    override fun getPID(): String {
        return "Main_page product"
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
                MainPageWithPrepaymentFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
