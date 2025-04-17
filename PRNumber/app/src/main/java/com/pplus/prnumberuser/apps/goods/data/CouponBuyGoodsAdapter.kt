//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_coupon_buy_goods.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class CouponBuyGoodsAdapter : RecyclerView.Adapter<CouponBuyGoodsAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<BuyGoods>? = null
//    var listener: OnItemClickListener? = null
//    var mBuy: Buy? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//
//        fun onRefresh()
//    }
//
//    constructor(context: Context, buy: Buy) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.mBuy = buy
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): BuyGoods {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<BuyGoods>? {
//
//        return mDataList
//    }
//
//    fun add(data: BuyGoods) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<BuyGoods>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<BuyGoods>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<BuyGoods>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: BuyGoods) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<BuyGoods>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<BuyGoods>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//
//        val text_name = itemView.text_coupon_buy_goods_coupon_name
//        val text_expire_date = itemView.text_coupon_buy_goods_expire_date
//        val layout_use_time = itemView.layout_coupon_buy_goods_use_time
//        val text_use_time = itemView.text_coupon_buy_goods_use_time
//        val layout_use_dayofweek = itemView.layout_coupon_buy_goods_use_dayofweek
//        val text_use_dayofweek = itemView.text_coupon_buy_goods_use_dayofweek
//        val layout_use_condition = itemView.layout_coupon_buy_goods_use_condition
//        val text_use_condition = itemView.text_coupon_buy_goods_use_condition
//        val text_count = itemView.text_coupon_buy_goods_count
//
//        init {
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
//        holder.text_name.text = item.title
//
//        if (StringUtils.isNotEmpty(item.expireDatetime)) {
//            holder.text_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.expireDatetime)) + " " + holder.itemView.context.getString(R.string.word_until)
//        }
//
//        if (item.allDays != null) {
//            holder.layout_use_time.visibility = View.VISIBLE
//            if (item.allDays!!) {
//                holder.text_use_time.text = holder.itemView.context.getString(R.string.word_every_time_use)
//            } else {
//                holder.text_use_time.text = item.startTime + "~" + item.endTime
//            }
//        }else{
//            holder.layout_use_time.visibility = View.GONE
//        }
//
//        if (item.allWeeks != null) {
//            holder.layout_use_dayofweek.visibility = View.VISIBLE
//            if (item.allWeeks!!) {
//                holder.text_use_dayofweek.text = holder.itemView.context.getString(R.string.word_every_dayofweek)
//            } else {
//                val dayOfWeek = item.dayOfWeeks!!.replace(",", "/").replace("0", holder.itemView.context.getString(R.string.word_mon)).replace("1", holder.itemView.context.getString(R.string.word_tue))
//                        .replace("2", holder.itemView.context.getString(R.string.word_wed)).replace("3", holder.itemView.context.getString(R.string.word_thu)).replace("4", holder.itemView.context.getString(R.string.word_fri))
//                        .replace("5", holder.itemView.context.getString(R.string.word_sat)).replace("6", holder.itemView.context.getString(R.string.word_sun))
//
//                holder.text_use_dayofweek.text = dayOfWeek
//            }
//        } else {
//            holder.layout_use_dayofweek.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(item.serviceCondition)) {
//            holder.layout_use_condition.visibility = View.VISIBLE
//            holder.text_use_condition.text = item.serviceCondition
//        } else {
//            holder.layout_use_condition.visibility = View.GONE
//        }
//        holder.text_count.text = holder.itemView.context.getString(R.string.format_count_unit3, FormatUtil.getMoneyType(item.count.toString()))
//
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position)
//            }
//        }
//    }
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_buy_goods, parent, false)
//        return ViewHolder(v)
//    }
//}