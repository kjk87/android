package com.pplus.luckybol.apps.event.data

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.EventJoinJpa
import com.pplus.luckybol.databinding.ItemLottoBinding
import com.pplus.luckybol.databinding.ItemLottoJoinNumberBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoJoinListAdapter() : RecyclerView.Adapter<LottoJoinListAdapter.ViewHolder>() {

    var mDataList: MutableList<EventJoinJpa>? = null
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

    fun getItem(position: Int): EventJoinJpa {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<EventJoinJpa>? {

        return mDataList
    }

    fun add(data: EventJoinJpa) {

        if (mDataList == null) {
            mDataList = ArrayList<EventJoinJpa>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventJoinJpa>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventJoinJpa>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventJoinJpa) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<EventJoinJpa>()
    }

    fun setDataList(dataList: MutableList<EventJoinJpa>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding:ItemLottoJoinNumberBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_join_date = binding.textLottoJoinDate
        val layout_join_number = binding.layoutLottoJoinNumber

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_join_date.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.textSize_34pt).toFloat())
        holder.text_join_date.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
        val output = SimpleDateFormat("yyyy.MM.dd")
        holder.text_join_date.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.joinDatetime))
        for (i in 0 until item.lottoSelectedNumberList!!.size) {
            val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoBinding.textLottoNumber.text = item.lottoSelectedNumberList!![i].lottoNumber.toString()
            lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_84)
            lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_84)

            if(item.lottoSelectedNumberList!![i].isAccord != null && item.lottoSelectedNumberList!![i].isAccord!!){
                lottoBinding.textLottoNumber.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_212529))
                val lottoNumber = item.lottoSelectedNumberList!![i].lottoNumber
                if (lottoNumber in 1..10) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_f2c443)
                } else if (lottoNumber in 11..20) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_85c5f1)
                } else if (lottoNumber in 21..30) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_e4807b)
                } else if (lottoNumber in 31..40) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_a689ee)
                } else {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_57d281)
                }
            }else{
                lottoBinding.textLottoNumber.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_8c969f))
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_line)
            }


            holder.layout_join_number.addView(lottoBinding.root)
            if (i < item.lottoSelectedNumberList!!.size - 1) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_36)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoJoinNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}