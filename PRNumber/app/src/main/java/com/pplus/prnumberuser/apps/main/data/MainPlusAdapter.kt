package com.pplus.prnumberuser.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.News
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Plus2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMainPlusBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPlusAdapter : RecyclerView.Adapter<MainPlusAdapter.ViewHolder> {

    var mDataList: MutableList<Plus2>? = null
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

    fun getItem(position: Int): Plus2 {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Plus2>? {

        return mDataList
    }

    fun add(data: Plus2) {

        if (mDataList == null) {
            mDataList = ArrayList<Plus2>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Plus2>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Plus2>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Plus2) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Plus2>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Plus2>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemMainPlusBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_page = binding.layoutMainPlusPage
        val image = binding.imageMainPlus
        val text_name = binding.textMainPlusName
        val recycler_goods = binding.recyclerMainPlusGoods
        val text_more = binding.textMainPlusMore

        init {
            text_name.setSingleLine()
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.layout_page.setOnClickListener {
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val x = location[0] + it.width / 2
            val y = location[1] + it.height / 2
            PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
        }

        holder.text_name.text = item.page!!.name

        if (StringUtils.isNotEmpty(item.page!!.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
        }

        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = MainPlusNewsAdapter()
        holder.recycler_goods.layoutManager = layoutManager
        holder.recycler_goods.adapter = adapter
        adapter.setDataList(item.newsList!!)

        adapter.setOnItemClickListener(object : MainPlusNewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
//                val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java)
//                intent.putExtra(Const.DATA, adapter.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                holder.itemView.context.startActivity(intent)
            }
        })
        var paging = 0
        var lackListView = false

        if(item.totalNewsElements!! > 5){
            holder.text_more.visibility = View.VISIBLE
            holder.text_more.setOnClickListener {
//                val page = Page()
//                page.no = item.pageSeqNo
//                val intent = Intent(holder.itemView.context, StoreNewsActivity::class.java)
//                intent.putExtra(Const.PAGE, page)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
            }
        }else{
            holder.text_more.visibility = View.GONE
        }

        holder.recycler_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (!lackListView) {
//                    if (totalItemCount < item.totalGoodsElements!! && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                    if (totalItemCount < item.totalNewsElements!! && visibleItemCount + pastVisibleItems >= totalItemCount) {
                        paging++
                        lackListView = true
                        val params = HashMap<String, String>()
                        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
                        params["page"] = paging.toString()
                        params["size"] = "5"
                        params["pageSeqNo"] = item.pageSeqNo.toString()

                        ApiBuilder.create().getNewsListByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<News>>> {
                            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<News>>>?, response: NewResultResponse<SubResultResponse<News>>?) {
                                lackListView = false
                                adapter.addAll(response!!.data.content!!)
                            }

                            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<News>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<News>>?) {

                            }
                        }).build().call()
                    }
                }
            }
        })

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(holder.adapterPosition, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainPlusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}