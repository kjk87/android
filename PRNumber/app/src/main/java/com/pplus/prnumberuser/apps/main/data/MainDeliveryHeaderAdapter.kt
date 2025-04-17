package com.pplus.prnumberuser.apps.main.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.category.data.CategoryAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.core.network.model.dto.Page2
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.*
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MainDeliveryHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mDataList: MutableList<Page2>? = null
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Page2 {

        return mDataList!!.get(position)
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

    class ViewHeader(binding: HeaderDeliveryCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val recycler = binding.recyclerHeaderDeliveryCategory

        init {

        }
    }

    class ViewHolder(binding: ItemDeliveryPageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imagePage
        val text_name = binding.textPageName
        val text_dintance = binding.textPageDistance
        val text_delivery = binding.textPageDelivery
        val text_package = binding.textPagePackage

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {
            val adapter = CategoryAdapter()
            holder.recycler.layoutManager = GridLayoutManager(holder.itemView.context, 4)
            holder.recycler.adapter = adapter
            val params = HashMap<String, String>()
            params["major"] = "8"
            ApiBuilder.create().getCategoryMinorList(params).setCallback(object : PplusCallback<NewResultResponse<CategoryMinor>> {
                override fun onResponse(call: Call<NewResultResponse<CategoryMinor>>?, response: NewResultResponse<CategoryMinor>?) {
                    val categoryList = response!!.datas
                    val list = arrayListOf<CategoryMinor>()

                    list.add(CategoryMinor(-1L, 8, holder.itemView.context.getString(R.string.word_total)))

                    if (categoryList != null) {
                        list.addAll(categoryList)
                    }

                    adapter.setDataList(list)

                }

                override fun onFailure(call: Call<NewResultResponse<CategoryMinor>>?, t: Throwable?, response: NewResultResponse<CategoryMinor>?) {
                }
            }).build().call()
        } else if (holder is ViewHolder) {
            val item = getItem(position - 1)

            holder.itemView.setOnClickListener {
                listener?.onItemClick(position - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val binding = HeaderDeliveryCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHeader(binding)
        } else if (viewType == TYPE_ITEM) {
            val binding = ItemDeliveryPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}