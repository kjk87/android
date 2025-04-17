package com.pplus.prnumberuser.apps.menu.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.menu.ui.*
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.CartOption
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.databinding.ItemOrderPurchaseHistoryBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class OrderPurchaseHistoryAdapter() : RecyclerView.Adapter<OrderPurchaseHistoryAdapter.ViewHolder>() {

    var mDataList: MutableList<OrderPurchase>? = null
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

    fun getItem(position: Int): OrderPurchase {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<OrderPurchase>? {

        return mDataList
    }

    fun add(data: OrderPurchase) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<OrderPurchase>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: OrderPurchase) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<OrderPurchase>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemOrderPurchaseHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_sales_type = binding.textOrderPurchaseHistorySalesType
        val text_date = binding.textOrderPurchaseHistoryDate
        val text_status = binding.textOrderPurchaseHistoryStatus
        val text_remain_time = binding.textOrderPurchaseHistoryRemainTime
        val image_thumbnail = binding.imageOrderPurchaseHistoryPageThumbnail
        val text_page_name = binding.textOrderPurchaseHistoryPageName
        val text_title = binding.textOrderPurchaseHistoryTitle
        val text_cancel = binding.textOrderPurchaseHistoryCancel
        val text_re_order = binding.textOrderPurchaseHistoryReOrder
        val text_review = binding.textOrderPurchaseHistoryReview

        init {
            text_page_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_page_name.visibility = View.VISIBLE
        holder.text_page_name.text = item.page!!.name
        Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.bg_f1f2f4_radius_6).error(R.drawable.bg_f1f2f4_radius_6)).into(holder.image_thumbnail)

        holder.text_title.text = item.title
        holder.text_review.visibility = View.GONE
        holder.text_re_order.visibility = View.GONE
        holder.text_cancel.visibility = View.GONE

        var date = ""
        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
        holder.text_remain_time.visibility = View.GONE
        when (item.salesType) {
            1 -> { //방문
                holder.text_sales_type.setBackgroundResource(R.drawable.bg_ea726e_radius_15)
                holder.text_sales_type.text = holder.itemView.context.getString(R.string.word_visit_order)
            }
            2 -> { //배달
                holder.text_sales_type.setBackgroundResource(R.drawable.bg_69bd87_radius_15)
                holder.text_sales_type.text = holder.itemView.context.getString(R.string.word_delivery_order)
            }
            5 -> { //포장
                holder.text_sales_type.setBackgroundResource(R.drawable.bg_9973eb_radius_15)
                holder.text_sales_type.text = holder.itemView.context.getString(R.string.word_package_order)
            }
        }

        when (item.status) {
            EnumData.OrderPurchaseStatus.PAY.status, EnumData.OrderPurchaseStatus.AFTER_PAY.status -> {
                holder.text_status.setText(R.string.word_before_confirm)
                holder.text_cancel.visibility = View.VISIBLE
                holder.text_cancel.setOnClickListener {
                    val intent = Intent(holder.itemView.context, AlertCancelOrderActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    if(holder.itemView.context is OrderPurchaseHistoryActivity){
                        (holder.itemView.context as OrderPurchaseHistoryActivity).launcher.launch(intent)
                    }else{
                        launcher?.launch(intent)
                    }
                }
            }
            EnumData.OrderPurchaseStatus.CONFIRM.status -> {
                holder.text_status.setText(R.string.word_order_confirm)

                when (item.salesType) {
                    1 -> { //방문
                        when (item.statusShop) { // 매장 0:접수대기, 1:접수완료, 2:취소, 99:사용완료
                            1 -> {
                                holder.text_status.setText(R.string.word_order_confirm)
                            }
                            99 -> {
                                holder.text_status.setText(R.string.word_visit_complete)
                            }
                        }
                    }
                    2 -> { //배달
                        holder.text_remain_time.visibility = View.VISIBLE
                        when (item.statusRider) { // 배달 0:접수대기, 1:접수완료, 2:배달취소, 3:기사배정  4:배달중(기사픽업), 99:배달완료
                            1,3 -> {
                                holder.text_status.setText(R.string.word_order_confirm)
                            }
                            4 -> {
                                holder.text_status.setText(R.string.word_delivery_ing)
                            }
                            99 -> {
                                holder.text_status.setText(R.string.word_delivery_complete)
                                holder.text_remain_time.visibility = View.GONE
                            }
                        }

                        if(item.riderTime != null && StringUtils.isNotEmpty(item.receiptDatetime)){
                            holder.text_remain_time.visibility = View.VISIBLE
                            val regDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.receiptDatetime)
                            val expectTime = regDate.time + (item.riderTime!!*60*1000)
                            val currentTime = System.currentTimeMillis()
                            val remainTime = expectTime - currentTime
                            if(remainTime > 0){
                                var remainMinute = remainTime/1000/60
                                if(remainMinute == 0L){
                                    remainMinute = 1
                                }
                                holder.text_remain_time.text = "(${holder.itemView.context.getString(R.string.format_remain_minute, remainMinute.toString())})"

                            }else{
                                holder.text_remain_time.text = "(${holder.itemView.context.getString(R.string.format_remain_minute, "0")})"
                            }
                        }else{
                            holder.text_remain_time.visibility = View.GONE
                        }
                    }
                    5 -> { //포장
                        when (item.statusPack) { // 포장 0:접수대기, 1:접수완료, 2:포장취소, 99:포장완료
                            1 -> {
                                holder.text_status.setText(R.string.word_order_confirm)
                            }
                            99 -> {
                                holder.text_status.setText(R.string.word_order_cancel)
                            }
                        }
                    }
                }
            }
            EnumData.OrderPurchaseStatus.CANCEL_REQ.status -> {
                holder.text_status.setText(R.string.word_cancel_request)
            }
            EnumData.OrderPurchaseStatus.CANCEL_COMPLETE.status -> {
                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.cancelDatetime)
                    date = output.format(d)
                }

                holder.text_status.setText(R.string.word_order_cancel)
            }
            EnumData.OrderPurchaseStatus.COMPLETE.status -> {

                when (item.salesType) {
                    1 -> { //방문
                        holder.text_status.setText(R.string.word_visit_complete)
                    }
                    2 -> { //배달
                        holder.text_status.setText(R.string.word_delivery_complete)
                    }
                    5 -> { //포장
                        holder.text_status.setText(R.string.word_package_complete)
                    }
                }

                holder.text_re_order.visibility = View.VISIBLE

                holder.text_re_order.setOnClickListener {

                    val cartList = arrayListOf<Cart>()
                    for (orderPurchaseMenu in item.orderPurchaseMenuList!!) {
                        val cart = Cart()
                        cart.amount = orderPurchaseMenu.amount
                        cart.memberSeqNo = LoginInfoManager.getInstance().user.no
                        cart.pageSeqNo = orderPurchaseMenu.orderMenu!!.pageSeqNo
                        cart.orderMenuSeqNo = orderPurchaseMenu.orderMenu!!.seqNo
                        cart.salesType = 2
                        cart.orderMenu = orderPurchaseMenu.orderMenu
                        val list = arrayListOf<CartOption>()
                        for (orderPurchaseMenuOption in orderPurchaseMenu.orderPurchaseMenuOptionList!!) {

                            val cartOption = CartOption()
                            cartOption.menuOptionDetailSeqNo = orderPurchaseMenuOption.menuOptionDetail!!.seqNo
                            cartOption.menuOptionDetail = orderPurchaseMenuOption.menuOptionDetail
                            cartOption.type = orderPurchaseMenuOption.type
                            list.add(cartOption)
                        }

                        cart.cartOptionList = list
                        cartList.add(cart)
                    }

                    val intent = Intent(holder.itemView.context, OrderPurchasePgActivity::class.java)
                    intent.putExtra(Const.TYPE, item.salesType)
                    intent.putParcelableArrayListExtra(Const.DATA, cartList)
                    intent.putExtra(Const.PAGE, item.page)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    if(holder.itemView.context is OrderPurchaseHistoryActivity){
                        (holder.itemView.context as OrderPurchaseHistoryActivity).launcher.launch(intent)
                    }else{
                        launcher?.launch(intent)
                    }
                }

                if (item.isReviewExist != null && item.isReviewExist!!) {
                    holder.text_review.visibility = View.GONE
                } else {
                    holder.text_review.visibility = View.VISIBLE
                    holder.text_review.setText(R.string.word_review_write)

                    holder.text_review.setOnClickListener {
                        val intent = Intent(holder.itemView.context, OrderMenuReviewRegActivity::class.java)
                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                        intent.putExtra(Const.ORDER_PURCHASE, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        if(holder.itemView.context is OrderPurchaseHistoryActivity){
                            (holder.itemView.context as OrderPurchaseHistoryActivity).launcher.launch(intent)
                        }else{
                            launcher?.launch(intent)
                        }
                    }

                }
            }
        }

        val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
        date = output.format(d)
        if (StringUtils.isNotEmpty(date)) {
            holder.text_date.text = date
            holder.text_date.visibility = View.VISIBLE
        } else {
            holder.text_date.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderPurchaseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}