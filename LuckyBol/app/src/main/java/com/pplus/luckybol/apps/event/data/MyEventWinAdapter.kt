package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.event.ui.EventReviewRegActivity
import com.pplus.luckybol.apps.event.ui.MyEventWinFragment
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.databinding.ItemMyEventWinBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class MyEventWinAdapter() : RecyclerView.Adapter<MyEventWinAdapter.ViewHolder>() {

    var mDataList: MutableList<EventWin>? = null
    var listener: OnItemClickListener? = null
    var mMyEventWinFragment:MyEventWinFragment? = null


    interface OnItemClickListener {

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
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<EventWin>()

    }

    fun setDataList(dataList: MutableList<EventWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMyEventWinBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMyEventWin
        val text_gift_name = binding.textMyEventWinGiftName
        val text_win_date = binding.textMyEventWinDate
        val text_reg_review = binding.textMyEventWinRegReview
        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.gift!!.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        holder.text_gift_name.text = item.gift!!.title


        holder.text_reg_review.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventReviewRegActivity::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.putExtra(Const.EVENT_WIN, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            mMyEventWinFragment?.eventReviewRegLauncher?.launch(intent)
        }

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winDate)
        val output = SimpleDateFormat("yyyy.MM.dd")
        holder.text_win_date.text = output.format(d)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyEventWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}