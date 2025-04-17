package com.pplus.prnumberbiz.apps.coupon.data

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.coupon.ui.CouponConfigDetailActivity
import com.pplus.prnumberbiz.apps.coupon.ui.CouponRegActivity
import com.pplus.prnumberbiz.apps.coupon.ui.CouponSaleHistoryActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_coupon_config.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CouponConfigAdapter : RecyclerView.Adapter<CouponConfigAdapter.ViewHolder> {

    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun onRefresh()
    }

    constructor(context: Context) : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Goods {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Goods>? {

        return mDataList
    }

    fun add(data: Goods) {

        if (mDataList == null) {
            mDataList = ArrayList<Goods>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Goods>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Goods>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Goods) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Goods>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Goods>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text_reg_date = itemView.text_coupon_reg_date
        val text_config = itemView.text_coupon_config
        val layout_coupon = itemView.layout_coupon
        val image = itemView.image_coupon_page_image
        val text_name = itemView.text_coupon_name
        val text_origin_price = itemView.text_coupon_origin_price
        val text_sale_price = itemView.text_coupon_sale_price
        val view_sold_status = itemView.view_coupon_sold_status
        val text_sold_status = itemView.text_coupon_sold_status
        val text_expire_date = itemView.text_coupon_expire_date
//        var text_send = itemView.text_goods_send
//        var view_bar2 = itemView.view_goods_bar2

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            view_bar.visibility = View.GONE
//            text_send.visibility = View.VISIBLE
//            view_bar2.visibility = View.VISIBLE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        var ing = false

        if (item.status == EnumData.GoodsStatus.soldout.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_out)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
        } else if (item.status == EnumData.GoodsStatus.finish.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_finish)
            holder.text_sold_status.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.white))
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
        } else if (item.status == EnumData.GoodsStatus.stop.status) {
            holder.view_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.visibility = View.VISIBLE
            holder.text_sold_status.setText(R.string.word_sold_stop)
            holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_stop)
        } else {

            //"expireDatetime":"2018-10-18 15:00:59"

            if (StringUtils.isNotEmpty(item.expireDatetime)) {
                val format = SimpleDateFormat(PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                var date = Date()
                try {
                    date = format.parse(item.expireDatetime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val currentMillis = System.currentTimeMillis()
                if (date.time <= currentMillis) {
                    holder.view_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.visibility = View.VISIBLE
                    holder.text_sold_status.setText(R.string.word_sold_finish)
                    holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_soldout)
                } else {
                    holder.view_sold_status.visibility = View.GONE
                    holder.text_sold_status.visibility = View.GONE
                    ing = true
                }
            } else {
                holder.view_sold_status.visibility = View.GONE
                holder.text_sold_status.visibility = View.GONE
                ing = true
            }
        }

        if (ing) {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_ff4646))
            if(item.represent != null && item.represent!!){
                holder.text_sold_status.visibility = View.VISIBLE
                holder.text_sold_status.setText(R.string.word_represent_coupon)
                holder.text_sold_status.setBackgroundResource(R.drawable.bg_coupon_representation)
            }else{
                holder.text_sold_status.visibility = View.GONE
            }
        } else {
            holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
        }

        if (StringUtils.isNotEmpty(item.expireDatetime)) {
            holder.text_expire_date.visibility = View.VISIBLE
            holder.text_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.expireDatetime)) + " " + holder.itemView.context.getString(R.string.word_until)
        }else{
            holder.text_expire_date.visibility = View.GONE
        }

        if (StringUtils.isNotEmpty(item.regDatetime)) {
            holder.text_reg_date.visibility = View.VISIBLE
            holder.text_reg_date.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
        }else{
            holder.text_reg_date.visibility = View.GONE
        }

        holder.text_name.text = item.name
        if (item.originPrice != null && item.originPrice!! > 0) {

            if (item.originPrice!! <= item.price!!) {
                holder.text_origin_price.visibility = View.GONE
            } else {
                holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                holder.text_origin_price.visibility = View.VISIBLE
            }

        } else {
            holder.text_origin_price.visibility = View.GONE
        }

        holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))


        if (LoginInfoManager.getInstance().user.page!!.profileImage != null) {
            Glide.with(holder.itemView.context).load(LoginInfoManager.getInstance().user.page!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_page_profile_circle_default).error(R.drawable.img_page_profile_circle_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_page_profile_circle_default)
        }

        holder.text_config.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)

            val contentsList = arrayListOf<String>()

            if(item.represent == null || !item.represent!!){
                builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_set_represent_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                contentsList.add(holder.itemView.context.getString(R.string.word_set_represent_coupon))
            }
            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_sale_history), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            contentsList.add(holder.itemView.context.getString(R.string.word_sale_history))
            if(item.status != EnumData.GoodsStatus.soldout.status){
                builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_sold_out), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                contentsList.add(holder.itemView.context.getString(R.string.word_sold_out))
            }

            when (item.status) {
                EnumData.GoodsStatus.ing.status -> {
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_sold_stop), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    contentsList.add(holder.itemView.context.getString(R.string.word_sold_stop))
                }
                EnumData.GoodsStatus.stop.status -> {
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_sold_resume), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                    contentsList.add(holder.itemView.context.getString(R.string.word_sold_resume))
                }
            }
            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_modified), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.word_delete), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            contentsList.add(holder.itemView.context.getString(R.string.word_modified))
            contentsList.add(holder.itemView.context.getString(R.string.word_delete))

            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST -> {
                            when(contentsList[event_alert.value-1]){
                                holder.itemView.context.getString(R.string.word_set_represent_coupon)->{
                                    setMainGoods(holder.itemView.context, item)
                                }
                                holder.itemView.context.getString(R.string.word_sale_history)->{
                                    val intent = Intent(holder.itemView.context, CouponSaleHistoryActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    (holder.itemView.context as BaseActivity).startActivity(intent)

                                }
                                holder.itemView.context.getString(R.string.word_sold_out)->{
                                    if(item.represent != null && item.represent!!){
                                        ToastUtil.showAlert(holder.itemView.context, R.string.msg_can_not_change_represent_coupon)
                                        return
                                    }
                                    putGoodsStatus(holder.itemView.context, item, EnumData.GoodsStatus.soldout.status)
                                }
                                holder.itemView.context.getString(R.string.word_sold_stop)->{
                                    if(item.represent != null && item.represent!!){
                                        ToastUtil.showAlert(holder.itemView.context, R.string.msg_can_not_change_represent_coupon)
                                        return
                                    }
                                    putGoodsStatus(holder.itemView.context, item, EnumData.GoodsStatus.stop.status)
                                }
                                holder.itemView.context.getString(R.string.word_sold_resume)->{
                                    putGoodsStatus(holder.itemView.context, item, EnumData.GoodsStatus.ing.status)
                                }
                                holder.itemView.context.getString(R.string.word_modified)->{
                                    val intent = Intent(holder.itemView.context, CouponRegActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                                }
                                holder.itemView.context.getString(R.string.word_delete)->{
                                    if(item.represent != null && item.represent!!){
                                        ToastUtil.showAlert(holder.itemView.context, R.string.msg_can_not_delete_represent_coupon)
                                        return
                                    }
                                    delete(holder.itemView.context, item)
                                }
                            }
                        }
                    }
                }
            }).builder().show(holder.itemView.context)
        }

        holder.layout_coupon.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    private fun putGoodsStatus(context: Context, goods: Goods, status : Int){
        val params = HashMap<String, String>()
        params["seqNo"] = goods.seqNo.toString()
        params["status"] = status.toString()
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().putGoodsStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()
                goods.status = params["status"]!!.toInt()
                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                notifyDataSetChanged()
            }
        }).build().call()
    }

    private fun setMainGoods(context: Context, goods: Goods){
        val params = HashMap<String, String>()
        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["mainGoodsSeqNo"] = goods.seqNo.toString()

        (context as BaseActivity).showProgress("")
        ApiBuilder.create().putMainGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                context.hideProgress()

                context.showAlert(R.string.msg_set_main_goods)
                LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo = goods.seqNo
                LoginInfoManager.getInstance().save()
                if(listener != null){
                    listener!!.onRefresh()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                context.hideProgress()
            }
        }).build().call()
    }

    private fun delete(context: Context, goods: Goods) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(context.getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(context.getString(R.string.msg_question_delete_coupon), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(context.getString(R.string.word_cancel)).setRightText(context.getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = goods.seqNo.toString()
                        (context as BaseActivity).showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                context.hideProgress()
                                ToastUtil.show(context, R.string.msg_deleted_coupon)
                                if (listener != null) {
                                    listener!!.onRefresh()
                                }
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                context.hideProgress()
                                ToastUtil.showAlert(context, R.string.msg_can_not_delete_history_coupon)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_config, parent, false)
        return ViewHolder(v)
    }
}