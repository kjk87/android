//package com.pplus.prnumberuser.apps.my.data
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
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.User
//import kotlinx.android.synthetic.main.item_ranking.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class RankingAdapter : RecyclerView.Adapter<RankingAdapter.ViewHolder> {
//
//    var mContext: Context? = null
//    var mDataList: MutableList<User>? = null
//    var listener: OnItemClickListener? = null
//    var rankType = EnumData.RankType.recommend.name
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context, rankType: String) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.rankType = rankType
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): User {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<User>? {
//
//        return mDataList
//    }
//
//    fun add(data: User) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<User>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<User>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<User>()
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
//        mDataList = ArrayList<User>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<User>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_ranking = itemView.image_ranking
//        val text_ranking = itemView.text_ranking
//        val image = itemView.image_ranking_profile
//        val text_name = itemView.text_ranking_name
//        val text_info = itemView.text_ranking_info
//
//        init {
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = mDataList!![position]
//
//        holder.text_name.text = item.nickname!!
//
//        if (LoginInfoManager.getInstance().isMember && item.no == LoginInfoManager.getInstance().user.no) {
//            holder.text_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ranking_me, 0)
//        } else {
//            holder.text_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//        }
//
//        if (item.profileImage != null && StringUtils.isNotEmpty(item.profileImage!!.url)) {
//            Glide.with(holder.itemView.context).load(item.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.ic_event_profile_default)).into(holder.image)
//        } else {
//            holder.image.setImageResource(R.drawable.ic_event_profile_default)
//        }
//
//        when (item.ranking) {
//            1L -> {
//                holder.image_ranking.visibility = View.VISIBLE
//                holder.text_ranking.visibility = View.GONE
//                holder.image_ranking.setImageResource(R.drawable.ic_ranking_1)
//            }
//            2L -> {
//                holder.image_ranking.visibility = View.VISIBLE
//                holder.text_ranking.visibility = View.GONE
//                holder.image_ranking.setImageResource(R.drawable.ic_ranking_2)
//            }
//            3L -> {
//                holder.image_ranking.visibility = View.VISIBLE
//                holder.text_ranking.visibility = View.GONE
//                holder.image_ranking.setImageResource(R.drawable.ic_ranking_3)
//            }
//            else -> {
//                holder.image_ranking.visibility = View.GONE
//                holder.text_ranking.visibility = View.VISIBLE
//                holder.text_ranking.text = item.ranking.toString()
//            }
//        }
//
//
//        when (rankType) {
//            EnumData.RankType.recommend.name -> {
//                holder.text_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ranking_recommend, 0, 0, 0)
//                holder.text_info.text = mContext?.getString(R.string.format_count_unit2, FormatUtil.getMoneyType(item.rankingCount.toString()))
//            }
//            EnumData.RankType.reward.name -> {
//                holder.text_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ranking_point, 0, 0, 0)
//                holder.text_info.text = mContext?.getString(R.string.format_cash_unit, FormatUtil.getMoneyType(item.rankingCount.toString()))
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
//        return ViewHolder(v)
//    }
//}