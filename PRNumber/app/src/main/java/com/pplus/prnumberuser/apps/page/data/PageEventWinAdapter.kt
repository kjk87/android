package com.pplus.prnumberuser.apps.page.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.databinding.ItemPageEventWinBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PageEventWinAdapter() : RecyclerView.Adapter<PageEventWinAdapter.ViewHolder>() {

    var mDataList: MutableList<EventWin>? = null
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

    class ViewHolder(binding: ItemPageEventWinBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imagePageEventWinProfile
        val text_nickname = binding.textPageEventWinNickname

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        if(item.user!!.profileImage != null){
            Glide.with(holder.itemView.context).load(item.user!!.profileImage!!.url).apply(RequestOptions().fitCenter().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image_profile)
        }else{
            holder.image_profile.setImageResource(R.drawable.ic_event_profile_default)
        }
        holder.text_nickname.text = item.user!!.nickname

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPageEventWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}