package com.lejel.wowbox.apps.luckybox.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyBox
import com.lejel.wowbox.core.network.model.dto.LuckyBoxProductGroupItem
import com.lejel.wowbox.databinding.ItemLuckyBoxItemBinding
import com.pplus.utils.part.format.FormatUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxAdapter() : RecyclerView.Adapter<LuckyBoxAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxProductGroupItem>? = null
    var listener: OnItemClickListener? = null
    var mLuckyBox: LuckyBox? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyBoxProductGroupItem {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<LuckyBoxProductGroupItem>? {

        return mDataList
    }

    fun add(data: LuckyBoxProductGroupItem) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyBoxProductGroupItem>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyBoxProductGroupItem>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyBoxProductGroupItem>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyBoxProductGroupItem) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyBoxProductGroupItem>()

    }

    fun setDataList(dataList: MutableList<LuckyBoxProductGroupItem>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyBoxItemBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.textLuckyBoxItemProductPrice.paintFlags = binding.textLuckyBoxItemProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]


        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().fitCenter()).into(holder.binding.imageLuckBoxItem)
        holder.binding.textLuckBoxItemName.text = item.productName
        holder.binding.textLuckyBoxItemProductPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
        holder.binding.textLuckyBoxItemEngagePrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(mLuckyBox!!.engagePrice.toString()))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

//        if (item.productPriceSeqNo != null) {
//            holder.layout_url.visibility = View.VISIBLE
//            holder.layout_url.setOnClickListener {
//                val productPrice = ProductPrice()
//                productPrice.seqNo = item.productPriceSeqNo
//                val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
//                intent.putExtra(Const.DATA, productPrice)
//                holder.itemView.context.startActivity(intent)
//            }
//        } else {
//            holder.layout_url.visibility = View.GONE
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}