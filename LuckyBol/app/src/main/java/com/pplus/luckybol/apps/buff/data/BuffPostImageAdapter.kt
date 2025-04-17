package com.pplus.luckybol.apps.buff.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.BuffPostImage
import com.pplus.luckybol.databinding.ItemImageBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffPostImageAdapter() : RecyclerView.Adapter<BuffPostImageAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffPostImage>? = null
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

    fun getItem(position: Int): BuffPostImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffPostImage>? {

        return mDataList
    }

    fun add(data: BuffPostImage) {

        if (mDataList == null) {
            mDataList = ArrayList<BuffPostImage>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffPostImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<BuffPostImage>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffPostImage) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<BuffPostImage>()
    }

    fun setDataList(dataList: MutableList<BuffPostImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.image

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}