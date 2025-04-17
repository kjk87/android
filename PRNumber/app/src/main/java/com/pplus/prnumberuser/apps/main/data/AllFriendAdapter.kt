//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
//import com.pplus.prnumberuser.core.code.common.PageOpenBoundsCode
//import com.pplus.prnumberuser.core.database.DBManager
//import com.pplus.prnumberuser.core.database.entity.ContactDao
//import com.pplus.prnumberuser.core.network.model.dto.Friend
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.item_all_friend.view.*
//import java.util.*
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//class AllFriendAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<AllFriendAdapter.ViewHolder> {
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
//    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        val layout_user = itemView.layout_all_friend_user
//        val layout_not_user = itemView.layout_all_friend_not_user
//        val image = itemView.image_all_friend
//        val text_name = itemView.text_all_friend_name
//        var text_point = itemView.text_all_friend_point
//        val layout_enter_page = itemView.layout_all_friend_enter_page
//
//        val text_not_user_name = itemView.text_all_friend_not_user_name
//        val layout_not_user_enter_page = itemView.layout_all_friend_not_user_enter_page
//        val layout_not_user_invite = itemView.layout_all_friend_not_user_invite
//
//        init {
//            text_name.setSingleLine()
//            text_not_user_name.setSingleLine()
//            text_point.setSingleLine()
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_all_friend, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//        val contacts = mContactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.mobile)).list()
//
//        if(item.friend != null){
//
//            if (StringUtils.isNotEmpty(item.friend.nickname)) {
//                holder.layout_user.visibility = View.VISIBLE
//                holder.layout_not_user.visibility = View.GONE
//
//                var name = ""
//                if (contacts != null && contacts.size > 0) {
//                    name = contacts[0].memberName
//                } else {
//                    if (StringUtils.isNotEmpty(item.friend.nickname)) {
//                        name = item.friend.nickname!!
//                    } else if (StringUtils.isNotEmpty(item.friend.name)) {
//                        name = item.friend.name!!
//                    } else {
//                        name = holder.itemView.context.getString(R.string.word_unknown)
//                    }
//                }
//
//                holder.text_name.text = name
//                holder.text_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(item.friend.totalBol.toString())))
//
//
//                if (item.friend.profileImage != null) {
//                    Glide.with(holder.itemView.context).load(item.friend.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_square_profile_default).error(R.drawable.img_square_profile_default)).into(holder.image)
//                } else {
//                    holder.image.setImageResource(R.drawable.img_square_profile_default)
//                }
//
//                if(item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name){
//                    holder.layout_enter_page.visibility = View.VISIBLE
//                    holder.layout_enter_page.setOnClickListener {
//                        if (item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name) {
//                            val location = IntArray(2)
//                            it.getLocationOnScreen(location)
//                            val x = location[0] + it.width / 2
//                            val y = location[1] + it.height / 2
//
//                            PplusCommonUtil.goPage(mContext, item.friend.page!!, x, y)
//                        }
//                    }
//                }else{
//                    holder.layout_enter_page.visibility = View.GONE
//                }
//            }else{
//                holder.layout_user.visibility = View.GONE
//                holder.layout_not_user.visibility = View.VISIBLE
//
//                var name = ""
//                if (contacts != null && contacts.size > 0) {
//                    name = contacts[0].memberName
//                } else if (StringUtils.isNotEmpty(item.friend.name)) {
//                    name = item.friend.name!!
//                } else {
//                    name = holder.itemView.context.getString(R.string.word_unknown)
//                }
//
//                holder.text_not_user_name.text = name
//
//                if(item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name){
//                    holder.layout_not_user_enter_page.visibility = View.VISIBLE
//                    holder.layout_not_user_invite.visibility = View.GONE
//
//                    holder.layout_not_user_enter_page.setOnClickListener {
//                        if (item.friend.page != null && item.friend.page!!.openBound == PageOpenBoundsCode.everybody.name) {
//                            val location = IntArray(2)
//                            it.getLocationOnScreen(location)
//                            val x = location[0] + it.width / 2
//                            val y = location[1] + it.height / 2
//
//                            PplusCommonUtil.goPage(mContext, item.friend.page!!, x, y)
//                        }
//                    }
//
//                }else{
//                    holder.layout_not_user_enter_page.visibility = View.GONE
//                    holder.layout_not_user_invite.visibility = View.VISIBLE
//                    holder.layout_not_user_invite.setOnClickListener {
//                        val intent = Intent(holder.itemView.context, InviteActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        holder.itemView.context.startActivity(intent)
//                    }
//                }
//            }
//
//
//        }else{
//            holder.layout_user.visibility = View.GONE
//            holder.layout_not_user.visibility = View.VISIBLE
//
//            var name = ""
//            if (contacts != null && contacts.size > 0) {
//                name = contacts[0].memberName
//            } else {
//                name = holder.itemView.context.getString(R.string.word_unknown)
//            }
//
//            holder.text_not_user_name.text = name
//
//            holder.layout_not_user_enter_page.visibility = View.GONE
//            holder.layout_not_user_invite.visibility = View.VISIBLE
//
//            holder.layout_not_user_invite.setOnClickListener {
//                val intent = Intent(holder.itemView.context, InviteActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                holder.itemView.context.startActivity(intent)
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
