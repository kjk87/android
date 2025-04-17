package com.lejel.wowbox.apps.luckybox.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.databinding.ItemLuckyBoxNotOpenItemBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxNotOpenItemAdapter() : RecyclerView.Adapter<LuckyBoxNotOpenItemAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxPurchaseItem>? = null
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

    fun getItem(position: Int): LuckyBoxPurchaseItem {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyBoxPurchaseItem>? {

        return mDataList
    }

    fun add(data: LuckyBoxPurchaseItem) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyBoxPurchaseItem>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyBoxPurchaseItem) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<LuckyBoxPurchaseItem>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LuckyBoxPurchaseItem>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLuckyBoxNotOpenItemBinding) : RecyclerView.ViewHolder(binding.root) {


        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxNotOpenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}