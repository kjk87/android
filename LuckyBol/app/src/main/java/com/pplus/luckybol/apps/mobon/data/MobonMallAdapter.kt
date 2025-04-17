//package com.pplus.luckybol.apps.mobon.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.luckybol.R
//import com.pplus.luckybol.core.network.model.dto.MobonMall
//import kotlinx.android.synthetic.main.item_mobon_mall.view.*
//import java.net.URLDecoder
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MobonMallAdapter(s: String) : RecyclerView.Adapter<MobonMallAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<MobonMall>? = null
//    var listener: OnItemClickListener? = null
//    var s:String? = s
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    init {
//        this.mDataList = arrayListOf()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): MobonMall {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<MobonMall>? {
//
//        return mDataList
//    }
//
//    fun add(data: MobonMall) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<MobonMall>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<MobonMall>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<MobonMall>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: MobonMall) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<MobonMall>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<MobonMall>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_mobon_mall
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
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if(position == 0){
//            holder.image.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_390)
//            when (s) {
//                "20629" -> {//여성의류
//                    holder.image.setImageResource(R.drawable.img_number_banner_detail_2)
//                }
//                "20628" -> {//남성의류
//                    holder.image.setImageResource(R.drawable.img_number_banner_detail_1)
//                }
//            }
//
//        }else{
//            holder.image.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_600)
//            val item = mDataList!![position]
//            val p_name = URLDecoder.decode(item.p_name, "UTF-8");
//            var imageUrl = item.p_img
//
//            if(!imageUrl!!.startsWith("http:") && !imageUrl.startsWith("https:")){
//                imageUrl = "http:${item.p_img}"
//            }
//
//            LogUtil.e("IMAGE URL", "name : {} url : {}", p_name, imageUrl)
//
//            val glideUrl = GlideUrl(imageUrl)
////            Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).transition(DrawableTransitionOptions.withCrossFade()).into(holder.image)
//            Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.absoluteAdapterPosition)
//            }
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_mobon_mall, parent, false)
//        return ViewHolder(v)
//    }
//}