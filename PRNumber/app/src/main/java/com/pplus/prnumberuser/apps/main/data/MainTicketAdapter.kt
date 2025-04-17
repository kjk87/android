package com.pplus.prnumberuser.apps.main.data

import android.content.Context
import android.graphics.Paint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductLike
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ItemMainTicketBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainTicketAdapter() : RecyclerView.Adapter<MainTicketAdapter.ViewHolder>() {

    var mDataList: MutableList<ProductPrice>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)

        fun changeLike()
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): ProductPrice {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductPrice>? {

        return mDataList
    }

    fun add(data: ProductPrice) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductPrice>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductPrice) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductPrice>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductPrice>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainTicketBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout = binding.layoutMainTicket
        val text_type = binding.textMainTicketType
        val text_use_time = binding.textMainTicketUseTime
        val image = binding.imageMainTicketImage
        val text_product_name = binding.textMainTicketName
        val text_discount_ratio = binding.textMainTicketDiscountRatio
        val text_sale_price = binding.textMainTicketSalePrice
        val text_origin_price = binding.textMainTicketOriginPrice
        val text_save_point = binding.textMainTicketSavePoint
        val layout_remain_count = binding.layoutMainTicketRemainCount
        val text_remain_count = binding.textMainTicketRemainCount
        val layout_sold_out_status = binding.layoutMainTicketSoldOutStatus
        val text_sold_status = binding.textMainTicketSoldStatus
        val text_purchase_time = binding.textMainTicketPurchaseTime
        val text_use_time2 = binding.textMainTicketUseTime2
        val text_remain_time = binding.textMainRemainTime
        var countTimer: CountDownTimer? = null

        init {
            layout.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.width_852)
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        when (item.productType) {
            "lunch" -> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_lunch_event)
            }
            "dinner" -> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_dinner_event)
            }
            "time" -> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_time_event)
            }
        }

        if (StringUtils.isNotEmpty(item.startTime) && StringUtils.isNotEmpty(item.endTime)) {
            holder.text_use_time.text = holder.itemView.context.getString(R.string.format_use_time2, item.startTime!!.substring(0, 5), item.endTime!!.substring(0, 5))
            holder.text_use_time2.text = holder.itemView.context.getString(R.string.format_use_time3, item.startTime!!.substring(0, 5), item.endTime!!.substring(0, 5))
            val purchaseWait = CountryConfigManager.getInstance().config.properties!!.purchaseWait!!
            val startMin = (item.startTime!!.split(":")[0].toInt() * 60) + item.startTime!!.split(":")[1].toInt()
            val purchaseMin = startMin - purchaseWait
            val hour = purchaseMin/60
            val min = purchaseMin%60
            val strHour = DateFormatUtils.formatTime(hour)
            val strMin = DateFormatUtils.formatTime(min)
            holder.text_purchase_time.text = holder.itemView.context.getString(R.string.format_purchase_time, "00:00", "$strHour:$strMin")
        } else {
            holder.text_use_time.text = ""
            holder.text_use_time2.text = ""
            holder.text_purchase_time.text = ""
        }

        if (item.product!!.imageList != null && item.product!!.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        if (item.isPoint != null && item.isPoint!! && item.point != null && item.point!! > 0) {
            holder.text_save_point.visibility = View.VISIBLE
            holder.text_save_point.text = holder.itemView.context.getString(R.string.format_cash_unit2, FormatUtil.getMoneyType(item.point!!.toInt().toString()))
        } else {
            holder.text_save_point.visibility = View.GONE
        }

        holder.text_product_name.text = item.product!!.name

        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        //        holder.text_discount.visibility = View.GONE
        if (item.discountRatio != null && item.discountRatio!!.toInt() > 0) {
            holder.text_discount_ratio.visibility = View.VISIBLE
            holder.text_discount_ratio.text = holder.itemView.context.getString(R.string.format_percent, item.discountRatio!!.toInt().toString())
        } else {
            holder.text_discount_ratio.visibility = View.GONE
        }
        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        var isSoldOut = false

        if (item.dailyCount != null && item.dailyCount!! > 0) {
            holder.layout_remain_count.visibility = View.VISIBLE
            var soldCount = 0
            if (item.dailySoldCount != null) {
                soldCount = item.dailySoldCount!!
            }

            val remainCount = item.dailyCount!! - soldCount

            if (remainCount > 0) {
                holder.layout_remain_count.visibility = View.VISIBLE
                holder.text_remain_count.text = remainCount.toString()
            } else {
                holder.layout_remain_count.visibility = View.GONE
                isSoldOut = true
            }

        } else {
            holder.layout_remain_count.visibility = View.GONE
        }

        var isTimeOver = false
        holder.countTimer?.cancel()
        holder.text_remain_time.visibility = View.GONE
        if (StringUtils.isNotEmpty(item.startTime) && StringUtils.isNotEmpty(item.endTime)) {
            val startMin = (item.startTime!!.split(":")[0].toInt() * 60) + item.startTime!!.split(":")[1].toInt()
            val endMin = (item.endTime!!.split(":")[0].toInt() * 60) + item.endTime!!.split(":")[1].toInt()
            val cal = Calendar.getInstance()
            val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
            val purchaseWait = CountryConfigManager.getInstance().config.properties!!.purchaseWait!!
            if (currentMin >= startMin - purchaseWait) {
                isTimeOver = true
            }else{
                val remainMin = startMin - purchaseWait - currentMin
                if(remainMin > 0){
                    holder.text_remain_time.visibility = View.VISIBLE
                    val remainMinMillis = remainMin.toLong()*60*1000
                    holder.countTimer = object : CountDownTimer(remainMinMillis, 1000) {

                        override fun onTick(millisUntilFinished: Long) {

                            val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                            val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                            val seconds = (millisUntilFinished / 1000).toInt() % 60

                            val strH = DateFormatUtils.formatTime(hours)
                            val strM = DateFormatUtils.formatTime(minutes)
                            val strS = DateFormatUtils.formatTime(seconds)

                            if (hours > 0) {
                                holder.text_remain_time?.text = "$strH:$strM:$strS"
                            } else {
                                if (minutes > 0) {
                                    holder.text_remain_time?.text = "$strM:$strS"
                                } else {
                                    holder.text_remain_time?.text = strS
                                }
                            }
                        }

                        override fun onFinish() {
                            try {
                                notifyItemChanged(holder.adapterPosition)

                            } catch (e: Exception) {

                            }
                        }
                    }
                    holder.countTimer!!.start()
                }
            }
        }

        if(isSoldOut || isTimeOver){
            if(isSoldOut){
                holder.text_sold_status.setText(R.string.word_sold_out_en)
            }else if(isTimeOver){
                holder.text_sold_status.setText(R.string.word_time_over_en)
            }
            holder.layout_remain_count.visibility = View.GONE
            holder.layout_sold_out_status.visibility = View.VISIBLE
        }else{
            holder.layout_sold_out_status.visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }
    }

    private fun postLike(context: Context, item: ProductPrice, position: Int) {

        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = item.productSeqNo
        productLike.productPriceSeqNo = item.seqNo
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().insertProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()
                item.isLike = true
                notifyItemChanged(position)
                ToastUtil.show(context, R.string.msg_goods_like)
                listener?.changeLike()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    private fun deleteLike(context: Context, item: ProductPrice, position: Int) {
        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = item.productSeqNo
        productLike.productPriceSeqNo = item.seqNo
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().deleteProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()
                item.isLike = false
                notifyItemChanged(position)
                ToastUtil.show(context, R.string.msg_delete_goods_like)
                listener?.changeLike()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}