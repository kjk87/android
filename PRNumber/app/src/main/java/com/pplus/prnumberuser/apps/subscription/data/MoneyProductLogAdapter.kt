package com.pplus.prnumberuser.apps.subscription.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionLog
import com.pplus.prnumberuser.databinding.ItemAttendanceBinding
import com.pplus.prnumberuser.databinding.ItemMoneyProductLogBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MoneyProductLogAdapter: RecyclerView.Adapter<MoneyProductLogAdapter.ViewHolder>() {

    var mDataList: MutableList<SubscriptionLog>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): SubscriptionLog {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<SubscriptionLog>? {

        return mDataList
    }

    fun add(data: SubscriptionLog) {

        if (mDataList == null) {
            mDataList = ArrayList<SubscriptionLog>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<SubscriptionLog>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<SubscriptionLog>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: SubscriptionLog) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<SubscriptionLog>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<SubscriptionLog>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMoneyProductLogBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_date = binding.textMoneyProductLogDate
        val text_price = binding.textMoneyProductLogPrice

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
//        try {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//
//            val output = SimpleDateFormat("yyyy.MM.dd")
//            holder.text_date.text = output.format(d)
//
//        } catch (e: Exception) {
//            holder.text_date.text = ""
//        }

        holder.text_date.text = item.regDatetime
        holder.text_price.text = "- ${holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(item.usePrice.toString()))}"

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoneyProductLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}