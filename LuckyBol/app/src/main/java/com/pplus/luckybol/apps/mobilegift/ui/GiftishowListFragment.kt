package com.pplus.luckybol.apps.mobilegift.ui

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
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.mobilegift.data.GiftishowAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Giftishow
import com.pplus.luckybol.core.network.model.dto.GiftishowCategory
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentMobileGiftListBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class GiftishowListFragment : BaseFragment<BaseActivity?>() {
    override fun getPID(): String {
        return ""
    }

    private var _binding: FragmentMobileGiftListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMobileGiftListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var mAdapter: GiftishowAdapter? = null
    var mLayoutManager: LinearLayoutManager? = null
    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mCategory: GiftishowCategory? = null
    private var mSort = "seqNo,desc"
    private var mPos = 0
    private var mIsLast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mCategory = requireArguments().getParcelable(Const.CATEGORY)
            mPos = requireArguments().getInt(Const.POSITION)
        }
    }

    override fun init() {
        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerMobileGiftList.layoutManager = mLayoutManager
//        recycler_mobile_gift_list.addItemDecoration(ItemOffsetDecoration(activity!!, R.dimen.height_16))
        mAdapter = GiftishowAdapter()
        binding.recyclerMobileGiftList.adapter = mAdapter
        binding.textMobileGiftTotalCount.text = getString(R.string.format_count_unit, "0")
//        text_mobile_gift_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_high_price), getString(R.string.word_sort_low_price))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//                override fun onCancel() {}
//                override fun onResult(event_alert: EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            text_mobile_gift_sort.text = getString(R.string.word_sort_recent)
//                            mSort = MobileGiftSortType.recent
//                        }
//                        2 -> {
//                            text_mobile_gift_sort.text = getString(R.string.word_sort_high_price)
//                            mSort = MobileGiftSortType.highPrice
//                        }
//                        3 -> {
//                            text_mobile_gift_sort.text = getString(R.string.word_sort_low_price)
//                            mSort = MobileGiftSortType.lowPrice
//                        }
//                    }
//                    getCount()
//                }
//            }).builder().show(activity)
//        }
        binding.recyclerMobileGiftList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var pastVisibleItems = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })
        mAdapter!!.setOnItemClickListener(object : GiftishowAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, GiftishowDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })
        mSort = "seqNo,desc"
//        text_mobile_gift_sort.setText(R.string.word_sort_low_price)

        binding.layoutMobileGiftLoading.visibility = View.VISIBLE
        mPage = 0
        listCall(mPage)
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        if (mCategory!!.seqNo != null) {
            params["categorySeqNo"] = "" + mCategory!!.seqNo
        }
        params["page"] = page.toString()
        params["sort"] = mSort
        mLockListView = true
//        if (page != 1 || mPos == 0) {
//            showProgress("")
//        }
        ApiBuilder.create().getGiftishowList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Giftishow>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Giftishow>>>?,
                                    response: NewResultResponse<SubResultResponse<Giftishow>>?) {
                if (!isAdded) {
                    return
                }
                binding.layoutMobileGiftLoading.visibility = View.GONE
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        binding.textMobileGiftTotalCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Giftishow>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<Giftishow>>?) {
                mLockListView = false
                binding.layoutMobileGiftLoading.visibility = View.GONE
            }

        }).build().call()
    }

    private inner class ItemOffsetDecoration(private val mItemOffset: Int, private val mRightOffset: Int) : ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(R.dimen.width_8)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position < 2) {
                outRect[mRightOffset, mItemOffset, mRightOffset] = mRightOffset
            } else {
                outRect[mRightOffset, mRightOffset, mRightOffset] = mRightOffset
            }
        }

    }

    companion object {
        fun newInstance(category: GiftishowCategory?, position: Int): GiftishowListFragment {
            val fragment = GiftishowListFragment()
            val args = Bundle()
            args.putParcelable(Const.CATEGORY, category)
            args.putInt(Const.POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}