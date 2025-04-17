//package com.pplus.prnumberuser.apps.post.data
//
//import android.content.Context
//import android.content.Intent
//import androidx.viewpager.widget.ViewPager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.friend.ui.SameFriendActivity
//import com.pplus.prnumberuser.apps.post.ui.MyPostActivity
//import com.pplus.prnumberuser.apps.post.ui.PostReplyActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_page_post.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Post>? = null
//    var listener: OnItemClickListener? = null
//    var mTopVisible = true
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context, topVisible:Boolean) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//        mTopVisible = topVisible
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Post {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Post>? {
//
//        return mDataList
//    }
//
//    fun add(data: Post) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Post>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Post>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Post>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Post) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Post>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Post>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout_profile = itemView.layout_post_profile
//        val image_profile = itemView.image_page_post_profile
//        val text_name = itemView.text_page_post_name
//        val layout_image = itemView.layout_page_post_image
//        val pager_image = itemView.pager_page_post_image
//        val indicator_post = itemView.indicator_page_post
//        val text_title = itemView.text_page_post_title
//        val text_contents = itemView.text_page_post_contents
//        val text_regDate = itemView.text_page_post_regDate
//        val text_comment = itemView.text_page_post_comment
//        val image_share = itemView.layout_page_post_share
//        val image_more = itemView.image_page_post_more
//        val layout_reply = itemView.layout_page_post_reply
//        val text_link = itemView.text_page_post_link
//        val layout_who = itemView.layout_page_post_who
//
//        init {
//            itemView.layout_page_post_bottom.visibility = View.VISIBLE
//            itemView.layout_page_post_detail_bottom.visibility = View.GONE
//            text_contents.maxLines = 2
//            if (itemView.context is MyPostActivity) {
//                layout_profile.visibility = View.GONE
//            }
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
//
//            if(item.author != null && item.author!!.page != null){
//                Glide.with(holder.itemView.context).load(item.author!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profile)
//
//                holder.image_profile.setOnClickListener {
//                    val location = IntArray(2)
//                    it.getLocationOnScreen(location)
//                    val x = location[0] + it.width / 2
//                    val y = location[1] + it.height / 2
//                    PplusCommonUtil.goPage(holder.itemView.context, item.author!!.page!!, x, y)
//                }
//
//                holder.text_name.text = item.author!!.page!!.name
//            }
//        }else{
//            holder.layout_profile.visibility = View.GONE
//        }
//
//
//        if (StringUtils.isNotEmpty(item.subject)) {
//            holder.text_title.visibility = View.VISIBLE
//            holder.text_title.text = item.subject
//        } else {
//            holder.text_title.visibility = View.GONE
//        }
//
//        holder.text_contents.text = item.contents
//        holder.text_comment.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reply, FormatUtil.getMoneyType(item.commentCount.toString())))
//
//        try {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)
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
//            holder.text_regDate.text = ""
//        }
//
//        holder.image_share.setOnClickListener {
//            share(item)
//        }
//
//        holder.layout_reply.setOnClickListener {
//            val intent = Intent(mContext, PostReplyActivity::class.java)
//            intent.putExtra(Const.DATA, item)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            mContext?.startActivity(intent)
//        }
//
//        if (item.author?.haveSameFriends!!) {
//            holder.layout_who.visibility = View.GONE
//        } else {
//            holder.layout_who.visibility = View.GONE
//        }
//
//        holder.layout_who.setOnClickListener {
//            val intent = Intent(holder.itemView.context, SameFriendActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.putExtra(Const.DATA, item.author)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            holder.itemView.context.startActivity(intent)
//        }
//
//        holder.image_more.setOnClickListener {
//            postAlert(item, position - 1)
//        }
//
//        if (item.attachList != null && item.attachList!!.isNotEmpty()) {
////            holder.text_text_title.maxLines = 5
//            holder.layout_image.visibility = View.VISIBLE
//            holder.pager_image.visibility = View.VISIBLE
//            val imageAdapter = PostImagePagerAdapter(holder.itemView.context, item.attachList!!)
//            holder.pager_image.adapter = imageAdapter
//
//            if (item.attachList!!.size > 1) {
//                holder.indicator_post.visibility = View.VISIBLE
//            } else {
//                holder.indicator_post.visibility = View.GONE
//            }
//
//            holder.indicator_post.removeAllViews()
//            holder.indicator_post.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
//            holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//
//                    holder.indicator_post.setCurrentItem(position)
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//
//                }
//            })
//
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
//            imageAdapter.setListener(object : PostImagePagerAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//                    if (listener != null) {
//                        listener!!.onItemClick(holder.adapterPosition)
//                    }
//                }
//            })
////            imageAdapter.setListener {
////                if (listener != null) {
////                    listener!!.onItemClick(holder.adapterPosition)
////                }
////            }
//        } else {
////            holder.text_text_title.maxLines = 5
//            holder.indicator_post.removeAllViews()
//            holder.indicator_post.visibility = View.GONE
//            holder.pager_image.visibility = View.GONE
//            holder.pager_image.adapter = null
//            holder.layout_image.visibility = View.GONE
//        }
//
//        if(StringUtils.isNotEmpty(item.articleUrl)){
//            holder.text_link.visibility = View.VISIBLE
//        }else{
//            holder.text_link.visibility = View.GONE
//        }
//
//        holder.text_link.setOnClickListener {
//            if(StringUtils.isNotEmpty(item.articleUrl)){
//                PplusCommonUtil.openChromeWebView(holder.itemView.context, item.articleUrl!!)
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
//    private fun postAlert(post: Post, position: Int) {
//
//        val builder = AlertBuilder.Builder()
//        builder.setLeftText(mContext?.getString(R.string.word_cancel))
//
//        builder.setContents(mContext?.getString(R.string.word_share), mContext?.getString(R.string.msg_report))
//
//        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//            override fun onCancel() {
//
//            }
//
//            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                when (event_alert.getValue()) {
//                    1 -> share(post)
//                    2 -> {
//                        PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.article, post.no)
//                    }
//                }
//            }
//        }).builder().show(mContext)
//    }
//
//    fun share(item: Post) {
//        var contents = item.contents!!
//
//        if (contents.length > 50) {
//            contents = contents.substring(0, 50)
//        }
//        val shareText = contents + "\n" + mContext?.getString(R.string.format_msg_page_url, "news.php?postNo=" + item.no!!)
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.action = Intent.ACTION_SEND
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT, shareText)
//
//        val chooserIntent = Intent.createChooser(intent, mContext?.getString(R.string.word_share_post))
//        mContext?.startActivity(chooserIntent)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_page_post, parent, false)
//        return ViewHolder(v)
//    }
//}