package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_goods_history.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class GoodsHistoryAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsHistoryAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<BuyGoods>? = null
    var listener: OnItemClickListener? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    constructor() : super()

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): BuyGoods {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<BuyGoods>? {

        return mDataList
    }

    fun add(data: BuyGoods) {

        if (mDataList == null) {
            mDataList = ArrayList<BuyGoods>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<BuyGoods>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<BuyGoods>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: BuyGoods) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<BuyGoods>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<BuyGoods>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_goods_history_image
        val text_name = itemView.text_goods_history_name
        val text_info = itemView.text_goods_history_info

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!!.get(position)

        if (item.goods != null) {
            holder.text_name.text = item.goods!!.name
            if (item.goods!!.attachments != null && item.goods!!.attachments!!.images != null && item.goods!!.attachments!!.images!!.isNotEmpty()) {
                if (mContext != null) {
                    val id = item.goods!!.attachments!!.images!![0]

                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                }

            } else {
                holder.image.setImageResource(R.drawable.prnumber_default_img)
            }
        }

        var date:String? = null
        when (item.process) {
            EnumData.BuyGoodsProcess.PAY.process -> {
                date = item.payDatetime
            }
            EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
                date = item.cancelDatetime
            }
            EnumData.BuyGoodsProcess.USE.process -> {
                date = item.useDatetime
            }
            else ->{
                date = item.payDatetime
            }
        }
        if (StringUtils.isNotEmpty(date)) {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date)
            val output = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            date = output.format(d)
        }

        when (item.process) {
            EnumData.BuyGoodsProcess.PAY.process -> {
                holder.text_info.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_history_info_579ffb, item.buy!!.buyerName, FormatUtil.getMoneyType(item.price.toString()), date))
            }
            EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
                holder.text_info.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_history_info_ff4646_1, item.buy!!.buyerName, FormatUtil.getMoneyType(item.price.toString()), date))
            }
            EnumData.BuyGoodsProcess.USE.process -> {
                holder.text_info.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_history_info_b7b7b7, item.buy!!.buyerName, FormatUtil.getMoneyType(item.price.toString()), date))
            }
            else ->{
                holder.text_info.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_history_info_579ffb, item.buy!!.buyerName, FormatUtil.getMoneyType(item.price.toString()), date))
            }
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_history, parent, false)
        return ViewHolder(v)
    }
}