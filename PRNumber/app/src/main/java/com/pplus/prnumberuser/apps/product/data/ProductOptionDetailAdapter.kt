package com.pplus.prnumberuser.apps.product.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.model.dto.ProductOptionDetail
import com.pplus.prnumberuser.databinding.ItemGoodsOptionItemBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
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

    class ViewHolder(binding: ItemGoodsOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textGoodsOptionItemName
        val text_count = binding.textGoodsOptionItemCont
        val text_price = binding.textGoodsOptionItemPrice


        init {
            text_name.setSingleLine()
            text_count.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if (item.item2 != null) {
            holder.text_name.text = item.item2!!.item
        } else {
            holder.text_name.text = item.item1!!.item
        }

        if(optionType == EnumData.OptionType.union.name){
            val remainCount = item.amount!! - item.soldCount!!

            if (remainCount > 0) {
                holder.text_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_count.text = holder.itemView.context.getString(R.string.format_count_unit, remainCount.toString())

                holder.itemView.isEnabled = true
            } else {
                holder.text_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_count.setText(R.string.word_sold_out2)
                holder.itemView.isEnabled = false
            }

            if (item.price!! > 0) {
                holder.text_price.visibility = View.VISIBLE
                holder.text_price.text = "+${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))}"
            } else {
                holder.text_price.visibility = View.GONE
            }
        }else{
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.text_count.visibility = View.GONE
            holder.text_price.visibility = View.GONE
            holder.itemView.isEnabled = true
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoodsOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}