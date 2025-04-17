package com.root37.buflexz.apps.partner.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.core.network.model.dto.ProfitPartner
import com.root37.buflexz.databinding.ItemChildProfitPartnerBinding


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

        Glide.with(holder.itemView.context).load(Const.CDN_URL + "profile/${item.memberTotal!!.userKey}/index.html").apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(holder.binding.imageChildProfitPartnerProfile)
        holder.binding.textChildProfitPartnerNickname.text = item.memberTotal!!.nickname
        Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${item.memberTotal!!.nation!!.uppercase()}.png")).into(holder.binding.imageChildProfitPartnerFlag)

        val nation = NationManager.getInstance().nationMap!![item.memberTotal!!.nation]
        if(nation!!.code == "KR"){
            holder.binding.textChildProfitPartnerNation.text = nation.name
        }else{
            holder.binding.textChildProfitPartnerNation.text = nation.nameEn
        }

        holder.binding.textChildProfitPartnerProfitTitle.text = holder.itemView.context.getString(R.string.format_profit, item.calculateMonth)
        val profit = item.adProfit!! + item.ballProfit!!
        holder.binding.textChildProfitPartnerProfit.text = holder.itemView.context.getString(R.string.format_dollar_unit, FormatUtil.getMoneyTypeFloat(profit.toString()))


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChildProfitPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}