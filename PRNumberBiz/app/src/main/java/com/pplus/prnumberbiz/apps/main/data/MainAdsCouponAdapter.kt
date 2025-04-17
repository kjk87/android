package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Advertise
import kotlinx.android.synthetic.main.item_main_ads_coupon.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList

/**
 * Created by imac on 2018. 1. 8..
 */
class MainAdsCouponAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<MainAdsCouponAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Advertise>? = null
    var listener: OnItemClickListener? = null
    internal var mEvents: Array<String>

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context:Context) : super(){
        this.mContext = context
        this.mDataList = ArrayList()
        mEvents = context.resources.getStringArray(R.array.coupon_event_type)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Advertise {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Advertise>? {

        return mDataList
    }

    fun add(data: Advertise) {

        if (mDataList == null) {
            mDataList = ArrayList<Advertise>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Advertise>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Advertise>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Advertise) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {
        val size = this.mDataList?.size
        this.mDataList?.clear()
        notifyItemRangeRemoved(0, size!!)
//        mDataList = ArrayList<Advertise>()
//        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Advertise>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_coupon_type = itemView.text_main_ads_coupon_type
        val text_title = itemView.text_main_ads_coupon_title
        val text_duration = itemView.text_main_ads_coupon_duration
        val text_count = itemView.text_main_ads_coupon_visit_count
        val view_rate = itemView.view_main_ads_coupon_visit_rate
        val layout_rate = itemView.layout_main_ads_coupon_visit_rate

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item:Advertise = mDataList!!.get(position);

        val eventType: String

        if (StringUtils.isEmpty(item.template?.type.orEmpty())) {
            eventType = mEvents[0]
        } else {
            eventType = mEvents[EnumData.CouponType.valueOf(item.template?.type.orEmpty()).ordinal]
        }
        holder.text_coupon_type.setText(eventType)

        when (EnumData.CouponType.valueOf(item.template?.type.orEmpty())) {

            EnumData.CouponType.discount -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff4141))
            }
            EnumData.CouponType.dayOfWeek -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_fbc000))
            }
            EnumData.CouponType.visit -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_fbc000))
            }
            EnumData.CouponType.consult -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_1cbeb1))
            }
            EnumData.CouponType.expert -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_af3dff))
            }
            EnumData.CouponType.time -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff4141))
            }
            EnumData.CouponType.etc -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_6d6d6d))
            }
            else -> {
                holder.text_coupon_type.setTextColor(ResourceUtil.getColor(mContext, R.color.color_ff4141))
            }
        }

        holder.text_title.text = item.template?.name

        try {
            val d1 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.template?.duration!!.start)
            val d2 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.template?.duration!!.end)

            val output = SimpleDateFormat("yyyy.MM.dd")

            holder.text_duration.text = output.format(d1) + "~" + output.format(d2)

        } catch (e: Exception) {

        }

        holder.text_count.text = mContext?.getString(R.string.format_visit, item.currentCount)

        var layoutParams:ViewGroup.LayoutParams = holder.view_rate.layoutParams;

        holder.layout_rate.weightSum = item.totalCount!!.toFloat()
        if(layoutParams is LinearLayout.LayoutParams){
            layoutParams.weight = item.currentCount!!.toFloat()
        }
        holder.view_rate.requestLayout()
        holder.itemView.setOnClickListener{
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_ads_coupon, parent, false)
        return ViewHolder(v)
    }
}