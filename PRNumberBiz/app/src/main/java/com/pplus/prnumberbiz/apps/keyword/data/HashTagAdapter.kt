package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberbiz.core.network.model.dto.HashTag
import kotlinx.android.synthetic.main.item_hashtag.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class HashTagAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<HashTagAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<HashTag>? = null
    var listener: OnItemListener? = null
    var mMyTagList: MutableList<String>? = null


    interface OnItemListener {

        fun onItemSelect(tag: String)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemListener(listener: OnItemListener) {

        this.listener = listener
    }

    fun getItem(position: Int): HashTag {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<HashTag>? {

        return mDataList
    }

    fun add(data: HashTag) {

        if (mDataList == null) {
            mDataList = ArrayList<HashTag>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<HashTag>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<HashTag>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: HashTag) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<HashTag>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<HashTag>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_key = itemView.text_hashtag_key
        val recycler_value = itemView.recycler_hashtag_value

        init {
            recycler_value.addItemDecoration(BottomItemOffsetDecoration(itemView.context, R.dimen.height_30))
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.text_key.text = item.key

        val keywordList = item.value!!.split(",").toMutableList()

        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(holder.itemView.context, 30)
        layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                val keyWord = keywordList[position]

                return if (keyWord.length < 3) {
                    7
                } else if (keyWord.length < 4) {
                    8
                } else if (keyWord.length < 5) {
                    9
                } else if (keyWord.length < 6) {
                    10
                } else if (keyWord.length < 7) {
                    12
                } else if (keyWord.length < 8) {
                    13
                } else if (keyWord.length < 10) {
                    14
                } else {
                    20
                }
            }
        }

        holder.recycler_value.layoutManager = layoutManager
        val adapter = HashTagValueAdapter(holder.itemView.context, false)
        adapter.mMyTagList = mMyTagList
        adapter.setDataList(keywordList)
        holder.recycler_value.adapter = adapter
        adapter.setOnItemClickListener(object : HashTagValueAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(listener != null){
                    listener!!.onItemSelect(keywordList[position])
                }

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_hashtag, parent, false)
        return ViewHolder(v)
    }
}