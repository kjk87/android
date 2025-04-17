package com.pplus.prnumberbiz.apps.sale.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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
import com.pplus.prnumberbiz.apps.sale.data.SaleGoodsHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Price
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_sale_goods_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleGoodsHistoryFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sale_goods_history
    }

    override fun initializeView(container: View?) {}

    var mInputEnd = false
    var mInputStart = false
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mStart = ""
    var mEnd = ""
    var mPage: Page? = null
    var mProcess = ""

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: SaleGoodsHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mType = EnumData.GoodsType.normal.name
    override fun init() {


        mPage = LoginInfoManager.getInstance().user.page
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_sale_goods_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_sale_goods_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))



        text_sale_goods_history_start_date.setOnClickListener {
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

                text_sale_goods_history_start_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_goods_history_end_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_sale_goods_history_end_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_goods_history_filter.setOnClickListener {

            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_total), getString(R.string.word_pay_complete), getString(R.string.word_use_complete), getString(R.string.word_buy_cancel))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            text_sale_goods_history_filter.setText(R.string.word_total)
                            mProcess = ""
                        }
                        2 -> {
                            text_sale_goods_history_filter.setText(R.string.word_pay_complete)
                            mProcess = "1"
                        }

                        3 -> {
                            text_sale_goods_history_filter.setText(R.string.word_use_complete)
                            mProcess = "3"
                        }
                        4 -> {
                            text_sale_goods_history_filter.setText(R.string.word_buy_cancel)
                            mProcess = "2"
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)
                }
            }).builder().show(activity)
        }

        when(mType){
            EnumData.GoodsType.hotdeal.name->{
                text_sale_goods_history_title.setText(R.string.word_hotdeal_goods_sale_history)
            }
            EnumData.GoodsType.plus.name->{
                text_sale_goods_history_title.setText(R.string.word_plus_goods_sale_history)
            }
            else->{
                text_sale_goods_history_title.setText(R.string.word_normal_goods_sale_history)
            }
        }

        text_sale_goods_history_search.setOnClickListener {
            getTotalPrice()
        }

        mLayoutManager = LinearLayoutManager(activity)
        recycler_sale_goods_history.layoutManager = mLayoutManager
        mAdapter = SaleGoodsHistoryAdapter(activity!!)
        recycler_sale_goods_history.adapter = mAdapter
//        recycler_sale_goods_history.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_30))

        recycler_sale_goods_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : SaleGoodsHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, SaleGoodsDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)

            }
        })

        getTotalPrice()

    }


    private fun getTotalPrice() {
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["process"] = "3"
        params["type"] = "1"
        when(mType){
            EnumData.GoodsType.hotdeal.name->{
                params["isHotdeal"] = "true"
            }
            EnumData.GoodsType.plus.name->{
                params["isPlus"] = "true"
            }
            else->{
                params["isHotdeal"] = "false"
                params["isPlus"] = "false"
            }
        }
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        ApiBuilder.create().getBuyGoodsPrice(params).setCallback(object : PplusCallback<NewResultResponse<Price>> {
            override fun onResponse(call: Call<NewResultResponse<Price>>?, response: NewResultResponse<Price>?) {
                if(!isAdded){
                    return
                }
                if (response != null && response.data != null) {
                    var price = 0

                    if (response.data.price != null) {
                        price = response.data.price!!
                    }
                    text_sale_order_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(price.toString())))
                } else {
                    text_sale_order_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, "0"))
                }

                mPaging = 0
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Price>>?, t: Throwable?, response: NewResultResponse<Price>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["type"] = "1"
        params["startDuration"] = mStart
        params["endDuration"] = mEnd

        when(mType){
            EnumData.GoodsType.hotdeal.name->{
                params["isHotdeal"] = "true"
            }
            EnumData.GoodsType.plus.name->{
                params["isPlus"] = "true"
            }
            else->{
                params["isHotdeal"] = "false"
                params["isPlus"] = "false"
            }
        }

        if (StringUtils.isNotEmpty(mProcess)) {
            params["process"] = mProcess
        }

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc}"
        showProgress("")
        ApiBuilder.create().getBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuyGoods>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()

                if(!isAdded){
                    return
                }

                if (response != null) {
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
//                        text_goods_sale_total_history_count.text = getString(R.string.format_goods_sale_history, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            layout_sale_goods_history_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_sale_goods_history_not_exist.visibility = View.GONE
                        }
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
            Const.REQ_DETAIL -> {
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
            mType = it.getString(Const.TYPE)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(type: String) =
                SaleGoodsHistoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(Const.TYPE, type)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
