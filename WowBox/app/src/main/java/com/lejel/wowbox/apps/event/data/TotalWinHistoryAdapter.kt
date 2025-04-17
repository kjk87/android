package com.lejel.wowbox.apps.event.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.databinding.ItemWinHistoryBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class TotalWinHistoryAdapter : RecyclerView.Adapter<TotalWinHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<Event>? = null
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

    fun getItem(position: Int): Event {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Event>? {

        return mDataList
    }

    fun add(data: Event) {

        if (mDataList == null) {
            mDataList = ArrayList<Event>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Event>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Event>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Event) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Event>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Event>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemWinHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.imageWinHistoryNew.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textWinHistoryName.text = item.title
        holder.binding.textWinHistoryGiftName.text = holder.itemView.context.getString(R.string.format_winner_count, FormatUtil.getMoneyType(item.winnerCount.toString()))

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.endDatetime)
        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textWinHistoryDate.text = output.format(d)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWinHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}