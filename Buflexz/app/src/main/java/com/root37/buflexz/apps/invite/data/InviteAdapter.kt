package com.root37.buflexz.apps.invite.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemInviteBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class InviteAdapter() : RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    var mDataList: MutableList<Member>? = null
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

    fun getItem(position: Int): Member {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Member>? {

        return mDataList
    }

    fun add(data: Member) {

        if (mDataList == null) {
            mDataList = ArrayList<Member>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Member>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Member>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Member) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Member>()
    }

    fun setDataList(dataList: MutableList<Member>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemInviteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(Const.CDN_URL + "profile/${item.userKey}/index.html").apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(holder.binding.imageInviteProfile)
        holder.binding.textInviteNickname.text = item.nickname
        Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${item.nation!!.uppercase()}.png")).into(holder.binding.imageInviteFlag)


        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textInviteDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.joinDatetime)))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInviteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}