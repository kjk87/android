//package com.pplus.prnumberuser.apps.main.data
//
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_coupon.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class CouponAdapter : RecyclerView.Adapter<CouponAdapter.ViewHolder> {
//
//    var mDataList: MutableList<Goods>? = null
//    var listener: OnItemClickListener? = null
//    var mIsRealTime = false
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//
//        fun onCheckDownload(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Goods {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Goods>? {
//
//        return mDataList
//    }
//
//    fun add(data: Goods) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Goods>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Goods>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Goods>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Goods) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Goods>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Goods>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_coupon_page_image
//        val text_page_name = itemView.text_coupon_page_name
//        val text_name = itemView.text_coupon_name
//        val text_discount_ratio = itemView.text_coupon_discount_ratio
//        val text_origin_price = itemView.text_coupon_origin_price
//        val text_sale_price = itemView.text_coupon_sale_price
//        val text_distance = itemView.text_coupon_distance
//        val text_remain_count = itemView.text_coupon_remain_count
//        val layout_download = itemView.layout_coupon_download
//        val text_real_time = itemView.text_coupon_realtime
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//
//        holder.text_name.text = item.name
//        if (item.originPrice != null && item.originPrice!! > 0) {
//
//            if (item.originPrice!! <= item.price!!) {
//                holder.text_origin_price.visibility = View.GONE
//            } else {
//                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
//                holder.text_origin_price.visibility = View.VISIBLE
//            }
//
//        } else {
//            holder.text_origin_price.visibility = View.GONE
//        }
//
//        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))
//
//        holder.text_page_name.text = item.page!!.name
//
//        if (StringUtils.isNotEmpty(item.page!!.thumbnail)) {
//            Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        if(item.discountRatio != null && item.discountRatio!! > 0){
//            holder.text_discount_ratio.visibility = View.VISIBLE
//            holder.text_discount_ratio.text = "${item.discountRatio!!.toInt()}%"
//        }else{
//            holder.text_discount_ratio.visibility = View.GONE
//        }
//
//        if (item.count != null && item.count != -1) {
//            var soldCount = 0
//            if (item.soldCount != null) {
//                soldCount = item.soldCount!!
//            }
//            holder.text_remain_count.visibility = View.VISIBLE
//            holder.text_remain_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
//        } else {
//            holder.text_remain_count.visibility = View.GONE
//        }
//
//        if(mIsRealTime){
//            holder.text_real_time.visibility = View.VISIBLE
//        }else{
//            holder.text_real_time.visibility = View.GONE
//        }
//
////        if (item.page!!.commissionPoint != null && item.page!!.commissionPoint!!.point!! > 0) {
////            holder.text_point.visibility = View.VISIBLE
////            val point = item.price!! * (item.page!!.commissionPoint!!.point!! / 100)
////            holder.text_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point, FormatUtil.getMoneyType(point.toInt().toString())))
////
////        } else {
////            holder.text_point.visibility = View.GONE
////        }
//
////        holder.image.setOnClickListener {
////            val location = IntArray(2)
////            it.getLocationOnScreen(location)
////            val x = location[0] + it.width / 2
////            val y = location[1] + it.height / 2
////
////            PplusCommonUtil.goPage(it.context, item.page!!, x, y)
////        }
//
//        if (item.distance != null && item.distance!! > 0) {
//            holder.text_distance.visibility = View.VISIBLE
//            var strDistance: String? = null
//            if (item.distance!! > 1) {
//                strDistance = String.format("%.2f", item.distance!!)
//                holder.text_distance.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_distance, strDistance, "km"))
//            } else {
//                strDistance = (item.distance!! * 1000).toInt().toString()
//                holder.text_distance.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_distance, strDistance, "m"))
//            }
//        } else {
//            holder.text_distance.visibility = View.GONE
//        }
//
//        holder.layout_download.setOnClickListener {
//            if (listener != null) {
//                listener!!.onCheckDownload(holder.adapterPosition)
//            }
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition, it)
//            }
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
//        return ViewHolder(v)
//    }
//}