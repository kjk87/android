package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.event.ui.EventImpressionActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ItemEventBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class EventAdapter(isPrEvent: Boolean) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var mIsPrEvent = isPrEvent
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

    class ViewHolder(binding:ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout = binding.layoutEvent
        val image = binding.imageEvent
        val text_time = binding.textEventTime
        val layout_close = binding.layoutEventClose
        val text_win_time = binding.textEventWinTime
        val text_proceeding = binding.textEventProceeding
        val image_win_result = binding.imageEventWinResult
        var countTimer: CountDownTimer? = null

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(mIsPrEvent){
            holder.layout.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_750)
        }

        val item = mDataList!![position]

        val currentMillis = System.currentTimeMillis()
        val endMillis = PPLUS_DATE_FORMAT.parse(item.duration!!.end).time

        var isWinAnnounce = false

        var isClose = false
        var isOpen = false

        if (currentMillis > endMillis) {
            if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                holder.layout_close.visibility = View.GONE
            } else {
                holder.layout_close.visibility = View.VISIBLE
            }

            isClose = true
        } else {
            holder.layout_close.visibility = View.GONE
            isClose = false
        }

        holder.image_win_result.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventImpressionActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }

        if (item.winnerCount!! > 0) {
            holder.image_win_result.visibility = View.GONE
        } else {
            holder.image_win_result.visibility = View.GONE
        }

        if (!item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
            holder.image_win_result.visibility = View.GONE
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
            if (currentMillis > winAnnounceDate) {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
//                holder.image_win_result.visibility = View.VISIBLE
                isClose = false
                isWinAnnounce = true
            } else {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            }
        } else {
            Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        }

        if (holder.countTimer != null) {
            holder.countTimer!!.cancel()
        }

        if (isClose && !isWinAnnounce && item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time

            val remainWinMillis = winAnnounceDate - currentMillis
            if (remainWinMillis > 0) {
                holder.layout_close.visibility = View.VISIBLE
                holder.countTimer = object : CountDownTimer(remainWinMillis, 1000) {

                    override fun onTick(millisUntilFinished: Long) {

                        val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                        val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                        val seconds = (millisUntilFinished / 1000).toInt() % 60

                        val strH = DateFormatUtils.formatTime(hours)
                        val strM = DateFormatUtils.formatTime(minutes)
                        val strS = DateFormatUtils.formatTime(seconds)

                        if (hours > 0) {
                            holder.text_win_time.text = "$strH:$strM:$strS"
                        } else {
                            if (minutes > 0) {
                                holder.text_win_time.text = "$strM:$strS"
                            } else {
                                holder.text_win_time.text = strS
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
                holder.countTimer!!.start()
            } else {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            }
        } else {
            if (item.displayTimeList!!.isEmpty() || isClose || isWinAnnounce) {
                holder.text_time.visibility = View.GONE
                holder.text_proceeding.visibility = View.GONE
                isOpen = item.displayTimeList!!.isEmpty()
            } else {
                holder.text_time.visibility = View.VISIBLE
                isOpen = true
//                holder.text_time.visibility = View.GONE
//                holder.text_proceeding.visibility = View.VISIBLE
                holder.text_proceeding.visibility = View.GONE

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date(currentMillis)).split(":")
                val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
                var endDisplayMillis = 0L
                for (time in item.displayTimeList!!) {
                    val startSecond = (time.start.substring(0, 2).toInt() * 60 * 60) + (time.start.substring(2).toInt() * 60)
                    val endSecond = (time.end.substring(0, 2).toInt() * 60 * 60) + (time.end.substring(2).toInt() * 60)

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
                                    holder.text_time.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, "$strH:$strM:$strS"))
                                    holder.text_time.text = "$strH:$strM:$strS"
                                } else {
                                    if (minutes > 0) {
                                        holder.text_time.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, "$strM:$strS"))
                                        holder.text_time.text = "$strM:$strS"
                                    } else {
                                        holder.text_time.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_event_time, strS))
                                        holder.text_time.text = strS
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
                        holder.countTimer!!.start()
                    } else {
                        holder.text_time.visibility = View.GONE
                        isOpen = false
                        holder.text_proceeding.visibility = View.GONE
                    }
                } else {
                    holder.text_time.visibility = View.GONE
                    isOpen = false
                    holder.text_proceeding.visibility = View.GONE
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