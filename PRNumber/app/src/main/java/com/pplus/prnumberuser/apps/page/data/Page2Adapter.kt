//package com.pplus.prnumberuser.apps.page.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.BusProviderData
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Page2
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.item_list_page.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class Page2Adapter : RecyclerView.Adapter<Page2Adapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Page2>? = null
//    var listener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Page2 {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Page2>? {
//
//        return mDataList
//    }
//
//    fun add(data: Page2) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Page2>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Page2>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Page2>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Page2) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Page2>()
//        notifyDataSetChanged()
//    }
//
//    fun setBusPlus(data: BusProviderData) {
//        if (mDataList != null) {
//            val page = data.subData
//            if (page is Page2) {
//                for (i in 0 until mDataList!!.size - 1) {
//                    if (mDataList!![i] == page) {
//                        mDataList!![i] = page
//                        break
//                    }
//                }
//            }
//            notifyDataSetChanged()
//        }
//
//    }
//
//    fun setDataList(dataList: MutableList<Page2>) {
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
////        val text_number = itemView.text_list_page_number
////        val image_plus = itemView.image_list_page_plus
//
//        init {
//
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
//            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.img_square_profile_default)
//        }
//
////        if(item.virtualPage == "Y"){
////            holder.image_plus.visibility = View.GONE
////        }else{
////            holder.image_plus.visibility = View.VISIBLE
////        }
//
////        holder.text_count.text = FormatUtil.getMoneyType(item.plusCount.toString())
//
//        if (StringUtils.isNotEmpty(item.catchphrase)) {
//            holder.text_description.text = item.catchphrase
//        } else {
//            holder.text_description.text = ""
//        }
//
////        if (item.numberList != null && item.numberList!!.isNotEmpty()) {
////            holder.text_number.visibility = View.VISIBLE
////            val number = item.numberList!![0].virtualNumber!!.virtualNumber
////            holder.text_number.text = PplusNumberUtil.getPrNumberFormat(number)
////        } else {
////            holder.text_number.visibility = View.GONE
////        }
//
//        val distance = item.distance
//        if (distance != null) {
//            holder.text_distance.visibility = View.VISIBLE
//            var strDistance: String? = null
//            if (distance > 1) {
//                strDistance = String.format("%.2f", distance) + "km"
//            } else {
//                strDistance = (distance * 1000).toInt().toString() + "m"
//            }
//            holder.text_distance.text = strDistance
//        } else {
//            holder.text_distance.visibility = View.GONE
//        }
//
//        if(item.point != null && item.point!! > 0){
//            holder.text_reward.visibility = View.VISIBLE
//            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.point!!}%"))
//        }else{
//            holder.text_reward.visibility = View.GONE
//        }
//
////        if (item.plus == null) {
////            item.plus = false
////        }
////        holder.image_plus.isSelected = item.plus!!
////
////        holder.image_plus.setOnClickListener {
////            if (!item.plus!!) {
////                val params = Plus()
////                params.no = item.seqNo
////                (mContext as BaseActivity).showProgress("")
////                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
////
////                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {
////
////                        if (mContext != null) {
////                            (mContext as BaseActivity).hideProgress()
////                            ToastUtil.show(mContext!!, mContext!!.getString(R.string.msg_plus_ing))
////                            item.plus = true
////                            val bus = BusProviderData()
////                            bus.subData = item
////                            bus.type = BusProviderData.BUS_MAIN
////                            BusProvider.getInstance().post(bus)
////                            notifyItemChanged(holder.adapterPosition)
////                        }
////
////                    }
////
////                    override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {
////
////                        (mContext as BaseActivity).hideProgress()
////                    }
////                }).build().call()
////            } else {
////                val params = HashMap<String, String>()
////                params["no"] = "" + item.seqNo
////                (mContext as BaseActivity).showProgress("")
////                ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
////
////                    override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
////
////                        if (mContext != null) {
////                            (mContext as BaseActivity).hideProgress()
////                            ToastUtil.show(mContext!!, mContext!!.getString(R.string.msg_plus_released))
////                            item.plus = false
////                            val bus = BusProviderData()
////                            bus.subData = item
////                            bus.type = BusProviderData.BUS_MAIN
////                            BusProvider.getInstance().post(bus)
////                            notifyItemChanged(holder.adapterPosition)
////                        }
////                    }
////
////                    override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
////
////                        (mContext as BaseActivity).showProgress("")
////                    }
////                }).build().call()
////            }
////
////        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position, it)
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_page, parent, false)
//        return ViewHolder(v)
//    }
//}