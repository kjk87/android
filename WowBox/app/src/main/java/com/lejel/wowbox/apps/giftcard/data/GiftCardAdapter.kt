package com.lejel.wowbox.apps.giftcard.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.GiftCard
import com.lejel.wowbox.databinding.ItemGiftCardBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class GiftCardAdapter() : RecyclerView.Adapter<GiftCardAdapter.ViewHolder>() {

    var mDataList: MutableList<GiftCard>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): GiftCard {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<GiftCard>? {

        return mDataList
    }

    fun add(data: GiftCard) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GiftCard>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GiftCard) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<GiftCard>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GiftCard>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemGiftCardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.binding.imageGiftCard)
        holder.binding.textGiftCardTitle.text = item.title
        holder.binding.textGiftCardPrice.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat((item.price!!*1000).toString()))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}