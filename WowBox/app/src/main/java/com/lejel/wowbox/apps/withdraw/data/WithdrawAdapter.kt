package com.lejel.wowbox.apps.withdraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.withdraw.ui.AlertWithdrawReturnReasonActivity
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemWithdrawBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class WithdrawAdapter() : RecyclerView.Adapter<WithdrawAdapter.ViewHolder>() {

    var mDataList: MutableList<Withdraw>? = null
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

    fun getItem(position: Int): Withdraw {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Withdraw>? {

        return mDataList
    }

    fun add(data: Withdraw) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Withdraw>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Withdraw) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList()
    }

    fun setDataList(dataList: MutableList<Withdraw>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemWithdrawBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))
        holder.binding.textWithdrawRegDate.text = regDate
        holder.binding.textWithdrawAmount.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.withdraw.toString()))
        holder.binding.textWithdrawStatus.setOnClickListener {

        }
        when(item.status){
            "pending"->{
                holder.binding.textWithdrawStatus.setText(R.string.word_withdraw_pending)
                holder.binding.textWithdrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
                holder.binding.textWithdrawStatus.setBackgroundResource(R.drawable.bg_24b5b5b5_radius_20)
            }
            "complete"->{
                holder.binding.textWithdrawStatus.setText(R.string.word_withdraw_complete)
                holder.binding.textWithdrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
                holder.binding.textWithdrawStatus.setBackgroundResource(R.drawable.bg_24b5b5b5_radius_20)
            }
            "return"->{
                holder.binding.textWithdrawStatus.setText(R.string.msg_view_return_reason)
                holder.binding.textWithdrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textWithdrawStatus.setBackgroundResource(R.drawable.bg_ff5e5e_radius_20)
                holder.binding.textWithdrawStatus.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AlertWithdrawReturnReasonActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
        holder.binding.textWithdrawStatus.setPadding(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_16), 0, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_16), 0)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWithdrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}