package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.core.network.model.dto.Attachment
import kotlinx.android.synthetic.main.item_goods_reg_image.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by imac on 2018. 1. 8..
 */
class GoodsRegImageAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<GoodsRegImageAdapter.ViewHolder>, DraggableItemAdapter<GoodsRegImageAdapter.ViewHolder> {

    var mContext: Context? = null
    var mDataList: ArrayList<String>? = null
    var listener: OnItemDeleteListener? = null
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemDeleteListener {

        fun onItemDelete(position: Int)
    }

    constructor(context: Context) : super() {
        setHasStableIds(true)

        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {

        this.listener = listener
    }

    override fun getItemId(position: Int): Long {

        return mDataList!![position].hashCode().toLong()
    }

    fun getItem(position: Int): String {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<String>? {

        return mDataList
    }

    fun add(data: String) {

        if (mDataList == null) {
            mDataList = ArrayList<String>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun insertAttach(index: String, attachment: Attachment) {

        mDataList!![index.toInt()] = attachment.id
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<String>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<String>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: String) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<String>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: ArrayList<String>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {

        val image = itemView.image_goods_reg
        val image_delete = itemView.image_goods_reg_delete

        init {
        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList!![position]

        val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item}")
        Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)

        holder.image_delete.setOnClickListener {
            mDataList!!.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            if (listener != null) {
                listener!!.onItemDelete(holder.adapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_goods_reg_image, parent, false)
        return ViewHolder(v)
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = mDataList!!.removeAt(fromPosition);
        mDataList!!.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onGetItemDraggableRange(holder: ViewHolder?, position: Int): ItemDraggableRange? {
        return null
    }

    override fun onCheckCanStartDrag(holder: ViewHolder?, position: Int, x: Int, y: Int): Boolean {
        return true
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }
}