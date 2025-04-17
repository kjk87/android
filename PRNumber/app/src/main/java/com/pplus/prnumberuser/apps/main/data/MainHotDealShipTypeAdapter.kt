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
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.GoodsPrice
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import kotlinx.android.synthetic.main.item_main_hot_deal_ship_type.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MainHotDealShipTypeAdapter() : RecyclerView.Adapter<MainHotDealShipTypeAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<GoodsPrice>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
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
//    fun getItem(position: Int): GoodsPrice {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsPrice>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsPrice) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsPrice>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsPrice) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<GoodsPrice>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsPrice>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_main_hot_deal_ship_type_image
//        val text_save_point = itemView.text_main_hot_deal_ship_type_save_point
//        val text_goods_name = itemView.text_main_hot_deal_ship_type_name
//        val text_discount_ratio = itemView.text_main_hot_deal_ship_type_discount_ratio
//        val text_sale_price = itemView.text_main_hot_deal_ship_type_sale_price
//        val text_origin_price = itemView.text_main_hot_deal_ship_type_origin_price
//        val layout_count = itemView.layout_main_hot_deal_ship_type_count
//        val text_remain_count = itemView.text_main_hot_deal_ship_type_remain_count
//        val text_count = itemView.text_main_hot_deal_ship_type_count
//        val layout_rate_total = itemView.layout_main_hot_deal_ship_type_rate_total
//        val layout_rate = itemView.layout_main_hot_deal_ship_type_rate
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
//        if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        if(item.isLuckyball != null && item.isLuckyball!!){
//            holder.text_save_point.visibility = View.VISIBLE
//            holder.layout_count.visibility = View.VISIBLE
//        }else{
//            holder.text_save_point.visibility = View.GONE
//            holder.layout_count.visibility = View.GONE
//        }
//
//        if (item.goods!!.count != -1) {
//            holder.layout_rate_total.visibility = View.VISIBLE
//            holder.text_count.visibility = View.VISIBLE
//            holder.text_remain_count.visibility = View.VISIBLE
//            var soldCount = 0
//            if (item.goods!!.soldCount != null) {
//                soldCount = item.goods!!.soldCount!!
//            }
//
//            holder.text_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_count_per2, soldCount.toString(), item.goods!!.count.toString()))
//            holder.text_remain_count.text = holder.itemView.context.getString(R.string.format_remain_count, (item.goods!!.count!! - soldCount).toString())
//
//            var weightSum = 1
//            if (item.goods!!.count!! > 0) {
//                weightSum = item.goods!!.count!!
//            }
//            holder.layout_rate_total.weightSum = weightSum.toFloat()
//            val layoutParams = holder.layout_rate.layoutParams
//
//            if (layoutParams is LinearLayout.LayoutParams) {
//                layoutParams.weight = item.goods!!.soldCount!!.toFloat()
//            }
//            holder.layout_rate.requestLayout()
//
//        } else {
//            holder.text_remain_count.visibility = View.GONE
//            holder.layout_rate_total.visibility = View.GONE
//            holder.text_count.visibility = View.GONE
//        }
//
//        holder.text_save_point.text = holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType((item.price!!*0.1f).toInt().toString()))
//
//        holder.text_goods_name.text = item.goods!!.name
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
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_hot_deal_ship_type, parent, false)
//        return ViewHolder(v)
//    }
//}