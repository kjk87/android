package com.lejel.wowbox.apps.luckydraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyDrawNumber
import com.lejel.wowbox.core.network.model.dto.LuckyDrawPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyDrawPurchaseBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawPurchaseAdapter() : RecyclerView.Adapter<LuckyDrawPurchaseAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawPurchase>? = null
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

    fun getItem(position: Int): LuckyDrawPurchase {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawPurchase>? {

        return mDataList
    }

    fun add(data: LuckyDrawPurchase) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawPurchase>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawPurchase>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawPurchase>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawPurchase) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawPurchase>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawPurchase>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textLuckyDrawPurchaseTitle.text = item.title
        if(item.engagedPrice != null && item.engagedPrice!! > 0){
            holder.binding.textLuckyDrawPurchaseEngageBallTitle.visibility = View.VISIBLE
            holder.binding.textLuckyDrawPurchaseEngageBall.visibility = View.VISIBLE
            holder.binding.textLuckyDrawPurchaseEngageBall.text = holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyType(item.engagedPrice.toString()))
        }else{
            holder.binding.textLuckyDrawPurchaseEngageBallTitle.visibility = View.GONE
            holder.binding.textLuckyDrawPurchaseEngageBall.visibility = View.GONE
        }


        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textLuckyDrawPurchaseDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))


        val numbers = item.winNumber!!.split(":")
        val list = arrayListOf<LuckyDrawNumber>()
        for (number in numbers) {
            if (StringUtils.isNotEmpty(number)) {
                val selectNumbers = number.split("|")
                val luckyDrawNumber = LuckyDrawNumber()
                when (selectNumbers.size) {
                    1 -> {
                        luckyDrawNumber.first = selectNumbers[0]
                    }

                    2 -> {
                        luckyDrawNumber.first = selectNumbers[0]
                        luckyDrawNumber.third = selectNumbers[1]
                    }

                    3 -> {
                        luckyDrawNumber.first = selectNumbers[0]
                        luckyDrawNumber.second = selectNumbers[1]
                        luckyDrawNumber.third = selectNumbers[2]
                    }
                }
                luckyDrawNumber.winNumber = number
                list.add(luckyDrawNumber)
            }
        }

        holder.binding.recyclerLuckyDrawPurchaseNumber.layoutManager = LinearLayoutManager(holder.itemView.context)
        val adapter = LuckyDrawNumberAdapter()
        holder.binding.recyclerLuckyDrawPurchaseNumber.adapter = adapter
        adapter.setDataList(list)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}