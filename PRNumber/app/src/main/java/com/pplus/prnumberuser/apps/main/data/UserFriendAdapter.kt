//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.code.common.PageOpenBoundsCode
//import com.pplus.prnumberuser.core.database.DBManager
//import com.pplus.prnumberuser.core.database.entity.ContactDao
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.item_user_friend.view.*
//import java.util.*
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class UserFriendAdapter : RecyclerView.Adapter<UserFriendAdapter.ViewHolder> {
//
//    private var mDataList: MutableList<Friend>? = null
//    private var listener: OnItemClickListener? = null
//    private val mContactDao: ContactDao
//    private val mContext: Context
//
//    constructor(context: Context) : super() {
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
//        val image = itemView.image_user_friend
//        val text_name = itemView.text_user_friend_name
//        var text_point = itemView.text_user_friend_point
//        val layout_enter_page = itemView.layout_user_friend_enter_page
//
//        init {
//            text_name.setSingleLine()
//            text_point.setSingleLine()
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user_friend, parent, false)
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
//            } else if (StringUtils.isNotEmpty(item.friend.name)) {
//                name = item.friend.name
//            } else {
//                name = mContext.getString(R.string.word_unknown)
//            }
//        }
//
//        holder.text_name.text = name
//        holder.text_point.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(item.friend.totalBol.toString())))
//
//
//        if (item.friend.profileImage != null) {
//            Glide.with(mContext).load(item.friend.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.img_square_profile_default)
//        }
//
//        if(item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name){
//            holder.layout_enter_page.visibility = View.VISIBLE
//            holder.layout_enter_page.setOnClickListener {
//                if (item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name) {
//                    val location = IntArray(2)
//                    it.getLocationOnScreen(location)
//                    val x = location[0] + it.width / 2
//                    val y = location[1] + it.height / 2
//
//                    PplusCommonUtil.goPage(mContext, item.friend.page!!, x, y)
//                }
//            }
//        }else{
//            holder.layout_enter_page.visibility = View.GONE
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
