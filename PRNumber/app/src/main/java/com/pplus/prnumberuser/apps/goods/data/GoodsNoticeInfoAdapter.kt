//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.NoticeInfo
//import kotlinx.android.synthetic.main.item_goods_notice_info.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsNoticeInfoAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsNoticeInfoAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<NoticeInfo>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//        fun onRefresh()
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): NoticeInfo {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<NoticeInfo>? {
//
//        return mDataList
//    }
//
//    fun add(data: NoticeInfo) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<NoticeInfo>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<NoticeInfo>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<NoticeInfo>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: NoticeInfo) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<NoticeInfo>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<NoticeInfo>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val text_key = itemView.text_goods_notice_info_key
//        val text_value = itemView.text_goods_notice_info_value
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//
//        holder.text_key.text = item.key
//        holder.text_value.text = item.value
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_notice_info, parent, false)
//        return ViewHolder(v)
//    }
//}