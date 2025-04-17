package com.lejel.wowbox.apps.luckydraw.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.utils.time.DateFormatUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.luckydraw.ui.LuckyDrawWinReplyReportActivity
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWinReply
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLuckyDrawWinReplyBinding
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawWinReplyAdapter() : RecyclerView.Adapter<LuckyDrawWinReplyAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawWinReply>? = null
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

    fun getItem(position: Int): LuckyDrawWinReply {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawWinReply>? {

        return mDataList
    }

    fun add(data: LuckyDrawWinReply) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawWinReply>()
        }
        mDataList!!.add(data)
        notifyItemInserted(mDataList!!.size - 1)
    }

    fun addAll(dataList: List<LuckyDrawWinReply>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawWinReply>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawWinReply) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawWinReply>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawWinReply>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawWinReplyBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        holder.binding.textLuckyDrawWinReplyNickname.text = item.memberTotal?.nickname
        Glide.with(holder.itemView.context).load(Const.API_URL+"profile/${item.userKey}").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageLuckyDrawWinReplyProfile)

        holder.binding.textLuckyDrawWinReply.text = item.reply

        if (LoginInfoManager.getInstance().isMember() && LoginInfoManager.getInstance().member!!.userKey == item.memberTotal?.userKey) {
            holder.binding.textLuckyDrawWinReplyMe.visibility = View.VISIBLE
        } else {
            holder.binding.textLuckyDrawWinReplyMe.visibility = View.GONE
        }
        val format = SimpleDateFormat(holder.itemView.context.getString(R.string.word_date_format2))
        val regDate = format.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime))
        holder.binding.textLuckyDrawWinReplyRegDate.text = regDate
        holder.binding.imageLuckyDrawWinReplyReport.setOnClickListener {
            val intent = Intent(holder.itemView.context, LuckyDrawWinReplyReportActivity::class.java)
            intent.putExtra(Const.DATA, item)
            launcher?.launch(intent)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawWinReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}