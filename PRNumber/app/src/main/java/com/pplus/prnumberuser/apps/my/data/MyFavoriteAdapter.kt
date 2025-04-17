package com.pplus.prnumberuser.apps.my.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
import com.pplus.prnumberuser.core.network.model.dto.CategoryMinor
import com.pplus.prnumberuser.databinding.ItemHashtagBinding
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class MyFavoriteAdapter() : RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder>() {

    var mDataList: MutableList<CategoryMajor>? = null
    var listener: OnItemListener? = null
    var mMyCategoryList: MutableList<CategoryMinor>? = null


    interface OnItemListener {

        fun onItemSelect(categoryMinor: CategoryMinor)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemListener(listener: OnItemListener) {

        this.listener = listener
    }

    fun getItem(position: Int): CategoryMajor {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<CategoryMajor>? {

        return mDataList
    }

    fun add(data: CategoryMajor) {

        if (mDataList == null) {
            mDataList = ArrayList()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<CategoryMajor>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: CategoryMajor) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<CategoryMajor>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemHashtagBinding) : RecyclerView.ViewHolder(binding.root) {

        val text_key = binding.textHashtagKey
        val recycler_value = binding.recyclerHashtagValue

        init {
            recycler_value.addItemDecoration(BottomItemOffsetDecoration(itemView.context, R.dimen.height_30))
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        holder.text_key.text = item.name

        val minorList = item.minorList

        val layoutManager = GridLayoutManager(holder.itemView.context, 40)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                val keyWord = minorList!![position].name!!

                return if (keyWord.length < 3) {
                    10
                } else if (keyWord.length < 4) {
                    11
                } else if (keyWord.length < 5) {
                    12
                } else if (keyWord.length < 6) {
                    13
                } else if (keyWord.length < 7) {
                    14
                } else if (keyWord.length < 8) {
                    15
                } else if (keyWord.length < 10) {
                    17
                } else {
                    20
                }
            }
        }

        holder.recycler_value.layoutManager = layoutManager
        val adapter = CategoryMinorAdapter( false)
        adapter.mMyCategoryList = mMyCategoryList
        adapter.setDataList(item.minorList!!.toMutableList())
        holder.recycler_value.adapter = adapter
        adapter.setOnItemClickListener(object : CategoryMinorAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                listener?.onItemSelect(minorList!![position])

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}