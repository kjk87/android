package com.pplus.prnumberbiz.apps.coupon.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_coupon_sale_history.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CouponSaleHistoryAdapter : RecyclerView.Adapter<CouponSaleHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<BuyGoods>? = null
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

        val text_pay_type = itemView.text_coupon_sale_history_pay_type
        val text_price = itemView.text_coupon_sale_history_price
        val text_date = itemView.text_coupon_sale_history_date_name


        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!!.get(position)
        var payMethod = ""

        if (item.buy != null) {
            when (item.buy!!.payMethod) {
                "card" -> {
                    payMethod = holder.itemView.context.getString(R.string.word_credit_card)
                }
                "BANK" -> {
                    payMethod = holder.itemView.context.getString(R.string.word_real_time_transfer)
                }
            }
            holder.text_pay_type.text = payMethod

            holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))
        }

        try {
            if (StringUtils.isNotEmpty(item.regDatetime)) {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)

                val output = SimpleDateFormat("yyyy.MM.dd")
                holder.text_date.text = output.format(d) + " " + item.buy!!.buyerName
            }


        } catch (e: Exception) {

        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_sale_history, parent, false)
        return ViewHolder(v)
    }
}