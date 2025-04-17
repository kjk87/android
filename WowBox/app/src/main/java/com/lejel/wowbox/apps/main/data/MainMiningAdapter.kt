package com.lejel.wowbox.apps.main.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.attendance.ui.AttendanceActivity
import com.lejel.wowbox.apps.buff.ui.AlertAdMiningCompleteActivity
import com.lejel.wowbox.apps.buff.ui.BuffAirDropActivity
import com.lejel.wowbox.apps.buff.ui.BuffGuerrillaMiningActivity
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.invite.ui.InviteActivity
import com.lejel.wowbox.apps.invite.ui.InviteMiningListActivity
import com.lejel.wowbox.apps.wallet.ui.WalletMakeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ItemMainMiningBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class MainMiningAdapter() : RecyclerView.Adapter<MainMiningAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }


    fun clear() {
        notifyItemRangeRemoved(0, itemCount)
    }

    class ViewHolder(val binding: ItemMainMiningBinding) : RecyclerView.ViewHolder(binding.root) {

        var countTimer: CountDownTimer? = null

        init {

        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.countTimer?.cancel()
        holder.binding.layoutMainMining1.visibility = View.GONE
        holder.binding.layoutMainMining2.visibility = View.GONE
        holder.binding.layoutMainMining3.visibility = View.GONE
        holder.binding.layoutMainMining4.visibility = View.GONE
        holder.binding.layoutMainMining5.visibility = View.GONE
        holder.binding.textMainMining.visibility = View.VISIBLE
        holder.binding.textMainMining.setText(R.string.msg_mining)
        when (position) {
            0 -> {
                holder.binding.layoutMainMiningBg.setBackgroundResource(R.drawable.bg_mining1)
                holder.binding.imageMainMining.visibility = View.VISIBLE
                holder.binding.imageMainMining.setImageResource(R.drawable.ic_mining1)
                holder.binding.layoutMainMining1.visibility = View.VISIBLE
                holder.binding.textMainMining.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_48b778))
                holder.binding.textMainMining.setText(R.string.msg_airdrop)
                holder.binding.textMainMining.setOnClickListener {
                    if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                        return@setOnClickListener
                    }

                    val intent = Intent(holder.itemView.context, BuffAirDropActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }
            }

            1 -> {
                holder.binding.layoutMainMiningBg.setBackgroundResource(R.drawable.bg_mining2)
                holder.binding.imageMainMining.visibility = View.VISIBLE
                holder.binding.imageMainMining.setImageResource(R.drawable.ic_mining2)
                holder.binding.layoutMainMining2.visibility = View.VISIBLE
                holder.binding.textMainMining.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_38408d))
                holder.binding.textMainMining.setOnClickListener {
                    if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                        return@setOnClickListener
                    }

                    val intent = Intent(holder.itemView.context, AttendanceActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }
            }

            2 -> {
                holder.binding.layoutMainMiningBg.setBackgroundResource(R.drawable.bg_mining3)
                holder.binding.imageMainMining.visibility = View.VISIBLE
                holder.binding.imageMainMining.setImageResource(R.drawable.ic_mining3)
                holder.binding.layoutMainMining3.visibility = View.VISIBLE
                holder.binding.textMainMining.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_f28f66))
                if (LoginInfoManager.getInstance().isMember()) {
                    holder.binding.layoutMainMining3RecommendKey.visibility = View.VISIBLE
                    holder.binding.textMainMining3RecommendKey.text = LoginInfoManager.getInstance().member!!.userKey
                    holder.binding.imageMainMining3RecommendKeyCopy.setOnClickListener {
                        val clipboard = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(holder.itemView.context.getString(R.string.word_recommend_code), LoginInfoManager.getInstance().member!!.userKey)
                        clipboard.setPrimaryClip(clip)
                        ToastUtil.show(holder.itemView.context, R.string.msg_copied_clipboard)
                    }

                    holder.binding.layoutMainMining3InviteMining.setOnClickListener {
                        if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                            return@setOnClickListener
                        }

                        val intent = Intent(holder.itemView.context, InviteMiningListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        holder.itemView.context.startActivity(intent)
                    }

                    ApiBuilder.create().getBuffInviteMiningCount().setCallback(object : PplusCallback<NewResultResponse<Int>> {
                        override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                            if (response?.result != null) {
                                holder.binding.textMainMining3InviteMiningCount.text = response.result.toString()
                            } else {
                                holder.binding.textMainMining3InviteMiningCount.text = "0"
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

                        }
                    }).build().call()
                } else {
                    holder.binding.layoutMainMining3RecommendKey.visibility = View.GONE
                }

                holder.binding.textMainMining.setOnClickListener {
                    if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                        return@setOnClickListener
                    }

                    val intent = Intent(holder.itemView.context, InviteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }

            }

            3 -> {
                holder.binding.layoutMainMiningBg.setBackgroundResource(R.drawable.bg_mining4)
                holder.binding.imageMainMining.visibility = View.VISIBLE
                holder.binding.imageMainMining.setImageResource(R.drawable.ic_mining4)
                holder.binding.layoutMainMining4.visibility = View.VISIBLE
                holder.binding.textMainMining.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_5c6fff))

                holder.binding.layoutMainMining4Telegram.setOnClickListener {
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BuffCoin_Official_chat")))
                }

                holder.binding.layoutMainMining4Discord.setOnClickListener {
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/channels/1194087825486909460/1194543366025781378")))
                }

                holder.binding.textMainMining.setOnClickListener {
                    val intent = Intent(holder.itemView.context, BuffGuerrillaMiningActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    holder.itemView.context.startActivity(intent)
                }
            }

            4 -> {
                holder.binding.layoutMainMiningBg.setBackgroundResource(R.drawable.bg_mining5)
                holder.binding.imageMainMining.visibility = View.GONE
                holder.binding.layoutMainMining5.visibility = View.VISIBLE
                holder.binding.textMainMining.visibility = View.GONE

                holder.binding.imageMainMining5Play1.isClickable = false
                holder.binding.imageMainMining5Play2.isClickable = false
                holder.binding.imageMainMining5Play3.isClickable = false

                holder.binding.textMainMining5Ticket.setText(R.string.word_question)
                holder.binding.textMainMining5Point.setText(R.string.word_question)
                holder.binding.textMainMining5Buff.setText(R.string.word_question)

                holder.binding.textMainMining5RemainTimeTitle.visibility = View.GONE
                holder.binding.textMainMining5RemainTime.visibility = View.GONE

                if (LoginInfoManager.getInstance().isMember()) {
                    val member = LoginInfoManager.getInstance().member!!

                    if (member.rewardTicket == null && member.rewardPoint == null && member.rewardBuff == null && StringUtils.isEmpty(member.rewardDate)) {
                        holder.binding.imageMainMining5Play1.visibility = View.VISIBLE
                        holder.binding.textMainMining5Step1.visibility = View.GONE
                        holder.binding.imageMainMining5Play1.setImageResource(R.drawable.ic_ad_mining_play)
                        holder.binding.imageMainMining5Play1.isClickable = true
                        holder.binding.imageMainMining5Play1.setOnClickListener {
                            callAd(holder.itemView.context, 1)
                        }

                        holder.binding.imageMainMining5Play2.visibility = View.GONE
                        holder.binding.textMainMining5Step2.visibility = View.VISIBLE

                        holder.binding.imageMainMining5Play3.visibility = View.GONE
                        holder.binding.textMainMining5Step3.visibility = View.VISIBLE
                    } else {

                        val joinAbleTermMillis: Long
                        val termsMillis: Long

                        if (member.rewardTicket != null && member.rewardPoint != null && member.rewardBuff != null && StringUtils.isNotEmpty(member.rewardDate)) {
                            val lastJoinTime = PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(member.rewardDate)).time
                            joinAbleTermMillis = 3 * 60 * 60 * 1000
                            termsMillis = System.currentTimeMillis() - lastJoinTime

                            if (termsMillis > joinAbleTermMillis) {

                                holder.binding.imageMainMining5Play1.visibility = View.VISIBLE
                                holder.binding.textMainMining5Step1.visibility = View.GONE
                                holder.binding.imageMainMining5Play1.setImageResource(R.drawable.ic_ad_mining_play)
                                holder.binding.imageMainMining5Play1.isClickable = true
                                holder.binding.imageMainMining5Play1.setOnClickListener {

                                    val termsMillis = System.currentTimeMillis() - lastJoinTime
                                    if (termsMillis > joinAbleTermMillis) {
                                        callAd(holder.itemView.context, 1)
                                    }
                                }

                                holder.binding.imageMainMining5Play2.visibility = View.GONE
                                holder.binding.textMainMining5Step2.visibility = View.VISIBLE

                                holder.binding.imageMainMining5Play3.visibility = View.GONE
                                holder.binding.textMainMining5Step3.visibility = View.VISIBLE
                            } else {

                                holder.binding.textMainMining5Ticket.text = FormatUtil.getMoneyType(member.rewardTicket.toString())
                                holder.binding.textMainMining5Point.text = FormatUtil.getMoneyType(member.rewardPoint.toString())
                                holder.binding.textMainMining5Buff.text = FormatUtil.getMoneyTypeFloat(member.rewardBuff.toString())

                                holder.binding.imageMainMining5Play1.visibility = View.VISIBLE
                                holder.binding.textMainMining5Step1.visibility = View.GONE
                                holder.binding.imageMainMining5Play1.setImageResource(R.drawable.ic_ad_mining_check)

                                holder.binding.imageMainMining5Play2.visibility = View.VISIBLE
                                holder.binding.textMainMining5Step2.visibility = View.GONE
                                holder.binding.imageMainMining5Play2.setImageResource(R.drawable.ic_ad_mining_check)

                                holder.binding.imageMainMining5Play3.visibility = View.VISIBLE
                                holder.binding.textMainMining5Step3.visibility = View.GONE
                                holder.binding.imageMainMining5Play3.setImageResource(R.drawable.ic_ad_mining_check)
                            }
                        } else {

                            holder.binding.imageMainMining5Play1.visibility = View.VISIBLE
                            holder.binding.textMainMining5Step1.visibility = View.GONE
                            holder.binding.imageMainMining5Play1.setImageResource(R.drawable.ic_ad_mining_check)

                            val lastJoinTime = PreferenceUtil.getDefaultPreference(holder.itemView.context).get(Const.AD_MINING_TIME, 0L)
                            joinAbleTermMillis = 10 * 60 * 1000
                            termsMillis = System.currentTimeMillis() - lastJoinTime

                            if (member.rewardTicket != null && member.rewardPoint == null) {

                                holder.binding.textMainMining5Ticket.text = FormatUtil.getMoneyType(member.rewardTicket.toString())
                                holder.binding.textMainMining5Point.setText(R.string.word_question)
                                holder.binding.textMainMining5Buff.setText(R.string.word_question)

                                if (termsMillis > joinAbleTermMillis) {
                                    holder.binding.imageMainMining5Play2.visibility = View.VISIBLE
                                    holder.binding.textMainMining5Step2.visibility = View.GONE
                                    holder.binding.imageMainMining5Play2.setImageResource(R.drawable.ic_ad_mining_play)
                                    holder.binding.imageMainMining5Play2.isClickable = true
                                    holder.binding.imageMainMining5Play2.setOnClickListener {
                                        val termsMillis = System.currentTimeMillis() - lastJoinTime
                                        if (termsMillis > joinAbleTermMillis) {
                                            callAd(holder.itemView.context, 2)
                                        }
                                    }
                                } else {
                                    holder.binding.imageMainMining5Play2.visibility = View.GONE
                                    holder.binding.textMainMining5Step2.visibility = View.VISIBLE
                                }

                                holder.binding.imageMainMining5Play3.visibility = View.GONE
                                holder.binding.textMainMining5Step3.visibility = View.VISIBLE

                            } else if (member.rewardTicket != null && member.rewardPoint != null && member.rewardBuff == null) {

                                holder.binding.textMainMining5Ticket.setText(R.string.word_question)
                                holder.binding.textMainMining5Point.text = FormatUtil.getMoneyType(member.rewardPoint.toString())
                                holder.binding.textMainMining5Buff.setText(R.string.word_question)

                                holder.binding.imageMainMining5Play2.visibility = View.VISIBLE
                                holder.binding.textMainMining5Step2.visibility = View.GONE
                                holder.binding.imageMainMining5Play2.setImageResource(R.drawable.ic_ad_mining_check)

                                if (termsMillis > joinAbleTermMillis) {
                                    holder.binding.imageMainMining5Play3.visibility = View.VISIBLE
                                    holder.binding.textMainMining5Step3.visibility = View.GONE
                                    holder.binding.imageMainMining5Play3.setImageResource(R.drawable.ic_ad_mining_play)
                                    holder.binding.imageMainMining5Play3.isClickable = true
                                    holder.binding.imageMainMining5Play3.setOnClickListener {
                                        if (member.isAuthEmail != null && member.isAuthEmail!! && StringUtils.isNotEmpty(member.authEmail)) {
                                            val termsMillis = System.currentTimeMillis() - lastJoinTime
                                            if (termsMillis > joinAbleTermMillis) {
                                                callAd(holder.itemView.context, 3)
                                            }
                                        } else {
                                            alertWalletMake(holder.itemView.context)
                                        }

                                    }
                                } else {
                                    holder.binding.imageMainMining5Play3.visibility = View.GONE
                                    holder.binding.textMainMining5Step3.visibility = View.VISIBLE
                                }
                            }
                        }

                        if (joinAbleTermMillis > termsMillis) {
                            holder.binding.textMainMining5RemainTimeTitle.visibility = View.VISIBLE
                            holder.binding.textMainMining5RemainTime.visibility = View.VISIBLE
                            holder.countTimer = object : CountDownTimer(joinAbleTermMillis - termsMillis, 1000) {

                                override fun onTick(millisUntilFinished: Long) {

                                    val hours = (millisUntilFinished / (1000 * 60 * 60) % 24).toInt()
                                    val minutes = (millisUntilFinished / (1000 * 60) % 60).toInt()
                                    val seconds = (millisUntilFinished / 1000).toInt() % 60

                                    val strH = DateFormatUtils.formatTime(hours)
                                    val strM = DateFormatUtils.formatTime(minutes)
                                    val strS = DateFormatUtils.formatTime(seconds)

                                    if (hours > 0) {
                                        holder.binding.textMainMining5RemainTime.text = "$strH:$strM:$strS"
                                    } else {
                                        if (minutes > 0) {
                                            holder.binding.textMainMining5RemainTime.text = "$strM:$strS"
                                        } else {
                                            holder.binding.textMainMining5RemainTime.text = strS
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
                        }
                    }
                } else {
                    holder.binding.imageMainMining5Play1.visibility = View.VISIBLE
                    holder.binding.textMainMining5Step1.visibility = View.GONE
                    holder.binding.imageMainMining5Play1.setImageResource(R.drawable.ic_ad_mining_play)
                    holder.binding.imageMainMining5Play1.isClickable = true
                    holder.binding.imageMainMining5Play1.setOnClickListener {
                        PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)
                    }

                    holder.binding.imageMainMining5Play2.visibility = View.GONE
                    holder.binding.textMainMining5Step2.visibility = View.VISIBLE

                    holder.binding.imageMainMining5Play3.visibility = View.GONE
                    holder.binding.textMainMining5Step3.visibility = View.VISIBLE
                }


            }
        } //        holder.itemView.setOnClickListener {
        //            listener?.onItemClick(holder.absoluteAdapterPosition)
        //        }
    }

    var mIsRewarded = false
    var mRewardedAmount = 0F
    private fun callAd(context: Context, depth: Int) {

        if (!AdmobUtil.getInstance(context).mIsRewardedLoaded) {
            ToastUtil.showAlert(context, R.string.msg_loading_ad)
            return
        }

        AdmobUtil.getInstance(context).mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                if (mIsRewarded) {
                    PreferenceUtil.getDefaultPreference(context).put(Const.AD_MINING_TIME, System.currentTimeMillis())
                    mIsRewarded = false
                    val intent = Intent(context, AlertAdMiningCompleteActivity::class.java)
                    if (depth == 3) {
                        intent.putExtra(Const.AMOUNT, mRewardedAmount.toString())
                    } else {
                        intent.putExtra(Const.AMOUNT, mRewardedAmount.toInt().toString())
                    }
                    var type = ""
                    when (depth) {
                        1 -> {
                            type = "lottery"
                        }

                        2 -> {
                            type = "point"
                        }

                        3 -> {
                            type = "buff"
                        }
                    }
                    intent.putExtra(Const.TYPE, type)
                    intent.putExtra(Const.DEPTH, depth)
                    context.startActivity(intent)
                    PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

                        override fun reload() {
                            notifyItemChanged(4)
                        }
                    })
                }
            }
        }

        AdmobUtil.getInstance(context).mRewardedAd?.let { ad ->
            ad.show(context as BaseActivity, OnUserEarnedRewardListener { rewardItem -> // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type

                AdmobUtil.getInstance(context).mIsRewardedLoaded = false
                AdmobUtil.getInstance(context).initRewardAd()

                val params = HashMap<String, String>()
                params["depth"] = depth.toString()
                context.showProgress("")
                ApiBuilder.create().reward(params).setCallback(object : PplusCallback<NewResultResponse<Float>> {
                    override fun onResponse(call: Call<NewResultResponse<Float>>?, response: NewResultResponse<Float>?) {
                        context.hideProgress()
                        if (response?.result != null) {
                            mIsRewarded = true
                            mRewardedAmount = response.result!!

                        } else {
                            mIsRewarded = false
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Float>>?, t: Throwable?, response: NewResultResponse<Float>?) {
                        context.hideProgress()
                        mIsRewarded = false
                        if (response?.code == 666) {

                        }
                    }
                }).build().call()

            })
        } ?: run {
            ToastUtil.showAlert(context, R.string.msg_loading_ad)
            AdmobUtil.getInstance(context).mIsRewardedLoaded = false
            AdmobUtil.getInstance(context).initRewardAd()
        }
    }

    private fun alertWalletMake(context: Context) {
        val builder = AlertBuilder.Builder()
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
        builder.addContents(AlertData.MessageData(context.getString(R.string.msg_alert_make_wallet), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(context.getString(R.string.word_cancel)).setRightText(context.getString(R.string.msg_make_wallet))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val intent = Intent(context, WalletMakeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher?.launch(intent)
                    }

                    else -> {

                    }
                }
            }
        })
        builder.builder().show(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainMiningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}