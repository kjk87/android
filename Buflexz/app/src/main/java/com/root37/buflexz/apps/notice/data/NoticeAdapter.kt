package com.root37.buflexz.apps.notice.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.Notice
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemNoticeBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class NoticeAdapter() : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    var mDataList: MutableList<Notice>? = null
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

    fun getItem(position: Int): Notice {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Notice>? {

        return mDataList
    }

    fun add(data: Notice) {

        if (mDataList == null) {
            mDataList = ArrayList<Notice>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Notice>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Notice>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Notice) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Notice>()
    }

    fun setDataList(dataList: MutableList<Notice>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textNoticeTitle.text = item.title

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textNoticeDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}