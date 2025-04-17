package com.pplus.prnumberuser.apps.event.data

import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemTuneEventBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class TuneEventAdapter : RecyclerView.Adapter<TuneEventAdapter.ViewHolder> {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Event {

        return mDataList!![position]
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
            notifyItemChanged(position)
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

    class ViewHolder(binding:ItemTuneEventBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_image = binding.layoutTuneEventImage
        val image = binding.imageTuneEvent
        val text_time = binding.textTuneEventTime
        val layout_close = binding.layoutTuneEventClose
        val text_win_time = binding.textTuneEventWinTime
        val text_proceeding = binding.textTuneEventProceeding
        val image_win_result = binding.imageTuneEventWinResult
        val text_title = binding.textTuneEventTitle
        val text_duration_date = binding.textTuneEventDurationDate
        val layout_limit = binding.layoutTuneEventTypeLimit
        val layout_rate_total = binding.layoutTuneEventJoinRateTotal
        val layout_rate = binding.layoutTuneEventJoinRate
        val text_join_count = binding.textTuneEventJoinCount
        val text_join_count2 = binding.textTuneEventJoinCount2
        val view_graph = binding.viewTuneEventGraph
        val image_graph = binding.imageTuneEventGraph
        val text_amount = binding.textTuneEventJoinAmount
        val layout_graph = binding.layoutTuneEventGraph
        var countTimer: CountDownTimer? = null

        init {
            text_amount.visibility = View.GONE
//            text_title.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if(item.groupSeqNo == 2L){
            holder.layout_image.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_720)
        }else{
            holder.layout_image.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_600)
        }

        val currentMillis = System.currentTimeMillis()
        val endMillis = PPLUS_DATE_FORMAT.parse(item.duration!!.end).time

        var isWinAnnounce = false

        var isClose = false

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

        if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
            holder.image_win_result.visibility = View.GONE
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
            if (currentMillis > winAnnounceDate) {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            } else {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
            }
        } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
            holder.image_win_result.visibility = View.GONE
            if (item.joinCount!! < item.maxJoinCount!!) {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
            } else {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            }
        } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
            if (item.winnerCount!! < item.totalGiftCount!!) {
                if (currentMillis > endMillis && item.winnerCount!! > 0) {
                    holder.image_win_result.visibility = View.GONE
                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                    holder.layout_close.visibility = View.GONE
                    isClose = false
                    isWinAnnounce = true
                }else{
                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                }

            } else {
                holder.image_win_result.visibility = View.GONE
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            }
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
                            notifyItemChanged(holder.adapterPosition)
                        } catch (e: Exception) {

                        }


                    }
                }
                holder.countTimer!!.start()
            } else {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                holder.layout_close.visibility = View.GONE
                isClose = false
                isWinAnnounce = true
            }
        } else {
            if (item.displayTimeList!!.isEmpty() || isClose || isWinAnnounce) {
                holder.text_time.visibility = View.GONE
                holder.text_proceeding.visibility = View.GONE
            } else {
                holder.text_time.visibility = View.VISIBLE
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
                                    holder.text_time.text = "$strH:$strM:$strS"
                                } else {
                                    if (minutes > 0) {
                                        holder.text_time.text = "$strM:$strS"
                                    } else {
                                        holder.text_time.text = strS
                                    }
                                }
                            }

                            override fun onFinish() {


                                try {
                                    notifyItemChanged(holder.adapterPosition)

                                } catch (e: Exception) {

                                }
                            }
                        }
                        holder.countTimer!!.start()
                    } else {
                        holder.text_time.visibility = View.GONE
                        holder.text_proceeding.visibility = View.GONE
                    }
                } else {
                    holder.text_time.visibility = View.GONE
                    holder.text_proceeding.visibility = View.GONE
                }

            }
        }

        holder.text_title.text = item.title

//        holder.text_amount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//        if (item.reward!! < 0) {
//            holder.text_amount.text = holder.itemView.context.getString(R.string.format_bol, FormatUtil.getMoneyType(Math.abs(item.reward!!).toString()))
//        } else {
//            holder.text_amount.text = holder.itemView.context.getString(R.string.word_free)
//        }

        when (item.winAnnounceType) {
            EventType.WinAnnounceType.immediately.name -> {

                holder.layout_limit.visibility = View.GONE
                holder.text_duration_date.visibility = View.GONE
                holder.text_join_count2.visibility = View.GONE
            }
            EventType.WinAnnounceType.special.name -> {
                holder.layout_limit.visibility = View.GONE
                holder.text_duration_date.visibility = View.VISIBLE
                val startDate = PPLUS_DATE_FORMAT.parse(item.duration!!.start).time
                val endDate = PPLUS_DATE_FORMAT.parse(item.duration!!.end).time
                val output = SimpleDateFormat("yyyy.MM.dd")
                holder.text_duration_date.text = "${output.format(startDate)} ~ ${output.format(endDate)}"
                if(item.groupSeqNo == 1L){
                    holder.text_join_count2.visibility = View.GONE
                    holder.text_join_count2.text = holder.itemView.context.getString(R.string.format_join_count, FormatUtil.getMoneyType(item.joinCount.toString()))
                }else{
                    holder.text_join_count2.visibility = View.GONE
                }
            }
            EventType.WinAnnounceType.limit.name -> {
                holder.layout_limit.visibility = View.VISIBLE
                holder.text_duration_date.visibility = View.GONE
                holder.text_join_count2.visibility = View.GONE
                holder.layout_rate_total.weightSum = item.maxJoinCount!!.toFloat()

                if (item.joinCount!! < item.maxJoinCount!!) {
                    holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full)
                    holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                    holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                } else {
                    var joinCount = item.joinCount!!
                    if(joinCount >= item.maxJoinCount!!){
                        joinCount = item.maxJoinCount!!
                    }
                    holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full_red)
                    holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon_red)
                    holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                }

                val layoutParams = holder.layout_rate.layoutParams;

                if (layoutParams is LinearLayout.LayoutParams) {
                    when (item.winAnnounceType) {
                        EventType.WinAnnounceType.special.name -> {
                            if (item.minJoinCount != null && item.minJoinCount!! > 0) {
                                if (item.joinCount!! > item.minJoinCount!!) {
                                    layoutParams.weight = item.minJoinCount!!.toFloat()
                                } else {
                                    layoutParams.weight = item.joinCount!!.toFloat()
                                }
                            } else {
                                layoutParams.weight = 1f
                            }

                        }
                        EventType.WinAnnounceType.limit.name -> {
                            layoutParams.weight = item.joinCount!!.toFloat()
                        }
                    }

                }
                holder.layout_rate.requestLayout()
            }
        }

        holder.itemView.setOnClickListener {
            if (isClose) {
                PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item)
            } else {
                listener?.onItemClick(holder.adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTuneEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}