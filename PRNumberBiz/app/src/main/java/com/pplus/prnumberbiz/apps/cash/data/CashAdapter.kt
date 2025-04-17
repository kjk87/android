package com.pplus.prnumberbiz.apps.cash.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Cash
import kotlinx.android.synthetic.main.item_point.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CashAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CashAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Cash>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    constructor() : super()

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Cash {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Cash>? {

        return mDataList
    }

    fun add(data: Cash) {

        if (mDataList == null) {
            mDataList = ArrayList<Cash>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Cash>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Cash>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Cash) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Cash>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Cash>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_name = itemView.text_point_name
        val text_date = itemView.text_point_date
        val text_price = itemView.text_point_price

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: Cash = mDataList!!.get(position)

        var name: String? = null

        try {
            holder.text_name.text = ""
//buy, recvAdmin, refundMsgFail, cancelSendMsg, useTargetPush, usePush, useSms, useLBS, useAdKeyword, buyBol, useAdvertise, refundAdvertise
            when (item.secondaryType!!) {
                EnumData.CashType.buy.name -> {
                    name = mContext?.getString(R.string.type_cash_buy)
                }
                EnumData.CashType.recvAdmin.name -> {
                    name = mContext?.getString(R.string.type_cash_recvAdmin)
                }
                EnumData.CashType.refundAdmin.name -> {
                    name = mContext?.getString(R.string.type_cash_refundAdmin)
                }
                EnumData.CashType.refundMsgFail.name -> {
                    name = mContext?.getString(R.string.type_cash_refundMsgFail)
                }
                EnumData.CashType.cancelSendMsg.name -> {
                    name = mContext?.getString(R.string.type_cash_cancelSendMsg)
                }
                EnumData.CashType.useTargetPush.name -> {
//                    name = mContext?.getString(R.string.type_buy)
                }
                EnumData.CashType.usePush.name -> {
                    name = mContext?.getString(R.string.type_cash_usePush)
                }
                EnumData.CashType.useSms.name -> {
                    name = mContext?.getString(R.string.type_cash_useSms)
                }
                EnumData.CashType.useLBS.name -> {
//                    name = mContext?.getString(R.string.type_usel)
                }
                EnumData.CashType.useAdKeyword.name -> {
                    name = mContext?.getString(R.string.type_cash_useAdKeyword)
                }
                EnumData.CashType.buyBol.name -> {
//                    name = mContext?.getString(R.string.type_cash_b)
                }
                EnumData.CashType.useAdvertise.name -> {
                    name = mContext?.getString(R.string.type_cash_useAdvertise)
                }
                EnumData.CashType.refundAdvertise.name -> {
                    name = mContext?.getString(R.string.type_cash_refundAdvertise)
                }
                else -> {
                    name = ""
                }
            }

            holder.text_name.text = name
        } catch (e: Exception) {

        }

        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = output.format(d)

        } catch (e: Exception) {

        }

        when (item.secondaryType) {

            EnumData.CashType.useTargetPush.name, EnumData.CashType.usePush.name, EnumData.CashType.useSms.name, EnumData.CashType.useLBS.name, EnumData.CashType.useAdKeyword.name, EnumData.CashType.buyBol.name, EnumData.CashType.useAdvertise.name
                , EnumData.CashType.refundAdmin.name-> {
                holder.text_price.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff0000))
                holder.text_price.text = "- ${mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.amount))}"
            }

            EnumData.CashType.buy.name, EnumData.CashType.recvAdmin.name, EnumData.CashType.refundMsgFail.name, EnumData.CashType.cancelSendMsg.name, EnumData.CashType.refundAdvertise.name -> {
                holder.text_price.text = "${mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.amount))}"
                holder.text_price.setTextColor(ResourceUtil.getColor(mContext, R.color.color_579ffb))
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false)
        return ViewHolder(v)
    }
}