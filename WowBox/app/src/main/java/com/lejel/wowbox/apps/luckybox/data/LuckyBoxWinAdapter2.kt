package com.lejel.wowbox.apps.luckybox.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxReplyActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyBoxWin2Binding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxWinAdapter2() : RecyclerView.Adapter<LuckyBoxWinAdapter2.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxPurchaseItem>? = null
    var listener: OnItemClickListener? = null
    var replyLauncher: ActivityResultLauncher<Intent>? = null
//    var replyLuckyPickLauncher: ActivityResultLauncher<Intent>? = null

    interface OnItemClickListener {

        fun onClick(position: Int)
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

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LuckyBoxPurchaseItem>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyBoxWin2Binding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if((holder.itemView.context as BaseActivity).isDestroyed){
            return
        }

        holder.binding.textLuckyBoxWinUserName.text = item.memberTotal!!.nickname

        Glide.with(holder.itemView.context).load(item.productImage).apply(RequestOptions().fitCenter()).into(holder.binding.imageLuckyBoxWinProduct)

        holder.binding.textLuckyBoxWinProductName.text = "(${item.productName})"
        holder.binding.textLuckyBoxWinProductPrice.text = holder.itemView.context.getString(R.string.format_origin_price, FormatUtil.getMoneyTypeFloat(item.productPrice.toString()))

        holder.itemView.setOnClickListener {
            listener?.onClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxWin2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}