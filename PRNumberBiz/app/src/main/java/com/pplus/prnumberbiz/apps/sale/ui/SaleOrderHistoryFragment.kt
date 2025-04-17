package com.pplus.prnumberbiz.apps.sale.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.sale.data.SaleOrderHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.dto.OrderTypeCount
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_sale_order_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleOrderHistoryFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sale_order_history
    }

    override fun initializeView(container: View?) {}

    var mInputEnd = false
    var mInputStart = false
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mStart = ""
    var mEnd = ""
    var mPage:Page? = null
    var mOrderType = ""

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: SaleOrderHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun init() {
        mPage = LoginInfoManager.getInstance().user.page
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_sale_order_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_sale_order_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))



        text_sale_order_history_start_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                startCalendar.set(i, i1, i2)
                //                        if(startCalendar.compareTo(mTodayCalendar) > 0){
                //                            ToastUtil.show(getActivity(), R.string.msg_input_startday_before_endday);
                //                            return;
                //                        }

                if (mInputEnd) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_startday_before_endday)
                        return@OnDateSetListener
                    }
                }
                mInputStart = true

                text_sale_order_history_start_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_order_history_end_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_sale_order_history_end_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1+1), DateFormatUtils.formatTime(i2))
                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1+1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_order_history_filter.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_total), getString(R.string.word_order_store), getString(R.string.word_order_packing), getString(R.string.word_order_delivery))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert.getValue()) {
                        1 -> {
                            text_sale_order_history_filter.setText(R.string.word_total)
                            mOrderType = ""
                        }
                        2 -> {
                            text_sale_order_history_filter.setText(R.string.word_order_store)
                            mOrderType = "0"
                        }
                        3 -> {
                            text_sale_order_history_filter.setText(R.string.word_order_packing)
                            mOrderType = "1"
                        }
                        4 -> {
                            text_sale_order_history_filter.setText(R.string.word_order_delivery)
                            mOrderType = "2"
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)
                }
            }).builder().show(activity)
        }

        text_sale_order_history_search.setOnClickListener {
            getTotalPrice()
        }

        text_sale_order_history_view_detail.setOnClickListener {
            val intent = Intent(activity, AlertSaleOrderActivity::class.java)
            intent.putExtra(Const.START, mStart)
            intent.putExtra(Const.END, mEnd)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)

        }

        mLayoutManager = LinearLayoutManager(activity)
        recycler_sale_order_history.layoutManager = mLayoutManager
        mAdapter = SaleOrderHistoryAdapter(activity!!)
        recycler_sale_order_history.adapter = mAdapter
        recycler_sale_order_history.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_30, R.dimen.height_30))

        recycler_sale_order_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

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

        mAdapter!!.setOnItemClickListener(object : SaleOrderHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, SaleOrderDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)

            }
        })

        getTotalPrice()

    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
        }
    }

    private fun getTotalPrice(){
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["type"] = "0"
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        if(StringUtils.isNotEmpty(mOrderType)){
            params["orderType"] = mOrderType
        }
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"

        showProgress("")
        ApiBuilder.create().getBuyPrice(params).setCallback(object : PplusCallback<NewResultResponse<Float>> {
            override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
                hideProgress()
                if(response!!.data != null){
                    text_sale_order_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(response.data.toInt().toString())))
                }else{
                    text_sale_order_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, "0"))
                }

                getOrderCount()

            }

            override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getOrderCount(){
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["type"] = "0"
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        if(StringUtils.isNotEmpty(mOrderType)){
            params["orderType"] = mOrderType
        }
        showProgress("")
        ApiBuilder.create().getBuyCountOrderType(params).setCallback(object : PplusCallback<NewResultResponse<OrderTypeCount>> {
            override fun onResponse(call: Call<NewResultResponse<OrderTypeCount>>?, response: NewResultResponse<OrderTypeCount>?) {

                if(!isAdded){
                    return
                }

                if(response != null){
                    text_sale_order_history_store_count.text = FormatUtil.getMoneyType(response.data.payCount!!.toInt().toString())
                    text_sale_order_history_packing_count.text = FormatUtil.getMoneyType(response.data.wrapCount!!.toInt().toString())
                    text_sale_order_history_delivery_count.text = FormatUtil.getMoneyType(response.data.deliveryCount!!.toInt().toString())
                }

                mPaging = 0
                listCall(mPaging)

            }

            override fun onFailure(call: Call<NewResultResponse<OrderTypeCount>>?, t: Throwable?, response: NewResultResponse<OrderTypeCount>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["type"] = "0"
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        params["page"] = page.toString()
        if(StringUtils.isNotEmpty(mOrderType)){
            params["orderType"] = mOrderType
        }

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
                            layout_sale_order_history_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_sale_order_history_not_exist.visibility = View.GONE
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL->{
                getTotalPrice()
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            mTab = it.getInt(Const.TAB)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                SaleOrderHistoryFragment().apply {
                    arguments = Bundle().apply {
                        //                        putInt(Const.TAB, tab)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
