package com.pplus.prnumberuser.apps.product.data

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductLike
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ItemProductShipBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductShipAdapter() : RecyclerView.Adapter<ProductShipAdapter.ViewHolder>() {

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

    class ViewHolder(binding: ItemProductShipBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageProductShipImage
        val text_product_name = binding.textProductShipName
        val text_discount_ratio = binding.textProductShipDiscountRatio
        val text_sale_price = binding.textProductShipSalePrice
        val text_origin_price = binding.textProductShipOriginPrice
        val text_save_point = binding.textProductShipSavePoint
        val image_like = binding.imageProductShipLike
        val text_remain_count = binding.textProductShipRemainCount

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if (item.product!!.imageList != null && item.product!!.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        holder.image_like.isSelected = item.isLike!!
        holder.image_like.setOnClickListener {
            if(item.isLike!!){
                deleteLike(holder.itemView.context, item, position)
            }else{
                postLike(holder.itemView.context, item, position)
            }
        }

        if (item.product!!.count != -1) {
            holder.text_remain_count.visibility = View.VISIBLE
            var soldCount = 0
            if (item.product!!.soldCount != null) {
                soldCount = item.product!!.soldCount!!
            }
            holder.text_remain_count.text = holder.itemView.context.getString(R.string.format_remain_count, (item.product!!.count!! - soldCount).toString())

        } else {
            holder.text_remain_count.visibility = View.GONE
        }
//
        if(item.isPoint != null && item.isPoint!! && item.point != null && item.point!! > 0){
            holder.text_save_point.visibility = View.VISIBLE
            holder.text_save_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_save_bol, FormatUtil.getMoneyType(item.point!!.toInt().toString())))
        }else{
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
            holder.text_discount_ratio.text = holder.itemView.context.getString(R.string.format_percent2, item.discountRatio!!.toInt().toString())
        } else {
            holder.text_discount_ratio.visibility = View.GONE
        }
        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString())))


        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }
    }

    private fun postLike(context:Context, item:ProductPrice, position: Int) {

        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = item.productSeqNo
        productLike.productPriceSeqNo = item.seqNo
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().insertProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                context.hideProgress()
                item.isLike = true
                notifyItemChanged(position)
                ToastUtil.show(context, R.string.msg_goods_like)
                listener?.changeLike()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    private fun deleteLike(context:Context, item:ProductPrice, position: Int) {
        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = item.productSeqNo
        productLike.productPriceSeqNo = item.seqNo
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().deleteProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                context.hideProgress()
                item.isLike = false
                notifyItemChanged(position)
                ToastUtil.show(context, R.string.msg_delete_goods_like)
                listener?.changeLike()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductShipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}