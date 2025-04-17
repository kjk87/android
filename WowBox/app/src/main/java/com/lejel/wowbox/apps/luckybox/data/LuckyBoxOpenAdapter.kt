package com.lejel.wowbox.apps.luckybox.data

import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxProductInfoActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.dto.Product
import com.lejel.wowbox.databinding.ItemLuckyBoxOpenBinding
import java.text.SimpleDateFormat
import java.util.Calendar


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxOpenAdapter() : RecyclerView.Adapter<LuckyBoxOpenAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxPurchaseItem>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onClick(position: Int)
        fun reqDelivery(position: Int)
        fun onCashBack(position: Int, type: String)
        fun onRegReview(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyBoxPurchaseItem {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyBoxPurchaseItem>? {

        return mDataList
    }

    fun add(data: LuckyBoxPurchaseItem) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyBoxPurchaseItem>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyBoxPurchaseItem) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<LuckyBoxPurchaseItem>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LuckyBoxPurchaseItem>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLuckyBoxOpenBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageLuckyBoxOpenProductImage
        val text_product_name = binding.textLuckyBoxOpenProductName
        val text_product_price = binding.textLuckyBoxOpenProductPrice
        val text_effective_date = binding.textLuckyBoxOpenEffectiveDate
        val text_open_date = binding.textLuckyBoxOpenDate
        val text_remain_days = binding.textLuckyBoxOpenRemainDays
        val text_shipping_status = binding.textLuckyBoxOpenShippingStatus
        val layout_exchange = binding.layoutLuckyBoxOpenExchange
        val text_req_bol_exchange = binding.textLuckyBoxOpenReqBolExchange
        val text_req_point_exchange = binding.textLuckyBoxOpenReqPointExchange
        val text_req_shipping = binding.textLuckyBoxOpenReqShipping
        val text_cash_exchanged = binding.textLuckyBoxOpenCashExchanged
        val text_reivew = binding.textLuckyBoxOpenReview

        init {
            text_req_bol_exchange.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.productImage).apply(RequestOptions().fitCenter()).into(holder.image)

        holder.image.setOnClickListener {
            if(!(holder.itemView.context.packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW))){
                return@setOnClickListener
            }
            val intent = Intent(holder.itemView.context, LuckyBoxProductInfoActivity::class.java)
            val product = Product()
            product.seqNo = item.productSeqNo
            intent.putExtra(Const.DATA, product)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }

        holder.text_product_name.text = item.productName
        holder.text_product_price.text = holder.itemView.context.getString(R.string.format_origin_price, FormatUtil.getMoneyTypeFloat(item.productPrice.toString()))

//        if(StringUtils.isNotEmpty(item.productPriceData!!.effectiveDate)){
//            holder.text_effective_date.visibility = View.VISIBLE
//            holder.text_effective_date.text = holder.itemView.context.getString(R.string.format_effective_date, item.productPriceData!!.effectiveDate)
//        }else{
//            holder.text_effective_date.visibility = View.GONE
//        }

        val openDate = PPLUS_DATE_FORMAT.parse(item.openDatetime)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        holder.text_open_date.text = sdf.format(openDate)

        holder.text_remain_days.visibility = View.GONE
        holder.text_shipping_status.visibility = View.GONE
        holder.layout_exchange.visibility = View.GONE
        holder.text_cash_exchanged.visibility = View.GONE
        holder.text_reivew.visibility = View.GONE
        if (item.status == 2) {
            if (item.deliveryStatus == null) {
                holder.layout_exchange.visibility = View.VISIBLE
                holder.text_remain_days.visibility = View.VISIBLE

                val cal = Calendar.getInstance()
                cal.time = openDate
                cal.add(Calendar.DAY_OF_MONTH, 7)
                val remainMillis = cal.timeInMillis - System.currentTimeMillis()
                var remainDays = 0
                if (remainMillis > 0) {
                    remainDays = (remainMillis / 1000 / 60 / 60 / 24).toInt()
                }

                holder.text_remain_days.text = holder.itemView.context.getString(R.string.format_remain_days, remainDays.toString())
                if (remainMillis < 1000 * 60 * 60 * 24) {
                    var remainTime = remainMillis / 1000 / 60 / 60
                    if (remainTime < 0) {
                        remainTime = 0
                    }
                    holder.text_remain_days.text = holder.itemView.context.getString(R.string.format_remain_time, remainTime.toString())
                }

                holder.text_req_point_exchange.text = holder.itemView.context.getString(R.string.format_cash_back, FormatUtil.getMoneyTypeFloat(item.price.toString()))
                holder.text_req_point_exchange.setOnClickListener {
                    listener?.onCashBack(holder.absoluteAdapterPosition, "point")
                }

                holder.text_req_bol_exchange.text = holder.itemView.context.getString(R.string.format_bol_back, FormatUtil.getMoneyTypeFloat(item.refundBol.toString()))
                holder.text_req_bol_exchange.setOnClickListener {
                    listener?.onCashBack(holder.absoluteAdapterPosition, "ball")
                }

                holder.text_req_shipping.setOnClickListener {
                    listener?.reqDelivery(holder.absoluteAdapterPosition)
                }

            } else {
                holder.text_shipping_status.visibility = View.VISIBLE
                when (item.deliveryStatus) { // 1:배송대기(배송신청), 2:배송중, 3:배송완료
                    0 -> {
                        holder.text_shipping_status.setBackgroundResource(R.drawable.bg_232323_radius_13)
                        holder.text_shipping_status.setText(R.string.word_preparing_goods)
                    }
                    1 -> {
                        holder.text_shipping_status.setBackgroundResource(R.drawable.bg_232323_radius_13)
                        holder.text_shipping_status.setText(R.string.word_shipping_ready)
                    }
                    2 -> {
                        holder.text_shipping_status.setBackgroundResource(R.drawable.bg_232323_radius_13)
                        holder.text_shipping_status.setText(R.string.word_shipping)
                    }
                    3 -> {
                        holder.text_shipping_status.setBackgroundResource(R.drawable.bg_232323_radius_13)
                        holder.text_shipping_status.setText(R.string.word_shipping_complete) //                        if(item.isReviewExist != null && !item.isReviewExist!!){
                        //                            holder.text_reivew.visibility = View.VISIBLE
                        //                            holder.text_reivew.setOnClickListener {
                        //                                listener?.onRegReview(holder.absoluteAdapterPosition)
                        //                            }
                        //                        }else{
                        //                            holder.text_req_cash_exchange.visibility = View.VISIBLE
                        //                            holder.text_req_cash_exchange.text = holder.itemView.context.getString(R.string.word_complete_reg_lucky_box_review)
                        //                        }
                    }
                }
            }

        } else if (item.status == 4) {
            holder.text_cash_exchanged.visibility = View.VISIBLE
            holder.text_cash_exchanged.text = holder.itemView.context.getString(R.string.format_exchanged_lucky_box_cash, FormatUtil.getMoneyTypeFloat(item.price.toString()))
        } else if (item.status == 5) {
            holder.text_cash_exchanged.visibility = View.VISIBLE
            holder.text_cash_exchanged.text = holder.itemView.context.getString(R.string.format_exchanged_lucky_box_bol, FormatUtil.getMoneyTypeFloat(item.refundBol.toString()))
        }

        holder.itemView.setOnClickListener {
            listener?.onClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxOpenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}