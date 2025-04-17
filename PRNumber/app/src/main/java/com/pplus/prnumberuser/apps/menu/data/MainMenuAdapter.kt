package com.pplus.prnumberuser.apps.menu.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.OrderMenu
import com.pplus.prnumberuser.databinding.ItemMainMenuBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainMenuAdapter() : RecyclerView.Adapter<MainMenuAdapter.ViewHolder>() {

    var mDataList: MutableList<OrderMenu>? = null
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

    fun getItem(position: Int): OrderMenu {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<OrderMenu>? {

        return mDataList
    }

    fun add(data: OrderMenu) {

        if (mDataList == null) {
            mDataList = ArrayList<OrderMenu>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<OrderMenu>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<OrderMenu>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: OrderMenu) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<OrderMenu>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<OrderMenu>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMainMenu
        val text_discount = binding.textMainMenuDiscount
        val text_name = binding.textMainMenuName
        val text_price = binding.textMainMenuPrice
        val text_origin_price = binding.textMainMenuOriginPrice
        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.title

        if (item.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_f1f2f4_radius_6).error(R.drawable.bg_f1f2f4_radius_6)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.bg_f1f2f4_radius_6)
        }

        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toInt().toString()))

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice!!.toInt().toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        if(item.discount != null && item.discount!! > 0){
            holder.text_discount.visibility = View.VISIBLE

            val discountRatio = item.discount!!/item.originPrice!!*100
            holder.text_discount.text = holder.itemView.context.getString(R.string.format_discount, discountRatio.toInt().toString())
        }else{
            holder.text_discount.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}