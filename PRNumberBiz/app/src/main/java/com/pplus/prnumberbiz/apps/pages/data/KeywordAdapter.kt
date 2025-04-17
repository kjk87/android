package com.pplus.prnumberbiz.apps.pages.data

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_keyword.view.*

/**
 * Created by imac on 2018. 1. 8..
 */
class KeywordAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<KeywordAdapter.ViewHolder> {

    var mDataList: MutableList<String>? = null
    var listener: OnItemClickListener? = null
    private var mDataChangedListener: DataChangedListener? = null

    private val bgRes = intArrayOf(R.drawable.bg_keyword_1, R.drawable.bg_keyword_2, R.drawable.bg_keyword_3, R.drawable.bg_keyword_4, R.drawable.bg_keyword_5)

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    interface DataChangedListener {

        fun onChanged(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun setDataChangedListener(dataChangedListener: DataChangedListener) {

        this.mDataChangedListener = dataChangedListener
    }

    fun getItem(position: Int): String {

        return mDataList!!.get(position)
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

        val text_keyword = itemView.text_keyword
        val image_delete = itemView.image_delete_keyword


        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!!.get(position)

        var index = position%bgRes.size

        holder.text_keyword.setBackgroundResource(bgRes[index])

        holder.text_keyword.text = item
        holder.image_delete.setOnClickListener{
            if (mDataChangedListener != null) {
                mDataChangedListener!!.onChanged(position)
            }
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword, parent, false)
        return ViewHolder(v)
    }
}