package com.pplus.prnumberuser.apps.plus.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.plus.ui.PlusInfoActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ItemPlusPageBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PlusAdapter : RecyclerView.Adapter<PlusAdapter.ViewHolder> {

    var mDataList: MutableList<Plus>? = null
    var listener: OnItemClickListener? = null
    var mIsAlarm = false

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)

        fun onRefresh()
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Plus {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Plus>? {

        return mDataList
    }

    fun add(data: Plus) {

        if (mDataList == null) {
            mDataList = ArrayList<Plus>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Plus>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Plus>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Plus) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Plus>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Plus>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemPlusPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imagePlusPage
        val text_name = binding.textPlusPageName
        val text_description = binding.textPlusPageIntroduce
        val image_plus = binding.imagePlusPagePlus
        val text_plus_info = binding.textPlusPagePlusInfo
        val text_reward = binding.textPlusPageReward
        val image_alarm = binding.imagePlusPageAlarm
        val text_main_product = binding.textPlusPageMainProduct

        init {
            text_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        if (mIsAlarm) {
            holder.image_plus.visibility = View.GONE
            holder.image_alarm.visibility = View.VISIBLE
        } else {
//            holder.layout_plus.visibility = View.VISIBLE
            holder.image_alarm.visibility = View.GONE
        }

        holder.text_name.text = item.name

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        holder.text_description.text = item.catchphrase

        if (item.point != null && item.point!! > 0) {
            holder.text_reward.visibility = View.VISIBLE
            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.point!!}%"))
        } else {
            holder.text_reward.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }


        if (item.plus == null) {
            item.plus = true
        }

//        if (StringUtils.isNotEmpty(item.plusInfo)) {
//            holder.text_plus_info.visibility = View.VISIBLE
//            holder.text_plus_info.setOnClickListener {
//                val intent = Intent(holder.itemView.context, PlusInfoActivity::class.java)
//                val page = Page()
//                page.no = item.no
//                intent.putExtra(Const.DATA, page)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_PLUS)
//                return@setOnClickListener
//            }
//        }else{
//            holder.text_plus_info.visibility = View.GONE
//        }

//        if(item.mainProductPriceSeqNo != null){
//            holder.text_main_product.visibility = View.VISIBLE
//            holder.text_main_product.setOnClickListener {
//                val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
//                val productPrice = ProductPrice()
//                productPrice.seqNo = item.mainProductPriceSeqNo
//                intent.putExtra(Const.DATA, productPrice)
//                holder.itemView.context.startActivity(intent)
//            }
//        }else{
//            holder.text_main_product.visibility = View.GONE
//        }

        if (item.plusGiftReceived != null && item.plusGiftReceived!!) {
            holder.image_plus.setImageResource(R.drawable.btn_number_plus_sel)
        }else{
            holder.image_plus.setImageResource(R.drawable.btn_number_plus_blue)
        }

        holder.image_plus.setOnClickListener {

            if (item.plus!!) {

                if (item.plusGiftReceived != null && item.plusGiftReceived!!) {
                    ToastUtil.showAlert(holder.itemView.context, holder.itemView.context.getString(R.string.format_can_not_plus_released, item.name))
                } else {

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.format_msg_question_cancel_plus, item.name), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val params = HashMap<String, String>()
                                    params["no"] = "" + item.no!!
                                    (holder.itemView.context as BaseActivity).showProgress("")
                                    ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                                            (holder.itemView.context as BaseActivity).hideProgress()
                                            listener?.onRefresh()
//                                        item.plus = false
//                                        notifyItemChanged(holder.adapterPosition)
                                        }

                                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                                            (holder.itemView.context as BaseActivity).hideProgress()
                                        }
                                    }).build().call()
                                }
                            }
                        }
                    }).builder().show(holder.itemView.context)

                }
            } else {

                if (StringUtils.isNotEmpty(item.plusInfo)) {
                    val intent = Intent(holder.itemView.context, PlusInfoActivity::class.java)
                    val page = Page()
                    page.no = item.no
                    intent.putExtra(Const.DATA, page)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_PLUS)
                    return@setOnClickListener
                }

                val params = Plus()
                params.no = item.no
                (holder.itemView.context as BaseActivity).showProgress("")
                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {

                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {

                        (holder.itemView.context as BaseActivity).hideProgress()
                        ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.msg_plus_ing))
                        item.plus = true
                        notifyItemChanged(holder.adapterPosition)
                    }

                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {

                        (holder.itemView.context as BaseActivity).hideProgress()
                    }
                }).build().call()
            }

        }

        holder.image_alarm.isSelected = (item.pushActivate != null && item.pushActivate!!) && (LoginInfoManager.getInstance().user.plusPush != null && LoginInfoManager.getInstance().user.plusPush!!)
        holder.image_alarm.setOnClickListener {
            updatePushActivate(holder.itemView.context, item, holder.adapterPosition)
        }

    }

    private fun updatePushActivate(context: Context, item: Plus, position: Int) {
        val params = HashMap<String, String>()
        params["plusNo"] = item.plusNo.toString()
        params["pushActivate"] = (!item.pushActivate!!).toString()
        (context as BaseActivity).showProgress("")
        ApiBuilder.create().updatePushActivate(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                (context as BaseActivity).hideProgress()

                if (item.pushActivate!!) {
                    ToastUtil.show(context, context.getString(R.string.msg_not_receive_alarm))
                } else {
                    ToastUtil.show(context, context.getString(R.string.msg_receive_alarm))
                }

                item.pushActivate = !item.pushActivate!!
                notifyItemChanged(position)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                (context as BaseActivity).hideProgress()
            }
        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlusPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}