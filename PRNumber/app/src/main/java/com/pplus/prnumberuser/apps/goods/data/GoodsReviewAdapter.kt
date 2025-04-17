//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
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
//import kotlinx.android.synthetic.main.item_goods_review.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsReviewAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsReviewAdapter.ViewHolder> {
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
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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
//        if(item.member != null){
//            holder.text_name.text = item.member!!.nickname
//            if(item.member!!.profileAttachment != null){
//                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
//                Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
//            }else{
//                holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
//            }
//        }
//
//        holder.text_contents.text = item.review
//
//        if (StringUtils.isNotEmpty(item.reviewReply)) {
//            holder.layout_reply.visibility = View.VISIBLE
//            holder.text_reply.text = item.reviewReply
//            if(StringUtils.isNotEmpty(item.reviewReplyDate)){
//                holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
//            }
//
//        } else {
//            holder.layout_reply.visibility = View.GONE
//        }
//
//        if (item.eval != null) {
//            val eval = String.format("%.1f", item.eval!!.toFloat())
//            holder.grade_bar.build(eval)
//        } else {
//            val eval = String.format("%.1f", 0f)
//            holder.grade_bar.build(eval)
//        }
//
//        try {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//            val c = Calendar.getInstance()
//            c.time = d
//
//            val year = c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//
//            if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
//                holder.text_regDate.text = output.format(d)
//            } else {
//                val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
//                holder.text_regDate.text = output.format(d)
//            }
//
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
//            imageAdapter.setListener(object : GoodsReviewPagerAdapter.OnItemClickListener{
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
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_review, parent, false)
//        return ViewHolder(v)
//    }
//}