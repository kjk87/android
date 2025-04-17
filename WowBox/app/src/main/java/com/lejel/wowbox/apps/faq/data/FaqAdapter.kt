package com.lejel.wowbox.apps.faq.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.Faq
import com.lejel.wowbox.databinding.ItemFaqBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class FaqAdapter() : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    var mDataList: MutableList<Faq>? = null
    var listener: OnItemClickListener? = null
    var mOpenFaq:Faq? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Faq {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Faq>? {

        return mDataList
    }

    fun add(data: Faq) {

        if (mDataList == null) {
            mDataList = ArrayList<Faq>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Faq>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Faq>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Faq) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Faq>()
    }

    fun setDataList(dataList: MutableList<Faq>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textFaqTitle.text = item.title
        holder.binding.textFaqContents.text = item.contents

        if(mOpenFaq != null && mOpenFaq!!.seqNo == item.seqNo){
            holder.binding.textFaqContents.visibility = View.VISIBLE
            holder.binding.imageFaqArrow.setImageResource(R.drawable.ic_faq_arrow_up)
        }else{
            holder.binding.textFaqContents.visibility = View.GONE
            holder.binding.imageFaqArrow.setImageResource(R.drawable.ic_faq_arrow_down)
        }

        holder.binding.layoutFaqTitle.setOnClickListener {
            if(mOpenFaq != null && mOpenFaq!!.seqNo == item.seqNo){
                mOpenFaq = null
            }else{
                mOpenFaq = item
            }

            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}