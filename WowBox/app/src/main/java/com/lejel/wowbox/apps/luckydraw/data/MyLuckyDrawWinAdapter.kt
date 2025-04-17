package com.lejel.wowbox.apps.luckydraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWin
import com.lejel.wowbox.databinding.ItemMyLuckyDrawWinBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MyLuckyDrawWinAdapter() : RecyclerView.Adapter<MyLuckyDrawWinAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawWin>? = null
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

    fun getItem(position: Int): LuckyDrawWin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawWin>? {

        return mDataList
    }

    fun add(data: LuckyDrawWin) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawWin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawWin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawWin) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawWin>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemMyLuckyDrawWinBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textMyLuckyDrawWinTitle.text = item.luckyDraw!!.title
        Glide.with(holder.itemView.context).load(item.giftImage).apply(RequestOptions().centerCrop()).into(holder.binding.imageMyLuckyDrawWinGift)
        holder.binding.textMyLuckyDrawWinGiftTitle.text = item.giftName
        holder.binding.textMyLuckyDrawWinGiftPrice.text = FormatUtil.getMoneyTypeFloat(item.giftPrice.toString())

        when(item.luckyDrawGift!!.type){//goods, coin, giftCard, ball, point, buffCoin
            "goods", "coin", "giftCard"->{
                holder.binding.textMyLuckyDrawWinGiftPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(item.giftPrice.toString()))
            }
            "ball"->{
                holder.binding.textMyLuckyDrawWinGiftPrice.text = holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(item.giftPrice.toString()))
            }
            "point"->{
                holder.binding.textMyLuckyDrawWinGiftPrice.text = holder.itemView.context.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(item.giftPrice.toString()))
            }
        }

        val winNumbers = item.winNumber!!.split("|")
        when (winNumbers.size) {
            1 -> {

                holder.binding.textMyLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textMyLuckyDrawWinNumberSecond.visibility = View.GONE
                holder.binding.textMyLuckyDrawWinNumberThird.visibility = View.GONE

                holder.binding.textMyLuckyDrawWinNumberFirst.text = winNumbers[0]
            }

            2 -> {
                holder.binding.textMyLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textMyLuckyDrawWinNumberSecond.visibility = View.GONE
                holder.binding.textMyLuckyDrawWinNumberThird.visibility = View.VISIBLE

                holder.binding.textMyLuckyDrawWinNumberFirst.text = winNumbers[0]
                holder.binding.textMyLuckyDrawWinNumberThird.text = winNumbers[1]
            }

            3 -> {
                holder.binding.textMyLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textMyLuckyDrawWinNumberSecond.visibility = View.VISIBLE
                holder.binding.textMyLuckyDrawWinNumberThird.visibility = View.VISIBLE

                holder.binding.textMyLuckyDrawWinNumberFirst.text = winNumbers[0]
                holder.binding.textMyLuckyDrawWinNumberSecond.text = winNumbers[1]
                holder.binding.textMyLuckyDrawWinNumberThird.text = winNumbers[2]
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyLuckyDrawWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}