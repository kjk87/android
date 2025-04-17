package com.lejel.wowbox.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawByGroupActivity
import com.lejel.wowbox.core.network.model.dto.LuckyDrawTheme
import com.lejel.wowbox.core.network.model.dto.LuckyDrawThemeGroup
import com.lejel.wowbox.databinding.ItemLuckyDrawThemeBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawThemeAdapter() : RecyclerView.Adapter<LuckyDrawThemeAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawTheme>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): LuckyDrawTheme {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawTheme>? {

        return mDataList
    }

    fun add(data: LuckyDrawTheme) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawTheme>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawTheme>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawTheme>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawTheme) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawTheme>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawTheme>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawThemeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textLuckyDrawThemeTitle.text = item.title

        val adapter = LuckyDrawGroupAdapter()
        adapter.launcher = launcher
        adapter.listener = object : LuckyDrawGroupAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(holder.itemView.context, LuckyDrawByGroupActivity::class.java)
                intent.putExtra(Const.DATA, adapter.getItem(position).luckyDrawGroup)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                launcher?.launch(intent)
            }
        }
        holder.binding.recyclerLuckyDrawThemeGroup.adapter = adapter
        holder.binding.recyclerLuckyDrawThemeGroup.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setDataList(item.groupList as MutableList<LuckyDrawThemeGroup>)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}