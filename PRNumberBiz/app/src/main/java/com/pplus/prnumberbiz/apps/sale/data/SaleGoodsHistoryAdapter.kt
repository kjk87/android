package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import kotlinx.android.synthetic.main.item_sale_goods_history.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SaleGoodsHistoryAdapter : RecyclerView.Adapter<SaleGoodsHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<BuyGoods>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

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

    fun getItem(position: Int): BuyGoods {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<BuyGoods>? {

        return mDataList
    }

    fun add(data: BuyGoods) {

        if (mDataList == null) {
            mDataList = ArrayList<BuyGoods>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuyGoods>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<BuyGoods>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuyGoods) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuyGoods>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuyGoods>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_name = itemView.text_sale_goods_name
        val text_buyer_name = itemView.text_sale_goods_buyer_name
        val text_status = itemView.text_sale_goods_status

        init {
            text_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_name.text = item.goods!!.name
        holder.text_buyer_name.text = item.buy!!.buyerName

        when (item.process) {
            1 -> {//결제완료
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                holder.text_status.setText(R.string.word_pay_complete)
            }
            2 -> {//취소
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_purchase_cancel)
            }
            3 -> {//사용완료
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_use_complete)
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_goods_history, parent, false)
        return ViewHolder(v)
    }
}