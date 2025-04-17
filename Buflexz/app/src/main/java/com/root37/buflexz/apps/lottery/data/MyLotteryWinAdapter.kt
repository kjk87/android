package com.root37.buflexz.apps.lottery.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.LotteryWin
import com.root37.buflexz.databinding.ItemLottoBinding
import com.root37.buflexz.databinding.ItemMyLotteryWinBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MyLotteryWinAdapter() : RecyclerView.Adapter<MyLotteryWinAdapter.ViewHolder>() {

    var mDataList: MutableList<LotteryWin>? = null
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

    fun getItem(position: Int): LotteryWin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LotteryWin>? {

        return mDataList
    }

    fun add(data: LotteryWin) {

        if (mDataList == null) {
            mDataList = ArrayList<LotteryWin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LotteryWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LotteryWin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LotteryWin) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LotteryWin>()
    }

    fun setDataList(dataList: MutableList<LotteryWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemMyLotteryWinBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        when(item.grade){
            1->{
                holder.binding.textMyLotteryWinGrade.text = holder.itemView.context.getString(R.string.word_grade_1)
            }
            2->{
                holder.binding.textMyLotteryWinGrade.text = holder.itemView.context.getString(R.string.word_grade_2)
            }
            3->{
                holder.binding.textMyLotteryWinGrade.text = holder.itemView.context.getString(R.string.word_grade_3)
            }
            else->{
                holder.binding.textMyLotteryWinGrade.text = holder.itemView.context.getString(R.string.format_grade_other, item.grade.toString())
            }
        }

        when (item.status) {
            "active" -> {
                holder.binding.imageMyLotteryWinStatus.setImageResource(R.drawable.ic_my_lottery_win_get)
                holder.binding.imageMyLotteryWinStatus.setOnClickListener {
                    listener?.onItemClick(position)
                }
            }

            "complete" -> {
                holder.binding.imageMyLotteryWinStatus.setImageResource(R.drawable.ic_my_lottery_win_complete)
                holder.binding.imageMyLotteryWinStatus.setOnClickListener {

                }
            }
        }

        holder.binding.layoutMyLotteryWinNumber.removeAllViews()

        if (item.no1 != null) {
            val numberList = arrayListOf<Int>()
            numberList.add(item.no1!!)
            numberList.add(item.no2!!)
            numberList.add(item.no3!!)
            numberList.add(item.no4!!)
            numberList.add(item.no5!!)
            numberList.add(item.no6!!)

            val isWinList = arrayListOf<Boolean>()
            isWinList.add(item.winNo1!!)
            isWinList.add(item.winNo2!!)
            isWinList.add(item.winNo3!!)
            isWinList.add(item.winNo4!!)
            isWinList.add(item.winNo5!!)
            isWinList.add(item.winNo6!!)

            for (j in 0 until numberList.size) {
                val lottoNumber = numberList[j]
                val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                lottoBinding.textLottoNumber.text = lottoNumber.toString()

                lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_90)
                lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_90)

                if (lottoNumber in 1..10) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
                } else if (lottoNumber in 11..20) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
                } else if (lottoNumber in 21..30) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
                } else if (lottoNumber in 31..40) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
                } else {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
                }

                val isAccord = isWinList[j]

                if (item.grade == 2 && !isAccord) {
                    lottoBinding.textLottoNumber.isSelected = item.winAdd != null && item.winAdd!!
                } else {
                    lottoBinding.textLottoNumber.isSelected = isAccord
                }

                holder.binding.layoutMyLotteryWinNumber.addView(lottoBinding.root)
                if (j < numberList.size - 1) {
                    (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_6)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyLotteryWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}