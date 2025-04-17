package com.lejel.wowbox.apps.cash.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.HistoryCash
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemHistoryCashBinding
import com.pplus.utils.part.resource.ResourceUtil
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class HistoryCashAdapter() : RecyclerView.Adapter<HistoryCashAdapter.ViewHolder>() {

    var mDataList: MutableList<HistoryCash>? = null
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

    fun getItem(position: Int): HistoryCash {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<HistoryCash>? {

        return mDataList
    }

    fun add(data: HistoryCash) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<HistoryCash>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: HistoryCash) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList()
    }

    fun setDataList(dataList: MutableList<HistoryCash>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemHistoryCashBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))
        holder.binding.textHistoryCashRegDate.text = regDate
        holder.binding.textHistoryCashSubject.text = item.subject
        when(item.type){
            "used", "retrieve"->{
                holder.binding.textHistoryCash.text = "- ${FormatUtil.getMoneyType(item.cash.toString())}"
                holder.binding.textHistoryCash.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
            }
            "charge", "provide"->{
                holder.binding.textHistoryCash.text = FormatUtil.getMoneyType(item.cash.toString())
                holder.binding.textHistoryCash.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
            }
        }
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryCashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}