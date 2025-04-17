//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_buy_shipping_history.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class BuyShippingHistoryAdapter : RecyclerView.Adapter<BuyShippingHistoryAdapter.ViewHolder> {
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
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<BuyGoods>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
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
//        mDataList = ArrayList()
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
//        val image = itemView.image_buy_shipping_history_image
//        val text_page_name = itemView.text_buy_shipping_history_page_name
//        val text_text_title = itemView.text_buy_shipping_history_title
//        val text_date = itemView.text_buy_shipping_history_date
//        val text_review = itemView.text_buy_shipping_history_review
//        val text_status = itemView.text_buy_shipping_history_status
//        val text_price = itemView.text_buy_shipping_history_price
//
//        init {
//            text_text_title.setSingleLine()
//            text_page_name.setSingleLine()
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
//        if(item.goods!!.page != null){
//            holder.text_page_name.visibility = View.VISIBLE
//            holder.text_page_name.text = item.goods!!.page!!.name
//
//            if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//                Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
//            } else {
//                holder.image.setImageResource(R.drawable.img_page_profile_default)
//            }
//        }else{
//            holder.text_page_name.visibility = View.GONE
//        }
//
//        holder.text_text_title.text = item.title
//        holder.text_review.visibility = View.GONE
//        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        when (item.process) {
//            EnumData.BuyGoodsProcess.PAY.process -> {
//
//
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//
//                when(item.orderProcess){
//                    EnumData.BuyGoodsOrderProcess.ready.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//                        holder.text_status.setText(R.string.word_pay_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.confirm.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//                        holder.text_status.setText(R.string.word_preparing_goods)
//                    }
//                    EnumData.BuyGoodsOrderProcess.shipping.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
//                        holder.text_status.setText(R.string.word_shipping)
//                    }
//                    EnumData.BuyGoodsOrderProcess.shipping_complete.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//                        holder.text_status.setText(R.string.word_shipping_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.refund_wait.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//                        holder.text_status.setText(R.string.word_refund_request)
//                    }
//                    EnumData.BuyGoodsOrderProcess.refund.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//                        holder.text_status.setText(R.string.word_refund_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.exchange_wait.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//                        holder.text_status.setText(R.string.word_exchange_request)
//                    }
//                    EnumData.BuyGoodsOrderProcess.exchange.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//                        holder.text_status.setText(R.string.word_exchange_complete)
//                    }
//                    EnumData.BuyGoodsOrderProcess.complete.process->{
//                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//                        holder.text_status.setText(R.string.word_confirm_buy)
//                        holder.text_review.visibility = View.VISIBLE
//                        if(item.isReviewExist != null && item.isReviewExist!!){
//                            holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
//                            holder.text_review.setText(R.string.word_review_write_complete)
//                            holder.text_review.setOnClickListener { }
//                        }else{
//                            holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
//                            if(item.reviewPoint != null && item.reviewPoint!! > 0){
//                                holder.text_review.text = holder.itemView.context.getString(R.string.format_review_write_point, FormatUtil.getMoneyType(item.reviewPoint.toString()))
//                            }else{
//                                holder.text_review.setText(R.string.word_review_write)
//                            }
//
//                            holder.text_review.setOnClickListener {
//                                val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
//                                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                                intent.putExtra(Const.BUY_GOODS, item)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//                            }
//
//                        }
//                    }
//                }
//            }
//            EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.BIZ_CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
//                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.cancelDatetime)
//                    date = output.format(d)
//                }
//
//                holder.text_status.setText(R.string.word_cancel_complete)
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            }
//        }
//
//        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//        date = output.format(d)
//        if (StringUtils.isNotEmpty(date)) {
//            holder.text_date.text = date
//            holder.text_date.visibility = View.VISIBLE
//        } else {
//            holder.text_date.visibility = View.GONE
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position)
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_shipping_history, parent, false)
//        return ViewHolder(v)
//    }
//}