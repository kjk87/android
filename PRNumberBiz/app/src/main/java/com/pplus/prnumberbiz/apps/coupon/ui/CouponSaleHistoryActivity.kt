package com.pplus.prnumberbiz.apps.coupon.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.coupon.data.CouponSaleHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.dto.Count
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.activity_coupon_sale_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class CouponSaleHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_coupon_sale_history
    }

    private var mAdapter: CouponSaleHistoryAdapter? = null

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mCoupon: Goods? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {

        mCoupon = intent.getParcelableExtra(Const.GOODS)

        mLayoutManager = LinearLayoutManager(this)
        recycler_coupon_sale_history.layoutManager = mLayoutManager
        mAdapter = CouponSaleHistoryAdapter()
        recycler_coupon_sale_history.adapter = mAdapter

        recycler_coupon_sale_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

//        mAdapter!!.setOnItemClickListener(object : CouponSaleHistoryAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//            }
//        })

        text_coupon_sale_history_coupon_name.text = mCoupon!!.name
        text_coupon_sale_history_sale_count.text = FormatUtil.getMoneyType(mCoupon!!.soldCount.toString())
        getUseCount()
        mPaging = 0
        listCall(mPaging)
    }

    private fun getUseCount() {
        val params = HashMap<String, String>()

        params["goodsSeqNo"] = mCoupon!!.seqNo.toString()
        params["process"] = "3"
        ApiBuilder.create().getBuyGoodsCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
                if (response != null) {
                    val count = response.data.count
                    text_coupon_sale_history_use_count.text = FormatUtil.getMoneyType(count.toString())
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()

        params["goodsSeqNo"] = mCoupon!!.seqNo.toString()
        params["page"] = page.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc}"
        showProgress("")
        ApiBuilder.create().getBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuyGoods>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        if(mTotalCount == 0){
                            layout_coupon_sale_not_exist.visibility = View.VISIBLE
                        }else{
                            layout_coupon_sale_not_exist.visibility = View.GONE
                        }
                        mAdapter!!.clear()
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            Const.REQ_START_DATE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null && StringUtils.isNotEmpty(data.getStringExtra(Const.DATA))) {
//                        val date = data.getStringExtra(Const.DATA)
//                        text_coupon_sale_history_start_date.text = date
//
//                        mStart = date.replace(".", "-")
//                    }
//                }
//            }
//            Const.REQ_END_DATE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null && StringUtils.isNotEmpty(data.getStringExtra(Const.DATA))) {
//                        val date = data.getStringExtra(Const.DATA)
//                        text_coupon_sale_history_end_date.text = date
//                        mEnd = date.replace(".", "-")
//                    }
//                }
//            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history_detail), ToolbarOption.ToolbarMenu.LEFT)

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
