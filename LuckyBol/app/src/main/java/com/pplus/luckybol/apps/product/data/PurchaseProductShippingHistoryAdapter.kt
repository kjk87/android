package com.pplus.luckybol.apps.product.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.ui.ProductReviewRegActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.model.dto.PurchaseProduct
import com.pplus.luckybol.databinding.ItemBuyShippingHistoryBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PurchaseProductShippingHistoryAdapter : RecyclerView.Adapter<PurchaseProductShippingHistoryAdapter.ViewHolder> {

    var mDataList: MutableList<PurchaseProduct>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): PurchaseProduct {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PurchaseProduct>? {

        return mDataList
    }

    fun add(data: PurchaseProduct) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PurchaseProduct>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PurchaseProduct) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PurchaseProduct>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBuyShippingHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBuyShippingHistoryImage
        val text_page_name = binding.textBuyShippingHistoryPageName
        val text_text_title = binding.textBuyShippingHistoryTitle
        val text_date = binding.textBuyShippingHistoryDate
        val text_review = binding.textBuyShippingHistoryReview
        val text_status = binding.textBuyShippingHistoryStatus
        val text_price = binding.textBuyShippingHistoryPrice

        init {
            text_text_title.setSingleLine()
            text_page_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.page != null){
            holder.text_page_name.visibility = View.VISIBLE
            holder.text_page_name.text = item.page!!.name

            if (item.product!!.imageList != null && item.product!!.imageList!!.isNotEmpty()) {
                Glide.with(holder.itemView.context).load(item.product!!.imageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.img_page_profile_default)
            }
        }else{
            holder.text_page_name.visibility = View.GONE
        }

        holder.text_text_title.text = item.title
        holder.text_review.visibility = View.GONE
        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))

        var date = ""
        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        when (item.status) {
            EnumData.PurchaseProductStatus.PAY.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                holder.text_status.setText(R.string.word_shipping_ready)
                when(item.deliveryStatus){
                    EnumData.DeliveryStatus.READY_ING.status->{
                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
                        holder.text_status.setText(R.string.word_shipping_ready)
                    }
                    EnumData.DeliveryStatus.ING.status->{
                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_fc5c57))
                        holder.text_status.setText(R.string.word_shipping)
                    }
                    EnumData.DeliveryStatus.COMPLETE.status->{
                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                        holder.text_status.setText(R.string.word_shipping_complete)
//                        holder.text_review.visibility = View.VISIBLE
//                        if(item.isReviewExist != null && item.isReviewExist!!){
//                            holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
//                            holder.text_review.setText(R.string.word_review_write_complete)
//                            holder.text_review.setOnClickListener { }
//                        }else{
//                            holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
//                            holder.text_review.setText(R.string.word_review_write)
//
//                            holder.text_review.setOnClickListener {
//                                val intent = Intent(holder.itemView.context, ProductReviewRegActivity::class.java)
//                                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                                //                        intent.putExtra(Const.PRODUCT_PRICE, item.productPriceData)
//                                intent.putExtra(Const.PURCHASE_PRODUCT, item)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//                            }
//
//                        }
                    }
                }
            }
            EnumData.PurchaseProductStatus.CANCEL_REQ.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_cancel_request)
            }
            EnumData.PurchaseProductStatus.CANCEL_COMPLETE.status -> {
                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.cancelDatetime)
                    date = output.format(d)
                }

                holder.text_status.setText(R.string.word_cancel_complete)
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            }
            EnumData.PurchaseProductStatus.REFUND_REQ.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_refund_request)
            }
            EnumData.PurchaseProductStatus.REFUND_COMPLETE.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_refund_complete)
            }
            EnumData.PurchaseProductStatus.EXCHANGE_REQ.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
                holder.text_status.setText(R.string.word_exchange_request)
            }
            EnumData.PurchaseProductStatus.EXCHANGE_COMPLETE.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_exchange_complete)
            }
            EnumData.PurchaseProductStatus.COMPLETE.status -> {
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_status.setText(R.string.word_confirm_buy)
//                holder.text_status.setText(R.string.word_shipping_complete)
                holder.text_review.visibility = View.VISIBLE
                if(item.isReviewExist != null && item.isReviewExist!!){
                    holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
                    holder.text_review.setText(R.string.word_review_write_complete)
                    holder.text_review.setOnClickListener { }
                }else{
                    holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
                    holder.text_review.setText(R.string.word_review_write)

                    holder.text_review.setOnClickListener {
                        val intent = Intent(holder.itemView.context, ProductReviewRegActivity::class.java)
                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                        intent.putExtra(Const.PRODUCT_PRICE, item.productPriceData)
                        intent.putExtra(Const.PURCHASE_PRODUCT, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        if(launcher != null){
                            launcher!!.launch(intent)
                        }else{
                            holder.itemView.context.startActivity(intent)
                        }
                    }

                }
            }
        }

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
        date = output.format(d)
        if (StringUtils.isNotEmpty(date)) {
            holder.text_date.text = date
            holder.text_date.visibility = View.VISIBLE
        } else {
            holder.text_date.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuyShippingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}