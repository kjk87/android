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
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.ui.PostReplyActivity
//import com.pplus.prnumberuser.apps.post.ui.UserPostActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_user_post.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class UserPostAdapter : RecyclerView.Adapter<UserPostAdapter.ViewHolder> {
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
//        val image_profile = itemView.image_user_post_profile
//        val text_name = itemView.text_user_post_name
//        val layout_image = itemView.layout_user_post_image
//        val pager_image = itemView.pager_user_post_image
//        val indicator_post = itemView.indicator_user_post
//        val text_contents = itemView.text_user_post_contents
//        val text_regDate = itemView.text_user_post_regDate
//        val text_comment = itemView.text_user_post_comment
//        val image_more = itemView.image_user_post_more
//
//        init {
////            text_contents.maxLines = 2
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
//        if (item.author!!.profileImage != null) {
//            Glide.with(holder.itemView.context).load(item.author!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profile)
//        } else {
//            holder.image_profile.setImageResource(R.drawable.img_post_profile_default)
//        }
//
////        holder.image_profile.setOnClickListener {
////            val location = IntArray(2)
////            it.getLocationOnScreen(location)
////            val x = location[0] + it.width / 2
////            val y = location[1] + it.height / 2
////            PplusCommonUtil.goPage(mContext!!, item.author!!.page!!, x, y)
////        }
//
//        holder.text_name.text = item.author!!.nickname
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
//        holder.image_more.setOnClickListener {
//            postAlert(item, position - 1)
//        }
//
//        holder.text_comment.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PostReplyActivity::class.java)
//            intent.putExtra(Const.DATA, item)
//            intent.putExtra(Const.POSITION, position)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            if(holder.itemView.context is UserPostActivity){
//                (holder.itemView.context as UserPostActivity).replyLauncher.launch(intent)
//            }
//
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
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user_post, parent, false)
//        return ViewHolder(v)
//    }
//}