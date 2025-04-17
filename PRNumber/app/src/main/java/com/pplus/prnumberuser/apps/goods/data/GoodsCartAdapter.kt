//package com.pplus.prnumberuser.apps.goods.data
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Cart
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_goods_cart.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsCartAdapter() : RecyclerView.Adapter<GoodsCartAdapter.ViewHolder>() {
//
//    var mDataList: MutableList<Cart>? = null
//    var listener: OnItemClickListener? = null
//    var checkListener: OnItemCheckListener? = null
//
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
//    init {
//        this.mDataList = ArrayList()
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
//    fun getItem(position: Int): Cart {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Cart>? {
//
//        return mDataList
//    }
//
//    fun add(data: Cart) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Cart>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Cart>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Cart>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Cart) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Cart>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Cart>) {
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
//
//
//        holder.check.isChecked = item.check
//        holder.text_count.text = item.count.toString()
//
//        holder.image_minus.setOnClickListener {
//            if (item.count!! > 1) {
//                item.count = item.count!! - 1
//
//                update(item, holder.adapterPosition)
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
//                    update(item, holder.adapterPosition)
//                }
//            }else{
//                item.count = item.count!! + 1
//                update(item, holder.adapterPosition)
//            }
//        }
//
//        if (item.goods != null) {
//            holder.text_title.text = item.goods!!.name
//            holder.text_total_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_total_price, FormatUtil.getMoneyType((item.goods!!.price!! * item.count!!).toString())))
//            if (item.goods!!.goodsImageList != null && item.goods!!.goodsImageList!!.isNotEmpty()) {
//                Glide.with(holder.itemView.context).load(item.goods!!.goodsImageList!![0].image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
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
//    fun update(cart: Cart, position: Int) {
//        notifyItemChanged(position)
//        val params = Cart()
//        params.seqNo = cart.seqNo
//        params.goodsSeqNo = cart.goods!!.seqNo
//        params.count = cart.count
//        params.memberSeqNo = LoginInfoManager.getInstance().user.no
//
//        ApiBuilder.create().putCart(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                if (checkListener != null) {
//                    checkListener!!.onItemCheck()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//
//            }
//        }).build().call()
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_cart, parent, false)
//        return ViewHolder(v)
//    }
//}