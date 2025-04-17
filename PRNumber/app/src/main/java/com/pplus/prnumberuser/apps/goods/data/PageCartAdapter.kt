//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.SelectedGoodsManager
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import kotlinx.android.synthetic.main.item_goods_cart.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PageCartAdapter : RecyclerView.Adapter<PageCartAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mPageSeqNo : Long? = null
//    var mDataList: MutableList<BuyGoods>? = null
//    var listener: OnItemClickListener? = null
//    var checkListener: OnItemCheckListener? = null
//
//    internal var mTodayYear: Int = 0
//    internal var mTodayMonth: Int = 0
//    internal var mTodayDay: Int = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    interface OnItemCheckListener {
//        fun onItemCheck()
//    }
//
//    constructor(context: Context, pageSeqNo:Long) : super() {
//        this.mContext = context
//        this.mPageSeqNo = pageSeqNo
//        this.mDataList = ArrayList()
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setOnItemCheckListener(listener: OnItemCheckListener) {
//
//        this.checkListener = listener
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun setAllCheck(check: Boolean) {
//        for (i in 0 until mDataList!!.size) {
//            mDataList!![i].check = check
//        }
//        notifyDataSetChanged()
//    }
//
//    fun getItem(position: Int): BuyGoods {
//
//        return mDataList!![position]
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
//        val check = itemView.check_goods_cart
//        val image = itemView.image_goods_cart_image
//        val text_title = itemView.text_goods_cart_title
//        val text_count = itemView.text_goods_cart_count
//        val image_minus = itemView.image_goods_cart_minus
//        val image_plus = itemView.image_goods_cart_plus
//        val text_total_price = itemView.text_goods_cart_total_price
//
//        init {
//            text_title.setSingleLine()
//            text_total_price.setSingleLine()
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
//        holder.check.isChecked = item.check!!
//        holder.text_count.text = item.count.toString()
//
//        holder.image_minus.setOnClickListener {
//            if (item.count!! > 1) {
//                item.count = item.count!! - 1
//
//                SelectedGoodsManager.getInstance(mPageSeqNo!!).load().buyGoodsList = mDataList
//                SelectedGoodsManager.getInstance(mPageSeqNo!!).save()
//                notifyDataSetChanged()
//            }
//        }
//
//        holder.image_plus.setOnClickListener {
//
//            var maxCount = -1
//            if(item.goods!!.count != -1){
//                var soldCount = 0
//                if(item.goods!!.soldCount != null){
//                    soldCount = item.goods!!.soldCount!!
//                }
//                maxCount = item.goods!!.count!! - soldCount
//            }
//
//            if(maxCount != -1){
//                if(item.count!! < maxCount){
//                    item.count = item.count!! + 1
//                }
//            }else{
//                item.count = item.count!! + 1
//            }
//
//            SelectedGoodsManager.getInstance(mPageSeqNo!!).load().buyGoodsList = mDataList
//            SelectedGoodsManager.getInstance(mPageSeqNo!!).save()
//
//            if (checkListener != null) {
//                checkListener!!.onItemCheck()
//            }
//
//            notifyDataSetChanged()
//        }
//
//        if (item.goods != null) {
//            holder.text_title.text = item.goods!!.name
//            holder.text_total_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType((item.goods!!.price!! * item.count!!).toString()))
//            if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//                Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
//
//            } else {
//                holder.image.setImageResource(R.drawable.prnumber_default_img)
//            }
//        }
//
//        holder.check.setOnCheckedChangeListener { buttonView, isChecked ->
//            mDataList!![position].check = isChecked
//            if (checkListener != null) {
//                checkListener!!.onItemCheck()
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_cart, parent, false)
//        return ViewHolder(v)
//    }
//}