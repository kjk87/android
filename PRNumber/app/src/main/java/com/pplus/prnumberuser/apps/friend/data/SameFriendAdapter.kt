//package com.pplus.prnumberuser.apps.friend.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.database.DBManager
//import com.pplus.prnumberuser.core.database.entity.ContactDao
//import com.pplus.prnumberuser.core.network.model.dto.User
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.PplusNumberUtil
//import kotlinx.android.synthetic.main.item_friend.view.*
//import java.util.*
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class SameFriendAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SameFriendAdapter.ViewHolder> {
//
//    private var mDataList: MutableList<User>? = null
//    private var listener: OnItemClickListener? = null
//    private val mContactDao: ContactDao
//    private val mContext:Context
//
//    lateinit var dataList: MutableList<User>
//
//    constructor(context: Context) : super(){
//        mDataList = ArrayList()
//        mContext = context
//        mContactDao = DBManager.getInstance(context).session.contactDao
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
//    fun getItem(position: Int): User {
//
//        return mDataList!![position]
//    }
//
//    fun add(data: User) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<User>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: User) {
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
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val layout_page = itemView.layout_friend_page
//        val image_page = itemView.image_friend_page
//        val text_name = itemView.text_friend_name
//        val text_page_name = itemView.text_friend_page_name
//        val text_page_number = itemView.text_friend_page_number
//        val text_name2 = itemView.text_friend_name2
//        var text_2depth_shop = itemView.layout_friend_2depth_shop
//
//        init {
//            text_2depth_shop.visibility = View.GONE
//        }
//    }
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//        val contacts = mContactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.mobile)).list()
//        var name: String? = null
//        if (contacts != null && contacts.size > 0) {
//            name = contacts[0].memberName
//        } else {
//            if (StringUtils.isNotEmpty(item.nickname)) {
//                name = item.nickname
//            } else if (item.page != null && StringUtils.isNotEmpty(item.page!!.name)) {
//                name = item.page!!.name
//            } else if (StringUtils.isNotEmpty(item.name)) {
//                name = item.name
//            } else {
//                name = mContext.getString(R.string.word_unknown)
//            }
//        }
//
//
//
//        if (item.page == null) {
//            holder.text_name2.visibility = View.VISIBLE
//            holder.layout_page.visibility = View.GONE
//            holder.text_name2.text = name
//
//        } else {
//            holder.text_name.text = name
//
//            holder.text_name2.visibility = View.GONE
//            holder.layout_page.visibility = View.VISIBLE
//            holder.text_name.text = name
//            if (item.page != null) {
//                Glide.with(mContext).load(item.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image_page)
//            } else if (item.profileImage != null) {
//                Glide.with(mContext).load(item.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image_page)
//            } else {
//                holder.image_page.setImageResource(R.drawable.img_square_profile_default)
//            }
//
//            holder.text_page_name.text = item.page!!.name
//            if(item.page!!.numberList != null && item.page!!.numberList!!.isNotEmpty()){
//                val number = item.page!!.numberList!![0].virtualNumber
//                holder.text_page_number.text = PplusNumberUtil.getPrNumberFormat(number)
//            }
//
//
//            holder.itemView.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//
//                PplusCommonUtil.goPage(mContext, item.page!!, x, y)
//            }
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
