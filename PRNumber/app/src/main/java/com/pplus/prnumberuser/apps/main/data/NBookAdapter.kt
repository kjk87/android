package com.pplus.prnumberuser.apps.main.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.databinding.ItemNBookBinding
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class NBookAdapter() : RecyclerView.Adapter<NBookAdapter.ViewHolder>() {

    var mDataList: List<VirtualNumberManage>? = null
    var listener: OnItemClickListener? = null

    init {
        this.mDataList = ArrayList()
    }

    interface OnItemClickListener {

        fun onItemClick(item:VirtualNumberManage)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }


    class ViewHolder(binding: ItemNBookBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageNBook

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}