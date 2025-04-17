package com.pplus.prnumberbiz.apps.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.customer.ui.CustomerDirectRegActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Customer
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_customer.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CustomerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Customer>? = null
    var listener: OnItemClickListener? = null
    var deleteListener: ItemDeleteListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    interface ItemDeleteListener{
        fun onItemDelete(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun setOnDeleteListener(listener: ItemDeleteListener) {

        this.deleteListener = listener
    }

    fun getItem(position: Int): Customer {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Customer>? {

        return mDataList
    }

    fun add(data: Customer) {

        if (mDataList == null) {
            mDataList = ArrayList<Customer>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Customer>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Customer>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Customer) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Customer>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Customer>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_customer_profile
        val text_name = itemView.text_customer_name
        val text_number = itemView.text_customer_number
        val image_more = itemView.image_customer_more

        init {
//            itemView.setPadding(itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0, itemView.context.resources.getDimensionPixelSize(R.dimen.width_88), 0)
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Customer = mDataList!!.get(position);

        holder.text_name.text = item.name

        if (item.target != null && item.target.profileImage != null) {
            Glide.with(mContext!!).load(item.target.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_gift_profile_default)
        }

        holder.text_number.text = FormatUtil.getPhoneNumber(item.mobile)

        holder.image_more.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM)
            builder.setContents(*arrayOf(mContext?.getString(R.string.word_modified), mContext?.getString(R.string.word_delete)))
            builder.setLeftText(mContext?.getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.LIST -> {
                            var intent: Intent? = null
                            when (event_alert.getValue()) {
                                1 -> {
                                    intent = Intent(mContext, CustomerDirectRegActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    (mContext as Activity).startActivityForResult(intent, Const.REQ_GROUP_CONFIG)
                                }
                                2 -> {
                                    val builder = AlertBuilder.Builder()
                                    builder.setTitle(mContext?.getString(R.string.word_notice_alert))
                                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                                    builder.addContents(AlertData.MessageData(mContext?.getString(R.string.format_msg_delete_group2, item.name), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                                    builder.setLeftText(mContext?.getString(R.string.word_cancel)).setRightText(mContext?.getString(R.string.word_confirm))
                                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                                        override fun onCancel() {

                                        }

                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                            when (event_alert) {
                                                AlertBuilder.EVENT_ALERT.RIGHT//그룹삭제
                                                -> {
                                                    val params = HashMap<String, String>()
                                                    params["no"] = "" + item.no!!
                                                    params["page.no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
                                                    (mContext as BaseActivity).showProgress("")
                                                    ApiBuilder.create().deleteCustomer(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                                                            (mContext as BaseActivity).hideProgress()
                                                            ToastUtil.showAlert(mContext, R.string.msg_delete_complete)
                                                            mDataList!!.removeAt(holder.adapterPosition)
                                                            notifyItemRemoved(holder.adapterPosition)
                                                            if(deleteListener != null){
                                                                deleteListener!!.onItemDelete(holder.adapterPosition)
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                                                            (mContext as BaseActivity).hideProgress()
                                                        }
                                                    }).build().call()
                                                }
                                            }
                                        }
                                    }).builder().show(mContext)
                                }
                            }
                        }
                    }
                }
            }).builder().show(mContext)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(v)
    }
}