//package com.pplus.prnumberuser.apps.search.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.prnumberuser.R
//import kotlinx.android.synthetic.main.item_recommend_number.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class RecommendNumberAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<RecommendNumberAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<String>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        mDataList!!.add(context.getString(R.string.word_pr_number))
//        mDataList!!.add(context.getString(R.string.word_friend))
//        mDataList!!.add(context.getString(R.string.word_personal_pro))
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): String {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<String>? {
//
//        return mDataList
//    }
//
//    fun add(data: String) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<String>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<String>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<String>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: String) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<String>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<String>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val text_number = itemView.text_recommend_number
//
//        init {
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//        holder.text_number.text = item
//        when (position) {
//            0 -> {
//                holder.text_number.setTextColor(ResourceUtil.getColor(mContext, R.color.color_579ffb))
//                holder.text_number.setBackgroundResource(R.drawable.underbar_579ffb_ffffff)
//            }
//            1 -> {//남성의류
//                holder.text_number.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff7979))
//                holder.text_number.setBackgroundResource(R.drawable.underbar_ff7979_ffffff)
//            }
//            2 -> {//신발
//                holder.text_number.setTextColor(ResourceUtil.getColor(mContext, R.color.color_d674ff))
//                holder.text_number.setBackgroundResource(R.drawable.underbar_d674ff_ffffff)
//            }
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition)
//            }
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_number, parent, false)
//        return ViewHolder(v)
//    }
//}