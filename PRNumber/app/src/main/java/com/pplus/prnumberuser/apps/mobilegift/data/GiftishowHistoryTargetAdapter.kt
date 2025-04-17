package com.pplus.prnumberuser.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.GiftishowTarget
import com.pplus.prnumberuser.databinding.ItemMobileGiftHistoryTargetBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class GiftishowHistoryTargetAdapter() : RecyclerView.Adapter<GiftishowHistoryTargetAdapter.ViewHolder>() {
    private var mDataList: MutableList<GiftishowTarget>?
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemId(position: Int): Long {
        return mDataList!![position].seqNo!!
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun getItem(position: Int): GiftishowTarget {
        return mDataList!![position]
    }

    fun add(data: GiftishowTarget) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GiftishowTarget>?) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GiftishowTarget) {
        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GiftishowTarget>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMobileGiftHistoryTargetBinding) : RecyclerView.ViewHolder(binding.root) {
        var text_name = binding.textMobileGiftHistoryDetailTargetName
        var text_mobile_number = binding.textMobileGiftHistoryDetailTargetMobileNumber

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileGiftHistoryTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        if(StringUtils.isEmpty(item.name)){
            holder.text_name.text = holder.itemView.context.getString(R.string.word_me)
        }else{
            holder.text_name.text = item.name
        }

        holder.text_mobile_number.text = FormatUtil.getPhoneNumber(item.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    init {
        setHasStableIds(true)
        mDataList = ArrayList()
    }
}