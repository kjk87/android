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
import com.pplus.prnumberuser.apps.main.ui.MainPageWithSubscriptionProductFragment
import com.pplus.prnumberuser.apps.subscription.ui.SubscriptionDetailActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ItemMainPageWithProductBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainPageWithSubscriptionProductAdapter : RecyclerView.Adapter<MainPageWithSubscriptionProductAdapter.ViewHolder> {

    var mDataList: MutableList<Page2>? = null
    var listener: OnItemClickListener? = null
    var mMainPageWithSubscriptionProductFragment: MainPageWithSubscriptionProductFragment? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int, view: View)
    }

    constructor() : super() {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Page2 {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Page2>? {

        return mDataList
    }

    fun add(data: Page2) {

        if (mDataList == null) {
            mDataList = ArrayList<Page2>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Page2>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Page2>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Page2) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Page2>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Page2>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding:ItemMainPageWithProductBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_page = binding.layoutMainPageWithProductPage
        val image = binding.imageMainPageWithProduct
        val text_name = binding.textMainPageWithProductName
        val text_distance = binding.textMainPageWithProductDistance
        val recycler_product = binding.recyclerMainPageWithProduct

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
            PplusCommonUtil.goPage(holder.itemView.context, item, x, y)
        }

        holder.text_name.text = item.name

        if (StringUtils.isNotEmpty(item.thumbnail)) {
            Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.prnumber_default_img)
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

        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = MainSubscriptionAdapter()
        holder.recycler_product.layoutManager = layoutManager
        holder.recycler_product.adapter = adapter
        adapter.setDataList(item.productPriceList!! as MutableList<ProductPrice>)

        adapter.setOnItemClickListener(object : MainSubscriptionAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(holder.itemView.context, SubscriptionDetailActivity::class.java)
                intent.putExtra(Const.DATA, adapter.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                if(mMainPageWithSubscriptionProductFragment != null){
                    mMainPageWithSubscriptionProductFragment!!.signInLauncher.launch(intent)
                }else{
                    holder.itemView.context.startActivity(intent)
                }

            }
        })
        var paging = 0
        var lackListView = false

//        if(item.totalProductPriceElements!! > 5){
//            holder.text_more.visibility = View.VISIBLE
//            holder.text_more.setOnClickListener {
//                val page = Page()
//                page.no = item.seqNo
//                val intent = Intent(holder.itemView.context, StoreNewsActivity::class.java)
//                intent.putExtra(Const.PAGE, page)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
//            }
//        }else{
//            holder.text_more.visibility = View.GONE
//        }

        holder.recycler_product.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                    if (totalItemCount < item.totalProductPriceElements!! && visibleItemCount + pastVisibleItems >= totalItemCount) {
                        paging++
                        lackListView = true
                        val params = HashMap<String, String>()
                        params["page"] = paging.toString()
                        params["size"] = "5"
                        params["pageSeqNo"] = item.seqNo.toString()

                        ApiBuilder.create().getProductPriceListByIsSubscriptionAndIsPrepaymentOnlyNormal(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductPrice>>> {
                            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {
                                lackListView = false
                                adapter.addAll(response!!.data.content!!)
                            }

                            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductPrice>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductPrice>>?) {

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
        val binding = ItemMainPageWithProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}