package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.ReviewReplyActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_goods_review.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

/**
 * Created by imac on 2018. 1. 8..
 */
class GoodsReviewAdapter : RecyclerView.Adapter<GoodsReviewAdapter.ViewHolder> {

    var mDataList: MutableList<GoodsReview>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): GoodsReview {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<GoodsReview>? {

        return mDataList
    }

    fun add(data: GoodsReview) {

        if (mDataList == null) {
            mDataList = ArrayList<GoodsReview>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GoodsReview>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<GoodsReview>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GoodsReview) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<GoodsReview>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GoodsReview>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image_profile = itemView.image_goods_review_profile
        val text_name = itemView.text_goods_review_name
        val layout_image = itemView.layout_goods_review_image
        val pager_image = itemView.pager_goods_review_image
        val indicator = itemView.indicator_goods_review
        val text_contents = itemView.text_goods_review_contents
        val text_regDate = itemView.text_goods_review_regDate
        val grade_bar = itemView.grade_bar_goods_review
        val text_reply_reg = itemView.text_goods_review_reply_reg
        val layout_reply = itemView.layout_goods_review_reply
        val text_reply_contents = itemView.text_goods_review_reply_contents
        val text_reply_date = itemView.text_goods_review_reply_date
        val text_reply_modify = itemView.text_goods_review_reply_modify
        val text_reply_delete = itemView.text_goods_review_reply_delete

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if (item.member != null) {
            holder.text_name.text = item.member!!.nickname
            if (item.member!!.profileAttachment != null) {
                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
            } else {
                holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
            }
        }

        holder.text_contents.text = item.review

        val eval = String.format("%.1f", item.eval!!.toFloat())
        holder.grade_bar.build(eval)


        if (StringUtils.isNotEmpty(item.reviewReply)) {
            holder.text_reply_reg.visibility = View.GONE
            holder.layout_reply.visibility = View.VISIBLE
            holder.text_reply_contents.text = item.reviewReply
            holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
            holder.text_reply_modify.setOnClickListener {
                val intent = Intent(holder.itemView.context, ReviewReplyActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REPLY)
            }
            holder.text_reply_delete.setOnClickListener {
                val builder = AlertBuilder.Builder()
                builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_question_delete_review_of_reply), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                item.reviewReply = null
                                (holder.itemView.context as BaseActivity).showProgress("")
                                ApiBuilder.create().putGoodsReviewReply(item).setCallback(object : PplusCallback<NewResultResponse<GoodsReview>> {
                                    override fun onResponse(call: Call<NewResultResponse<GoodsReview>>?, response: NewResultResponse<GoodsReview>?) {
                                        (holder.itemView.context as BaseActivity).hideProgress()
                                        notifyItemChanged(holder.adapterPosition)
                                    }

                                    override fun onFailure(call: Call<NewResultResponse<GoodsReview>>?, t: Throwable?, response: NewResultResponse<GoodsReview>?) {
                                        (holder.itemView.context as BaseActivity).hideProgress()
                                    }
                                }).build().call()
                            }
                        }
                    }
                }).builder().show(holder.itemView.context)
            }
        } else {
            holder.layout_reply.visibility = View.GONE
            holder.text_reply_reg.visibility = View.VISIBLE
            holder.text_reply_reg.setOnClickListener {
                val intent = Intent(holder.itemView.context, ReviewReplyActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REPLY)
            }
        }

        try {
            holder.text_regDate.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
        } catch (e: Exception) {

        }

        if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
            holder.layout_image.visibility = View.VISIBLE
            holder.pager_image.visibility = View.VISIBLE
            val imageAdapter = GoodsImagePagerAdapter(holder.itemView.context)
            imageAdapter.dataList = item.attachments!!.images!! as ArrayList<String>
            holder.pager_image.adapter = imageAdapter
            holder.indicator.visibility = View.VISIBLE
            holder.indicator.removeAllViews()
            holder.indicator.build(LinearLayout.HORIZONTAL, item.attachments!!.images!!.size)
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
            imageAdapter.setListener(object : GoodsImagePagerAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (listener != null) {
                        listener!!.onItemClick(holder.adapterPosition)
                    }
                }
            })
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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_review, parent, false)
        return ViewHolder(v)
    }
}