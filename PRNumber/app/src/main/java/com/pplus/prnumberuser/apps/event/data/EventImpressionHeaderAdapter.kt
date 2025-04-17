package com.pplus.prnumberuser.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.apps.event.ui.EventWinDetailActivity
import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.database.DBManager
import com.pplus.prnumberuser.core.database.entity.ContactDao
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.HeaderEventImpressionBinding
import com.pplus.prnumberuser.databinding.ItemEventGiftBinding
import com.pplus.prnumberuser.databinding.ItemEventImpressionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class EventImpressionHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mEvent: Event? = null
    var mDataList: MutableList<EventWin>? = null
    var mMyWinList: MutableList<EventWin>? = null
    var mGiftList: MutableList<EventGift>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
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

    class ViewHeader(binding: HeaderEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_title = binding.textEventImpressionEventTitle
        val text_win_code = binding.textEventImpressionWinCode
        val text_count = binding.textEventImpressionJoinCount
        val layout_list = binding.layoutEventImpressionGiftList
        val layout_invite = binding.layoutEventImpressionInvite
        val layout_not_exist = binding.layoutEventImpressionNotExistWinner

        init {

        }
    }

    class ViewHolder(binding: ItemEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageEventImpression
        val text_name = binding.textEventImpressionGiftName
        val text_gift_name = binding.textEventImpressionGiftName
        val text_impression = binding.textEventImpression
        val layout_friend = binding.layoutEventImpressionFriend
        val text_friend = binding.textEventImpressionFriend

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

            if (item.primaryType == EventType.PrimaryType.number.name && (item.winnerCount!! == 0)) {
                holder.layout_not_exist.visibility = View.VISIBLE
            } else {
                holder.layout_not_exist.visibility = View.GONE
            }

            if (item.primaryType == EventType.PrimaryType.number.name) {
                holder.text_win_code.visibility = View.VISIBLE
                holder.text_win_code.text = holder.itemView.context.getString(R.string.format_event_win_code, item.winCode)
            } else {
                holder.text_win_code.visibility = View.GONE
            }

            holder.text_title.text = item.title
//            if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                holder.text_count.text = holder.itemView.context.getString(R.string.format_real_time_event_join_count, item.joinCount)
//            } else {
//                holder.text_count.text = holder.itemView.context.getString(R.string.format_event_join_count, item.joinCount)
//            }

            if (item.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                holder.text_count.text = holder.itemView.context.getString(R.string.format_real_time_event_winner_count, item.winnerCount)
            } else {
                holder.text_count.text = holder.itemView.context.getString(R.string.format_event_winner_count, item.winnerCount)
            }

            if (mGiftList != null && mGiftList!!.isNotEmpty()) {
                holder.layout_list.removeAllViews()
                for (i in 0..(mGiftList!!.size - 1)) {
                    val gift = mGiftList!![i]

                    val giftBinding = ItemEventGiftBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                    holder.layout_list.addView(giftBinding.root)
                    Glide.with(holder.itemView.context).load(gift.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(giftBinding.imageEventGift)
                    giftBinding.textEventGiftName.text = gift.title
                    giftBinding.textEventGiftRemainCount.visibility = View.GONE
                    giftBinding.textEventGiftDetailView.visibility = View.GONE
                    if(gift.type == "visit" && mMyWinList != null){
                        for(myWin in mMyWinList!!){
                            if(gift.giftNo == myWin.gift!!.giftNo){
                                giftBinding.textEventGiftDetailView.visibility = View.VISIBLE

                                when(myWin.giftStatus){
                                    EnumData.GiftStatus.wait.status ->{
                                        giftBinding.textEventGiftDetailView.isEnabled = true
                                        giftBinding.textEventGiftDetailView.text = holder.itemView.context.getString(R.string.word_detail_view2)
                                        giftBinding.textEventGiftDetailView.setOnClickListener {
                                            val intent = Intent(holder.itemView.context, EventWinDetailActivity::class.java)
                                            intent.putExtra(Const.DATA, myWin)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            if(holder.itemView.context is EventImpressionActivity){
                                                (holder.itemView.context as EventImpressionActivity).eventWinDetailLauncher.launch(intent)
                                            }

                                        }
                                    }
                                    EnumData.GiftStatus.use.status->{
                                        if(gift.reviewPoint != null && gift.reviewPoint!! > 0){
                                            giftBinding.textEventGiftDetailView.text = holder.itemView.context.getString(R.string.format_review_write_point, FormatUtil.getMoneyType(gift.reviewPoint.toString()))
                                        }else{
                                            giftBinding.textEventGiftDetailView.text = holder.itemView.context.getString(R.string.word_review_write)
                                        }
                                        giftBinding.textEventGiftDetailView.isEnabled = true
                                        giftBinding.textEventGiftDetailView.setOnClickListener {
                                            val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
                                            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                                            intent.putExtra(Const.EVENT_WIN, myWin)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            if(holder.itemView.context is EventImpressionActivity){
                                                (holder.itemView.context as EventImpressionActivity).eventWinDetailLauncher.launch(intent)
                                            }
                                        }
                                    }
                                    EnumData.GiftStatus.reviewWrite.status->{
                                        giftBinding.textEventGiftDetailView.isEnabled = false
                                        giftBinding.textEventGiftDetailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                        giftBinding.textEventGiftDetailView.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, android.R.color.transparent))
                                        giftBinding.textEventGiftDetailView.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                                        giftBinding.textEventGiftDetailView.text = holder.itemView.context.getString(R.string.word_review_write_complete)
                                    }
                                    EnumData.GiftStatus.expired.status->{
                                        giftBinding.textEventGiftDetailView.isEnabled = false
                                        giftBinding.textEventGiftDetailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                        giftBinding.textEventGiftDetailView.setBackgroundColor(ResourceUtil.getColor(holder.itemView.context, android.R.color.transparent))
                                        giftBinding.textEventGiftDetailView.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                                        giftBinding.textEventGiftDetailView.text = holder.itemView.context.getString(R.string.word_expire)
                                    }
                                }


                                break
                            }
                        }
                    }


                    if (i < (mGiftList!!.size - 1)) {
                        (giftBinding.root.layoutParams as LinearLayout.LayoutParams).bottomMargin = holder.itemView.context.resources!!.getDimensionPixelSize(R.dimen.height_20)
                    }
                }
            }

            holder.layout_invite.setOnClickListener {
                if (!PplusCommonUtil.loginCheck((holder.itemView.context as FragmentActivity), (holder.itemView.context as EventImpressionActivity).signInLauncher)) {
                    return@setOnClickListener
                }
                val intent = Intent(holder.itemView.context, InviteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                holder.itemView.context.startActivity(intent)
            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)
            if (item.user!!.profileImage != null) {
                Glide.with(holder.itemView.context).load(item.user!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.ic_event_profile_default)
            }

            holder.text_name.text = item.user!!.nickname


            if (StringUtils.isEmpty(item.amount)) {
                if (mGiftList != null && mGiftList!!.isNotEmpty()) {
                    if (mGiftList!!.size > 1) {
                        holder.text_gift_name.visibility = View.VISIBLE
                    } else {
                        holder.text_gift_name.visibility = View.GONE
                    }
                } else {
                    holder.text_gift_name.visibility = View.GONE
                }
                holder.text_gift_name.text = item.gift!!.title
            } else {
                holder.text_gift_name.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(item.amount)))
            }


            if (StringUtils.isNotEmpty(item.impression)) {
                holder.text_impression.visibility = View.VISIBLE
                holder.text_impression.text = item.impression
            } else {
                holder.text_impression.visibility = View.GONE
            }

            if (item.user!!.friend) {
                holder.layout_friend.visibility = View.VISIBLE

                val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.user!!.mobile)).list()
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
                if (listener != null) {
                    listener!!.onItemClick(position - 1)
                }
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