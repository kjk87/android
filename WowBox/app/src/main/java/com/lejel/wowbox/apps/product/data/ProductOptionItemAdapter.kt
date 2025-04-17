package com.lejel.wowbox.apps.product.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.core.network.model.dto.ProductOptionItem
import com.lejel.wowbox.databinding.ItemProductOptionItemBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductOptionItemAdapter : RecyclerView.Adapter<ProductOptionItemAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<ProductOptionItem>? = null
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

    fun getItem(position: Int): ProductOptionItem {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductOptionItem>? {

        return mDataList
    }

    fun add(data: ProductOptionItem) {

        if (mDataList == null) {
            mDataList = ArrayList<ProductOptionItem>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductOptionItem>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProductOptionItem>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductOptionItem) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductOptionItem>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductOptionItem>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProductOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textProductOptionItemName.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        holder.binding.textProductOptionItemName.text = item.item
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}