//package com.pplus.prnumberuser.apps.mobilegift.data
//
//import android.content.Context
//import android.text.Html
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.MobileGift
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.calculateBol
//import kotlinx.android.synthetic.main.item_mobile_gift.view.*
//import java.util.*
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class MobileGiftAdapter() : RecyclerView.Adapter<MobileGiftAdapter.ViewHolder>() {
//    private var mDataList: MutableList<MobileGift>?
//    private var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    override fun getItemId(position: Int): Long {
//        return mDataList!![position].no
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener?) {
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): MobileGift {
//        return mDataList!![position]
//    }
//
//    val dataList: List<MobileGift>?
//        get() = mDataList
//
//    fun add(data: MobileGift) {
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<MobileGift>?) {
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.addAll(dataList!!)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: MobileGift) {
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//        mDataList = ArrayList()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<MobileGift>?) {
//        mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image = itemView.image_mobile_gift
//        val text_company_name = itemView.text_mobile_gift_company_name
//        val text_name = itemView.text_mobile_gift_name
//        val text_price = itemView.text_mobile_gift_price
//
//        init {
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_mobile_gift, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//        holder.text_company_name.text = item.companyName
//        holder.text_name.text = item.name
//        holder.text_name.setSingleLine()
//        Glide.with(holder.itemView.context).load(item.baseImage).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(item.salesPrice.toString())))
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    init {
//        mDataList = ArrayList()
//    }
//}