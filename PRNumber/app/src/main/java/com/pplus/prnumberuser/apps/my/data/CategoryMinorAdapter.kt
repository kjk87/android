package com.pplus.prnumberuser.apps.my.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.databinding.ItemCategoryMinorBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CategoryMinorAdapter(isMe: Boolean) : RecyclerView.Adapter<CategoryMinorAdapter.ViewHolder>() {

    var mDataList: MutableList<CategoryMinor>? = null
    var listener: OnItemClickListener? = null
    var mMyCategoryList: MutableList<CategoryMinor>? = null
    var mIsMe = isMe


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): CategoryMinor {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<CategoryMinor>? {

        return mDataList
    }

    fun add(data: CategoryMinor) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<CategoryMinor>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: CategoryMinor) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<CategoryMinor>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemCategoryMinorBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_value= binding.textCategoryMinor

        init {
            text_value.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.text_value.text = "#${item.name}"

        holder.itemView.isSelected = ((mMyCategoryList != null && mMyCategoryList!!.contains(item)) || mIsMe)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryMinorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}