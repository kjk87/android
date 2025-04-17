package com.pplus.prnumberuser.apps.product.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMainSubscriptionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class StoreSubscriptionAdapter: RecyclerView.Adapter<StoreSubscriptionAdapter.ViewHolder>() {

    var mDataList: MutableList<ProductPrice>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): ProductPrice {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductPrice>? {

        return mDataList
    }

    fun add(data: ProductPrice) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductPrice>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductPrice) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductPrice>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductPrice>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainSubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {


        val layout = binding.layoutMainSubscription
        val layout_end = binding.layoutMainSubscriptionEnd
        val text_product_name = binding.textMainSubscriptionName
        val text_discount_ratio = binding.textMainSubscriptionDiscountRatio
        val text_sale_price = binding.textMainSubscriptionSalePrice
        val text_origin_price = binding.textMainSubscriptionOriginPrice
        val text_desc = binding.textMainSubscriptionDesc


        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text_product_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.isSubscription != null && item.isSubscription!!){
            holder.layout_end.setBackgroundResource(R.drawable.bg_4694fb_right_radius_30)
            holder.text_discount_ratio.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.text_desc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_999999))
            holder.text_desc.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_subscription_count, item.times.toString()))
        }else{
            holder.layout_end.setBackgroundResource(R.drawable.bg_ffcf5c_right_radius_30)
            holder.text_discount_ratio.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
            holder.text_desc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
            holder.text_desc.text = holder.itemView.context.getString(R.string.word_remain_money_manage_type)
        }

        holder.text_product_name.text = item.product!!.name
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            holder.text_discount_ratio.visibility = View.VISIBLE
            holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit2, item.discountRatio!!.toInt().toString()))
        } else {
            holder.text_discount_ratio.visibility = View.GONE
        }
        holder.text_sale_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainSubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}