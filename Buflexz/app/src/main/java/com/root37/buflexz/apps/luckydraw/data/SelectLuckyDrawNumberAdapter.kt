package com.root37.buflexz.apps.luckydraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.core.network.model.dto.LuckyDrawNumber
import com.root37.buflexz.databinding.ItemSelectLuckyDrawNumberBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class SelectLuckyDrawNumberAdapter() : RecyclerView.Adapter<SelectLuckyDrawNumberAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawNumber>? = null
    var listener: OnItemListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemListener {

        fun delete(item:LuckyDrawNumber)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): LuckyDrawNumber {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawNumber>? {

        return mDataList
    }

    fun add(data: LuckyDrawNumber) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawNumber>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawNumber>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawNumber>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawNumber) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawNumber>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawNumber>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSelectLuckyDrawNumberBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        if(StringUtils.isNotEmpty(item.first)){
            holder.binding.textSelectLuckyDrawNumberFirst.visibility = View.VISIBLE
            holder.binding.textSelectLuckyDrawNumberFirst.text = item.first
        }else{
            holder.binding.textSelectLuckyDrawNumberFirst.visibility = View.GONE
        }

        if(StringUtils.isNotEmpty(item.second)){
            holder.binding.textSelectLuckyDrawNumberSecond.visibility = View.VISIBLE
            holder.binding.textSelectLuckyDrawNumberSecond.text = item.second
        }else{
            holder.binding.textSelectLuckyDrawNumberSecond.visibility = View.GONE
        }

        if(StringUtils.isNotEmpty(item.third)){
            holder.binding.textSelectLuckyDrawNumberThird.visibility = View.VISIBLE
            holder.binding.textSelectLuckyDrawNumberThird.text = item.third
        }else{
            holder.binding.textSelectLuckyDrawNumberThird.visibility = View.GONE
        }

        holder.binding.imageSelectLuckyDrawNumberDelete.setOnClickListener {
            mDataList!!.removeAt(holder.absoluteAdapterPosition)
            notifyItemRemoved(holder.absoluteAdapterPosition)
            listener?.delete(item)
        }

        holder.itemView.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectLuckyDrawNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}