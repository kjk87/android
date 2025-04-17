package com.pplus.luckybol.apps.main.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.databinding.ItemBuffMemberBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffMemberThumbAdapter() : RecyclerView.Adapter<BuffMemberThumbAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffMember>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
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

    class ViewHolder(binding: ItemBuffMemberBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageBuffMember

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if(item.member!!.profileAttachment != null){
            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image)
        }else{
            holder.image.setImageResource(R.drawable.ic_contact_profile_default)
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition, it)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuffMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}