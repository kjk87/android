package com.pplus.prnumberuser.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.GiftishowBuy
import com.pplus.prnumberuser.databinding.ItemMobileGiftHistoryBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class GiftishowBuyHistoryAdapter() : RecyclerView.Adapter<GiftishowBuyHistoryAdapter.ViewHolder>() {
    private var mDataList: MutableList<GiftishowBuy>?
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    init {
        setHasStableIds(true)
        mDataList = ArrayList()
    }

    override fun getItemId(position: Int): Long {
        return mDataList!![position].seqNo!!
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun getItem(position: Int): GiftishowBuy {
        return mDataList!![position]
    }

    fun add(data: GiftishowBuy) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GiftishowBuy>?) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GiftishowBuy) {
        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GiftishowBuy>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMobileGiftHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var image = binding.imageMobileGiftHistory
        var text_name = binding.textMobileGiftHistoryName
        var text_price = binding.textMobileGiftHistoryPrice
        var text_date = binding.textMobileGiftHistoryDate
        var text_receiver = binding.textMobileGiftHistoryReceiver

        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileGiftHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.giftishow!!.goodsImgS).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        holder.text_name.text = item.giftishow!!.goodsName
        holder.text_price.text = holder.itemView.context.getString(R.string.format_product_price, FormatUtil.getMoneyType(item.giftishow!!.realPrice.toString()))
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = holder.itemView.context.getString(R.string.format_buy_date, output.format(d))
        } catch (e: Exception) {
        }
        if (item.totalCount!! > 1) {
            holder.text_receiver.text = holder.itemView.context.getString(R.string.format_send_target, holder.itemView.context.getString(R.string.format_other, item.targetList!![0].name, item.totalCount!! - 1))
        } else {
            if(StringUtils.isNotEmpty(item.targetList!![0].name)){
                holder.text_receiver.text = holder.itemView.context.getString(R.string.format_send_target, item.targetList!![0].name)
            }else{
                holder.text_receiver.text = holder.itemView.context.getString(R.string.format_send_target, holder.itemView.context.getString(R.string.word_me))
            }

        }
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }
}