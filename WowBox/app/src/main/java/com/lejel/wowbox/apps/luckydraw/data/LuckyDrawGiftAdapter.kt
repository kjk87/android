package com.lejel.wowbox.apps.luckydraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGift
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyDrawGiftBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawGiftAdapter() : RecyclerView.Adapter<LuckyDrawGiftAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawGift>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyDrawGift {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawGift>? {

        return mDataList
    }

    fun add(data: LuckyDrawGift) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawGift>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawGift>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawGift>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawGift) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawGift>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawGift>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textLuckyDrawGiftGrade.text = item.grade.toString()
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().centerCrop()).into(holder.binding.imageLuckyDrawGift)
        holder.binding.textLuckyDrawGiftTitle.text = item.title
        holder.binding.textLuckyDrawGiftPrice.text = FormatUtil.getMoneyTypeFloat(item.price.toString())
        holder.binding.textLuckyDrawGiftCount.text = holder.itemView.context.getString(R.string.format_lucky_draw_gift_count, item.amount.toString())

        when(item.type){//goods, coin, giftCard, ball, point, buffCoin
            "goods", "coin", "giftCard"->{
                holder.binding.textLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
            }
            "ball"->{
                holder.binding.textLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
            }
            "point"->{
                holder.binding.textLuckyDrawGiftPrice.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(item.price.toString()))
            }
        }

        holder.itemView.setOnClickListener {
            if(StringUtils.isNotEmpty(item.giftLink)){
                PplusCommonUtil.openChromeWebView(holder.itemView.context, item.giftLink!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}