package com.pplus.prnumberuser.apps.main.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Prepayment
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMainPrepaymentBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPrepaymentAdapter: RecyclerView.Adapter<MainPrepaymentAdapter.ViewHolder>() {

    var mDataList: MutableList<Prepayment>? = null
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

    fun getItem(position: Int): Prepayment {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Prepayment>? {

        return mDataList
    }

    fun add(data: Prepayment) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Prepayment>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Prepayment) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Prepayment>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Prepayment>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainPrepaymentBinding) : RecyclerView.ViewHolder(binding.root) {


        val layout = binding.layoutMainPrepayment
        val layout_end = binding.layoutMainPrepaymentEnd
        val text_price = binding.textMainPrepaymentPrice
        val text_total_price = binding.textMainPrepaymentTotalPrice
        val text_discount = binding.textMainPrepaymentDiscount


        init {
            layout.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.width_750)
            (layout.layoutParams as RelativeLayout.LayoutParams).marginEnd = itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
            text_price.paintFlags = text_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]


        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toInt().toString()))
        holder.text_total_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((item.price!! + item.addPrice!!).toInt().toString()))

        //        holder.text_discount.visibility = View.GONE
        if (item.discount != null && item.discount!!.toInt() > 0) {
            holder.text_discount.visibility = View.VISIBLE
            holder.text_discount.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.format_add_price_discount, item.discount!!.toInt().toString()))
        } else {
            holder.text_discount.visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition, it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainPrepaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}