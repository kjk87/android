package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.NoticeInfo
import kotlinx.android.synthetic.main.item_goods_notice_info.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class GoodsNoticeInfoAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsNoticeInfoAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<NoticeInfo>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun onRefresh()
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): NoticeInfo {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<NoticeInfo>? {

        return mDataList
    }

    fun add(data: NoticeInfo) {

        if (mDataList == null) {
            mDataList = ArrayList<NoticeInfo>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<NoticeInfo>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<NoticeInfo>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: NoticeInfo) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<NoticeInfo>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<NoticeInfo>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_key = itemView.text_goods_notice_info_key
        val edit_value = itemView.edit_goods_notice_info_value

        init {
            edit_value.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_key.text = item.key
        holder.edit_value.setText(item.value)

        holder.edit_value.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                item.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_notice_info, parent, false)
        return ViewHolder(v)
    }
}