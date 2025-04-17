package com.lejel.wowbox.apps.event.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemWinHistoryBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MyWinHistoryAdapter : RecyclerView.Adapter<MyWinHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<EventWin>? = null
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

    fun getItem(position: Int): EventWin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventWin>? {

        return mDataList
    }

    fun add(data: EventWin) {

        if (mDataList == null) {
            mDataList = ArrayList<EventWin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventWin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventWin) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventWin>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemWinHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textWinHistoryName.text = item.event!!.title

        if(item.eventGift!!.giftType == "bol"){
            holder.binding.textWinHistoryGiftName.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(item.amount.toString())))
        }else if(item.eventGift!!.giftType == "point"){
            holder.binding.textWinHistoryGiftName.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(item.amount.toString())))
        }else{
            holder.binding.textWinHistoryGiftName.text = item.giftTitle
        }


        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winDatetime)
        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textWinHistoryDate.text = output.format(d)


        if(StringUtils.isEmpty(item.impression)){
            holder.binding.imageWinHistoryNew.visibility = View.VISIBLE
        }else{
            holder.binding.imageWinHistoryNew.visibility = View.GONE
        }

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