package com.root37.buflexz.apps.product.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.databinding.ItemProductBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var mDataList: MutableList<Product>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): Product {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Product>? {

        return mDataList
    }

    fun add(data: Product) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Product>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Product) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Product>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Product>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        if (item.imageList != null && item.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.imageList!![0].image).apply(RequestOptions().centerCrop()).into(holder.binding.imageProduct)
        } else {
            holder.binding.imageProduct.setBackgroundResource(0)
        }

        holder.binding.textProductTitle.text = item.title
        holder.binding.textProductPrice.text = FormatUtil.getMoneyTypeFloat(item.price.toString())

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}