//package com.pplus.prnumberuser.apps.goods.data
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_goods_order.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsBuyAdapter : RecyclerView.Adapter<GoodsBuyAdapter.ViewHolder> {
//
//    var mDataList: MutableList<BuyGoods>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): BuyGoods {
//
//        return mDataList!!.get(position)
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
//        val image = itemView.image_goods_order
//        val text_goods_title = itemView.text_goods_order_title
//        val text_count = itemView.text_goods_order_count
//        val text_expire_date = itemView.text_goods_order_expire_date
//        val text_dayofweeks = itemView.text_goods_order_dayofweeks
//        val text_use_time = itemView.text_goods_order_use_time
//        val text_price = itemView.text_goods_order_price
//
//        init {
//            text_goods_title.setSingleLine()
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
//        if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        holder.text_goods_title.text = item.goods!!.name
//        holder.text_count.text = holder.itemView.context.getString(R.string.format_order_count, FormatUtil.getMoneyType(item.count.toString()))
//
//
//        val price = item.goods!!.price!! * item.count!!
//        if (StringUtils.isNotEmpty(item.goods!!.expireDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.goods!!.expireDatetime)
//            val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_format_date))
//            holder.text_expire_date.text = holder.itemView.context.getString(R.string.format_expire_date2, output.format(d))
//        }else{
//            holder.text_expire_date.text = holder.itemView.context.getString(R.string.format_expire_date2, holder.itemView.context.getString(R.string.word_buy_after_30))
//        }
//
//        if(item.goods!!.isPlus!! || item.goods!!.isHotdeal!!){
//            holder.text_dayofweeks.visibility = View.VISIBLE
//            holder.text_use_time.visibility = View.VISIBLE
//
//            if (item.goods!!.allDays != null) {
//                if (item.goods!!.allDays!!) {
//                    holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ holder.itemView.context.getString(R.string.word_every_time_use)
//                } else {
//                    holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ item.goods!!.startTime + "~" + item.goods!!.endTime
//                }
//            } else {
//                holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ holder.itemView.context.getString(R.string.word_every_time_use)
//            }
//
//            if (item.goods!!.allWeeks != null) {
//                if (item.goods!!.allWeeks!!) {
//                    holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_every_dayofweek)
//                } else {
//                    val dayOfWeek = item.goods!!.dayOfWeeks!!.replace(",", "/").replace("0", holder.itemView.context.getString(R.string.word_mon)).replace("1", holder.itemView.context.getString(R.string.word_tue))
//                            .replace("2", holder.itemView.context.getString(R.string.word_wed)).replace("3", holder.itemView.context.getString(R.string.word_thu)).replace("4", holder.itemView.context.getString(R.string.word_fri))
//                            .replace("5", holder.itemView.context.getString(R.string.word_sat)).replace("6", holder.itemView.context.getString(R.string.word_sun))
//
//                    holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_use_enable_dayofweek) + " : "+ dayOfWeek
//                }
//            } else {
//                holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_use_enable_dayofweek) + " : "+ holder.itemView.context.getString(R.string.word_every_dayofweek)
//            }
//        }else{
//            holder.text_dayofweeks.visibility = View.GONE
//            holder.text_use_time.visibility = View.GONE
//        }
//
////        if (StringUtils.isNotEmpty(item.goods!!.startTime) && StringUtils.isNotEmpty(item.goods!!.endTime)) {
////            holder.text_use_time.visibility = View.VISIBLE
////            holder.text_use_time.text = holder.itemView.context.getString(R.string.format_use_time, item.goods!!.startTime + " ~ " + item.goods!!.endTime)
////        } else {
////            holder.text_use_time.visibility = View.GONE
////        }
//
////        holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_pay_price, FormatUtil.getMoneyType(price.toString())))
//
////        if (item.goods!!.rewardLuckybol != null && item.goods!!.rewardLuckybol!! > 0) {
////            holder.text_reward.visibility = View.VISIBLE
////            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point3, FormatUtil.getMoneyType(item.goods!!.rewardLuckybol!!.toString())))
////
////        } else {
////            holder.text_reward.visibility = View.GONE
////        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_order, parent, false)
//        return ViewHolder(v)
//    }
//}