package com.pplus.luckybol.apps.event.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.databinding.ItemLottoBinding
import com.pplus.luckybol.databinding.ItemLottoWinnerBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoWinnerAdapter() : RecyclerView.Adapter<LottoWinnerAdapter.ViewHolder>() {

    var mDataList: MutableList<EventWinJpa>? = null
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

    fun getItem(position: Int): EventWinJpa {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventWinJpa>? {

        return mDataList
    }

    fun add(data: EventWinJpa) {

        if (mDataList == null) {
            mDataList = ArrayList<EventWinJpa>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventWinJpa>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventWinJpa>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventWinJpa) {
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<EventWinJpa>()

    }

    fun setDataList(dataList: MutableList<EventWinJpa>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLottoWinnerBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageLottoWinnerProfile
        val text_nickname = binding.textLottoWinnerNickname
        val text_impression = binding.textLottoWinnerImpression
        val text_gift_name = binding.textLottoWinnerGiftName
        val layout_lotto_winner_number = binding.layoutLottoWinnerNumber

        init {
            text_nickname.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if (item.member!!.profileAttachment != null) {
            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_event_profile_default)
        }

        holder.text_nickname.text = item.member!!.nickname
        holder.text_impression.text = item.impression
        holder.text_gift_name.text = item.giftTitle

        for (j in 0 until item.eventJoin!!.lottoSelectedNumberList!!.size) {
            val lottoNumber = item.eventJoin!!.lottoSelectedNumberList!![j].lottoNumber
            val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
            lottoBinding.textLottoNumber.text = lottoNumber.toString()

            lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_72)
            lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_72)

            if (lottoNumber in 1..10) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_1)
            } else if (lottoNumber in 11..20) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_2)
            } else if (lottoNumber in 21..30) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_3)
            } else if (lottoNumber in 31..40) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_4)
            } else {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_5)
            }

            val isAccord = item.eventJoin!!.lottoSelectedNumberList!![j].isAccord != null && item.eventJoin!!.lottoSelectedNumberList!![j].isAccord!!
            lottoBinding.textLottoNumber.isSelected = isAccord

            holder.layout_lotto_winner_number.addView(lottoBinding.root)
            if (j < item.eventJoin!!.lottoSelectedNumberList!!.size - 1) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_24)
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoWinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}