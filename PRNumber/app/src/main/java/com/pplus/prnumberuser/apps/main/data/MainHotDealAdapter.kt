//package com.pplus.prnumberuser.apps.main.data
//
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_main_goods.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MainHotDealAdapter : RecyclerView.Adapter<MainHotDealAdapter.ViewHolder> {
//
//    var mDataList: MutableList<Goods>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
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
//        val image = itemView.image_main_goods_image
//        val text_goods_name = itemView.text_main_goods_goods_name
//        val text_discount_ratio = itemView.text_main_goods_discount_ratio
//        val text_origin_price = itemView.text_main_goods_origin_price
//        val text_count = itemView.text_main_goods_count
//        val text_remain_count = itemView.text_main_goods_remain_count
//        val text_sale_price = itemView.text_main_goods_sale_price
//        val text_distance = itemView.text_main_goods_page_distance
//        val layout_rate_total = itemView.layout_main_goods_rate_total
//        val layout_rate = itemView.layout_main_goods_rate
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
//        if (item.goodsImageList != null && item.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(item.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        if (item.count != -1) {
//            holder.layout_rate_total.visibility = View.VISIBLE
//            holder.text_count.visibility = View.VISIBLE
//            holder.text_remain_count.visibility = View.VISIBLE
//            var soldCount = 0
//            if (item.soldCount != null) {
//                soldCount = item.soldCount!!
//            }
//
//            holder.text_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_count_per2, soldCount.toString(), item.count.toString()))
//            holder.text_remain_count.text = holder.itemView.context.getString(R.string.format_remain_count, (item.count!! - soldCount).toString())
//
//            var weightSum = 1
//            if (item.count!! > 0) {
//                weightSum = item.count!!
//            }
//            holder.layout_rate_total.weightSum = weightSum.toFloat()
//            val layoutParams = holder.layout_rate.layoutParams
//
//            if (layoutParams is LinearLayout.LayoutParams) {
//                layoutParams.weight = item.soldCount!!.toFloat()
//            }
//            holder.layout_rate.requestLayout()
//
//        } else {
//            holder.text_remain_count.visibility = View.GONE
//            holder.layout_rate_total.visibility = View.GONE
//            holder.text_count.visibility = View.GONE
//        }
//
//        holder.text_goods_name.text = item.name
//
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
////        holder.text_discount.visibility = View.GONE
//        if (item.discountRatio != null && item.discountRatio!! > 0) {
//            holder.text_discount_ratio.visibility = View.VISIBLE
//            holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit, item.discountRatio!!.toInt().toString()))
//        } else {
//            holder.text_discount_ratio.visibility = View.GONE
//        }
//        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))
//
////        if(item.page!!.point != null && item.page!!.point!! > 0){
////            holder.text_point.visibility = View.VISIBLE
////            val point = (item.price!!*(item.page!!.point!!/100f)).toInt()
////            holder.text_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${FormatUtil.getMoneyType(point.toString())}P"))
////        }else{
////            holder.text_point.visibility = View.GONE
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
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition, it)
//            }
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_goods, parent, false)
//        return ViewHolder(v)
//    }
//}