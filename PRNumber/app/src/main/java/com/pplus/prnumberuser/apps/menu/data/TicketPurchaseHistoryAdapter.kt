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
import com.pplus.prnumberuser.apps.menu.ui.AlertCancelOrderActivity
import com.pplus.prnumberuser.apps.menu.ui.OrderMenuReviewRegActivity
import com.pplus.prnumberuser.apps.menu.ui.OrderPurchaseHistoryActivity
import com.pplus.prnumberuser.apps.menu.ui.TicketPurchasePgActivity
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.model.dto.Cart
import com.pplus.prnumberuser.core.network.model.dto.CartOption
import com.pplus.prnumberuser.core.network.model.dto.OrderPurchase
import com.pplus.prnumberuser.databinding.ItemTicketPurchaseHistoryBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class TicketPurchaseHistoryAdapter() : RecyclerView.Adapter<TicketPurchaseHistoryAdapter.ViewHolder>() {

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

    class ViewHolder(binding: ItemTicketPurchaseHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_date = binding.textTicketPurchaseHistoryDate
        val text_status = binding.textTicketPurchaseHistoryStatus
        val image_thumbnail = binding.imageTicketPurchaseHistoryPageThumbnail
        val text_page_name = binding.textTicketPurchaseHistoryPageName
        val text_title = binding.textTicketPurchaseHistoryTitle
        val text_cancel = binding.textTicketPurchaseHistoryCancel
        val text_re_order = binding.textTicketPurchaseHistoryReOrder
        val text_review = binding.textTicketPurchaseHistoryReview

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

        when (item.status) {
            EnumData.OrderPurchaseStatus.PAY.status, EnumData.OrderPurchaseStatus.AFTER_PAY.status, EnumData.OrderPurchaseStatus.CONFIRM.status -> {
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

                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_373c42))
                when (item.statusTicket) {
                    4-> {
                        holder.text_status.setText(R.string.msg_req_ing_use)
                    }
                    else ->{
                        holder.text_status.setText(R.string.word_use_ready)
                    }
                }
            }
            EnumData.OrderPurchaseStatus.CANCEL_REQ.status -> {
                holder.text_status.setText(R.string.word_cancel_request)
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4026))
            }
            EnumData.OrderPurchaseStatus.CANCEL_COMPLETE.status -> {
                if (StringUtils.isNotEmpty(item.cancelDatetime)) {
                    val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.cancelDatetime)
                    date = output.format(d)
                }

                holder.text_status.setText(R.string.word_buy_cancel)
                holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4026))
            }
            EnumData.OrderPurchaseStatus.COMPLETE.status -> {


                when (item.statusTicket) { // 티켓 0:접수대기, 1:접수완료, 2:취소, 3:기간만료, 99:사용완료

                    3 ->{
                        holder.text_status.setText(R.string.word_expire)
                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4026))
                    }
                    99->{
                        holder.text_status.setText(R.string.word_use_complete)
                        holder.text_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_4694fb))

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

                            val intent = Intent(holder.itemView.context, TicketPurchasePgActivity::class.java)
                            intent.putParcelableArrayListExtra(Const.DATA, cartList)
                            intent.putExtra(Const.PAGE, item.page)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            launcher?.launch(intent)
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
        val binding = ItemTicketPurchaseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}