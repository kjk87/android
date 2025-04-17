package com.pplus.prnumberuser.apps.setting.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData.MessageData
import com.pplus.prnumberuser.apps.common.ui.custom.swipeLayout.ViewBinderHelper
import com.pplus.prnumberuser.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberuser.core.network.model.dto.NotificationBox
import com.pplus.prnumberuser.databinding.ItemAlarmBinding
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.util.*

/**
 * Created by ksh on 2016-09-28.
 */
class NotificationBoxAdapter() : RecyclerView.Adapter<NotificationBoxAdapter.ViewHolder>() {
    private var mDataList: ArrayList<NotificationBox>
    var listener: OnItemClickListener? = null
    private var viewBinderHelper: ViewBinderHelper

    init {
        mDataList = ArrayList()
        viewBinderHelper = ViewBinderHelper()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun getItem(postion: Int): NotificationBox {
        return mDataList[postion]
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun addAll(channels: List<NotificationBox>?) {
        mDataList.addAll(channels!!)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList[position]

        viewBinderHelper.bind(holder.swipeLayout, position.toString())

        if (StringUtils.isNotEmpty(item.subject)) {
            holder.text_title.visibility = View.VISIBLE
            holder.text_title.text = item.subject
        } else {
            holder.text_title.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(item.contents)) {
            holder.text_contents.visibility = View.VISIBLE
            holder.text_contents.text = item.contents
        } else {
            holder.text_contents.visibility = View.GONE
        }

        holder.text_date.visibility = View.GONE
        val date = DateFormatUtils.convertDateFormat(item.regDatetime, "yyyy.MM.dd")
        holder.text_date.text = date

        holder.image_leave.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
            builder.addContents(MessageData(holder.itemView.context.getString(R.string.msg_question_delete_alarm), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {}
                override fun onResult(event_alert: EVENT_ALERT) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> if (holder.itemView.context is AlarmContainerActivity) {
                            holder.swipeLayout.close(true)
                            (holder.itemView.context as AlarmContainerActivity).delete(item.seqNo!!)
                        }
                    }
                }
            }).builder().show(holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class ViewHolder(binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        var swipeLayout = binding.swipeLayout
        var layout_alarm = binding.layoutAlarm
        var text_title = binding.textAlarmTitle
        var text_contents = binding.textAlarmContents
        var text_date = binding.textAlarmDate
        var image_leave = binding.imageAlarmLeave

        init {
            text_title.setSingleLine()
            text_contents.setSingleLine()
        }
    }
}