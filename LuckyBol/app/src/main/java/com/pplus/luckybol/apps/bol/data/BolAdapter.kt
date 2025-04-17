package com.pplus.luckybol.apps.bol.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.Bol
import com.pplus.luckybol.databinding.ItemPointBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat


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

    class ViewHolder(binding: ItemPointBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textPointName
        val text_date = binding.textPointDate
        val text_price = binding.textPointPrice

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.subject

        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = output.format(d)

        } catch (e: Exception) {
            holder.text_date.text = ""
        }

        when (item.primaryType) {

            "decrease"-> {
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff0000))
                holder.text_price.text = "- ${holder.itemView.context.getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(item.amount))}"
            }

            "increase" -> {
                holder.text_price.text = holder.itemView.context.getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(item.amount))
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_fc5c57))
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.bindingAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}