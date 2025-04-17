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
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoodsOption
//import kotlinx.android.synthetic.main.item_goods_buy_ship_type.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsBuyShipTypeAdapter(buyGoods: BuyGoods) : RecyclerView.Adapter<GoodsBuyShipTypeAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<BuyGoodsOption>? = null
//    var listener: OnItemClickListener? = null
//    var mBuyGoods = buyGoods
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    init {
//        if(buyGoods.buyGoodsOptionSelectList == null || buyGoods.buyGoodsOptionSelectList!!.isEmpty()){
//            mDataList = ArrayList()
//            mDataList!!.add(BuyGoodsOption())
//        }else{
//            mDataList = buyGoods.buyGoodsOptionSelectList!!.toMutableList()
//        }
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): BuyGoodsOption {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<BuyGoodsOption>? {
//
//        return mDataList
//    }
//
//    fun add(data: BuyGoodsOption) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<BuyGoodsOption>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<BuyGoodsOption>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<BuyGoodsOption>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: BuyGoodsOption) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<BuyGoodsOption>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<BuyGoodsOption>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_goods_buy_ship_type
//        val text_goods_title = itemView.text_goods_buy_ship_type_title
//        val text_option = itemView.text_goods_buy_ship_type_option
//        val text_count = itemView.text_goods_buy_ship_type_count
//        val text_delivery_fee = itemView.text_goods_buy_ship_type_delivery_fee
//        val text_price = itemView.text_goods_buy_ship_type_price
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
//        if (mBuyGoods.goods!!.goodsImageList != null && mBuyGoods.goods!!.goodsImageList!!.isNotEmpty()) {
//            Glide.with(holder.itemView.context).load(mBuyGoods.goods!!.goodsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.img_page_profile_default)
//        }
//
//        holder.text_goods_title.text = mBuyGoods.goods!!.name
//        if(mBuyGoods.deliveryFee != null && mBuyGoods.deliveryFee!! > 0){
//            holder.text_delivery_fee.text = holder.itemView.context.getString(R.string.format_ship_price, FormatUtil.getMoneyType(mBuyGoods.deliveryFee.toString()))
//        }else{
//            holder.text_delivery_fee.text = holder.itemView.context.getString(R.string.format_ship_price, holder.itemView.context.getString(R.string.word_free_ship))
//        }
//
//
//        var price = 0
//        if(item.seqNo == null){
//            holder.text_option.visibility = View.GONE
//            price = mBuyGoods.goodsPriceData!!.price!!.toInt() * mBuyGoods.count!!
//            holder.text_count.text = holder.itemView.context.getString(R.string.format_order_count, FormatUtil.getMoneyType(mBuyGoods.count.toString()))
//        }else{
//            holder.text_option.visibility = View.VISIBLE
//            if(StringUtils.isNotEmpty(item.depth3)){
//                holder.text_option.text = "${item.depth1} / ${item.depth2} / ${item.depth3}"
//            }else if(StringUtils.isNotEmpty(item.depth2)){
//                holder.text_option.text = "${item.depth1} / ${item.depth2}"
//            }else{
//                holder.text_option.text = "${item.depth1}"
//            }
//            holder.text_count.text = holder.itemView.context.getString(R.string.format_order_count, FormatUtil.getMoneyType(item.amount.toString()))
//            price = (mBuyGoods.goodsPriceData!!.price!!.toInt() + item.price!!)*item.amount!!
//        }
//
//        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(price.toString()))
//
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_buy_ship_type, parent, false)
//        return ViewHolder(v)
//    }
//}