package com.lejel.wowbox.apps.giftcard.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.giftcard.ui.AlertGiftCardReturnReasonActivity
import com.lejel.wowbox.core.network.model.dto.GiftCardPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemGiftCardPurchaseBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class GiftCardPurchaseAdapter() : RecyclerView.Adapter<GiftCardPurchaseAdapter.ViewHolder>() {

    var mDataList: MutableList<GiftCardPurchase>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun getItem(position: Int): GiftCardPurchase {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<GiftCardPurchase>? {

        return mDataList
    }

    fun add(data: GiftCardPurchase) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GiftCardPurchase>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GiftCardPurchase) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<GiftCardPurchase>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GiftCardPurchase>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemGiftCardPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.giftCardImage).apply(RequestOptions().centerCrop()).into(holder.binding.imageGiftCardPurchase)
        holder.binding.textGiftCardPurchaseTitle.text = item.giftCardName
        holder.binding.textGiftCardPurchasePoint.text = FormatUtil.getMoneyType(item.usePoint.toString())

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textGiftCardPurchaseDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))

        holder.binding.textGiftCardPurchaseEmail.text = item.buyerEmail

        when (item.deliveryStatus) { //0:주문확인전, 1:발송대기, 2:발송반려, 3:발송완료
            0, 1 -> {
                holder.binding.textGiftCardPurchaseStatus.setBackgroundResource(R.drawable.bg_dbb5b5b5_radius_20)
                holder.binding.textGiftCardPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textGiftCardPurchaseStatus.setText(R.string.word_send_wait)
                holder.binding.textGiftCardPurchaseStatus.setOnClickListener {  }
            }

            2 -> {
                holder.binding.textGiftCardPurchaseStatus.setBackgroundResource(R.drawable.bg_24ff5e5e_radius_20)
                holder.binding.textGiftCardPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff5e5e))
                holder.binding.textGiftCardPurchaseStatus.setText(R.string.msg_view_return_reason)
                holder.binding.textGiftCardPurchaseStatus.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AlertGiftCardReturnReasonActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }
            }
            3 -> {
                holder.binding.textGiftCardPurchaseStatus.setBackgroundResource(R.drawable.bg_dbb5b5b5_radius_20)
                holder.binding.textGiftCardPurchaseStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_3e78ff))
                holder.binding.textGiftCardPurchaseStatus.setText(R.string.word_send_complete)
                holder.binding.textGiftCardPurchaseStatus.setOnClickListener {  }
            }

        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftCardPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}