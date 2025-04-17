//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.content.Intent
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.friend.ui.FriendPageActivity
//import com.pplus.prnumberuser.core.code.common.PageOpenBoundsCode
//import com.pplus.prnumberuser.core.database.DBManager
//import com.pplus.prnumberuser.core.database.entity.ContactDao
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.PplusNumberUtil
//import kotlinx.android.synthetic.main.item_friend.view.*
//import java.util.*
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class FriendAdapter : RecyclerView.Adapter<FriendAdapter.ViewHolder> {
//
//    private var mDataList: MutableList<Friend>? = null
//    private var listener: OnItemClickListener? = null
//    private val mContactDao: ContactDao
//    private val mContext:Context
//
//    lateinit var dataList: MutableList<Friend>
//
//    constructor(context: Context) : super(){
//        mDataList = ArrayList()
//        mContext = context
//        mContactDao = DBManager.getInstance(context).session.contactDao
//    }
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view:View)
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
//        val layout_page = itemView.layout_friend_page
//        val image_page = itemView.image_friend_page
//        val image_page_seller = itemView.image_friend_page_seller
//        val text_name = itemView.text_friend_name
//        val text_page_name = itemView.text_friend_page_name
//        val text_page_number = itemView.text_friend_page_number
//        val text_name2 = itemView.text_friend_name2
//        var text_2depth_shop = itemView.layout_friend_2depth_shop
//
//        init {
//            text_name.setSingleLine()
//            text_name2.setSingleLine()
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
//            if (StringUtils.isNotEmpty(item.friend.nickname)) {
//                name = item.friend.nickname
//            } else if (item.friend.page != null && StringUtils.isNotEmpty(item.friend.page!!.name)) {
//                name = item.friend.page!!.name
//            } else if (StringUtils.isNotEmpty(item.friend.name)) {
//                name = item.friend.name
//            } else {
//                name = mContext.getString(R.string.word_unknown)
//            }
//        }
//
//
//        if (item.friend.page == null || item.friend.page!!.openBound == PageOpenBoundsCode.nobody.name) {
//            holder.text_name2.visibility = View.VISIBLE
//            holder.layout_page.visibility = View.GONE
//            holder.text_name2.text = name
//
//        } else {
//            holder.text_name2.visibility = View.GONE
//            holder.layout_page.visibility = View.VISIBLE
//            holder.text_name.text = name
//            if (StringUtils.isNotEmpty(item.friend.page!!.thumbnail)) {
//                Glide.with(mContext).load(item.friend.page!!.thumbnail).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image_page)
//            } else if (item.friend.profileImage != null) {
//                Glide.with(mContext).load(item.friend.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image_page)
//            } else {
//                holder.image_page.setImageResource(R.drawable.img_square_profile_default)
//            }
//
//            if (item.friend.page!!.isSeller != null && item.friend.page!!.isSeller!!) {
//                holder.image_page_seller.visibility = View.VISIBLE
//            } else {
//                holder.image_page_seller.visibility = View.GONE
//            }
//
//            holder.text_page_name.text = item.friend.page!!.name
//            if(item.friend.page!!.numberList != null && item.friend.page!!.numberList!!.isNotEmpty()){
//                val number = item.friend.page!!.numberList!![0].number
//                holder.text_page_number.text = PplusNumberUtil.getPrNumberFormat(number)
//            }
//        }
//
//        if(item.isExistsPageInContact){
//            holder.text_2depth_shop.visibility = View.VISIBLE
//
//            holder.text_2depth_shop.setOnClickListener {
//                val intent = Intent(mContext, FriendPageActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                mContext.startActivity(intent)
//            }
//        }else{
//            holder.text_2depth_shop.visibility = View.GONE
//        }
//
//        holder.itemView.setOnClickListener {
//            if (item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name) {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//
//                PplusCommonUtil.goPage(mContext, item.friend.page!!, x, y)
//            }
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//
//        return mDataList!!.size
//    }
//}
