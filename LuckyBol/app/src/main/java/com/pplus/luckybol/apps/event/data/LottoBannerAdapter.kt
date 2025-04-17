package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ItemLottoBannerBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoBannerAdapter() : RecyclerView.Adapter<LottoBannerAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
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

    fun getItem(position: Int): Event {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Event>? {

        return mDataList
    }

    fun add(data: Event) {

        if (mDataList == null) {
            mDataList = ArrayList<Event>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Event>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Event>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Event) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Event>()
    }

    fun setDataList(dataList: MutableList<Event>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemLottoBannerBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageLottoBanner
        val text_status = binding.textLottoBannerStatus
        val layout_winner_count = binding.layoutLottoBannerWinnerCount
        val text_winner_count = binding.textLottoBannerCount

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.layout_winner_count.visibility = View.GONE
        holder.text_status.visibility = View.VISIBLE
        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        when (item.status) {
            "active" -> {
                holder.text_status.setText(R.string.word_recruit_ing)
            }
            "conclude" -> {
                holder.text_status.setText(R.string.word_recruit_complete)
            }
            "pending" -> {
                holder.text_status.setText(R.string.word_announce_wait)
            }
            "announce", "complete", "finish" -> {
                holder.text_status.visibility = View.GONE
                val params = HashMap<String, String>()
                params["eventSeqNo"] = item.no.toString()
                ApiBuilder.create().getLottoWinnerCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                    override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                            response: NewResultResponse<Int>?) {
                        if(response?.data != null){
                            holder.layout_winner_count.visibility = View.VISIBLE
                            holder.text_winner_count.text = response.data.toString()
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Int>?) {
                    }
                }).build().call()
            }
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}