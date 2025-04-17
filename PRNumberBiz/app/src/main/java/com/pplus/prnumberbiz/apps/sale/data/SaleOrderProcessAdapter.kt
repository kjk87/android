package com.pplus.prnumberbiz.apps.sale.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Buy
import kotlinx.android.synthetic.main.item_sale_order_process.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class SaleOrderProcessAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SaleOrderProcessAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Buy>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Buy {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Buy>? {

        return mDataList
    }

    fun add(data: Buy) {

        if (mDataList == null) {
            mDataList = ArrayList<Buy>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Buy>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Buy>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Buy) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Buy>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Buy>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_book_time = itemView.text_sale_order_process_book_time
        val text_title = itemView.text_sale_order_process_title
        val text_address = itemView.text_sale_order_process_address
        val text_memo = itemView.text_sale_order_process_memo
        val layout_type = itemView.layout_sale_order_process_type
        val text_type = itemView.text_sale_order_process_type

        init {
            text_title.setSingleLine()
            text_address.setSingleLine()
            text_memo.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_title.text = item.title

        when(item.orderType){
            0->{//매장
                holder.layout_type.setBackgroundResource(R.color.color_ff696a)
                holder.text_type.setText(R.string.word_order_store)
                holder.text_book_time.visibility =View.GONE
                holder.text_address.visibility = View.GONE
            }
            1->{//포장
                holder.layout_type.setBackgroundResource(R.color.color_a26df3)
                holder.text_type.setText(R.string.word_order_packing)

                if(StringUtils.isNotEmpty(item.bookDatetime)){
                    holder.text_book_time.visibility =View.VISIBLE

                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.bookDatetime)
                    val output = SimpleDateFormat("HH:mm", Locale.getDefault())
                    holder.text_book_time.text = mContext!!.getString(R.string.format_book_time, output.format(d))
                }else{
                    holder.text_book_time.visibility =View.GONE
                }
                holder.text_address.visibility = View.GONE
            }
            2->{//배달
                holder.layout_type.setBackgroundResource(R.color.color_3ec082)
                holder.text_type.setText(R.string.word_order_delivery)
                holder.text_book_time.visibility =View.VISIBLE
                if(StringUtils.isNotEmpty(item.clientAddress)){
                    holder.text_address.visibility = View.VISIBLE
                    holder.text_address.text = item.clientAddress
                }else{
                    holder.text_address.visibility = View.GONE
                }

            }
        }

        if(StringUtils.isNotEmpty(item.memo)){
            holder.text_memo.visibility = View.VISIBLE
            holder.text_memo.text = item.memo
        }else{
            holder.text_memo.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sale_order_process, parent, false)
        return ViewHolder(v)
    }
}