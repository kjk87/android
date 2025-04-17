package com.pplus.luckybol.apps.event.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResultJpa
import com.pplus.luckybol.databinding.ItemEventResultBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class EventResultAdapter() : RecyclerView.Adapter<EventResultAdapter.ViewHolder>() {

    var mDataList: MutableList<EventResultJpa>? = null
    var listener: OnItemClickListener? = null
    var mEvent: Event? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventResultJpa {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventResultJpa>? {

        return mDataList
    }

    fun add(data: EventResultJpa) {

        if (mDataList == null) {
            mDataList = ArrayList<EventResultJpa>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventResultJpa>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventResultJpa>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventResultJpa) {
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<EventResultJpa>()

    }

    fun setDataList(dataList: MutableList<EventResultJpa>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemEventResultBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_msg = binding.textEventResultMsg
        val text_gift = binding.textEventResultGift

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if(item.win == null){
            holder.text_msg.text = holder.itemView.context.getString(R.string.word_save_reward_bol)
            holder.text_gift.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_fc5c57))
            if(mEvent != null){
                holder.text_gift.text = holder.itemView.context.getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat(mEvent!!.earnedPoint.toString()))
            }
        }else{
            holder.text_msg.text = holder.itemView.context.getString(R.string.word_cong_win)
            holder.text_gift.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
            holder.text_gift.text = item.win!!.eventGift!!.title
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}