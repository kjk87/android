package com.lejel.wowbox.apps.my.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.databinding.ItemBannerBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class BannerAdapter() : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    var mDataList: MutableList<Banner>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Banner {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Banner>? {

        return mDataList
    }

    fun add(data: Banner) {

        if (mDataList == null) {
            mDataList = ArrayList<Banner>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Banner>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Banner>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Banner) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Banner>()
    }

    fun setDataList(dataList: MutableList<Banner>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {


        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.itemView.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_690)
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerInside()).into(holder.binding.imageBanner)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}