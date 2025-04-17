package com.pplus.prnumberuser.apps.menu.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.databinding.ItemPurchaseCartBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PurchaseCartAdapter() : RecyclerView.Adapter<PurchaseCartAdapter.ViewHolder>() {

    var mDataList: MutableList<Cart>? = null
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

    fun getItem(position: Int): Cart {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Cart>? {

        return mDataList
    }

    fun add(data: Cart) {

        if (mDataList == null) {
            mDataList = ArrayList<Cart>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Cart>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Cart>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Cart) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Cart>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Cart>) {

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
        for ((i, cartOption) in item.cartOptionList!!.withIndex()) {

            if (i != 0) {
                option += ", "
            }

            option += cartOption.menuOptionDetail!!.title
            if (cartOption.type == 1) {
                option += "(${holder.itemView.context.getString(R.string.word_essential)})"
            } else {
                option += "(${holder.itemView.context.getString(R.string.word_add)})"
            }

            if (cartOption.menuOptionDetail!!.price == null) {
                cartOption.menuOptionDetail!!.price = 0f
            }

            optionPrice += cartOption.menuOptionDetail!!.price!!.toInt()
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