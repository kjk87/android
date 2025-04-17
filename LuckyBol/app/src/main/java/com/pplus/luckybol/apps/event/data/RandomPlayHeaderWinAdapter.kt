package com.pplus.luckybol.apps.event.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.HeaderRandomPlayWinBinding
import com.pplus.luckybol.databinding.ItemGiftBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils


/**
 * Created by imac on 2018. 1. 8..
 */
class RandomPlayHeaderWinAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mEvent: Event? = null
    var mDataList: MutableList<EventWin>? = null
    var listener: OnItemClickListener? = null
    var mTotalCount = 0

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

    class ViewHeader(binding: HeaderRandomPlayWinBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageRandomPlayWin

        init {
        }
    }

    class ViewHolder(binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageGift
        val layout_url = binding.layoutGiftUrl
        val text_name = binding.textGiftName
        val text_price = binding.textGiftPrice
        val view_left_bar = binding.viewGiftLeftBar
        val view_right_bar = binding.viewGiftRightBar
        val image_winner_profile = binding.imageGiftWinnerProfile
        val text_winner_name = binding.textGiftWinnerName
        val layout_winner_friend = binding.layoutGiftWinnerFriend

        init {
            binding.layoutGiftWinner.visibility = View.VISIBLE
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
            val item = mEvent

            if (item != null) {

                Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            }

        } else if (holder is ViewHolder) {

            var pos = position
            if (mEvent != null) {
                pos = position - 1
            }
            if(pos % 2 == 0){
                holder.view_left_bar.visibility = View.GONE
                holder.view_right_bar.visibility = View.VISIBLE
            }else{
                holder.view_left_bar.visibility = View.VISIBLE
                holder.view_right_bar.visibility = View.GONE
            }

            val item = getItem(pos)
            Glide.with(holder.itemView.context).load(item.gift!!.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
            holder.text_name.text = item.gift!!.title
            holder.text_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_normal_price, FormatUtil.getMoneyType(item.gift!!.price.toString())))

            if(StringUtils.isNotEmpty(item.gift!!.giftLink)){
                holder.layout_url.visibility = View.VISIBLE
                holder.layout_url.setOnClickListener {
                    PplusCommonUtil.openChromeWebView(holder.itemView.context, item.gift!!.giftLink!!)
                }
            }else{
                holder.layout_url.visibility = View.GONE
            }

            if (item.user!!.profileImage != null) {
                Glide.with(holder.itemView.context).load(item.user!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image_winner_profile)
            } else {
                holder.image_winner_profile.setImageResource(R.drawable.ic_event_profile_default)
            }

            holder.text_winner_name.text = item.user!!.nickname

            if (item.user!!.friend) {
                holder.layout_winner_friend.visibility = View.VISIBLE

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

                holder.layout_winner_friend.setOnClickListener {
                    ToastUtil.show(holder.itemView.context, holder.itemView.context?.getString(R.string.format_is_friend, name))
                }

            } else {
                holder.layout_winner_friend.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_HEADER) {
            val binding = HeaderRandomPlayWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0 && mEvent != null
    }

}