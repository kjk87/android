//package com.lejel.wowbox.apps.luckydraw.data
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import android.widget.PopupWindow
//import android.widget.RelativeLayout
//import androidx.activity.result.ActivityResultLauncher
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.lejel.wowbox.Const
//import com.lejel.wowbox.R
//import com.lejel.wowbox.apps.common.builder.AlertBuilder
//import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
//import com.lejel.wowbox.apps.common.builder.data.AlertData
//import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
//import com.lejel.wowbox.apps.common.ui.base.BaseActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawCheckPrivateActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawCompleteListActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawDetailActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawGiftActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawJoinActivity
//import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawWinActivity
//import com.lejel.wowbox.apps.luckydraw.ui.MyLuckyDrawHistoryActivity
//import com.lejel.wowbox.apps.main.data.MainLuckyDrawGiftAdapter
//import com.lejel.wowbox.apps.wallet.ui.WalletMakeActivity
//import com.lejel.wowbox.core.network.ApiBuilder
//import com.lejel.wowbox.core.network.model.dto.LuckyDraw
//import com.lejel.wowbox.core.network.model.dto.LuckyDrawGift
//import com.lejel.wowbox.core.network.model.dto.WalletRes
//import com.lejel.wowbox.core.network.model.response.NewResultResponse
//import com.lejel.wowbox.core.util.PplusCommonUtil
//import com.lejel.wowbox.databinding.HeaderEventImpressionBinding
//import com.lejel.wowbox.databinding.HeaderLuckyDrawBinding
//import com.lejel.wowbox.databinding.ItemEventGiftBinding
//import com.lejel.wowbox.databinding.ItemEventImpressionBinding
//import com.lejel.wowbox.databinding.ItemLuckyDrawBinding
//import com.lejel.wowbox.databinding.PopupGuideBinding
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.resource.ResourceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import retrofit2.Call
//import java.text.SimpleDateFormat
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class LuckyDrawHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mDataList: MutableList<LuckyDraw>? = null
//    var listener: OnItemClickListener? = null
//    var launcher: ActivityResultLauncher<Intent>? = null
//    var checkPrivateLauncher: ActivityResultLauncher<Intent>? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): LuckyDraw {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<LuckyDraw>? {
//
//        return mDataList
//    }
//
//    fun add(data: LuckyDraw) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<LuckyDraw>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<LuckyDraw>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<LuckyDraw>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: LuckyDraw) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<LuckyDraw>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<LuckyDraw>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(val binding: HeaderLuckyDrawBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        init {
//        }
//    }
//
//    class ViewHolder(val binding: ItemLuckyDrawBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size + 1
//    }
//
////    private fun goJoinHistory(context: Context){
////        if (!PplusCommonUtil.loginCheck((context as BaseActivity), launcher)) {
////            return
////        }
////
////        val intent = Intent(context, MyLuckyDrawHistoryActivity::class.java)
////        intent.putExtra(Const.TAB, 0)
////        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////        launcher?.launch(intent)
////    }
////
////    private fun goWinHistory(context: Context){
////        if (!PplusCommonUtil.loginCheck((context as BaseActivity), launcher)) {
////            return
////        }
////
////        val intent = Intent(context, MyLuckyDrawHistoryActivity::class.java)
////        intent.putExtra(Const.TAB, 1)
////        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////        launcher?.launch(intent)
////    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            holder.binding.textMainLuckyDrawComplete.setOnClickListener {
//                val intent = Intent(holder.itemView.context, LuckyDrawCompleteListActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                launcher?.launch(intent)
//            }
//
//            holder.binding.textMainLuckyDrawMyWinHistory.setOnClickListener {
//                if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
//                    return@setOnClickListener
//                }
//
//                val intent = Intent(holder.itemView.context, MyLuckyDrawHistoryActivity::class.java)
//                intent.putExtra(Const.TAB, 1)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                launcher?.launch(intent)
//            }
//
//            holder.binding.textMainLuckyDrawMyPurchaseHistory.setOnClickListener {
//                if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
//                    return@setOnClickListener
//                }
//
//                val intent = Intent(holder.itemView.context, MyLuckyDrawHistoryActivity::class.java)
//                intent.putExtra(Const.TAB, 0)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                launcher?.launch(intent)
//            }
//
////            var startX = 0f
////            var startY = 0f
////            holder.binding.layoutMainLuckyDrawHistory.setOnTouchListener { view, motionEvent ->
////                if (motionEvent.action === MotionEvent.ACTION_DOWN) {
////                    startX = motionEvent.x
////                    startY = motionEvent.y
////                }
////
////                if (motionEvent.action === MotionEvent.ACTION_UP) {
////                    val dX = Math.abs((motionEvent.x - startX).toDouble()).toInt()
////                    val dY = Math.abs((motionEvent.y - startY).toDouble()).toInt()
////
////                    if (dX < 10 && dY < 10) { // Your on click logic here...
////                        val leftX = ((view.height - motionEvent.y) * 1.293) + (view.width*0.546*0.75)
////                        if(motionEvent.x <= leftX){
////                            goJoinHistory(holder.itemView.context)
////                        }else{
////                            val margin = view.width*0.06
////                            val rightY = leftX + margin
////                            if(motionEvent.x >= rightY){
////                                goWinHistory(holder.itemView.context)
////                            }
////                        }
////
////                        true
////                    }
////                }
////
////                true
////            }
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//            holder.binding.textLuckyDrawTitle.text = item.title
//            if (item.announceType == "live") {
//                holder.binding.textLuckyDrawLive.visibility = View.VISIBLE
//                holder.binding.textLuckyDrawLive.setOnClickListener {
//                    val popupBinding = PopupGuideBinding.inflate(LayoutInflater.from(holder.itemView.context))
//                    val popup = PopupWindow(popupBinding.root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT) //팝업 뷰 터치 되도록
//                    popup.isTouchable = true //팝업 뷰 포커스도 주고
//                    popup.isFocusable = true //팝업 뷰 이외에도 터치되게 (터치시 팝업 닫기 위한 코드)
//                    popup.isOutsideTouchable = true
//
//                    popupBinding.textPopupGuideTitle.setText(R.string.word_live_en)
//                    popupBinding.textPopupGuideDesc.setText(R.string.msg_lucky_draw_live_desc)
//
//                    popupBinding.imagePopupGuideClose.setOnClickListener {
//                        popup.dismiss()
//                    }
//                    popup.contentView = popupBinding.root
//                    popup.showAsDropDown(it)
//                }
//            } else {
//                holder.binding.textLuckyDrawLive.visibility = View.GONE
//            }
//
//            val adapter = MainLuckyDrawGiftAdapter()
//            adapter.launcher = launcher
//            adapter.listener = object : MainLuckyDrawGiftAdapter.OnItemClickListener{
//                override fun onItemClick(position: Int) {
//                    val data = adapter.getItem(position)
//                    if(data.seqNo == -1L){
//                        LogUtil.e("onItemClick", item.contents)
//                        if(StringUtils.isNotEmpty(item.contents)){
//                            if(holder.itemView.context.packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
//                                val intent = Intent(holder.itemView.context, LuckyDrawDetailActivity::class.java)
//                                intent.putExtra(Const.DATA, item)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                launcher?.launch(intent)
//                            }
//
//                        }
//                    }else{
//                        val intent = Intent(holder.itemView.context, LuckyDrawGiftActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        launcher?.launch(intent)
//                    }
//
//                }
//            }
//            holder.binding.recyclerLuckyDrawGift.adapter = adapter
//            holder.binding.recyclerLuckyDrawGift.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
//
//            val giftList = item.giftList as MutableList<LuckyDrawGift>
//            if(StringUtils.isNotEmpty(item.bannerImage)){
//                val bannerImage = LuckyDrawGift(-1L)
//                bannerImage.image = item.bannerImage
//                giftList.add(0, bannerImage)
//            }
//            adapter.setDataList(item.giftList as MutableList<LuckyDrawGift>)
//
//            holder.binding.layoutLuckyDrawJoin.setOnClickListener {
//
//                if(StringUtils.isNotEmpty(item.contents)){
//                    if(holder.itemView.context.packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
//                        val intent = Intent(holder.itemView.context, LuckyDrawDetailActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        launcher?.launch(intent)
//                        return@setOnClickListener
//                    }
//                }
//
//                when (item.status) {
//                    "active" -> {
//
//                        if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
//                            return@setOnClickListener
//                        }
//
//                        var checkWallet = false
//                        if (item.giftList != null) {
//                            for (gift in item.giftList!!) {
//                                if (gift.type == "buffCoin") {
//                                    checkWallet = true
//                                    break
//                                }
//                            }
//                        }
//                        if (checkWallet) {
//                            if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.authEmail)) {
//                                val params = HashMap<String, String>()
//                                params["email"] = LoginInfoManager.getInstance().member!!.authEmail!!
//                                (holder.itemView.context as BaseActivity).showProgress("")
//                                ApiBuilder.create().walletDuplicateUser(params).setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
//                                    override fun onResponse(call: Call<NewResultResponse<WalletRes>>?, response: NewResultResponse<WalletRes>?) {
//                                        (holder.itemView.context as BaseActivity).hideProgress()
//                                        if (response?.result != null) {
//                                            if (response.result!!.result == "SUCCESS") { //미가입
//                                                alertWalletMake(holder.itemView.context)
//                                            } else {
//                                                if(item.isPrivate != null && item.isPrivate!!){
//                                                    val intent = Intent(holder.itemView.context, LuckyDrawCheckPrivateActivity::class.java)
//                                                    intent.putExtra(Const.DATA, item)
//                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                    checkPrivateLauncher?.launch(intent)
//                                                    return
//                                                }
//                                                val intent = Intent(holder.itemView.context, LuckyDrawJoinActivity::class.java)
//                                                intent.putExtra(Const.DATA, item)
//                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                launcher?.launch(intent)
//                                            }
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
//                                        (holder.itemView.context as BaseActivity).hideProgress()
//                                    }
//                                }).build().call()
//                            } else {
//                                alertWalletMake(holder.itemView.context)
//                            }
//                        }else{
//
//                            if(item.isPrivate != null && item.isPrivate!!){
//                                val intent = Intent(holder.itemView.context, LuckyDrawCheckPrivateActivity::class.java)
//                                intent.putExtra(Const.DATA, item)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                checkPrivateLauncher?.launch(intent)
//                                return@setOnClickListener
//                            }
//
//                            val intent = Intent(holder.itemView.context, LuckyDrawJoinActivity::class.java)
//                            intent.putExtra(Const.DATA, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            launcher?.launch(intent)
//                        }
//                    }
//                    "expire", "pending" -> {
//                        if (StringUtils.isNotEmpty(item.liveUrl)) {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.liveUrl))
//                            holder.itemView.context.startActivity(intent)
//                        }
//                    }
//                    "complete" -> {
//                        val intent = Intent(holder.itemView.context, LuckyDrawWinActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        launcher?.launch(intent)
//                    }
//                }
//            }
//
//            if(item.engageType == "free"){
//                holder.binding.textLuckyDrawJoinPrice.setBackgroundResource(R.drawable.bg_ff5e5e_radius_13)
//                holder.binding.textLuckyDrawJoinPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                holder.binding.textLuckyDrawJoinPrice.setText(R.string.word_gratis)
//                holder.binding.textLuckyDrawJoinPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            }else{
//                holder.binding.textLuckyDrawJoinPrice.setBackgroundResource(R.drawable.bg_f7f7f7_radius_13)
//                holder.binding.textLuckyDrawJoinPrice.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff5e5e))
//                holder.binding.textLuckyDrawJoinPrice.text = item.engageBall.toString()
//                holder.binding.textLuckyDrawJoinPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wow_ball_s, 0, 0, 0)
//            }
//            holder.binding.textLuckyDrawJoinPrice.setPadding(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_11), 0, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_11), 0)
//
//            holder.binding.layoutLuckyDrawJoinRate.visibility = View.GONE
//            holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
//
//            when (item.status) {
//                "active" -> {
//                    holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
//                    holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
//                    holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_77f5ae))
//                    holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_recruit_ing)
//                    holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_343434_radius_13)
//
//                    holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                    holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_join)
//
//                    holder.binding.layoutLuckyDrawJoinRate.visibility = View.VISIBLE
//                    holder.binding.textLuckyDrawStatus.visibility = View.GONE
//
//                    val joinRate = ((item.joinCount!!.toFloat() / item.totalEngage!!.toFloat()) * 100f).toInt()
//                    holder.binding.textLuckyDrawJoinRate.text = "${FormatUtil.getMoneyType(item.joinCount.toString())} / ${FormatUtil.getMoneyType(item.totalEngage.toString())}"
//
//                    holder.binding.layoutLuckyDrawJoinRateTotal.weightSum = item.totalEngage!!.toFloat()
//                    (holder.binding.viewLuckyDrawJoinRate.layoutParams as LinearLayout.LayoutParams).weight = item.joinCount!!.toFloat()
//                    holder.binding.viewLuckyDrawJoinRate.requestLayout()
//                }
//
//                "expire" -> {
//                    holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_878787_radius_30)
//                    holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
//                    holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                    holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_recruit_complete)
//                    holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_717171_radius_13)
//
//                    holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                    holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_checking_win_announce_date)
//
//                }
//
//                "pending" -> {
//                    if (item.announceType == "live" && StringUtils.isNotEmpty(item.liveUrl)) {
//                        holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_ff5e5e_radius_30)
//                        holder.binding.textLuckyDrawStatus.visibility = View.GONE
//
//                        holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                        holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_view_live)
//                    } else {
//                        holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_ffffff_radius_30)
//
//                        holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
//                        holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
//                        holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_announce_wait)
//                        holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_343434_radius_13)
//
//                        holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_333333))
//                        val winAnnounceFormat = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format3))
//                        try{
//                            holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.format_lotto_win_announce_date, winAnnounceFormat.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime))))
//                        }catch (e: Exception){
//                            LogUtil.e("LuckyDrawAdapter", e.toString())
//                        }
//
//                    }
//                }
//
//                "complete" -> {
//                    holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_border_3px_ea5506_transparent_radius_33)
//                    holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
//                    holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_898989))
//                    holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_announce_complete)
//                    holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_cdcdcd_radius_13)
//
//                    holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ea5506))
//                    holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_view_winner)
//                }
//            }
//
//
//            //        holder.binding.textLuckyDrawStatus.setPadding(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_16))
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.absoluteAdapterPosition)
//            }
//        }
//    }
//
//    private fun alertWalletMake(context:Context){
//        val builder = AlertBuilder.Builder()
//        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//        builder.addContents(AlertData.MessageData(context.getString(R.string.msg_alert_make_wallet), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//        builder.setLeftText(context.getString(R.string.word_cancel)).setRightText(context.getString(R.string.msg_make_wallet))
//        builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//            override fun onCancel() {
//
//            }
//
//            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                when (event_alert) {
//                    AlertBuilder.EVENT_ALERT.RIGHT -> {
//                        val intent = Intent(context, WalletMakeActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        launcher?.launch(intent)
//                    }
//
//                    else -> {
//
//                    }
//                }
//            }
//        })
//        builder.builder().show(context)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            val binding = HeaderLuckyDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHeader(binding)
//        } else if (viewType == TYPE_ITEM) {
//            val binding = ItemLuckyDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHolder(binding)
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