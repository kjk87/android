package com.pplus.luckybol.apps.buff.data

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.BuffDividedBolLog
import com.pplus.luckybol.databinding.ItemBuffLogBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffLogAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<BuffLogAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffDividedBolLog>? = null
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

    fun getItem(position: Int): BuffDividedBolLog {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffDividedBolLog>? {

        return mDataList
    }

    fun add(data: BuffDividedBolLog) {

        if (mDataList == null) {
            mDataList = arrayListOf()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffDividedBolLog>) {

        if (this.mDataList == null) {
            this.mDataList = arrayListOf()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffDividedBolLog) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuffDividedBolLog>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffDividedBolLog>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBuffLogBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBuffLogProfile
        val text_name = binding.textBuffLogName
        val text_type = binding.textBuffLogType
        val text_date = binding.textBuffLogDate
        val text_amount = binding.textBuffLogAmount

        init {
            text_name.setSingleLine()

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if (item.member!!.profileAttachment != null) {
            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_contact_profile_default)
        }

        holder.text_name.text= item.member!!.nickname
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = output.format(d)

        } catch (e: Exception) {
            holder.text_date.text = ""
        }

        when (item.type) {
            "shopping"-> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_shopping_save)
            }
            "lotto"-> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_lotto_win_save)
            }
            "event"-> {
                holder.text_type.text = holder.itemView.context.getString(R.string.word_event_win_save)
            }
        }

        when(item.moneyType){
            "bol"->{
                holder.text_amount.text = holder.itemView.context.getString(R.string.format_divided_bol, FormatUtil.getMoneyTypeFloat(item.amount.toString()))
            }
            "point"->{
                holder.text_amount.text = holder.itemView.context.getString(R.string.format_divided_cash, FormatUtil.getMoneyTypeFloat(item.amount.toString()))
            }
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuffLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}