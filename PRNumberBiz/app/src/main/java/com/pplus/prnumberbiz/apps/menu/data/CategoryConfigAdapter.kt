package com.pplus.prnumberbiz.apps.menu.data

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.menu.ui.AlertCategoryRegActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_category_config.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CategoryConfigAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryConfigAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<PageGoodsCategory>? = null
    var listener: OnItemClickListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun onItemChanged()
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

    fun getItem(position: Int): PageGoodsCategory {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<PageGoodsCategory>? {

        return mDataList
    }

    fun add(data: PageGoodsCategory) {

        if (mDataList == null) {
            mDataList = ArrayList<PageGoodsCategory>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<PageGoodsCategory>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<PageGoodsCategory>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: PageGoodsCategory) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<PageGoodsCategory>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<PageGoodsCategory>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_name = itemView.text_category_config_name
        val image_more = itemView.image_category_config_more

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_name.text = item.goodsCategory!!.name


        holder.image_more.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST -> {
                            when (event_alert.value) {
                                1 -> {
                                    val intent = Intent(holder.itemView.context, AlertCategoryRegActivity::class.java)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                }
                                2 -> {
                                    delete(item)
                                }
                            }

                        }
                    }
                }
            }).builder().show(holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition)
            }
        }
    }

    private fun delete(item: PageGoodsCategory) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_question_delete_category), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = item.seqNo.toString()
                        (mContext!! as BaseActivity).showProgress("")
                        ApiBuilder.create().deletePageGoodsCategory(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext!! as BaseActivity).hideProgress()
                                if (listener != null) {
                                    listener!!.onItemChanged()
                                }
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext!! as BaseActivity).hideProgress()

                                if (response!!.resultCode == 702) {
                                    ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_default_category)
                                }

                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_config, parent, false)
        return ViewHolder(v)
    }
}