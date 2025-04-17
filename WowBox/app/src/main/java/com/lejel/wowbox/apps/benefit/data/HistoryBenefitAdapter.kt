package com.lejel.wowbox.apps.benefit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.HistoryBenefit
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemHistoryBenefitBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class HistoryBenefitAdapter() : RecyclerView.Adapter<HistoryBenefitAdapter.ViewHolder>() {

    var mDataList: MutableList<HistoryBenefit>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): HistoryBenefit {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<HistoryBenefit>? {

        return mDataList
    }

    fun add(data: HistoryBenefit) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<HistoryBenefit>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: HistoryBenefit) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<HistoryBenefit>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<HistoryBenefit>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemHistoryBenefitBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if(position % 2 == 0){
            holder.itemView.setBackgroundResource(R.color.color_2d2d2d)
        }else{
            holder.itemView.setBackgroundResource(android.R.color.transparent)
        }

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textHistoryBenefitDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))
        holder.binding.textHistoryBenefitProceeds.text = holder.itemView.context.getString(R.string.format_dollar_unit2, FormatUtil.getMoneyTypeFloat(item.proceeds.toString()))
        holder.binding.textHistoryBenefitBenefit.text = holder.itemView.context.getString(R.string.format_dollar_unit2, FormatUtil.getMoneyTypeFloat(item.benefit.toString()))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBenefitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}