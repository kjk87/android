package com.pplus.luckybol.apps.setting.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.core.network.model.dto.Notice
import com.pplus.luckybol.databinding.CommonItemTwoLineBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.util.*

/**
 * Created by ksh on 2016-09-28.
 */
class NoticeAdapter() : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {
    private var mDataList: ArrayList<Notice>
    private var listener: OnItemClickListener? = null

    init {
        mDataList = ArrayList()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItem(postion: Int): Notice {
        return mDataList[postion]
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun addAll(channels: List<Notice>?) {
        mDataList.addAll(channels!!)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommonItemTwoLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList[position]

        holder.text_subject.text = item.subject // 제목
        val date = DateFormatUtils.convertDateFormat(item.duration!!.start, "yyyy.MM.dd")
        holder.text_date.text = date // 날짜 변환

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class ViewHolder(binding: CommonItemTwoLineBinding) : RecyclerView.ViewHolder(binding.root) {
        var text_subject = binding.textSubject
        var text_date = binding.textDate

        init {
            text_subject.setSingleLine()
        }
    }
}