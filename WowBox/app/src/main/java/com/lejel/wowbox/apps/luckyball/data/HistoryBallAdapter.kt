package com.lejel.wowbox.apps.luckyball.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.HistoryBall
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemHistoryBallBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class HistoryBallAdapter() : RecyclerView.Adapter<HistoryBallAdapter.ViewHolder>() {

    var mDataList: MutableList<HistoryBall>? = null
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

    fun getItem(position: Int): HistoryBall {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<HistoryBall>? {

        return mDataList
    }

    fun add(data: HistoryBall) {

        if (mDataList == null) {
            mDataList = ArrayList<HistoryBall>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<HistoryBall>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<HistoryBall>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: HistoryBall) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<HistoryBall>()
    }

    fun setDataList(dataList: MutableList<HistoryBall>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemHistoryBallBinding) : RecyclerView.ViewHolder(binding.root) {

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
        holder.binding.textHistoryBallRegDate.text = regDate
        holder.binding.textHistoryBallSubject.text = item.subject
        when(item.type){
            "used", "retrieve"->{
                holder.binding.textHistoryBall.text = "- ${FormatUtil.getMoneyType(item.ball.toString())}"
            }
            "charge", "provide"->{
                holder.binding.textHistoryBall.text = FormatUtil.getMoneyType(item.ball.toString())
            }
        }
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}