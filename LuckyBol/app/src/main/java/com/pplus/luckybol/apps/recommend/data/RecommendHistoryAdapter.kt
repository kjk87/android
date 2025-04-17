package com.pplus.luckybol.apps.recommend.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.databinding.ItemRecommendHistoryBinding
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class RecommendHistoryAdapter : RecyclerView.Adapter<RecommendHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<User>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): User {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<User>? {

        return mDataList
    }

    fun add(data: User) {

        if (mDataList == null) {
            mDataList = ArrayList<User>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<User>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<User>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: User) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<User>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<User>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemRecommendHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_name = binding.textRecommendHistoryNickname
        val text_date = binding.textRecommendHistoryDate
        val text_expire_date = binding.textRecommendHistoryBolRewardExpireDate

        init {

            text_expire_date.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: User = mDataList!![position]

        holder.text_name.text = item.nickname

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.joinDate)
        val output = SimpleDateFormat("yyyy.MM.dd")
        holder.text_date.text = output.format(d)
        val cal = Calendar.getInstance()
        cal.time = d
        cal.add(Calendar.YEAR, 1)
        holder.text_expire_date.text = holder.itemView.context.getString(R.string.format_bol_reward_expire_date, output.format(cal.time))

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}