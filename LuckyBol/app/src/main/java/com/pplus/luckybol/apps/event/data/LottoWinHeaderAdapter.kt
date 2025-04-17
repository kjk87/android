package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.core.network.model.dto.LottoWinNumber
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.HeaderLottoWinBinding
import com.pplus.luckybol.databinding.ItemLottoBinding
import com.pplus.luckybol.databinding.ItemLottoWinnerBinding
import com.pplus.luckybol.databinding.ItemSelectedLottoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoWinHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    var mEvent: Event? = null
    var mEventGift: EventGift? = null
    var mDataList: MutableList<EventWinJpa>? = null
    var listener: OnItemClickListener? = null
    var mWinnerCount = 0

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

    fun getItem(position: Int): EventWinJpa {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventWinJpa>? {

        return mDataList
    }

    fun add(data: EventWinJpa) {

        if (mDataList == null) {
            mDataList = ArrayList<EventWinJpa>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventWinJpa>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventWinJpa>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventWinJpa) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventWinJpa>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventWinJpa>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(binding: HeaderLottoWinBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_gift = binding.textHeaderLottoWinGift
        val text_gift_price_title = binding.textHeaderLottoWinGiftPriceTitle
        val text_gift_price = binding.textHeaderLottoWinGiftPrice
        val text_gift_detail = binding.textHeaderLottoWinGiftDetail
        val layout_lotto_win = binding.layoutHeaderLottoWinWin
        val layout_lotto_win_number = binding.layoutHeaderLottoWinWinNumber
        val view_lotto_win_announce_Bar = binding.viewHeaderLottoWinWinAnnounceBar
        val text_lotto_win_announce = binding.textHeaderLottoWinWinAnnounce
        val text_lotto_win_winner_desc = binding.textHeaderLottoWinWinnerDesc
        val layout_winner_title = binding.layoutLottoWinWinnerTitle
        val text_winner_not_exist = binding.textLottoWinWinnerNotExist

        init {
        }
    }

    class ViewHolder(binding: ItemLottoWinnerBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageLottoWinnerProfile
        val text_nickname = binding.textLottoWinnerNickname
        val text_impression = binding.textLottoWinnerImpression
        val text_gift_name = binding.textLottoWinnerGiftName
        val layout_lotto_winner_number = binding.layoutLottoWinnerNumber

        init {
            text_nickname.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            if (mEvent == null || mEventGift == null) {
                return
            }




            holder.text_gift.text = mEvent!!.title


            if(StringUtils.isNotEmpty(mEventGift!!.giftLink)){
                holder.text_gift_detail.visibility = View.VISIBLE
                holder.text_gift_detail.setOnClickListener {
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mEventGift!!.giftLink)))
                }
            }else{
                holder.text_gift_detail.visibility = View.GONE
            }

            when(mEventGift!!.type){
                "bol"->{
                    holder.text_gift_price.text = holder.itemView.context.getString(R.string.format_bol_unit, FormatUtil.getMoneyType(mEventGift!!.price.toString()))
                    holder.text_gift_price_title.setText(R.string.word_win_bol)
                    holder.text_gift_detail.setText(R.string.msg_view_sponsor_info)
                }
                "point"->{
                    holder.text_gift_price.text = holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(mEventGift!!.price.toString()))
                    holder.text_gift_price_title.setText(R.string.word_win_cash)
                    holder.text_gift_detail.setText(R.string.msg_view_sponsor_info)
                }
                else->{
                    holder.text_gift_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(mEventGift!!.price.toString()))
                    holder.text_gift_price_title.setText(R.string.word_gift_price)
                    holder.text_gift_detail.setText(R.string.msg_view_gift_detail)
                }
            }



            when (mEvent!!.status) {
                "announce", "complete", "finish" -> {
                    if(mWinnerCount > 0){
                        holder.layout_winner_title.visibility = View.VISIBLE
                        holder.layout_lotto_win.visibility = View.VISIBLE
                        holder.text_winner_not_exist.visibility = View.GONE
                        if(mWinnerCount > 1){
                            holder.text_lotto_win_winner_desc.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_lucky_lotto_winner_desc, mWinnerCount.toString()))
                        }else{
                            holder.text_lotto_win_winner_desc.text = holder.itemView.context.getString(R.string.msg_lucky_lotto_gift_desc)
                        }
                    }else{
                        holder.layout_lotto_win.visibility = View.GONE
                        holder.layout_winner_title.visibility = View.GONE
                        holder.text_winner_not_exist.visibility = View.VISIBLE
                    }


                    val params = HashMap<String, String>()
                    params["eventSeqNo"] = mEvent!!.no.toString()
                    ApiBuilder.create().getLottoWinNumberList(params).setCallback(object : PplusCallback<NewResultResponse<LottoWinNumber>> {
                        override fun onResponse(call: Call<NewResultResponse<LottoWinNumber>>?,
                                                response: NewResultResponse<LottoWinNumber>?) {
                            if (response?.datas != null) {

                                val list = response.datas!!
                                holder.layout_lotto_win_number.removeAllViews()
                                for (i in 0 until list.size) {
                                    val selectedLottoBinding = ItemSelectedLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                                    selectedLottoBinding.textSelectedLottoNumber.text = list[i].lottoNumber.toString()

                                    if (list[i].lottoNumber in 1..10) {
                                        selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_f2c443)
                                    } else if (list[i].lottoNumber in 11..20) {
                                        selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_85c5f1)
                                    } else if (list[i].lottoNumber in 21..30) {
                                        selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_e4807b)
                                    } else if (list[i].lottoNumber in 31..40) {
                                        selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_a689ee)
                                    } else {
                                        selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_57d281)
                                    }

                                    holder.layout_lotto_win_number.addView(selectedLottoBinding.root)
                                    if (i < list.size - 1) {
                                        (selectedLottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<LottoWinNumber>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<LottoWinNumber>?) {
                        }
                    }).build().call()


                    if(StringUtils.isNotEmpty(mEvent!!.winAnnounceUrl)){
                        holder.view_lotto_win_announce_Bar.visibility = View.VISIBLE
                        holder.text_lotto_win_announce.visibility = View.VISIBLE
                        holder.text_lotto_win_announce.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mEvent!!.winAnnounceUrl))
                            holder.itemView.context.startActivity(intent)
                        }
                    }else{
                        holder.view_lotto_win_announce_Bar.visibility = View.GONE
                        holder.text_lotto_win_announce.visibility = View.GONE
                    }

                }
                else -> {
                    holder.layout_lotto_win.visibility = View.GONE
                    holder.layout_winner_title.visibility = View.GONE
                }
            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            if (item.member!!.profileAttachment != null) {
                Glide.with(holder.itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.ic_event_profile_default)
            }

            holder.text_nickname.text = item.member!!.nickname
            holder.text_impression.text = item.impression
            holder.text_gift_name.text = item.giftTitle

            for (j in 0 until item.eventJoin!!.lottoSelectedNumberList!!.size) {
                val lottoNumber = item.eventJoin!!.lottoSelectedNumberList!![j].lottoNumber
                val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                lottoBinding.textLottoNumber.text = lottoNumber.toString()

                lottoBinding.textLottoNumber.layoutParams.width = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_72)
                lottoBinding.textLottoNumber.layoutParams.height = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_72)

                if (lottoNumber in 1..10) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_1)
                } else if (lottoNumber in 11..20) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_2)
                } else if (lottoNumber in 21..30) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_3)
                } else if (lottoNumber in 31..40) {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_4)
                } else {
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_5)
                }

                val isAccord = item.eventJoin!!.lottoSelectedNumberList!![j].isAccord != null && item.eventJoin!!.lottoSelectedNumberList!![j].isAccord!!
                lottoBinding.textLottoNumber.isSelected = isAccord

                holder.layout_lotto_winner_number.addView(lottoBinding.root)
                if (j < item.eventJoin!!.lottoSelectedNumberList!!.size - 1) {
                    (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_24)
                }
            }

            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.absoluteAdapterPosition - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderLottoWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemLottoWinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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