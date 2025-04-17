//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_visit_buy_history.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//import kotlin.collections.HashMap
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class VisitHistoryAdapter : RecyclerView.Adapter<VisitHistoryAdapter.ViewHolder> {
//
//    var mDataList: MutableList<Buy>? = null
//    var listener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor() : super() {
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Buy {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Buy>? {
//
//        return mDataList
//    }
//
//    fun add(data: Buy) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Buy>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Buy>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Buy>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Buy) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Buy>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Buy>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_visit_buy_history_image
//        val text_page_name = itemView.text_visit_buy_history_page_name
//        val text_price = itemView.text_visit_buy_history_price
//        val text_point = itemView.text_visit_buy_history_point
//        val text_review = itemView.text_visit_buy_history_review
//        val text_date = itemView.text_visit_buy_history_date
//        val text_status = itemView.text_visit_buy_history_status
//
//        init {
//            text_page_name.setSingleLine()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//
//        if (item.page != null) {
//            holder.text_page_name.text = item.page!!.name
//
//            if (StringUtils.isNotEmpty(item.page!!.thumbnail)) {
//                Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
//            } else {
//                holder.image.setImageResource(R.drawable.img_page_profile_default)
//            }
//        }
//
//
//        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
//        holder.text_point.visibility = View.GONE
//        var date = ""
//        when (item.process) {
//            1 -> {//결제완료
//
//                if (StringUtils.isNotEmpty(item.payDatetime)) {
//                    date = PplusCommonUtil.getDateFormat(item.payDatetime!!)
//                }
//                holder.text_review.visibility = View.VISIBLE
//                holder.text_status.setText(R.string.word_pay_complete)
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//
//                if(item.isReviewExist != null && item.isReviewExist!!){
//                    holder.text_review.isEnabled = false
//                    holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
//                    holder.text_review.setText(R.string.word_review_write_complete)
//                }else{
//                    holder.text_review.isEnabled = true
//                    holder.text_review.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
//                    holder.text_review.setText(R.string.word_review_write)
//                    holder.text_review.setOnClickListener {
//                        val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
//                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                        intent.putExtra(Const.BUY, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//                    }
//                }
//
//                if(item.savedPoint != null && item.savedPoint!! > 0){
//                    holder.text_point.visibility = View.VISIBLE
//                    if(item.isPaymentPoint != null && item.isPaymentPoint!!){
//                        holder.text_point.text = holder.itemView.context.getString(R.string.format_saved, FormatUtil.getMoneyType(item.savedPoint.toString()))
//                    }else{
//                        holder.text_point.text = holder.itemView.context.getString(R.string.format_will_save, FormatUtil.getMoneyType(item.savedPoint.toString()))
//                    }
//                }
//
//            }
//            2 -> {//취소
//
//                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
//                    date = PplusCommonUtil.getDateFormat(item.cancelDatetime!!)
//                }
//                holder.text_review.visibility = View.GONE
//                holder.text_status.setText(R.string.word_cancel_complete)
//                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
//            }
//        }
//
//        if (StringUtils.isNotEmpty(date)) {
//            holder.text_date.text = date
//            holder.text_date.visibility = View.VISIBLE
//        } else {
//            holder.text_date.visibility = View.GONE
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position)
//            }
//        }
//
//    }
//
//
//    private fun getBuy(context: Context, seqNo: Long, position: Int) {
//        val params = HashMap<String, String>()
//        params["seqNo"] = seqNo.toString()
//        (context as BaseActivity).showProgress("")
//        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                (context as BaseActivity).hideProgress()
//                if (response!!.data != null) {
//                    mDataList!![position] = response.data
//                    notifyItemChanged(position)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                (context as BaseActivity).hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_visit_buy_history, parent, false)
//        return ViewHolder(v)
//    }
//}