//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Intent
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailViewerActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsNoticeInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsRefundInfoActivity
//import com.pplus.prnumberuser.apps.page.data.GoodsReviewPagerAdapter
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import com.pplus.prnumberuser.core.network.model.dto.GoodsPrice
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.header_goods_ship_type.view.*
//import kotlinx.android.synthetic.main.item_goods_review.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsShipTypeHeaderReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mGoodsPrice: GoodsPrice? = null
//    var mDataList: MutableList<GoodsReview>? = null
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
//    fun getItem(position: Int): GoodsReview {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsReview>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsReview) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<GoodsReview>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsReview>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<GoodsReview>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsReview) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<GoodsReview>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsReview>) {
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
//            itemView.layout_header_goods_ship_type_info.visibility = View.VISIBLE
//            itemView.layout_header_goods_ship_type_review.visibility = View.VISIBLE
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_profile = itemView.image_goods_review_profile
//        val text_name = itemView.text_goods_review_name
//        val layout_image = itemView.layout_goods_review_image
//        val pager_image = itemView.pager_goods_review_image
//        val indicator = itemView.indicator_goods_review
//        val text_contents = itemView.text_goods_review_contents
//        val text_regDate = itemView.text_goods_review_regDate
//        val grade_bar = itemView.grade_bar_goods_review
//        val layout_reply = itemView.layout_goods_review_reply
//        val text_reply = itemView.text_goods_review_reply
//        val text_reply_date = itemView.text_goods_review_reply_date
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
//
//            if (item.member != null) {
//                holder.text_name.text = item.member!!.nickname
//                if (item.member!!.profileAttachment != null) {
//                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
//                    Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
//                } else {
//                    holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
//                }
//            }
//
//            holder.text_contents.text = item.review
//            if (StringUtils.isNotEmpty(item.reviewReply)) {
//                holder.layout_reply.visibility = View.VISIBLE
//                holder.text_reply.text = item.reviewReply
//                if(StringUtils.isNotEmpty(item.reviewReplyDate)){
//                    holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
//                }
//
//            } else {
//                holder.layout_reply.visibility = View.GONE
//            }
//
//            if (item.eval != null) {
//                val eval = String.format("%.1f", item.eval!!.toFloat())
//                holder.grade_bar.build(eval)
//            } else {
//                val eval = String.format("%.1f", 0f)
//                holder.grade_bar.build(eval)
//            }
//
//            try {
//                holder.text_regDate.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
//
//            } catch (e: Exception) {
//
//            }
//
//            if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
//                holder.layout_image.visibility = View.VISIBLE
//                holder.pager_image.visibility = View.VISIBLE
//                val imageAdapter = GoodsReviewPagerAdapter(holder.itemView.context)
//                imageAdapter.dataList = item.attachments!!.images!! as ArrayList<String>
//                holder.pager_image.adapter = imageAdapter
//                holder.indicator.visibility = View.VISIBLE
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.attachments!!.images!!.size)
//                holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
//                imageAdapter.setListener(object : GoodsReviewPagerAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        if (listener != null) {
//                            listener!!.onItemClick(holder.adapterPosition - 1)
//                        }
//                    }
//                })
//            } else {
//                holder.indicator.removeAllViews()
//                holder.indicator.visibility = View.GONE
//                holder.pager_image.visibility = View.GONE
//                holder.pager_image.adapter = null
//                holder.layout_image.visibility = View.GONE
//            }
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.adapterPosition - 1)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.header_goods_ship_type, parent, false))
//        } else if (viewType == TYPE_ITEM) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods_review, parent, false))
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