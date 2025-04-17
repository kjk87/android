package com.pplus.luckybol.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.MobileBrand
import com.pplus.luckybol.databinding.ItemMobileBrandBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MobileBrandAdapter() : RecyclerView.Adapter<MobileBrandAdapter.ViewHolder>() {

    var mDataList: MutableList<MobileBrand>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): MobileBrand {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<MobileBrand>? {

        return mDataList
    }

    fun add(data: MobileBrand) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<MobileBrand>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: MobileBrand) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<MobileBrand>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<MobileBrand>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMobileBrandBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMobileBrand

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
            listener?.onItemClick(holder.absoluteAdapterPosition, it)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}