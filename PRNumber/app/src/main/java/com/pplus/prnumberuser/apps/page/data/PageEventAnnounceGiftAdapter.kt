package com.pplus.prnumberuser.apps.page.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.page.ui.PageEventWinDetailActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.EventGift
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ItemPageEventAnnounceGiftBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by imac on 2018. 1. 8..
 */
class PageEventAnnounceGiftAdapter : RecyclerView.Adapter<PageEventAnnounceGiftAdapter.ViewHolder> {

    var mDataList: MutableList<EventGift>? = null
    var listener: OnItemClickListener? = null
//    var mTopVisible = true

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
//        mTopVisible = topVisible
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): EventGift {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<EventGift>? {

        return mDataList
    }

    fun add(data: EventGift) {

        if (mDataList == null) {
            mDataList = ArrayList<EventGift>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<EventGift>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<EventGift>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: EventGift) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<EventGift>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<EventGift>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPageEventAnnounceGiftBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_gift_name= binding.textPageEventAnnounceGiftName
        val recycler_win = binding.recyclerPageEventAnnounceGiftWin
        val text_more = binding.idPageEventAnnounceGiftMore

        init {
            text_gift_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        val count = item.totalCount!! - item.remainCount!!

        holder.text_gift_name.text = "${item.title}(${holder.itemView.context.getString(R.string.format_count_unit4, FormatUtil.getMoneyType(count.toString()))})"

        val adapter = PageEventWinAdapter()
        holder.recycler_win.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recycler_win.adapter = adapter
        holder.text_more.visibility = View.GONE
        holder.text_more.setOnClickListener {
            val intent = Intent(holder.itemView.context, PageEventWinDetailActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }
        val params = HashMap<String, String>()
        params["giftSeqNo"] = item.giftNo.toString()
        params["pg"] = "1"
        ApiBuilder.create().getEventWinListByGiftSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>?, response: NewResultResponse<EventWin>?) {
                if(response?.datas != null){

                    if(response.datas.size > 3){
                        holder.text_more.visibility = View.VISIBLE
                        adapter.setDataList(response.datas.subList(0, 3))
                    }else {
                        holder.text_more.visibility = View.GONE
                        adapter.setDataList(response.datas)
                    }

                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>?, t: Throwable?, response: NewResultResponse<EventWin>?) {
            }
        }).build().call()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPageEventAnnounceGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}