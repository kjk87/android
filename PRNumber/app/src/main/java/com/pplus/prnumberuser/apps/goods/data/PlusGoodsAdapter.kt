//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_plus_goods.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PlusGoodsAdapter : RecyclerView.Adapter<PlusGoodsAdapter.ViewHolder> {
//
//    var mDataList: MutableList<Goods>? = null
//    var listener: OnItemClickListener? = null
//    var mTopVisible = true
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context, topVisible:Boolean) : super() {
//        this.mDataList = ArrayList()
//        mTopVisible = topVisible
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
//        val layout_top = itemView.layout_plus_goods_top
//        val layout_page = itemView.layout_plus_goods_page
//        val image_page = itemView.image_plus_goods_page_image
//        val text_page_name = itemView.text_plus_goods_page_name
//        val pager = itemView.pager_plus_goods_image
//        val indicator = itemView.indicator_plus_goods
//        val text_name = itemView.text_plus_goods_name
//        val text_origin_price = itemView.text_plus_goods_origin_price
//        val text_sale_price = itemView.text_plus_goods_sale_price
//        val text_remain_count = itemView.text_plus_goods_remain_count
//        val text_contents = itemView.text_plus_goods_contents
//        val text_review = itemView.text_plus_goods_review_count
//        val image_more = itemView.image_plus_goods_more
//        val text_point = itemView.text_plus_goods_point
//
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
//        if(mTopVisible){
//            holder.layout_top.visibility = View.VISIBLE
//        }else{
//            holder.layout_top.visibility = View.GONE
//        }
//
//        holder.text_name.text = item.name
//        holder.text_contents.text = item.description
//
////        var avgEval = "0.0"
////        if (item.avgEval != null) {
////            avgEval = String.format("%.1f", item.avgEval)
////        }
//
//        holder.text_review.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_review2, FormatUtil.getMoneyType(item.reviewCount.toString())))
//        holder.text_origin_price.visibility = View.GONE
//
//        if (item.originPrice != null) {
//
//            val origin_price = item.originPrice!!
//
//            if (origin_price > item.price!!) {
//                holder.text_origin_price.visibility = View.VISIBLE
////                holder.text_discount.visibility = View.VISIBLE
////                holder.text_discount_type.visibility = View.VISIBLE
//
//                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(origin_price.toString()))
//
////                val percent = (100 - (item.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
////                holder.text_discount.text = FormatUtil.getMoneyType(percent.toString())
//            }
//        }
//
////        holder.text_discount.visibility = View.GONE
////        holder.text_discount_type.visibility = View.GONE
//
//        if (item.rewardLuckybol != null && item.rewardLuckybol!! > 0) {
//            holder.text_point.visibility = View.VISIBLE
//            holder.text_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point, FormatUtil.getMoneyType(item.rewardLuckybol!!.toString())))
//
//        } else {
//            holder.text_point.visibility = View.GONE
//        }
//
//        holder.text_sale_price.text = FormatUtil.getMoneyType(item.price.toString())
//
//        if (item.status == EnumData.GoodsStatus.soldout.status) {
//            holder.text_remain_count.visibility = View.GONE
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
//
//        if (item.goodsImageList != null && item.goodsImageList!!.isNotEmpty()) {
//
//            if (item.goodsImageList!!.size > 1) {
//                holder.indicator.visibility = View.VISIBLE
//            } else {
//                holder.indicator.visibility = View.GONE
//            }
//
//            val imageAdapter = GoodsImagePagerAdapter(holder.itemView.context)
//            imageAdapter.dataList = item.goodsImageList!! as ArrayList<GoodsImage>
//
//            holder.pager.adapter = imageAdapter
//            holder.indicator.removeAllViews()
//            holder.indicator.build(LinearLayout.HORIZONTAL, item.goodsImageList!!.size)
//            holder.pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//
//                    holder.indicator.setCurrentItem(position)
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//
//                }
//            })
//
//            imageAdapter.mListener = object : GoodsImagePagerAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//                    if (listener != null) {
//                        listener!!.onItemClick(holder.adapterPosition)
//                    }
//                }
//
//            }
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
//        holder.image_more.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
//            builder.setContents(holder.itemView.context.getString(R.string.word_share), holder.itemView.context.getString(R.string.msg_report))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            val contents = item.name
//
//                            val shareText = contents + "\n" + holder.itemView.context.getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${item.seqNo}")
//
//                            val intent = Intent(Intent.ACTION_SEND)
//                            intent.action = Intent.ACTION_SEND
//                            intent.type = "text/plain"
//                            //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//                            intent.putExtra(Intent.EXTRA_TEXT, shareText)
//
//                            val chooserIntent = Intent.createChooser(intent, holder.itemView.context.getString(R.string.word_share))
//                            //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//                            holder.itemView.context.startActivity(chooserIntent)
//                        }
//                        2 -> {
//                            PplusCommonUtil.report(holder.itemView.context, EnumData.REPORT_TYPE.goods, item.seqNo)
//                        }
//                    }
//                }
//            }).builder().show(holder.itemView.context)
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
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_plus_goods, parent, false)
//        return ViewHolder(v)
//    }
//}