package com.pplus.luckybol.apps.event.data

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.HeaderRandomPlayBinding
import com.pplus.luckybol.databinding.ItemGiftBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class RandomPlayHeaderGiftAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mEvent: Event? = null
    var mDataList: MutableList<EventGift>? = null
    var listener: OnItemClickListener? = null
    var mTotalCount = 0

    interface OnItemClickListener {

        fun onHeaderClick()

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventGift {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventGift>? {

        return mDataList
    }

    fun add(data: EventGift) {

        if (mDataList == null) {
            mDataList = ArrayList<EventGift>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventGift>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventGift>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventGift) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventGift>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventGift>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(binding: HeaderRandomPlayBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageRandomPlay
        val layout_rate_total = binding.layoutRandomPlayJoinRateTotal
        val layout_rate = binding.layoutRandomPlayJoinRate
        val text_join_count = binding.textRandomPlayJoinCount
        val view_graph = binding.viewRandomPlayGraph
        val image_graph = binding.imageRandomPlayGraph
        val text_amount = binding.textRandomPlayJoinAmount
        val layout_graph = binding.layoutRandomPlayGraph
        val layout_announce_time = binding.layoutRandomPlayAnnounceTime
        val text_announce_time = binding.textRandomPlayAnnounceTime

        init {
        }
    }

    class ViewHolder(binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageGift
        val layout_url = binding.layoutGiftUrl
        val text_name = binding.textGiftName
        val text_price = binding.textGiftPrice
        val text_reward_play = binding.textGiftRewardPlay
        val view_left_bar = binding.viewGiftLeftBar
        val view_right_bar = binding.viewGiftRightBar

        init {
            binding.layoutGiftWinner.visibility = View.GONE
            text_price.paintFlags = text_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        if (mEvent == null) {
            return mDataList!!.size
        }
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {
            val item = mEvent

            if (item != null) {

                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)

                val currentMillis = System.currentTimeMillis()
                val endMillis = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.duration!!.end).time

                var isWinAnnounce = false

                var isClose = false

                if (currentMillis > endMillis) {
                    isClose = true
                } else {
                    isClose = false
                }

                if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
                    val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
                    if (currentMillis > winAnnounceDate) {
                        isClose = false
                        isWinAnnounce = true
                    } else {
                        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
                    }
                } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
                    if (item.joinCount!! >= item.maxJoinCount!!) {
                        isClose = false
                        isWinAnnounce = true
                    }
                } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                    if (item.winnerCount!! < item.totalGiftCount!!) {
                        if (currentMillis > endMillis && item.winnerCount!! > 0) {
                            isClose = false
                            isWinAnnounce = true
                        }
                    } else {
                        isClose = false
                        isWinAnnounce = true
                    }
                }

                if (item.reward!! < 0) {
                    holder.text_amount.text = holder.itemView.context.getString(R.string.format_bol, FormatUtil.getMoneyTypeFloat(Math.abs(item.reward!!).toString()))
                } else {
                    holder.text_amount.text = holder.itemView.context.getString(R.string.word_free)
                }

                val layoutParams = holder.layout_rate.layoutParams;

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
                            holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon_red)
                            holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3_red, FormatUtil.getMoneyType(item.joinCount.toString())))
                        } else {
                            holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full)
                            holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                            holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3, FormatUtil.getMoneyType(item.joinCount.toString())))
                        }
                    }
                    EventType.WinAnnounceType.limit.name -> {
                        holder.layout_graph.visibility = View.VISIBLE
                        holder.text_join_count.visibility = View.VISIBLE

                        holder.layout_rate_total.weightSum = item.maxJoinCount!!.toFloat()

                        if (item.joinCount!! < item.maxJoinCount!!) {
                            holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full)
                            holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
                            holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
                        } else {
                            var joinCount = item.joinCount!!
                            if (joinCount >= item.maxJoinCount!!) {
                                joinCount = item.maxJoinCount!!
                            }
                            holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full_red)
                            holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon_red)
                            holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
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
                if(StringUtils.isNotEmpty(item.winAnnounceRandomDatetime)){
                    holder.layout_announce_time.visibility = View.VISIBLE
                    holder.text_announce_time.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_announce_time_desc, item.winAnnounceRandomDatetime!!.substring(0, 5)))
                }else{
                    holder.layout_announce_time.visibility = View.GONE
                }

                holder.itemView.setOnClickListener {
//                    if (isClose) {
//                        PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item)
//                    } else {
//                        listener?.onHeaderClick()
//                    }
                }
            }

        } else if (holder is ViewHolder) {

            var pos = position
            if (mEvent != null) {
                pos = position - 1
            }
            if(pos % 2 == 0){
                holder.view_left_bar.visibility = View.GONE
                holder.view_right_bar.visibility = View.VISIBLE
            }else{
                holder.view_left_bar.visibility = View.VISIBLE
                holder.view_right_bar.visibility = View.GONE
            }

            val item = getItem(pos)
            Glide.with(holder.itemView.context).load(item.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            holder.text_name.text = item.title
            holder.text_price.text = holder.itemView.context.getString(R.string.format_normal_price, FormatUtil.getMoneyType(item.price.toString()))
            holder.text_reward_play.text = holder.itemView.context.getString(R.string.format_join_price, FormatUtil.getMoneyTypeFloat(mEvent!!.rewardPlay.toString()))

            if(StringUtils.isNotEmpty(item.giftLink)){
                holder.layout_url.visibility = View.VISIBLE
                holder.layout_url.setOnClickListener {
                    PplusCommonUtil.openChromeWebView(holder.itemView.context, item.giftLink!!)
                }
            }else{
                holder.layout_url.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_HEADER) {
            val binding = HeaderRandomPlayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0 && mEvent != null
    }

}