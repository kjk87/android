//package com.pplus.luckybol.apps.event.data
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.CountDownTimer
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.annotation.Nullable
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.buzzvil.buzzad.benefit.core.ad.AdError
//import com.buzzvil.buzzad.benefit.presentation.media.CtaPresenter
//import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAd
//import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdLoader
//import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdLoader.OnAdLoadedListener
//import com.buzzvil.buzzad.benefit.presentation.nativead.NativeAdView
//import com.buzzvil.buzzad.benefit.presentation.reward.RewardResult
//import com.buzzvil.buzzad.benefit.presentation.video.VideoErrorStatus
//import com.buzzvil.buzzad.benefit.presentation.video.VideoEventListener
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.event.ui.EventImpressionActivity
//import com.pplus.luckybol.core.code.common.EventType
//import com.pplus.luckybol.core.network.model.dto.Event
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import kotlinx.android.synthetic.main.item_buzvill_banner.view.*
//import kotlinx.android.synthetic.main.item_play.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PlayGroupDHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    val TYPE_HEADER = 0
//    val TYPE_ITEM = 1
//
////    var mEvent: Event? = null
//    var mDataList: MutableList<Event>? = null
//    var listener: OnItemClickListener? = null
//    var mTotalCount = 0
//
//    interface OnItemClickListener {
//
//        fun onHeaderClick(item: Event)
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
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
//        if(mDataList != null && mDataList!!.isNotEmpty()){
//            mDataList!!.set(position, data)
//            notifyDataSetChanged()
//        }
//
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
//        val native_ad_view = itemView.native_ad_view
//        val mediaView = itemView.mediaView
//        val textTitle = itemView.textTitle
//        val textDescription = itemView.textDescription
//        val imageIcon = itemView.imageIcon
//        val ctaView = itemView.ctaView
//        init {
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_play
//        val text_time = itemView.text_play_time
//        val layout_close = itemView.layout_play_close
//        val text_win_time = itemView.text_play_win_time
//        val text_proceeding = itemView.text_play_proceeding
//        val image_win_result = itemView.image_play_win_result
//        val text_title = itemView.text_play_title
//        val layout_rate_total = itemView.layout_play_join_rate_total
//        val layout_rate = itemView.layout_play_join_rate
//        val text_join_count = itemView.text_play_join_count
//        val view_graph = itemView.view_play_graph
//        val image_graph = itemView.image_play_graph
//        val text_amount = itemView.text_play_join_amount
//        val layout_graph = itemView.layout_play_graph
//        var countTimer: CountDownTimer? = null
//
//        init {
//            text_title.setSingleLine()
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
//            val loader = NativeAdLoader("256561826471494")
//            loader.loadAd(object : OnAdLoadedListener {
//                override fun onLoadError(adError: AdError) {
//                    LogUtil.e("NativeAdLoader", "Failed to load a native ad.", adError)
//                }
//
//                override fun onAdLoaded(nativeAd: NativeAd) {
//
//                    val ad = nativeAd.ad
//                    val creativeType = if (ad.creative == null) null else ad.creative!!.getType()
//                    val ctaPresenter = CtaPresenter(holder.ctaView)
//                    ctaPresenter.bind(nativeAd);
//
//                    // 1) Assign Ad Properties.
//                    // 1) Assign Ad Properties.
//                    if (holder.mediaView != null) {
//                        holder.mediaView.setCreative(ad.creative)
//                        holder.mediaView.setVideoEventListener(object : VideoEventListener { // Override and implement methods
//                            override fun onVideoStarted() {
//
//                            }
//
//                            override fun onResume() {
//
//                            }
//
//                            override fun onPause() {
//
//                            }
//
//                            override fun onReplay() {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onLanding() {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onError(p0: VideoErrorStatus, p1: String?) {
//                                TODO("Not yet implemented")
//                            }
//
//                            override fun onVideoEnded() {
//                                TODO("Not yet implemented")
//                            }
//                        })
//                    }
//
//                    if (holder.textTitle != null) {
//                        holder.textTitle.text = ad.title
//                    }
//
//                    if (holder.imageIcon != null) {
//                        Glide.with(holder.itemView.context).load(ad.iconUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.imageIcon)
//                    }
//
//                    if (holder.textDescription != null) {
//                        holder.textDescription.setText(ad.description)
//                    }
//
//                    // 2) Create a list of clickable views.
//
//                    // 2) Create a list of clickable views.
//                    val clickableViews: MutableList<View> = ArrayList()
//                    clickableViews.add(holder.ctaView)
//                    clickableViews.add(holder.mediaView)
//                    clickableViews.add(holder.textTitle)
//                    clickableViews.add(holder.textDescription)
//
//                    // 3) Register ClickableViews, MediaView and NativeAd to NativeAdView.
//
//                    // 3) Register ClickableViews, MediaView and NativeAd to NativeAdView.
//                    holder.native_ad_view.setClickableViews(clickableViews)
//                    holder.native_ad_view.setMediaView(holder.mediaView)
//                    holder.native_ad_view.setNativeAd(nativeAd)
//
//                    // 4) Add OnNativeAdEventListener to NativeAdView and implement participated UI.
//                    // 4) Add OnNativeAdEventListener to NativeAdView and implement participated UI.
//                    holder.native_ad_view.addOnNativeAdEventListener(object : NativeAdView.OnNativeAdEventListener {
//                        override fun onImpressed(view: NativeAdView, nativeAd: NativeAd) {}
//                        override fun onClicked(view: NativeAdView, nativeAd: NativeAd) {
//                            // Action형 광고에 대한 CTA 처리
//                            ctaPresenter.bind(nativeAd)
//                        }
//
//                        override fun onRewardRequested(view: NativeAdView, nativeAd: NativeAd) {
//                            // 퍼블리셔 기획에 따라 리워드 로딩 이미지를 보여주는 등의 처리
//                        }
//
//                        override fun onRewarded(view: NativeAdView,
//                                       nativeAd: NativeAd,
//                                       @Nullable rewardResult: RewardResult?) {
//                            // 리워드 적립의 결과 (RewardResult) SUCCESS, ALREADY_PARTICIPATED, MISSING_REWARD 등에 따라서 적절한 유저 커뮤니케이션 처리
//                        }
//
//                        override fun onParticipated(view: NativeAdView, nativeAd: NativeAd) {
//                            // 퍼블리셔 기획에 따라 UI 처리
//                            ctaPresenter.bind(nativeAd)
//                        }
//                    })
//
//                }
//            })
//
//        } else if (holder is ViewHolder) {
//
//            val pos = position - 1
//
//            val item = getItem(pos)
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
//                val intent = Intent(holder.itemView.context, EventImpressionActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//
//            if (item.winnerCount!! > 0) {
//                holder.image_win_result.visibility = View.GONE
//            } else {
//                holder.image_win_result.visibility = View.GONE
//            }
//
//            if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
//                holder.image_win_result.visibility = View.GONE
//                val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
//                if (currentMillis > winAnnounceDate) {
//                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                    holder.layout_close.visibility = View.GONE
//                    isClose = false
//                    isWinAnnounce = true
//                } else {
//                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                }
//            } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.limit.name)) {
//                holder.image_win_result.visibility = View.GONE
//                if (item.joinCount!! < item.maxJoinCount!!) {
//                    Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                } else {
//                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                    holder.layout_close.visibility = View.GONE
//                    isClose = false
//                    isWinAnnounce = true
//                }
//            } else if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                if (item.winnerCount!! < item.totalGiftCount!!) {
//                    if (currentMillis > endMillis && item.winnerCount!! > 0) {
//                        holder.image_win_result.visibility = View.GONE
//                        Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                        holder.layout_close.visibility = View.GONE
//                        isClose = false
//                        isWinAnnounce = true
//                    } else {
//                        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                    }
//
//
//                } else {
//                    holder.image_win_result.visibility = View.GONE
//                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
//                    holder.layout_close.visibility = View.GONE
//                    isClose = false
//                    isWinAnnounce = true
//                }
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
//                                holder.text_win_time.text = "$strH:$strM:$strS"
//                            } else {
//                                if (minutes > 0) {
//                                    holder.text_win_time.text = "$strM:$strS"
//                                } else {
//                                    holder.text_win_time.text = strS
//                                }
//                            }
//                        }
//
//                        override fun onFinish() {
//
//
//                            try {
//                                notifyItemChanged(holder.absoluteAdapterPosition)
//                            } catch (e: Exception) {
//
//                            }
//
//                        }
//                    }
//                    holder.countTimer!!.start()
//                } else {
//                    Glide.with(holder.itemView.context).load(item.winImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
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
//                    //                holder.text_proceeding.visibility = View.VISIBLE
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
//                                        holder.text_time.text = "$strH:$strM:$strS"
//                                    } else {
//                                        if (minutes > 0) {
//                                            holder.text_time.text = "$strM:$strS"
//                                        } else {
//                                            holder.text_time.text = strS
//                                        }
//                                    }
//                                }
//
//                                override fun onFinish() {
//
//
//                                    try {
//                                        notifyItemChanged(holder.absoluteAdapterPosition)
//
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
//            holder.text_title.text = item.title
//
//            if(item.rewardPlay != null && item.rewardPlay!! > 0){
//                holder.text_amount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//                holder.text_amount.text = holder.itemView.context.getString(R.string.format_join_price, FormatUtil.getMoneyType(item.rewardPlay.toString()))
//            }else{
//                holder.text_amount.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
//                if (item.reward!! < 0) {
//                    holder.text_amount.text = holder.itemView.context.getString(R.string.format_bol, FormatUtil.getMoneyType(Math.abs(item.reward!!).toString()))
//                } else {
//                    holder.text_amount.text = holder.itemView.context.getString(R.string.word_free)
//                }
//            }
//
//            val layoutParams = holder.layout_rate.layoutParams;
//
//            when (item.winAnnounceType) {
//                EventType.WinAnnounceType.immediately.name -> {
//
//                    holder.layout_graph.visibility = View.GONE
//                    holder.text_join_count.visibility = View.GONE
//                }
//                EventType.WinAnnounceType.special.name -> {
//                    holder.layout_graph.visibility = View.VISIBLE
//                    holder.text_join_count.visibility = View.VISIBLE
//                    var weightSum = 1
//                    if (item.minJoinCount!! > 0) {
//                        weightSum = item.minJoinCount!!
//                    }
//                    holder.layout_rate_total.weightSum = weightSum.toFloat()
//
//                    if (isClose || isWinAnnounce) {
//                        holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full_red)
//                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon_red)
//                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3_red, FormatUtil.getMoneyType(item.joinCount.toString())))
//                    } else {
//                        holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full)
//                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
//                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count3, FormatUtil.getMoneyType(item.joinCount.toString())))
//                    }
//                }
//                EventType.WinAnnounceType.limit.name -> {
//                    holder.layout_graph.visibility = View.VISIBLE
//                    holder.text_join_count.visibility = View.VISIBLE
//
//                    holder.layout_rate_total.weightSum = item.maxJoinCount!!.toFloat()
//
//                    if (item.joinCount!! < item.maxJoinCount!!) {
//                        holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full)
//                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon)
//                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count, FormatUtil.getMoneyType(item.joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
//                    } else {
//                        var joinCount = item.joinCount!!
//                        if (joinCount >= item.maxJoinCount!!) {
//                            joinCount = item.maxJoinCount!!
//                        }
//                        holder.view_graph.setBackgroundResource(R.drawable.img_play_graph_full_red)
//                        holder.image_graph.setImageResource(R.drawable.img_play_graph_full_icon_red)
//                        holder.text_join_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_msg_play_join_count_red, FormatUtil.getMoneyType(joinCount.toString()), FormatUtil.getMoneyType(item.maxJoinCount.toString())))
//                    }
//                }
//            }
//
//            if (layoutParams is LinearLayout.LayoutParams) {
//                when (item.winAnnounceType) {
//                    //                EventType.WinAnnounceType.immediately.name -> {
//                    //                    layoutParams.weight = item.winnerCount!!.toFloat()
//                    //                }
//                    EventType.WinAnnounceType.special.name -> {
//                        if (item.minJoinCount!! > 0) {
//                            if (item.joinCount!! > item.minJoinCount!!) {
//                                layoutParams.weight = item.minJoinCount!!.toFloat()
//                            } else {
//                                layoutParams.weight = item.joinCount!!.toFloat()
//                            }
//                        } else {
//                            layoutParams.weight = 1f
//                        }
//
//                    }
//                    EventType.WinAnnounceType.limit.name -> {
//                        layoutParams.weight = item.joinCount!!.toFloat()
//                    }
//                }
//
//            }
//            holder.layout_rate.requestLayout()
//
//            holder.itemView.setOnClickListener {
//                if (isClose) {
//                    PplusCommonUtil.showEventAlert(holder.itemView.context, 0, item)
//                } else {
//                    listener?.onItemClick(holder.absoluteAdapterPosition - 1)
//
//                }
//            }
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_buzvill_banner, parent, false))
//        } else if (viewType == TYPE_ITEM) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_play, parent, false))
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
//    private fun existDaummapApp(context: Context): Boolean {
//        val pm = context.packageManager
//
//        try {
//            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
//        } catch (e: PackageManager.NameNotFoundException) {
//            return false;
//        }
//    }
//}