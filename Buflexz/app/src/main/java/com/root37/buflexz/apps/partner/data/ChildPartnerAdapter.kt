package com.root37.buflexz.apps.partner.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemChildpartnerBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class ChildPartnerAdapter() : RecyclerView.Adapter<ChildPartnerAdapter.ViewHolder>() {

    var mDataList: MutableList<Partner>? = null
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

    fun getItem(position: Int): Partner {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Partner>? {

        return mDataList
    }

    fun add(data: Partner) {

        if (mDataList == null) {
            mDataList = ArrayList<Partner>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Partner>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Partner>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Partner) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Partner>()
    }

    fun setDataList(dataList: MutableList<Partner>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemChildpartnerBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(Const.CDN_URL + "profile/${item.memberTotal!!.userKey}/index.html").apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(holder.binding.imageChildPartnerProfile)
        holder.binding.textChildPartnerNickname.text = item.memberTotal!!.nickname
        Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${item.memberTotal!!.nation!!.uppercase()}.png")).into(holder.binding.imageChildPartnerFlag)

        val nation = NationManager.getInstance().nationMap!![item.memberTotal!!.nation]
        if(nation!!.code == "KR"){
            holder.binding.textChildPartnerNation.text = nation.name
        }else{
            holder.binding.textChildPartnerNation.text = nation.nameEn
        }

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textChildPartnerDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.requestDatetime)))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChildpartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}