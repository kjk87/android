package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import kotlinx.android.synthetic.main.item_sale_order_history.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SaleOrderHistoryAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SaleOrderHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Buy>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Buy {

        return mDataList!!.get(position)
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

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_title = itemView.text_sale_order_title
        val text_buyer_name = itemView.text_sale_order_buyer_name
        val text_status = itemView.text_sale_order_status
        val text_type = itemView.text_sale_order_type

        init {
            text_title.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_title.text = item.title

        holder.text_buyer_name.text = item.buyerName

        when (item.orderType) {
            0 -> {//매장
                holder.text_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff696a))
                holder.text_type.setText(R.string.word_order_store)
            }
            1 -> {//포장
                holder.text_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_a26df3))
                holder.text_type.setText(R.string.word_order_packing)

            }
            2 -> {//배달
                holder.text_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_3ec082))
                holder.text_type.setText(R.string.word_order_delivery)

            }
        }

        LogUtil.e("ORDERPROCESS", "item.orderProcess : {}", item.orderProcess)
        when (item.orderProcess) {
            -1 -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_status.setText(R.string.word_pay_complete)
            }
            0 -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_status.setText(R.string.word_order_ready)
            }
            1 -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_status.setText(R.string.word_order_ing)
            }
            2 -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_complete)
            }
            3 -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_order_cancel)
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_order_history, parent, false)
        return ViewHolder(v)
    }
}