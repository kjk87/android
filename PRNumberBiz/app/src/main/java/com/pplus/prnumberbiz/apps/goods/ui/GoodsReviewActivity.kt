package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsReviewAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_goods_review.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class GoodsReviewActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_product review"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_review
    }

    private var mAdapter: GoodsReviewAdapter? = null
    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mPage:Page? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = LoginInfoManager.getInstance().user.page

        if (mPage!!.avgEval != null) {
            val avgEval = String.format("%.1f", mPage!!.avgEval)
            text_goods_review_grade.text = avgEval
            grade_bar_goods_review_total.build(avgEval)
        } else {
            text_goods_review_grade.text = "0.0"
            grade_bar_goods_review_total.build("0.0")
        }

        mLayoutManager = LinearLayoutManager(this)
        recycler_goods_review.layoutManager = mLayoutManager
        mAdapter = GoodsReviewAdapter()
        recycler_goods_review.adapter = mAdapter

        recycler_goods_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["pageSeqNo"] = mPage!!.no.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoodsReview(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<GoodsReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        text_goods_review_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_review, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            layout_goods_review_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_goods_review_not_exist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<GoodsReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<GoodsReview>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_REPLY->{
                mPaging = 0
                listCall(mPaging)
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_review), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
