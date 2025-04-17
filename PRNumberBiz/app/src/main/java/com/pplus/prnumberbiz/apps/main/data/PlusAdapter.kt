package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Fan
import kotlinx.android.synthetic.main.item_customer.view.*
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PlusAdapter : RecyclerView.Adapter<PlusAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Fan>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Fan {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Fan>? {

        return mDataList
    }

    fun add(data: Fan) {

        if (mDataList == null) {
            mDataList = ArrayList<Fan>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Fan>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Fan>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Fan) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Fan>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Fan>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_customer_profile
        val text_name = itemView.text_customer_name
        val text_number = itemView.text_customer_number
        val image_more = itemView.image_customer_more

        init {
            text_number.visibility = View.GONE
            image_more.visibility = View.GONE
//            itemView.setPadding(itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0, itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Fan = mDataList!![position]

        holder.text_name.text = item.nickname

        if (item.profileImage != null) {
            Glide.with(mContext!!).load(item.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_gift_profile_default)
        }

//        holder.text_number.text = FormatUtil.getPhoneNumber(item.mobile)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(v)
    }
}