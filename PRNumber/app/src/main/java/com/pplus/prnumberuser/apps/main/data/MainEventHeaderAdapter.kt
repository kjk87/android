//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.content.Intent
//import android.os.CountDownTimer
//import androidx.viewpager.widget.ViewPager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.PointConfigActivity
//import com.pplus.prnumberuser.apps.bol.ui.TicketConfigActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyHistoryActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyReviewActivity
//import com.pplus.prnumberuser.apps.main.ui.HotDealActivity
//import com.pplus.prnumberuser.apps.main.ui.UserFriendActivity
//import com.pplus.prnumberuser.apps.my.ui.MyWinHistoryActivity
//import com.pplus.prnumberuser.apps.my.ui.RankingActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.header_event_main.view.*
//import kotlinx.android.synthetic.main.item_event.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MainEventHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Event>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
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
//        return mDataList!![position]
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
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Event>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Event>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val pager_main_banner = itemView.pager_main_banner
//        val indicator_main_banner = itemView.indicator_main_banner
//        val text_main_retention_bol = itemView.text_main_retention_bol
//        val text_main_buy_history = itemView.text_main_buy_history
//        val text_main_buy_review = itemView.text_main_buy_review
//        val text_main_friend = itemView.text_main_friend
//        val text_main_win_history = itemView.text_main_win_history
//        val text_main_lotto_ticket = itemView.text_main_lotto_ticket
//        val text_main_ranking_more = itemView.text_main_ranking_more
//        val text_main_my_bol_ranking = itemView.text_main_my_bol_ranking
//        val text_main_my_recommend_ranking = itemView.text_main_my_recommend_ranking
//        val image_main_hot_deal = itemView.image_main_hot_deal
//
//        init {
//
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val layout = itemView.layout_event
//        val image = itemView.image_event
//        val text_time = itemView.text_event_time
//        val layout_close = itemView.layout_event_close
//        val text_win_time = itemView.text_event_win_time
//        val text_proceeding = itemView.text_event_proceeding
//        val image_win_result = itemView.image_event_win_result
//        var countTimer: CountDownTimer? = null
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//
//            val pagerAdapter = MainPagerAdapter(holder.itemView.context)
//            holder.pager_main_banner.adapter = pagerAdapter
//            holder.pager_main_banner.interval = 4000
//            holder.pager_main_banner.startAutoScroll()
//            holder.indicator_main_banner.removeAllViews()
//            holder.indicator_main_banner.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
//            holder.pager_main_banner.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//
//                    holder.indicator_main_banner.setCurrentItem(position)
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//
//                }
//            })
//
//            holder.text_main_retention_bol.setOnClickListener {
//                val intent = Intent(holder.itemView.context, PointConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            }
//
//            holder.text_main_buy_history.setOnClickListener {
//                val intent = Intent(holder.itemView.context, BuyHistoryActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                (holder.itemView.context as BaseActivity).startActivity(intent)
//            }
//
//            holder.text_main_buy_review.setOnClickListener {
//                val intent = Intent(holder.itemView.context, BuyReviewActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                (holder.itemView.context as BaseActivity).startActivity(intent)
//            }
//
//            holder.text_main_friend.setOnClickListener {
//                val intent = Intent(holder.itemView.context, UserFriendActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_main_win_history.setOnClickListener {
//                val intent = Intent(holder.itemView.context, MyWinHistoryActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_main_lotto_ticket.setOnClickListener {
//                val intent = Intent(holder.itemView.context, TicketConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_main_ranking_more.setOnClickListener {
//                val intent = Intent(holder.itemView.context, RankingActivity::class.java)
//                intent.putExtra(Const.TYPE, EnumData.RankType.recommend.name)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//                override fun reload() {
//
//                    if(holder.itemView.context == null){
//                        return
//                    }
//
//                    holder.text_main_retention_bol?.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//                }
//            })
//
//            ApiBuilder.create().myInviteRanking.setCallback(object : PplusCallback<NewResultResponse<User>> {
//                override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
//                    if (response!!.data != null) {
//                        if(holder.itemView.context == null){
//                            return
//                        }
//
//                        if (response.data.ranking!! > 999) {
//                            holder.text_main_my_recommend_ranking?.text = "-"
//                        } else {
//                            holder.text_main_my_recommend_ranking?.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_rank_unit, response.data.ranking.toString()))
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
//
//                }
//            }).build().call()
//
//            ApiBuilder.create().myRewardRanking.setCallback(object : PplusCallback<NewResultResponse<User>> {
//                override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
//                    if(holder.itemView.context == null){
//                        return
//                    }
//
//                    if (response!!.data != null) {
//                        if (response.data.ranking!! > 999) {
//                            holder.text_main_my_bol_ranking?.text = "-"
//                        } else {
//                            holder.text_main_my_bol_ranking?.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_rank_unit, response.data.ranking.toString()))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
//
//                }
//            }).build().call()
//
//            holder.image_main_hot_deal.setOnClickListener {
//                val intent = Intent(holder.itemView.context, HotDealActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//        } else if (holder is ViewHolder) {
//            holder.layout.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_500)
//            val item: Event = mDataList!![position-1]
//
//            val currentMillis = System.currentTimeMillis()
//            val endMillis = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.duration!!.end).time
//
//            var isWinAnnounce = false
//
//            var isClose = false
//
//            if (currentMillis > endMillis) {
//                if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                    holder.layout_close.visibility = View.GONE
//                } else {
//                    holder.layout_close.visibility = View.VISIBLE
//                }
//
//                isClose = true
//            } else {
//                holder.layout_close.visibility = View.GONE
//                isClose = false
//            }
//
//            holder.image_win_result.setOnClickListener {
//                val intent = Intent(mContext, EventImpressionActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                mContext?.startActivity(intent)
//            }
//
//            if (item.winnerCount!! > 0) {
//                holder.image_win_result.visibility = View.VISIBLE
//            } else {
//                holder.image_win_result.visibility = View.GONE
//            }
//
//            if (!item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                holder.image_win_result.visibility = View.GONE
//                val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//                if (currentMillis > winAnnounceDate) {
//                    Glide.with(holder.itemView.context).load(item.winImage?.url).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                    holder.layout_close.visibility = View.GONE
////                holder.image_win_result.visibility = View.VISIBLE
//                    isClose = false
//                    isWinAnnounce = true
//                } else {
//                    Glide.with(holder.itemView.context).load(item.bannerImage?.url).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                }
//            } else {
//                Glide.with(holder.itemView.context).load(item.bannerImage?.url).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//            }
//
//            if (holder.countTimer != null) {
//                holder.countTimer!!.cancel()
//            }
//
//            if (isClose && !isWinAnnounce && item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
//                val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//
//                val remainWinMillis = winAnnounceDate - currentMillis
//                if (remainWinMillis > 0) {
//                    holder.layout_close.visibility = View.VISIBLE
//                    holder.countTimer = object : CountDownTimer(remainWinMillis, 1000) {
//
//                        override fun onTick(millisUntilFinished: Long) {
//
//                            val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
//                            val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
//                            val seconds = (millisUntilFinished / 1000).toInt() % 60
//
//                            val strH = DateFormatUtils.formatTime(hours)
//                            val strM = DateFormatUtils.formatTime(minutes)
//                            val strS = DateFormatUtils.formatTime(seconds)
//
//                            if (hours > 0) {
//                                holder.text_win_time?.text = "$strH:$strM:$strS"
//                            } else {
//                                if (minutes > 0) {
//                                    holder.text_win_time?.text = "$strM:$strS"
//                                } else {
//                                    holder.text_win_time?.text = strS
//                                }
//                            }
//                        }
//
//                        override fun onFinish() {
//                            try {
//                                notifyItemChanged(holder.adapterPosition)
//
//                            } catch (e: Exception) {
//
//                            }
//
//
//                        }
//                    }
//                    holder.countTimer!!.start()
//                } else {
//                    Glide.with(holder.itemView.context).load(item.winImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                    holder.layout_close.visibility = View.GONE
//                    isClose = false
//                    isWinAnnounce = true
//                }
//            } else {
//                if (item.displayTimeList!!.isEmpty() || isClose || isWinAnnounce) {
//                    holder.text_time.visibility = View.GONE
//                    holder.text_proceeding.visibility = View.GONE
//                } else {
//                    holder.text_time.visibility = View.VISIBLE
////                holder.text_proceeding.visibility = View.VISIBLE
//                    holder.text_proceeding.visibility = View.GONE
//
//                    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//                    val currentTime = sdf.format(Date(currentMillis)).split(":")
//                    val currentSecond = (currentTime[0].toInt() * 60 * 60) + (currentTime[1].toInt() * 60) + currentTime[2].toInt()
//                    var endDisplayMillis = 0L
//                    for (time in item.displayTimeList!!) {
//                        val startSecond = (time.start.substring(0, 2).toInt() * 60 * 60) + (time.start.substring(2).toInt() * 60)
//                        val endSecond = (time.end.substring(0, 2).toInt() * 60 * 60) + (time.end.substring(2).toInt() * 60)
//
//                        if (currentSecond in startSecond..endSecond) {
//                            endDisplayMillis = endSecond * 1000L
//                            break
//                        }
//                    }
//
//                    if (endDisplayMillis > 0) {
//                        val remainMillis = endDisplayMillis - (currentSecond * 1000)
//
//                        if (remainMillis > 0) {
//                            holder.countTimer = object : CountDownTimer(remainMillis, 1000) {
//
//                                override fun onTick(millisUntilFinished: Long) {
//
//                                    val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
//                                    val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
//                                    val seconds = (millisUntilFinished / 1000).toInt() % 60
//
//                                    val strH = DateFormatUtils.formatTime(hours)
//                                    val strM = DateFormatUtils.formatTime(minutes)
//                                    val strS = DateFormatUtils.formatTime(seconds)
//
//                                    if (hours > 0) {
//                                        holder.text_time?.text = "$strH:$strM:$strS"
//                                    } else {
//                                        if (minutes > 0) {
//                                            holder.text_time?.text = "$strM:$strS"
//                                        } else {
//                                            holder.text_time?.text = strS
//                                        }
//                                    }
//                                }
//
//                                override fun onFinish() {
//
//                                    try {
//                                        notifyItemChanged(holder.adapterPosition)
//                                    } catch (e: Exception) {
//
//                                    }
//                                }
//                            }
//                            holder.countTimer!!.start()
//                        } else {
//                            holder.text_time.visibility = View.GONE
//                            holder.text_proceeding.visibility = View.GONE
//                        }
//                    } else {
//                        holder.text_time.visibility = View.GONE
//                        holder.text_proceeding.visibility = View.GONE
//                    }
//
//                }
//            }
//
//            holder.itemView.setOnClickListener {
//                if (isClose) {
//                    PplusCommonUtil.showEventAlert(mContext!!, 0, item)
//                } else {
//                    if (listener != null) {
//                        listener!!.onItemClick(holder.adapterPosition-1)
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.header_event_main, parent, false)
//            return ViewHeader(v)
//        } else if (viewType == TYPE_ITEM) {
//            var v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
//            return ViewHolder(v)
//        }
//        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
//    }
//
//    private fun isPositionHeader(position: Int): Boolean {
//        return position == 0
//    }
//
//}