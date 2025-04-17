package com.pplus.luckybol.apps.point.data

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.PointHistory
import com.pplus.luckybol.databinding.ItemPointBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PointHistoryAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<PointHistoryAdapter.ViewHolder>() {

    var mDataList: MutableList<PointHistory>? = null
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

    fun getItem(position: Int): PointHistory {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PointHistory>? {

        return mDataList
    }

    fun add(data: PointHistory) {

        if (mDataList == null) {
            mDataList = ArrayList<PointHistory>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PointHistory>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<PointHistory>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PointHistory) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<PointHistory>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PointHistory>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPointBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textPointName
        val text_date = binding.textPointDate
        val text_price = binding.textPointPrice

        init {
            text_name.setSingleLine()

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text= item.subject
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = output.format(d)

        } catch (e: Exception) {
            holder.text_date.text = ""
        }

        when (item.type) {

            "used"-> {
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_6e7780))
                holder.text_price.text = "- ${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(item.point.toString()))}"
            }

            "charge"-> {
                holder.text_price.text = "${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(item.point.toString()))}"
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_373c42))
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}