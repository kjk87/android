package com.pplus.prnumberuser.apps.page.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.page.data.VisitPageAdapter
import com.pplus.prnumberuser.core.code.common.SortCode
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.FragmentCategoryPageBinding
import com.pplus.utils.BusProvider
import com.squareup.otto.Subscribe
import retrofit2.Call
import java.util.*

class CategoryMinorVisitPageFragment : BaseFragment<BaseActivity>() {

    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mIsLast = false
    private var mSortCode: SortCode = SortCode.distance
    private var mAdapter: VisitPageAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var category: CategoryMinor? = null

    private var _binding: FragmentCategoryPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCategoryPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerCategoryPage.layoutManager = mLayoutManager
        mAdapter = VisitPageAdapter()
        binding.recyclerCategoryPage.adapter = mAdapter

        binding.recyclerCategoryPage.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : VisitPageAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {
//                val location = IntArray(2)
//                view.getLocationOnScreen(location)
//                val x = location[0] + view.width / 2
//                val y = location[1] + view.height / 2
//
//                PplusCommonUtil.goPage(activity!!, mAdapter!!.getItem(position), x, y)

                val intent = Intent(activity, PageVisitMenuActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })

        binding.layoutCategoryPageLoading.visibility = View.VISIBLE

        mSortCode = SortCode.distance
        mLocationData = LocationUtil.specifyLocationData
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {
        mLockListView = true
        val params = HashMap<String, String>()
        //        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        if(mLocationData != null){
            params["latitude"] = mLocationData!!.latitude.toString()
            params["longitude"] = mLocationData!!.longitude.toString()
        }

        if (category!!.seqNo != -1L) {
            params["categoryMinorSeqNo"] = category!!.seqNo.toString()
        } else {
            params["categoryMajorSeqNo"] = category!!.major.toString()
        }
        params["page"] = page.toString()
        ApiBuilder.create().getVisitPageList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutCategoryPageLoading.visibility  =View.GONE
                if (response?.data != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutCategoryPageNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutCategoryPageNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    binding.layoutCategoryPageLoading.visibility = View.GONE
                    mAdapter!!.addAll(response.data!!.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                binding.layoutCategoryPageLoading.visibility  =View.GONE
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

    @Subscribe
    fun setPlus(data: BusProviderData) {
        if (data.type == BusProviderData.BUS_MAIN && data.subData is Page) {
            mAdapter!!.setBusPlus(data)
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onDestroy() {
        super.onDestroy()
        BusProvider.getInstance().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.getInstance().register(this)
        arguments?.let {
            category = it.getParcelable(Const.DATA) //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(category: CategoryMinor) = CategoryMinorVisitPageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.DATA, category) //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
