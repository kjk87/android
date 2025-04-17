//package com.pplus.prnumberuser.apps.chat.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.database.entity.Chat
//import kotlinx.android.synthetic.main.item_chat.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class ChatAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ChatAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Chat>? = null
//    var listener: OnItemClickListener? = null
//
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
//    constructor() : super()
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Chat {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Chat>? {
//
//        return mDataList
//    }
//
//    fun add(index:Int, data: Chat) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Chat>()
//        }
//        mDataList!!.add(index, data)
//        notifyDataSetChanged()
//    }
//
//    fun add(data: Chat) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Chat>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Chat>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Chat>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Chat) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Chat>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Chat>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val text_msg = itemView.text_chat_msg
//
//        init {
//
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if(mDataList == null) {
//            mDataList = ArrayList<Chat>()
//        }
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//
//        holder.text_msg.text = item.msg
//
//
//        holder.itemView.setOnClickListener {
//            listener?.onItemClick(holder.adapterPosition)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
//        return ViewHolder(v)
//    }
//}