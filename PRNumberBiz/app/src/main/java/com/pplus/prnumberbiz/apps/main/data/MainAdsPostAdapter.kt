package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.logs.LogUtil
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Advertise
import kotlinx.android.synthetic.main.item_main_ads_post.view.*
import java.util.ArrayList

/**
 * Created by imac on 2018. 1. 8..
 */
class MainAdsPostAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<MainAdsPostAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Advertise>? = null
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

    fun getItem(position: Int): Advertise {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Advertise>? {

        return mDataList
    }

    fun add(data: Advertise) {

        if (mDataList == null) {
            mDataList = ArrayList<Advertise>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Advertise>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Advertise>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Advertise) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        val size = this.mDataList?.size
        this.mDataList?.clear()
        notifyItemRangeRemoved(0, size!!)
    }

    fun setDataList(dataList: MutableList<Advertise>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_main_ads_post
        val text_count = itemView.text_main_ads_post_visit_count
        val view_rate = itemView.view_main_ads_post_visit_rate
        val layout_rate = itemView.layout_main_ads_post_visit_rate

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Advertise = mDataList!!.get(position);

        if (item.article?.attachList != null && item.article?.attachList?.isNotEmpty()!!) {
            LogUtil.e("IMG", "no : {}, url : {}", item.no, item.article?.attachList?.get(0)?.url)
            Glide.with(mContext!!).load(item.article?.attachList?.get(0)?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        holder.text_count.text = mContext?.getString(R.string.format_visit, item.currentCount)

        val layoutParams: ViewGroup.LayoutParams = holder.view_rate.layoutParams;

        holder.layout_rate.weightSum = item.totalCount!!.toFloat()

        if (layoutParams is LinearLayout.LayoutParams) {
            layoutParams.weight = item.currentCount!!.toFloat()
        }
        holder.view_rate.requestLayout()

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_ads_post, parent, false)
        return ViewHolder(v)
    }
}