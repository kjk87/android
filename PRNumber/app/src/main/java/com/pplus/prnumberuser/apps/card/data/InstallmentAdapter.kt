package com.pplus.prnumberuser.apps.card.data

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.databinding.ItemInstallmentBinding
import java.util.*

/**
 * Created by imac on 2018. 1. 8..
 */
class InstallmentAdapter : RecyclerView.Adapter<InstallmentAdapter.ViewHolder> {

    var mDataList: MutableList<String>? = null
    var listener: OnItemClickListener? = null
    var mTodayYear: Int = 0
    var mTodayMonth: Int = 0
    var mTodayDay: Int = 0
    var mSelectData = "00"

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = arrayListOf("00", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): String {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<String>? {

        return mDataList
    }

    fun add(data: String) {

        if (mDataList == null) {
            mDataList = ArrayList<String>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<String>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<String>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: String) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<String>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<String>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemInstallmentBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_installment = binding.textInstallment
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if(position == 0){
            holder.text_installment.setText(R.string.word_one_pay)
        }else{
            holder.text_installment.text = holder.itemView.context.getString(R.string.format_installment_period, (position+1).toString())
        }

        if(mSelectData == item){
            holder.itemView.isSelected = true
            holder.text_installment.typeface = Typeface.DEFAULT_BOLD
        }else{
            holder.itemView.isSelected = false
            holder.text_installment.typeface = Typeface.DEFAULT
        }

        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition)
//            }
            mSelectData = item
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInstallmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}