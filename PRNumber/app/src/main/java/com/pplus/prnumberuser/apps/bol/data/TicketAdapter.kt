package com.pplus.prnumberuser.apps.bol.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.model.dto.Bol
import com.pplus.prnumberuser.databinding.ItemPointBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class TicketAdapter() : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    var mDataList: MutableList<Bol>? = null
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

    fun getItem(position: Int): Bol {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Bol>? {

        return mDataList
    }

    fun add(data: Bol) {

        if (mDataList == null) {
            mDataList = ArrayList<Bol>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Bol>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Bol>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Bol) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Bol>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Bol>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemPointBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        var name: String? = null

        try{
            holder.binding.textPointName.text= ""
            var type = EnumData.BolType.admin.name

            type = item.secondaryType!!

            when (type) {
                EnumData.BolType.giftBol.name -> {
                    name = holder.itemView.context.getString(R.string.type_giftBol)
                }
                EnumData.BolType.giftBols.name -> {
                    name = holder.itemView.context.getString(R.string.type_giftBols)
                }
                EnumData.BolType.exchange.name -> {
                    name = holder.itemView.context.getString(R.string.type_exchange)
                }
                EnumData.BolType.buyMobileGift.name -> {
                    name = holder.itemView.context.getString(R.string.type_buyMobileGift)
                }
                EnumData.BolType.refundMobileGift.name -> {
                    name = holder.itemView.context.getString(R.string.type_buyMobileGift)
                }
                EnumData.BolType.buy.name -> {
                    name = holder.itemView.context.getString(R.string.type_buy)
                }
                EnumData.BolType.joinEvent.name -> {
                    name = holder.itemView.context.getString(R.string.type_joinEvent)
                }
                EnumData.BolType.winEvent.name -> {
                    name = holder.itemView.context.getString(R.string.type_winEvent)
                }
                EnumData.BolType.invite.name -> {
                    name = holder.itemView.context.getString(R.string.type_invite)
                }
                EnumData.BolType.invitee.name -> {
                    name = holder.itemView.context.getString(R.string.type_invitee)
                }
                EnumData.BolType.sendPush.name -> {

                }
                EnumData.BolType.recvPush.name -> {
                    name = holder.itemView.context.getString(R.string.type_recvPush)
                }
                EnumData.BolType.review.name -> {
                    name = holder.itemView.context.getString(R.string.type_review)
                }
                EnumData.BolType.rewardReview.name -> {
                    name = holder.itemView.context.getString(R.string.type_rewardReview)
                }
                EnumData.BolType.comment.name -> {
                    name = holder.itemView.context.getString(R.string.type_comment)
                }
                EnumData.BolType.rewardComment.name -> {
                    name = holder.itemView.context.getString(R.string.type_rewardComment)
                }
                EnumData.BolType.recvGift.name -> {
                    name = holder.itemView.context.getString(R.string.type_recvGift)
                }
                EnumData.BolType.reqExchange.name -> {
                    name = holder.itemView.context.getString(R.string.type_reqExchange)
                }
                EnumData.BolType.denyExchange.name -> {
                    name = holder.itemView.context.getString(R.string.type_denyExchange)
                }
                EnumData.BolType.denyRecv.name -> {
                    name = holder.itemView.context.getString(R.string.type_denyRecv)
                }

                EnumData.BolType.purchaseMobileGift.name -> {
                    name = holder.itemView.context.getString(R.string.type_purchaseMobileGift)
                }
                EnumData.BolType.winAdvertise.name -> {
                    name = holder.itemView.context.getString(R.string.type_winAdvertise)
                }
                EnumData.BolType.useAdvertise.name -> {
                    name = holder.itemView.context.getString(R.string.type_useAdvertise)
                }
                EnumData.BolType.refundAdvertise.name -> {
                    name = holder.itemView.context.getString(R.string.type_refundAdvertise)
                }
                EnumData.BolType.adpcReward.name->{
                    name = holder.itemView.context.getString(R.string.type_adpcReward)
                }
                EnumData.BolType.winRecommend.name->{
                    name = holder.itemView.context.getString(R.string.type_winRecommend)
                }
                EnumData.BolType.joinMember.name->{
                    name = holder.itemView.context.getString(R.string.type_joinMember)
                }
                EnumData.BolType.joinReduceEvent.name->{
                    name = holder.itemView.context.getString(R.string.type_joinReduceEvent2)
                }
                EnumData.BolType.refundJoinEvent.name->{
                    name = holder.itemView.context.getString(R.string.type_refundJoinEvent)
                }
                EnumData.BolType.admin.name->{
                    name = holder.itemView.context.getString(R.string.type_admin)
                }
                EnumData.BolType.refundAdmin.name->{
                    name = holder.itemView.context.getString(R.string.type_refundAdmin)
                }
                EnumData.BolType.buyGoodsUse.name->{
                    name = holder.itemView.context.getString(R.string.type_buyGoodsUse)
                }
            }

            holder.binding.textPointName.text = name
        }catch (e:Exception){
            holder.binding.textPointName.text = ""
        }

        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.binding.textPointDate.text = output.format(d)

        } catch (e: Exception) {
            holder.binding.textPointDate.text = ""
        }

        when (EnumData.BolType.valueOf(item.secondaryType!!)) {

            EnumData.BolType.giftBol, EnumData.BolType.giftBols, EnumData.BolType.exchange, EnumData.BolType.buyMobileGift, EnumData.BolType.sendPush,
            EnumData.BolType.rewardReview, EnumData.BolType.rewardComment, EnumData.BolType.reqExchange, EnumData.BolType.purchaseMobileGift, EnumData.BolType.useAdvertise, EnumData.BolType.joinReduceEvent, EnumData.BolType.refundAdmin-> {
                holder.binding.textPointPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff0000))
                holder.binding.textPointPrice.text = "- ${holder.itemView.context.getString(R.string.format_ticket_unit, FormatUtil.getMoneyType(item.amount))}"
            }

            EnumData.BolType.buy, EnumData.BolType.winEvent, EnumData.BolType.invite, EnumData.BolType.invitee, EnumData.BolType.recvPush,
            EnumData.BolType.review, EnumData.BolType.comment, EnumData.BolType.recvGift, EnumData.BolType.denyExchange, EnumData.BolType.denyRecv, EnumData.BolType.winAdvertise, EnumData.BolType.joinEvent, EnumData.BolType.refundAdvertise, EnumData.BolType.adpcReward
                , EnumData.BolType.winRecommend, EnumData.BolType.joinMember, EnumData.BolType.refundJoinEvent, EnumData.BolType.admin, EnumData.BolType.refundMobileGift, EnumData.BolType.buyGoodsUse -> {
                holder.binding.textPointPrice.text = "${holder.itemView.context.getString(R.string.format_ticket_unit, FormatUtil.getMoneyType(item.amount))}"
                holder.binding.textPointPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}