//package com.pplus.prnumberuser.apps.post.data
//
//import android.app.Activity.RESULT_OK
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
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.ui.ReplyDetailActivity
//import com.pplus.prnumberuser.apps.post.ui.ReviewDetailActivity
//import com.pplus.prnumberuser.apps.post.ui.ReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Comment
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_reply.view.*
//import kotlinx.android.synthetic.main.item_review.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ReviewHeaderCommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mPage: Page? = null
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
//    fun setPage(page: Page?) {
//        this.mPage = page
//    }
//
//    fun setPost(post: Post) {
//        this.mPost = post
//    }
//
//    fun setcCommentList(cCommentList: Map<Long, ArrayList<Comment>>) {
//
//        this.cCommentList = cCommentList
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
//        val image = itemView.image_review_profile
//        val text_name = itemView.text_review_name
//        val layout_image = itemView.layout_review_image
//        val pager_image = itemView.pager_review_image
//        val indicator = itemView.indicator_review
//        val text_contents = itemView.text_review_contents
//        val text_regDate = itemView.text_review_regDate
//        val text_comment = itemView.text_review_comment
//        val image_more = itemView.image_review_more
//
//        init {
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
//        if (mPost == null) {
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mPost!!
//            if(item.author!!.useStatus == EnumData.UseStatus.leave.name){
//                holder.text_name.setText(R.string.word_unknown)
//                holder.image.setImageResource(R.drawable.img_post_profile_default)
//            }else{
//                if (item.author!!.profileImage != null) {
//                    Glide.with(holder.itemView.context).load(item.author!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image)
//                } else {
//                    holder.image.setImageResource(R.drawable.img_post_profile_default)
//                }
//
//                holder.text_name.text = item.author!!.nickname
//            }
//
//            holder.text_contents.text = item.contents
//            holder.text_comment.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_reply, FormatUtil.getMoneyType(item.commentCount.toString())))
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
//            if (item.attachList != null && item.attachList!!.size > 0) {
//                holder.layout_image.visibility = View.VISIBLE
//                holder.pager_image.visibility = View.VISIBLE
//                val imageAdapter = PostImagePagerAdapter(holder.itemView.context, item.attachList!!)
//                holder.pager_image.adapter = imageAdapter
//                holder.indicator.visibility = View.VISIBLE
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
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
//                imageAdapter.setListener(object : PostImagePagerAdapter.OnItemClickListener{
//                    override fun onItemClick(position: Int) {
//                        if (listener != null) {
//                            listener!!.onItemClick(holder.adapterPosition)
//                        }
//                    }
//                })
//
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
////                imageAdapter.setListener {
////                    if (listener != null) {
////                        listener!!.onItemClick(holder.getAdapterPosition())
////                    }
////                }
//            } else {
//                holder.indicator.removeAllViews()
//                holder.indicator.visibility = View.GONE
//                holder.pager_image.visibility = View.GONE
//                holder.pager_image.adapter = null
//                holder.layout_image.visibility = View.GONE
//            }
//
//            holder.image_more.setOnClickListener {
//                postAlert()
//            }
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//
//            if (item.isDeleted) {
//                holder.text_name.setText(R.string.word_unknown)
//                holder.text_contents.setText(R.string.msg_delete_comment)
//                holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//            } else {
//                if(item.author.useStatus == EnumData.UseStatus.leave.name){
//                    holder.text_name.setText(R.string.word_unknown)
//                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }else{
//                    if(item.author.page != null && mPage != null && item.author.page!!.no == mPage!!.no){
//                        holder.text_name.text = mPage!!.name
//                        Glide.with(holder.itemView.context).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//
//                    }else{
//                        holder.text_name.text = item.author.nickname
//                        if (item.author.profileImage != null) {
//                            Glide.with(holder.itemView.context).load(item.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage)
//                        } else {
//                            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                        }
//
//                    }
//                }
//
//                holder.text_contents.text = item.comment
//            }
//
//
//            if (cCommentList!![item.no] != null) {
//                holder.layout_child.visibility = View.VISIBLE
//
//                val replyCount = cCommentList!![item.no]!!.size
//                val childComment = cCommentList!![item.no]!![cCommentList!![item.no]!!.size - 1]
//
//                if(childComment.author.useStatus == EnumData.UseStatus.leave.name){
//                    holder.text_child_name.setText(R.string.word_unknown)
//                    holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                }else{
//                    if(childComment.author.page != null && mPage != null && childComment.author.page!!.no == mPage!!.no){
//                        Glide.with(holder.itemView.context).load(mPage!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
//
//                        holder.text_child_name.text = mPage!!.nickname
//                    }else{
//                        if (childComment.author.profileImage != null) {
//                            Glide.with(holder.itemView.context).load(childComment.author.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage)
//                        } else {
//                            holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default)
//                        }
//
//                        holder.text_child_name.text = childComment.author.nickname
//                    }
//                }
//
//                holder.text_child_contents.text = childComment.comment
//                holder.text_child_replyCount.text = mContext?.getString(R.string.format_word_count_reply_of_reply, replyCount)
//
//            } else {
//                holder.layout_child.visibility = View.GONE
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
//                    val output = SimpleDateFormat("yyyy/MM/dd")
//                    holder.text_regDate.text = output.format(d)
//                }
//
//            } catch (e: Exception) {
//                holder.text_regDate.text = ""
//            }
//
//
//            holder.layout_child.setOnClickListener {
//                val intent = Intent(mContext, ReplyDetailActivity::class.java)
//                intent.putExtra(Const.POST, mPost)
//                intent.putExtra(Const.REPLY, item)
//
//                if (cCommentList!!.get(item.no) != null) {
//                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!![item.no])
//                }
//                if (mContext is ReviewDetailActivity) {
//                    (mContext as ReviewDetailActivity).startActivityForResult(intent, Const.REQ_REPLY)
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
//                val intent = Intent(mContext, ReplyDetailActivity::class.java)
//                intent.putExtra(Const.POST, mPost)
//                intent.putExtra(Const.REPLY, item)
//
//                if (cCommentList!!.get(item.no) != null) {
//                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList!!.get(item.no))
//                }
//
//                if (mContext is ReviewDetailActivity) {
//                    (mContext as ReviewDetailActivity).startActivityForResult(intent, Const.REQ_REPLY)
//                }
//            }
//        }
//    }
//
//
//    private fun postAlert() {
//
//        if (mPost == null) {
//            return
//        }
//
//        val builder = AlertBuilder.Builder()
//        builder.setLeftText(mContext?.getString(R.string.word_cancel))
//
//        if (mPost!!.author!!.no == LoginInfoManager.getInstance().user.no) {
//            builder.setContents(mContext?.getString(R.string.word_modified), mContext?.getString(R.string.word_delete))
//        } else {
//            builder.setContents(mContext?.getString(R.string.msg_report))
//        }
//
//
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
//                    1 -> {
//                        if (mPost!!.author!!.no == LoginInfoManager.getInstance().user.no) {
//                            val intent = Intent(mContext, ReviewWriteActivity::class.java)
//                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
//                            intent.putExtra(Const.POST, mPost)
//                            (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
//
//                        } else {
//                            PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.article, mPost!!.no)
//                        }
//                    }
//                    2 -> {
//                        deletePost(mPost!!.no)
//                    }
//                }
//            }
//        }).builder().show(mContext)
//    }
//
//    private fun deletePost(no: Long?) {
//
//        ApiBuilder.create().deletePost(no!!).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//                (mContext as BaseActivity).setResult(RESULT_OK)
//                (mContext as BaseActivity).finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//                ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_review)
//
//            }
//        }).build().call()
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
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