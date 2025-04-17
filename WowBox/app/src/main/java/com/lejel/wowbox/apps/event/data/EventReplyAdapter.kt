package com.lejel.wowbox.apps.event.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.core.network.model.dto.EventReply
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemEventReplyBinding
import com.pplus.utils.part.utils.time.DateFormatUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class EventReplyAdapter() : RecyclerView.Adapter<EventReplyAdapter.ViewHolder>() {

    var mDataList: MutableList<EventReply>? = null
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

    fun getItem(position: Int): EventReply {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventReply>? {

        return mDataList
    }

    fun add(data: EventReply) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventReply>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventReply) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventReply>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventReply>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemEventReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.memberTotal != null){
            holder.binding.textEventReplyName.text = item.memberTotal!!.nickname
            Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageEventReplyProfile)
        }

        holder.binding.textEventReply.text = item.reply
        holder.binding.textEventReplyRegDate.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, item.regDatetime)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}