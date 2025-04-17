package com.pplus.prnumberbiz.apps.bol.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Bol
import kotlinx.android.synthetic.main.item_point.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PointAdapter : RecyclerView.Adapter<PointAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Bol>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    constructor() : super()

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Bol {

        return mDataList!!.get(position)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_name = itemView.text_point_name
        val text_date = itemView.text_point_date
        val text_price = itemView.text_point_price

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: Bol = mDataList!!.get(position)

        var name: String? = null

        try{
            holder.text_name.text= ""
            val type = EnumData.BolType.valueOf(item.secondaryType!!)

            when (type) {
                EnumData.BolType.giftBol -> {
                    name = mContext?.getString(R.string.type_giftBol)
                }
                EnumData.BolType.giftBols -> {
                    name = mContext?.getString(R.string.type_giftBols)
                }
                EnumData.BolType.exchange -> {
                    name = mContext?.getString(R.string.type_exchange)
                }
                EnumData.BolType.buyMobileGift -> {
                    name = mContext?.getString(R.string.type_buyMobileGift)
                }
                EnumData.BolType.buy -> {
                    name = mContext?.getString(R.string.type_buy)
                }
                EnumData.BolType.joinEvent -> {
                    name = mContext?.getString(R.string.type_joinEvent)
                }
                EnumData.BolType.winEvent -> {
                    name = mContext?.getString(R.string.type_winEvent)
                }
                EnumData.BolType.invite -> {
                    name = mContext?.getString(R.string.type_invite)
                }
                EnumData.BolType.invitee -> {
                    name = mContext?.getString(R.string.type_invitee)
                }
                EnumData.BolType.sendPush -> {

                }
                EnumData.BolType.recvPush -> {
                    name = mContext?.getString(R.string.type_recvPush)
                }
                EnumData.BolType.review -> {
                    name = mContext?.getString(R.string.type_review)
                }
                EnumData.BolType.rewardReview -> {
                    name = mContext?.getString(R.string.type_rewardReview)
                }
                EnumData.BolType.comment -> {
                    name = mContext?.getString(R.string.type_comment)
                }
                EnumData.BolType.rewardComment -> {
                    name = mContext?.getString(R.string.type_rewardComment)
                }
                EnumData.BolType.recvGift -> {
                    name = mContext?.getString(R.string.type_recvGift)
                }
                EnumData.BolType.reqExchange -> {
                    name = mContext?.getString(R.string.type_reqExchange)
                }
                EnumData.BolType.denyExchange -> {
                    name = mContext?.getString(R.string.type_denyExchange)
                }
                EnumData.BolType.denyRecv -> {
                    name = mContext?.getString(R.string.type_denyRecv)
                }

                EnumData.BolType.purchaseMobileGift -> {
                    name = mContext?.getString(R.string.type_purchaseMobileGift)
                }
                EnumData.BolType.winAdvertise -> {
                    name = mContext?.getString(R.string.type_winAdvertise)
                }
                EnumData.BolType.useAdvertise -> {
                    name = mContext?.getString(R.string.type_useAdvertise)
                }
                EnumData.BolType.refundAdvertise -> {
                    name = mContext?.getString(R.string.type_refundAdvertise)
                }
                EnumData.BolType.adpcReward->{
                    name = mContext?.getString(R.string.type_adpcReward)
                }
                EnumData.BolType.winRecommend->{
                    name = mContext?.getString(R.string.type_winRecommend)
                }
                EnumData.BolType.joinMember->{
                    name = mContext?.getString(R.string.type_joinMember)
                }
                EnumData.BolType.joinReduceEvent->{
                    name = mContext?.getString(R.string.type_joinReduceEvent)
                }
                EnumData.BolType.refundJoinEvent->{
                    name = mContext?.getString(R.string.type_refundJoinEvent)
                }
                EnumData.BolType.admin->{
                    name = mContext?.getString(R.string.type_admin)
                }
                EnumData.BolType.refundAdmin->{
                    name = mContext?.getString(R.string.type_refundAdmin)
                }
            }

            holder.text_name.text = name
        }catch (e:Exception){

        }

        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            holder.text_date.text = output.format(d)

        } catch (e: Exception) {

        }

        when (EnumData.BolType.valueOf(item.secondaryType!!)) {

            EnumData.BolType.giftBol, EnumData.BolType.giftBols, EnumData.BolType.exchange, EnumData.BolType.buyMobileGift, EnumData.BolType.sendPush,
            EnumData.BolType.rewardReview, EnumData.BolType.rewardComment, EnumData.BolType.reqExchange, EnumData.BolType.purchaseMobileGift, EnumData.BolType.useAdvertise, EnumData.BolType.joinReduceEvent, EnumData.BolType.refundAdmin-> {
                holder.text_price.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff0000))
                holder.text_price.text = "- ${mContext!!.getString(R.string.format_point_unit, FormatUtil.getMoneyType(item.amount))}"
            }

            EnumData.BolType.buy, EnumData.BolType.winEvent, EnumData.BolType.invite, EnumData.BolType.invitee, EnumData.BolType.recvPush,
            EnumData.BolType.review, EnumData.BolType.comment, EnumData.BolType.recvGift, EnumData.BolType.denyExchange, EnumData.BolType.denyRecv, EnumData.BolType.winAdvertise, EnumData.BolType.joinEvent, EnumData.BolType.refundAdvertise, EnumData.BolType.adpcReward
                , EnumData.BolType.winRecommend, EnumData.BolType.joinMember, EnumData.BolType.refundJoinEvent, EnumData.BolType.admin -> {
                holder.text_price.text = "${mContext!!.getString(R.string.format_point_unit, FormatUtil.getMoneyType(item.amount))}"
                holder.text_price.setTextColor(ResourceUtil.getColor(mContext, R.color.color_579ffb))
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false)
        return ViewHolder(v)
    }
}