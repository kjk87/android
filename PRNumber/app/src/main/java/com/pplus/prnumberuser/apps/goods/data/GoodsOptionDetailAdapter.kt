//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.GoodsOptionDetail
//import kotlinx.android.synthetic.main.item_goods_option_item.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsOptionDetailAdapter : RecyclerView.Adapter<GoodsOptionDetailAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<GoodsOptionDetail>? = null
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
//    fun getItem(position: Int): GoodsOptionDetail {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsOptionDetail>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsOptionDetail) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsOptionDetail>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsOptionDetail) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsOptionDetail>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val text_name = itemView.text_goods_option_item_name
//        val text_count = itemView.text_goods_option_item_cont
//        val text_price = itemView.text_goods_option_item_price
//
//
//        init {
//            text_name.setSingleLine()
//            text_count.visibility = View.VISIBLE
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//        if(item.item3 != null){
//            holder.text_name.text = item.item3!!.item
//        }else if(item.item2 != null){
//            holder.text_name.text = item.item2!!.item
//        }else{
//            holder.text_name.text = item.item1!!.item
//        }
//
//        val remainCount = item.amount!! - item.soldCount!!
//
//        if(remainCount > 0){
//            holder.text_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
//            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
//            holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
//            holder.text_count.text = holder.itemView.context.getString(R.string.format_count_unit, remainCount.toString())
//
//            holder.itemView.isEnabled = true
//        }else{
//            holder.text_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            holder.text_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            holder.text_count.setText(R.string.word_sold_out2)
//            holder.itemView.isEnabled = false
//        }
//
//        if(item.price!! > 0){
//            holder.text_price.visibility = View.VISIBLE
//            holder.text_price.text = "+${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))}"
//        }else{
//            holder.text_price.visibility = View.GONE
//        }
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_option_item, parent, false)
//        return ViewHolder(v)
//    }
//}