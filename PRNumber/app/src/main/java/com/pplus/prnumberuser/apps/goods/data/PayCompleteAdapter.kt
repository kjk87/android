//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_pay_complete.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PayCompleteAdapter : RecyclerView.Adapter<PayCompleteAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<BuyGoods>? = null
//    var mBuy: Buy? = null
//    var listener: OnItemClickListener? = null
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
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
//        val text_goods_name = itemView.text_pay_complete_goods_name
//        val text_count = itemView.text_pay_complete_buy_count
//        val text_price = itemView.text_pay_complete_price
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
//        if(item.goods != null){
//            holder.text_goods_name.text = item.goods!!.name
//            holder.text_count.text = mContext!!.getString(R.string.format_count_unit, FormatUtil.getMoneyType(item.count.toString()))
//            holder.text_price.text = mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
//        }
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pay_complete, parent, false)
//        return ViewHolder(v)
//    }
//}