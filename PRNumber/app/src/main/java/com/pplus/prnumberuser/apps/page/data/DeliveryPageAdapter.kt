package com.pplus.prnumberuser.apps.page.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.databinding.ItemDeliveryPageBinding
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class DeliveryPageAdapter() : RecyclerView.Adapter<DeliveryPageAdapter.ViewHolder>() {

    var mDataList: MutableList<Page2>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Page2 {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Page2>? {

        return mDataList
    }

    fun add(data: Page2) {

        if (mDataList == null) {
            mDataList = ArrayList<Page2>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Page2>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Page2>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Page2) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Page2>()
        notifyDataSetChanged()
    }

    fun setBusPlus(data: BusProviderData) {
        if (mDataList != null) {
            val page = data.subData
            if (page is Page2) {
                for (i in 0 until mDataList!!.size - 1) {
                    if (mDataList!![i] == page) {
                        mDataList!![i] = page
                        break
                    }
                }
            }
            notifyDataSetChanged()
        }

    }

    fun setDataList(dataList: MutableList<Page2>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemDeliveryPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imagePage
        val text_name = binding.textPageName
        val text_distance = binding.textPageDistance
        val text_delivery = binding.textPageDelivery
        val text_package = binding.textPagePackage
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.name!!

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_square_profile_default)
        }


        val distance = item.distance
        if (distance != null) {
            holder.text_distance.visibility = View.VISIBLE
            var strDistance: String? = null
            if (distance > 1) {
                strDistance = String.format("%.2f", distance) + "km"
            } else {
                strDistance = (distance * 1000).toInt().toString() + "m"
            }
            holder.text_distance.text = strDistance
        } else {
            holder.text_distance.visibility = View.GONE
        }

        if(item.orderType!!.contains("3")){
            holder.text_delivery.visibility = View.VISIBLE
        }else{
            holder.text_delivery.visibility = View.GONE
        }

        if(item.orderType!!.contains("2")){
            holder.text_package.visibility = View.VISIBLE
        }else{
            holder.text_package.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position, it)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeliveryPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}