package com.pplus.luckybol.apps.card.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Card
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ItemCardConfigBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class CardConfigAdapter() : RecyclerView.Adapter<CardConfigAdapter.ViewHolder>() {

    var mDataList: MutableList<Card>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)

        fun onRefresh()
    }

    init {
        this.mDataList = arrayListOf()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Card {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Card>? {

        return mDataList
    }

    fun add(data: Card) {

        if (mDataList == null) {
            mDataList = ArrayList<Card>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Card>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Card>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Card) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Card>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Card>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemCardConfigBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageCardConfig
        val text_name = binding.textCardConfigName
        val text_number = binding.textCardConfigNumber
        val image_delete = binding.imageCardConfigDelete

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        when (item.cardCode) {
            EnumData.CardCode.CCSU.name -> {
                holder.image.setImageResource(R.drawable.bg_card_suhyup_s)
                holder.text_name.setText(R.string.card_CCSU)
            }
            EnumData.CardCode.CCSS.name -> {
                holder.image.setImageResource(R.drawable.bg_card_samsung_s)
                holder.text_name.setText(R.string.card_CCSS)
            }
            EnumData.CardCode.CCNH.name -> {//NH카드
                holder.image.setImageResource(R.drawable.bg_card_nh_s)
                holder.text_name.setText(R.string.card_CCNH)
            }
            EnumData.CardCode.CCLO.name -> {//롯데카드
                holder.image.setImageResource(R.drawable.bg_card_lotte_s)
                holder.text_name.setText(R.string.card_CCLO)
            }
            EnumData.CardCode.CCLG.name -> {//신한카드
                holder.image.setImageResource(R.drawable.bg_card_sinhan_s)
                holder.text_name.setText(R.string.card_CCLG)
            }
            EnumData.CardCode.CCKM.name -> {
                //국민카드
                holder.image.setImageResource(R.drawable.bg_card_kb_s)
                holder.text_name.setText(R.string.card_CCKM)
            }
            EnumData.CardCode.CCKJ.name -> {
                //광주은행
                holder.image.setImageResource(R.drawable.bg_card_kj_s)
                holder.text_name.setText(R.string.card_CCKJ)
            }
            EnumData.CardCode.CCKE.name -> {
                //외환은행
                holder.image.setImageResource(R.drawable.bg_card_hana_s)
                holder.text_name.setText(R.string.card_CCKE)
            }
            EnumData.CardCode.CCJB.name -> {
                //전북은행
                holder.image.setImageResource(R.drawable.bg_card_jb_s)
                holder.text_name.setText(R.string.card_CCJB)
            }
            EnumData.CardCode.CCHN.name -> {
                //하나SK카드
                holder.image.setImageResource(R.drawable.bg_card_sk_s)
                holder.text_name.setText(R.string.card_CCHN)
            }
            EnumData.CardCode.CCDI.name -> {
                //현대카드
                holder.image.setImageResource(R.drawable.bg_card_hyundai_s)
                holder.text_name.setText(R.string.card_CCDI)
            }
            EnumData.CardCode.CCCT.name -> {
                //씨티은행
                holder.image.setImageResource(R.drawable.bg_card_city_s)
                holder.text_name.setText(R.string.card_CCCT)
            }
            EnumData.CardCode.CCCJ.name -> {
                //제주은행
                holder.image.setImageResource(R.drawable.bg_card_jj_s)
                holder.text_name.setText(R.string.card_CCCJ)
            }
            EnumData.CardCode.CCBC.name -> {
                //비씨카드
                holder.image.setImageResource(R.drawable.bg_card_bc_s)
                holder.text_name.setText(R.string.card_CCBC)
            }
        }

        holder.text_number.text = "**** ${item.cardNumber}"

        holder.image_delete.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_question_delete_card), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: EVENT_ALERT?) {
                    when (event_alert) {
                        EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["id"] = item.id.toString()
                            (holder.itemView.context as BaseActivity).showProgress("")
                            ApiBuilder.create().deleteCard(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    (holder.itemView.context as BaseActivity).hideProgress()
                                    listener?.onRefresh()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    (holder.itemView.context as BaseActivity).hideProgress()
                                }
                            }).build().call()
                        }
                        else -> {}
                    }
                }
            }).builder().show(holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardConfigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}