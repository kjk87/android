package com.pplus.luckybol.apps.buff.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.BuffRequest
import com.pplus.luckybol.databinding.ItemReceiveBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class ReceiveAdapter() : RecyclerView.Adapter<ReceiveAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffRequest>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onConsent(position: Int)

        fun onReject(position: Int)
    }

    init {
        this.mDataList = arrayListOf()
    }

    fun getItem(position: Int): BuffRequest {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffRequest>? {

        return mDataList
    }

    fun add(data: BuffRequest) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffRequest>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffRequest) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuffRequest>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffRequest>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemReceiveBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageReceiveProfile
        val text_name = binding.textReceiveName
        val text_buff = binding.textReceiveBuff
        val text_consent = binding.textReceiveConsent
        val text_reject = binding.textReceiveReject

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.member!!.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
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
            holder.text_buff.text = "#${item.buff!!.title}"
        }

        holder.text_consent.setOnClickListener {
            listener?.onConsent(holder.absoluteAdapterPosition)
        }

        holder.text_reject.setOnClickListener {
            listener?.onReject(holder.absoluteAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}