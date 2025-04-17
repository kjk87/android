//package com.pplus.prnumberuser.apps.event.data
//
//import android.content.Context
//import android.content.Intent
//import android.os.CountDownTimer
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_number_event.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class NumberEventAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<NumberEventAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Event>? = null
//    var listener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, isOpen: Boolean)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Event {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<Event>? {
//
//        return mDataList
//    }
//
//    fun add(data: Event) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Event>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Event>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Event>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Event) {
//        if (position != -1 && mDataList!!.size > 0) {
//            mDataList!!.removeAt(position)
//            mDataList!!.add(position, data)
//            notifyItemChanged(position)
//        }
//
//    }
//
//    fun clear() {
//        notifyItemRangeRemoved(0, mDataList!!.size)
//        mDataList = ArrayList<Event>()
//    }
//
//    fun setDataList(dataList: MutableList<Event>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_number_event
//        val text_time = itemView.text_number_event_time
//        val layout_close = itemView.layout_number_event_close
//        val text_win_time = itemView.text_number_event_win_time
//        val text_proceeding = itemView.text_number_event_proceeding
//        val image_win_result = itemView.image_number_event_win_result
//        var countTimer: CountDownTimer? = null
//
//        init {
//
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item: Event = mDataList!!.get(position)
//
//        val currentMillis = System.currentTimeMillis()
//        val endMillis = PPLUS_DATE_FORMAT.parse(item.duration!!.end).time
//
//        var isWinAnnounce = false
//
//        var isClose = false
//        var isOpen = false
//
//        if (currentMillis > endMillis) {
//            if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                holder.layout_close.visibility = View.GONE
//            } else {
//                holder.layout_close.visibility = View.VISIBLE
//            }
//
//            isClose = true
//        } else {
//            holder.layout_close.visibility = View.GONE
//            isClose = false
//        }
//
//        holder.image_win_result.setOnClickListener {
//            val intent = Intent(mContext, EventImpressionActivity::class.java)
//            intent.putExtra(Const.DATA, item)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            mContext?.startActivity(intent)
//        }
//
//        if (item.winnerCount!! > 0) {
//            holder.image_win_result.visibility = View.VISIBLE
//        } else {
//            holder.image_win_result.visibility = View.GONE
//        }
//
//        if (!item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//            holder.image_win_result.visibility = View.GONE
//            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//            if (currentMillis > winAnnounceDate) {
//                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                holder.layout_close.visibility = View.GONE
////                holder.image_win_result.visibility = View.VISIBLE
//                isClose = false
//                isWinAnnounce = true
//            } else {
//                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//            }
//        } else {
//            Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        }
//
//        if (holder.countTimer != null) {
//            holder.countTimer!!.cancel()
//        }
//
//        if (isClose && !isWinAnnounce && item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
//            val winAnnounceDate = PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//
//            val remainWinMillis = winAnnounceDate - currentMillis
//            if (remainWinMillis > 0) {
//                holder.layout_close.visibility = View.VISIBLE
//                holder.countTimer = object : CountDownTimer(remainWinMillis, 1000) {
//
//                    override fun onTick(millisUntilFinished: Long) {
//
//                        val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
//                        val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
//                        val seconds = (millisUntilFinished / 1000).toInt() % 60
//
//                        val strH = DateFormatUtils.formatTime(hours)
//                        val strM = DateFormatUtils.formatTime(minutes)
//                        val strS = DateFormatUtils.formatTime(seconds)
//
//                        if (hours > 0) {
//                            holder.text_win_time?.text = "$strH:$strM:$strS"
//                        } else {
//                            if (minutes > 0) {
//                                holder.text_win_time?.text = "$strM:$strS"
//                            } else {
//                                holder.text_win_time?.text = strS
//                            }
//                        }
//                    }
//
//                    override fun onFinish() {
//                        try {
//                            notifyItemChanged(holder.adapterPosition)
//
////                            if (mContext != null) {
////                                Glide.with(mContext).load(item.winImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
////                                holder.layout_close.visibility = View.GONE
////                                isClose = false
////                                isWinAnnounce = true
////                            }
//                        } catch (e: Exception) {
//
//                        }
//
//
//                    }
//                }
//                holder.countTimer!!.start()
//            } else {
//                Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                holder.layout_close.visibility = View.GONE
//                isClose = false
//                isWinAnnounce = true
//            }
//        } else {
//            if (item.displayTimeList!!.isEmpty() || isClose || isWinAnnounce) {
//                holder.text_time.visibility = View.GONE
//
//                holder.text_proceeding.visibility = View.GONE
//                isOpen = item.displayTimeList!!.isEmpty()
//            } else {
//                holder.text_time.visibility = View.VISIBLE
//                isOpen = true
////                holder.text_proceeding.visibility = View.VISIBLE
//                holder.text_proceeding.visibility = View.GONE
//
//                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//                val currentTime = sdf.format(Date(currentMillis)).split(":")
//                val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
//                var endDisplayMillis = 0L
//                for (time in item.displayTimeList!!) {
//                    val startSecond = (time.start.substring(0, 2).toInt() * 60 * 60) + (time.start.substring(2).toInt() * 60)
//                    val endSecond = (time.end.substring(0, 2).toInt() * 60 * 60) + (time.end.substring(2).toInt() * 60)
//
//                    if (currentSecond in startSecond..endSecond) {
//                        endDisplayMillis = endSecond * 1000L
//                        break
//                    }
//                }
//
//                if (endDisplayMillis > 0) {
//                    val remainMillis = endDisplayMillis - (currentSecond * 1000)
//
//                    if (remainMillis > 0) {
//                        holder.countTimer = object : CountDownTimer(remainMillis, 1000) {
//
//                            override fun onTick(millisUntilFinished: Long) {
//
//                                val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
//                                val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
//                                val seconds = (millisUntilFinished / 1000).toInt() % 60
//
//                                val strH = DateFormatUtils.formatTime(hours)
//                                val strM = DateFormatUtils.formatTime(minutes)
//                                val strS = DateFormatUtils.formatTime(seconds)
//
//                                if (hours > 0) {
//                                    holder.text_time?.text = "$strH:$strM:$strS"
//                                } else {
//                                    if (minutes > 0) {
//                                        holder.text_time?.text = "$strM:$strS"
//                                    } else {
//                                        holder.text_time?.text = strS
//                                    }
//                                }
//                            }
//
//                            override fun onFinish() {
//
//                                try {
//                                    notifyItemChanged(holder.adapterPosition)
//                                } catch (e: Exception) {
//
//                                }
//                            }
//                        }
//                        holder.countTimer!!.start()
//                    } else {
//                        holder.text_time.visibility = View.GONE
//                        isOpen = false
//                        holder.text_proceeding.visibility = View.GONE
//                    }
//                } else {
//                    holder.text_time.visibility = View.GONE
//                    isOpen = false
//                    holder.text_proceeding.visibility = View.GONE
//                }
//
//            }
//        }
//
//        holder.itemView.setOnClickListener {
//            if (isClose) {
//                PplusCommonUtil.showEventAlert(mContext!!, 0, item)
//            } else {
//                if (listener != null) {
//                    listener!!.onItemClick(holder.adapterPosition, isOpen)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_number_event, parent, false)
//        return ViewHolder(v)
//    }
//}