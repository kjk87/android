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
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.friend.ui.SameFriendActivity
//import com.pplus.prnumberuser.apps.post.ui.PhotoDetailViewerActivity
//import com.pplus.prnumberuser.apps.post.ui.PostDetailActivity
//import com.pplus.prnumberuser.apps.post.ui.ReplyDetailActivity
//import com.pplus.prnumberuser.apps.post.ui.ReplyEditActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Attachment
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_page_post.view.*
//import kotlinx.android.synthetic.main.item_reply.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PostHeaderCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mPost: Post? = null
//    var mContext: Context? = null
//    var mDataList: MutableList<Comment>? = null
//    var listener: OnItemClickListener? = null
//    var cCommentList: Map<Long, ArrayList<Comment>>? = null
//    val mTodayYear: Int = 0
//    val mTodayMonth: Int = 0
//    val mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    fun setPost(post: Post){
//        this.mPost = post
//    }
//
//    fun setcCommentList(cCommentList: Map<Long, ArrayList<Comment>>) {
//
//        this.cCommentList = cCommentList
//    }
//
//    fun setCommentCount(count:Int){
//        if(mPost != null){
//            mPost!!.commentCount = count
//        }
//
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Comment {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Comment>? {
//
//        return mDataList
//    }
//
//    fun add(data: Comment) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Comment>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Comment>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Comment>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Comment) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Comment>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Comment>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_profile = itemView.image_page_post_profile
//        val text_name = itemView.text_page_post_name
//        val layout_image = itemView.layout_page_post_image
//        val pager_image = itemView.pager_page_post_image
//        val indicator_post = itemView.indicator_page_post
//        val text_title = itemView.text_page_post_title
//        val text_contents = itemView.text_page_post_contents
//        val text_regDate = itemView.text_page_post_regDate
//        val text_comment = itemView.text_page_post_comment
//        val text_comment_count = itemView.text_page_post_comment_count
//        val image_share = itemView.layout_page_post_share
//        val image_more = itemView.image_page_post_more
//        val text_link = itemView.text_page_post_link
//        val layout_who = itemView.layout_page_post_who
//
//        init {
//            itemView.layout_page_post_bottom.visibility = View.GONE
//            itemView.layout_page_post_detail_bottom.visibility = View.VISIBLE
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout_reply_parent = itemView.layout_reply_parent
//        val image_profileImage = itemView.image_reply_profileImage
//        val image_child_profileImage = itemView.image_reply_child_profileImage
//        var text_name = itemView.text_reply_name
//        val text_contents = itemView.text_reply_contents
//        var text_regDate = itemView.text_reply_regDate
//        val text_child_name = itemView.text_reply_child_name
//        val text_child_contents = itemView.text_reply_child_contents
//        val text_child_replyCount = itemView.text_reply_child_replyCount
//        val text_reply = itemView.text_reply_reply
//        val layout_child = itemView.layout_reply_child
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if(mPost == null){
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mPost!!
//
//            if (StringUtils.isNotEmpty(item.subject)) {
//                holder.text_title.visibility = View.VISIBLE
//                holder.text_title.text = item.subject
//            } else {
//                holder.text_title.visibility = View.GONE
//            }
//
//            holder.text_contents.text = item.contents
//
//            holder.text_comment_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_total_count2, FormatUtil.getMoneyType(item.commentCount.toString())))
//            holder.text_comment.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reply, FormatUtil.getMoneyType(item.commentCount.toString())))
//
//            if (item.author != null && item.author!!.page != null) {
//                Glide.with(holder.itemView.context).load(item.author!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profile)
//            } else {
//                holder.image_profile.setImageResource(R.drawable.prnumber_default_img)
//            }
//
//            holder.image_profile.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width/2
//                val y = location[1] + it.height/2
//                PplusCommonUtil.goPage(holder.itemView.context, item.author!!.page!!, x, y)
//            }
//
//            holder.text_name.text = item.author!!.page!!.name
//
//            try {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)
//                val c = Calendar.getInstance()
//                c.time = d
//
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                    val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
//                    holder.text_regDate.text = output.format(d)
//                } else {
//                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
//                    holder.text_regDate.text = output.format(d)
//                }
//
//            } catch (e: Exception) {
//                holder.text_regDate.text = ""
//            }
//
//            holder.image_share.setOnClickListener {
//                share(item)
//            }
//
//            if (item.author?.haveSameFriends!!) {
//                holder.layout_who.visibility = View.VISIBLE
//            } else {
//                holder.layout_who.visibility = View.GONE
//            }
//
//            holder.layout_who.setOnClickListener {
//                val intent = Intent(holder.itemView.context, SameFriendActivity::class.java)
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width/2
//                val y = location[1] + it.height/2
//                intent.putExtra(Const.X, x)
//                intent.putExtra(Const.Y, y)
//                intent.putExtra(Const.DATA, item.author)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.image_more.setOnClickListener {
//                postAlert(item)
//            }
//
//            if (item.attachList != null && item.attachList!!.isNotEmpty()) {
//                holder.layout_image.visibility = View.VISIBLE
//                holder.pager_image.visibility = View.VISIBLE
//                val imageAdapter = PostImagePagerAdapter(holder.itemView.context, item.attachList!!)
//                holder.pager_image.adapter = imageAdapter
//                holder.indicator_post.visibility = View.VISIBLE
//                holder.indicator_post.removeAllViews()
//                holder.indicator_post.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
//                holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                    }
//
//                    override fun onPageSelected(position: Int) {
//
//                        holder.indicator_post.setCurrentItem(position)
//                    }
//
//                    override fun onPageScrollStateChanged(state: Int) {
//
//                    }
//                })
//
//                imageAdapter.setListener(object : PostImagePagerAdapter.OnItemClickListener{
//                    override fun onItemClick(position: Int) {
//                        val intent = Intent(holder.itemView.context, PhotoDetailViewerActivity::class.java)
//                        intent.putExtra(Const.POSITION, holder.pager_image.currentItem)
//                        intent.putParcelableArrayListExtra(Const.DATA, imageAdapter.getDataList() as ArrayList<Attachment>)
//                        holder.itemView.context.startActivity(intent)
//                    }
//                })
//
////                imageAdapter.setListener {
////                    val intent = Intent(mContext, PhotoDetailViewerActivity::class.java)
////                    intent.putExtra(Const.POSITION, holder.pager_image.currentItem)
////                    intent.putParcelableArrayListExtra(Const.DATA, imageAdapter.dataList as ArrayList<Attachment>)
////                    mContext?.startActivity(intent)
////                }
//            } else {
//                holder.indicator_post.removeAllViews()
//                holder.indicator_post.visibility = View.GONE
//                holder.pager_image.visibility = View.GONE
//                holder.pager_image.adapter = null
//                holder.layout_image.visibility = View.GONE
//            }
//
//            if(StringUtils.isNotEmpty(item.articleUrl)){
//                holder.text_link.visibility = View.VISIBLE
//            }else{
//                holder.text_link.visibility = View.GONE
//            }
//
//            holder.text_link.setOnClickListener {
//                if(StringUtils.isNotEmpty(item.articleUrl)){
//                    PplusCommonUtil.openChromeWebView(holder.itemView.context, item.articleUrl!!)
//                }
//            }
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//
//            if (item.isDeleted) {
//                holder.text_name.setText(R.string.word_unknown)
//                holder.text_contents.setText(R.string.msg_delete_comment)
//                holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//            } else {
//
//                if(item.author.useStatus == EnumData.UseStatus.leave.name){
//                    holder.text_name.setText(R.string.word_unknown)
//                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }else{
//                    if (item.author.no == mPost!!.author!!.no) {
//                        holder.text_name.text = mPost!!.author!!.page!!.name
//                        Glide.with(holder.itemView.context).load(mPost!!.author!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//
//                    } else {
//                        holder.text_name.text = item.author.nickname
//                        if (item.author.profileImage != null) {
//                            Glide.with(holder.itemView.context).load(item.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//                        } else {
//                            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                        }
//                    }
//                }
//
//                holder.text_contents.text = item.comment
//            }
//
//
//            if (cCommentList!!.get(item.no) != null) {
//                holder.layout_child.visibility = View.VISIBLE
//
//                val replyCount = cCommentList!![item.no]!!.size
//                val childComment = cCommentList!![item.no]!![cCommentList!![item.no]!!.size - 1]
//
//                if(childComment.author.useStatus == EnumData.UseStatus.leave.name){
//                    holder.text_child_name.setText(R.string.word_unknown)
//                }else{
//                    if (childComment.author.no == mPost!!.author!!.no) {
//                        holder.text_child_name.text = mPost!!.author!!.page!!.name
//
//                        Glide.with(holder.itemView.context).load(mPost!!.author!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
//                    }else{
//                        holder.text_child_name.text = childComment.author.nickname
//
//                        if (childComment.author.profileImage != null) {
//                            Glide.with(holder.itemView.context).load(childComment.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
//                        } else {
//                            holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                        }
//                    }
//                }
//
//
//
//                holder.text_child_name.text = childComment.author.nickname
//
//
//                holder.text_child_contents.text = childComment.comment
//                holder.text_child_replyCount.text = holder.itemView.context.getString(R.string.format_word_count_reply_of_reply, replyCount)
//
//            } else {
//                holder.layout_child.visibility =View.GONE
//            }
//
//            try {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)
//                val c = Calendar.getInstance()
//                c.time = d
//
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                    val output = SimpleDateFormat("a HH:mm")
//                    holder.text_regDate.text = output.format(d)
//                } else {
//                    val output = SimpleDateFormat("yyyy.MM.dd")
//                    holder.text_regDate.text = output.format(d)
//                }
//
//            } catch (e: Exception) {
//                holder.text_regDate.text = ""
//            }
//
//
//            holder.layout_child.setOnClickListener {
//                val intent = Intent(holder.itemView.context, ReplyDetailActivity::class.java)
//                intent.putExtra(Const.POST, mPost)
//                intent.putExtra(Const.REPLY, item)
//
//                if (cCommentList!![item.no] != null) {
//                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!![item.no])
//                }
//                if(holder.itemView.context is PostDetailActivity){
//                    (holder.itemView.context as PostDetailActivity).replyLauncher.launch(intent)
//                }
//            }
//
//            if (item.author.no == LoginInfoManager.getInstance().user.no) {
//                holder.text_reply.visibility = View.GONE
//            } else {
//                holder.text_reply.visibility = View.VISIBLE
//            }
//
//            holder.text_reply.setOnClickListener {
//                val intent = Intent(holder.itemView.context, ReplyDetailActivity::class.java)
//                intent.putExtra(Const.POST, mPost)
//                intent.putExtra(Const.REPLY, item)
//
//                if (cCommentList!![item.no] != null) {
//                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!![item.no])
//                }
//                if(holder.itemView.context is PostDetailActivity){
//                    (holder.itemView.context as PostDetailActivity).replyLauncher.launch(intent)
//                }
//            }
//
//            holder.layout_reply_parent.setOnClickListener {
//                val builder = AlertBuilder.Builder()
//                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
//
//                val isMe = LoginInfoManager.getInstance().user.no == item.author.no
//
//                if (isMe) {
//                    builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
//                } else {
//                    builder.setContents(holder.itemView.context.getString(R.string.msg_report))
//                }
//
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                        when (event_alert.getValue()) {
//                            1 -> if (isMe) {
//
//                                val intent = Intent(holder.itemView.context, ReplyEditActivity::class.java)
//                                intent.putExtra(Const.REPLY, item)
//                                if(holder.itemView.context is PostDetailActivity){
//                                    (holder.itemView.context as PostDetailActivity).replyLauncher.launch(intent)
//                                }
//
//                            } else {
//                                PplusCommonUtil.report(holder.itemView.context, EnumData.REPORT_TYPE.comment, item.no)
//                            }
//                            2 -> if (isMe) {
//
//                                AlertBuilder.Builder().setContents(holder.itemView.context.getString(R.string.msg_question_delete_reply)).setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm)).setOnAlertResultListener(object : OnAlertResultListener {
//
//                                    override fun onCancel() {
//
//                                    }
//
//                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                                        when (event_alert) {
//                                            AlertBuilder.EVENT_ALERT.RIGHT -> if (holder.itemView.context is PostDetailActivity) {
//                                                (holder.itemView.context as PostDetailActivity).deleteComment(item.no)
//                                            }
//                                        }
//                                    }
//                                }).builder().show(holder.itemView.context)
//
//                            }
//                        }
//                    }
//                }).builder().show(holder.itemView.context)
//            }
//        }
//    }
//
//    private fun postAlert(post: Post) {
//
//        if (post == null) {
//            return
//        }
//        val builder = AlertBuilder.Builder()
//        builder.setLeftText(mContext?.getString(R.string.word_cancel))
//
//        builder.setContents(mContext?.getString(R.string.word_share))
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
//                }
//            }
//        }).builder().show(mContext)
//    }
//
//    fun share(item:Post) {
//        val shareText = item.subject + "\n" + mContext?.getString(R.string.format_msg_page_url, "news.php?postNo=" + item.no!!)
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.action = Intent.ACTION_SEND
//        intent.type = "text/plain"
//        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//        intent.putExtra(Intent.EXTRA_TEXT, shareText)
//
//        val chooserIntent = Intent.createChooser(intent, mContext?.getString(R.string.word_share_post))
//        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//        mContext?.startActivity(chooserIntent)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_page_post, parent, false)
//            return ViewHeader(v)
//        } else if (viewType == TYPE_ITEM) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
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