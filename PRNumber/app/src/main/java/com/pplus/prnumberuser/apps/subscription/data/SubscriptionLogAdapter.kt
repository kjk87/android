package com.pplus.prnumberuser.apps.subscription.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionLog
import com.pplus.prnumberuser.databinding.ItemSubscriptionLogBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SubscriptionLogAdapter() : RecyclerView.Adapter<SubscriptionLogAdapter.ViewHolder>() {

    var mDataList: MutableList<SubscriptionLog>? = null
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

    fun getItem(position: Int): SubscriptionLog {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<SubscriptionLog>? {

        return mDataList
    }

    fun add(data: SubscriptionLog) {

        if (mDataList == null) {
            mDataList = ArrayList<SubscriptionLog>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<SubscriptionLog>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<SubscriptionLog>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: SubscriptionLog) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<SubscriptionLog>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<SubscriptionLog>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemSubscriptionLogBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_use = binding.layoutSubscriptionLogUse
        val text_count = binding.textSubscriptionLogCount
        val text_date = binding.textSubscriptionLogDate

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.seqNo == null){
            holder.layout_use.visibility = View.GONE
        }else{
            holder.layout_use.visibility = View.VISIBLE
            holder.text_count.text = (position+1).toString()
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
            val output = SimpleDateFormat("MM/dd")
            holder.text_date.text = output.format(d)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubscriptionLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}