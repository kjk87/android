package com.lejel.wowbox.apps.event.data

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.ui.PlayPercentActivity
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemPlayBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Created by imac on 2018. 1. 8..
 */
class PlayAdapter() : RecyclerView.Adapter<PlayAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)

        fun onPercentClick(position: Int)
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
        if(position != -1 && mDataList!!.size > position){
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

    class ViewHolder(val binding: ItemPlayBinding) : RecyclerView.ViewHolder(binding.root) {

        var countTimer: CountDownTimer? = null

        init {
            binding.textPlayTitle.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textPlayTitle.text = item.title

        holder.binding.textPlayJoinAmount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
        if (item.reward!! < 0) {
            holder.binding.textPlayJoinAmount.text = holder.itemView.context.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(Math.abs(item.reward!!).toString()))
        } else {
            holder.binding.textPlayJoinAmount.text = holder.itemView.context.getString(R.string.word_gratis)
        }

        holder.binding.textPlayWinRate.visibility = View.VISIBLE
        holder.binding.textPlayJoin.setText(R.string.word_play_join)
        holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_right_radius_11)

        var isWinAnnounce = false
        var isClose = false

        val layoutParams = holder.binding.layoutPlayJoinRate.layoutParams

        val currentMillis = System.currentTimeMillis()
        val endMillis = PPLUS_DATE_FORMAT.parse(item.endDatetime).time

        if (currentMillis > endMillis) {
            if (item.winAnnounceType.equals("immediately")) {
                holder.binding.layoutPlayClose.visibility = View.GONE
            } else {
                holder.binding.layoutPlayClose.visibility = View.VISIBLE
            }

            isClose = true
        } else {
            holder.binding.layoutPlayClose.visibility = View.GONE
            isClose = false
        }

        if (item.winAnnounceType.equals("special")) {
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
            if (currentMillis > winAnnounceDate) {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                holder.binding.layoutPlayClose.visibility = View.GONE
                holder.binding.textPlayWinRate.visibility = View.GONE
                holder.binding.textPlayJoin.setText(R.string.msg_view_winner)
                holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                isClose = false
                isWinAnnounce = true
            } else {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
            }
        } else if (item.winAnnounceType.equals("limit")) {

            if (item.joinCount!! < item.maxJoinCount!!) {
                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
            } else {
                if(item.winAnnounceDatetime != null){
                    val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time
                    if (currentMillis > winAnnounceDate) {
                        Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                        holder.binding.layoutPlayClose.visibility = View.GONE
                        holder.binding.textPlayWinRate.visibility = View.GONE
                        holder.binding.textPlayJoin.setText(R.string.msg_view_winner)
                        holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                        isClose = false
                        isWinAnnounce = true
                    }else{
                        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                    }
                }else{
                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                }

            }
        } else if (item.winAnnounceType.equals("immediately")) {
            if (item.winnerCount!! < item.totalGiftCount!!) {
                if (currentMillis > endMillis && item.winnerCount!! > 0) {
                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                    holder.binding.layoutPlayClose.visibility = View.GONE
                    holder.binding.textPlayWinRate.visibility = View.GONE
                    holder.binding.textPlayJoin.setText(R.string.msg_view_winner)
                    holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                    isClose = false
                    isWinAnnounce = true
                }else{
                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                }
            } else {
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                holder.binding.layoutPlayClose.visibility = View.GONE
                holder.binding.textPlayWinRate.visibility = View.GONE
                holder.binding.textPlayJoin.setText(R.string.msg_view_winner)
                holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                isClose = false
                isWinAnnounce = true
            }
        }

        holder.countTimer?.cancel()

        if (isClose && !isWinAnnounce && (item.winAnnounceType == "special" || item.winAnnounceType == "limit") && StringUtils.isNotEmpty(item.winAnnounceDatetime)) {
            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime).time

            val remainWinMillis = winAnnounceDate - currentMillis
            if (remainWinMillis > 0) {
                holder.binding.layoutPlayClose.visibility = View.VISIBLE
                holder.countTimer = object : CountDownTimer(remainWinMillis, 1000) {

                    override fun onTick(millisUntilFinished: Long) {

                        val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                        val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                        val seconds = (millisUntilFinished / 1000).toInt() % 60

                        val strH = DateFormatUtils.formatTime(hours)
                        val strM = DateFormatUtils.formatTime(minutes)
                        val strS = DateFormatUtils.formatTime(seconds)

                        if (hours > 0) {
                            holder.binding.textPlayWinTime.text = "$strH:$strM:$strS"
                        } else {
                            if (minutes > 0) {
                                holder.binding.textPlayWinTime.text = "$strM:$strS"
                            } else {
                                holder.binding.textPlayWinTime.text = strS
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
                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imagePlay)
                holder.binding.layoutPlayClose.visibility = View.GONE
                holder.binding.textPlayWinRate.visibility = View.GONE
                holder.binding.textPlayJoin.setText(R.string.msg_view_winner)
                holder.binding.textPlayJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_11)
                isClose = false
                isWinAnnounce = true
            }
        } else {
            if (item.eventTime!!.isEmpty() || isClose || isWinAnnounce) {
                holder.binding.textPlayTime.visibility = View.GONE
            } else {
                holder.binding.textPlayTime.visibility = View.VISIBLE
                //                holder.text_proceeding.visibility = View.VISIBLE

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
                                    holder.binding.textPlayTime.text = "$strH:$strM:$strS"
                                } else {
                                    if (minutes > 0) {
                                        holder.binding.textPlayTime.text = "$strM:$strS"
                                    } else {
                                        holder.binding.textPlayTime.text = strS
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
                        holder.binding.textPlayTime.visibility = View.GONE
                    }
                } else {
                    holder.binding.textPlayTime.visibility = View.GONE
                }

            }
        }
        Glide.with(holder.itemView.context).load(R.drawable.ic_lucky_draw_rate).into(holder.binding.imagePlayGraph)
        when (item.winAnnounceType) {
            "immediately" -> {

                holder.binding.layoutPlayGraph.visibility = View.GONE
                holder.binding.textPlayJoinCount.visibility = View.GONE
            }
            "special" -> {
                holder.binding.layoutPlayGraph.visibility = View.VISIBLE
                holder.binding.textPlayJoinCount.visibility = View.VISIBLE
                var weightSum = 1
                if (item.minJoinCount != null && item.minJoinCount!! > 0) {
                    weightSum = item.minJoinCount!!
                }
                holder.binding.layoutPlayJoinRateTotal.weightSum = weightSum.toFloat()

                if (isClose || isWinAnnounce) {
                    holder.binding.viewPlayGraph.setBackgroundResource(R.drawable.bg_ea5506_radius_100)
                    holder.binding.imagePlayGraph.setImageResource(R.drawable.img_play_graph_full_icon)
                    holder.binding.textPlayJoinCount.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3, FormatUtil.getMoneyType(item.joinCount.toString())))
                } else {
                    holder.binding.viewPlayGraph.setBackgroundResource(R.drawable.bg_ea5506_radius_100)
                    Glide.with(holder.itemView.context).load(R.drawable.ic_lucky_draw_rate).into(holder.binding.imagePlayGraph)
                    holder.binding.textPlayJoinCount.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3, FormatUtil.getMoneyType(item.joinCount.toString())))
                }
            }
            "limit" -> {
                holder.binding.layoutPlayGraph.visibility = View.VISIBLE
                holder.binding.textPlayJoinCount.visibility = View.VISIBLE

                holder.binding.layoutPlayJoinRateTotal.weightSum = item.maxJoinCount!!.toFloat()

                if (item.joinCount!! < item.maxJoinCount!!) {
                    holder.binding.viewPlayGraph.setBackgroundResource(R.drawable.bg_ea5506_radius_100)
                    Glide.with(holder.itemView.context).load(R.drawable.ic_lucky_draw_rate).into(holder.binding.imagePlayGraph)
                    holder.binding.textPlayJoinCount.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                } else {
                    var joinCount = item.joinCount!!
                    if(joinCount >= item.maxJoinCount!!){
                        joinCount = item.maxJoinCount!!
                    }
                    holder.binding.viewPlayGraph.setBackgroundResource(R.drawable.bg_ea5506_radius_100)
                    holder.binding.imagePlayGraph.setImageResource(R.drawable.img_play_graph_full_icon)
                    holder.binding.textPlayJoinCount.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                }
            }
        }



        if (layoutParams is LinearLayout.LayoutParams) {
            when (item.winAnnounceType) {
//                EventType.WinAnnounceType.immediately.name -> {
//                    layoutParams.weight = item.winnerCount!!.toFloat()
//                }
                "special" -> {
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
                "limit" -> {
                    layoutParams.weight = item.joinCount!!.toFloat()
                    val rate = item.joinCount!!.toFloat() / item.maxJoinCount!!.toFloat()
                    if(rate >= 0.15f){
                        val margin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_13)
                        (holder.binding.imagePlayGraph.layoutParams as LinearLayout.LayoutParams).marginStart = -margin
                    }else{
                        val margin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_402) * rate
                        (holder.binding.imagePlayGraph.layoutParams as LinearLayout.LayoutParams).marginStart = -margin.toInt()
                    }
                }
            }

        }
        holder.binding.layoutPlayJoinRate.requestLayout()

        holder.binding.textPlayWinRate.setOnClickListener {
            listener?.onPercentClick(holder.absoluteAdapterPosition)
        }

        holder.binding.textPlayJoin.setOnClickListener {
            if (isClose) {
                PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item, launcher)
            } else {
                listener?.onItemClick(holder.absoluteAdapterPosition)
            }
        }

//        holder.itemView.setOnClickListener {
//            if (isClose) {
//                PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item, launcher)
//            } else {
//                listener?.onItemClick(holder.absoluteAdapterPosition)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}