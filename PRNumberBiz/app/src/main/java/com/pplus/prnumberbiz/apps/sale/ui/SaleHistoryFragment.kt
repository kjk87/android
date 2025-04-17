package com.pplus.prnumberbiz.apps.sale.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoodsTypePrice
import com.pplus.prnumberbiz.core.network.model.dto.DeliveryTotalPrice
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_sale_history.*
import kotlinx.android.synthetic.main.item_delivery.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class SaleHistoryFragment : BaseFragment<BaseActivity>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_sale_history
    }

    override fun initializeView(container: View?) {

    }

    var mInputEnd = false
    var mInputStart = false
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    var mStart = ""
    var mEnd = ""
    var mPage: Page? = null

    override fun init() {
        mPage = LoginInfoManager.getInstance().user.page
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
        text_sale_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
        mEnd = endCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
        text_sale_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))



        text_sale_history_start_date.setOnClickListener {
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

                text_sale_history_start_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mStart = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 00:00:00"
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_history_end_date.setOnClickListener {
            DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                endCalendar.set(i, i1, i2)
                if (mInputStart) {
                    if (startCalendar.compareTo(endCalendar) > 0) {
                        ToastUtil.show(activity, R.string.msg_input_endday_after_startday)
                        return@OnDateSetListener
                    }
                }

                mInputEnd = true

                text_sale_history_end_date.text = getString(R.string.format_date, i.toString(), DateFormatUtils.formatTime(i1 + 1), DateFormatUtils.formatTime(i2))
                mEnd = "${i.toString()}-${DateFormatUtils.formatTime(i1 + 1)}-${DateFormatUtils.formatTime(i2)} 23:59:59"
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        text_sale_history_search.setOnClickListener {
            getData()
        }

        layout_sale_history_normal.setOnClickListener {
            val intent = Intent(activity, SaleGoodsHistoryActivity::class.java)
            intent.putExtra(Const.TYPE, EnumData.GoodsType.normal.name)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
        }
        layout_sale_history_hotdeal.setOnClickListener {
            val intent = Intent(activity, SaleGoodsHistoryActivity::class.java)
            intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
        }

        layout_sale_history_plus.setOnClickListener {
            val intent = Intent(activity, SaleGoodsHistoryActivity::class.java)
            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
        }
        getData()

    }

    private fun getData() {
        if (LoginInfoManager.getInstance().user.page!!.isDelivery!!) {
            layout_sale_history_not_delivery.visibility = View.GONE
            layout_sale_history_delivery.visibility = View.VISIBLE
            getDeliveryTotalPrice()
        } else {
            layout_sale_history_not_delivery.visibility = View.VISIBLE
            layout_sale_history_delivery.visibility = View.GONE
            getTotalPrice()
        }
    }

    private fun getDeliveryTotalPrice() {
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["startDuration"] = mStart
        params["endDuration"] = mEnd
        showProgress("")
        ApiBuilder.create().getDeliveryTotalPrice(params).setCallback(object : PplusCallback<NewResultResponse<DeliveryTotalPrice>> {
            override fun onResponse(call: Call<NewResultResponse<DeliveryTotalPrice>>?, response: NewResultResponse<DeliveryTotalPrice>?) {
                if(!isAdded){
                    return
                }
                hideProgress()
                if (response != null) {
                    val deliveryPrices = response.datas
                    layout_sale_history_delivery.removeAllViews()
                    var totalPrice = 0
                    for (delivery in deliveryPrices) {

                        val view = layoutInflater.inflate(R.layout.item_delivery, null)

                        when (delivery.companySeqNo) {
                            1L -> {//피플러스
                                view.image_delivery.setImageResource(R.drawable.ic_selling_app_prnumber)
                                view.text_delivery_title.setText(R.string.word_prnumber_sale_price)

                            }
                            2L -> {//배민
                                view.image_delivery.setImageResource(R.drawable.ic_selling_app_baemin)
                                view.text_delivery_title.setText(R.string.word_baemin_sale_price)
                            }
                            3L -> {//요기요
                                view.image_delivery.setImageResource(R.drawable.ic_selling_app_yogiyo)
                                view.text_delivery_title.setText(R.string.word_yogiyo_sale_price)
                            }
                            4L -> {//배달통
                                view.image_delivery.setImageResource(R.drawable.ic_selling_app_bdtong)
                                view.text_delivery_title.setText(R.string.word_baedaltong_sale_price)
                            }
                        }

                        view.setOnClickListener {
                            val intent = Intent(activity, SaleOrderHistoryActivity::class.java)
                            intent.putExtra(Const.KEY, delivery.companySeqNo!!.toInt())
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
                        }

                        view.text_delivery_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(delivery.price!!.toInt().toString())))


                        totalPrice += delivery.price!!.toInt()

                        layout_sale_history_delivery.addView(view)
                    }

                    text_sale_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(totalPrice.toString())))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<DeliveryTotalPrice>>?, t: Throwable?, response: NewResultResponse<DeliveryTotalPrice>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getTotalPrice() {
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

                    text_sale_history_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(data.totalPrice!!.toInt().toString())))
                    text_Sale_history_normal_goods_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(data.generalPrice!!.toInt().toString())))
                    text_sale_history_normal_hotdeal_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(data.hotdealPrice!!.toInt().toString())))
                    text_sale_history_normal_plus_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(data.plusPrice!!.toInt().toString())))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuyGoodsTypePrice>>?, t: Throwable?, response: NewResultResponse<BuyGoodsTypePrice>?) {
                hideProgress()
            }
        }).build().call()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
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
                SaleHistoryFragment().apply {
                    arguments = Bundle().apply {
                        //                        putInt(Const.TAB, tab)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
