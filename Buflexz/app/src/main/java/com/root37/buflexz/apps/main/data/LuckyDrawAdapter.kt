package com.root37.buflexz.apps.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawGiftActivity
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawJoinActivity
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawWinActivity
import com.root37.buflexz.apps.wallet.ui.WalletMakeActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.dto.LuckyDrawGift
import com.root37.buflexz.core.network.model.dto.WalletRes
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemLuckyDrawBinding
import com.root37.buflexz.databinding.PopupGuideBinding
import retrofit2.Call
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawAdapter() : RecyclerView.Adapter<LuckyDrawAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDraw>? = null
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

    fun getItem(position: Int): LuckyDraw {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDraw>? {

        return mDataList
    }

    fun add(data: LuckyDraw) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDraw>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDraw>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDraw>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDraw) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDraw>()
    }

    fun setDataList(dataList: MutableList<LuckyDraw>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textLuckyDrawTitle.text = item.title
        if (item.announceType == "live") {
            holder.binding.textLuckyDrawLive.visibility = View.VISIBLE
            holder.binding.textLuckyDrawLive.setOnClickListener {
                val popupBinding = PopupGuideBinding.inflate(LayoutInflater.from(holder.itemView.context))
                val popup = PopupWindow(popupBinding.root, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT) //팝업 뷰 터치 되도록
                popup.isTouchable = true //팝업 뷰 포커스도 주고
                popup.isFocusable = true //팝업 뷰 이외에도 터치되게 (터치시 팝업 닫기 위한 코드)
                popup.isOutsideTouchable = true

                popupBinding.textPopupGuideTitle.setText(R.string.word_live_en)
                popupBinding.textPopupGuideDesc.setText(R.string.msg_lucky_draw_live_desc)

                popupBinding.imagePopupGuideClose.setOnClickListener {
                    popup.dismiss()
                }
                popup.contentView = popupBinding.root
                popup.showAsDropDown(it)
            }
        } else {
            holder.binding.textLuckyDrawLive.visibility = View.GONE
        }

        val adapter = MainLuckyDrawGiftAdapter()
        adapter.launcher = launcher
        adapter.listener = object : MainLuckyDrawGiftAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(holder.itemView.context, LuckyDrawGiftActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                launcher?.launch(intent)
            }
        }
        holder.binding.recyclerLuckyDrawGift.adapter = adapter
        holder.binding.recyclerLuckyDrawGift.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setDataList(item.giftList as MutableList<LuckyDrawGift>)

        val joinRate = ((item.joinCount!!.toFloat() / item.totalEngage!!.toFloat()) * 100f).toInt()
        holder.binding.textLuckyDrawJoinRate.text = holder.itemView.context.getString(R.string.format_join_rate, joinRate.toString())
        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        holder.binding.textLuckyDrawEndDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.endDatetime)))
        holder.binding.textLuckyDrawEndGiftCount.text = item.giftList!!.size.toString()
        holder.binding.layoutLuckyDrawGiftCount.setOnClickListener {
            val intent = Intent(holder.itemView.context, LuckyDrawGiftActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher?.launch(intent)
        }

        holder.binding.layoutLuckyDrawJoin.setOnClickListener {
            when (item.status) {
                "active" -> {

                    if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                        return@setOnClickListener
                    }

                    var checkWallet = false
                    if (item.giftList != null) {
                        for (gift in item.giftList!!) {
                            if (gift.type == "buffCoin") {
                                checkWallet = true
                                break
                            }
                        }
                    }
                    if (checkWallet) {
                        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.authEmail)) {
                            val params = HashMap<String, String>()
                            params["email"] = LoginInfoManager.getInstance().member!!.authEmail!!
                            (holder.itemView.context as BaseActivity).showProgress("")
                            ApiBuilder.create().walletDuplicateUser(params).setCallback(object : PplusCallback<NewResultResponse<WalletRes>> {
                                override fun onResponse(call: Call<NewResultResponse<WalletRes>>?, response: NewResultResponse<WalletRes>?) {
                                    (holder.itemView.context as BaseActivity).hideProgress()
                                    if (response?.result != null) {
                                        if (response.result!!.result == "SUCCESS") { //미가입
                                            alertWalletMake(holder.itemView.context)
                                        } else {
                                            val intent = Intent(holder.itemView.context, LuckyDrawJoinActivity::class.java)
                                            intent.putExtra(Const.DATA, item)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            launcher?.launch(intent)
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
                                    (holder.itemView.context as BaseActivity).hideProgress()
                                }
                            }).build().call()
                        } else {
                            alertWalletMake(holder.itemView.context)
                        }
                    }else{
                        val intent = Intent(holder.itemView.context, LuckyDrawJoinActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        launcher?.launch(intent)
                    }


                }

                "expire", "pending" -> {
                    if (StringUtils.isNotEmpty(item.liveUrl)) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.liveUrl))
                        holder.itemView.context.startActivity(intent)
                    }
                }

                "complete" -> {
                    val intent = Intent(holder.itemView.context, LuckyDrawWinActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    launcher?.launch(intent)
                }
            }
        }

        when (item.status) {
            "active" -> {
                holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_48b778_radius_30)
                holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
                holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_77f5ae))
                holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_recruit_ing)
                holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_343434_radius_30)

                holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_join)
            }

            "expire" -> {
                holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_878787_radius_30)
                holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
                holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_recruit_complete)
                holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_717171_radius_30)

                holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_checking_win_announce_date)
            }

            "pending" -> {
                if (item.announceType == "live" && StringUtils.isNotEmpty(item.liveUrl)) {
                    holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_ff5e5e_radius_30)
                    holder.binding.textLuckyDrawStatus.visibility = View.GONE

                    holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                    holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_view_live)
                } else {
                    holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_ffffff_radius_30)

                    holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
                    holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
                    holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_announce_wait)
                    holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_343434_radius_30)

                    holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_373737))
                    val winAnnounceFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
                    holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.format_lotto_win_announce_date, winAnnounceFormat.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime))))
                    if (StringUtils.isNotEmpty(item.winAnnounceDatetime)) {
                        holder.binding.textLuckyDrawEndDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime)))
                    } else {
                        holder.binding.textLuckyDrawEndDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.endDatetime)))
                    }
                }
            }

            "complete" -> {
                holder.binding.layoutLuckyDrawJoin.setBackgroundResource(R.drawable.bg_border_3px_ff5e5e_232323_radius_30)
                holder.binding.textLuckyDrawStatus.visibility = View.VISIBLE
                holder.binding.textLuckyDrawStatus.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff5e5e))
                holder.binding.textLuckyDrawStatus.text = holder.itemView.context.getString(R.string.word_announce_complete)
                holder.binding.textLuckyDrawStatus.setBackgroundResource(R.drawable.bg_373737_radius_30)

                holder.binding.textLuckyDrawEndDate.text = format.format(PplusCommonUtil.setTimeZoneOffset(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDatetime)))

                holder.binding.textLuckyDrawStatusDesc.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff5e5e))
                holder.binding.textLuckyDrawStatusDesc.text = holder.itemView.context.getString(R.string.msg_view_winner)
            }
        }


        //        holder.binding.textLuckyDrawStatus.setPadding(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_16))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    private fun alertWalletMake(context:Context){
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
        val binding = ItemLuckyDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}