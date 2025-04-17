package com.pplus.prnumberbiz.apps.post.data

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.post.ui.*
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Comment
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_reply.view.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.viewpager.widget.ViewPager
import android.widget.LinearLayout
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import com.pplus.prnumberbiz.core.network.model.dto.Post
import kotlinx.android.synthetic.main.item_review.view.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ReviewHeaderCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mPost: Post? = null
    var mContext: Context? = null
    var mDataList: MutableList<Comment>? = null
    var listener: OnItemClickListener? = null
    var cCommentList: Map<Long, ArrayList<Comment>>? = null
    val mTodayYear: Int = 0
    val mTodayMonth: Int = 0
    val mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setPost(post: Post){
        this.mPost = post
    }

    fun setcCommentList(cCommentList: Map<Long, ArrayList<Comment>>) {

        this.cCommentList = cCommentList
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Comment {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Comment>? {

        return mDataList
    }

    fun add(data: Comment) {

        if (mDataList == null) {
            mDataList = ArrayList<Comment>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Comment>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Comment>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Comment) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Comment>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Comment>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_review_profileImage
        val text_name = itemView.text_review_name
        val layout_image = itemView.layout_review_image
        val pager_image = itemView.pager_review_image
        val indicator = itemView.indicator_review
        val text_contents = itemView.text_review_contents
        val text_regDate = itemView.text_review_regDate
        val text_comment = itemView.text_review_comment

        init {

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val layout_reply_parent = itemView.layout_reply_parent
        val image_profileImage = itemView.image_reply_profileImage
        val image_child_profileImage = itemView.image_reply_child_profileImage
        var text_name = itemView.text_reply_name
        val text_contents = itemView.text_reply_contents
        var text_regDate = itemView.text_reply_regDate
        val text_child_name = itemView.text_reply_child_name
        val text_child_contents = itemView.text_reply_child_contents
        val text_child_replyCount = itemView.text_reply_child_replyCount
        val text_reply = itemView.text_reply_reply
        val layout_child = itemView.layout_reply_child

        init {
        }
    }

    override fun getItemCount(): Int {
        if(mPost == null){
            return mDataList!!.size
        }
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {
            val item = mPost!!
            if (item.author!!.profileImage != null) {
                Glide.with(mContext!!).load(item.author!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.img_post_profile_default)
            }

            holder.text_name.text = item.author!!.nickname

            holder.text_contents.text = item.contents
            holder.text_comment.text = mContext?.getString(R.string.word_reply) + " " + item.commentCount

            try {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)
                val c = Calendar.getInstance()
                c.time = d

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                    val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
                    holder.text_regDate.text = output.format(d)
                } else {
                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                    holder.text_regDate.text = output.format(d)
                }

            } catch (e: Exception) {

            }

            if (item.attachList != null && item.attachList!!.size > 0) {
                holder.layout_image.visibility = View.VISIBLE
                holder.pager_image.visibility = View.VISIBLE
                val imageAdapter = PostImagePagerAdapter(mContext, item.attachList)
                holder.pager_image.adapter = imageAdapter
                holder.indicator.visibility = View.VISIBLE
                holder.indicator.removeAllViews()
                holder.indicator.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
                holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {

                        holder.indicator.setCurrentItem(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })

//            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
                imageAdapter.setListener {
                    val intent = Intent(mContext, PhotoDetailViewerActivity::class.java)
                    intent.putExtra(Const.POSITION, holder.pager_image.currentItem)
                    intent.putParcelableArrayListExtra(Const.DATA, imageAdapter.dataList as ArrayList<Attachment>)
                    mContext?.startActivity(intent)
                }
            } else {
                holder.indicator.removeAllViews()
                holder.indicator.visibility = View.GONE
                holder.pager_image.visibility = View.GONE
                holder.pager_image.adapter = null
                holder.layout_image.visibility = View.GONE
            }
        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            if (item.isDeleted) {
                holder.text_name.setText(R.string.word_unknown)
                holder.text_contents.setText(R.string.msg_delete_comment)
                holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
            } else {
                if(item.author.useStatus == EnumData.UseStatus.leave.name){
                    holder.text_name.setText(R.string.word_unknown)
                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
                }else{
                    if (item.author.no == LoginInfoManager.getInstance().user.no) {
                        holder.text_name.text = LoginInfoManager.getInstance().user.page!!.name
                        if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
                            Glide.with(mContext!!).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
                        } else {
                            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
                        }

                    } else {
                        holder.text_name.text = item.author.nickname
                        if (item.author.profileImage != null) {
                            Glide.with(mContext!!).load(item.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
                        } else {
                            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
                        }
                    }
                }


                holder.text_contents.text = item.comment
            }


            if (cCommentList!!.get(item.no) != null) {
                holder.layout_child.visibility = View.VISIBLE

                val replyCount = cCommentList!![item.no]!!.size
                val childComment = cCommentList!![item.no]!![cCommentList!![item.no]!!.size - 1]

                if(childComment.author.useStatus == EnumData.UseStatus.leave.name){
                    holder.text_child_name.setText(R.string.word_unknown)
                    holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
                }else{
                    if (childComment.author.no == LoginInfoManager.getInstance().user.no) {
                        holder.text_child_name.text = LoginInfoManager.getInstance().user.page!!.name

                        if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
                            Glide.with(mContext!!).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
                        } else {
                            holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
                        }
                    }else{
                        holder.text_child_name.text = childComment.author.nickname

                        if (childComment.author.profileImage != null) {
                            Glide.with(mContext!!).load(childComment.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
                        } else {
                            holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
                        }
                    }
                }

                holder.text_child_contents.text = childComment.getComment()
                holder.text_child_replyCount.setText(mContext?.getString(R.string.format_word_count_reply_of_reply, replyCount))

            } else {
                holder.layout_child.visibility = View.GONE
            }

            try {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)
                val c = Calendar.getInstance()
                c.time = d

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                    val output = SimpleDateFormat("a HH:mm")
                    holder.text_regDate.text = output.format(d)
                } else {
                    val output = SimpleDateFormat("yyyy/MM/dd")
                    holder.text_regDate.text = output.format(d)
                }

            } catch (e: Exception) {

            }


            holder.layout_child.setOnClickListener {
                val intent = Intent(mContext, ReplyDetailActivity::class.java)
                intent.putExtra(Const.POST, mPost)
                intent.putExtra(Const.REPLY, item)

                if (cCommentList!!.get(item.no) != null) {
                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!!.get(item.no))
                }
                if (mContext is ReviewDetailActivity) {
                    (mContext as ReviewDetailActivity).startActivityForResult(intent, Const.REQ_REPLY)
                }
            }

            if (item.author.no == LoginInfoManager.getInstance().user.no) {
                holder.text_reply.visibility = View.GONE
            } else {
                holder.text_reply.visibility = View.VISIBLE
            }

            holder.text_reply.setOnClickListener {
                val intent = Intent(mContext, ReplyDetailActivity::class.java)
                intent.putExtra(Const.REPLY, item)

                if (cCommentList!!.get(item.no) != null) {
                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!!.get(item.no))
                }

                if (mContext is ReviewDetailActivity) {
                    (mContext as ReviewDetailActivity).startActivityForResult(intent, Const.REQ_REPLY)
                }
            }

            holder.layout_reply_parent.setOnClickListener {
                val builder = AlertBuilder.Builder()
                builder.setLeftText(mContext?.getString(R.string.word_cancel))

                val isMe = LoginInfoManager.getInstance().user.no == item.author.no

                if (isMe) {
                    builder.setContents(mContext?.getString(R.string.word_modified), mContext?.getString(R.string.word_delete))
                } else {
                    builder.setContents(mContext?.getString(R.string.msg_report))
                }

                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert.getValue()) {
                            1 -> if (isMe) {

                                val intent = Intent(mContext, ReplyEditActivity::class.java)
                                intent.putExtra(Const.REPLY, item)

                                if (mContext is ReviewDetailActivity) {
                                    (mContext as ReviewDetailActivity).startActivityForResult(intent, Const.REQ_REPLY)
                                }

                            } else {
                                PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.comment, item.no)
                            }
                            2 -> if (isMe) {

                                AlertBuilder.Builder().setContents(mContext?.getString(R.string.msg_question_delete_reply)).setLeftText(mContext?.getString(R.string.word_cancel)).setRightText(mContext?.getString(R.string.word_confirm)).setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.RIGHT -> if (mContext is ReviewDetailActivity) {
                                                (mContext as ReviewDetailActivity).deleteComment(item.no)
                                                mPost!!.commentCount = mPost!!.commentCount!!-1
                                                notifyItemChanged(0)
                                            }
                                        }
                                    }
                                }).builder().show(mContext)

                            }
                        }
                    }
                }).builder().show(mContext)
            }
        }
    }

    private fun postAlert() {

        if (mPost == null) {
            return
        }
        val builder = AlertBuilder.Builder()
        builder.setLeftText(mContext?.getString(R.string.word_cancel))
        builder.setContents(mContext?.getString(R.string.msg_report))

        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert.getValue()) {
                    1 -> {
                        PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.article, mPost!!.no)
                    }
                }
            }
        }).builder().show(mContext)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
            return ViewHeader(v)
        } else if (viewType == TYPE_ITEM) {
            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
            return ViewHolder(v)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}