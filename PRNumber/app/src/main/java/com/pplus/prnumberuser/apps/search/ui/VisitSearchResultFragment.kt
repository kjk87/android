package com.pplus.prnumberuser.apps.search.ui

import android.app.Activity
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
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
import com.pplus.prnumberuser.apps.page.data.VisitPageAdapter
import com.pplus.prnumberuser.apps.page.ui.PageVisitMenuActivity
import com.pplus.prnumberuser.core.code.common.SortCode
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.OnAddressCallListener
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.callAddress
import com.pplus.prnumberuser.databinding.FragmentSearchResultBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class VisitSearchResultFragment : BaseFragment<BaseActivity>() {
    override fun getPID(): String {
        return ""
    }

    private var mSearchWord: String? = null
    private var mTotalCount = 0
    private var mLocationData: LocationData? = null
    private var mAdapter: VisitPageAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = false
    private var mPaging = 1
    private var mSortCode: SortCode? = null
    private var mIsLast = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mSearchWord = requireArguments().getString(WORD)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.textSearchResultNotExist.text = getString(R.string.msg_not_exist_searchResult, mSearchWord)

        binding.textSearchResultSortDistance.setOnClickListener{
            if (mSortCode != SortCode.distance) {
                mSortCode = SortCode.distance
                setOrder()
            }
        }

        binding.textSearchResultSortPlus.setOnClickListener{
            if (mSortCode != SortCode.plusCount) {
                mSortCode = SortCode.plusCount
                setOrder()
            }
        }

        mSortCode = SortCode.distance
        binding.textSearchResultSortDistance.isSelected = true
        binding.textSearchResultSortPlus.isSelected = false

        binding.layoutAddress.textAddress.setOnClickListener{
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
        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerSearchResult.layoutManager = mLayoutManager
        mAdapter = VisitPageAdapter()
        binding.recyclerSearchResult.adapter = mAdapter
        //        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_60, R.dimen.height_60));
        binding.recyclerSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                val intent = Intent(activity, PageVisitMenuActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        })
        mLocationData = LocationUtil.specifyLocationData
        setData()
    }

    private fun setData() {
        if (mLocationData != null) {
            if (StringUtils.isEmpty(mLocationData!!.address)) {
                callAddress(mLocationData, object : OnAddressCallListener {
                    override fun onResult(address: String) {
                        if (!isAdded) {
                            return
                        }

                        mLocationData = LocationUtil.specifyLocationData
                        binding.layoutAddress.textAddress.text = address
                    }
                })
            } else {
                binding.layoutAddress.textAddress.text = mLocationData!!.address
            }

        }
        mPaging = 0
        listCall(mPaging)
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect[0, mTopOffset, 0] = mItemOffset
            } else {
                outRect[0, 0, 0] = mItemOffset
            }
        }

    }

    private fun setOrder() {
        if (mSortCode == SortCode.distance) {
            binding.textSearchResultSortDistance.isSelected = true
            binding.textSearchResultSortPlus.isSelected = false
        } else {
            binding.textSearchResultSortDistance.isSelected = false
            binding.textSearchResultSortPlus.isSelected = true
        }
        mAdapter!!.clear()
        mPaging = 0
        listCall(mPaging)
    }

    fun reSearch(word: String?) {
        mSearchWord = word
        binding.textSearchResultNotExist.text = getString(R.string.msg_not_exist_searchResult, mSearchWord)
        mAdapter!!.clear()
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

        params["keyword"] = mSearchWord!!
        params["categoryMajorSeqNo"] = "8"
        params["page"] = page.toString()
        ApiBuilder.create().getVisitPageListByKeyword(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Page2>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                if (!isAdded) {
                    return
                }
                if (response?.data != null) {

                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        binding.textSearchResultCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.textSearchResultNotExist.visibility = View.VISIBLE
                        } else {
                            binding.textSearchResultNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    mAdapter!!.addAll(response.data!!.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Page2>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Page2>>?) {
                mLockListView = false
            }
        }).build().call()
    }

    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mLocationData = LocationUtil.specifyLocationData
            setData()
        }
    }

    companion object {
        const val WORD = "word"
        fun newInstance(word: String?): VisitSearchResultFragment {
            val fragment = VisitSearchResultFragment()
            val args = Bundle()
            args.putString(WORD, word)
            fragment.arguments = args
            return fragment
        }
    }
}