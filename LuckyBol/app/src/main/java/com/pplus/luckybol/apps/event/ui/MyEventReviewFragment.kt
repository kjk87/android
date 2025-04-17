package com.pplus.luckybol.apps.event.ui

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
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.EventReviewAdapter
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventReview
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.FragmentEventReviewBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class MyEventReviewFragment : BaseFragment<BaseActivity>() {

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
        mAdapter!!.mMyEventReviewFragment = this
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
                val item = mAdapter!!.getItem(position)
                if (item.memberSeqNo == LoginInfoManager.getInstance().user.no) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_event_review_modify), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val intent = Intent(activity, EventReviewRegActivity::class.java)
                                    intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    modifyLauncher.launch(intent)
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(activity)
                }
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
        ApiBuilder.create().getMyEventReviewList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventReview>>> {

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
                        (getParentActivity() as MyEventReviewActivity).getBinding().textMyEventReviewReviewTab.text = getString(R.string.format_registed_review, FormatUtil.getMoneyType(mTotalCount.toString()))
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


    val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setData()
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
        fun newInstance() = MyEventReviewFragment().apply {
            arguments = Bundle().apply {
//                putParcelable(Const.DATA, category)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }
}
