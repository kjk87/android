package com.pplus.prnumberuser.apps.page.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.databinding.ItemMainPageBinding
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPageAdapter : RecyclerView.Adapter<MainPageAdapter.ViewHolder> {

    var mDataList: MutableList<Page>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Page {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Page>? {

        return mDataList
    }

    fun add(data: Page) {

        if (mDataList == null) {
            mDataList = ArrayList<Page>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Page>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Page>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Page) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Page>()
        notifyDataSetChanged()
    }

    fun setBusPlus(data: BusProviderData) {
        if (mDataList != null) {
            val page = data.subData
            if (page is Page) {
                for (i in 0 until mDataList!!.size - 1) {
                    if (mDataList!![i] == page) {
                        mDataList!![i] = page
                        break
                    }
                }
            }
            notifyDataSetChanged()
        }

    }

    fun setDataList(dataList: MutableList<Page>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMainPage
        val text_name = binding.textMainPageName
        val text_description = binding.textMainPageIntroduce
        val text_distance = binding.textMainPageDistance
        val text_reward = binding.textMainPagePoint

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.name!!

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_square_profile_default)
        }


//        holder.text_count.text = FormatUtil.getMoneyType(item.plusCount.toString())

        if (StringUtils.isNotEmpty(item.catchphrase)) {
            holder.text_description.text = item.catchphrase
        } else {
            holder.text_description.text = ""
        }

//        if (item.numberList != null && item.numberList!!.isNotEmpty()) {
//            holder.text_number.visibility = View.VISIBLE
//            val number = item.numberList!![0].number
//            holder.text_number.text = PplusNumberUtil.getPrNumberFormat(number)
//        } else {
//            holder.text_number.visibility = View.GONE
//        }

        val distance = item.distance
        if (distance != null) {
            holder.text_distance.visibility = View.VISIBLE
            var strDistance: String? = null
            if (distance > 1) {
                strDistance = String.format("%.2f", distance) + "km"
            } else {
                strDistance = (distance * 1000).toInt().toString() + "m"
            }
            holder.text_distance.text = strDistance
        } else {
            holder.text_distance.visibility = View.GONE
        }

        if(item.point != null && item.point!! > 0){
            holder.text_reward.visibility = View.VISIBLE
//            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.point!!.toInt()}%"))
            holder.text_reward.text = "${item.point!!.toInt()}%"
        }else{
            holder.text_reward.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(position, it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}