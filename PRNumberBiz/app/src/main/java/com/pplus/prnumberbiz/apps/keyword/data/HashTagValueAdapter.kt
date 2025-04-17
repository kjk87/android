package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_hashtag_value.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class HashTagValueAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<HashTagValueAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<String>? = null
    var listener: OnItemClickListener? = null
    var mMyTagList: MutableList<String>? = null
    var mIsMe = false


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context, isMe:Boolean) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        this.mIsMe = isMe
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): String {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<String>? {

        return mDataList
    }

    fun add(data: String) {

        if (mDataList == null) {
            mDataList = ArrayList<String>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<String>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<String>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: String) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<String>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<String>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_value= itemView.text_hashtag_value

        init {
            text_value.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.text_value.text = "#$item"
        if((mMyTagList != null && mMyTagList!!.contains(item)) || mIsMe){
            holder.itemView.isSelected = true
        }else{
            holder.itemView.isSelected = false
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hashtag_value, parent, false)
        return ViewHolder(v)
    }
}