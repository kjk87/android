//package com.pplus.prnumberuser.apps.theme.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.ThemeCategory
//import kotlinx.android.synthetic.main.item_theme.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ThemeAdapter : RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<ThemeCategory>? = null
//    var listener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
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
//    fun getItem(position: Int): ThemeCategory {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<ThemeCategory>? {
//
//        return mDataList
//    }
//
//    fun add(data: ThemeCategory) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<ThemeCategory>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<ThemeCategory>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<ThemeCategory>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: ThemeCategory) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<ThemeCategory>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<ThemeCategory>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_theme
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
//
//        val item = mDataList!![position]
//
//        Glide.with(mContext!!).load(item.icon).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_main_hotplace_default).error(R.drawable.img_main_hotplace_default)).into(holder.image)
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition, it)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
//        return ViewHolder(v)
//    }
//}