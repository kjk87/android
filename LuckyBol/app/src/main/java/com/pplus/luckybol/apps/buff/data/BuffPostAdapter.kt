package com.pplus.luckybol.apps.buff.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.ui.BuffPostLikeActivity
import com.pplus.luckybol.apps.buff.ui.BuffPostReplyActivity
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.BuffPost
import com.pplus.luckybol.core.network.model.dto.BuffPostImage
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ItemBuffPostBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffPostAdapter() : RecyclerView.Adapter<BuffPostAdapter.ViewHolder>() {

    var mDataList: MutableList<BuffPost>? = null
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

    fun getItem(position: Int): BuffPost {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffPost>? {

        return mDataList
    }

    fun add(data: BuffPost) {

        if (mDataList == null) {
            mDataList = ArrayList<BuffPost>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffPost>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<BuffPost>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuffPost) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuffPost>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffPost>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemBuffPostBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageBuffPostProfile
        val text_name = binding.textBuffPostName
        val text_date = binding.textBuffPostRegDate
        val layout_friend = binding.layoutBuffPostFriend
        val pager_image = binding.pagerBuffPostImage
        val layout_contents = binding.layoutBuffPostContents
        val text_title = binding.textBuffPostTitle
        val text_contents = binding.textBuffPostContents
        val text_divide_amount = binding.textBuffPostDivideAmount
        val layout_none_img = binding.layoutBuffPostNoneImg
        val image_none_img = binding.imageBuffPostNoneImg
        val text_none_img = binding.textBuffPostNoneImg
        val text_win_price = binding.textBuffPostWinPrice
        val layout_gift_title = binding.layoutBuffPostGiftTitle
        val text_gift_title = binding.textBuffPostGiftTitle
        val text_like_count = binding.textBuffPostLikeCount
        val text_reply_count = binding.textBuffPostReplyCount
        val text_like = binding.textBuffPostLike
        val text_reply = binding.textBuffPostReply

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if (item.member!!.profileAttachment != null) {
            Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image_profile)
        } else {
            holder.image_profile.setImageResource(R.drawable.ic_contact_profile_default)
        }

        holder.text_name.text = item.member!!.nickname
        holder.text_date.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, item.regDatetime!!)
        holder.text_like_count.text = FormatUtil.getMoneyType(item.likeCount.toString())
        holder.text_reply_count.text = holder.itemView.context.getString(R.string.format_reply_count, FormatUtil.getMoneyType(item.replyCount.toString()))

        if(item.isFriend != null && item.isFriend!!){
            holder.layout_friend.visibility = View.VISIBLE
            holder.layout_friend.setOnClickListener {
                val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.member!!.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
                val memberName:String
                if (contacts != null && contacts.size > 0) {
                    memberName = contacts[0].memberName
                } else {
                    memberName = item.member!!.nickname!!
                }
                ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.format_friend_toast, memberName))
            }
        }else{
            holder.layout_friend.visibility = View.GONE
        }

        val imageAdapter = BuffPostImageAdapter()
        holder.pager_image.adapter = imageAdapter
        if (item.imageList != null && item.imageList!!.isNotEmpty()) {
            imageAdapter.setDataList(item.imageList as MutableList<BuffPostImage>)
        }

        holder.layout_none_img.visibility = View.GONE
        holder.text_win_price.visibility = View.GONE
        holder.layout_gift_title.visibility = View.GONE
        holder.layout_contents.visibility = View.VISIBLE
        when (item.type) { // normal, shoppingBuff, lottoBuff , eventBuff, eventGift
            "normal" -> {
                holder.text_title.visibility = View.GONE
                holder.text_contents.visibility = View.VISIBLE
                holder.text_contents.text = item.content
                holder.text_divide_amount.visibility = View.GONE
                holder.text_like_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_like, 0, 0, 0)
                holder.text_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_like, 0, 0, 0)
                holder.text_like.setText(R.string.word_buff_like)
            }
            "shoppingBuff" -> {
                holder.text_title.visibility = View.VISIBLE
                holder.text_title.setText(R.string.word_shopping_save)
                if (item.hidden != null && item.hidden!!) {
                    holder.text_contents.visibility = View.GONE
                    holder.layout_none_img.visibility = View.VISIBLE
                    holder.image_none_img.setImageResource(R.drawable.img_buff_post_shopping_hidden)
                    holder.text_none_img.setText(R.string.msg_buff_post_shopping_desc)
                } else {
                    holder.text_contents.visibility = View.VISIBLE
                }
                holder.text_divide_amount.visibility = View.VISIBLE
                when (item.divideType) {
                    "point" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_cash, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                    }
                    "bol" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_bol, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                    }
                }

                holder.text_like_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setText(R.string.word_buff_thankyou)

            }
            "lottoBuff" -> {
                holder.text_title.visibility = View.VISIBLE
                holder.text_title.setText(R.string.word_lotto_win)

                holder.layout_none_img.visibility = View.VISIBLE
                holder.image_none_img.setImageResource(R.drawable.img_buff_post_lotto)
                holder.text_none_img.setText(R.string.msg_buff_post_lotto_desc)

                holder.text_win_price.visibility = View.VISIBLE
                holder.text_divide_amount.visibility = View.VISIBLE
                when (item.divideType) {
                    "point" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_cash, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                        holder.text_win_price.text = holder.itemView.context.getString(R.string.format_win_cash, FormatUtil.getMoneyTypeFloat(item.winPrice.toString()))
                    }
                    "bol" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_bol, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                        holder.text_win_price.text = holder.itemView.context.getString(R.string.format_win_cash, FormatUtil.getMoneyTypeFloat(item.winPrice.toString()))
                    }
                }

                holder.text_like_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setText(R.string.word_buff_thankyou)
            }
            "eventBuff" -> {
                holder.text_title.visibility = View.VISIBLE
                holder.text_title.setText(R.string.word_event_win)

                holder.layout_none_img.visibility = View.VISIBLE
                holder.image_none_img.setImageResource(R.drawable.img_buff_post_event)
                holder.text_none_img.setText(R.string.msg_buff_post_event_desc)

                holder.text_divide_amount.visibility = View.VISIBLE
                when (item.divideType) {
                    "point" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_cash, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                    }
                    "bol" -> {
                        holder.text_divide_amount.text = holder.itemView.context.getString(R.string.format_divided_bol, FormatUtil.getMoneyTypeFloat(item.divideAmount.toString()))
                    }
                }

                holder.text_like_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_thankyou, 0, 0, 0)
                holder.text_like.setText(R.string.word_buff_thankyou)
            }
            "eventGift" -> {
                holder.layout_contents.visibility = View.GONE
                holder.text_like_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_cong, 0, 0, 0)
                holder.text_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_buff_post_cong, 0, 0, 0)
                holder.text_like.setText(R.string.word_buff_cong)
                holder.layout_gift_title.visibility = View.VISIBLE
                holder.text_gift_title.text = item.content
            }
        }

        holder.text_reply_count.setOnClickListener {
            val intent = Intent(holder.itemView.context, BuffPostReplyActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if(launcher != null){
                launcher!!.launch(intent)
            }else{
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.text_reply.setOnClickListener {
            val intent = Intent(holder.itemView.context, BuffPostReplyActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if(launcher != null){
                launcher!!.launch(intent)
            }else{
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.text_like_count.setOnClickListener {
            val intent = Intent(holder.itemView.context, BuffPostLikeActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            if(launcher != null){
                launcher!!.launch(intent)
            }else{
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.text_like.setOnClickListener {
            val params = HashMap<String, String>()
            params["buffPostSeqNo"] = item.seqNo.toString()
            (holder.itemView.context as BaseActivity).showProgress("")
            ApiBuilder.create().buffPostLike(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                override fun onResponse(call: Call<NewResultResponse<String>>?,
                                        response: NewResultResponse<String>?) {
                    (holder.itemView.context as BaseActivity).hideProgress()
                    if (response?.data != null) {
                        when (response.data) {
                            "like" -> {
                                item.isLike = true
                            }
                            "cancel" -> {
                                item.isLike = false
                            }
                        }
                    }
                    notifyItemChanged(holder.absoluteAdapterPosition)
                }

                override fun onFailure(call: Call<NewResultResponse<String>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<String>?) {
                    (holder.itemView.context as BaseActivity).hideProgress()
                }
            }).build().call()
        }

        holder.text_like.isSelected = (item.isLike != null && item.isLike!!)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuffPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}