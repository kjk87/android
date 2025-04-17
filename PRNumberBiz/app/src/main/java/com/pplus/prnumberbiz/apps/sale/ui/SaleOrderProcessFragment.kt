package com.pplus.prnumberbiz.apps.sale.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberbiz.apps.sale.data.SaleOrderProcessAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.fragment_sale_order_process.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class SaleOrderProcessFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sale_order_process
    }

    override fun initializeView(container: View?) {

    }

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: SaleOrderProcessAdapter? = null
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mTab = 0
    var today = Date()

    override fun init() {

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_sale_order_process.layoutManager = mLayoutManager
        mAdapter = SaleOrderProcessAdapter(activity!!)
        recycler_sale_order_process.adapter = mAdapter
        recycler_sale_order_process.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_30))

        recycler_sale_order_process.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

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

        mAdapter!!.setOnItemClickListener(object : SaleOrderProcessAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, SaleOrderDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
            }
        })

        today = Date(System.currentTimeMillis())
        val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        text_sale_order_process_today.text = output.format(today)

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["type"] = "0"
        params["startDuration"] = output.format(today) + " 00:00:00"
        params["endDuration"] = output.format(today) + " 23:59:59"
        params["page"] = page.toString()
        params["orderProcess"] = mTab.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"

        showProgress("")
        ApiBuilder.create().getBuy(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Buy>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()

                if(!isAdded){
                    return
                }

                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            layout_sale_order_process_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_sale_order_process_not_exist.visibility = View.GONE
                        }

                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mItemOffset
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL->{
                mPaging = 0
                listCall(mPaging)
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt(Const.TAB)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(tab: Int) =
                SaleOrderProcessFragment().apply {
                    arguments = Bundle().apply {
                        putInt(Const.TAB, tab)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
