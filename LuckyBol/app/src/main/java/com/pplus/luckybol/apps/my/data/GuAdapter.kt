package com.pplus.luckybol.apps.my.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Juso
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ItemGuBinding
import retrofit2.Call
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class GuAdapter() : RecyclerView.Adapter<GuAdapter.ViewHolder>() {

    var mDataList: MutableList<Juso>? = null
    var listener: OnItemClickListener? = null
    var mSelectData : Juso? = null
    var mDoCode = ""


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Juso {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Juso>? {

        return mDataList
    }

    fun add(data: Juso) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Juso>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Juso) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Juso>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemGuBinding) : RecyclerView.ViewHolder(binding.root) {

        val layout_gu = binding.layoutGu
        val text_name = binding.textGuName
        val view_bar = binding.viewGuBar
        val recycler_dong = binding.recyclerGuDong
        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.text_name.text = item.name

        if(mSelectData?.value == item.value){
            holder.layout_gu.isSelected = true
            holder.view_bar.visibility = View.GONE
            holder.recycler_dong.visibility = View.VISIBLE

            holder.recycler_dong.layoutManager = GridLayoutManager(holder.itemView.context, 3)
            val dongAdapter = DongAdapter()
            holder.recycler_dong.adapter = dongAdapter
            val params = HashMap<String, String>()
            params["type"] = "dong"
            params["value"] = mDoCode+item.value
//            showProgress("")
            ApiBuilder.create().getJusoList(params).setCallback(object : PplusCallback<NewResultResponse<Juso>> {
                override fun onResponse(call: Call<NewResultResponse<Juso>>?, response: NewResultResponse<Juso>?) {
//                    hideProgress()
                    if(response?.datas != null){
//                    if(response.datas.isNotEmpty()){
//                        mGuAdapter!!.mSelectData = response.datas[0]
//                    }
                        dongAdapter.mGuCode = mDoCode+item.value
                        dongAdapter.setDataList(response.datas!! as MutableList<Juso>)
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Juso>>?, t: Throwable?, response: NewResultResponse<Juso>?) {
//                    hideProgress()
                }
            }).build().call()

        }else{
            holder.layout_gu.isSelected = false
            holder.view_bar.visibility = View.VISIBLE
            holder.recycler_dong.visibility = View.GONE
        }




        holder.itemView.setOnClickListener {
            if(item.value == mSelectData?.value){
                mSelectData = null
            }else{
                mSelectData = item
            }

            notifyDataSetChanged()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}