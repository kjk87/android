package com.pplus.prnumberuser.apps.product.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.PurchaseProduct
import com.pplus.prnumberuser.core.network.model.dto.PurchaseProductOption
import com.pplus.prnumberuser.databinding.ItemPurchaseProductShipBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PurchaseProductShipAdapter(purchaseProduct: PurchaseProduct) : RecyclerView.Adapter<PurchaseProductShipAdapter.ViewHolder>() {

    var mDataList: MutableList<PurchaseProductOption>? = null
    var listener: OnItemClickListener? = null
    var mPurchaseProduct = purchaseProduct

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        if(purchaseProduct.purchaseProductOptionList == null || purchaseProduct.purchaseProductOptionList!!.isEmpty()){
            if(purchaseProduct.purchaseProductOptionSelectList == null || purchaseProduct.purchaseProductOptionSelectList!!.isEmpty()){
                mDataList = ArrayList()
                mDataList!!.add(PurchaseProductOption())
            }else{
                mDataList = purchaseProduct.purchaseProductOptionSelectList!!.toMutableList()
            }
        }else{
            mDataList = purchaseProduct.purchaseProductOptionList!!.toMutableList()
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): PurchaseProductOption {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<PurchaseProductOption>? {

        return mDataList
    }

    fun add(data: PurchaseProductOption) {

        if (mDataList == null) {
            mDataList = ArrayList<PurchaseProductOption>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PurchaseProductOption>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<PurchaseProductOption>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PurchaseProductOption) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<PurchaseProductOption>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PurchaseProductOption>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPurchaseProductShipBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imagePurchaseProductShip
        val text_goods_title = binding.textPurchaseProductShipTitle
        val text_option = binding.textPurchaseProductShipOption
        val text_use_time = binding.textPurchaseProductUseTime
        val text_count = binding.textPurchaseProductShipCount
        val text_price = binding.textPurchaseProductShipPrice

        init {
            text_goods_title.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if (mPurchaseProduct.product!!.imageList != null && mPurchaseProduct.product!!.imageList!!.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(mPurchaseProduct.product!!.imageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_page_profile_default)
        }

        holder.text_goods_title.text = mPurchaseProduct.product!!.name

        if(StringUtils.isNotEmpty(mPurchaseProduct.startTime) && StringUtils.isNotEmpty(mPurchaseProduct.endTime)){
            holder.text_use_time.visibility = View.VISIBLE
            holder.text_use_time.text = holder.itemView.context.getString(R.string.format_use_time3, mPurchaseProduct.startTime!!.substring(0, 5), mPurchaseProduct.endTime!!.substring(0, 5))
        }else{
            holder.text_use_time.visibility = View.GONE
        }

        var price = 0
        if(item.productOptionDetailSeqNo == null){
            holder.text_option.visibility = View.GONE

            var productPrice = mPurchaseProduct.productPriceData!!.price!!.toInt()
            if(mPurchaseProduct.productPrice != null){
                productPrice = mPurchaseProduct.productPrice!!.toInt()
            }

            price = productPrice * mPurchaseProduct.count!!
            holder.text_count.text = holder.itemView.context.getString(R.string.format_order_count, FormatUtil.getMoneyType(mPurchaseProduct.count.toString()))
        }else{
            holder.text_option.visibility = View.VISIBLE

            var optionPrice = ""
            if(item.price!! > 0){
                optionPrice = " (+${holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price!!.toString()))})"
            }

            if(StringUtils.isNotEmpty(item.depth2)){
                holder.text_option.text = "${item.depth1} / ${item.depth2}${optionPrice}"
            }else{
                holder.text_option.text = "${item.depth1}${optionPrice}"
            }
            holder.text_count.text = holder.itemView.context.getString(R.string.format_order_count, FormatUtil.getMoneyType(item.amount.toString()))

            var productPrice = mPurchaseProduct.productPriceData!!.price!!.toInt()
            if(mPurchaseProduct.productPrice != null){
                productPrice = mPurchaseProduct.productPrice!!.toInt()
            }

            price = (productPrice + item.price!!)*item.amount!!

        }

        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(price.toString()))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPurchaseProductShipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}