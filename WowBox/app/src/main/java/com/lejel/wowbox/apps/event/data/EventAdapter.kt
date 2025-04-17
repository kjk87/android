package com.lejel.wowbox.apps.event.data

import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.event.ui.EventImpressionActivity
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemEventBinding
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Created by imac on 2018. 1. 8..
 */
class EventAdapter() : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int, isOpen: Boolean)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Event {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Event>? {

        return mDataList
    }

    fun add(data: Event) {

        if (mDataList == null) {
            mDataList = ArrayList<Event>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Event>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Event>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Event) {
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position+1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Event>()
    }

    fun setDataList(dataList: MutableList<Event>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        var countTimer: CountDownTimer? = null

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        val currentMillis = System.currentTimeMillis()
        val endMillis = PPLUS_DATE_FORMAT.parse(item.endDatetime).time

        var isWinAnnounce = false

        var isClose = false
        var isOpen = false

        if (currentMillis > endMillis) {
            if (item.winAnnounceType.equals("immediately")) {
                holder.binding.layoutEventClose.visibility = View.GONE
            } else {
                holder.binding.layoutEventClose.visibility = View.VISIBLE
            }

            isClose = true
        } else {
            holder.binding.layoutEventClose.visibility = View.GONE
            isClose = false
        }

        holder.binding.imageEventWinResult.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventImpressionActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }

        if (item.winnerCount!! > 0) {
            holder.binding.imageEventWinResult.visibility = View.GONE
        } else {
            holder.binding.imageEventWinResult.visibility = View.GONE
        }

        if (!item.winAnnounceType.equals("immediately") && StringUtils.isNotEmpty(item.winAnnounceDatetime)) {
            holder.binding.imageEventWinResult.visibility = View.GONE
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
            if (currentMillis > winAnnounceDate) {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imageEvent)
                holder.binding.layoutEventClose.visibility = View.GONE
//                holder.image_win_result.visibility = View.VISIBLE
                isClose = false
                isWinAnnounce = true
            } else {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imageEvent)
            }
        } else {
            Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imageEvent)
        }

        holder.countTimer?.cancel()

        if (isClose && !isWinAnnounce && item.winAnnounceType.equals("special")) {
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time

            val remainWinMillis = winAnnounceDate - currentMillis
            if (remainWinMillis > 0) {
                holder.binding.layoutEventClose.visibility = View.VISIBLE
                holder.countTimer = object : CountDownTimer(remainWinMillis, 1000) {

                    override fun onTick(millisUntilFinished: Long) {

                        val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                        val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                        val seconds = (millisUntilFinished / 1000).toInt() % 60

                        val strH = DateFormatUtils.formatTime(hours)
                        val strM = DateFormatUtils.formatTime(minutes)
                        val strS = DateFormatUtils.formatTime(seconds)

                        if (hours > 0) {
                            holder.binding.textEventWinTime.text = "$strH:$strM:$strS"
                        } else {
                            if (minutes > 0) {
                                holder.binding.textEventWinTime.text = "$strM:$strS"
                            } else {
                                holder.binding.textEventWinTime.text = strS
                            }
                        }
                    }

                    override fun onFinish() {
                        try {
                            notifyItemChanged(holder.absoluteAdapterPosition)

                        } catch (e: Exception) {

                        }
                    }
                }
                holder.countTimer?.start()
            } else {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imageEvent)
                holder.binding.layoutEventClose.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            }
        } else {
            if (item.eventTime!!.isEmpty() || isClose || isWinAnnounce) {
                holder.binding.textEventTime.visibility = View.GONE
                holder.binding.textEventProceeding.visibility = View.GONE
                isOpen = item.eventTime!!.isEmpty()
            } else {
                holder.binding.textEventTime.visibility = View.VISIBLE
                isOpen = true
//                holder.text_time.visibility = View.GONE
//                holder.text_proceeding.visibility = View.VISIBLE
                holder.binding.textEventProceeding.visibility = View.GONE

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date(currentMillis)).split(":")
                val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
                var endDisplayMillis = 0L
                for (time in item.eventTime!!) {
                    val startSecond = (time.startTime!!.substring(0, 2).toInt() * 60 * 60) + (time.startTime!!.substring(2).toInt() * 60)
                    val endSecond = (time.endTime!!.substring(0, 2).toInt() * 60 * 60) + (time.endTime!!.substring(2).toInt() * 60)

                    if (currentSecond in startSecond..endSecond) {
                        endDisplayMillis = endSecond * 1000L
                        break
                    }
                }

                if (endDisplayMillis > 0) {
                    val remainMillis = endDisplayMillis - (currentSecond * 1000)

                    if (remainMillis > 0) {
                        holder.countTimer = object : CountDownTimer(remainMillis, 1000) {

                            override fun onTick(millisUntilFinished: Long) {

                                val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                                val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                                val seconds = (millisUntilFinished / 1000).toInt() % 60

                                val strH = DateFormatUtils.formatTime(hours)
                                val strM = DateFormatUtils.formatTime(minutes)
                                val strS = DateFormatUtils.formatTime(seconds)

                                if (hours > 0) {
                                    holder.binding.textEventTime.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, "$strH:$strM:$strS"))
                                    holder.binding.textEventTime.text = "$strH:$strM:$strS"
                                } else {
                                    if (minutes > 0) {
                                        holder.binding.textEventTime.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, "$strM:$strS"))
                                        holder.binding.textEventTime.text = "$strM:$strS"
                                    } else {
                                        holder.binding.textEventTime.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, strS))
                                        holder.binding.textEventTime.text = strS
                                    }
                                }
                            }

                            override fun onFinish() {

                                try {
                                    notifyItemChanged(holder.absoluteAdapterPosition)
                                } catch (e: Exception) {

                                }
                            }
                        }
                        holder.countTimer?.start()
                    } else {
                        holder.binding.textEventTime.visibility = View.GONE
                        isOpen = false
                        holder.binding.textEventProceeding.visibility = View.GONE
                    }
                } else {
                    holder.binding.textEventTime.visibility = View.GONE
                    isOpen = false
                    holder.binding.textEventProceeding.visibility = View.GONE
                }

            }
        }

        holder.itemView.setOnClickListener {
            if (isClose) {
                PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item, launcher)

            } else {
                if (listener != null) {
                    listener!!.onItemClick(holder.absoluteAdapterPosition, isOpen)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}