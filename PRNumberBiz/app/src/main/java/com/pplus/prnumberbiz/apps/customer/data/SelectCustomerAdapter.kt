package com.pplus.prnumberbiz.apps.customer.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerActivity
import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerConfigActivity
import com.pplus.prnumberbiz.core.network.model.dto.Customer
import kotlinx.android.synthetic.main.item_customer.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SelectCustomerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SelectCustomerAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Customer>? = null
    var listener: OnItemClickListener? = null
    var mSelectList: ArrayList<Customer>? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        this.mSelectList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Customer {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Customer>? {

        return mDataList
    }

    fun add(data: Customer) {

        if (mDataList == null) {
            mDataList = ArrayList<Customer>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Customer>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Customer>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Customer) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Customer>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Customer>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    fun getSelectList(): ArrayList<Customer> {

        return mSelectList!!
    }

    fun setSelectList(mSelectList: ArrayList<Customer>) {

        this.mSelectList = mSelectList
        notifyDataSetChanged()
    }

    fun noneSelect() {
        mSelectList = ArrayList()
        notifyDataSetChanged()
    }

    fun allSelect() {
        mSelectList = ArrayList()
        mSelectList!!.addAll(mDataList!!)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_customer_profile
        val text_name = itemView.text_customer_name
        val text_number = itemView.text_customer_number
        val image_more = itemView.image_customer_more
        val image_check_box = itemView.image_customer_checkbox

        init {
            image_more.visibility = View.GONE
            image_check_box.visibility = View.VISIBLE
//            itemView.setPadding(itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0, itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Customer = mDataList!!.get(position);

        holder.text_name.text = item.name

        if (item.target != null && item.target.profileImage != null) {
            Glide.with(mContext!!).load(item.target.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_gift_profile_default)
        }

        holder.text_number.text = FormatUtil.getPhoneNumber(item.mobile)


        holder.itemView.setOnClickListener {
            if (mSelectList!!.contains(item)) {
                mSelectList!!.remove(item)
            } else {
                mSelectList!!.add(item)
            }

            if (mContext is SelectCustomerConfigActivity) {
                (mContext as SelectCustomerConfigActivity).isTotal()
            } else if (mContext is SelectCustomerActivity) {
                (mContext as SelectCustomerActivity).isTotal()
            }
            notifyDataSetChanged()
        }

        holder.image_check_box.isSelected = mSelectList!!.contains(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(v)
    }
}