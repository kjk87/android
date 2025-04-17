package com.lejel.wowbox.apps.luckybox.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.LuckyBoxReply
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyBoxReplyBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyBoxReplyAdapter() : RecyclerView.Adapter<LuckyBoxReplyAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyBoxReply>? = null
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

    fun getItem(position: Int): LuckyBoxReply {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyBoxReply>? {

        return mDataList
    }

    fun add(data: LuckyBoxReply) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyBoxReply>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyBoxReply) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<LuckyBoxReply>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<LuckyBoxReply>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLuckyBoxReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageEventReplyProfile
        val text_name = binding.textEventReplyName
        val text_reply = binding.textEventReply
        val text_reg_date = binding.textEventReplyRegDate
        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().fitCenter().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position)).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop()).into(holder.image_profile)
        if(item.memberTotal != null){
            holder.text_name.text = item.memberTotal!!.nickname
        }

        holder.text_reply.text = item.reply
        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.text_reg_date.text = format.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime))

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyBoxReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}