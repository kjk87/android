package com.pplus.prnumberbiz.apps.post.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Post
import kotlinx.android.synthetic.main.item_select_post.view.*
import java.util.*

/**
 * Created by imac on 2018. 1. 8..
 */
class SelectPostAdapter : RecyclerView.Adapter<SelectPostAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Post>? = null
    var listener: OnItemClickListener? = null
    var mSelectData: Post? = null

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

    fun getItem(position: Int): Post {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Post>? {

        return mDataList
    }

    fun add(data: Post) {

        if (mDataList == null) {
            mDataList = ArrayList<Post>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Post>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Post>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Post) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Post>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Post>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_select_post
        val text_count = itemView.text_select_post_contents
        val image_check = itemView.image_select_post_check

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.attachList != null && item.attachList!!.isNotEmpty()){
            Glide.with(holder.itemView.context).load(item.attachList!![0].url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        }else{
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }


        holder.text_count.text = item.contents

        if (mSelectData != null && mSelectData == item) {
            holder.image_check.visibility = View.VISIBLE
        } else {
            holder.image_check.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_select_post, parent, false)
        return ViewHolder(v)
    }
}