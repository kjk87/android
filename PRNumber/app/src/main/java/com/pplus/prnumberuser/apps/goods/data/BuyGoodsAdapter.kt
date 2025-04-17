//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_buy_goods.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class BuyGoodsAdapter : RecyclerView.Adapter<BuyGoodsAdapter.ViewHolder> {
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
//        val image = itemView.image_buy_goods_image
//        val text_goods_name = itemView.text_buy_goods_goods_name
//        val text_expire_date = itemView.text_buy_goods_expire_date
//        val text_dayofweeks = itemView.text_buy_goods_dayofweeks
//        val text_use_time = itemView.text_buy_goods_use_time
//        val text_count = itemView.text_buy_goods_count
//        val text_price = itemView.text_buy_goods_price
//
//        init {
//            text_goods_name.setSingleLine()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//        if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//        holder.text_goods_name.text = item.title
//        holder.text_count.text = holder.itemView.context.getString(R.string.format_count_unit3, FormatUtil.getMoneyType(item.count.toString()))
//        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
//
//        if (StringUtils.isNotEmpty(item.expireDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.expireDatetime)
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
//            if (item.allDays != null) {
//                if (item.allDays!!) {
//                    holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ holder.itemView.context.getString(R.string.word_every_time_use)
//                } else {
//                    holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ item.startTime + "~" + item.endTime
//                }
//            } else {
//                holder.text_use_time.text = holder.itemView.context.getString(R.string.word_use_enable_time) + " : "+ holder.itemView.context.getString(R.string.word_every_time_use)
//            }
//
//            if (item.allWeeks != null) {
//                if (item.allWeeks!!) {
//                    holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_use_enable_dayofweek) + ": " + holder.itemView.context.getString(R.string.word_every_dayofweek)
//                } else {
//                    val dayOfWeek = item.dayOfWeeks!!.replace(",", "/").replace("0", holder.itemView.context.getString(R.string.word_mon)).replace("1", holder.itemView.context.getString(R.string.word_tue))
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
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_goods, parent, false)
//        return ViewHolder(v)
//    }
//}