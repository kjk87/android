//package com.pplus.prnumberuser.apps.page.data
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
//import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
//import com.pplus.prnumberuser.core.network.model.dto.News
//import com.pplus.prnumberuser.core.network.model.dto.NewsImage
//import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.item_news.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class NewsAdapter(topVisible: Boolean) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<News>? = null
//    var listener: OnItemClickListener? = null
//    var mTopVisible = topVisible
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
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
//    fun getItem(position: Int): News {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<News>? {
//
//        return mDataList
//    }
//
//    fun add(data: News) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<News>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<News>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<News>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: News) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<News>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<News>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout_image = itemView.layout_news_image
//        val pager = itemView.pager_news_image
//        val indicator = itemView.indicator_news
//        val text_title= itemView.text_news_title
//        val text_link = itemView.text_news_link
//        val text_contents = itemView.text_news_contents
//        val text_reg_date = itemView.text_news_reg_date
//        val layout_review_count = itemView.layout_news_review_count
//        val layout_divider = itemView.layout_news_divider
//        val layout_profile = itemView.layout_news_profile
//        val image_page_profile = itemView.image_news_page_profile
//        val text_page_name = itemView.text_news_page_name
//        val layout_page_info = itemView.layout_news_page_info
//
//        init {
//            text_title.setSingleLine()
//            layout_review_count.visibility = View.GONE
//            layout_divider.visibility = View.VISIBLE
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
//        if(mTopVisible){
//            holder.layout_profile.visibility = View.VISIBLE
//            Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_page_profile)
//            holder.text_page_name.text = item.page!!.name
//        }else{
//            holder.layout_profile.visibility = View.GONE
//        }
//
//        holder.layout_page_info.setOnClickListener{
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
//        }
//
//        holder.text_title.text = item.title
//        holder.text_contents.text = item.content
//
//        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//        val output = SimpleDateFormat("yyyy.MM.dd")
//        holder.text_reg_date.text = output.format(d)
//
//        if(StringUtils.isEmpty(item.link) && item.productSeqNo == null){
//            holder.text_link.visibility = View.GONE
//        }else{
//            holder.text_link.visibility = View.VISIBLE
//            holder.text_link.setOnClickListener {
//                if(item.productSeqNo != null){
//                    val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
//                    val productPrice = ProductPrice()
//                    productPrice.seqNo = item.productSeqNo
//                    intent.putExtra(Const.DATA, productPrice)
//                    holder.itemView.context.startActivity(intent)
//                }else if(StringUtils.isNotEmpty(item.link)){
//                    PplusCommonUtil.openChromeWebView(holder.itemView.context, item.link!!)
//                }
//            }
//        }
//
//        if (item.newsImageList != null && item.newsImageList!!.isNotEmpty()) {
//            holder.layout_image.visibility = View.VISIBLE
//            holder.text_contents.maxLines = 2
//            if (item.newsImageList!!.size > 1) {
//                holder.indicator.visibility = View.VISIBLE
//            } else {
//                holder.indicator.visibility = View.GONE
//            }
//
//            val imageAdapter = NewsImagePagerAdapter(holder.itemView.context)
//            imageAdapter.dataList = item.newsImageList!! as ArrayList<NewsImage>
//
//            holder.pager.adapter = imageAdapter
//            holder.indicator.removeAllViews()
//            holder.indicator.build(LinearLayout.HORIZONTAL, item.newsImageList!!.size)
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
//            imageAdapter.mListener = object : NewsImagePagerAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//                    listener?.onItemClick(holder.adapterPosition)
//                }
//
//            }
//        }else{
//            holder.layout_image.visibility = View.GONE
//            holder.text_contents.maxLines = 5
//        }
//
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
//        return ViewHolder(v)
//    }
//}