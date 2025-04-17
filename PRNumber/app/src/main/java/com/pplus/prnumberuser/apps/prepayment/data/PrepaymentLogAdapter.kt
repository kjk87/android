package com.pplus.prnumberuser.apps.prepayment.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.PrepaymentLog
import com.pplus.prnumberuser.databinding.ItemPrepaymentLogBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PrepaymentLogAdapter: RecyclerView.Adapter<PrepaymentLogAdapter.ViewHolder>() {

    var mDataList: MutableList<PrepaymentLog>? = null
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

    fun getItem(position: Int): PrepaymentLog {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PrepaymentLog>? {

        return mDataList
    }

    fun add(data: PrepaymentLog) {

        if (mDataList == null) {
            mDataList = ArrayList<PrepaymentLog>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PrepaymentLog>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<PrepaymentLog>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PrepaymentLog) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<PrepaymentLog>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PrepaymentLog>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPrepaymentLogBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_date = binding.textPrepaymentLogDate
        val text_price = binding.textPrepaymentLogPrice

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
        val binding = ItemPrepaymentLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}