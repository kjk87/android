package com.pplus.luckybol.apps.product.data

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ProductLike
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ItemProductShipBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductLikeShippingAdapter : RecyclerView.Adapter<ProductLikeShippingAdapter.ViewHolder> {

    var mDataList: MutableList<ProductLike>? = null
    var listener: OnItemClickListener? = null
    var deleteListener: OnItemDeleteListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    interface OnItemDeleteListener {

        fun onItemDelete()
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {

        this.deleteListener = listener
    }

    fun getItem(position: Int): ProductLike {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductLike>? {

        return mDataList
    }

    fun add(data: ProductLike) {

        if (mDataList == null) {
            mDataList = ArrayList<ProductLike>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductLike>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProductLike>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductLike) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductLike>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductLike>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemProductShipBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageProductShipImage
        val text_product_name = binding.textProductShipName
        val text_discount_ratio = binding.textProductShipDiscountRatio
        val text_sale_price = binding.textProductShipSalePrice
        val text_origin_price = binding.textProductShipOriginPrice
        val text_save_point = binding.textProductShipSavePoint
        val image_like = binding.imageProductShipLike

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        val productPrice = item.productPrice!!

        if (productPrice.product!!.imageList != null && productPrice.product!!.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(productPrice.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)

        } else {
            holder.image.setImageResource(R.drawable.luckybol_default_img)
        }

//        if (productPrice.product!!.count != -1) {
//
//
//            holder.layout_rate_total.visibility = View.VISIBLE
//            holder.text_count.visibility = View.VISIBLE
//            holder.text_remain_count.visibility = View.VISIBLE
//            var soldCount = 0
//            if (productPrice.product!!.soldCount != null) {
//                soldCount = productPrice.product!!.soldCount!!
//            }
//
//            holder.text_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_count_per2, soldCount.toString(), productPrice.product!!.count.toString()))
//            holder.text_remain_count.text = holder.itemView.context.getString(R.string.format_remain_count, (productPrice.product!!.count!! - soldCount).toString())
//
//            var weightSum = 1
//            if (productPrice.product!!.count!! > 0) {
//                weightSum = productPrice.product!!.count!!
//            }
//            holder.layout_rate_total.weightSum = weightSum.toFloat()
//            val layoutParams = holder.layout_rate.layoutParams
//
//            if (layoutParams is LinearLayout.LayoutParams) {
//                layoutParams.weight = productPrice.product!!.soldCount!!.toFloat()
//            }
//            holder.layout_rate.requestLayout()
//
//        } else {
//            holder.text_remain_count.visibility = View.GONE
//            holder.layout_rate_total.visibility = View.GONE
//            holder.text_count.visibility = View.GONE
//        }
//
//        holder.text_save_point.text = holder.itemView.context.getString(R.string.format_bol_unit, FormatUtil.getMoneyType((productPrice.price!!*0.1f).toInt().toString()))

        if(productPrice.isPoint != null && productPrice.isPoint!!){
            holder.text_save_point.visibility = View.VISIBLE
            holder.text_save_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_save_bol, FormatUtil.getMoneyTypeFloat(productPrice.point!!.toString())))
        }else{
            holder.text_save_point.visibility = View.GONE
        }

        holder.text_product_name.text = productPrice.product!!.name

        if (productPrice.originPrice != null && productPrice.originPrice!! > 0) {

            if (productPrice.originPrice!! <= productPrice.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(productPrice.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        if (productPrice.discountRatio != null && productPrice.discountRatio!!.toInt() > 0) {
            holder.text_discount_ratio.visibility = View.VISIBLE
            holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit, productPrice.discountRatio!!.toInt().toString()))
        } else {
            holder.text_discount_ratio.visibility = View.GONE
        }
        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(productPrice.price.toString())))

        holder.image_like.isSelected = true
        holder.image_like.setOnClickListener{
            deleteLike(holder.itemView.context, item)
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
        }
    }

    private fun deleteLike(context:Context, item:ProductLike) {
        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = item.productPrice!!.productSeqNo
        productLike.productPriceSeqNo = item.productPrice!!.seqNo
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().deleteProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                (context as BaseActivity). hideProgress()
                ToastUtil.show(context, R.string.msg_delete_goods_like)
                if(deleteListener != null){
                    deleteListener!!.onItemDelete()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                (context as BaseActivity).hideProgress()
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductShipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}