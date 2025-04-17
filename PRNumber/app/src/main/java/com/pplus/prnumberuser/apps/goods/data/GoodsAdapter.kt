//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
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
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_goods.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsAdapter : RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
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
//        val image = itemView.image_goods_image
//        val text_title = itemView.text_goods_title
//        val text_origin_price = itemView.text_goods_origin_price
//        val layout_origin_price = itemView.layout_goods_origin_price
//        val text_sale_price = itemView.text_goods_sale_price
//        val text_discount = itemView.text_goods_discount
////        val text_discount_type = itemView.text_goods_discount_type
//        val layout_page = itemView.layout_goods_page
//        val view_bar = itemView.view_goods_bar
//        val image_page = itemView.image_goods_page_image
//        val text_page_name = itemView.text_goods_page_name
//        val image_sold_out= itemView.image_goods_sold_out
//        val text_point = itemView.text_goods_point
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
//        holder.text_title.text = item.name
//        holder.text_origin_price.visibility = View.GONE
//        holder.text_discount.visibility = View.GONE
//        holder.layout_origin_price.visibility = View.GONE
//        if (item.originPrice != null) {
//
//            val origin_price = item.originPrice!!
//
//            if (origin_price > item.price!!) {
//                holder.text_origin_price.visibility = View.VISIBLE
//                holder.text_discount.visibility = View.VISIBLE
//                holder.layout_origin_price.visibility = View.VISIBLE
//
//                holder.text_origin_price.text = FormatUtil.getMoneyType(origin_price.toString())
//
//                val percent = (100 - (item.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
//                holder.text_discount.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_percent_unit, FormatUtil.getMoneyType(percent.toString())))
//            }
//        }
//
//        holder.text_discount.visibility = View.GONE
//
//        holder.text_sale_price.text = FormatUtil.getMoneyType(item.price.toString())
//
//        val point = (item.price!! * 0.01).toInt()
//        holder.text_point.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_cash_unit2, "+${FormatUtil.getMoneyType(point.toString())}"))
//
//        if (item.status == EnumData.GoodsStatus.soldout.status) {
//            holder.image_sold_out.visibility = View.VISIBLE
//        } else {
//            holder.image_sold_out.visibility = View.GONE
//        }
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
//        if (item.goodsImageList != null && item.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(item.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        holder.layout_page.visibility = View.VISIBLE
//        holder.view_bar.visibility = View.GONE
//
//        val distance = item.distance
//        if (distance != null && distance > 0) {
////            holder.text_distance.visibility = View.VISIBLE
//            var strDistance: String? = null
//            if (distance > 1) {
//                strDistance = String.format("%.2f", distance) + "km"
//            } else {
//                strDistance = (distance * 1000).toInt().toString() + "m"
//            }
////            holder.text_distance.text = strDistance
//        } else {
////            holder.text_distance.visibility = View.GONE
//        }
//
//        if (item.page != null) {
//            if (StringUtils.isNotEmpty(item.page!!.thumbnail)) {
//                Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_profile_default).error(R.drawable.img_commerce_profile_default)).into(holder.image_page)
//            } else {
//                holder.image_page.setImageResource(R.drawable.img_commerce_profile_default)
//            }
//
//            holder.text_page_name.text = item.page!!.name
//
//            holder.layout_page.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//
//
//                PplusCommonUtil.goPage(it.context, item.page!!, x, y)
//            }
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