package com.pplus.prnumberuser.apps.menu.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.databinding.ItemMenuCartBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by imac on 2018. 1. 8..
 */
class MenuCartAdapter() : RecyclerView.Adapter<MenuCartAdapter.ViewHolder>() {

    var mDataList: MutableList<Cart>? = null
    var listener: OnItemListener? = null


    interface OnItemListener {

        fun onItemClick(position: Int)
        fun changeAmount()
        fun delete(position: Int)
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

    class ViewHolder(binding: ItemMenuCartBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_title = binding.textMenuCartTitle
        val image_del = binding.imageMenuCartDel
        val text_option = binding.textMenuCartOption
        val text_price = binding.textMenuCartPrice
        val image_plus = binding.imageMenuCartPlus
        val image_minus = binding.imageMenuCartMinus
        val text_count = binding.textMenuCartCount

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_title.text = item.orderMenu!!.title

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
        holder.text_count.text = item.amount.toString()
        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((price * item.amount!!).toString()))

        holder.image_plus.setOnClickListener {
            item.amount = item.amount!! + 1

            holder.text_count.text = item.amount.toString()
            holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((price * item.amount!!).toString()))
            listener?.changeAmount()
            updateAmount(item.seqNo!!, item.amount!!)
        }

        holder.image_minus.setOnClickListener {
            if (item.amount!! > 1) {
                item.amount = item.amount!! - 1
            }

            holder.text_count.text = item.amount.toString()
            holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((price * item.amount!!).toString()))
            listener?.changeAmount()
            updateAmount(item.seqNo!!, item.amount!!)
        }

        holder.image_del.setOnClickListener {
            listener?.delete(position)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    private fun updateAmount(cartSeqNo: Long, amount: Int) {
        val params = HashMap<String, String>()
        params["cartSeqNo"] = cartSeqNo.toString()
        params["amount"] = amount.toString()
        ApiBuilder.create().updateAmount(params).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}