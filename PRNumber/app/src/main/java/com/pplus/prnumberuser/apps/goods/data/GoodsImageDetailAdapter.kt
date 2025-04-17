//package com.pplus.prnumberuser.apps.goods.data
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.request.target.*
//import com.bumptech.glide.request.target.Target
//import com.bumptech.glide.request.transition.Transition
//import com.pplus.utils.part.info.DeviceUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import kotlinx.android.synthetic.main.item_goods_image_detail.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsImageDetailAdapter() : RecyclerView.Adapter<GoodsImageDetailAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<GoodsImage>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    init {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): GoodsImage {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsImage>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsImage) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsImage>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsImage) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsImage>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_goods_image_detail
//
//        init {
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    class CustomBitmapImageViewTarget(view: ImageView?) : DrawableImageViewTarget(view) {
//
//        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//            super.onResourceReady(resource, transition)
//            val rate = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS.toFloat()/resource.intrinsicWidth.toFloat()
//            val changedHeight = resource.intrinsicHeight*rate
//            view.layoutParams.height = changedHeight.toInt()
//        }
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//
//
//        Glide.with(holder.itemView.context).load(item.image).into(CustomBitmapImageViewTarget(holder.image))
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_image_detail, parent, false)
//        return ViewHolder(v)
//    }
//}