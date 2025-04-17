package com.lejel.wowbox.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGift
import com.lejel.wowbox.databinding.ItemMainLuckyDrawGiftBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MainLuckyDrawGiftAdapter() : RecyclerView.Adapter<MainLuckyDrawGiftAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawGift>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyDrawGift {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawGift>? {

        return mDataList
    }

    fun add(data: LuckyDrawGift) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawGift>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawGift>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawGift>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawGift) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawGift>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawGift>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemMainLuckyDrawGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        if (item.seqNo == -1L) {
            holder.binding.layoutMainLuckyDrawGift.visibility = View.GONE
            holder.binding.layoutMainLuckyDrawImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions()).into(holder.binding.imageMainLuckyDrawBanner)
        } else {
            holder.binding.layoutMainLuckyDrawGift.visibility = View.VISIBLE
            holder.binding.layoutMainLuckyDrawImage.visibility = View.GONE
            Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.binding.imageMainLuckyDrawGift)
            holder.binding.textMainLuckyDrawGiftTitle.text = item.title
            holder.binding.textMainLuckyDrawGiftPrice.text = FormatUtil.getMoneyTypeFloat(item.price.toString())
            holder.binding.textMainLuckyDrawGiftCount.text = item.amount.toString()

            when (item.type) { //goods, coin, giftCard, ball, point, buffCoin
                "goods", "coin", "giftCard" -> {
                    holder.binding.textMainLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
                }

                "ball" -> {
                    holder.binding.textMainLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
                }

                "point" -> {
                    holder.binding.textMainLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
                }
            }

        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainLuckyDrawGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}