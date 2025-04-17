//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.GoodsLike
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_main_hot_deal_ship_type.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsLikeShippingAdapter : RecyclerView.Adapter<GoodsLikeShippingAdapter.ViewHolder> {
//
//    var mDataList: MutableList<GoodsLike>? = null
//    var listener: OnItemClickListener? = null
//    var deleteListener: OnItemDeleteListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    interface OnItemDeleteListener {
//
//        fun onItemDelete()
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
//    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {
//
//        this.deleteListener = listener
//    }
//
//    fun getItem(position: Int): GoodsLike {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsLike>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsLike) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<GoodsLike>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsLike>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<GoodsLike>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsLike) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<GoodsLike>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsLike>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_main_hot_deal_ship_type_image
//        val text_save_point = itemView.text_main_hot_deal_ship_type_save_point
//        val text_goods_name = itemView.text_main_hot_deal_ship_type_name
//        val text_discount_ratio = itemView.text_main_hot_deal_ship_type_discount_ratio
//        val text_sale_price = itemView.text_main_hot_deal_ship_type_sale_price
//        val text_origin_price = itemView.text_main_hot_deal_ship_type_origin_price
//        val layout_count = itemView.layout_main_hot_deal_ship_type_count
//        val text_remain_count = itemView.text_main_hot_deal_ship_type_remain_count
//        val text_count = itemView.text_main_hot_deal_ship_type_count
//        val layout_rate_total = itemView.layout_main_hot_deal_ship_type_rate_total
//        val layout_rate = itemView.layout_main_hot_deal_ship_type_rate
//        val image_like = itemView.image_main_hot_deal_ship_type_like
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            image_like.visibility = View.VISIBLE
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
//        val goodsPrice = item.goodsPrice!!
//
//        if (goodsPrice.goods!!.goodsImageList != null && goodsPrice.goods!!.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(goodsPrice.goods!!.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
//        if(goodsPrice.isLuckyball != null && goodsPrice.isLuckyball!!){
//            holder.text_save_point.visibility = View.VISIBLE
//            holder.layout_count.visibility = View.VISIBLE
//        }else{
//            holder.text_save_point.visibility = View.GONE
//            holder.layout_count.visibility = View.GONE
//        }
//
//        if (goodsPrice.goods!!.count != -1) {
//            holder.layout_rate_total.visibility = View.VISIBLE
//            holder.text_count.visibility = View.VISIBLE
//            holder.text_remain_count.visibility = View.VISIBLE
//            var soldCount = 0
//            if (goodsPrice.goods!!.soldCount != null) {
//                soldCount = goodsPrice.goods!!.soldCount!!
//            }
//
//            holder.text_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_count_per2, soldCount.toString(), goodsPrice.goods!!.count.toString()))
//            holder.text_remain_count.text = holder.itemView.context.getString(R.string.format_remain_count, (goodsPrice.goods!!.count!! - soldCount).toString())
//
//            var weightSum = 1
//            if (goodsPrice.goods!!.count!! > 0) {
//                weightSum = goodsPrice.goods!!.count!!
//            }
//            holder.layout_rate_total.weightSum = weightSum.toFloat()
//            val layoutParams = holder.layout_rate.layoutParams
//
//            if (layoutParams is LinearLayout.LayoutParams) {
//                layoutParams.weight = goodsPrice.goods!!.soldCount!!.toFloat()
//            }
//            holder.layout_rate.requestLayout()
//
//        } else {
//            holder.text_remain_count.visibility = View.GONE
//            holder.layout_rate_total.visibility = View.GONE
//            holder.text_count.visibility = View.GONE
//        }
//
//        holder.text_save_point.text = holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType((goodsPrice.price!!*0.1f).toInt().toString()))
//
//        holder.text_goods_name.text = goodsPrice.goods!!.name
//
//        if (goodsPrice.originPrice != null && goodsPrice.originPrice!! > 0) {
//
//            if (goodsPrice.originPrice!! <= goodsPrice.price!!) {
//                holder.text_origin_price.visibility = View.GONE
//            } else {
//                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(goodsPrice.originPrice.toString()))
//                holder.text_origin_price.visibility = View.VISIBLE
//            }
//
//        } else {
//            holder.text_origin_price.visibility = View.GONE
//        }
//
//        if (goodsPrice.discountRatio != null && goodsPrice.discountRatio!! > 0) {
//            holder.text_discount_ratio.visibility = View.VISIBLE
//            holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit, goodsPrice.discountRatio!!.toInt().toString()))
//        } else {
//            holder.text_discount_ratio.visibility = View.GONE
//        }
//        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(goodsPrice.price.toString())))
//
//        holder.image_like.setOnClickListener{
//            deleteLike(holder.itemView.context, item)
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition)
//            }
//        }
//    }
//
//    private fun deleteLike(context:Context, item:GoodsLike) {
//        val goodsLike = GoodsLike()
//        goodsLike.memberSeqNo = LoginInfoManager.getInstance().user.no
//        goodsLike.goodsSeqNo = item.goods!!.seqNo
//        goodsLike.goodsPriceSeqNo = item.goodsPrice!!.seqNo
//        goodsLike.pageSeqNo = item.pageSeqNo
//        (context as BaseActivity).showProgress("")
//        ApiBuilder.create().deleteGoodsLike(goodsLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                (context as BaseActivity). hideProgress()
//                ToastUtil.show(context, R.string.msg_delete_goods_like)
//                if(deleteListener != null){
//                    deleteListener!!.onItemDelete()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                (context as BaseActivity).hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_hot_deal_ship_type, parent, false)
//        return ViewHolder(v)
//    }
//}