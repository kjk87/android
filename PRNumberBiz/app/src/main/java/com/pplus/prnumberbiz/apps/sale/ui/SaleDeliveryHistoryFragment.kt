package com.pplus.prnumberbiz.apps.sale.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.sale.data.DeliveryHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Delivery
import com.pplus.prnumberbiz.core.network.model.dto.DeliveryTotalPrice
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_sale_delivery_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleDeliveryHistoryFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sale_delivery_history
    }

    override fun initializeView(container: View?) {}

    var mInputEnd = false
    var mInputStart = false
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mStart = ""
    var mEnd = ""
    var mPage: Page? = null
    var mOrderType = ""

    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mAdapter: DeliveryHistoryAdapter? = null
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mKey = 1

    override fun init() {
        mPage = LoginInfoManager.getInstance().user.page
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_sale_delivery_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_sale_delivery_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))



        text_sale_delivery_history_start_date.setOnClickListener {
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

                text_sale_delivery_history_start_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_delivery_history_end_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_sale_delivery_history_end_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_delivery_history_search.setOnClickListener {
            getDeliveryTotal()
        }

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler_sale_delivery_history.layoutManager = mLayoutManager
        mAdapter = DeliveryHistoryAdapter(activity!!)
        recycler_sale_delivery_history.adapter = mAdapter
//        recycler_sale_delivery_history.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_30))

        recycler_sale_delivery_history.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : DeliveryHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, SaleDeliveryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)

            }
        })

        when(mKey){
            2->{//배민
                image_sale_delivery_history_type.setImageResource(R.drawable.ic_selling_app_baemin)
                text_sale_delivery_history_total_price_title.setText(R.string.word_baemin_sale_price)
            }
            3->{//요기요
                image_sale_delivery_history_type.setImageResource(R.drawable.ic_selling_app_yogiyo)
                text_sale_delivery_history_total_price_title.setText(R.string.word_yogiyo_sale_price)
            }
            4->{//배달통
                image_sale_delivery_history_type.setImageResource(R.drawable.ic_selling_app_bdtong)
                text_sale_delivery_history_total_price_title.setText(R.string.word_baedaltong_sale_price)
            }
        }

        getDeliveryTotal()
    }

    private fun getDeliveryTotal(){
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["companySeqNo"] = mKey.toString()
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        showProgress("")
        ApiBuilder.create().getDeliveryCompanyTotalPrice(params).setCallback(object : PplusCallback<NewResultResponse<DeliveryTotalPrice>> {
            override fun onResponse(call: Call<NewResultResponse<DeliveryTotalPrice>>?, response: NewResultResponse<DeliveryTotalPrice>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                if(response!!.datas.isNotEmpty()){
                    var totalPrice = 0
                    for(deliveryTotal in response.datas){
                        when(deliveryTotal.payment){
                            getString(R.string.word_pre_pay)->{
                                text_sale_delivery_history_pre_pay_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_count_unit1, FormatUtil.getMoneyType(deliveryTotal.count.toString())))
                                text_sale_delivery_history_pre_pay_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(deliveryTotal.price.toString())))
                            }
                            getString(R.string.word_card)->{
                                text_sale_delivery_history_card_pay_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_count_unit1, FormatUtil.getMoneyType(deliveryTotal.count.toString())))
                                text_sale_delivery_history_card_pay_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(deliveryTotal.price.toString())))
                            }
                            getString(R.string.word_cash)->{
                                text_sale_delivery_history_cash_pay_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_count_unit1, FormatUtil.getMoneyType(deliveryTotal.count.toString())))
                                text_sale_delivery_history_cash_pay_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(deliveryTotal.price.toString())))
                            }
                        }

                        totalPrice += deliveryTotal.price!!.toInt()
                    }

                    text_sale_delivery_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(totalPrice.toString())))
                }

                mPaging = 0
                listCall(mPaging)

            }

            override fun onFailure(call: Call<NewResultResponse<DeliveryTotalPrice>>?, t: Throwable?, response: NewResultResponse<DeliveryTotalPrice>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        params["companySeqNo"] = mKey.toString()
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getDelivery(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Delivery>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Delivery>>>?, response: NewResultResponse<SubResultResponse<Delivery>>?) {
                hideProgress()
                if(!isAdded){
                    return
                }
                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        text_sale_delivery_history_total_count.text = getString(R.string.word_order_history) + "("+FormatUtil.getMoneyType(mTotalCount.toString())+")"
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            layout_sale_delivery_history_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_sale_delivery_history_not_exist.visibility = View.GONE
                        }

                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Delivery>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Delivery>>?) {
                hideProgress()
            }
        }).build().call()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_DETAIL->{
                getDeliveryTotal()
            }
        }
    }

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
                        mKey = it.getInt(Const.KEY)
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(key:Int) =
                SaleDeliveryHistoryFragment().apply {
                    arguments = Bundle().apply {
                        putInt(Const.KEY, key)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
