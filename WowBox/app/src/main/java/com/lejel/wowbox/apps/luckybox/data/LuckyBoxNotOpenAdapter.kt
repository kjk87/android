package com.lejel.wowbox.apps.luckybox.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.resource.ResourceUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchase
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.databinding.ItemLuckyBoxNotOpenBinding
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxNotOpenAdapter() : RecyclerView.Adapter<LuckyBoxNotOpenAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxPurchase>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun use(item:LuckyBoxPurchaseItem)

        fun cancel(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyBoxPurchase {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyBoxPurchase>? {

        return mDataList
    }

    fun add(data: LuckyBoxPurchase) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyBoxPurchase>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyBoxPurchase) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<LuckyBoxPurchase>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LuckyBoxPurchase>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLuckyBoxNotOpenBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_title = binding.textLuckBoxNotOpenTitle
        val text_expire_date = binding.textLuckyBoxNotOpenExpireDate
        val recycler_item = binding.recyclerLuckBoxNotOpenItem
        val text_cancel = binding.textLuckBoxNotOpenCancel

        init {
//            text_cancel.visibility = View.GONE
//            text_expire_date.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_title.text = item.title
        val payDate = PPLUS_DATE_FORMAT.parse(item.paymentDatetime)
        val cal = Calendar.getInstance()

        cal.time = payDate
        cal.add(Calendar.DAY_OF_MONTH, 7)
        val remainMillis = cal.timeInMillis - System.currentTimeMillis()
        var remainDays = 0
        if(remainMillis > 0){
            remainDays = (remainMillis/1000/60/60/24).toInt()
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if(item.isCancelable!! && remainMillis > 0){
            holder.text_cancel.setText(R.string.word_req_cancel)
            holder.text_cancel.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_545454))
            holder.text_cancel.setBackgroundResource(R.drawable.underbar_545454_ffffff)
            holder.text_expire_date.text = "${sdf.format(payDate)}(${holder.itemView.context.getString(R.string.format_remain_days, remainDays.toString())})"
            if(remainMillis < 1000*60*60*24){
                val remainTime = remainMillis/1000/60/60
                holder.text_expire_date.text = "${sdf.format(payDate)}(${holder.itemView.context.getString(R.string.format_remain_time, remainTime.toString())})"
            }
        }else{
            holder.text_cancel.setText(R.string.word_disable_pay_cancel)
            holder.text_cancel.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_c2c2c2))
            holder.text_cancel.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, android.R.color.transparent))

//            holder.text_expire_date.text = "${sdf.format(payDate)}(${holder.itemView.context.getString(R.string.format_remain_days, "0")})"
            holder.text_expire_date.text = sdf.format(payDate)
        }
        val adapter = LuckyBoxNotOpenItemAdapter()
        holder.recycler_item.adapter = adapter
        holder.recycler_item.layoutManager = LinearLayoutManager(holder.itemView.context)
        adapter.setOnItemClickListener(object : LuckyBoxNotOpenItemAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(item.isCancelable != null && item.isCancelable!!){
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(holder.itemView.context.getString(R.string.word_lucky_box_open))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    if(item.luckyboxPurchaseItem!!.size > 1){
                        builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_alert_lucky_box_open_desc1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    }else{
                        builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_alert_lucky_box_open_desc1_2), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    }

                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_alert_lucky_box_open_desc2), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    listener?.use(item.luckyboxPurchaseItem!![position])
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(holder.itemView.context)
                }else{
                    listener?.use(item.luckyboxPurchaseItem!![position])
                }
            }
        })

        adapter.setDataList(item.luckyboxPurchaseItem!! as MutableList<LuckyBoxPurchaseItem>)

        holder.text_cancel.setOnClickListener {
            if(item.isCancelable!! && remainMillis > 0){
                val builder = AlertBuilder.Builder()
                builder.setTitle(holder.itemView.context.getString(R.string.word_req_cancel))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_lucky_box_cancel_desc1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                if(item.paymentMethod != "cash"){
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_lucky_box_cancel_desc2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                }

                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                listener?.cancel(holder.absoluteAdapterPosition)
                            }
                            else -> {}
                        }
                    }
                }).builder().show(holder.itemView.context)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxNotOpenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}