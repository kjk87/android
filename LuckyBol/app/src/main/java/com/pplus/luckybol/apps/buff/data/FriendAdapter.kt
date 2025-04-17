package com.pplus.luckybol.apps.buff.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.Contact
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ItemContactBinding
import com.pplus.utils.part.apps.resource.ResourceUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class FriendAdapter() : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    var mDataList: MutableList<Contact>? = null
    var listener: OnItemClickListener? = null
    var mBuff:Buff? = null
    lateinit var mCheckList:MutableList<Long>

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = arrayListOf()
        mCheckList = arrayListOf()
    }

    fun getItem(position: Int): Contact {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Contact>? {

        return mDataList
    }

    fun add(data: Contact) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Contact>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Contact) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Contact>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Contact>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageContactProfile
        val text_name = binding.textContactName
        val text_buff = binding.textContactBuff
        val image_check = binding.imageContactCheck

        init {
            image_check.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
        if (item.member != null) {
            if (contacts != null && contacts.size > 0) {
                holder.text_name.text = contacts[0].memberName
            } else {
                holder.text_name.text = item.member!!.nickname
            }

            if (item.member!!.profileAttachment != null) {
                Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.ic_contact_profile_default)
            }
            holder.text_buff.visibility = View.VISIBLE
            if (item.member!!.buffMemberList != null && item.member!!.buffMemberList!!.isNotEmpty()) {
                val buffTitle = item.member!!.buffMemberList!![0].buff!!.title
                holder.text_buff.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_fc5c57))
                holder.text_buff.text = "#${buffTitle}"
            } else {
                holder.text_buff.text = "#${holder.itemView.context.getString(R.string.word_none_buff)}"
                holder.text_buff.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_6e7780))
            }
        }


        holder.itemView.setOnClickListener {
            if (item.member!!.buffMemberList != null && item.member!!.buffMemberList!!.isNotEmpty()) {
                if(mBuff!!.seqNo == item.member!!.buffMemberList!![0].buff!!.seqNo){
                    ToastUtil.show(holder.itemView.context, R.string.msg_same_buff_friend)
                    return@setOnClickListener
                }
            }

            holder.image_check.isSelected = !holder.image_check.isSelected
            if(mCheckList.contains(item.member!!.seqNo)){
                mCheckList.remove(item.member!!.seqNo)
            }else{
                mCheckList.add(item.member!!.seqNo!!)
            }
            notifyItemChanged(position)
            listener?.onItemClick(position)
        }

        holder.image_check.isSelected = mCheckList.contains(item.member!!.seqNo)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}