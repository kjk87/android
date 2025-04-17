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
import com.lejel.wowbox.databinding.ItemLuckyBoxWinBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxWinAdapter() : RecyclerView.Adapter<LuckyBoxWinAdapter.ViewHolder>() {

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

    class ViewHolder(binding: ItemLuckyBoxWinBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageLuckyBoxWinProfile
        val text_name = binding.textLuckyBoxWinUserName
        val image_product = binding.imageLuckyBoxWinProduct
        val text_product_name = binding.textLuckyBoxWinProductName
        val text_product_price = binding.textLuckyBoxWinProductPrice
        val text_price = binding.textLuckyBoxWinPrice
        val text_impression = binding.textLuckyBoxWinImpression
        val text_reply_count = binding.textLuckyBoxWinReplyCount

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().fitCenter().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.image_profile)
        holder.text_name.text = item.memberTotal!!.nickname

        Glide.with(holder.itemView.context).load(item.productImage).apply(RequestOptions().fitCenter()).into(holder.image_product)

        holder.text_product_name.text = item.productName
        holder.text_product_price.text = holder.itemView.context.getString(R.string.format_origin_price, FormatUtil.getMoneyTypeFloat(item.productPrice.toString()))
        holder.text_price.text = holder.itemView.context.getString(R.string.format_join_price, FormatUtil.getMoneyTypeFloat(item.price.toString()))

        if(StringUtils.isNotEmpty(item.impression)){
            holder.text_impression.visibility = View.VISIBLE
            holder.text_impression.text = item.impression
        }else{
            holder.text_impression.visibility = View.GONE
        }

        holder.text_reply_count.text = holder.itemView.context.getString(R.string.format_reply_count, item.replyCount.toString())

        holder.text_reply_count.setOnClickListener {

            val intent = Intent(holder.itemView.context, LuckyBoxReplyActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.putExtra(Const.POSITION, holder.absoluteAdapterPosition)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            replyLauncher?.launch(intent)
            (holder.itemView.context as BaseActivity).overridePendingTransition(R.anim.view_up, R.anim.fix)
        }

        holder.itemView.setOnClickListener {
            listener?.onClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

}