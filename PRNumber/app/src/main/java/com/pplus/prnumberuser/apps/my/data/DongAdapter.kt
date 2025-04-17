package com.pplus.prnumberuser.apps.my.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.apps.my.ui.SelectActiveAreaActivity
import com.pplus.prnumberuser.core.network.model.dto.Juso
import com.pplus.prnumberuser.databinding.ItemDongBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class DongAdapter() : RecyclerView.Adapter<DongAdapter.ViewHolder>() {

    var mDataList: MutableList<Juso>? = null
    var listener: OnItemClickListener? = null
    var mSelectData : Juso? = null
    var mGuCode = ""


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Juso {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Juso>? {

        return mDataList
    }

    fun add(data: Juso) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Juso>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Juso) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Juso>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemDongBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textDongName
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.name

        holder.text_name.isSelected = mSelectData?.value == item.value

        holder.itemView.setOnClickListener {
            mSelectData = item
            if(holder.itemView.context is SelectActiveAreaActivity){
                (holder.itemView.context as SelectActiveAreaActivity).mSelectGuCode = mGuCode
                (holder.itemView.context as SelectActiveAreaActivity).mSelectData = item
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}