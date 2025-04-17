package com.pplus.prnumberuser.apps.my.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.databinding.ItemWinHistoryBinding
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MyWinHistoryAdapter : RecyclerView.Adapter<MyWinHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<EventWin>? = null
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

    class ViewHolder(binding: ItemWinHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textWinHistoryName
        val text_gift_name = binding.textWinHistoryGiftName
        val text_date = binding.textWinHistoryDate
        val image_new = binding.imageWinHistoryNew

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.event!!.title
        holder.text_gift_name.text = item.gift!!.title

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winDate)
        val output = SimpleDateFormat("yyyy.MM.dd")
        holder.text_date.text = output.format(d)

        if(StringUtils.isEmpty(item.impression)){
            holder.image_new.visibility = View.VISIBLE
        }else{
            holder.image_new.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWinHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}