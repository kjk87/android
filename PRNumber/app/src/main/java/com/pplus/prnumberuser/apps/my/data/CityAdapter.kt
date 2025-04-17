package com.pplus.prnumberuser.apps.my.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.Province
import com.pplus.prnumberuser.databinding.ItemCityBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CityAdapter() : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    var mDataList: MutableList<Province>? = null
    var listener: OnItemClickListener? = null
    var mSelectData : Province? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Province {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Province>? {

        return mDataList
    }

    fun add(data: Province) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Province>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Province) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Province>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textCityName

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.itemView.isSelected = item.id == mSelectData?.id

        holder.text_name.text = item.dosubname

        holder.itemView.setOnClickListener {
            mSelectData = item
            listener?.onItemClick(holder.adapterPosition)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}