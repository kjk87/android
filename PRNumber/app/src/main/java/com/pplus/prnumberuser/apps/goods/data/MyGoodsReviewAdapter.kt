//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.page.data.GoodsReviewPagerAdapter
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_my_goods_review.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MyGoodsReviewAdapter : RecyclerView.Adapter<MyGoodsReviewAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<GoodsReview>? = null
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
//    fun getItem(position: Int): GoodsReview {
//
//        return mDataList!!.get(position)
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
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout_page = itemView.layout_my_goods_review_page
//        val image_page = itemView.image_my_goods_review_page
//        val text_page_name = itemView.text_my_goods_review_page_name
//        val layout_image = itemView.layout_my_goods_review_image
//        val pager_image = itemView.pager_my_goods_review_image
//        val indicator = itemView.indicator_my_goods_review
//        val text_contents = itemView.text_my_goods_review_contents
//        val text_regDate = itemView.text_my_goods_review_regDate
//        val grade_bar = itemView.grade_bar_my_goods_review
//        val layout_reply = itemView.layout_my_goods_review_reply
//        val text_reply = itemView.text_my_goods_review_reply
//        val text_reply_date = itemView.text_my_goods_review_reply_date
//
//        init {
//
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
//        if(item.page != null){
//            holder.layout_page.visibility = View.VISIBLE
//            holder.text_page_name.text = item.page!!.name
//            if (StringUtils.isNotEmpty(item.page!!.thumbnail)) {
//                Glide.with(mContext!!).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_page)
//            } else {
//                holder.image_page.setImageResource(R.drawable.img_commerce_user_profile_default)
//            }
//            holder.layout_page.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//                PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
//            }
//            holder.text_page_name.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//                PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
//            }
//        }else{
//            holder.layout_page.visibility = View.GONE
//        }
//
//        if(item.eval != null){
//            val eval = String.format("%.1f", item.eval!!.toFloat())
//            holder.grade_bar.build(eval)
//        }else{
//            val eval = String.format("%.1f", 0f)
//            holder.grade_bar.build(eval)
//        }
//
//        holder.text_contents.text = item.review
//        if(StringUtils.isNotEmpty(item.reviewReply)){
//            holder.layout_reply.visibility = View.VISIBLE
//            holder.text_reply.text = item.reviewReply
//
//            if(StringUtils.isNotEmpty(item.reviewReplyDate)){
//                holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
//            }
//
//        }else{
//            holder.layout_reply.visibility = View.GONE
//        }
//
//        try {
//            holder.text_regDate.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
//        } catch (e: Exception) {
//
//        }
//
//        if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
//            holder.layout_image.visibility = View.VISIBLE
//            holder.pager_image.visibility = View.VISIBLE
//            val imageAdapter = GoodsReviewPagerAdapter(mContext!!)
//            imageAdapter.dataList = item.attachments!!.images!! as ArrayList<String>
//            holder.pager_image.adapter = imageAdapter
//            holder.indicator.visibility = View.VISIBLE
//            holder.indicator.removeAllViews()
//            holder.indicator.build(LinearLayout.HORIZONTAL, item.attachments!!.images!!.size)
//            holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
//            imageAdapter.setListener(object : GoodsReviewPagerAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//                    if (listener != null) {
//                        listener!!.onItemClick(holder.adapterPosition)
//                    }
//                }
//            })
//        } else {
//            holder.indicator.removeAllViews()
//            holder.indicator.visibility = View.GONE
//            holder.pager_image.visibility = View.GONE
//            holder.pager_image.adapter = null
//            holder.layout_image.visibility = View.GONE
//        }
//
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_goods_review, parent, false)
//        return ViewHolder(v)
//    }
//}