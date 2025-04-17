package com.lejel.wowbox.apps.my.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.MemberProfileImage
import com.lejel.wowbox.databinding.ItemMemberProfileImageBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MemberProfileImageAdapter() : RecyclerView.Adapter<MemberProfileImageAdapter.ViewHolder>() {

    var mDataList: MutableList<MemberProfileImage>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): MemberProfileImage {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<MemberProfileImage>? {

        return mDataList
    }

    fun add(data: MemberProfileImage) {

        if (mDataList == null) {
            mDataList = ArrayList<MemberProfileImage>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<MemberProfileImage>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<MemberProfileImage>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: MemberProfileImage) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<MemberProfileImage>()
    }

    fun setDataList(dataList: MutableList<MemberProfileImage>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemMemberProfileImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position == 0){
            holder.binding.imageMemberProfileImage.setImageResource(R.drawable.ic_profile_image_none)
        }else{
            val item = mDataList!![position]
            Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().fitCenter()).into(holder.binding.imageMemberProfileImage)
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemberProfileImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}