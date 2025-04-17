package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_hashtag_search.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class HashTagSearchAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<HashTagSearchAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<String>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(word: String)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
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

        val text_search = itemView.text_hashtag_search

        init {
            text_search.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.text_search.text = "#$item"

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(item)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hashtag_search, parent, false)
        return ViewHolder(v)
    }
}