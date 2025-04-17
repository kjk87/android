package com.lejel.wowbox.apps.inquire.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.core.network.model.dto.InquireImage
import com.lejel.wowbox.databinding.ItemInquireImageBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class InquireImageAdapter() : RecyclerView.Adapter<InquireImageAdapter.ViewHolder>() {

    var mDataList: MutableList<InquireImage>? = null
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

    fun getItem(position: Int): InquireImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<InquireImage>? {

        return mDataList
    }

    fun add(data: InquireImage) {

        if (mDataList == null) {
            mDataList = ArrayList<InquireImage>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun insertAttach(index: String, data: InquireImage) {

        mDataList!![index.toInt()] = data
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<InquireImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<InquireImage>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: InquireImage) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<InquireImage>()
    }

    fun setDataList(dataList: MutableList<InquireImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemInquireImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.binding.imageInquireImage)

        holder.binding.imageInquireImageDel.setOnClickListener {
            mDataList!!.removeAt(holder.absoluteAdapterPosition)
            notifyItemRemoved(holder.absoluteAdapterPosition)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInquireImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}