package com.pplus.prnumberuser.apps.search.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
import com.pplus.prnumberuser.databinding.ItemCategoryLocationAroundBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CategoryLocationAroundAdapter : RecyclerView.Adapter<CategoryLocationAroundAdapter.ViewHolder> {

    var mDataList: MutableList<CategoryMajor>? = null
    var listener: OnItemClickListener? = null
    var mSelectData:CategoryMajor? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = arrayListOf()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): CategoryMajor {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<CategoryMajor>? {

        return mDataList
    }

    fun add(data: CategoryMajor) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<CategoryMajor>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: CategoryMajor) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<CategoryMajor>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemCategoryLocationAroundBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textCategoryLocationAround

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if(mSelectData == null){
            mSelectData = mDataList!![0]
        }

        holder.text_name.text = item.name

        holder.itemView.isSelected = mSelectData!!.seqNo == item.seqNo

        holder.itemView.setOnClickListener {
            mSelectData = item
            notifyDataSetChanged()
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryLocationAroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}