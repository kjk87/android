package com.pplus.prnumberuser.apps.menu.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuImage
import com.pplus.prnumberuser.databinding.ItemMenuImageBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainImageAdapter() : RecyclerView.Adapter<MainImageAdapter.ViewHolder>() {

    var mDataList: MutableList<OrderMenuImage>? = null
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

    fun getItem(position: Int): OrderMenuImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<OrderMenuImage>? {

        return mDataList
    }

    fun add(data: OrderMenuImage) {

        if (mDataList == null) {
            mDataList = ArrayList<OrderMenuImage>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<OrderMenuImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<OrderMenuImage>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: OrderMenuImage) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<OrderMenuImage>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<OrderMenuImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMenuImageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMenu
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop().placeholder(R.drawable.bg_f1f2f4_radius_6).error(R.drawable.bg_f1f2f4_radius_6)).into(holder.image)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}