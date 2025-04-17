//package com.pplus.luckybol.apps.event.data
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.event.ui.LottoActivity
//import com.pplus.luckybol.core.database.DBManager
//import com.pplus.luckybol.core.database.entity.ContactDao
//import com.pplus.luckybol.core.network.model.dto.Event
//import com.pplus.luckybol.core.network.model.dto.EventWin
//import com.pplus.luckybol.core.network.model.dto.LottoGift
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.databinding.HeaderLottoEventImpressionBinding
//import com.pplus.luckybol.databinding.ItemEventGiftBinding
//import com.pplus.luckybol.databinding.ItemEventImpressionBinding
//import com.pplus.luckybol.databinding.ItemSelectedLottoBinding
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class LottoEventImpressionHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mEvent: Event? = null
//    var mLottoTimes = ""
//    var mContext: Context? = null
//    var mDataList: MutableList<EventWin>? = null
//    var mGiftList: MutableList<LottoGift>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): EventWin {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<EventWin>? {
//
//        return mDataList
//    }
//
//    fun add(data: EventWin) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<EventWin>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<EventWin>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<EventWin>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: EventWin) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<EventWin>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<EventWin>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(binding: HeaderLottoEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {
//        val text_times = binding.textLottoEventImpressionTimes
//        val layout_wincode = binding.layoutLottoEventDetailImpressionWincode
//        val text_1st_gift_name = binding.textLottoEventImpression1stGiftName
//        val text_1st_win_count = binding.textLottoEventImpression1stGiftCount
//        val layout_list = binding.layoutLottoEventImpressionGiftList
//        val layout_not_exist = binding.layoutLottoEventImpressionNotExistWinner
//        val text_move_lotto = binding.textLottEventImpressionMoveLotto
//
//        init {
//            layout_not_exist.visibility = View.GONE
//        }
//    }
//
//    class ViewHolder(binding: ItemEventImpressionBinding) : RecyclerView.ViewHolder(binding.root) {
//        val image = binding.imageEventImpression
//        val text_name = binding.textEventImpressionGiftName
//        val text_gift_name = binding.textEventImpressionGiftName
//        val text_impression = binding.textEventImpression
//        val layout_friend = binding.layoutEventImpressionFriend
//        val text_friend = binding.textEventImpressionFriend
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if (mEvent == null) {
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mEvent!!
//
//            holder.text_times.text = mContext?.getString(R.string.format_luckybol_lotto_event_title, mLottoTimes)
//            holder.text_1st_gift_name.text = mContext?.getString(R.string.format_bol, FormatUtil.getMoneyType(item.lottoPrice.toString()))
//
//
//            holder.text_1st_win_count.text = mContext?.getString(R.string.format_lotto_win_count, item.winnerCount.toString())
//
//
//            if ((mDataList!!.size == 0)) {
//                holder.layout_not_exist.visibility = View.VISIBLE
//            } else {
//                holder.layout_not_exist.visibility = View.GONE
//            }
//
//            holder.text_move_lotto.setOnClickListener {
////                val intent = Intent(mContext, AppMainActivity2::class.java)
//                val intent = Intent(mContext, LottoActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                mContext?.startActivity(intent)
//            }
//
//            val winList = item.winCode!!.split(",")
//            holder.layout_wincode.removeAllViews()
//            for (i in 0 until winList.size) {
//                val selectedLottoBinding = ItemSelectedLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
//                selectedLottoBinding.textSelectedLottoNumber.text = winList[i]
//
//                if (winList[i].toInt() in 1..10) {
//                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_1)
//                } else if (winList[i].toInt() in 11..20) {
//                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_2)
//                } else if (winList[i].toInt() in 21..30) {
//                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_3)
//                } else if (winList[i].toInt() in 31..40) {
//                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_4)
//                } else {
//                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_5)
//                }
//
//                holder.layout_wincode.addView(selectedLottoBinding.root)
//                if (i < winList.size - 1) {
//                    (selectedLottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_26)
//                }
//            }
//
//            if (mGiftList != null && mGiftList!!.isNotEmpty()) {
//                holder.layout_list.visibility = View.VISIBLE
//                holder.layout_list.removeAllViews()
//                for (i in 0 until mGiftList!!.size) {
//                    val gift = mGiftList!![i].gift!!
//                    val giftBinding = ItemEventGiftBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
//
//                    Glide.with(holder.itemView.context).load(gift.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(giftBinding.imageEventGift)
//
//                    giftBinding.textEventGiftName.text = gift.title
//                    giftBinding.textEventGiftRemainCount.text = holder.itemView.context.getString(R.string.format_lotto_win_count, mGiftList!![i].winnerCount.toString())
//                    holder.layout_list.addView(giftBinding.root)
//                    if (i < (mGiftList!!.size - 1)) {
//                        (giftBinding.root.layoutParams as LinearLayout.LayoutParams).bottomMargin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_20)
//                    }
//                }
//            } else {
//                holder.layout_list.visibility = View.GONE
//            }
//
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//            if (item.user!!.profileImage != null) {
//                Glide.with(holder.itemView.context).load(item.user!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image)
//            } else {
//                holder.image.setImageResource(R.drawable.ic_event_profile_default)
//            }
//
//            holder.text_name.text = item.user!!.nickname
//
//            if (StringUtils.isEmpty(item.amount)) {
//                holder.text_gift_name.text = item.gift!!.title
//            } else {
//                holder.text_gift_name.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_bol_unit, FormatUtil.getMoneyType(item.amount)))
//            }
//
//            if (StringUtils.isNotEmpty(item.impression)) {
//                holder.text_impression.visibility = View.VISIBLE
//                holder.text_impression.text = item.impression
//            } else {
//                holder.text_impression.visibility = View.GONE
//            }
//
//            if (item.user!!.friend) {
//                holder.layout_friend.visibility = View.VISIBLE
//
//                val contacts = DBManager.getInstance(mContext).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.user!!.mobile?.replace(Const.APP_TYPE+"##", ""))).list()
//                var name: String? = null
//                if (contacts != null && contacts.size > 0) {
//                    name = contacts[0].memberName
//                } else {
//                    if (StringUtils.isNotEmpty(item.user!!.nickname)) {
//                        name = item.user!!.nickname
//                    } else if (StringUtils.isNotEmpty(item.user!!.name)) {
//                        name = item.user!!.name
//                    } else if (StringUtils.isNotEmpty(item.user!!.name)) {
//                        name = item.user!!.name
//                    } else {
//                        name = mContext?.getString(R.string.word_unknown)
//                    }
//                }
//
//                holder.text_friend.text = mContext?.getString(R.string.format_is_friend, name)
//            } else {
//                holder.layout_friend.visibility = View.GONE
//            }
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(position - 1)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            val binding = HeaderLottoEventImpressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHeader(binding)
//        } else if (viewType == TYPE_ITEM) {
//            val binding = ItemEventImpressionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            return ViewHolder(binding)
//        }
//        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
//    }
//
//    private fun isPositionHeader(position: Int): Boolean {
//        return position == 0
//    }
//
//}