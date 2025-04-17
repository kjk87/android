package com.pplus.luckybol.apps.event.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.databinding.ItemAttendanceBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class AttendanceAdapter() : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    var mDataList: MutableList<Int>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): Int {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Int>? {

        return mDataList
    }

    fun add(data: Int) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Int>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Int) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Int>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Int>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageAttendance

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.image.setImageResource(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}