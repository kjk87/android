//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import kotlinx.android.synthetic.main.item_filter_cateogry.view.*
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class FilterCategoryAdapter : RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Category>? = null
//    var listener: OnItemClickListener? = null
//    private var mSelectData: Category? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context:Context, category: Category) : super(){
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.mSelectData = category
//    }
//
//    fun setSelectData(position: Int) {
//
//        mSelectData = getItem(position)
//        notifyDataSetChanged()
//    }
//
//    fun getSelectData(): Category {
//
//        return mSelectData!!
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Category {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Category>? {
//
//        return mDataList
//    }
//
//    fun add(data: Category) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Category>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Category>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Category>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Category) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Category>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Category>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val text_name = itemView.text_filter_category_name
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
//
//        holder.text_name.text = item.name
//
//        holder.text_name.isSelected = item == mSelectData
//
//        holder.itemView.setOnClickListener{
//            listener?.onItemClick(holder.adapterPosition)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_cateogry, parent, false)
//        return ViewHolder(v)
//    }
//}