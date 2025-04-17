package com.pplus.prnumberuser.apps.page.ui


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
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.menu.data.PageMenuReviewHeaderAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuReview
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.databinding.FragmentStoreProductBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*

class PageMenuReviewFragment : BaseFragment<BaseActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPage = it.getParcelable(Const.PAGE)!!
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentStoreProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentStoreProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var mPage: Page2

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mAdapter: PageMenuReviewHeaderAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun init() {
        LogUtil.e(LOG_TAG, "init")


//        image_store_back.setOnClickListener {
//            activity?.finish()
//        }

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerStoreProduct.layoutManager = mLayoutManager!!
        mAdapter = PageMenuReviewHeaderAdapter()
        mAdapter!!.mPage = mPage
        binding.recyclerStoreProduct.adapter = mAdapter
        binding.recyclerStoreProduct.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        binding.recyclerStoreProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PageMenuReviewHeaderAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["pageSeqNo"] = mPage.seqNo.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getReviewByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<OrderMenuReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>?,
                                    response: NewResultResponse<SubResultResponse<OrderMenuReview>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        binding.textStoreProductCount.text = getString(R.string.format_review, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<OrderMenuReview>>?) {
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
        fun newInstance(page: Page2) =
                PageMenuReviewFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Const.PAGE, page)
                    }
                }
    }
}
