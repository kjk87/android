package com.pplus.prnumberbiz.apps.post.data

import android.content.Context
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Post
import kotlinx.android.synthetic.main.item_review.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by imac on 2018. 1. 8..
 */
class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Post>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Post {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Post>? {

        return mDataList
    }

    fun add(data: Post) {

        if (mDataList == null) {
            mDataList = ArrayList<Post>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Post>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Post>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Post) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Post>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Post>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_review_profileImage
        val text_name = itemView.text_review_name
        val layout_image = itemView.layout_review_image
        val pager_image = itemView.pager_review_image
        val indicator = itemView.indicator_review
        val text_contents = itemView.text_review_contents
        val text_regDate = itemView.text_review_regDate
        val text_comment = itemView.text_review_comment

        init {
            text_contents.maxLines = 5
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Post = mDataList!!.get(position);

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
//            holder.text_contents.maxLines = 2
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
                if (listener != null) {
                    listener!!.onItemClick(holder.getAdapterPosition())
                }
            }
        } else {
            holder.indicator.removeAllViews()
            holder.indicator.visibility = View.GONE
            holder.pager_image.visibility = View.GONE
            holder.pager_image.adapter = null
            holder.layout_image.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(v)
    }
}