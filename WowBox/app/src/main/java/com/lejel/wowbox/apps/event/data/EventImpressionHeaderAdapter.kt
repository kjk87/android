package com.lejel.wowbox.apps.event.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.event.ui.EventImpressionActivity
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventGift
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.HeaderEventImpressionBinding
import com.lejel.wowbox.databinding.ItemEventGiftBinding
import com.lejel.wowbox.databinding.ItemEventImpressionBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class EventImpressionHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mEvent: Event? = null
    var mContext: Context? = null
    var mDataList: MutableList<EventWin>? = null
    var mMyWinList: MutableList<EventWin>? = null
    var mGiftList: MutableList<EventGift>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventWin {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<EventWin>? {

        return mDataList
    }

    fun add(data: EventWin) {

        if (mDataList == null) {
            mDataList = ArrayList<EventWin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventWin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventWin) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventWin>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(val binding: HeaderEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    class ViewHolder(val binding: ItemEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
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
            val item = mEvent!!

            if (item.primaryType == "number" && (item.winnerCount!! == 0)) {
                holder.binding.layoutEventImpressionNotExistWinner.visibility = View.VISIBLE
            } else {
                holder.binding.layoutEventImpressionNotExistWinner.visibility = View.GONE
            }

            if (item.primaryType == "number") {
                holder.binding.textEventImpressionWinCode.visibility = View.VISIBLE
                holder.binding.textEventImpressionWinCode.text = mContext?.getString(R.string.format_event_win_code, item.winCode)
            } else {
                holder.binding.textEventImpressionWinCode.visibility = View.GONE
            }

            holder.binding.textEventImpressionEventTitle.text = item.title

            if (item.winAnnounceType.equals("immediately")) {
                holder.binding.textEventImpressionJoinCount.text = mContext?.getString(R.string.format_real_time_event_winner_count, item.winnerCount)
            } else {
                holder.binding.textEventImpressionJoinCount.text = mContext?.getString(R.string.format_event_winner_count, item.winnerCount)
            }

            if (mGiftList != null && mGiftList!!.isNotEmpty()) {
                holder.binding.layoutEventImpressionGiftList.removeAllViews()
                for (i in 0 until mGiftList!!.size) {
                    val gift = mGiftList!![i]
                    val giftBinding = ItemEventGiftBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                    holder.binding.layoutEventImpressionGiftList.addView(giftBinding.root)
                    Glide.with(holder.itemView.context).load(gift.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(giftBinding.imageEventGift)
                    giftBinding.textEventGiftName.text = gift.title
                    giftBinding.textEventGiftRemainCount.visibility = View.GONE
                    giftBinding.textEventGiftDetailView.visibility = View.GONE


                    if (i < (mGiftList!!.size - 1)) {
                        (giftBinding.root.layoutParams as LinearLayout.LayoutParams).bottomMargin = mContext?.resources!!.getDimensionPixelSize(R.dimen.height_20)
                    }
                }
            }

//            holder.layout_invite.setOnClickListener {
//                var launcher: ActivityResultLauncher<Intent>? = null
//                if (holder.itemView.context is EventImpressionActivity) {
//                    launcher = (holder.itemView.context as EventImpressionActivity).signInLauncher
//                }
//                if (!PplusCommonUtil.loginCheck((holder.itemView.context as FragmentActivity), launcher)) {
//                    return@setOnClickListener
//                }
//                PplusCommonUtil.share(holder.itemView.context)
//            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)
            Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageEventImpression)

            holder.binding.textEventImpressionName.text = item.memberTotal!!.nickname


            if (mEvent!!.primaryType == "number") {
                if (item.amount != null && item.amount!! > 0) {
                    if (item.eventGift!!.giftType == "ball") {
                        holder.binding.textEventImpressionGiftName.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.format_ball_unit, FormatUtil.getMoneyTypeFloat(item.amount.toString())))
                    } else if (item.eventGift!!.giftType == "point") {
                        holder.binding.textEventImpressionGiftName.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.format_point_unit, FormatUtil.getMoneyTypeFloat(item.amount.toString())))
                    } else {
                        holder.binding.textEventImpressionGiftName.text = item.giftTitle
                    }
                } else {
                    holder.binding.textEventImpressionGiftName.text = item.giftTitle
                }
            } else {
                holder.binding.textEventImpressionGiftName.text = item.giftTitle
            }

            if (item.eventGift!!.giftType == "ball" || item.eventGift!!.giftType == "point" || item.eventGift!!.giftType == "lotto") {
                holder.binding.textEventImpression.visibility = View.GONE
            } else {
                if (StringUtils.isNotEmpty(item.impression)) {
                    holder.binding.textEventImpression.visibility = View.VISIBLE
                    holder.binding.textEventImpression.text = item.impression
                } else {
                    holder.binding.textEventImpression.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                listener?.onItemClick(position - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderEventImpressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemEventImpressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}