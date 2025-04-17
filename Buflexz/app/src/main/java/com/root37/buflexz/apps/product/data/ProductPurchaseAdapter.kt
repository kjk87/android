package com.root37.buflexz.apps.product.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.ProductPurchase
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemProductPurchaseBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductPurchaseAdapter() : RecyclerView.Adapter<ProductPurchaseAdapter.ViewHolder>() {

    var mDataList: MutableList<ProductPurchase>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): ProductPurchase {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductPurchase>? {

        return mDataList
    }

    fun add(data: ProductPurchase) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductPurchase>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductPurchase) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductPurchase>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductPurchase>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProductPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.productImage).apply(RequestOptions().centerCrop()).into(holder.binding.imageProductPurchase)
        holder.binding.textProductPurchaseTitle.text = item.productName
        holder.binding.textProductPurchasePoint.text = FormatUtil.getMoneyType(item.usePoint.toString())

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textProductPurchaseDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))

        holder.binding.textProductPurchaseSearchShipping.visibility = View.GONE

        when (item.deliveryStatus) { //0:주문확인전, 1:상품준비중, 2:배송중, 3:배송완료, 4:취소
            0, 1 -> {
                holder.binding.textProductPurchaseStatus.setBackgroundResource(R.drawable.bg_dbb5b5b5_radius_20)
                holder.binding.textProductPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textProductPurchaseStatus.setText(R.string.word_shipping_wait)
                holder.binding.textProductPurchaseStatus.setOnClickListener {  }
            }
            2 -> {
                holder.binding.textProductPurchaseStatus.setBackgroundResource(R.drawable.bg_dbb5b5b5_radius_20)
                holder.binding.textProductPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_3e78ff))
                holder.binding.textProductPurchaseStatus.setText(R.string.word_shipping_ing)
                holder.binding.textProductPurchaseStatus.setOnClickListener {  }
                holder.binding.textProductPurchaseSearchShipping.visibility = View.VISIBLE
            }
            3 -> {
                holder.binding.textProductPurchaseStatus.setBackgroundResource(R.drawable.bg_dbb5b5b5_radius_20)
                holder.binding.textProductPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textProductPurchaseStatus.setText(R.string.word_shipping_complete)
                holder.binding.textProductPurchaseStatus.setOnClickListener {  }
            }
            4 -> {
                holder.binding.textProductPurchaseStatus.setBackgroundResource(R.drawable.bg_24ff5e5e_radius_20)
                holder.binding.textProductPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff5e5e))
                holder.binding.textProductPurchaseStatus.setText(R.string.word_cancel)
//                holder.binding.textProductPurchaseStatus.setOnClickListener {
//                    val intent = Intent(holder.itemView.context, AlertGiftCardReturnReasonActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    holder.itemView.context.startActivity(intent)
//                }
            }

        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}