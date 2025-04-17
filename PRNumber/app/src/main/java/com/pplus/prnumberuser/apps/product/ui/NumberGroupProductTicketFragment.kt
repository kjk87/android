package com.pplus.prnumberuser.apps.product.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
import com.pplus.prnumberuser.apps.product.data.ProductTicketAdapter
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentNumberGroupProductTicketBinding
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*

class NumberGroupProductTicketFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mVirtualNumberManage = it.getParcelable(Const.DATA)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentNumberGroupProductTicketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentNumberGroupProductTicketBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var mVirtualNumberManage: VirtualNumberManage

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mAdapter: ProductTicketAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false
    private var mLocationData: LocationData? = null

    override fun init() {
        LogUtil.e(LOG_TAG, "init")


//        image_store_back.setOnClickListener {
//            activity?.finish()
//        }

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerNumberGroupProductTicket.layoutManager = mLayoutManager!!
        mAdapter = ProductTicketAdapter()
        binding.recyclerNumberGroupProductTicket.adapter = mAdapter
        binding.recyclerNumberGroupProductTicket.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        binding.recyclerNumberGroupProductTicket.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

//                if (!PplusCommonUtil.loginCheck(activity!!)) {
//                    return
//                }

                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivity(intent)
            }
        })

        getParentActivity().mLocationListener = object : BaseActivity.LocationListener{
            override fun onLocation(result: ActivityResult) {
                if (result.resultCode == Activity.RESULT_OK) {
                    setData()
                }
            }
        }

        binding.textNumberGroupProductTicketAddress.setSingleLine()
        binding.textNumberGroupProductTicketAddress.setOnClickListener {
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

        setData()
    }

    private fun setData() {

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

                        binding.textNumberGroupProductTicketAddress.text = address
                    }
                })
                mLocationData = LocationUtil.specifyLocationData
                mPaging = 0
                listCall(mPaging)
            }
        })
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["manageSeqNo"] = mVirtualNumberManage.seqNo.toString()
        params["page"] = page.toString()
        params["latitude"] = mLocationData!!.latitude.toString()
        params["longitude"] = mLocationData!!.longitude.toString()
        showProgress("")
        ApiBuilder.create().getProductPriceListTicketTypeByManageSeqNoOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
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
                        if(mTotalCount > 0){
                            binding.layoutNumberGroupProductTicketNotExist.visibility = View.GONE
                        }else{
                            binding.layoutNumberGroupProductTicketNotExist.visibility = View.VISIBLE
                        }

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

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    companion object {

        @JvmStatic
        fun newInstance(virtualNumberManage: VirtualNumberManage) =
                NumberGroupProductTicketFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.DATA, virtualNumberManage)
                    }
                }
    }
}
