package com.root37.buflexz.apps.partner.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.HistoryCommission
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemHistoryCommissionBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class HistoryCommissionAdapter() : RecyclerView.Adapter<HistoryCommissionAdapter.ViewHolder>() {

    var mDataList: MutableList<HistoryCommission>? = null
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

    fun getItem(position: Int): HistoryCommission {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<HistoryCommission>? {

        return mDataList
    }

    fun add(data: HistoryCommission) {

        if (mDataList == null) {
            mDataList = ArrayList<HistoryCommission>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<HistoryCommission>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<HistoryCommission>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: HistoryCommission) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<HistoryCommission>()
    }

    fun setDataList(dataList: MutableList<HistoryCommission>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemHistoryCommissionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        val date = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))
        holder.binding.textHistoryCommissionDate.text = date
        when (item.type) {
            "ad" -> {
                holder.binding.textHistoryCommissionType.setText(R.string.word_commission_ad_type)
            }

            "ball" -> {
                holder.binding.textHistoryCommissionType.setText(R.string.word_commission_ball_type)
            }

            "bonus" -> {
                holder.binding.textHistoryCommissionType.setText(R.string.word_commission_bonus_type)
            }
        }
        holder.binding.textHistoryCommission.text = holder.itemView.context.getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(item.commission.toString()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryCommissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}