package com.lejel.wowbox.apps.withdraw.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ItemWithdrawPointBinding
import com.pplus.utils.part.format.FormatUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class WithdrawPointAdapter() : RecyclerView.Adapter<WithdrawPointAdapter.ViewHolder>() {

    var mSelectData = ""
    var mDataList = arrayListOf("150000", "300000", "500000", "1000000", "2000000", "3000000")
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): String {

        return mDataList[position]
    }

    fun getDataList(): MutableList<String>? {

        return mDataList
    }


    fun clear() {
        notifyItemRangeRemoved(0, mDataList.size)
        mDataList = ArrayList()
    }

    class ViewHolder(val binding: ItemWithdrawPointBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList[position]
        holder.binding.textWithdrawPoint.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyType(item))

        var withdrawPrice = 0
        if (item.toInt() >= 3000000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.20).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-20")
        } else if (item.toInt() >= 2000000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.25).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-25")
        } else if (item.toInt() >= 1000000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.30).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-30")
        } else if (item.toInt() >= 500000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.35).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-35")
        } else if (item.toInt() >= 300000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.4).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-40")
        } else if (item.toInt() >= 150000) {
            withdrawPrice = item.toInt() - (item.toInt() * 0.5).toInt()
            holder.binding.textWithdrawPercent.text = holder.itemView.context.getString(R.string.format_percent_unit, "-50")
        }

        holder.binding.textWithdrawPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(withdrawPrice.toString()))


        holder.itemView.isSelected = (item == mSelectData)
        holder.itemView.setOnClickListener {

            if(item.toInt() > LoginInfoManager.getInstance().member!!.point!!){
                ToastUtil.showAlert(holder.itemView.context, R.string.msg_lack_point)
                return@setOnClickListener
            }

            mSelectData = item
            listener?.onItemClick(holder.absoluteAdapterPosition)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWithdrawPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}