package com.lejel.wowbox.apps.lottery.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LotteryJoin
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLottoBinding
import com.lejel.wowbox.databinding.ItemLottoJoinNumberBinding
import java.text.SimpleDateFormat



/**
 * Created by imac on 2018. 1. 8..
 */
class LotteryJoinListAdapter() : RecyclerView.Adapter<LotteryJoinListAdapter.ViewHolder>() {

    var mDataList: MutableList<LotteryJoin>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LotteryJoin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LotteryJoin>? {

        return mDataList
    }

    fun add(data: LotteryJoin) {

        if (mDataList == null) {
            mDataList = ArrayList<LotteryJoin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LotteryJoin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LotteryJoin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LotteryJoin) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LotteryJoin>()
    }

    fun setDataList(dataList: MutableList<LotteryJoin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLottoJoinNumberBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        val output = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        val regDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))

        if (position == 0) {
            holder.binding.layoutLottoJoinDate.visibility = View.VISIBLE
            holder.binding.textLottoJoinDate.text = regDate
        } else {
            val beforeItem = mDataList!![position - 1]
            val beforeDate = output.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(beforeItem.regDatetime)))
            if (regDate.equals(beforeDate)) {
                holder.binding.layoutLottoJoinDate.visibility = View.GONE
            } else {
                holder.binding.layoutLottoJoinDate.visibility = View.VISIBLE
                holder.binding.textLottoJoinDate.text = regDate
            }
        }

        val numberList = arrayListOf<Int>()
        numberList.add(item.no1!!)
        numberList.add(item.no2!!)
        numberList.add(item.no3!!)
        numberList.add(item.no4!!)
        numberList.add(item.no5!!)
        numberList.add(item.no6!!)

        holder.binding.layoutLottoJoinNumber.removeAllViews()
        for ((i, number) in numberList.withIndex()) {
            val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoBinding.textLottoNumber.text = number.toString()
            lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_120)
            lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_120)

            if (number in 1..10) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
            } else if (number in 11..20) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
            } else if (number in 21..30) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
            } else if (number in 31..40) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
            } else {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
            }

            holder.binding.layoutLottoJoinNumber.addView(lottoBinding.root)
            if (i < numberList.size - 1) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_6)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoJoinNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}