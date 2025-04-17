package com.pplus.prnumberuser.apps.category.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.databinding.ItemCategoryBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    var mDataList: MutableList<CategoryMinor>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = arrayListOf()
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
            mDataList = ArrayList<CategoryMinor>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<CategoryMinor>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<CategoryMinor>()
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

        mDataList = ArrayList<CategoryMinor>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<CategoryMinor>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageCategory
        val text_name = binding.textCategoryName

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if(position == 0){
            holder.image.setImageResource(R.drawable.ic_category_minor_total)
        }else{
            Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_category_default).error(R.drawable.img_category_default)).into(holder.image)
        }


        holder.text_name.text = item.name

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}