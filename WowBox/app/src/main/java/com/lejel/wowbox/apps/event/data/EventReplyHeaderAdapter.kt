package com.lejel.wowbox.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.core.network.model.dto.EventReply
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.HeaderEventReplyBinding
import com.lejel.wowbox.databinding.ItemEventReplyBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import kotlin.random.Random


/**
 * Created by imac on 2018. 1. 8..
 */
class EventReplyHeaderAdapter(eventWin: EventWin) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mDataList: MutableList<EventReply>? = null
    var mEventWin: EventWin
    var listener: OnItemClickListener? = null
    var mTotalCount = 0
    var launcher: ActivityResultLauncher<Intent>? = null

    interface OnItemClickListener {

        fun onHeaderClick()

        fun onItemClick(position: Int)
    }

    init {
        this.mEventWin = eventWin
        this.mDataList = ArrayList()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventReply {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventReply>? {

        return mDataList
    }

    fun add(data: EventReply) {

        if (mDataList == null) {
            mDataList = ArrayList<EventReply>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventReply>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventReply>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventReply) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventReply>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventReply>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(val binding: HeaderEventReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

    class ViewHolder(val binding: ItemEventReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            val number = Random.nextInt(5)
            Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${mEventWin.userKey}").apply(RequestOptions().fitCenter().placeholder(PplusCommonUtil.getDefaultProfile(number)).error(PplusCommonUtil.getDefaultProfile(number))).into(holder.binding.imageHeaderEventReplyProfile)
            holder.binding.textHeaderEventReplyName.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_congratulation, mEventWin.memberTotal!!.nickname))
            holder.binding.textHeaderEventReplyImpression.text = mEventWin.impression
            holder.binding.textHeaderEventReplyRegDate.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, mEventWin.winDatetime)

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            holder.binding.textEventReplyName.text = item.memberTotal?.nickname
            Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageEventReplyProfile)

            holder.binding.textEventReply.text = item.reply
            holder.binding.textEventReplyRegDate.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, item.regDatetime)

            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.absoluteAdapterPosition - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderEventReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemEventReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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