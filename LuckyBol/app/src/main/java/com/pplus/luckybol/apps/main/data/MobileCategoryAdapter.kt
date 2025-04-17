package com.pplus.luckybol.apps.main.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.MobileCategory
import com.pplus.luckybol.databinding.ItemMobileCategoryBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MobileCategoryAdapter() : RecyclerView.Adapter<MobileCategoryAdapter.ViewHolder>() {

    var mDataList: MutableList<MobileCategory>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): MobileCategory {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<MobileCategory>? {

        return mDataList
    }

    fun add(data: MobileCategory) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<MobileCategory>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: MobileCategory) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<MobileCategory>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<MobileCategory>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMobileCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMobileCategory
        val text = binding.textMobileCategory

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        holder.text.text = item.name

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition, it)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}