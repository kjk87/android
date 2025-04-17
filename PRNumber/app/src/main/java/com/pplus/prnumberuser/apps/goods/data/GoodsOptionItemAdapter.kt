//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.GoodsOptionItem
//import kotlinx.android.synthetic.main.item_goods_option_item.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsOptionItemAdapter : RecyclerView.Adapter<GoodsOptionItemAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<GoodsOptionItem>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): GoodsOptionItem {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsOptionItem>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsOptionItem) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<GoodsOptionItem>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsOptionItem>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<GoodsOptionItem>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsOptionItem) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<GoodsOptionItem>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsOptionItem>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val text_name = itemView.text_goods_option_item_name
//
//
//        init {
//            text_name.setSingleLine()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//        holder.text_name.text = item.item
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_option_item, parent, false)
//        return ViewHolder(v)
//    }
//}