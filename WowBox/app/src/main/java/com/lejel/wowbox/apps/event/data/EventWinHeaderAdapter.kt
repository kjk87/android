package com.lejel.wowbox.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.ui.EventReplyActivity
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.HeaderEventWinBinding
import com.lejel.wowbox.databinding.ItemEventWinBinding
import com.pplus.utils.part.format.FormatUtil


/**
 * Created by imac on 2018. 1. 8..
 */
class EventWinHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mDataList: MutableList<EventWin>? = null
    var listener: OnItemClickListener? = null
    var mTotalCount = 0
    var launcher: ActivityResultLauncher<Intent>? = null

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

    fun getItem(position: Int): EventWin {

        return mDataList!![position]
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

    class ViewHeader(binding: HeaderEventWinBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    class ViewHolder(val binding: ItemEventWinBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textEventWinUserName.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            Glide.with(holder.itemView.context).load(item.eventGift!!.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.binding.imageEventWin)
            holder.binding.textEventWinUserName.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_congratulation, item.memberTotal!!.nickname))
            holder.binding.textEventWinGiftName.text = item.eventGift!!.title
            holder.binding.textEventWinGiftPrice.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.eventGift!!.price.toString()))
            if(item.eventGift!!.giftType == "ball" || item.eventGift!!.giftType == "point"){
                holder.binding.textEventWinImpression.visibility = View.GONE
            }else{
                holder.binding.textEventWinImpression.visibility = View.VISIBLE
                holder.binding.textEventWinImpression.text = item.impression
            }


            holder.binding.textEventWinReplyCount.text = holder.itemView.context.getString(R.string.format_reply_count, item.replyCount.toString())

            holder.binding.textEventWinReplyCount.setOnClickListener {
                val intent = Intent(holder.itemView.context, EventReplyActivity::class.java)
                intent.putExtra(Const.EVENT_WIN, item)
                intent.putExtra(Const.POSITION, holder.absoluteAdapterPosition-1)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                launcher?.launch(intent)
                (holder.itemView.context as BaseActivity).overridePendingTransition(R.anim.view_up, R.anim.fix)

            }

            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.absoluteAdapterPosition - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderEventWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemEventWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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