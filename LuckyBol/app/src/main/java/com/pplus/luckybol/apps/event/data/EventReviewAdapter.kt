package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.event.ui.EventReplyActivity
import com.pplus.luckybol.apps.event.ui.EventReviewFragment
import com.pplus.luckybol.apps.event.ui.MyEventReviewFragment
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.EventReview
import com.pplus.luckybol.core.network.model.dto.EventReviewImage
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ItemEventReviewBinding
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class EventReviewAdapter() : RecyclerView.Adapter<EventReviewAdapter.ViewHolder>() {

    var mDataList: MutableList<EventReview>? = null
    var listener: OnItemClickListener? = null
    var mMyEventReviewFragment: MyEventReviewFragment? = null
    var mEventReviewFragment: EventReviewFragment? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventReview {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventReview>? {

        return mDataList
    }

    fun add(data: EventReview) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventReview>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventReview) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventReview>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventReview>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemEventReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageEventReviewProfile
        val text_name = binding.textEventReviewName
        val layout_image = binding.layoutEventReviewImage
        val pager_image = binding.pagerEventReviewImage
        val indicator = binding.indicatorEventReview
        val text_gift = binding.textEventReviewGift
        val text_review = binding.textEventReview
        val text_reply = binding.textEventReviewReply
        val layout_friend = binding.layoutEventReviewFriend
        val text_friend = binding.textEventReviewFriend
        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.member != null){
            holder.text_name.text = item.member!!.nickname
            if(item.member!!.profileAttachment != null){
                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
            }else{
                holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
            }
        }

        holder.text_gift.text = item.eventWin?.eventGift?.title
        holder.text_review.text = item.review
        holder.text_reply.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reply, item.replyCount.toString()))
        holder.text_reply.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventReplyActivity::class.java)
            intent.putExtra(Const.EVENT_REVIEW, item)
            intent.putExtra(Const.POSITION, holder.absoluteAdapterPosition)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if(mMyEventReviewFragment != null){
                mMyEventReviewFragment!!.eventReplyLauncher.launch(intent)
            }else if(mEventReviewFragment != null){
                mEventReviewFragment!!.eventReplyLauncher.launch(intent)
            }
        }
        if (item.imageList != null && item.imageList!!.isNotEmpty()) {
            holder.layout_image.visibility = View.VISIBLE
            holder.pager_image.visibility = View.VISIBLE
            val imageAdapter = EventReviewImagePagerAdapter(holder.itemView.context)
            imageAdapter.dataList = item.imageList as ArrayList<EventReviewImage>
            holder.pager_image.adapter = imageAdapter
            holder.indicator.visibility = View.VISIBLE
            holder.indicator.removeAllViews()
            holder.indicator.build(LinearLayout.HORIZONTAL, item.imageList!!.size)
            holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {

                    holder.indicator.setCurrentItem(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            imageAdapter.setListener(object : EventReviewImagePagerAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    listener?.onItemClick(holder.absoluteAdapterPosition)
                }
            })
        } else {
            holder.indicator.removeAllViews()
            holder.indicator.visibility = View.GONE
            holder.pager_image.visibility = View.GONE
            holder.pager_image.adapter = null
            holder.layout_image.visibility = View.GONE
        }

        if(item.friend != null && item.friend!! && LoginInfoManager.getInstance().isMember && item.member!!.seqNo != LoginInfoManager.getInstance().user.no){
            holder.layout_friend.visibility = View.VISIBLE

            val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.member!!.mobileNumber?.replace(Const.APP_TYPE+"##", ""))).list()
            var name: String? = null
            if (contacts != null && contacts.size > 0) {
                name = contacts[0].memberName
            } else {
                if (StringUtils.isNotEmpty(item.member!!.nickname)) {
                    name = item.member!!.nickname
                } else if (StringUtils.isNotEmpty(item.member!!.memberName)) {
                    name = item.member!!.memberName
                } else {
                    name = holder.itemView.context.getString(R.string.word_unknown)
                }
            }

            holder.text_friend.text = holder.itemView.context.getString(R.string.format_is_friend, name)
        }else{
            holder.layout_friend.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}