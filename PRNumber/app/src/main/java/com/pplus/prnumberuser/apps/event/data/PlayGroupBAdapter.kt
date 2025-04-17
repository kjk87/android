package com.pplus.prnumberuser.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.databinding.ItemPlayGroupBBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PlayGroupBAdapter : RecyclerView.Adapter<PlayGroupBAdapter.ViewHolder> {

    var mDataList: MutableList<Event>? = null
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

    fun getItem(position: Int): Event {

        return mDataList!!.get(position)
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
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
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

    class ViewHolder(binding:ItemPlayGroupBBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_banner = binding.imagePlayGroupBBanner
        val text_winner = binding.textPlayGroupBWinner

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_banner)

        holder.text_winner.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventImpressionActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }

        if (item.winnerCount!! > 0) {
            holder.text_winner.visibility = View.VISIBLE
        } else {
            holder.text_winner.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayGroupBBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}