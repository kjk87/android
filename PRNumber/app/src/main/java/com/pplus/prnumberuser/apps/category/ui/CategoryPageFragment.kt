package com.pplus.prnumberuser.apps.category.ui

import android.content.Context
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
import com.pplus.prnumberuser.apps.page.data.MainPageAdapter
import com.pplus.prnumberuser.core.code.common.SortCode
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentCategoryPageBinding
import com.pplus.utils.BusProvider
import com.squareup.otto.Subscribe
import retrofit2.Call
import java.util.*

class CategoryPageFragment : BaseFragment<BaseActivity>() {

    private var mTotalCount = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mSortCode: SortCode = SortCode.distance
    private var mAdapter: MainPageAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLocationData: LocationData? = null
    private var category: CategoryMajor? = null

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
        mAdapter = MainPageAdapter()
        binding.recyclerCategoryPage.adapter = mAdapter
//        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))

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
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : MainPageAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                val x = location[0] + view.width / 2
                val y = location[1] + view.height / 2

                PplusCommonUtil.goPage(activity!!, mAdapter!!.getItem(position), x, y)
            }
        })

//        text_category_page_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_plus), getString(R.string.word_sort_distance))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            text_category_page_sort.setText(R.string.word_sort_plus)
//                            mSortCode = SortCode.plusCount
//                        }
//
//                        2 -> {
//                            text_category_page_sort.setText(R.string.word_sort_distance)
//                            mSortCode = SortCode.distance
//                        }
//                    }
//                    getCount()
//                }
//            }).builder().show(activity)
//        }

//        if (category!!.type == PageTypeCode.store.name) {
//            text_category_page_sort.setText(R.string.word_sort_distance)
//            mSortCode = SortCode.distance
//        } else if (category!!.type == PageTypeCode.person.name) {
//            text_category_page_sort.setText(R.string.word_sort_plus)
//            mSortCode = SortCode.plusCount
//        }

        binding.layoutCategoryPageLoading.visibility = View.VISIBLE
        mSortCode = SortCode.distance
        setData()
    }

    private fun setData() {
        mLocationData = LocationUtil.specifyLocationData
        if (mLocationData != null) {

            getCount()
        }
    }

    private fun getCount() {

        val params = HashMap<String, String>()
        if(category!!.seqNo != -1L){
            params["categoryMajorSeqNo"] = category!!.seqNo.toString()
        }
//        params["onlyPoint"] = "true"
        ApiBuilder.create().getPageCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {

                if (!isAdded) {
                    return
                }

                mTotalCount = response.data

                if (mTotalCount == 0) {
                    binding.layoutCategoryPageLoading.visibility = View.GONE
                    binding.layoutCategoryPageNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutCategoryPageNotExist.visibility = View.GONE
                }

                mPaging = 1
                mAdapter!!.clear()
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
//        params["search"] = mSearchWord
        params["align"] = mSortCode.name
        params["latitude"] = mLocationData!!.latitude.toString()
        params["longitude"] = mLocationData!!.longitude.toString()
        if(category!!.seqNo != -1L){
            params["categoryMajorSeqNo"] = category!!.seqNo.toString()
        }
//        params["onlyPoint"] = "true"
        params["pg"] = page.toString()
//        showProgress("")
        mLockListView = true
        ApiBuilder.create().getPageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
                if (!isAdded) {
                    return
                }
                binding.layoutCategoryPageLoading.visibility = View.GONE
                mLockListView = false
//                hideProgress()
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                mLockListView = false
                hideProgress()
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
            category = it.getParcelable(Const.DATA)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(category: CategoryMajor) =
                CategoryPageFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.DATA, category)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
