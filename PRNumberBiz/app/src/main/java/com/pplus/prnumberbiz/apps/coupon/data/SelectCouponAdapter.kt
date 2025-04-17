package com.pplus.prnumberbiz.apps.coupon.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_coupon_config.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SelectCouponAdapter : RecyclerView.Adapter<SelectCouponAdapter.ViewHolder> {

    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null
    var mSelectGoods:Goods? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val layout_top = itemView.layout_coupon_top
        val text_reg_date = itemView.text_coupon_reg_date
        val layout_coupon = itemView.layout_coupon
        val image = itemView.image_coupon_page_image
        val text_name = itemView.text_coupon_name
        val text_origin_price = itemView.text_coupon_origin_price
        val text_sale_price = itemView.text_coupon_sale_price
        val view_sold_status = itemView.view_coupon_sold_status
        val text_sold_status = itemView.text_coupon_sold_status
        val text_expire_date = itemView.text_coupon_expire_date
        val image_select = itemView.image_coupon_select
//        var text_send = itemView.text_goods_send
//        var view_bar2 = itemView.view_goods_bar2

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            layout_top.visibility = View.GONE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]


        var ing = false

        if (item.status == EnumData.GoodsStatus.soldout.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_out)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
        } else if (item.status == EnumData.GoodsStatus.finish.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_finish)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
        } else if (item.status == EnumData.GoodsStatus.stop.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_stop)
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_stop)
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
                    holder.view_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.setText(R.string.word_sold_finish)
                    holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
                } else {
                    holder.view_sold_status.visibility = View.GONE
                    holder.text_sold_status.visibility = View.GONE
                    ing = true
                }
            } else {
                holder.view_sold_status.visibility = View.GONE
                holder.text_sold_status.visibility = View.GONE
                ing = true
            }
        }

        if (ing) {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
            if(item.represent != null && item.represent!!){
                holder.text_sold_status.visibility = View.VISIBLE
                holder.text_sold_status.setText(R.string.word_represent_coupon)
                holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_representation)
            }else{
                holder.text_sold_status.visibility = View.GONE
            }
        } else {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
        }

        if (StringUtils.isNotEmpty(item.expireDatetime)) {
            holder.text_expire_date.visibility = View.VISIBLE
            holder.text_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(PPLUS_DATE_FORMAT.parse(item.expireDatetime)) + " " + holder.itemView.context.getString(R.string.word_until)
        }else{
            holder.text_expire_date.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(item.regDatetime)) {
            holder.text_reg_date.visibility = View.VISIBLE
            holder.text_reg_date.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
        }else{
            holder.text_reg_date.visibility = View.GONE
        }

        holder.text_name.text = item.name
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
            Glide.with(holder.itemView.context).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_page_profile_circle_default)
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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_config, parent, false)
        return ViewHolder(v)
    }
}