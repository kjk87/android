package com.root37.buflexz.apps.product.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.root37.buflexz.core.network.model.dto.ProductImage
import com.root37.buflexz.databinding.ItemBannerBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductImageAdapter() : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    var mDataList: MutableList<ProductImage>? = null
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

    fun getItem(position: Int): ProductImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductImage>? {

        return mDataList
    }

    fun add(data: ProductImage) {

        if (mDataList == null) {
            mDataList = ArrayList<ProductImage>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProductImage>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductImage) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<ProductImage>()
    }

    fun setDataList(dataList: MutableList<ProductImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBanner

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.image)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}