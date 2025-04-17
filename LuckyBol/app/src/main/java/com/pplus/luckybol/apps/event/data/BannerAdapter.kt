package com.pplus.luckybol.apps.event.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.EventBanner
import com.pplus.luckybol.databinding.ItemBannerBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class BannerAdapter : RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<EventBanner>? = null
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

    fun getItem(position: Int): EventBanner {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<EventBanner>? {

        return mDataList
    }

    fun add(data: EventBanner) {

        if (mDataList == null) {
            mDataList = ArrayList<EventBanner>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventBanner>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventBanner>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventBanner) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventBanner>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventBanner>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBanner
        init {}
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: EventBanner = mDataList!!.get(position)

        Glide.with(holder.itemView.context).load(item.image?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}