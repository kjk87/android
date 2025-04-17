package com.pplus.prnumberuser.apps.product.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.ProductNotice
import com.pplus.prnumberuser.databinding.ItemProductNoticeBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductNoticeAdapter : RecyclerView.Adapter<ProductNoticeAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<ProductNotice>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): ProductNotice {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductNotice>? {

        return mDataList
    }

    fun add(data: ProductNotice) {

        if (mDataList == null) {
            mDataList = ArrayList<ProductNotice>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductNotice>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProductNotice>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductNotice) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductNotice>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductNotice>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemProductNoticeBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_title = binding.textProductNoticeTitle
        val text_note = binding.textProductNoticeNote


        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        holder.text_title.text = item.title
        holder.text_note.text = item.note

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}