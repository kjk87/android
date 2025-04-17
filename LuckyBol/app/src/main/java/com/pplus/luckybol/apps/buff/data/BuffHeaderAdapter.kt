package com.pplus.luckybol.apps.buff.data

import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.ui.BuffCashHistoryActivity
import com.pplus.luckybol.apps.buff.ui.BuffMemberActivity
import com.pplus.luckybol.apps.buff.ui.BuffPostLikeActivity
import com.pplus.luckybol.apps.buff.ui.BuffPostReplyActivity
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.data.BuffMemberThumbAdapter
import com.pplus.luckybol.apps.product.ui.ProductShipDetailActivity
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.HeaderBuffBinding
import com.pplus.luckybol.databinding.ItemBuffPostBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class BuffHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mBuff: Buff? = null
    var mDataList: MutableList<BuffPost>? = null
    var listener: OnItemClickListener? = null
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

    fun getItem(position: Int): BuffPost {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<BuffPost>? {

        return mDataList
    }

    fun add(data: BuffPost) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuffPost>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
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

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuffPost>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(binding: HeaderBuffBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageBuffBackground = binding.imageBuffBackground
        val textBuffCatchphrase = binding.textBuffCatchphrase
        val textBuffTitle = binding.textBuffTitle
        val layoutBuffBol = binding.layoutBuffBol
        val textBuffTotalBol = binding.textBuffTotalBol
        val layoutBuffCash = binding.layoutBuffCash
        val textBuffTotalCash = binding.textBuffTotalCash
        val recyclerBuffMember = binding.recyclerBuffMember
        val textBuffMemberCount = binding.textBuffMemberCount

        init {
        }
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
        return mDataList!!.size + 1
    }

    val itemDecor = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position != 0) outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.width_21) * -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            if (mBuff == null) {
                return
            }

            holder.itemView.layoutParams.height = DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS

            val buffMemberAdapter = BuffMemberThumbAdapter()
            holder.recyclerBuffMember.adapter = buffMemberAdapter
            holder.recyclerBuffMember.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            if(holder.recyclerBuffMember.itemDecorationCount > 0){
                holder.recyclerBuffMember.removeItemDecoration(itemDecor)
            }
            holder.recyclerBuffMember.addItemDecoration(itemDecor)

            buffMemberAdapter.listener = object : BuffMemberThumbAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, view: View) {
                    val intent = Intent(holder.itemView.context, BuffMemberActivity::class.java)
                    intent.putExtra(Const.DATA, mBuff)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    if (launcher != null) {
                        launcher!!.launch(intent)
                    } else {
                        holder.itemView.context.startActivity(intent)
                    }
                }
            }

            holder.textBuffMemberCount.setOnClickListener {
                val intent = Intent(holder.itemView.context, BuffMemberActivity::class.java)
                intent.putExtra(Const.DATA, mBuff)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (launcher != null) {
                    launcher!!.launch(intent)
                } else {
                    holder.itemView.context.startActivity(intent)
                }
            }

            holder.layoutBuffBol.setOnClickListener {
                val intent = Intent(holder.itemView.context, BuffCashHistoryActivity::class.java)
                intent.putExtra(Const.TYPE, "bol")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (launcher != null) {
                    launcher!!.launch(intent)
                } else {
                    holder.itemView.context.startActivity(intent)
                }

            }

            holder.layoutBuffCash.setOnClickListener {
                val intent = Intent(holder.itemView.context, BuffCashHistoryActivity::class.java)
                intent.putExtra(Const.TYPE, "point")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (launcher != null) {
                    launcher!!.launch(intent)
                } else {
                    holder.itemView.context.startActivity(intent)
                }
            }

            Glide.with(holder.itemView.context).load(mBuff!!.image).apply(RequestOptions().centerCrop()).into(holder.imageBuffBackground)

            holder.textBuffTitle.text = mBuff!!.title
            holder.textBuffCatchphrase.text = mBuff!!.info
            holder.textBuffTotalBol.text = FormatUtil.getMoneyTypeFloat(mBuff!!.totalDividedBol.toString())
            holder.textBuffTotalCash.text = FormatUtil.getMoneyTypeFloat(mBuff!!.totalDividedPoint.toString())

            val params = HashMap<String, String>()
            params["buffSeqNo"] = mBuff!!.seqNo.toString()
            params["includeMe"] = "true"
            params["size"] = "5"
            ApiBuilder.create().getBuffMemberList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffMember>>> {
                override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                        response: NewResultResponse<SubResultResponse<BuffMember>>?) {

                    if (response?.data != null) {

                        holder.textBuffMemberCount.text = holder.textBuffMemberCount.context.getString(R.string.format_count_unit4, response.data!!.totalElements.toString())

                        buffMemberAdapter.setDataList(response.data!!.content as MutableList<BuffMember>)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<SubResultResponse<BuffMember>>?) {

                }
            }).build().call()

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            if (item.member!!.profileAttachment != null) {
                Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(holder.image_profile)
            } else {
                holder.image_profile.setImageResource(R.drawable.ic_contact_profile_default)
            }

            holder.text_name.text = item.member!!.nickname
            holder.text_date.text = DateFormatUtils.formatPostTimeString(holder.itemView.context, item.regDatetime!!)
            holder.text_like_count.text = FormatUtil.getMoneyType(item.likeCount.toString())
            holder.text_reply_count.text = holder.itemView.context.getString(R.string.format_reply_count, FormatUtil.getMoneyType(item.replyCount.toString()))

            if (item.isFriend != null && item.isFriend!!) {
                holder.layout_friend.visibility = View.VISIBLE
                holder.layout_friend.setOnClickListener {
                    val contacts = DBManager.getInstance(holder.itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.member!!.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
                    val memberName: String
                    if (contacts != null && contacts.size > 0) {
                        memberName = contacts[0].memberName
                    } else {
                        memberName = item.member!!.nickname!!
                    }
                    ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.format_friend_toast, memberName))
                }
            } else {
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
                "productBuff" -> {
                    holder.text_title.visibility = View.VISIBLE
                    holder.text_title.setText(R.string.word_shopping_save)
                    if (item.hidden != null && item.hidden!!) {
                        holder.text_contents.visibility = View.GONE
                        holder.layout_none_img.visibility = View.VISIBLE
                        holder.image_none_img.setImageResource(R.drawable.img_buff_post_shopping_hidden)
                        holder.text_none_img.setText(R.string.msg_buff_post_shopping_desc)
                    } else {
                        holder.text_contents.visibility = View.VISIBLE
                        holder.text_contents.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_format_under_bar, item.content))
                        holder.text_contents.setOnClickListener {
                            val productPrice = ProductPrice()
                            productPrice.seqNo = item.productPriceSeqNo

                            val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
                            intent.putExtra(Const.DATA, productPrice)
                            holder.itemView.context.startActivity(intent)
                        }
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
                    holder.text_contents.visibility = View.GONE
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
                    holder.text_contents.visibility = View.GONE
                    holder.layout_none_img.visibility = View.VISIBLE
                    holder.image_none_img.setImageResource(R.drawable.img_buff_post_event)
                    holder.text_none_img.setText(R.string.msg_buff_post_event_desc)

                    holder.text_divide_amount.visibility = View.VISIBLE
                    holder.text_win_price.visibility = View.VISIBLE
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
                if (launcher != null) {
                    launcher!!.launch(intent)
                } else {
                    holder.itemView.context.startActivity(intent)
                }
            }

            holder.text_like_count.setOnClickListener {
                val intent = Intent(holder.itemView.context, BuffPostLikeActivity::class.java)
                intent.putExtra(Const.DATA, item)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                if (launcher != null) {
                    launcher!!.launch(intent)
                } else {
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
                            if (item.likeCount == null) {
                                item.likeCount = 0
                            }
                            when (response.data) {
                                "like" -> {
                                    item.isLike = true
                                    item.likeCount = item.likeCount!! + 1
                                }
                                "cancel" -> {
                                    item.isLike = false
                                    item.likeCount = item.likeCount!! - 1
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
                listener?.onItemClick(holder.absoluteAdapterPosition - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderBuffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemBuffPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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