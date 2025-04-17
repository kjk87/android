package com.pplus.luckybol.apps.buff.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.BuffMemberAdapter
import com.pplus.luckybol.apps.buff.data.BuffMemberSelectOneAdapter
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBuffChangeOwnerBinding
import com.pplus.luckybol.databinding.ActivityBuffMemberBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class BuffMemberActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffMemberBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffMemberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mAdapter: BuffMemberAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0
    lateinit var mBuff: Buff

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuff = intent.getParcelableExtra(Const.DATA)!!

        mAdapter = BuffMemberAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffMember.layoutManager = mLayoutManager
        binding.recyclerBuffMember.adapter = mAdapter
        binding.recyclerBuffMember.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))

        binding.recyclerBuffMember.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context,
                    @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter.itemCount > 0 && position == mAdapter.itemCount - 1) {
                outRect.bottom = mLastOffset
            }
        }

    }

    private fun listCall(page: Int) {
        mLockListView = true
        val params = HashMap<String, String>()
        params["buffSeqNo"] = mBuff.seqNo.toString()
        params["page"] = page.toString()
        params["includeMe"] = "true"

        showProgress("")
        ApiBuilder.create().getBuffMemberList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffMember>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffMember>>?) {

                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()

                        val totalCount = response.data!!.totalElements!!
                        if (totalCount > 0) {
                            binding.layoutBuffBuffMemberNotExist.visibility = View.GONE
                        } else {
                            binding.layoutBuffBuffMemberNotExist.visibility = View.VISIBLE
                        }

                        binding.textBuffMemberCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_buff_count, FormatUtil.getMoneyType(totalCount.toString())))
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffMember>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buff_member), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}