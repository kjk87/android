package com.pplus.prnumberuser.apps.subscription.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMySubscriptionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MySubscriptionAdapter() : RecyclerView.Adapter<MySubscriptionAdapter.ViewHolder>() {

    var mDataList: MutableList<SubscriptionDownload>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): SubscriptionDownload {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<SubscriptionDownload>? {

        return mDataList
    }

    fun add(data: SubscriptionDownload) {

        if (mDataList == null) {
            mDataList = ArrayList<SubscriptionDownload>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<SubscriptionDownload>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<SubscriptionDownload>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: SubscriptionDownload) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<SubscriptionDownload>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<SubscriptionDownload>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMySubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_top = binding.layoutMySubscriptionTop
        val image_page_image = binding.imageMySubscriptionPageImage
        val text_page_name = binding.textMySubscriptionPageName
        val text_product_name = binding.textMySubscriptionName
        val text_remain_count = binding.textMySubscriptionRemainCount
        val text_remain_days = binding.textMySubscriptionRemainDays
        val text_date = binding.textMySubscriptionDate
        val text_status = binding.textMySubscriptionStatus

        init {
            text_product_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        when(item.type){
            "prepayment"->{
                holder.layout_top.setBackgroundResource(R.drawable.bg_ffcf5c_radius_top_30)
                holder.text_page_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
                holder.text_product_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
                holder.text_date.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4a3606))
                holder.text_status.setBackgroundResource(R.drawable.bg_border_4a3606_radius_15)
                holder.text_remain_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_remain_price, FormatUtil.getMoneyType((item.havePrice!! - item.usePrice!!).toString())))
            }
            else->{
                holder.layout_top.setBackgroundResource(R.drawable.bg_579ffb_radius_top_30)
                holder.text_page_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.text_product_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.text_date.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                holder.text_status.setBackgroundResource(R.drawable.bg_border_579ffb_radius_15)
                holder.text_remain_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_remain_count, (item.haveCount!! - item.useCount!!).toString()))
            }
        }

        Glide.with(holder.itemView.context).load(item.productPrice!!.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_page_image)
        holder.text_product_name.text = item.name
        holder.text_page_name.text = item.productPrice!!.page!!.name

        val currentDate = LocalDate.now()
        val expireDate = LocalDate.parse(item.expireDate)
        val remainDay = ChronoUnit.DAYS.between(currentDate, expireDate)
        holder.text_remain_days.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_remain_day, remainDay.toString()))
        val output = SimpleDateFormat("yyyy.MM.dd")
        when(item.status){ // 1:사용중, 2:사용완료, 3:기간만료
            1->{
                holder.text_status.setText(R.string.word_use_ing)
                holder.text_remain_count.visibility = View.VISIBLE
                holder.text_remain_days.visibility = View.VISIBLE
                holder.text_date.text = holder.itemView.context.getString(R.string.word_publish_date) + " " +output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime))
            }
            2->{
                holder.text_status.setText(R.string.word_use_complete)
                holder.text_remain_count.visibility = View.GONE
                holder.text_remain_days.visibility = View.GONE
                holder.text_date.text = holder.itemView.context.getString(R.string.word_use_complete_date) + " " +output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.completeDatetime))
                holder.layout_top.setBackgroundResource(R.drawable.bg_e8e8e8_radius_top_30)
            }
            3->{
                holder.text_status.setText(R.string.word_expire)
                holder.text_remain_count.visibility = View.GONE
                holder.text_remain_days.visibility = View.GONE
                holder.text_date.text = holder.itemView.context.getString(R.string.word_expire_date) + " " +item.expireDate
                holder.layout_top.setBackgroundResource(R.drawable.bg_e8e8e8_radius_top_30)
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMySubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}