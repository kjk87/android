package com.pplus.prnumberuser.apps.main.data

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.page.ui.PageAttendanceActivity
import com.pplus.prnumberuser.apps.page.ui.PageSnsCashBackActivity
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemListPageBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class PageNumberAdapter : RecyclerView.Adapter<PageNumberAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: MutableList<Page>? = null
    var listener: OnItemClickListener? = null
    var mSearchNumber = ""


    interface OnItemClickListener {

        fun onItemClick(position: Int, view:View)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
    }

    fun setSearchNumber(number:String){
        mSearchNumber = number
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Page {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Page>? {

        return mDataList
    }

    fun add(data: Page) {

        if (mDataList == null) {
            mDataList = ArrayList<Page>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Page>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Page>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Page) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Page>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Page>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    fun setBusPlus(data: BusProviderData) {
        if (mDataList != null) {
            val page = data.subData
            if (page is Page) {
                for (i in 0 until mDataList!!.size - 1) {
                    if (mDataList!![i] == page) {
                        mDataList!![i] = page
                        break
                    }
                }
            }
            notifyDataSetChanged()
        }

    }

    class ViewHolder(binding: ItemListPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageListPage
        val text_name = binding.textListPageName
        val text_description = binding.textListPageIntroduce
        val text_distance = binding.textListPageDistance
        val text_number = binding.textListPageNumber
        val text_benefit = binding.textListPageBenefit
        val text_sns_point = binding.textListPageSnsPoint
        val text_visit_point = binding.textListPageVisitPoint

        init {
            text_number.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.name!!

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

//        holder.text_count.text = FormatUtil.getMoneyType(item.plusCount.toString())

        if(StringUtils.isNotEmpty(item.catchphrase)){
            holder.text_description.text = item.catchphrase
        }else{
            holder.text_description.text = ""
        }

        val distance = item.distance
        if (distance != null) {
            holder.text_distance.visibility = View.VISIBLE
            var strDistance: String? = null
            if (distance > 1) {
                strDistance = String.format("%.2f", distance) + "km"
            } else {
                strDistance = (distance * 1000).toInt().toString() + "m"
            }
            holder.text_distance.text = strDistance
        } else {
            holder.text_distance.visibility = View.GONE
        }

//        if(item.point != null && item.point!! > 0){
//            holder.text_reward.visibility = View.VISIBLE
//            holder.text_reward.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.point!!}%"))
//        }else{
//            holder.text_reward.visibility = View.GONE
//        }


        if (item.numberList != null && item.numberList!!.isNotEmpty()) {
            val number = item.numberList!![0].virtualNumber

            if (StringUtils.isNotEmpty(mSearchNumber) && number!!.contains(mSearchNumber)) {
                val spannable = SpannableString(number)
                val pos = number.indexOf(mSearchNumber)
                spannable.setSpan(ForegroundColorSpan(Color.parseColor("#579ffb")), pos, pos + mSearchNumber.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(StyleSpan(Typeface.BOLD), pos, pos + mSearchNumber.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.text_number.text = spannable
            }
        }



        if (item.plus == null) {
            item.plus = false
        }

        if(item.visitPoint != null && item.visitPoint!! > 0){
            holder.text_visit_point.visibility = View.VISIBLE
            holder.text_visit_point.text = holder.itemView.context.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(item.visitPoint.toString()))
            holder.text_visit_point.setOnClickListener {
                if(!PplusCommonUtil.loginCheck(holder.itemView.context as FragmentActivity, null)){
                    return@setOnClickListener
                }
//                val intent = Intent(holder.itemView.context, PageCashBackActivity::class.java)
                val intent = Intent(holder.itemView.context, PageAttendanceActivity::class.java)
                intent.putExtra(Const.DATA, item)
                holder.itemView.context.startActivity(intent)
            }
        }else{
            holder.text_visit_point.visibility = View.GONE
        }

//        if(StringUtils.isNotEmpty(item.benefit)){
//            holder.text_benefit.visibility = View.VISIBLE
//            holder.text_benefit.setOnClickListener {
//                if(!PplusCommonUtil.loginCheck(holder.itemView.context as FragmentActivity, null)){
//                    return@setOnClickListener
//                }
//                val intent = Intent(holder.itemView.context, PageFirstBenefitActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                holder.itemView.context.startActivity(intent)
//            }
//        }else{
//            holder.text_benefit.visibility = View.GONE
//        }
        holder.text_benefit.visibility = View.GONE
        if(item.useSns != null && item.useSns!!){
            holder.text_sns_point.visibility = View.VISIBLE
            holder.text_sns_point.setOnClickListener {
                if(!PplusCommonUtil.loginCheck(holder.itemView.context as FragmentActivity, null)){
                    return@setOnClickListener
                }
                val intent = Intent(holder.itemView.context, PageSnsCashBackActivity::class.java)
                intent.putExtra(Const.DATA, item)
                holder.itemView.context.startActivity(intent)
            }
        }else{
            holder.text_sns_point.visibility = View.GONE
        }
//        holder.layout_plus.isSelected = item.plus!!
//
//        holder.layout_plus.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(holder.itemView.context as BaseActivity)){
//                return@setOnClickListener
//            }
//
//            if(StringUtils.isNotEmpty(item.plusInfo)){
//                val intent = Intent(holder.itemView.context, PlusInfoActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//                return@setOnClickListener
//            }
//            if(!item.plus!!){
//                val params = Plus()
//                params.no = item.no
//                (mContext as BaseActivity).showProgress("")
//                ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {
//
//                    override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {
//
//                        if(mContext != null){
//                            (mContext as BaseActivity).hideProgress()
//                            ToastUtil.show(mContext!!, mContext!!.getString(R.string.msg_plus_ing))
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
//                        (mContext as BaseActivity).hideProgress()
//                    }
//                }).build().call()
//            }else{
//                val params = HashMap<String, String>()
//                params["no"] = "" + item.no!!
//                (mContext as BaseActivity).showProgress("")
//                ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//                    override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                        if(mContext != null){
//                            (mContext as BaseActivity).hideProgress()
//                            ToastUtil.show(mContext!!, mContext!!.getString(R.string.msg_plus_released))
//                            item.plus = false
//                            val bus = BusProviderData()
//                            bus.subData = item
//                            bus.type = BusProviderData.BUS_MAIN
//                            BusProvider.getInstance().post(bus)
//                            notifyItemChanged(holder.adapterPosition)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                        (mContext as BaseActivity).showProgress("")
//                    }
//                }).build().call()
//            }
//
//        }

        holder.itemView.setOnClickListener {
            if(listener != null){
                listener!!.onItemClick(position, it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}