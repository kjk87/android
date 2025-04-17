//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.graphics.Paint
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Page2
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_main_goods.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MainGoodsAdapter : RecyclerView.Adapter<MainGoodsAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Page2>? = null
//    var listener: OnItemClickListener? = null
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
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
//    fun getItem(position: Int): Page2 {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Page2>? {
//
//        return mDataList
//    }
//
//    fun add(data: Page2) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Page2>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Page2>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Page2>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Page2) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Page2>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Page2>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_page = itemView.image_main_goods_page_image
//        val text_name = itemView.text_main_goods_page_name
//        val image = itemView.image_main_goods_image
//        val text_goods_name = itemView.text_main_goods_goods_name
//        val text_origin_price = itemView.text_main_goods_origin_price
//        val text_sale_price = itemView.text_main_goods_sale_price
////        val text_discount = itemView.text_main_goods_discount
//        val text_distance = itemView.text_main_goods_page_distance
//        val text_point = itemView.text_main_goods_point
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
//        val item: Page2 = mDataList!![position]
//
//        if (item.goods != null) {
//
//            if (item.goods!!.attachments != null && item.goods!!.attachments!!.images != null && item.goods!!.attachments!!.images!!.isNotEmpty()) {
//                if (mContext != null) {
//                    val id = item.goods!!.attachments!!.images!![0]
//                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
//                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                }
//
//            } else {
//                holder.image.setImageResource(R.drawable.prnumber_default_img)
//            }
//
//            holder.text_goods_name.text = item.goods!!.name
//
////            holder.text_discount.visibility = View.GONE
//
//            if (item.goods!!.originPrice != null) {
//
//                val origin_price = item.goods!!.originPrice!!
//
//                if(origin_price <=  item.goods!!.price!!){
//                    holder.text_origin_price.visibility = View.GONE
//                }else{
//                    holder.text_origin_price.text = mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.goods!!.originPrice.toString()))
//                    holder.text_origin_price.visibility = View.VISIBLE
////                    holder.text_discount.visibility = View.VISIBLE
//
////                    val percent = (100 - (item.goods!!.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
////                    holder.text_discount.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_percent_unit, FormatUtil.getMoneyType(percent.toString())))
//                }
//
//            } else {
//                holder.text_origin_price.visibility = View.GONE
//            }
//
////            holder.text_discount.visibility = View.GONE
//
//            holder.text_sale_price.text = FormatUtil.getMoneyType(item.goods!!.price.toString())
//
//            val point = (item.goods!!.price!! * 0.01).toInt()
//            holder.text_point.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_cash_unit2, "+${FormatUtil.getMoneyType(point.toString())}"))
//        }
//
//        if (item.profileAttachment != null && StringUtils.isNotEmpty(item.profileAttachment!!.id)) {
//            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.profileAttachment!!.id}")
//            Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_page)
//        } else {
//            holder.image_page.setImageResource(R.drawable.prnumber_default_img)
//        }
//        holder.text_name.text = item.name
//
//        holder.image_page.setOnClickListener {
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//
//            PplusCommonUtil.goPage(it.context, item, x, y)
//        }
//
//        if (item.distance != null) {
//            holder.text_distance.visibility = View.VISIBLE
//            var strDistance: String? = null
//            if (item.distance!! > 1) {
//                strDistance = String.format("%.2f", item.distance!!)
//                holder.text_distance.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_distance, strDistance, "km"))
//            } else {
//                strDistance = (item.distance!! * 1000).toInt().toString()
//                holder.text_distance.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_distance, strDistance, "m"))
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