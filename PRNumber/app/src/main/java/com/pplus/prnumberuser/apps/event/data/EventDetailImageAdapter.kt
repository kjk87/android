package com.pplus.prnumberuser.apps.event.data

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.pplus.prnumberuser.core.network.model.dto.EventDetailImage
import com.pplus.prnumberuser.databinding.ItemGoodsImageDetailBinding
import com.pplus.utils.part.info.DeviceUtil
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class EventDetailImageAdapter() : RecyclerView.Adapter<EventDetailImageAdapter.ViewHolder>() {

    var mDataList: MutableList<EventDetailImage>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventDetailImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventDetailImage>? {

        return mDataList
    }

    fun add(data: EventDetailImage) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventDetailImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventDetailImage) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventDetailImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemGoodsImageDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageGoodsImageDetail

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    class CustomBitmapImageViewTarget(view: ImageView?) : DrawableImageViewTarget(view) {

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            super.onResourceReady(resource, transition)
            val rate = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS.toFloat()/resource.intrinsicWidth.toFloat()
            val changedHeight = resource.intrinsicHeight*rate
            view.layoutParams.height = changedHeight.toInt()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]


        Glide.with(holder.itemView.context).load(item.image).into(CustomBitmapImageViewTarget(holder.image))
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoodsImageDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}