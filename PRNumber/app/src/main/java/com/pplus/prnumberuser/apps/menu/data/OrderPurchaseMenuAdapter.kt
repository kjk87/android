package com.pplus.prnumberuser.apps.menu.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchaseMenu
import com.pplus.prnumberuser.databinding.ItemPurchaseCartBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class OrderPurchaseMenuAdapter() : RecyclerView.Adapter<OrderPurchaseMenuAdapter.ViewHolder>() {

    var mDataList: MutableList<OrderPurchaseMenu>? = null
    var listener: OnItemListener? = null


    interface OnItemListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemListener(listener: OnItemListener) {

        this.listener = listener
    }

    fun getItem(position: Int): OrderPurchaseMenu {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<OrderPurchaseMenu>? {

        return mDataList
    }

    fun add(data: OrderPurchaseMenu) {

        if (mDataList == null) {
            mDataList = ArrayList<OrderPurchaseMenu>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<OrderPurchaseMenu>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<OrderPurchaseMenu>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: OrderPurchaseMenu) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<OrderPurchaseMenu>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<OrderPurchaseMenu>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPurchaseCartBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_title = binding.textPurchaseCartTitle
        val text_option = binding.textPurchaseCartOption
        val text_price = binding.textPurchaseCartPrice

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_title.text = "${item.orderMenu!!.title} x ${item.amount}"

        var option = ""
        var optionPrice = 0
        for ((i, orderPurchaseMenuOption) in item.orderPurchaseMenuOptionList!!.withIndex()) {

            if (i != 0) {
                option += ", "
            }

            option += orderPurchaseMenuOption.title
            if (orderPurchaseMenuOption.type == 1) {
                option += "(${holder.itemView.context.getString(R.string.word_essential)})"
            } else {
                option += "(${holder.itemView.context.getString(R.string.word_add)})"
            }

            if (orderPurchaseMenuOption.price == null) {
                orderPurchaseMenuOption.price = 0f
            }

            optionPrice += orderPurchaseMenuOption.price!!.toInt()
        }

        holder.text_option.text = option

        val price = item.orderMenu!!.price!!.toInt() + optionPrice
        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((price * item.amount!!).toString()))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPurchaseCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}