package com.pplus.luckybol.apps.my.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.databinding.ItemWinHistoryBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class TotalWinHistoryAdapter : RecyclerView.Adapter<TotalWinHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
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

    class ViewHolder(binding: ItemWinHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textWinHistoryName
        val text_gift_name = binding.textWinHistoryGiftName
        val text_date = binding.textWinHistoryDate
        val image_new = binding.imageWinHistoryNew

        init {

            image_new.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.title
        holder.text_gift_name.text = mContext?.getString(R.string.format_winner_count, FormatUtil.getMoneyType(item.winnerCount.toString()))

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.duration!!.end)
        val output = SimpleDateFormat("yyyy.MM.dd")
        holder.text_date.text = output.format(d)

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWinHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}