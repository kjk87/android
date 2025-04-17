package com.lejel.wowbox.apps.giftcard.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.core.network.model.dto.GiftCardBrand
import com.lejel.wowbox.databinding.ItemGiftCardBrandBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class GiftCardBrandAdapter() : RecyclerView.Adapter<GiftCardBrandAdapter.ViewHolder>() {

    var mDataList: MutableList<GiftCardBrand>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): GiftCardBrand {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<GiftCardBrand>? {

        return mDataList
    }

    fun add(data: GiftCardBrand) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GiftCardBrand>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GiftCardBrand) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<GiftCardBrand>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GiftCardBrand>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemGiftCardBrandBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.delegateImage).apply(RequestOptions().centerCrop()).into(holder.binding.imageGiftCardBrand)
        holder.binding.textGiftCardBrand.text = item.title


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftCardBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}