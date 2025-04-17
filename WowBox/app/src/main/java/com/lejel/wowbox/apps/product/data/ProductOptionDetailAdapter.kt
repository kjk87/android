package com.lejel.wowbox.apps.product.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.ProductOptionDetail
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.lejel.wowbox.databinding.ItemProductOptionItemBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductOptionDetailAdapter(var optionType: String? = null) : RecyclerView.Adapter<ProductOptionDetailAdapter.ViewHolder>() {

    var mContext: Context? = null
    var mDataList: MutableList<ProductOptionDetail>? = null
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

    fun getItem(position: Int): ProductOptionDetail {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductOptionDetail>? {

        return mDataList
    }

    fun add(data: ProductOptionDetail) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductOptionDetail>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductOptionDetail) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductOptionDetail>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProductOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textProductOptionItemName.setSingleLine()
            binding.textProductOptionItemCont.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if (item.item2 != null) {
            holder.binding.textProductOptionItemName.text = item.item2!!.item
        } else {
            holder.binding.textProductOptionItemName.text = item.item1!!.item
        }

        if(optionType == "union"){
            val remainCount = item.amount!! - item.soldCount!!

            if (remainCount > 0) {
                holder.binding.textProductOptionItemCont.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_fc5c57))
                holder.binding.textProductOptionItemName.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.binding.textProductOptionItemPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.binding.textProductOptionItemCont.text = holder.itemView.context.getString(R.string.format_count_unit, remainCount.toString())

                holder.itemView.isEnabled = true
            } else {
                holder.binding.textProductOptionItemCont.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.binding.textProductOptionItemName.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.binding.textProductOptionItemPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.binding.textProductOptionItemCont.setText(R.string.word_sold_out)
                holder.itemView.isEnabled = false
            }

            if (item.price!! > 0) {
                holder.binding.textProductOptionItemPrice.visibility = View.VISIBLE
                holder.binding.textProductOptionItemPrice.text = "+${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))}"
            } else {
                holder.binding.textProductOptionItemPrice.visibility = View.GONE
            }
        }else{
            holder.binding.textProductOptionItemName.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.binding.textProductOptionItemCont.visibility = View.GONE
            holder.binding.textProductOptionItemPrice.visibility = View.GONE
            holder.itemView.isEnabled = true
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}