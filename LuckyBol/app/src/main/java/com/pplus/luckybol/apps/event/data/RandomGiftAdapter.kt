package com.pplus.luckybol.apps.event.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ItemGiftBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class RandomGiftAdapter : RecyclerView.Adapter<RandomGiftAdapter.ViewHolder> {

    var mDataList: MutableList<EventGift>? = null
    var listener: OnItemClickListener? = null
    var mEvent: Event? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventGift {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<EventGift>? {

        return mDataList
    }

    fun add(data: EventGift) {

        if (mDataList == null) {
            mDataList = ArrayList<EventGift>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventGift>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventGift>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventGift) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<EventGift>()

    }

    fun setDataList(dataList: MutableList<EventGift>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageGift
        val layout_url = binding.layoutGiftUrl
        val text_name = binding.textGiftName
        val text_price = binding.textGiftPrice
        val text_reward_play = binding.textGiftRewardPlay
        val view_left_bar = binding.viewGiftLeftBar
        val view_right_bar = binding.viewGiftRightBar

        init {
            binding.layoutGiftWinner.visibility = View.GONE
            text_price.paintFlags = text_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!!.get(position)

        if (position % 2 == 0) {
            holder.view_left_bar.visibility = View.GONE
            holder.view_right_bar.visibility = View.VISIBLE
        } else {
            holder.view_left_bar.visibility = View.VISIBLE
            holder.view_right_bar.visibility = View.GONE
        }

        Glide.with(holder.itemView.context).load(item.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        holder.text_name.text = item.title
        holder.text_price.text = holder.itemView.context.getString(R.string.format_normal_price, FormatUtil.getMoneyType(item.price.toString()))
        if (mEvent != null) {
            holder.text_reward_play.text = holder.itemView.context.getString(R.string.format_join_price, FormatUtil.getMoneyTypeFloat(mEvent!!.rewardPlay.toString()))
        }


        if (StringUtils.isNotEmpty(item.giftLink)) {
            holder.layout_url.visibility = View.VISIBLE
            holder.layout_url.setOnClickListener {
                PplusCommonUtil.openChromeWebView(holder.itemView.context, item.giftLink!!)
            }
        } else {
            holder.layout_url.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}