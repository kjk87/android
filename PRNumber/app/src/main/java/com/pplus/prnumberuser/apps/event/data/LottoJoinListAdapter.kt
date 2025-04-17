package com.pplus.prnumberuser.apps.event.data

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.databinding.ItemLottoBinding
import com.pplus.prnumberuser.databinding.ItemLottoJoinListBinding
import com.pplus.prnumberuser.databinding.ItemLottoJoinNumberBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoJoinListAdapter() : RecyclerView.Adapter<LottoJoinListAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null


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

        return mDataList!!.get(position)
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
            notifyItemChanged(position)
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

    class ViewHolder(binding:ItemLottoJoinListBinding) : RecyclerView.ViewHolder(binding.root) {

        val title = binding.textLottoJoinListEventTitle
        val layout_join_list = binding.layoutLottoJoinList

        init {
            title.visibility = View.GONE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

//        holder.title.text = item.title

        if(StringUtils.isNotEmpty(item.winCode)){
            val winList = item.winCode!!.split(",")

            for (i in 0 until item.joinList!!.size) {
                val joinCodeList = item.joinList!![i].winCode!!.split(",")
                val lottoJoinNumberBinding = ItemLottoJoinNumberBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
//                val output = SimpleDateFormat("yyyy.MM.dd")
//                layout_join.text_lotto_join_date.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.joinList!![i].joinDate))

                var matchCount = 0
                for (j in 0 until joinCodeList.size) {
                    val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                    lottoBinding.textLottoNumber.text = joinCodeList[j]

                    if(winList.contains(joinCodeList[j])){
                        matchCount++
                        if (joinCodeList[j].toInt() in 1..10) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_1_sel)
                        } else if (joinCodeList[j].toInt() in 11..20) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_2_sel)
                        } else if (joinCodeList[j].toInt() in 21..30) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_3_sel)
                        } else if (joinCodeList[j].toInt() in 31..40) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_4_sel)
                        } else {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_5_sel)
                        }
                    }else{
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_1)
                    }

                    lottoJoinNumberBinding.layoutLottoJoinNumber.addView(lottoBinding.root)
                    if (j < joinCodeList.size - 1) {
                        (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_14)
                    }
                }

                lottoJoinNumberBinding.textLottoJoinDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.textSize_46pt).toFloat())

                if(matchCount >= item.lottoMatchNum!!){
                    lottoJoinNumberBinding.textLottoJoinDate.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                    lottoJoinNumberBinding.textLottoJoinDate.setText(R.string.word_cong_win)
                }else{
                    lottoJoinNumberBinding.textLottoJoinDate.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                    lottoJoinNumberBinding.textLottoJoinDate.text = "-"
                }


                holder.layout_join_list.addView(lottoJoinNumberBinding.root)

                if (i < item.joinList!!.size - 1) {
                    (lottoJoinNumberBinding.root.layoutParams as LinearLayout.LayoutParams).bottomMargin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_30)
                }
            }
        }else{
            for (i in 0 until item.joinList!!.size) {
                val joinCodeList = item.joinList!![i].winCode!!.split(",")
                val lottoJoinNumberBinding = ItemLottoJoinNumberBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)

                lottoJoinNumberBinding.textLottoJoinDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, holder.itemView.context.resources.getDimensionPixelSize(R.dimen.textSize_34pt).toFloat())
                lottoJoinNumberBinding.textLottoJoinDate.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                val output = SimpleDateFormat("yyyy.MM.dd")
                lottoJoinNumberBinding.textLottoJoinDate.text = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.joinList!![i].joinDate))
                for (j in 0 until joinCodeList.size) {
                    val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                    lottoBinding.textLottoNumber.text = joinCodeList[j]
                    lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.ic_lotto_number_bg_line)

                    lottoJoinNumberBinding.layoutLottoJoinNumber.addView(lottoBinding.root)
                    if (j < joinCodeList.size - 1) {
                        (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_14)
                    }
                }

                holder.layout_join_list.addView(lottoJoinNumberBinding.root)

                if (i < item.joinList!!.size - 1) {
                    (lottoJoinNumberBinding.root.layoutParams as LinearLayout.LayoutParams).bottomMargin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.height_30)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoJoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}