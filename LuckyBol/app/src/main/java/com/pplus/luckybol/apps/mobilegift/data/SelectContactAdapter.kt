package com.pplus.luckybol.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.core.database.entity.Contact
import com.pplus.luckybol.databinding.ItemContactBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class SelectContactAdapter : RecyclerView.Adapter<SelectContactAdapter.ViewHolder>() {
    private var mDataList: MutableList<Contact>?
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun getItem(position: Int): Contact {
        return mDataList!![position]
    }


    fun add(data: Contact) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Contact>?) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Contact) {
        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Contact>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_contact_name = binding.textContactName
        val text_contact_buff = binding.textContactBuff

        init {
            text_contact_buff.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        holder.text_contact_name.text = item.memberName
        holder.text_contact_buff.text = FormatUtil.getPhoneNumber(item.mobileNumber)

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.absoluteAdapterPosition)
            }
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