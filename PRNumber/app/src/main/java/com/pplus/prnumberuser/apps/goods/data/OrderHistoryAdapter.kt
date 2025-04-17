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
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import kotlinx.android.synthetic.main.item_order_history.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class OrderHistoryAdapter() : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<Buy>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    init {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Buy {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Buy>? {
//
//        return mDataList
//    }
//
//    fun add(data: Buy) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Buy>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Buy>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Buy>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Buy) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Buy>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Buy>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_order_history_image
//        val text_text_title = itemView.text_order_history_info
//        val text_goods_name = itemView.text_order_history_title
//        val text_order_type = itemView.text_order_history_order_type
//        val text_date = itemView.text_order_history_date
//        val text_status = itemView.text_order_history_status
//
//        init {
//            text_text_title.setSingleLine()
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
//
//        holder.text_goods_name.text = item.title
//
//        val goods = item.buyGoodsList!![0].goods
//
//        if (goods != null) {
//            if(goods.page != null){
//                holder.text_text_title.visibility = View.VISIBLE
//                LogUtil.e("LOG", goods.page!!.name)
//                holder.text_text_title.text = goods.page!!.name
//            }else{
//                holder.text_text_title.visibility = View.GONE
//            }
//
//            if (goods.goodsImageList != null && goods.goodsImageList!!.isNotEmpty()) {
//                Glide.with(holder.itemView.context).load(goods.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//
//            } else {
//                holder.image.setImageResource(R.drawable.prnumber_default_img)
//            }
//
//        }
//
//        when(item.orderType){
//            0->{//매장
//                holder.text_order_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff696a))
//                holder.text_order_type.setText(R.string.word_order_store)
//            }
//            1->{//포장
//                holder.text_order_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_a26df3))
//                holder.text_order_type.setText(R.string.word_order_packing)
//            }
//            2->{//배달
//                holder.text_order_type.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_3ec082))
//                holder.text_order_type.setText(R.string.word_order_delivery)
//            }
//        }
//
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//
//        if (StringUtils.isNotEmpty(item.regDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//            date = output.format(d)
//            holder.text_date.text = date
//            holder.text_date.visibility = View.VISIBLE
//        }else{
//            holder.text_date.visibility = View.GONE
//        }
//
//
//        when (item.orderProcess) {
//            EnumData.OrderProcess.ready.process -> {//접수대기
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//                holder.text_status.setText(R.string.word_order_ready)
//            }
//            EnumData.OrderProcess.ing.process -> {//처리중
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//                holder.text_status.setText(R.string.word_order_ing)
//            }
//            EnumData.OrderProcess.complete.process -> {//완료
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//                when(item.orderType){
//                    2->{
//                        holder.text_status.setText(R.string.word_delivery_complete)
//                    }
//                    else->{
//                        holder.text_status.setText(R.string.word_order_complete)
//                    }
//                }
//            }
//            EnumData.OrderProcess.cancel.process -> {//취소
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//                holder.text_status.setText(R.string.word_cancel_complete)
//            }
//            EnumData.OrderProcess.shipping.process -> {//배송중
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            }
//        }
//
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
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
//        return ViewHolder(v)
//    }
//}