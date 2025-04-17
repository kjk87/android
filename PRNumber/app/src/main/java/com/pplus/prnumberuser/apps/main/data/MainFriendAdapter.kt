//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import kotlinx.android.synthetic.main.item_main_friend.view.*
//import java.util.*
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class MainFriendAdapter : RecyclerView.Adapter<MainFriendAdapter.ViewHolder> {
//
//    private var mDataList: MutableList<Friend>? = null
//    private var listener: OnItemClickListener? = null
//    private val mContext: Context
//
//    constructor(context: Context) : super() {
//        mDataList = ArrayList()
//        mContext = context
//    }
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    override fun getItemId(position: Int): Long {
//
//        return position.toLong()
//    }
//
//    fun getItem(position: Int): Friend {
//
//        return mDataList!![position]
//    }
//
//    fun add(data: Friend) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Friend>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Friend) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList()
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image = itemView.image_main_friend
//
//        init {
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_friend, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//        if (item.friend.profileImage != null) {
//            Glide.with(mContext).load(item.friend.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_home_friend_default).error(R.drawable.img_home_friend_default)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.img_home_friend_default)
//        }
//
//        holder.itemView.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(holder.adapterPosition, it)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//
//        return mDataList!!.size
//    }
//}
