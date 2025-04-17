package com.pplus.prnumberuser.apps.page.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ItemPageEventBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PageEventAdapter : RecyclerView.Adapter<PageEventAdapter.ViewHolder> {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var mIsPrEvent = false


    interface OnItemClickListener {

        fun onItemClick(position: Int, isWinAnnounce: Boolean)
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
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Event>()
    }

    fun setDataList(dataList: MutableList<Event>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPageEventBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_title = binding.textPageEventTitle
        val text_duration = binding.textPageEventDuration
        val text_status = binding.textPageEventStatus

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item = mDataList!![position]

        holder.text_title.text = item.title

        val start = PPLUS_DATE_FORMAT.parse(item.duration!!.start)
        val end = PPLUS_DATE_FORMAT.parse(item.duration!!.end)
        val output = SimpleDateFormat("yy.MM.dd HH:mm")

        holder.text_duration.text = "${output.format(start)} ~ ${output.format(end)}"

        val currentMillis = System.currentTimeMillis()
        val endMillis = end.time
        val startMillis = start.time

        var isWinAnnounce = false

        var isClose = false

        if (currentMillis > endMillis) {
            if (item.status != EventType.EventStatus.announce.name) {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                holder.text_status.text = holder.itemView.context.getString(R.string.word_event_close)
            } else {
                holder.text_status.text = holder.itemView.context.getString(R.string.word_event_announce)
                isWinAnnounce = true
            }

            isClose = true
        } else {
            if (item.status == EventType.EventStatus.active.name) {
                if (currentMillis > startMillis) {
                    holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                    holder.text_status.text = holder.itemView.context.getString(R.string.word_event_ing)
                } else {
                    holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                    holder.text_status.text = holder.itemView.context.getString(R.string.word_event_will)
                }
            }
        }


        holder.itemView.setOnClickListener {

            if (isClose && !isWinAnnounce) {
                ToastUtil.show(holder.itemView.context, R.string.msg_closed_event2)
            } else {
                listener?.onItemClick(holder.adapterPosition, isWinAnnounce)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPageEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}