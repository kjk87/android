package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import kotlinx.android.synthetic.main.item_bank.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class BankSelectAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<BankSelectAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<String>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
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
        val text_bank_name = itemView.text_bank_name
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_bank_name.text = item

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_bank, parent, false)
        return ViewHolder(v)
    }
}