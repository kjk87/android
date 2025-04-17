package com.lejel.wowbox.apps.partner.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.NationManager
import com.lejel.wowbox.core.network.model.dto.ProfitPartner
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemChildProfitPartnerBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class ChildProfitPartnerAdapter() : RecyclerView.Adapter<ChildProfitPartnerAdapter.ViewHolder>() {

    var mDataList: MutableList<ProfitPartner>? = null
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

    fun getItem(position: Int): ProfitPartner {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProfitPartner>? {

        return mDataList
    }

    fun add(data: ProfitPartner) {

        if (mDataList == null) {
            mDataList = ArrayList<ProfitPartner>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProfitPartner>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProfitPartner>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProfitPartner) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<ProfitPartner>()
    }

    fun setDataList(dataList: MutableList<ProfitPartner>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemChildProfitPartnerBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageChildProfitPartnerProfile)
        holder.binding.textChildProfitPartnerNickname.text = item.memberTotal!!.nickname

        val calMonth = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format)).format(SimpleDateFormat("yyyy-MM-dd").parse(item.calculateMonth))
        holder.binding.textChildProfitPartnerProfitTitle.text = holder.itemView.context.getString(R.string.format_profit, calMonth)
        val profit = item.adProfit!! + item.ballProfit!!
        holder.binding.textChildProfitPartnerProfit.text = holder.itemView.context.getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(profit.toString()))
        val bonusProfit = profit*item.parentsPartnerPer!!
        holder.binding.textChildProfitPartnerBonusProfit.text = holder.itemView.context.getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(bonusProfit.toString()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChildProfitPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}