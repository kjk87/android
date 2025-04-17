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
import com.pplus.prnumberbiz.apps.sale.data.BuyHistoryAdapter
import com.pplus.prnumberbiz.apps.sale.data.SaleGoodsHistoryAdapter
import com.pplus.prnumberbiz.apps.sale.data.VisitHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_buy_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class BuyHistoryFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_buy_history
    }

    override fun initializeView(container: View?) {}

    var mInputEnd = false
    var mInputStart = false
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mTab = 0
    var mStart = ""
    var mEnd = ""
    var mPage: Page? = null
    var mProcess = ""

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: BuyHistoryAdapter? = null
    private var mVisitAdapter: VisitHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
//    private var mType = EnumData.GoodsType.normal.name
    private var mIsLast = false

    override fun init() {


        mPage = LoginInfoManager.getInstance().user.page
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_buy_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_buy_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))



        text_buy_history_start_date.setOnClickListener {
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

                text_buy_history_start_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_buy_history_end_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_buy_history_end_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_buy_history_filter.setOnClickListener {

            val builder = AlertBuilder.Builder()
            when(mTab){
                0->{
                    builder.setContents(getString(R.string.word_total), getString(R.string.word_pay_complete), getString(R.string.word_use_complete), getString(R.string.word_buy_cancel))
                }
                1->{
                    builder.setContents(getString(R.string.word_total), getString(R.string.word_pay_complete), getString(R.string.word_buy_cancel))
                }
            }

            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when(mTab){
                        0->{
                            when (event_alert.getValue()) {
                                1 -> {
                                    text_buy_history_filter.setText(R.string.word_total)
                                    mProcess = ""
                                }
                                2 -> {
                                    text_buy_history_filter.setText(R.string.word_pay_complete)
                                    mProcess = "1"
                                }

                                3 -> {
                                    text_buy_history_filter.setText(R.string.word_use_complete)
                                    mProcess = "3"
                                }
                                4 -> {
                                    text_buy_history_filter.setText(R.string.word_buy_cancel)
                                    mProcess = "2"
                                }
                            }
                            mPaging = 0
                            listCall(mPaging)
                        }
                        1->{
                            when (event_alert.getValue()) {
                                1 -> {
                                    text_buy_history_filter.setText(R.string.word_total)
                                    mProcess = ""
                                }
                                2 -> {
                                    text_buy_history_filter.setText(R.string.word_pay_complete)
                                    mProcess = "1"
                                }

                                3 -> {
                                    text_buy_history_filter.setText(R.string.word_buy_cancel)
                                    mProcess = "2"
                                }
                            }
                            mPaging = 0
                            visitListCall(mPaging)
                        }
                    }

                }
            }).builder().show(activity)
        }
        text_buy_history_title.setText(R.string.word_normal_goods_sale_history)

        text_buy_history_search.setOnClickListener {
            getNFCPrice()
        }

        mLayoutManager = LinearLayoutManager(activity)
        recycler_buy_history.layoutManager = mLayoutManager
        mAdapter = BuyHistoryAdapter()
        mVisitAdapter = VisitHistoryAdapter()
//        recycler_sale_goods_history.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_30))

        recycler_buy_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : BuyHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, BuyHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)

            }
        })

        layout_buy_history_goods_tab.setOnClickListener {
            if(mTab != 0){
                mTab = 0
                setTab()
            }

        }

        layout_buy_history_visit_tab.setOnClickListener {
            if(mTab != 1){
                mTab = 1
                setTab()
            }
        }

        getNFCPrice()
        mTab = 0
        setTab()

    }

    private fun setTab(){
        when(mTab){
            0->{
                layout_buy_history_goods_tab.isSelected = true
                layout_buy_history_visit_tab.isSelected = false
                recycler_buy_history.adapter = mAdapter
                text_buy_history_filter.setText(R.string.word_total)
                mProcess = ""
                mPaging = 0
                listCall(mPaging)
                text_buy_history_title.text = getString(R.string.word_pre_pay_history)
            }
            1->{
                layout_buy_history_goods_tab.isSelected = false
                layout_buy_history_visit_tab.isSelected = true
                recycler_buy_history.adapter = mVisitAdapter
                text_buy_history_filter.setText(R.string.word_total)
                mProcess = ""
                mPaging = 0
                visitListCall(mPaging)
                text_buy_history_title.text = getString(R.string.word_visit_pay_history)
            }
        }
    }

    private fun getNFCPrice(){
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        params["orderType"] = EnumData.OrderType.nfc.type.toString()
        params["process"] = "1"
        showProgress("")
        ApiBuilder.create().getBuyPrice(params).setCallback(object : PplusCallback<NewResultResponse<Float>> {
            override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
                hideProgress()
                if(response != null && response.data != null){
                    val nfcPrice = response.data.toInt()
                    getPrice(nfcPrice)
                }else{
                    getPrice(0)
                }


            }

            override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getPrice(nfcPrice:Int) {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["type"] = "1"
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        showProgress("")
        ApiBuilder.create().getPriceGoodsType(params).setCallback(object : PplusCallback<NewResultResponse<BuyGoodsTypePrice>> {
            override fun onResponse(call: Call<NewResultResponse<BuyGoodsTypePrice>>?, response: NewResultResponse<BuyGoodsTypePrice>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                if (response != null) {
                    val data = response.data
                    var price = 0

                    if (response.data.generalPrice != null) {
                        price = response.data.generalPrice!!.toInt()
                    }
                    text_buy_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType((price+nfcPrice).toString())))
                }else{
                    text_buy_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, nfcPrice.toString()))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuyGoodsTypePrice>>?, t: Throwable?, response: NewResultResponse<BuyGoodsTypePrice>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun visitListCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["isHotdeal"] = "false"
        params["isPlus"] = "false"
        params["orderType"] = "4"

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getBuy(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Buy>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()

                if(!isAdded){
                    return
                }

                if (response != null) {

                    mIsLast = response.data.last!!

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        text_buy_history_title.text = getString(R.string.word_visit_pay_history) + " " + getString(R.string.format_count2, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mVisitAdapter!!.clear()
                        if (mTotalCount > 0) {
                            layout_buy_history_not_exist.visibility = View.GONE
                        } else {
                            layout_buy_history_not_exist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mVisitAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                mLockListView = false
            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["isHotdeal"] = "false"
        params["isPlus"] = "false"
        params["type"] = "1"

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getBuy(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Buy>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()

                if(!isAdded){
                    return
                }

                if (response != null) {

                    mIsLast = response.data.last!!

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        text_buy_history_title.text = getString(R.string.word_pre_pay_history) + " " + getString(R.string.format_count2, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mAdapter!!.clear()
                        if (mTotalCount > 0) {
                            layout_buy_history_not_exist.visibility = View.GONE
                        } else {
                            layout_buy_history_not_exist.visibility = View.VISIBLE
                        }
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Buy>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Buy>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                mLockListView = false
            }
        }).build().call()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL -> {
                getNFCPrice()
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            mType = it.getString(Const.TYPE)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                BuyHistoryFragment().apply {
                    arguments = Bundle().apply {
//                        putString(Const.TYPE, type)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
