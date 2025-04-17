package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_sale_goods_history.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class BuyHistoryAdapter : RecyclerView.Adapter<BuyHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<Buy>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Buy {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Buy>? {

        return mDataList
    }

    fun add(data: Buy) {

        if (mDataList == null) {
            mDataList = ArrayList<Buy>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Buy>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Buy>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Buy) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Buy>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Buy>) {

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

        holder.text_name.text = item.title

        val buyGoods = item.buyGoodsList!![0]
        var date = ""
        when (item.process) {
            1 -> {//결제완료
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                holder.text_status.setText(R.string.word_pay_complete)

                if (StringUtils.isNotEmpty(buyGoods.payDatetime)) {
                    date = PplusCommonUtil.getDateFormat(buyGoods.payDatetime!!)
                }
            }
            2 -> {//취소
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_purchase_cancel)

                if (StringUtils.isNotEmpty(buyGoods.cancelDatetime)) {
                    date = PplusCommonUtil.getDateFormat(buyGoods.cancelDatetime!!)
                }
            }
            3 -> {//사용완료
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_use_complete)
                if (StringUtils.isNotEmpty(buyGoods.useDatetime)) {
                    date = PplusCommonUtil.getDateFormat(buyGoods.useDatetime!!)
                }
            }
        }

        holder.text_buyer_name.text = date + " " + item.buyerName

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