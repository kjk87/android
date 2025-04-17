//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import kotlinx.android.synthetic.main.item_banner_list.view.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class BannerListAdapter : RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    private val imageRes = intArrayOf(R.drawable.img_main_banner_1, R.drawable.img_main_banner_2, R.drawable.img_main_banner_3, R.drawable.img_main_banner_4, R.drawable.img_main_banner_5, R.drawable.img_main_banner_6, R.drawable.img_main_banner_7)
//    var listener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_banner_list
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return imageRes.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = imageRes[position]
//
//        holder.image.setImageResource(item)
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition, it)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_list, parent, false)
//        return ViewHolder(v)
//    }
//}