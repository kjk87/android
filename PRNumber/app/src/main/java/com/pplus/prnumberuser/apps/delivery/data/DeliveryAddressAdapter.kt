package com.pplus.prnumberuser.apps.delivery.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.core.network.model.dto.DeliveryAddress
import com.pplus.prnumberuser.databinding.ItemDeliveryAddressBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class DeliveryAddressAdapter() : RecyclerView.Adapter<DeliveryAddressAdapter.ViewHolder>() {

    var mDataList: MutableList<DeliveryAddress>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)

        fun onItemDelete(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): DeliveryAddress {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<DeliveryAddress>? {

        return mDataList
    }

    fun add(data: DeliveryAddress) {

        if (mDataList == null) {
            mDataList = ArrayList<DeliveryAddress>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<DeliveryAddress>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<DeliveryAddress>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: DeliveryAddress) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<DeliveryAddress>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<DeliveryAddress>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemDeliveryAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_address = binding.textDeliveryAddress
        val image_close = binding.imageDeliveryAddressClose
        val text_old_adderss = binding.textDeliveryOldAddress
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        val pre = item.address!!.split(" ")[0]
        val address = item.address!!.replace("$pre ", "")
        holder.text_address.text = address+" "+item.addressDetail

        val jibunAddress = item.jibunAddress!!.replace("$pre ", "")
        holder.text_old_adderss.text = jibunAddress+" "+item.addressDetail

        holder.image_close.setOnClickListener {
            listener?.onItemDelete(position)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeliveryAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}