package com.pplus.luckybol.apps.event.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.EventReviewAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventReview
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.FragmentEventReviewBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class EventReviewFragment : BaseFragment<BaseActivity>() {

    private var _binding: FragmentEventReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEventReviewBinding.inflate(inflater, container, false)
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
    private var mAdapter: EventReviewAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false
    private var mSort = "seqNo,desc"

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerEventReview.layoutManager = mLayoutManager!!
        mAdapter = EventReviewAdapter()
        mAdapter!!.mEventReviewFragment = this
        binding.recyclerEventReview.adapter = mAdapter
        binding.recyclerEventReview.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
        binding.recyclerEventReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : EventReviewAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

            }
        })

        binding.layoutEventReviewLoading.visibility = View.VISIBLE
        mSort = "seqNo,desc"
        setData()
    }

    fun setData() {
        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["sort"] = mSort
        //        showProgress("")
        ApiBuilder.create().getEventReviewList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<EventReview>>>?,
                                    response: NewResultResponse<SubResultResponse<EventReview>>?) {
                if (!isAdded) {
                    return
                }

                binding.layoutEventReviewLoading.visibility = View.GONE
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.layoutEventReviewNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutEventReviewNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<EventReview>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<EventReview>>?) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
                binding.layoutEventReviewLoading.visibility = View.GONE
            }

        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    val eventReplyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.data != null){
            val data = result.data!!
            val eventReview = data.getParcelableExtra<EventReview>(Const.EVENT_REVIEW)
            val position = data.getIntExtra(Const.POSITION, -1)
            if(eventReview != null && position != -1){
                getEventReview(eventReview, position)
            }
        }
    }

    private fun getEventReview(eventReview: EventReview, position:Int){
        val params = HashMap<String, String>()
        params["seqNo"] = eventReview.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getEventReview(params).setCallback(object : PplusCallback<NewResultResponse<EventReview>>{
            override fun onResponse(call: Call<NewResultResponse<EventReview>>?,
                                    response: NewResultResponse<EventReview>?) {
                hideProgress()
                if(response?.data != null){
                    mAdapter!!.mDataList!![position] = response.data!!
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventReview>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventReview>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            category = it.getParcelable(Const.DATA)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EventReviewFragment().apply {
            arguments = Bundle().apply {
//                putParcelable(Const.DATA, category)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
