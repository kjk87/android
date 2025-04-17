package com.pplus.luckybol.apps.event.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.EventReply
import com.pplus.luckybol.databinding.ItemEventReplyBinding
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

    class ViewHolder(binding: ItemEventReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageEventReplyProfile
        val text_name = binding.textEventReplyName
        val text_reply = binding.textEventReply
        val text_reg_date = binding.textEventReplyRegDate
        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.member != null){
            holder.text_name.text = item.member!!.nickname
            if(item.member!!.profileAttachment != null){
                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
            }else{
                holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
            }
        }

        holder.text_reply.text = item.reply
        holder.text_reg_date.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, item.regDatetime)

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}