//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.content.Intent
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsReviewWriteActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_review_enable.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ReviewEnableAdapter : RecyclerView.Adapter<ReviewEnableAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<BuyGoods>? = null
//    var listener: OnItemClickListener? = null
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): BuyGoods {
//
//        return mDataList!!.get(position)
//    }
//
//    fun getDataList(): MutableList<BuyGoods>? {
//
//        return mDataList
//    }
//
//    fun add(data: BuyGoods) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<BuyGoods>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<BuyGoods>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<BuyGoods>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: BuyGoods) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<BuyGoods>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<BuyGoods>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_review_enable_goods_image
//        val text_goods_name = itemView.text_review_enable_goods_name
//        val text_date = itemView.text_review_enable_date
//        val text_review_write = itemView.text_review_enable_write
//
//        init {
//            text_goods_name.setSingleLine()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = mDataList!![position]
//
//        if (item.goods != null) {
//            if (item.goods!!.attachments != null && item.goods!!.attachments!!.images != null && item.goods!!.attachments!!.images!!.isNotEmpty()) {
//                if (mContext != null) {
//                    val id = item.goods!!.attachments!!.images!![0]
//
//                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
//                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//                }
//
//            } else {
//                holder.image.setImageResource(R.drawable.prnumber_default_img)
//            }
//
//            holder.text_goods_name.text = item.goods!!.name
//        }
//
//        var date = ""
//        val output = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        if (StringUtils.isNotEmpty(item.payDatetime)) {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.payDatetime)
//            date = output.format(d)
//        }
//        holder.text_date.text = date
//
//        holder.text_review_write.setOnClickListener {
//            val intent = Intent(holder.itemView.context, GoodsReviewWriteActivity::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            intent.putExtra(Const.BUY_GOODS, item)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REVIEW)
//        }
//
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_review_enable, parent, false)
//        return ViewHolder(v)
//    }
//}