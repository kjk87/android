package com.lejel.wowbox.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.core.network.model.dto.PointMallCategory
import com.lejel.wowbox.databinding.ItemMainPointMallBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPointMallAdapter() : RecyclerView.Adapter<MainPointMallAdapter.ViewHolder>() {

    var mDataList: MutableList<PointMallCategory>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): PointMallCategory {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PointMallCategory>? {

        return mDataList
    }

    fun add(data: PointMallCategory) {

        if (mDataList == null) {
            mDataList = ArrayList<PointMallCategory>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PointMallCategory>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<PointMallCategory>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PointMallCategory) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<PointMallCategory>()
    }

    fun setDataList(dataList: MutableList<PointMallCategory>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemMainPointMallBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.binding.imageMainPointMall)
        holder.binding.textMainPointMallTitle.text = item.title

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainPointMallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}