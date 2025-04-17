package com.pplus.prnumberbiz.apps.post.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.pages.ui.PostFragment
import com.pplus.prnumberbiz.apps.post.ui.PostRegActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_page_post.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by imac on 2018. 1. 8..
 */
class MyPostAdapter : RecyclerView.Adapter<MyPostAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Post>? = null
    var listener: OnItemClickListener? = null
    var mTodayYear: Int = 0
    var mTodayMonth: Int = 0
    var mTodayDay: Int = 0
    var mFragment:PostFragment? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun refresh()
    }

    constructor(context: Context, fragment:PostFragment) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        mFragment = fragment
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Post {

        return mDataList!![position]
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

        val layout_image = itemView.layout_page_post_image
        val pager_image = itemView.pager_page_post_image
        val indicator_post = itemView.indicator_page_post
        val text_title = itemView.text_page_post_title
        val text_contents = itemView.text_page_post_contents
        val text_regDate = itemView.text_page_post_regDate
        val text_link = itemView.text_page_post_link
        val image_more = itemView.image_page_post_more

        init {
            itemView.layout_page_post_bottom.visibility = View.VISIBLE
            itemView.layout_page_post_detail_bottom.visibility = View.GONE
            text_contents.maxLines = 2
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!!.get(position)

        if (StringUtils.isNotEmpty(item.subject)) {
            holder.text_title.visibility = View.VISIBLE
            holder.text_title.text = item.subject
        } else {
            holder.text_title.visibility = View.GONE
        }

        holder.text_contents.text = item.contents

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
            holder.text_regDate.text = ""
        }

        holder.image_more.setOnClickListener {
            postAlert(item)
        }

        if (item.attachList != null && item.attachList!!.isNotEmpty()) {
//            holder.text_text_title.maxLines = 5
            holder.layout_image.visibility = View.VISIBLE
            holder.pager_image.visibility = View.VISIBLE
            val imageAdapter = PostImagePagerAdapter(holder.itemView.context, item.attachList)
            holder.pager_image.adapter = imageAdapter

            if (item.attachList!!.size > 1) {
                holder.indicator_post.visibility = View.VISIBLE
            } else {
                holder.indicator_post.visibility = View.GONE
            }

            holder.indicator_post.removeAllViews()
            holder.indicator_post.build(LinearLayout.HORIZONTAL, item.attachList!!.size)
            holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                    holder.indicator_post.setCurrentItem(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            imageAdapter.setListener(object : PostImagePagerAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (listener != null) {
                        listener!!.onItemClick(holder.adapterPosition)
                    }
                }
            })
        } else {
            holder.indicator_post.removeAllViews()
            holder.indicator_post.visibility = View.GONE
            holder.pager_image.visibility = View.GONE
            holder.pager_image.adapter = null
            holder.layout_image.visibility = View.GONE
        }

        if(StringUtils.isNotEmpty(item.articleUrl)){
            holder.text_link.visibility = View.VISIBLE
        }else{
            holder.text_link.visibility = View.GONE
        }

        holder.text_link.setOnClickListener {
            if(StringUtils.isNotEmpty(item.articleUrl)){
                PplusCommonUtil.openChromeWebView(holder.itemView.context, item.articleUrl!!)
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    private fun postAlert(post: Post) {

        if (post == null) {
            return
        }
        val builder = AlertBuilder.Builder()
        builder.setLeftText(mContext?.getString(R.string.word_cancel))

        builder.setContents(mContext?.getString(R.string.word_modified), mContext?.getString(R.string.word_delete))

        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert.getValue()) {
                    1 -> {
                        val intent = Intent(mContext, PostRegActivity::class.java)
                        intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                        intent.putExtra(Const.DATA, post)
                        (mContext as Activity).startActivityForResult(intent, Const.REQ_MODIFY)
                    }
                    2 -> AlertBuilder.Builder().setContents(mContext?.getString(R.string.msg_question_delete_post)).setLeftText(mContext?.getString(R.string.word_cancel)).setRightText(mContext?.getString(R.string.word_confirm)).setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> deletePost(post.no)
                            }
                        }
                    }).builder().show(mContext)
                }
            }
        }).builder().show(mContext)
    }

    private fun deletePost(no: Long?) {

        ApiBuilder.create().deletePost(no).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                if (listener != null) {
                    listener!!.refresh()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_post)
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_page_post, parent, false)
        return ViewHolder(v)
    }
}