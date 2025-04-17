//package com.pplus.prnumberuser.apps.news.data
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.news.ui.NewsDetailViewerActivity
//import com.pplus.prnumberuser.apps.page.data.NewsImagePagerAdapter
//import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.News
//import com.pplus.prnumberuser.core.network.model.dto.NewsImage
//import com.pplus.prnumberuser.core.network.model.dto.NewsReview
//import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.item_news.view.*
//import kotlinx.android.synthetic.main.item_news_review.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class NewsHeaderReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mNews: News? = null
//    var mTotalCount = 0
//    var mDataList: MutableList<NewsReview>? = null
//    var listener: OnItemClickListener? = null
//    val mTodayYear: Int = 0
//    val mTodayMonth: Int = 0
//    val mTodayDay: Int = 0
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
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): NewsReview {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<NewsReview>? {
//
//        return mDataList
//    }
//
//    fun add(data: NewsReview) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<NewsReview>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<NewsReview>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<NewsReview>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: NewsReview) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<NewsReview>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<NewsReview>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout_page_info = itemView.layout_news_page_info
//        val layout_image = itemView.layout_news_image
//        val pager = itemView.pager_news_image
//        val indicator = itemView.indicator_news
//        val text_title= itemView.text_news_title
//        val text_link = itemView.text_news_link
//        val text_contents = itemView.text_news_contents
//        val text_reg_date = itemView.text_news_reg_date
//        val layout_review_count = itemView.layout_news_review_count
//        val layout_divider = itemView.layout_news_divider
//        val text_review_count = itemView.text_news_review_count
//        val layout_profile = itemView.layout_news_profile
//        val image_page_profile = itemView.image_news_page_profile
//        val text_page_name = itemView.text_news_page_name
//
//        init {
////            text_title.setSingleLine()
//            layout_review_count.visibility = View.VISIBLE
//            layout_divider.visibility = View.GONE
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_profileImage = itemView.image_news_review_profileImage
//        var text_name = itemView.text_news_review_name
//        val text_contents = itemView.text_news_review_contents
//        var text_reg_date = itemView.text_news_review_reg_date
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if(mNews == null){
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mNews!!
//
//            holder.layout_profile.visibility = View.VISIBLE
//            Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_page_profile)
//            holder.text_page_name.text = item.page!!.name
//            holder.layout_page_info.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//                PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
//            }
//
//            holder.text_title.text = item.title
//            holder.text_contents.text = item.content
//
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//            val output = SimpleDateFormat("yyyy.MM.dd")
//            holder.text_reg_date.text = output.format(d)
//
//            holder.text_review_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reply, FormatUtil.getMoneyType(mTotalCount.toString())))
//
//            if(StringUtils.isEmpty(item.link) && item.productSeqNo == null){
//                holder.text_link.visibility = View.GONE
//            }else{
//                holder.text_link.visibility = View.VISIBLE
//                holder.text_link.setOnClickListener {
//                    if(item.productSeqNo != null){
//                        val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
//                        val productPrice = ProductPrice()
//                        productPrice.seqNo = item.productSeqNo
//                        intent.putExtra(Const.DATA, productPrice)
//                        holder.itemView.context.startActivity(intent)
//                    }else if(StringUtils.isNotEmpty(item.link)){
//                        PplusCommonUtil.openChromeWebView(holder.itemView.context, item.link!!)
//                    }
//                }
//            }
//
//            if (item.newsImageList != null && item.newsImageList!!.isNotEmpty()) {
//                holder.layout_image.visibility = View.VISIBLE
//                if (item.newsImageList!!.size > 1) {
//                    holder.indicator.visibility = View.VISIBLE
//                } else {
//                    holder.indicator.visibility = View.GONE
//                }
//
//                val imageAdapter = NewsImagePagerAdapter(holder.itemView.context)
//                imageAdapter.dataList = item.newsImageList!! as ArrayList<NewsImage>
//
//                holder.pager.adapter = imageAdapter
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.newsImageList!!.size)
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
//                imageAdapter.mListener = object : NewsImagePagerAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        val intent = Intent(holder.itemView.context, NewsDetailViewerActivity::class.java)
//                        intent.putExtra(Const.POSITION, holder.pager.currentItem)
//                        intent.putExtra(Const.DATA, imageAdapter.dataList)
//                        holder.itemView.context.startActivity(intent)
//                    }
//
//                }
//            }else{
//                holder.layout_image.visibility = View.GONE
//            }
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//
//            if (item.deleted!!) {
//                holder.text_name.setText(R.string.word_unknown)
//                holder.text_contents.setText(R.string.msg_delete_comment)
//                holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//            } else {
//
//                if(item.member == null || item.member!!.useStatus == EnumData.UseStatus.leave.name){
//                    holder.text_name.setText(R.string.word_unknown)
//                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }else{
//                    if (item.memberSeqNo == mNews!!.page!!.memberSeqNo) {
//                        holder.text_name.text = mNews!!.page!!.name
//                        Glide.with(holder.itemView.context).load(mNews!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//
//                    } else {
//                        holder.text_name.text = item.member!!.nickname
//                        if (item.member!!.profileAttachment != null) {
//                            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//                        } else {
//                            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                        }
//                    }
//                }
//
//                holder.text_contents.text = item.review
//            }
//            try {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//                val c = Calendar.getInstance()
//                c.time = d
//
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                    val output = SimpleDateFormat("a HH:mm")
//                    holder.text_reg_date.text = output.format(d)
//                } else {
//                    val output = SimpleDateFormat("yyyy.MM.dd")
//                    holder.text_reg_date.text = output.format(d)
//                }
//
//            } catch (e: Exception) {
//                holder.text_reg_date.text = ""
//            }
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.adapterPosition-1)
//            }
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
//            return ViewHeader(v)
//        } else if (viewType == TYPE_ITEM) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_news_review, parent, false)
//            return ViewHolder(v)
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
//
//}