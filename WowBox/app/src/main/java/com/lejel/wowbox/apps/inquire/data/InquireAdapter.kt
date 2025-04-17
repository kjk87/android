package com.lejel.wowbox.apps.inquire.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.Inquire
import com.lejel.wowbox.core.network.model.dto.InquireImage
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemInquireBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class InquireAdapter() : RecyclerView.Adapter<InquireAdapter.ViewHolder>() {

    var mDataList: MutableList<Inquire>? = null
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

    fun getItem(position: Int): Inquire {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Inquire>? {

        return mDataList
    }

    fun add(data: Inquire) {

        if (mDataList == null) {
            mDataList = ArrayList<Inquire>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Inquire>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Inquire>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Inquire) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Inquire>()
    }

    fun setDataList(dataList: MutableList<Inquire>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemInquireBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        when(item.status){
            "pending"->{
                holder.binding.textInquireStatus.setBackgroundResource(R.drawable.bg_eeeeee_radius_11)
                holder.binding.textInquireStatus.setText(R.string.word_pending_reply)
                holder.binding.textInquireStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_898989))
                holder.binding.textInquireReply.visibility = View.GONE
            }
            "complete"->{
                holder.binding.textInquireStatus.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                holder.binding.textInquireStatus.setText(R.string.word_complete_reply)
                holder.binding.textInquireStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textInquireReply.visibility = View.VISIBLE
                holder.binding.textInquireReply.text = item.reply
            }
        }

        when(item.type){//general, partnership, error, etc
            "general"->{
                holder.binding.textInquireType.setText(R.string.word_general)
            }
            "partnership"->{
                holder.binding.textInquireType.setText(R.string.word_partnership)
            }
            "error"->{
                holder.binding.textInquireType.setText(R.string.word_error)
            }
            "etc"->{
                holder.binding.textInquireType.setText(R.string.word_etc)
            }
        }

        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textInquireDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))

        holder.binding.textInquireTitle.text = item.title
        holder.binding.textInquireContents.text = item.contents

        if(item.imageList != null && item.imageList!!.isNotEmpty()){
            holder.binding.recyclerInquireImage.visibility = View.VISIBLE
            holder.binding.recyclerInquireImage.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = InquireImageAdapter()
            holder.binding.recyclerInquireImage.adapter = adapter
            adapter.setDataList(item.imageList as MutableList<InquireImage>)
        }else{
            holder.binding.recyclerInquireImage.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInquireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}