package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_goods.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SelectGoodsAdapter : RecyclerView.Adapter<SelectGoodsAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0
    var mSelectGoods:Goods? = null

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_goods_image
        val text_name = itemView.text_goods_name
        val text_origin_price = itemView.text_goods_origin_price
        val text_sale_price = itemView.text_goods_sale_price
        val text_reward = itemView.text_goods_reward
        val text_remain_count = itemView.text_goods_remain_count
        val layout_sold_status = itemView.layout_goods_sold_status
        val text_sold_status = itemView.text_goods_sold_status
        val image_select = itemView.image_goods_select
        val image_more = itemView.image_goods_more
        val view_bar = itemView.view_goods_bar
//        var text_send = itemView.text_goods_send
//        var view_bar2 = itemView.view_goods_bar2

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            view_bar.visibility = View.VISIBLE
//            text_send.visibility = View.GONE
//            view_bar2.visibility = View.GONE
            image_more.visibility = View.GONE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Goods = mDataList!![position]

        if (item.status == EnumData.GoodsStatus.soldout.status) {
            holder.layout_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_out)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
        } else if (item.status == EnumData.GoodsStatus.finish.status) {
            holder.layout_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_stop)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.color_ff4646))
            holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
        } else {

            //"expireDatetime":"2018-10-18 15:00:59"

            if (StringUtils.isNotEmpty(item.expireDatetime)) {
                val format = SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault())
                var date = Date()
                try {
                    date = format.parse(item.expireDatetime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val currentMillis = System.currentTimeMillis()
                if (date.time <= currentMillis) {
                    holder.layout_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.setText(R.string.word_sold_finish)
                    holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                } else {
                    holder.layout_sold_status.visibility = View.GONE
                }
            } else {
                holder.layout_sold_status.visibility = View.GONE
            }
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
            holder.text_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_main_goods_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
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

        if(mSelectGoods != null && item.seqNo == mSelectGoods!!.seqNo){
            holder.image_select.visibility = View.VISIBLE
        }else{
            holder.image_select.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false)
        return ViewHolder(v)
    }
}