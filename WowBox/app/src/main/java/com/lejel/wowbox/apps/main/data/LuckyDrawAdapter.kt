package com.lejel.wowbox.apps.main.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawCheckPrivateActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawDetailActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawJoinActivity
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawWinActivity
import com.lejel.wowbox.apps.wallet.ui.WalletMakeActivity
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyDrawBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawAdapter() : RecyclerView.Adapter<LuckyDrawAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDraw>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null
    var checkPrivateLauncher: ActivityResultLauncher<Intent>? = null


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
        } else {

        }

        Glide.with(holder.itemView.context).load(item.bannerImage).apply(RequestOptions()).into(holder.binding.imageLuckyDrawBanner)

        holder.binding.layoutLuckyDrawJoinRate.visibility = View.GONE

        when (item.status) {
            "active" -> {
                holder.binding.layoutLuckyDrawJoinRate.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(R.drawable.ic_lucky_draw_rate).into(holder.binding.imageLuckyDrawGraph)

                //                val joinRate = ((item.joinCount!!.toFloat() / item.totalEngage!!.toFloat()) * 100f).toInt()
                holder.binding.textLuckyDrawJoinRate.text = "${FormatUtil.getMoneyType(item.joinCount.toString())} / ${FormatUtil.getMoneyType(item.totalEngage.toString())}"

                holder.binding.layoutLuckyDrawJoinRateTotal.weightSum = item.totalEngage!!.toFloat()
                (holder.binding.viewLuckyDrawJoinRate.layoutParams as LinearLayout.LayoutParams).weight = item.joinCount!!.toFloat()
                holder.binding.viewLuckyDrawJoinRate.requestLayout()

                val rate = item.joinCount!!.toFloat() / item.totalEngage!!.toFloat()

                if (rate >= 0.06f) {
                    val margin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_30)
                    (holder.binding.imageLuckyDrawGraph.layoutParams as LinearLayout.LayoutParams).marginStart = -margin
                } else {
                    val margin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_992) * rate
                    (holder.binding.imageLuckyDrawGraph.layoutParams as LinearLayout.LayoutParams).marginStart = -margin.toInt()
                }
            }

            "expire" -> {
            }

            "pending" -> {
            }

            "complete" -> {
            }
        }


        //        holder.binding.textLuckyDrawStatus.setPadding(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_16))

        holder.itemView.setOnClickListener {

            if (StringUtils.isNotEmpty(item.contents)) {
                if (holder.itemView.context.packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)) {
                    val intent = Intent(holder.itemView.context, LuckyDrawDetailActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    launcher?.launch(intent)
                    return@setOnClickListener
                }
            }

            when (item.status) {
                "active" -> {

                    if (!PplusCommonUtil.loginCheck((holder.itemView.context as BaseActivity), launcher)) {
                        return@setOnClickListener
                    }

                    if (item.isPrivate != null && item.isPrivate!!) {
                        val intent = Intent(holder.itemView.context, LuckyDrawCheckPrivateActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        checkPrivateLauncher?.launch(intent)
                        return@setOnClickListener
                    }

                    val intent = Intent(holder.itemView.context, LuckyDrawJoinActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    launcher?.launch(intent)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}