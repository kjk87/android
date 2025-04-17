package com.pplus.prnumberuser.apps.bol.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Bol
import com.pplus.prnumberuser.databinding.ItemPointBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class BolAdapter() : RecyclerView.Adapter<BolAdapter.ViewHolder>() {

    var mDataList: MutableList<Bol>? = null
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

    fun getItem(position: Int): Bol {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Bol>? {

        return mDataList
    }

    fun add(data: Bol) {

        if (mDataList == null) {
            mDataList = ArrayList<Bol>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Bol>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Bol>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Bol) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Bol>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Bol>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemPointBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textPointName.text = item.subject

        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.binding.textPointDate.text = output.format(d)

        } catch (e: Exception) {
            holder.binding.textPointDate.text = ""
        }

        when (item.primaryType) {

            "decrease" -> {
                holder.binding.textPointPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff0000))
                holder.binding.textPointPrice.text = "- ${holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(item.amount))}"
            }
            "increase" -> {
                holder.binding.textPointPrice.text = "${holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(item.amount))}"
                holder.binding.textPointPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}