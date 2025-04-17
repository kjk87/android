//package com.pplus.prnumberbiz.apps.goods.data
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Paint
//import android.support.v4.view.ViewPager
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.load.model.LazyHeaders
//import com.bumptech.glide.request.RequestOptions
//import com.google.gson.JsonParser
//import com.pple.pplus.utils.part.format.FormatUtil
//import com.pple.pplus.utils.part.logs.LogUtil
//import com.pple.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.core.network.model.dto.Goods
//import com.pplus.prnumberbiz.core.network.model.dto.Page
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_goods.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsOtherAdapter : RecyclerView.Adapter<GoodsOtherAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Goods>? = null
//    var listener: OnItemClickListener? = null
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Goods {
//
//        return mDataList!!.get(position)
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
//        val image = itemView.image_goods_image
//        val text_title = itemView.text_goods_title
//        val text_origin_price = itemView.text_goods_origin_price
//        val layout_origin_price = itemView.layout_goods_origin_price
//        val text_sale_price = itemView.text_goods_sale_price
////        val layout_discount = itemView.layout_goods_discount
//        val text_discount = itemView.text_goods_discount
////        val text_discount_type = itemView.text_goods_discount_type
////        val text_remain_count = itemView.text_goods_remain_count
//        val layout_page = itemView.layout_goods_page
//        val image_page = itemView.image_goods_page_image
//        val text_page_name = itemView.text_goods_page_name
//        val view_bar = itemView.view_goods_bar
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            view_bar.visibility = View.GONE
//            layout_page.visibility = View.VISIBLE
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item: Goods = mDataList!![position]
//
//        holder.text_title.text = item.name
//        holder.layout_origin_price.visibility = View.GONE
//        holder.text_discount.visibility = View.GONE
////        holder.layout_discount.visibility = View.GONE
//        if (item.originPrice != null) {
//
//            val origin_price = item.originPrice!!
//
//            if (origin_price > item.price!!) {
//                holder.layout_origin_price.visibility = View.VISIBLE
//                holder.text_discount.visibility = View.VISIBLE
////                    holder.layout_discount.visibility = View.VISIBLE
//
//                holder.text_origin_price.text = FormatUtil.getMoneyType(origin_price.toString())
//
//                val percent = (100 - (item.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
//                holder.text_discount.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_percent_unit, FormatUtil.getMoneyType(percent.toString())))
//            }
//        }
//
//        holder.text_sale_price.text = FormatUtil.getMoneyType(item.price.toString())
//
//        if(item.count!= null && item.count != -1){
//            var soldCount = 0
//            if(item.soldCount != null){
//                soldCount = item.soldCount!!
//            }
////            holder.text_remain_count.visibility = View.VISIBLE
////            holder.text_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_main_goods_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
//        }else{
////            holder.text_remain_count.visibility = View.GONE
//        }
//
//
//        if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
//            if (mContext != null) {
//                val id = item.attachments!!.images!![0]
//                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
//                Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//            }
//
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        if (item.page != null) {
//            if (item.page!!.profileAttachment != null && StringUtils.isNotEmpty(item.page!!.profileAttachment!!.id)) {
//                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.page!!.profileAttachment!!.id}")
//                Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_commerce_profile_default).error(R.drawable.img_commerce_profile_default)).into(holder.image_page)
//            } else {
//                holder.image_page.setImageResource(R.drawable.img_commerce_profile_default)
//            }
//
//            holder.text_page_name.text = item.page!!.name
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition)
//            }
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false)
//        return ViewHolder(v)
//    }
//}