package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ItemPlayBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PlayAdapter() : RecyclerView.Adapter<PlayAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
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

    class ViewHolder(binding: ItemPlayBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_live = binding.imagePlayLive
        val image = binding.imagePlay
        val text_time = binding.textPlayTime
        val layout_close = binding.layoutPlayClose
        val text_win_time = binding.textPlayWinTime
        val text_proceeding = binding.textPlayProceeding
        val text_title = binding.textPlayTitle
        val layout_rate_total = binding.layoutPlayJoinRateTotal
        val layout_rate = binding.layoutPlayJoinRate
        val text_join_count = binding.textPlayJoinCount
        val view_graph = binding.viewPlayGraph
        val image_graph = binding.imagePlayGraph
        val text_amount = binding.textPlayJoinAmount
        val layout_graph = binding.layoutPlayGraph
        val layout_lotto_status = binding.layoutPlayLottoStatus
        val text_lotto_status = binding.textPlayLottoStatus
        val layout_lotto_winner_count = binding.layoutPlayLottoWinnerCount
        val text_lotto_winner_count = binding.textPlayLottoCount
        var countTimer: CountDownTimer? = null

        init {
            text_title.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        val isLotto = item.isLotto != null && item.isLotto!!

        holder.text_title.text = item.title

        if(item.rewardPlay != null && item.rewardPlay!! > 0){
            holder.text_amount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
            holder.text_amount.text = holder.itemView.context.getString(R.string.format_join_price, FormatUtil.getMoneyTypeFloat(item.rewardPlay.toString()))
        }else{
            holder.text_amount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
            if (item.reward!! < 0) {
                holder.text_amount.text = holder.itemView.context.getString(R.string.format_bol, FormatUtil.getMoneyTypeFloat(Math.abs(item.reward!!).toString()))
            } else {
                holder.text_amount.text = holder.itemView.context.getString(R.string.word_free)
            }
        }

        var isWinAnnounce = false
        var isClose = false

        val layoutParams = holder.layout_rate.layoutParams

        holder.layout_lotto_winner_count.visibility = View.GONE
        if(isLotto){
            holder.image_live.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            when (item.status) {
                "active" -> {
                    holder.layout_lotto_status.visibility = View.GONE
                    holder.text_lotto_status.setText(R.string.word_recruit_ing)
                }
                "conclude" -> {
                    isClose = true
                    holder.layout_lotto_status.visibility = View.VISIBLE
                    holder.text_lotto_status.visibility = View.VISIBLE
                    holder.text_lotto_status.setText(R.string.word_recruit_complete)
                }
                "pending" -> {
                    isClose = true
                    holder.layout_lotto_status.visibility = View.VISIBLE
                    holder.text_lotto_status.visibility = View.VISIBLE
                    holder.text_lotto_status.setText(R.string.word_announce_wait)
                }
                "announce", "complete", "finish" -> {
                    isClose = true
                    isWinAnnounce = true
                    holder.layout_lotto_status.visibility = View.VISIBLE
                    holder.text_lotto_status.visibility = View.GONE

                    val params = HashMap<String, String>()
                    params["eventSeqNo"] = item.no.toString()
                    ApiBuilder.create().getLottoWinnerCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                        override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                                response: NewResultResponse<Int>?) {
                            if(response?.data != null){
                                holder.layout_lotto_winner_count.visibility = View.VISIBLE
                                holder.text_lotto_winner_count.text = response.data.toString()
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<Int>?) {
                        }
                    }).build().call()
                }
            }

            holder.layout_graph.visibility = View.VISIBLE
            holder.text_join_count.visibility = View.VISIBLE

            holder.layout_rate_total.weightSum = item.maxJoinCount!!.toFloat()

            if (item.joinCount!! < item.maxJoinCount!!) {
                holder.view_graph.setBackgroundResource(R.drawable.bg_fc5c57_radius_66)
                holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
            } else {
                var joinCount = item.joinCount!!
                joinCount = item.maxJoinCount!!
                holder.view_graph.setBackgroundResource(R.drawable.bg_fc5c57_radius_66)
                holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
            }

        }else{
            holder.image_live.visibility = View.GONE
            holder.layout_lotto_status.visibility = View.GONE

            val currentMillis = System.currentTimeMillis()
            val endMillis = PPLUS_DATE_FORMAT.parse(item.duration!!.end).time

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

            if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
                val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
                if (currentMillis > winAnnounceDate) {
                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                    holder.layout_close.visibility = View.GONE
                    isClose = false
                    isWinAnnounce = true
                } else {
                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                }
            } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
                if (item.joinCount!! < item.maxJoinCount!!) {
                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                } else {
                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                    holder.layout_close.visibility = View.GONE
                    isClose = false
                    isWinAnnounce = true
                }
            } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                if (item.winnerCount!! < item.totalGiftCount!!) {
                    if (currentMillis > endMillis && item.winnerCount!! > 0) {
                        Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                        holder.layout_close.visibility = View.GONE
                        isClose = false
                        isWinAnnounce = true
                    }else{
                        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                    }


                } else {
                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
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
                                        notifyItemChanged(holder.absoluteAdapterPosition)

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

            when (item.winAnnounceType) {
                EventType.WinAnnounceType.immediately.name -> {

                    holder.layout_graph.visibility = View.GONE
                    holder.text_join_count.visibility = View.GONE
                }
                EventType.WinAnnounceType.special.name -> {
                    holder.layout_graph.visibility = View.VISIBLE
                    holder.text_join_count.visibility = View.VISIBLE
                    var weightSum = 1
                    if (item.minJoinCount != null && item.minJoinCount!! > 0) {
                        weightSum = item.minJoinCount!!
                    }
                    holder.layout_rate_total.weightSum = weightSum.toFloat()

                    if (isClose || isWinAnnounce) {
                        holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full_red)
                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3_red, FormatUtil.getMoneyType(item.joinCount.toString())))
                    } else {
                        holder.view_graph.setBackgroundResource(R.drawable.bg_fc5c57_radius_100)
                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3, FormatUtil.getMoneyType(item.joinCount.toString())))
                    }
                }
                EventType.WinAnnounceType.limit.name -> {
                    holder.layout_graph.visibility = View.VISIBLE
                    holder.text_join_count.visibility = View.VISIBLE

                    holder.layout_rate_total.weightSum = item.maxJoinCount!!.toFloat()

                    if (item.joinCount!! < item.maxJoinCount!!) {
                        holder.view_graph.setBackgroundResource(R.drawable.bg_fc5c57_radius_100)
                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                    } else {
                        var joinCount = item.joinCount!!
                        if(joinCount >= item.maxJoinCount!!){
                            joinCount = item.maxJoinCount!!
                        }
                        holder.view_graph.setBackgroundResource(R.drawable.bg_fc5c57_radius_100)
                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                    }
                }
            }
        }



        if (layoutParams is LinearLayout.LayoutParams) {
            when (item.winAnnounceType) {
//                EventType.WinAnnounceType.immediately.name -> {
//                    layoutParams.weight = item.winnerCount!!.toFloat()
//                }
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

        holder.itemView.setOnClickListener {
            if(isLotto){
                listener?.onItemClick(holder.absoluteAdapterPosition)
            }else{
                if (isClose) {
                    PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item, launcher)
                } else {
                    listener?.onItemClick(holder.absoluteAdapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}