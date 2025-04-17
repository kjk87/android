package com.pplus.luckybol.apps.shippingsite.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.shippingsite.ui.ShippingSiteRegActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.ShippingSite
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ItemShippingSiteBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by imac on 2018. 1. 8..
 */
class ShippingSiteAdapter : RecyclerView.Adapter<ShippingSiteAdapter.ViewHolder> {

    var mDataList: MutableList<ShippingSite>? = null
    var listener: OnItemClickListener? = null
    var deleteListener: OnItemDeleteListener? = null
    var mSelectData : ShippingSite? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    interface OnItemDeleteListener {

        fun onItemDelete()
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {

        this.deleteListener = listener
    }

    fun getItem(position: Int): ShippingSite {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ShippingSite>? {

        return mDataList
    }

    fun add(data: ShippingSite) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ShippingSite>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ShippingSite) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ShippingSite>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemShippingSiteBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_site_name = binding.textShippingSiteName
        val text_address = binding.textShippingSiteAddress
        val text_modify = binding.textShippingSiteModify
        val text_delete = binding.textShippingSiteDelete
        val image_check = binding.imageShippingSiteCheck

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_site_name.text = item.receiverName
        holder.text_address.text = item.address + " " + item.addressDetail

        holder.image_check.isSelected = mSelectData != null && mSelectData!!.seqNo == item.seqNo

        holder.text_modify.setOnClickListener {
            val intent = Intent(holder.itemView.context, ShippingSiteRegActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            launcher?.launch(intent)
        }

        holder.text_delete.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.msg_question_delete_site), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            delete(holder.itemView.context, item)
                        }
                        else -> {}
                    }
                }
            })
            builder.builder().show(holder.itemView.context)
        }


        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.absoluteAdapterPosition)
//            }
            mSelectData = item
            notifyDataSetChanged()
        }
    }

    private fun delete(context: Context, item:ShippingSite){
        val params = HashMap<String, String>()
        params["seqNo"] = item.seqNo.toString()
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().deleteShippingSite(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()
                mDataList!!.remove(item)
                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShippingSiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}