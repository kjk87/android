package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Delivery
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_delivery_history.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class DeliveryHistoryAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<DeliveryHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Delivery>? = null
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

    fun getItem(position: Int): Delivery {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Delivery>? {

        return mDataList
    }

    fun add(data: Delivery) {

        if (mDataList == null) {
            mDataList = ArrayList<Delivery>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Delivery>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Delivery>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Delivery) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Delivery>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Delivery>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_address = itemView.text_delivery_address
        val text_price = itemView.text_delivery_price
        val text_date = itemView.text_delivery_date

        init {
            text_address.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_address.text = item.clientAddress
        holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.totalPrice!!.toInt().toString())))

        val output = SimpleDateFormat("MM.dd HH:mm:ss")
        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
        holder.text_date.text = output.format(d)

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_delivery_history, parent, false)
        return ViewHolder(v)
    }
}