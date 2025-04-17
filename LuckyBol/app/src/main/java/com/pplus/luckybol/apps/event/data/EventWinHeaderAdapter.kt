package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.event.ui.EventReplyActivity
import com.pplus.luckybol.apps.event.ui.EventWinFragment
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.HeaderEventWinBinding
import com.pplus.luckybol.databinding.ItemEventWinBinding
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class EventWinHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mDataList: MutableList<EventWin>? = null
    var listener: OnItemClickListener? = null
    var mTotalCount = 0
    var mEventWinFragment:EventWinFragment? = null

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

    class ViewHolder(binding: ItemEventWinBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageEventWin
        val text_user_name = binding.textEventWinUserName
        val text_gift_name = binding.textEventWinGiftName
        val text_impression = binding.textEventWinImpression
        val text_reply_count = binding.textEventWinReplyCount
        val layout_friend = binding.layoutEventWinFriend
        val text_friend = binding.textEventWinFriend

        init {
            text_user_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            Glide.with(holder.itemView.context).load(item.gift!!.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            holder.text_user_name.text = item.user!!.nickname
            holder.text_gift_name.text = item.gift!!.title
            holder.text_impression.text = item.impression

            holder.text_reply_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reply, item.replyCount.toString()))

            holder.text_reply_count.setOnClickListener {
                val intent = Intent(holder.itemView.context, EventReplyActivity::class.java)
                intent.putExtra(Const.EVENT_WIN, item)
                intent.putExtra(Const.POSITION, holder.absoluteAdapterPosition-1)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                mEventWinFragment?.eventReplyLauncher?.launch(intent)
            }

            if (item.user!!.friend) {
                holder.layout_friend.visibility = View.VISIBLE

                val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.user!!.mobile?.replace(Const.APP_TYPE+"##", ""))).list()
                var name: String? = null
                if (contacts != null && contacts.size > 0) {
                    name = contacts[0].memberName
                } else {
                    if (StringUtils.isNotEmpty(item.user!!.nickname)) {
                        name = item.user!!.nickname
                    } else if (StringUtils.isNotEmpty(item.user!!.name)) {
                        name = item.user!!.name
                    } else if (StringUtils.isNotEmpty(item.user!!.name)) {
                        name = item.user!!.name
                    } else {
                        name = holder.itemView.context.getString(R.string.word_unknown)
                    }
                }

                holder.text_friend.text = holder.itemView.context.getString(R.string.format_is_friend, name)
            } else {
                holder.layout_friend.visibility = View.GONE
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