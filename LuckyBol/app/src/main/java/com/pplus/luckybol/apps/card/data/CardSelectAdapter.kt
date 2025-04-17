package com.pplus.luckybol.apps.card.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.card.ui.CardRegActivity
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.ui.PurchaseProductShipPgActivity
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.model.dto.Card
import com.pplus.luckybol.databinding.ItemCardSelectBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class CardSelectAdapter : RecyclerView.Adapter<CardSelectAdapter.ViewHolder> {

    var mDataList: MutableList<Card>? = null
    var listener: OnItemClickListener? = null
    var mSelectData:Card? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)

        fun onRefresh()
    }

    constructor() : super() {
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

    class ViewHolder(binding: ItemCardSelectBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout = binding.layoutCardSelect
        val layout_not_exist = binding.layoutCardSelectNotExist
        val text_name = binding.textCardSelectName
        val text_number = binding.textCardSelectNumber
        val image_select = binding.imageCardCardSelect

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        if(item.id != null){
            holder.layout.visibility = View.VISIBLE
            holder.layout_not_exist.visibility = View.GONE
            when (item.cardCode) {
                EnumData.CardCode.CCSU.name -> {
                    holder.layout.setBackgroundResource(R.drawable.bg_card_suhyup)
                    holder.text_name.setText(R.string.card_CCSU)
                }
                EnumData.CardCode.CCSS.name -> {
                    holder.layout.setBackgroundResource(R.drawable.bg_card_samsung)
                    holder.text_name.setText(R.string.card_CCSS)
                }
                EnumData.CardCode.CCNH.name -> {//NH카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_nh)
                    holder.text_name.setText(R.string.card_CCNH)
                }
                EnumData.CardCode.CCLO.name -> {//롯데카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_lotte)
                    holder.text_name.setText(R.string.card_CCLO)
                }
                EnumData.CardCode.CCLG.name -> {//신한카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_sinhan)
                    holder.text_name.setText(R.string.card_CCLG)
                }
                EnumData.CardCode.CCKM.name -> {
                    //국민카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_kb)
                    holder.text_name.setText(R.string.card_CCKM)
                }
                EnumData.CardCode.CCKJ.name -> {
                    //광주은행
                    holder.layout.setBackgroundResource(R.drawable.bg_card_kj)
                    holder.text_name.setText(R.string.card_CCKJ)
                }
                EnumData.CardCode.CCKE.name -> {
                    //외환은행
                    holder.layout.setBackgroundResource(R.drawable.bg_card_hana)
                    holder.text_name.setText(R.string.card_CCKE)
                }
                EnumData.CardCode.CCJB.name -> {
                    //전북은행
                    holder.layout.setBackgroundResource(R.drawable.bg_card_jb)
                    holder.text_name.setText(R.string.card_CCJB)
                }
                EnumData.CardCode.CCHN.name -> {
                    //하나SK카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_sk)
                    holder.text_name.setText(R.string.card_CCHN)
                }
                EnumData.CardCode.CCDI.name -> {
                    //현대카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_hyundai)
                    holder.text_name.setText(R.string.card_CCDI)
                }
                EnumData.CardCode.CCCT.name -> {
                    //씨티은행
                    holder.layout.setBackgroundResource(R.drawable.bg_card_city)
                    holder.text_name.setText(R.string.card_CCCT)
                }
                EnumData.CardCode.CCCJ.name -> {
                    //제주은행
                    holder.layout.setBackgroundResource(R.drawable.bg_card_jj)
                    holder.text_name.setText(R.string.card_CCCJ)
                }
                EnumData.CardCode.CCBC.name -> {
                    //비씨카드
                    holder.layout.setBackgroundResource(R.drawable.bg_card_bc)
                    holder.text_name.setText(R.string.card_CCBC)
                }
            }

            holder.text_number.text = "**** ${item.cardNumber}"


            if(mSelectData != null && mSelectData!!.id == item.id){
                holder.image_select.visibility = View.VISIBLE
            }else{
                holder.image_select.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                mSelectData = item
                notifyDataSetChanged()
//            listener?.onItemClick(holder.absoluteAdapterPosition)
            }
        }else{
            holder.layout.visibility = View.GONE
            holder.layout_not_exist.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                if(LoginInfoManager.getInstance().user.verification!!.media == "external"){
                    val intent = Intent(holder.itemView.context, CardRegActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    if(holder.itemView.context as BaseActivity is PurchaseProductShipPgActivity){
                        (holder.itemView.context as PurchaseProductShipPgActivity).cardRegLauncher.launch(intent)
                    }

                }else{
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {}
                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val intent = Intent(holder.itemView.context, VerificationMeActivity::class.java)
                                    intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    (holder.itemView.context as BaseActivity).verificationLauncher.launch(intent)
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(holder.itemView.context)
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}