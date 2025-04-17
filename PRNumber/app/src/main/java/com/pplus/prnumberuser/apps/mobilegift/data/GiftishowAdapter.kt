package com.pplus.prnumberuser.apps.mobilegift.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Giftishow
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMobileGiftBinding
import com.pplus.utils.part.format.FormatUtil
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class GiftishowAdapter() : RecyclerView.Adapter<GiftishowAdapter.ViewHolder>() {
    private var mDataList: MutableList<Giftishow>?
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemId(position: Int): Long {
        return mDataList!![position].seqNo!!
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun getItem(position: Int): Giftishow {
        return mDataList!![position]
    }

    fun add(data: Giftishow) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Giftishow>?) {
        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Giftishow) {
        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Giftishow>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMobileGiftBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageMobileGift
        val text_company_name = binding.textMobileGiftCompanyName
        val text_name = binding.textMobileGiftName
        val text_price = binding.textMobileGiftPrice

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMobileGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]
        holder.text_company_name.text = item.brandName
        holder.text_name.text = item.goodsName
        holder.text_name.setSingleLine()
        Glide.with(holder.itemView.context).load(item.goodsImgS).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(item.realPrice)))
        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    init {
        mDataList = ArrayList()
    }
}