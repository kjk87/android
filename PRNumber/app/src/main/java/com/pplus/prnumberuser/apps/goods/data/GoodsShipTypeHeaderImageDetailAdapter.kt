//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Intent
//import android.graphics.Paint
//import android.graphics.drawable.Drawable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.DrawableImageViewTarget
//import com.bumptech.glide.request.transition.Transition
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailViewerActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsNoticeInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsRefundInfoActivity
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import com.pplus.prnumberuser.core.network.model.dto.GoodsPrice
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.info.DeviceUtil
//import kotlinx.android.synthetic.main.header_goods_ship_type.view.*
//import kotlinx.android.synthetic.main.item_goods_image_detail.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsShipTypeHeaderImageDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mGoodsPrice: GoodsPrice? = null
//    var mDataList: MutableList<GoodsImage>? = null
//    var listener: OnItemClickListener? = null
//    var mTotalCount = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
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
//            mDataList = ArrayList<GoodsImage>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsImage>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<GoodsImage>()
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
//        mDataList = ArrayList<GoodsImage>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsImage>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val pager = itemView.pager_header_goods_ship_type_image
//        val indicator = itemView.indicator_header_goods_ship_type
//        val text_goods_name = itemView.text_header_goods_ship_type_name
//        val text_discount_ratio = itemView.text_header_goods_ship_type_discount_ratio
//        val text_origin_price = itemView.text_header_goods_ship_type_origin_price
//        val text_sale_price = itemView.text_header_goods_ship_type_sale_price
//        val text_remain_count = itemView.text_header_goods_ship_type_remain_count
//        val text_contents = itemView.text_header_goods_ship_type_contents
//        val text_goods_notice_info = itemView.text_header_goods_ship_type_notice_info
//        val text_refund_exchange_info = itemView.text_header_goods_ship_type_refund_exchange_info
//        val text_buy_warning = itemView.text_header_goods_ship_type_buy_warning
//        val text_page_review_count = itemView.text_header_goods_ship_type_review_count
//        val text_page_review_grade = itemView.text_header_goods_ship_type_review_grade
//        val grade_bar_page_review_total = itemView.grade_bar_header_goods_ship_type_review_total
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_goods_image_detail
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if (mGoodsPrice == null) {
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mGoodsPrice!!
//            if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//
//                if (item.goods!!.goodsImageList!!.size > 1) {
//                    holder.indicator.visibility = View.VISIBLE
//                } else {
//                    holder.indicator.visibility = View.GONE
//                }
//
//                val imageAdapter = GoodsImagePagerAdapter(holder.itemView.context)
//                imageAdapter.dataList = item.goods!!.goodsImageList!! as ArrayList<GoodsImage>
//
//                holder.pager.adapter = imageAdapter
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.goods!!.goodsImageList!!.size)
//                holder.pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                    }
//
//                    override fun onPageSelected(position: Int) {
//
//                        holder.indicator.setCurrentItem(position)
//                    }
//
//                    override fun onPageScrollStateChanged(state: Int) {
//
//                    }
//                })
//
//                imageAdapter.mListener = object : GoodsImagePagerAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        val intent = Intent(holder.itemView.context, GoodsDetailViewerActivity::class.java)
//                        intent.putExtra(Const.POSITION, holder.pager.currentItem)
//                        intent.putExtra(Const.DATA, imageAdapter.dataList)
//                        holder.itemView.context.startActivity(intent)
//                    }
//
//                }
//
//            }
//
//            if (item.goods!!.count != -1) {
//                var soldCount = 0
//                if (item.goods!!.soldCount != null) {
//                    soldCount = item.goods!!.soldCount!!
//                }
//                holder.text_remain_count.visibility = View.VISIBLE
//                holder.text_remain_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((item.goods!!.count!! - soldCount).toString())))
//            } else {
//                holder.text_remain_count.visibility = View.GONE
//            }
//
//            holder.text_goods_name.text = item.goods!!.name
//
//            if (item.originPrice != null && item.originPrice!! > 0) {
//
//                if (item.originPrice!! <= item.price!!) {
//                    holder.text_origin_price.visibility = View.GONE
//                } else {
//                    holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
//                    holder.text_origin_price.visibility = View.VISIBLE
//                }
//
//            } else {
//                holder.text_origin_price.visibility = View.GONE
//            }
//
//            if (item.discountRatio != null && item.discountRatio!! > 0) {
//                holder.text_discount_ratio.visibility = View.VISIBLE
//                holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit, item.discountRatio!!.toInt().toString()))
//            } else {
//                holder.text_discount_ratio.visibility = View.GONE
//            }
//            holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))
//            holder.text_contents.text = item.goods!!.description
//
//            holder.text_goods_notice_info.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsNoticeInfoActivity::class.java)
//                intent.putExtra(Const.DATA, item.goods)
//                intent.putExtra(Const.KEY, Const.REFUND)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_refund_exchange_info.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsRefundInfoActivity::class.java)
//                intent.putExtra(Const.DATA, item.goods)
//                intent.putExtra(Const.KEY, Const.REFUND)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_buy_warning.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsInfoActivity::class.java)
//                intent.putExtra(Const.KEY, Const.WARNING)
//                intent.putExtra(Const.GOODS, item.goods)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_page_review_count.text = holder.itemView.context.getString(R.string.format_review_count, FormatUtil.getMoneyType(mTotalCount.toString()))
//
//            if (item.avgEval != null) {
//                val avgEval = String.format("%.1f", item.avgEval)
//                holder.text_page_review_grade.text = avgEval
//                holder.grade_bar_page_review_total.build(avgEval)
//            } else {
//                holder.text_page_review_grade.text = "0.0"
//                holder.grade_bar_page_review_total.build("0.0")
//            }
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//            Glide.with(holder.itemView.context).load(item.image).into(GoodsImageDetailAdapter.CustomBitmapImageViewTarget(holder.image))
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.adapterPosition - 1)
//            }
//        }
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
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.header_goods_ship_type, parent, false))
//        } else if (viewType == TYPE_ITEM) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods_image_detail, parent, false))
//        }
//        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
//    }
//
//    private fun isPositionHeader(position: Int): Boolean {
//        return position == 0
//    }
//}