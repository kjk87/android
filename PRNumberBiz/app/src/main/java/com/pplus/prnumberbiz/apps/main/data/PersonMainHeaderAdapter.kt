package com.pplus.prnumberbiz.apps.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.post.data.PostImagePagerAdapter
import com.pplus.prnumberbiz.apps.post.ui.PostRegActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_page_post.view.*
import kotlinx.android.synthetic.main.item_person_main_header.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PersonMainHeaderAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    //    var mPage: Page? = null
    var mContext: Context? = null
    var mDataList: MutableList<Post>? = null
    var listener: OnItemClickListener? = null
    //    var mSnsList: List<Sns>? = null
    var mTodayYear = 0
    var mTodayMonth = 0
    var mTodayDay = 0
    var mTotalCount = -1

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun refresh()
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

//    fun setSNS(snsList: List<Sns>?) {
//        mSnsList = snsList
//        notifyItemChanged(0)
//    }

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

        mDataList!![position] = data
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
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

    class ViewHeader(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val layout_person_main_not_exist_post = itemView.layout_person_main_not_exist_post
        val text_person_main_not_exist_post_reg = itemView.text_person_main_not_exist_post_reg
        val layout_person_main_post = itemView.layout_person_main_post
        val text_person_main_post_reg = itemView.text_person_main_post_reg

        init {


        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val layout_image = itemView.layout_page_post_image
        val pager_image = itemView.pager_page_post_image
        val indicator_post = itemView.indicator_page_post
        val text_title = itemView.text_page_post_title
        val text_contents = itemView.text_page_post_contents
        val text_regDate = itemView.text_page_post_regDate
        val image_more = itemView.image_page_post_more

        init {
            itemView.layout_page_post_bottom.visibility = View.VISIBLE
            itemView.layout_page_post_detail_bottom.visibility = View.GONE
            text_contents.maxLines = 2
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {
//            val page = LoginInfoManager.getInstance().user.page!!

            if (mTotalCount == 0) {
                holder.layout_person_main_not_exist_post.visibility = View.VISIBLE
                holder.layout_person_main_post.visibility = View.GONE
                holder.text_person_main_not_exist_post_reg.setOnClickListener {
                    val intent = Intent(holder.itemView.context, PostRegActivity::class.java)
                    intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                }
            } else if (mTotalCount > 0) {
                holder.layout_person_main_not_exist_post.visibility = View.GONE
                holder.layout_person_main_post.visibility = View.VISIBLE
                holder.text_person_main_post_reg.setOnClickListener {
                    val intent = Intent(holder.itemView.context, PostRegActivity::class.java)
                    intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                }
            }else{
                holder.layout_person_main_not_exist_post.visibility = View.GONE
                holder.layout_person_main_post.visibility = View.GONE
            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

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
                            listener!!.onItemClick(holder.adapterPosition - 1)
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

            holder.itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onItemClick(holder.adapterPosition - 1)
                }
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

    private val onSnsClickListener = View.OnClickListener { v ->
        val sns = v.tag as Sns
        snsEvent(sns)
    }

    private fun snsEvent(sns: Sns) {
        // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) {
            // 계정으로 이동
            mContext?.startActivity(PplusCommonUtil.getOpenSnsIntent(mContext!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_person_main_header, parent, false))
        } else if (viewType == TYPE_ITEM) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page_post, parent, false))
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0) {
            return TYPE_HEADER
        } else {
            return TYPE_ITEM
        }
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}