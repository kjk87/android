package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_visit_history.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class VisitHistoryAdapter : RecyclerView.Adapter<VisitHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<Buy>? = null
    var listener: OnItemClickListener? = null
    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Buy {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Buy>? {

        return mDataList
    }

    fun add(data: Buy) {

        if (mDataList == null) {
            mDataList = ArrayList<Buy>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Buy>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Buy>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Buy) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Buy>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Buy>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_price = itemView.text_visit_history_price
        val text_date = itemView.text_visit_history_date
        val text_status = itemView.text_visit_history_status
        val text_cancel = itemView.text_visit_history_cancel

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))

        holder.text_cancel.visibility = View.GONE
        var date = ""
        when (item.process) {
            1 -> {//결제완료
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                holder.text_status.setText(R.string.word_pay_complete)

                if (StringUtils.isNotEmpty(item.completeDatetime)) {
                    date = PplusCommonUtil.getDateFormat(item.completeDatetime!!)
                    if(PplusCommonUtil.isToday(item.completeDatetime!!)){
                        holder.text_cancel.visibility = View.VISIBLE
                        holder.text_cancel.setOnClickListener {
                            val builder = AlertBuilder.Builder()
                            builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
                            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.html_money_unit2, FormatUtil.getMoneyType(item.price.toString())), AlertBuilder.MESSAGE_TYPE.HTML, 1))
                            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_question_pay_cancel), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                            builder.setOnAlertResultListener(object : OnAlertResultListener {
                                override fun onCancel() {

                                }

                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                                    when (event_alert) {
                                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                                            cancel(holder.itemView.context, item, holder.adapterPosition)
                                        }
                                    }
                                }
                            }).builder().show(holder.itemView.context)
                        }
                    }
                }
            }
            2 -> {//취소
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_purchase_cancel)

                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
                    date = PplusCommonUtil.getDateFormat(item.cancelDatetime!!)
                }
            }
        }

        holder.text_date.text = date

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    private fun getBuy(context: Context, seqNo: Long, position: Int) {
        val params = HashMap<String, String>()
        params["seqNo"] = seqNo.toString()
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
                context.hideProgress()
                if (response!!.data != null) {
                    mDataList!![position] = response.data
                    notifyItemChanged(position)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    private fun cancel(context:Context, buy:Buy, position: Int){
        val params = HashMap<String, String>()
        params["buySeqNo"] = buy.seqNo.toString()
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().buyGoodsListCancel(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()
                context.showAlert(R.string.msg_pay_canceled)
                getBuy(context, buy.seqNo!!, position)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                context.hideProgress()

                if(response!!.resultCode == 704){
                    context.showAlert(R.string.msg_can_not_pay_cancel)
                }
            }
        }).build().call()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_visit_history, parent, false)
        return ViewHolder(v)
    }
}