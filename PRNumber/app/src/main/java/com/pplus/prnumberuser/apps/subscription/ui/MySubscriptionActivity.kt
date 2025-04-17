package com.pplus.prnumberuser.apps.subscription.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.subscription.data.MySubscriptionAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMySubscriptionBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class MySubscriptionActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMySubscriptionBinding

    override fun getLayoutView(): View {
        binding = ActivityMySubscriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: MySubscriptionAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerMySubscription.layoutManager = mLayoutManager!!
        mAdapter = MySubscriptionAdapter()
        binding.recyclerMySubscription.adapter = mAdapter
        binding.recyclerMySubscription.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_48, R.dimen.height_48))
        binding.recyclerMySubscription.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.listener = object : MySubscriptionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                when (item.type) {
                    Const.PREPAYMENT -> {
                        val intent = Intent(this@MySubscriptionActivity, MoneyProductDownloadDetailActivity::class.java)
                        intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        detailLauncher.launch(intent)
                    }
                    else -> {
                        val intent = Intent(this@MySubscriptionActivity, SubscriptionDownloadDetailActivity::class.java)
                        intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        detailLauncher.launch(intent)
                    }
                }

            }
        }

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getSubscriptionDownloadListByMemberSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<SubscriptionDownload>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<SubscriptionDownload>>>?, response: NewResultResponse<SubResultResponse<SubscriptionDownload>>?) {
                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        if (mTotalCount > 0) {
                            binding.textMySubscriptionNotExist.visibility = View.GONE
                        } else {
                            binding.textMySubscriptionNotExist.visibility = View.VISIBLE
                        }
                        binding.textMySubscriptionCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<SubscriptionDownload>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<SubscriptionDownload>>?) {
                mLockListView = false
                hideProgress()
            }

        }).build().call()
    }

    val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 0
        listCall(mPaging)
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

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_retention_prepayment_voucher), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}