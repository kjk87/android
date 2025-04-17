package com.pplus.prnumberuser.apps.shippingsite.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.SearchAddressJuso
import com.pplus.prnumberuser.databinding.ItemSearchAddressBinding
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class SearchAddressAdapter() : RecyclerView.Adapter<SearchAddressAdapter.ViewHolder>() {
    private var mDataList: MutableList<SearchAddressJuso>?
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun getItem(position: Int): SearchAddressJuso {
        return mDataList!![position]
    }

    val dataList: List<SearchAddressJuso>?
        get() = mDataList

    fun add(data: SearchAddressJuso) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<SearchAddressJuso>?) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: SearchAddressJuso) {
        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<SearchAddressJuso>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemSearchAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_roadAddress = binding.textSearchRoadAddress
        val text_jibunAddress = binding.textSearchJibunAddress
        val text_zipcode = binding.textSearchZipcode

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        holder.text_zipcode.text = item.zipNo
        holder.text_roadAddress.text = item.roadAddrPart1
        holder.text_jibunAddress.text = item.jibunAddr
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    init {
        setHasStableIds(true)
        mDataList = ArrayList()
    }
}