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
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.product.ui.CheckAuthCodeActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Buy
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_buy_history.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.HashMap
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class BuyHistoryAdapter : RecyclerView.Adapter<BuyHistoryAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Buy>? = null
//    var listener: OnItemClickListener? = null
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Buy {
//
//        return mDataList!!.get(position)
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
//        val image = itemView.image_buy_goods_history_image
//        val text_page_name = itemView.text_buy_history_page_name
//        val text_text_title = itemView.text_buy_history_title
//        val text_date = itemView.text_buy_history_date
//        val text_price = itemView.text_buy_history_price
//        val text_use = itemView.text_buy_history_use
//        val text_status = itemView.text_buy_history_status
//        val text_point = itemView.text_buy_history_point
//        val image_arrow = itemView.image_buy_history_arrow
//
//        init {
//            text_text_title.setSingleLine()
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
//        if(item.page != null){
//            holder.text_page_name.visibility = View.VISIBLE
//            holder.text_page_name.text = item.page!!.name
//        }else{
//            holder.text_page_name.visibility = View.GONE
//        }
//        holder.text_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.price.toString()))
//
//        if(item.payType == "qr"){
//            holder.image_arrow.visibility = View.GONE
//
//            holder.text_text_title.text = holder.itemView.context.getString(R.string.word_qr_pay)
//
//            if (item.page != null) {
//                Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
//            } else {
//                holder.image.setImageResource(R.drawable.img_page_profile_default)
//            }
//        }else{
//            holder.image_arrow.visibility = View.VISIBLE
//            holder.text_text_title.text = item.title
//            if (item.buyGoodsList != null && item.buyGoodsList!!.isNotEmpty() && item.buyGoodsList!![0].goods!!.goodsImageList != null && item.buyGoodsList!![0].goods!!.goodsImageList!!.isNotEmpty()) {
//                Glide.with(holder.itemView.context).load(item.buyGoodsList!![0].goods!!.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_page_profile_default).error(R.drawable.img_page_profile_default)).into(holder.image)
//            } else {
//                holder.image.setImageResource(R.drawable.img_page_profile_default)
//            }
//        }
//
//
//        holder.text_point.visibility = View.GONE
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        holder.text_use.visibility = View.GONE
//        when (item.process) {
//            EnumData.BuyGoodsProcess.PAY.process -> {
//
//                if(item.payType == "qr"){
//                    if (StringUtils.isNotEmpty(item.payDatetime)) {
//                        date = PplusCommonUtil.getDateFormat(item.payDatetime!!)
//                    }
//                    holder.text_use.visibility = View.VISIBLE
//                    holder.text_status.setText(R.string.word_pay_complete)
//                    holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_737373))
//
//                    if(item.isReviewExist != null && item.isReviewExist!!){
//                        holder.text_use.isEnabled = false
//                        holder.text_use.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
//                        holder.text_use.setText(R.string.word_review_write_complete)
//                    }else{
//                        holder.text_use.isEnabled = true
//                        holder.text_use.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
//                        holder.text_use.setText(R.string.word_review_write)
//                        holder.text_use.setOnClickListener {
//                            val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
//                            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                            intent.putExtra(Const.BUY, item)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//                        }
//                    }
//
//                    if(item.savedPoint != null && item.savedPoint!! > 0){
//                        holder.text_point.visibility = View.VISIBLE
//                        if(item.isPaymentPoint != null && item.isPaymentPoint!!){
//                            holder.text_point.text = holder.itemView.context.getString(R.string.format_saved, FormatUtil.getMoneyType(item.savedPoint.toString()))
//                        }else{
//                            holder.text_point.text = holder.itemView.context.getString(R.string.format_will_save, FormatUtil.getMoneyType(item.savedPoint.toString()))
//                        }
//                    }
//                }else{
//                    holder.text_status.setText(R.string.word_pay_complete)
//                    holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_737373))
//
//                    holder.text_use.visibility = View.VISIBLE
//                    holder.text_use.setBackgroundResource(R.drawable.btn_buy_list_blue)
//                    holder.text_use.setText(R.string.msg_use)
//
//                    holder.text_use.setOnClickListener {
//                        //사용처리
//                        val intent = Intent(holder.itemView.context, CheckAuthCodeActivity::class.java)
//                        intent.putExtra(Const.DATA, item)
//                        intent.putExtra(Const.POSITION, holder.adapterPosition)
//                        (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_USE)
//                    }
//                }
//
//
//            }
//            EnumData.BuyGoodsProcess.CANCEL.process, EnumData.BuyGoodsProcess.BIZ_CANCEL.process, EnumData.BuyGoodsProcess.REFUND.process -> {
//                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.cancelDatetime)
//                    date = output.format(d)
//                }
//
//                holder.text_status.setText(R.string.word_cancel_complete)
//                holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_b7b7b7))
//                holder.text_use.visibility = View.GONE
//            }
//            EnumData.BuyGoodsProcess.USE.process -> {
//                if (StringUtils.isNotEmpty(item.buyGoodsList!![0].useDatetime)) {
//                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.buyGoodsList!![0].useDatetime)
//                    date = output.format(d)
//                }
//
//                holder.text_use.visibility = View.VISIBLE
//                holder.text_status.setText(R.string.word_use_complete)
//                holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_b7b7b7))
//
//                if(item.isReviewExist != null && item.isReviewExist!!){
//                    holder.text_use.isEnabled = false
//                    holder.text_use.setBackgroundResource(R.drawable.btn_buy_list_gray_s)
//                    holder.text_use.setText(R.string.word_review_write_complete)
//                }else{
//                    holder.text_use.isEnabled = true
//                    holder.text_use.setBackgroundResource(R.drawable.btn_buy_list_blue_s)
//                    holder.text_use.setText(R.string.word_review_write)
//                    holder.text_use.setOnClickListener {
//                        val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
//                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                        intent.putExtra(Const.BUY, item)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//                    }
//                }
//
//            }
//            EnumData.BuyGoodsProcess.CANCEL_WAIT.process, EnumData.BuyGoodsProcess.REFUND_WAIT.process -> {
//
////                if (StringUtils.isNotEmpty(item.buyGoodsList!![0].cancelDatetime)) {
////                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.buyGoodsList!![0].cancelDatetime)
////                    date = output.format(d)
////                }
//
//                holder.text_use.visibility = View.GONE
//                holder.text_status.setText(R.string.word_buy_cancel)
//                holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_b7b7b7))
//            }
//            EnumData.BuyGoodsProcess.EXPIRE_WAIT.process -> {
//
////                if (StringUtils.isNotEmpty(item.buyGoodsList!![0].expireDatetime)) {
////                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.buyGoodsList!![0].expireDatetime)
////                    date = output.format(d)
////                }
//
//                holder.text_use.visibility = View.GONE
//                holder.text_status.setText(R.string.word_expire_wait)
//                holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_b7b7b7))
//            }
//            EnumData.BuyGoodsProcess.EXPIRE.process -> {
////                if (StringUtils.isNotEmpty(item.buyGoodsList!![0].expireDatetime)) {
////                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.buyGoodsList!![0].expireDatetime)
////                    date = output.format(d)
////                }
//
//                holder.text_use.visibility = View.GONE
//                holder.text_status.setText(R.string.word_expire)
//                holder.text_status.setTextColor(ResourceUtil.getColor(mContext, R.color.color_b7b7b7))
//            }
//        }
//
//        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.payDatetime)
//        date = output.format(d)
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
//    private fun use(buy: Buy, position: Int){
//        val params = HashMap<String, String>()
//        params["buySeqNo"] = buy.seqNo.toString()
//        (mContext as BaseActivity).showProgress("")
//        ApiBuilder.create().buyGoodsListUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                if (mContext == null) {
//                    return
//                }
//
//                (mContext as BaseActivity).hideProgress()
//
//                ToastUtil.showAlert(mContext!!, mContext!!.getString(R.string.msg_use_complete))
//                getBuy(buy.seqNo!!, position)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                (mContext as BaseActivity).hideProgress()
//            }
//        }).build().call()
//    }
//
//    fun getBuy(seqNo: Long, position: Int) {
//        val params = HashMap<String, String>()
//        params["seqNo"] = seqNo.toString()
//        (mContext as BaseActivity).showProgress("")
//        ApiBuilder.create().getOneBuyDetail(params).setCallback(object : PplusCallback<NewResultResponse<Buy>> {
//            override fun onResponse(call: Call<NewResultResponse<Buy>>?, response: NewResultResponse<Buy>?) {
//                (mContext as BaseActivity).hideProgress()
//                if (response!!.data != null) {
//                    mDataList!![position] = response.data
//                    notifyItemChanged(position)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Buy>>?, t: Throwable?, response: NewResultResponse<Buy>?) {
//                (mContext as BaseActivity).hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_history, parent, false)
//        return ViewHolder(v)
//    }
//}