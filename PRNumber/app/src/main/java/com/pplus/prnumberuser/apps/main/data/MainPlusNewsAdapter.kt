package com.pplus.prnumberuser.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.core.network.model.dto.News
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMainPlusNewsBinding
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPlusNewsAdapter : RecyclerView.Adapter<MainPlusNewsAdapter.ViewHolder> {

    var mDataList: MutableList<News>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): News {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<News>? {

        return mDataList
    }

    fun add(data: News) {

        if (mDataList == null) {
            mDataList = ArrayList<News>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<News>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<News>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: News) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<News>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: List<News>) {

        this.mDataList = dataList as MutableList<News>
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainPlusNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.imageMainPlusNewsImage
        val text_title= binding.textMainPlusNewsTitle
        val text_link = binding.textMainPlusNewsLink
        val text_contents = binding.textMainPlusNewsContents

        init {
            text_title.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        holder.text_title.text = item.title
        holder.text_contents.text = item.content


        if(StringUtils.isEmpty(item.link) && item.productSeqNo == null){
            holder.text_link.visibility = View.GONE
        }else{
            holder.text_link.visibility = View.VISIBLE
            holder.text_link.setOnClickListener {
                if(item.productSeqNo != null){
                    val intent = Intent(holder.itemView.context, ProductShipDetailActivity::class.java)
                    val productPrice = ProductPrice()
                    productPrice.seqNo = item.productSeqNo
                    intent.putExtra(Const.DATA, productPrice)
                    holder.itemView.context.startActivity(intent)
                }else if(StringUtils.isNotEmpty(item.link)){
                    PplusCommonUtil.openChromeWebView(holder.itemView.context, item.link!!)
                }
            }
        }

        if (item.newsImageList != null && item.newsImageList!!.isNotEmpty()) {
            holder.image.visibility = View.VISIBLE
            holder.text_contents.maxLines = 2
            Glide.with(holder.itemView.context).load(item.newsImageList!![0].image).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        }else{
            holder.image.visibility = View.GONE
            holder.text_contents.maxLines = 5
        }

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainPlusNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}