package com.pplus.luckybol.apps.buff.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.databinding.ItemBuffMemberSelectBinding
import com.pplus.utils.part.format.FormatUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffMemberSelectAdapter() : RecyclerView.Adapter<BuffMemberSelectAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffMember>? = null
    var listener: OnItemClickListener? = null
    lateinit var mCheckList: ArrayList<BuffMember>

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = arrayListOf()
        mCheckList = arrayListOf()
    }

    fun getItem(position: Int): BuffMember {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffMember>? {

        return mDataList
    }

    fun add(data: BuffMember) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffMember>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffMember) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuffMember>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffMember>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBuffMemberSelectBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBuffMemberSelectProfile
        val text_name = binding.textBuffMemberSelectName
        val text_cash = binding.textBuffMemberSelectCash
        val image_check = binding.imageBuffMemberSelectCheck
        val text_friend = binding.textBuffMemberSelectFriend

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if (item.member != null) {
            holder.text_name.text = item.member!!.nickname

            if (item.member!!.profileAttachment != null) {
                Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.ic_contact_profile_default)
            }

            holder.text_cash.text = holder.itemView.context.getString(R.string.format_divided_cash, FormatUtil.getMoneyTypeFloat(item.dividedPoint.toString()))
        }

        if (item.isFriend != null && item.isFriend!!) {
            holder.text_friend.visibility = View.VISIBLE
        } else {
            holder.text_friend.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {

            if(item.member!!.seqNo == LoginInfoManager.getInstance().user.no){
                (holder.itemView.context as BaseActivity).showAlert(R.string.msg_can_not_select_own)
                return@setOnClickListener
            }

            if (mCheckList.contains(item)) {
                mCheckList.remove(item)
            } else {
                mCheckList.add(item)
            }

//            holder.image_check.isSelected = !holder.image_check.isSelected

            notifyItemChanged(position)
            listener?.onItemClick(position)
        }

        holder.image_check.isSelected = mCheckList.contains(item)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuffMemberSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}