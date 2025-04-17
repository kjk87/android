//package com.pplus.prnumberuser.apps.plus.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.BusProviderData
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Plus
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import com.pplus.utils.BusProvider
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.item_list_page.view.*
//import retrofit2.Call
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class MainPlusAdapter : RecyclerView.Adapter<MainPlusAdapter.ViewHolder> {
//
//    var mDataList: MutableList<Plus>? = null
//    var listener: OnItemClickListener? = null
//    var mIsAlarm = false
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//
//        fun onRefresh()
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
//    fun getItem(position: Int): Plus {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<Plus>? {
//
//        return mDataList
//    }
//
//    fun add(data: Plus) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Plus>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Plus>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Plus>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Plus) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Plus>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Plus>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_list_page
//        val text_name = itemView.text_list_page_name
//        val text_description = itemView.text_list_page_introduce
//        val text_distance = itemView.text_list_page_distance
//        val text_reward = itemView.text_list_page_point
//        val text_number = itemView.text_list_page_number
//        val image_plus = itemView.image_list_page_plus
//        val text_plus_info = itemView.text_list_page_plus_info
//        val text_main_product = itemView.text_list_page_main_product
//
//        init {
//            text_number.visibility = View.VISIBLE
//            text_reward.visibility = View.GONE
//            text_distance.visibility = View.GONE
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//
//        holder.text_name.text = item.name!!
//
//        if (StringUtils.isNotEmpty(item.thumbnail)) {
//            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.prnumber_default_img)
//        }
//
////        holder.text_count.text = FormatUtil.getMoneyType(item.plusCount.toString())
//
//        if (StringUtils.isNotEmpty(item.catchphrase)) {
//            holder.text_description.text = item.catchphrase
//        } else {
//            holder.text_description.text = ""
//        }
//
////        val distance = item.distance
////        if (distance != null) {
////            holder.text_distance.visibility = View.VISIBLE
////            var strDistance: String? = null
////            if (distance > 1) {
////                strDistance = String.format("%.2f", distance) + "km"
////            } else {
////                strDistance = (distance * 1000).toInt().toString() + "m"
////            }
////            holder.text_distance.text = strDistance
////        } else {
////            holder.text_distance.visibility = View.GONE
////        }
//
////        if(item.point != null && item.point!! > 0){
////            holder.text_reward.visibility = View.VISIBLE
////            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.point!!}%"))
////        }else{
////            holder.text_reward.visibility = View.GONE
////        }
//
//
//        if (item.numberList != null && item.numberList!!.isNotEmpty()) {
//            val number = item.numberList!![0].number
//            holder.text_number.visibility = View.VISIBLE
//            holder.text_number.text = number
//
////            if (StringUtils.isNotEmpty(mSearchNumber) && number.contains(mSearchNumber)) {
////                val spannable = SpannableString(number)
////                val pos = number.indexOf(mSearchNumber)
////                spannable.setSpan(ForegroundColorSpan(Color.parseColor("#579ffb")), pos, pos + mSearchNumber.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////                spannable.setSpan(StyleSpan(Typeface.BOLD), pos, pos + mSearchNumber.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////                holder.text_number.text = spannable
////            }
//        } else {
//            holder.text_number.visibility = View.GONE
//        }
//
//        if (StringUtils.isNotEmpty(item.plusInfo)) {
//            holder.text_plus_info.visibility = View.VISIBLE
//        } else {
//            holder.text_plus_info.visibility = View.GONE
//        }
//
//        if (item.plus == null) {
//            item.plus = true
//        }
//
//        holder.image_plus.isSelected = item.plus!!
//
//        holder.image_plus.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(holder.itemView.context as BaseActivity)) {
//                return@setOnClickListener
//            }
//            if (!item.plus!!) {
//
//                val params = Plus()
//                params.no = item.no
//                (holder.itemView.context as BaseActivity).showProgress("")
//                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
//
//                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {
//
//                        if (holder.itemView.context != null) {
//                            (holder.itemView.context as BaseActivity).hideProgress()
//                            ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.msg_plus_ing))
//                            item.plus = true
//                            val bus = BusProviderData()
//                            bus.subData = item
//                            bus.type = BusProviderData.BUS_MAIN
//                            BusProvider.getInstance().post(bus)
//                            notifyItemChanged(holder.adapterPosition)
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {
//
//                        (holder.itemView.context as BaseActivity).hideProgress()
//                    }
//                }).build().call()
//            } else {
//
//                if (item.plusGiftReceived != null && item.plusGiftReceived!!) {
//                    ToastUtil.showAlert(holder.itemView.context, holder.itemView.context.getString(R.string.format_can_not_plus_released, item.name))
//                } else {
//
//                    val builder = AlertBuilder.Builder()
//                    builder.setTitle(holder.itemView.context.getString(R.string.word_notice_alert))
//                    builder.addContents(AlertData.MessageData(holder.itemView.context.getString(R.string.format_msg_question_cancel_plus, item.name), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                    builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel)).setRightText(holder.itemView.context.getString(R.string.word_confirm))
//                    builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                            when (event_alert) {
//                                AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                    val params = HashMap<String, String>()
//                                    params["no"] = "" + item.no!!
//                                    (holder.itemView.context as BaseActivity).showProgress("")
//                                    ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//                                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                                            if (holder.itemView.context != null) {
//                                                (holder.itemView.context as BaseActivity).hideProgress()
//                                                ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.msg_plus_released))
//                                                item.plus = false
//                                                val bus = BusProviderData()
//                                                bus.subData = item
//                                                bus.type = BusProviderData.BUS_MAIN
//                                                BusProvider.getInstance().post(bus)
//                                                notifyItemChanged(holder.adapterPosition)
//                                            }
//                                        }
//
//                                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                                            (holder.itemView.context as BaseActivity).showProgress("")
//                                        }
//                                    }).build().call()
//                                }
//                            }
//                        }
//                    }).builder().show(holder.itemView.context)
//
//                }
//            }
//
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position, it)
//            }
//        }
//
//    }
//
//    private fun updatePushActivate(context: Context, item: Plus, position: Int) {
//        val params = HashMap<String, String>()
//        params["plusNo"] = item.plusNo.toString()
//        params["pushActivate"] = (!item.pushActivate!!).toString()
//        (context as BaseActivity).showProgress("")
//        ApiBuilder.create().updatePushActivate(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                (context as BaseActivity).hideProgress()
//
//                if (item.pushActivate!!) {
//                    ToastUtil.show(context, context.getString(R.string.msg_not_receive_alarm))
//                } else {
//                    ToastUtil.show(context, context.getString(R.string.msg_receive_alarm))
//                }
//
//                item.pushActivate = !item.pushActivate!!
//                notifyItemChanged(position)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                (context as BaseActivity).hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_page, parent, false)
//        return ViewHolder(v)
//    }
//}