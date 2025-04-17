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
import com.pplus.luckybol.core.network.model.dto.BuffPostLike
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ItemBuffPostLikeBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffPostLikeAdapter() : RecyclerView.Adapter<BuffPostLikeAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffPostLike>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = arrayListOf()
    }

    fun getItem(position: Int): BuffPostLike {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffPostLike>? {

        return mDataList
    }

    fun add(data: BuffPostLike) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffPostLike>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffPostLike) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuffPostLike>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffPostLike>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBuffPostLikeBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageBuffPostLikeProfile
        val text_name = binding.textBuffPostLikeName
        val layout_friend = binding.layoutBuffPostLikeFriend

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        if (item.member!!.profileAttachment != null) {
            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image_profile)
        } else {
            holder.image_profile.setImageResource(R.drawable.ic_contact_profile_default)
        }

        holder.text_name.text = item.member!!.nickname

        if(item.isFriend != null && item.isFriend!!){
            holder.layout_friend.visibility = View.VISIBLE
            holder.layout_friend.setOnClickListener {
                val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.member!!.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
                var memberName:String
                if (contacts != null && contacts.size > 0) {
                    memberName = contacts[0].memberName
                } else {
                    memberName = item.member!!.nickname!!
                }
                ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.format_friend_toast, memberName))
            }
        }else{
            holder.layout_friend.visibility = View.GONE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuffPostLikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}