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
import com.pplus.luckybol.apps.buff.data.BuffPostLikeAdapter
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.dto.BuffPostLike
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.ActivityBuffPostLikeBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffPostLikeActivity : BaseActivity() {

    private lateinit var binding: ActivityBuffPostLikeBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffPostLikeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mBuffPost:BuffPost
    private lateinit var mAdapter: BuffPostLikeAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mIsLast = false
    private var mLockListView = false
    private var mPaging = 0

    override fun initializeView(savedInstanceState: Bundle?) {
        mBuffPost = intent.getParcelableExtra(Const.DATA)!!

        mAdapter = BuffPostLikeAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffPostLike.layoutManager = mLayoutManager
        binding.recyclerBuffPostLike.adapter = mAdapter
        binding.recyclerBuffPostLike.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_200))

        binding.recyclerBuffPostLike.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        when (mBuffPost.type) { // normal, shoppingBuff, lottoBuff , eventBuff, eventGift
            "normal" -> {
                binding.textBuffPostLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_like, 0, 0, 0)
                binding.textBuffPostLikeTitle.setText(R.string.word_buff_like)
            }
            "shoppingBuff" -> {
                binding.textBuffPostLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                binding.textBuffPostLikeTitle.setText(R.string.word_buff_thankyou)

            }
            "lottoBuff" -> {
                binding.textBuffPostLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                binding.textBuffPostLikeTitle.setText(R.string.word_buff_thankyou)
            }
            "eventBuff" -> {
                binding.textBuffPostLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                binding.textBuffPostLikeTitle.setText(R.string.word_buff_thankyou)
            }
            "eventGift" -> {
                binding.textBuffPostLikeCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_cong, 0, 0, 0)
                binding.textBuffPostLikeTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_cong, 0, 0, 0)
            }
        }
        mPaging = 0
        listCall(mPaging)
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

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
        params["page"] = page.toString()
        showProgress("")
        ApiBuilder.create().getBuffPostLikeList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffPostLike>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffPostLike>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffPostLike>>?) {

                hideProgress()
                if (response?.data != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mAdapter.clear()

                        val totalCount = response.data!!.totalElements!!

                        binding.textBuffPostLikeCount.text = FormatUtil.getMoneyType(totalCount.toString())
                    }
                    mLockListView = false

                    val dataList = response.data!!.content!!
                    mAdapter.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffPostLike>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffPostLike>>?) {
                hideProgress()
            }
        }).build().call()
    }
}