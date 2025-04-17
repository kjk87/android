package com.lejel.wowbox.apps.benefit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.BenefitCalculate
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemBenefitCalculateBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class BenefitCalculateAdapter() : RecyclerView.Adapter<BenefitCalculateAdapter.ViewHolder>() {

    var mDataList: MutableList<BenefitCalculate>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): BenefitCalculate {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BenefitCalculate>? {

        return mDataList
    }

    fun add(data: BenefitCalculate) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BenefitCalculate>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BenefitCalculate) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BenefitCalculate>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BenefitCalculate>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBenefitCalculateBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if(position % 2 == 0){
            holder.itemView.setBackgroundResource(R.color.color_2d2d2d)
        }else{
            holder.itemView.setBackgroundResource(android.R.color.transparent)
        }

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format))
        holder.binding.textBenefitCalculateDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(SimpleDateFormat("yyyy-MM-dd").parse(item.calculateMonth)))
        holder.binding.textBenefitCalculateBenefit.text = holder.itemView.context.getString(R.string.format_dollar_unit2, FormatUtil.getMoneyTypeFloat(item.benefit.toString()))

        when(item.status){//pending: 정산대기 transfer: 이월 complete: 정산완료
            "pending"->{
                holder.binding.textBenefitCalculateStatus.setText(R.string.word_profit_pending)
            }
            "transfer"->{
                holder.binding.textBenefitCalculateStatus.setText(R.string.word_profit_transfer)
            }
            "complete"->{
                holder.binding.textBenefitCalculateStatus.setText(R.string.word_profit_pending)
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBenefitCalculateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}