package com.root37.buflexz.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.MemberProfileImage
import com.root37.buflexz.databinding.ItemMainLotteryProfileImageBinding
import com.root37.buflexz.databinding.ItemMemberProfileImageBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class MainLotteryProfileImageAdapter() : RecyclerView.Adapter<MainLotteryProfileImageAdapter.ViewHolder>() {

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

    class ViewHolder(val binding: ItemMainLotteryProfileImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.image).apply(RequestOptions().fitCenter()).into(holder.binding.imageMainLotteryProfileImage)

        if(position < itemCount -1){
            (holder.binding.layoutMainLotteryProfileImage.layoutParams as RecyclerView.LayoutParams).marginEnd = (-1*holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_44))
        }else{
            (holder.binding.layoutMainLotteryProfileImage.layoutParams as RecyclerView.LayoutParams).marginEnd = 0
        }



        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainLotteryProfileImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}