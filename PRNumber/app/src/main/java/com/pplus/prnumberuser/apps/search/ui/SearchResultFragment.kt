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
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
import com.pplus.prnumberuser.apps.page.data.PageAdapter
import com.pplus.prnumberuser.core.code.common.SortCode
import com.pplus.prnumberuser.core.location.LocationUtil
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.OnAddressCallListener
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.callAddress
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.fromHtml
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.goPage
import com.pplus.prnumberuser.databinding.FragmentSearchResultBinding
import com.pplus.utils.BusProvider
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.squareup.otto.Subscribe
import retrofit2.Call
import java.util.*

class SearchResultFragment : BaseFragment<BaseActivity>() {
    override fun getPID(): String {
        return ""
    }

    private var mSearchWord: String? = null
    private var mTotalCount = 0
    private var mSelectPosition = 0
    private var mLocationData: LocationData? = null
    private var mAdapter: PageAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = false
    private var mPaging = 1
    private var mSortCode: SortCode? = null
    private var mPage: Page? = null
    private var mPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BusProvider.getInstance().register(this)

        if (arguments != null) {
            mSearchWord = requireArguments().getString(WORD)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        BusProvider.getInstance().unregister(this)
    }

    @Subscribe
    fun setPlus(data: BusProviderData) {
        if (data.type == BusProviderData.BUS_MAIN && data.subData is Page) {
            mAdapter!!.setBusPlus(data)
        }
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

        mSelectPosition = -1
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
        mAdapter = PageAdapter()
        binding.recyclerSearchResult.adapter = mAdapter
        //        recyclerView.addItemDecoration(new CustomItemOffsetDecoration(getActivity(), R.dimen.height_60, R.dimen.height_60));
        binding.recyclerSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
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
        mAdapter!!.setOnItemClickListener(object : PageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                val x = location[0] + view.width / 2
                val y = location[1] + view.height / 2
                mPage = mAdapter!!.getItem(position)
                mPosition = position
                goPage(activity!!, mAdapter!!.getItem(position), x, y)
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
        getCount()
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
        getCount()
    }

    fun reSearch(word: String?) {
        mSearchWord = word
        binding.textSearchResultNotExist.text = getString(R.string.msg_not_exist_searchResult, mSearchWord)
        mAdapter!!.clear()
        getCount()
    }

        private fun getCount() {
            val params = HashMap<String, String>()
            if(StringUtils.isNotEmpty(mSearchWord)){
                params["search"] = mSearchWord!!
            }
            params["onlyPoint"] = "false"
            ApiBuilder.create().getPageCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {

                    if (!isAdded) {
                        return
                    }

                    mTotalCount = response!!.data
                    binding.textSearchResultCount.text = fromHtml(getString(R.string.html_search_result_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                    if (mTotalCount > 0) {
                        binding.textSearchResultNotExist.visibility = View.GONE
                    } else {
                        binding.textSearchResultNotExist.visibility = View.VISIBLE
                    }
                    mPaging = 1
                    mAdapter!!.clear()
                    listCall(mPaging)
                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                }

            }).build().call()
        }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        if(StringUtils.isNotEmpty(mSearchWord)){
            params["search"] = mSearchWord!!
        }
        if(mLocationData != null){
            params["latitude"] = mLocationData!!.latitude.toString()
            params["longitude"] = mLocationData!!.longitude.toString()
        }

        params["align"] = mSortCode!!.name
        params["pg"] = "" + page
        params["onlyPoint"] = "false"
//        showProgress("")
        mLockListView = true
        ApiBuilder.create().getPageList(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {
                if (!isAdded) {
                    return
                }

                mLockListView = false
//                hideProgress()
                mAdapter!!.addAll(response!!.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                mLockListView = false
//                hideProgress()
            }
        }).build().call()
    }

    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mLocationData = LocationUtil.specifyLocationData
            setData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_CHROME -> if (mPage != null) {
                val no = mPage!!.no.toString()
                val params = HashMap<String, String>()
                params["no"] = no
                showProgress("")
                ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
                    override fun onResponse(call: Call<NewResultResponse<Page>>?, response: NewResultResponse<Page>?) {

                        if (!isAdded) {
                            return
                        }

                        hideProgress()
                        mAdapter!!.replaceData(mPosition, response!!.data!!)
                    }

                    override fun onFailure(call: Call<NewResultResponse<Page>>?, t: Throwable?, response: NewResultResponse<Page>?) {
                        hideProgress()
                    }

                }).build().call()
            }
        }
    }

    companion object {
        const val WORD = "word"
        fun newInstance(word: String?): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle()
            args.putString(WORD, word)
            fragment.arguments = args
            return fragment
        }
    }
}