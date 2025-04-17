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
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.post.ui.ReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_review.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Post>? = null
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
//        val image_profile = itemView.image_review_profile
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
//            text_contents.maxLines = 5
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
//        holder.text_name.text = item.author!!.nickname
//
//        holder.text_contents.text = item.contents
//        holder.text_comment.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_reply, FormatUtil.getMoneyType(item.commentCount.toString())))
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
//        if (item.attachList != null && item.attachList!!.size > 0) {
//            holder.text_contents.maxLines = 2
//            holder.layout_image.visibility = View.VISIBLE
//            holder.pager_image.visibility = View.VISIBLE
//            val imageAdapter = PostImagePagerAdapter(holder.itemView.context, item.attachList!!)
//            holder.pager_image.adapter = imageAdapter
//            holder.indicator.visibility = View.VISIBLE
//            holder.indicator.removeAllViews()
//            holder.indicator.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
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
//            imageAdapter.setListener(object : PostImagePagerAdapter.OnItemClickListener{
//                override fun onItemClick(position: Int) {
//                    if (listener != null) {
//                        listener!!.onItemClick(holder.adapterPosition)
//                    }
//                }
//            })
//
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
////            imageAdapter.setListener {
////                if (listener != null) {
////                    listener!!.onItemClick(holder.adapterPosition)
////                }
////            }
//        } else {
//            holder.indicator.removeAllViews()
//            holder.indicator.visibility = View.GONE
//            holder.pager_image.visibility = View.GONE
//            holder.pager_image.adapter = null
//            holder.layout_image.visibility = View.GONE
//        }
//
//        holder.image_more.setOnClickListener {
//            postAlert(item, holder.adapterPosition)
//        }
//
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//
//    }
//
//    private fun postAlert(post: Post, position: Int) {
//
//        val builder = AlertBuilder.Builder()
//        builder.setLeftText(mContext?.getString(R.string.word_cancel))
//
//        if (post.author!!.no == LoginInfoManager.getInstance().user.no) {
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
//                        if (post.author!!.no == LoginInfoManager.getInstance().user.no) {
//                            val intent = Intent(mContext, ReviewWriteActivity::class.java)
//                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
//                            intent.putExtra(Const.POST, post)
//                            (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
//                        } else {
//                            PplusCommonUtil.report(mContext!!, EnumData.REPORT_TYPE.article, post.no)
//                        }
//                    }
//                    2 -> {
//                        deletePost(post.no, position)
//                    }
//                }
//            }
//        }).builder().show(mContext)
//    }
//
//    private fun deletePost(no: Long?, position: Int) {
//
//        ApiBuilder.create().deletePost(no!!).setCallback(object : PplusCallback<NewResultResponse<Any>> {
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
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
//        return ViewHolder(v)
//    }
//}