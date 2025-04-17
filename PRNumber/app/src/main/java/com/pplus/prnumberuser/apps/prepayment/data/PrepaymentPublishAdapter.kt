package com.pplus.prnumberuser.apps.prepayment.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.PrepaymentPublish
import com.pplus.prnumberuser.databinding.ItemPrepaymentPublishBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PrepaymentPublishAdapter: RecyclerView.Adapter<PrepaymentPublishAdapter.ViewHolder>() {

    var mDataList: MutableList<PrepaymentPublish>? = null
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

    fun getItem(position: Int): PrepaymentPublish {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PrepaymentPublish>? {

        return mDataList
    }

    fun add(data: PrepaymentPublish) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PrepaymentPublish>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PrepaymentPublish) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<PrepaymentPublish>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PrepaymentPublish>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPrepaymentPublishBinding) : RecyclerView.ViewHolder(binding.root) {


        val layout = binding.layoutPrepaymentPublish
        val text_total_price = binding.textPrepaymentPublishTotalPrice
        val text_status = binding.textPrepaymentPublishStatus
        val text_have_price = binding.textPrepaymentPublishHavePrice
        val text_expire_date = binding.textPrepaymentExpireDate


        init {
            layout.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.width_750)
            (layout.layoutParams as RelativeLayout.LayoutParams).marginEnd = itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]


        holder.text_total_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.totalPrice!!.toInt().toString()))
        holder.text_have_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.havePrice!!.toInt().toString()))
        holder.text_expire_date.text = "${holder.itemView.context.getString(R.string.word_expire_date2)} ${item.expireDate}"

        when(item.status){
            "normal"->{
                holder.text_status.setText(R.string.word_using)
                holder.layout.setBackgroundResource(R.drawable.bg_4694fb_radius_30)
            }
            "completed"->{
                holder.text_status.setText(R.string.word_use_complete)
                holder.layout.setBackgroundResource(R.drawable.bg_8c969f_radius_30)
            }
            "expired"->{
                holder.text_status.setText(R.string.word_expire)
                holder.layout.setBackgroundResource(R.drawable.bg_8c969f_radius_30)
            }
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition, it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPrepaymentPublishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}