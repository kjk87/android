package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsRegActivity2
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_goods.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class GoodsAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun onRefresh()
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Goods {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Goods>? {

        return mDataList
    }

    fun add(data: Goods) {

        if (mDataList == null) {
            mDataList = ArrayList<Goods>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Goods>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Goods>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Goods) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Goods>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Goods>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_goods_image
        val text_name = itemView.text_goods_name
        val text_origin_price = itemView.text_goods_origin_price
        val text_sale_price = itemView.text_goods_sale_price
        val text_reward = itemView.text_goods_reward
        val text_remain_count = itemView.text_goods_remain_count
        val layout_sold_status = itemView.layout_goods_sold_status
        val text_sold_status = itemView.text_goods_sold_status
        val image_goods_more = itemView.image_goods_more
        val view_bar = itemView.view_goods_bar
//        var text_send = itemView.text_goods_send
//        var view_bar2 = itemView.view_goods_bar2

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            view_bar.visibility = View.GONE
//            text_send.visibility = View.VISIBLE
//            view_bar2.visibility = View.VISIBLE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        var ing = false

        if (item.status == EnumData.GoodsStatus.soldout.status) {
            holder.layout_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_out)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
            holder.image_goods_more.visibility = View.GONE
        } else if (item.status == EnumData.GoodsStatus.finish.status) {
            holder.layout_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_finish)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
            holder.image_goods_more.visibility = View.GONE
        } else if (item.status == EnumData.GoodsStatus.stop.status) {
            holder.layout_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_stop)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.color_ff4646))
            holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
            holder.image_goods_more.visibility = View.VISIBLE
        } else {

            //"expireDatetime":"2018-10-18 15:00:59"

            if (StringUtils.isNotEmpty(item.expireDatetime)) {
                val format = SimpleDateFormat(PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                var date = Date()
                try {
                    date = format.parse(item.expireDatetime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val currentMillis = System.currentTimeMillis()
                if (date.time <= currentMillis) {
                    holder.image_goods_more.visibility = View.GONE
                    holder.layout_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.setText(R.string.word_sold_finish)
                    holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                    holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                } else {
                    holder.layout_sold_status.visibility = View.GONE
                    holder.image_goods_more.visibility = View.VISIBLE
                    ing = true
                }
            } else {
                holder.layout_sold_status.visibility = View.GONE
                holder.image_goods_more.visibility = View.VISIBLE
                ing = true
            }
        }

        if (ing) {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.text_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
//            holder.text_send.setBackgroundResource(R.drawable.btn_plus_event_nor)
//            holder.text_send.setOnClickListener {
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//
//                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
//                builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_alert_goods_news), AlertBuilder.MESSAGE_TYPE.TEXT, 5))
//
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//                    override fun onCancel() {
//
//                    }
//
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val params = HashMap<String, String>()
//
//                                params["seqNo"] = item.seqNo.toString()
//                                ApiBuilder.create().goodsNews(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                                        (holder.itemView.context as BaseActivity).showAlert(R.string.msg_sent_goods_news)
//                                    }
//
//                                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                                        (holder.itemView.context as BaseActivity).showAlert(R.string.msg_alert_error_goods_news, 5)
//                                    }
//                                }).build().call()
//                            }
//                        }
//                    }
//                }).builder().show(holder.itemView.context)
//            }
        } else {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            holder.text_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            holder.text_send.setBackgroundResource(R.drawable.btn_plus_event_dim)
//            holder.text_send.setOnClickListener(null)
        }

        holder.text_name.text = item.name
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        holder.text_sale_price.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        if (item.rewardLuckybol != null && item.rewardLuckybol!! > 0) {
            holder.text_reward.visibility = View.VISIBLE
            holder.text_reward.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_reward_point, FormatUtil.getMoneyType(item.rewardLuckybol!!.toString())))

        } else {
            holder.text_reward.visibility = View.GONE
        }

        if (item.count != null && item.count != -1) {
            var soldCount = 0
            if (item.soldCount != null) {
                soldCount = item.soldCount!!
            }
            holder.text_remain_count.visibility = View.VISIBLE
            holder.text_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
        } else {
            holder.text_remain_count.visibility = View.GONE
        }

        if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
            if (mContext != null) {
                val id = item.attachments!!.images!![0]
                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
            }

        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        holder.image_goods_more.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)

            when (item.status) {
                EnumData.GoodsStatus.ing.status -> {
                    builder.setContents(holder.itemView.context.getString(R.string.word_sold_stop), holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                }
                EnumData.GoodsStatus.stop.status -> {
                    builder.setContents(holder.itemView.context.getString(R.string.word_sold_resume), holder.itemView.context.getString(R.string.word_delete))
                }
                else -> {
                    builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                }
            }

//            builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST -> {

                            when (item.status) {
                                EnumData.GoodsStatus.ing.status, EnumData.GoodsStatus.stop.status -> {
                                    when (event_alert.value) {
                                        1 -> {
                                            putGoodsStatus(item, holder.adapterPosition)
                                        }
                                        2 -> {
                                            val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                            intent.putExtra(Const.DATA, item)
                                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                        }
                                        3 -> {
                                            delete(item)
                                        }
                                    }
                                }
                                else -> {
                                    when (event_alert.value) {
                                        1 -> {
                                            val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                            intent.putExtra(Const.DATA, item)
                                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                        }
                                        2 -> {
                                            delete(item)
                                        }
                                    }
                                }
                            }


                        }
                    }
                }
            }).builder().show(holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    private fun putGoodsStatus(goods: Goods, position: Int) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)

        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        if (goods.status == EnumData.GoodsStatus.ing.status) {

            builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_alert_sale_stop1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        } else if (goods.status == EnumData.GoodsStatus.stop.status) {
            builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_alert_sale_resume1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        }
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = goods.seqNo.toString()
                        if (goods.status == EnumData.GoodsStatus.ing.status) {
                            params["status"] = EnumData.GoodsStatus.stop.status.toString()
                        } else if (goods.status == EnumData.GoodsStatus.stop.status) {
                            params["status"] = EnumData.GoodsStatus.ing.status.toString()
                        }
                        (mContext as BaseActivity).showProgress("")
                        ApiBuilder.create().putGoodsStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                                mDataList!![position].status = params["status"]!!.toInt()
                                notifyItemChanged(position)
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }

    private fun delete(goods: Goods) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_question_delete_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = goods.seqNo.toString()
                        (mContext as BaseActivity).showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                                ToastUtil.show(mContext, mContext!!.getString(R.string.msg_deleted_goods))
                                if (listener != null) {
                                    listener!!.onRefresh()
                                }
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()

                                ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_history_goods)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false)
        return ViewHolder(v)
    }
}