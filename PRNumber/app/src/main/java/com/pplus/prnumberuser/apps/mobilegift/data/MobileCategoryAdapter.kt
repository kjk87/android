package com.pplus.prnumberuser.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.MobileCategory
import com.pplus.prnumberuser.databinding.ItemMobileCategoryBinding
import com.pplus.utils.part.logs.LogUtil
import java.util.*


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
        return mDataList!!.size+1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position < mDataList!!.size){
            val item = mDataList!![position]

            Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
            holder.text.text = item.name
        }else{
            holder.image.setImageResource(R.drawable.ic_cash_exchange)
            holder.text.text = holder.itemView.context.getString(R.string.word_cash_exchange)
        }


        holder.itemView.setOnClickListener {

            LogUtil.e("MobileCategoryAdapter", "click")
            listener?.onItemClick(holder.adapterPosition, it)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}