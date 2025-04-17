package com.root37.buflexz.apps.my.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.R
import com.root37.buflexz.core.network.model.dto.NotificationBox
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemNotificationBoxBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class NotificationBoxAdapter() : RecyclerView.Adapter<NotificationBoxAdapter.ViewHolder>() {

    var mDataList: MutableList<NotificationBox>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


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

    fun getItem(position: Int): NotificationBox {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<NotificationBox>? {

        return mDataList
    }

    fun add(data: NotificationBox) {

        if (mDataList == null) {
            mDataList = ArrayList<NotificationBox>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<NotificationBox>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<NotificationBox>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: NotificationBox) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<NotificationBox>()
    }

    fun setDataList(dataList: MutableList<NotificationBox>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemNotificationBoxBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textNotificationBoxTitle.text = item.title
        holder.binding.textNotificationBoxContents.text = item.contents
        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textNotificationBoxDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)))

        if(item.isRead != null && item.isRead!!){
            holder.binding.textNotificationBoxTitle.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_878787))
            holder.binding.textNotificationBoxContents.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_878787))
            holder.binding.textNotificationBoxDate.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_878787))
            holder.binding.imageNotificationBox.setImageResource(R.drawable.ic_alarm_read)
        }else{
            holder.binding.textNotificationBoxTitle.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.binding.textNotificationBoxContents.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_dcdcdc))
            holder.binding.textNotificationBoxDate.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_dcdcdc))
            holder.binding.imageNotificationBox.setImageResource(R.drawable.ic_alarm_un_read)
        }


        holder.binding.textNotificationBoxDelete.setOnClickListener {
            listener?.onItemDelete(holder.absoluteAdapterPosition)
        }

        holder.binding.layoutNotificationBox.setOnClickListener {
            item.isRead = true
            notifyItemChanged(holder.absoluteAdapterPosition)
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}