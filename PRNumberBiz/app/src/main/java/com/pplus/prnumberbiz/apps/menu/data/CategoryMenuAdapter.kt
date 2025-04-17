package com.pplus.prnumberbiz.apps.menu.data

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.MenuRegActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_menu.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class CategoryMenuAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryMenuAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null
    var mKey = ""
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

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val layout_image = itemView.layout_image_menu
        val image = itemView.image_menu
        val text_name = itemView.text_menu_name
        val text_price = itemView.text_menu_price
        val image_more = itemView.image_menu_more

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_name.text = item.name

        holder.text_price.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))

        if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
            holder.layout_image.visibility = View.VISIBLE
            holder.image.visibility = View.VISIBLE
            if (mContext != null) {
                val imageNo = item.attachments!!.images!![0]
                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${imageNo}")
                Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
            }
        } else {
            holder.layout_image.visibility = View.GONE
        }

        if(mKey == Const.SELECT){
            holder.image_more.visibility = View.GONE
        }else{
            holder.image_more.visibility = View.VISIBLE
        }

        holder.image_more.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
            builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
            builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {

                        AlertBuilder.EVENT_ALERT.LIST ->{
                            when(event_alert.value){
                                1->{
                                    val intent = Intent(holder.itemView.context, MenuRegActivity::class.java)
                                    intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                    intent.putExtra(Const.DATA, item)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                                }
                                2->{
                                    deleteGoods(item)
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

    private fun deleteGoods(goods: Goods) {

        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_question_delete_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = goods.seqNo.toString()
                        (mContext!! as BaseActivity).showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext!! as BaseActivity).hideProgress()
                                if (listener != null) {
                                    listener!!.onItemChanged()
                                }
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext!! as BaseActivity).hideProgress()

//                                                                        if (response!!.resultCode == 698) {
//                                                                            showAlert(R.string.msg_can_not_delete_history_goods)
//                                                                        }

                                ToastUtil.showAlert(mContext, R.string.msg_can_not_delete_history_goods)
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(v)
    }
}