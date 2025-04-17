//package com.pplus.prnumberuser.apps.post.data
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.FragmentActivity
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.ui.PostReplyActivity
//import com.pplus.prnumberuser.apps.post.ui.ReplyDetailActivity
//import com.pplus.prnumberuser.apps.post.ui.ReplyEditActivity
//import com.pplus.prnumberuser.apps.post.ui.ReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_reply.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ReplyAdapter : RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Comment>? = null
//    var listener: OnItemClickListener? = null
//    var cCommentList: Map<Long, ArrayList<Comment>>? = null
//    val mTodayYear: Int = 0
//    val mTodayMonth: Int = 0
//    val mTodayDay: Int = 0
//    var mPost: Post? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context, post: Post) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.mPost = post
//    }
//
//    fun setcCommentList(cCommentList: Map<Long, ArrayList<Comment>>) {
//
//        this.cCommentList = cCommentList
//    }
//
//    fun setCommentCount(count: Int) {
//        if (mPost != null) {
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
//        return mDataList!!.get(position)
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
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!!.get(position);
//
//        if (item.isDeleted) {
//            holder.text_name.setText(R.string.word_unknown)
//            holder.text_contents.setText(R.string.msg_delete_comment)
//            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//        } else {
//
//            if (item.author.useStatus == EnumData.UseStatus.leave.name) {
//                holder.text_name.setText(R.string.word_unknown)
//                holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//            } else {
//                holder.text_name.text = item.author.nickname
//                if (item.author.profileImage != null) {
//                    Glide.with(mContext!!).load(item.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//                } else {
//                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }
//            }
//
//            holder.text_contents.text = item.comment
//        }
//
//        if (cCommentList != null && cCommentList!![item.no] != null) {
//            holder.layout_child.visibility = View.VISIBLE
//
//            val replyCount = cCommentList!![item.no]!!.size
//            val childComment = cCommentList!![item.no]!![cCommentList!![item.no]!!.size - 1]
//
//            if (childComment.author.useStatus == EnumData.UseStatus.leave.name) {
//                holder.text_child_name.setText(R.string.word_unknown)
//            } else {
//                holder.text_child_name.text = childComment.author.nickname
//
//                if (childComment.author.profileImage != null) {
//                    Glide.with(mContext!!).load(childComment.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
//                } else {
//                    holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }
//            }
//
//            holder.text_child_name.text = childComment.author.nickname
//
//            holder.text_child_contents.text = childComment.comment
//            holder.text_child_replyCount.text = mContext?.getString(R.string.format_word_count_reply_of_reply, replyCount)
//
//        } else {
//            holder.layout_child.visibility = View.GONE
//        }
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
//                val output = SimpleDateFormat("a HH:mm")
//                holder.text_regDate.text = output.format(d)
//            } else {
//                val output = SimpleDateFormat("yyyy/MM/dd")
//                holder.text_regDate.text = output.format(d)
//            }
//
//        } catch (e: Exception) {
//            holder.text_regDate.text = ""
//        }
//
//
//        holder.layout_child.setOnClickListener {
//            val intent = Intent(mContext, ReplyDetailActivity::class.java)
//            intent.putExtra(Const.POST, mPost)
//            intent.putExtra(Const.REPLY, item)
//
//            if (cCommentList!![item.no] != null) {
//                intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!![item.no])
//            }
//            (holder.itemView.context as PostReplyActivity).replyLauncher.launch(intent)
//        }
//
//        if (LoginInfoManager.getInstance().isMember && item.author.no == LoginInfoManager.getInstance().user.no) {
//            holder.text_reply.visibility = View.GONE
//        } else {
//            holder.text_reply.visibility = View.VISIBLE
//        }
//
//        holder.text_reply.setOnClickListener {
//            val intent = Intent(mContext, ReplyDetailActivity::class.java)
//            intent.putExtra(Const.POST, mPost)
//            intent.putExtra(Const.REPLY, item)
//
//            if (cCommentList!![item.no] != null) {
//                intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!![item.no])
//            }
//            (holder.itemView.context as PostReplyActivity).replyLauncher.launch(intent)
//        }
//
//        holder.layout_reply_parent.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck((holder.itemView.context as FragmentActivity), null)) {
//                return@setOnClickListener
//            }
//
//            val builder = AlertBuilder.Builder()
//            builder.setLeftText(mContext?.getString(R.string.word_cancel))
//
//            val isMe = LoginInfoManager.getInstance().user.no == item.author.no
//
//            if (isMe) {
//                builder.setContents(mContext?.getString(R.string.word_modified), mContext?.getString(R.string.word_delete))
//            } else {
//                builder.setContents(mContext?.getString(R.string.msg_report))
//            }
//
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert.getValue()) {
//                        1 -> if (isMe) {
//
//                            val intent = Intent(mContext, ReplyEditActivity::class.java)
//                            intent.putExtra(Const.REPLY, item)
//                            (holder.itemView.context as PostReplyActivity).replyLauncher.launch(intent)
//
//                        } else {
//                            PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.comment, item.no)
//                        }
//                        2 -> if (isMe) {
//
//                            AlertBuilder.Builder().setContents(mContext?.getString(R.string.msg_question_delete_reply)).setLeftText(mContext?.getString(R.string.word_cancel)).setRightText(mContext?.getString(R.string.word_confirm)).setOnAlertResultListener(object : OnAlertResultListener {
//
//                                override fun onCancel() {
//
//                                }
//
//                                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                                    when (event_alert) {
//                                        AlertBuilder.EVENT_ALERT.RIGHT -> if (mContext is PostReplyActivity) {
//                                            (mContext as PostReplyActivity).deleteComment(item.no)
//                                        }
//                                    }
//                                }
//                            }).builder().show(mContext)
//
//                        }
//                    }
//                }
//            }).builder().show(mContext)
//        }
//
//    }
//
//    private fun deletePost(no: Long, position: Int) {
//
//        ApiBuilder.create().deletePost(no).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//                mDataList?.removeAt(position)
//                notifyItemRemoved(position)
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//                ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_review)
//
//            }
//        }).build().call()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
//        return ViewHolder(v)
//    }
//}