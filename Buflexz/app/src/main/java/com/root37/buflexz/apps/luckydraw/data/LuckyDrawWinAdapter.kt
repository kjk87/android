package com.root37.buflexz.apps.luckydraw.data

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.apps.luckydraw.ui.LuckyDrawWinReplyActivity
import com.root37.buflexz.core.network.model.dto.LuckyDrawWin
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ItemLuckyDrawWinBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class LuckyDrawWinAdapter() : RecyclerView.Adapter<LuckyDrawWinAdapter.ViewHolder>() {

    var mDataList: MutableList<LuckyDrawWin>? = null
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

    fun getItem(position: Int): LuckyDrawWin {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<LuckyDrawWin>? {

        return mDataList
    }

    fun add(data: LuckyDrawWin) {

        if (mDataList == null) {
            mDataList = ArrayList<LuckyDrawWin>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<LuckyDrawWin>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<LuckyDrawWin>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: LuckyDrawWin) {
        if (position != -1 && mDataList!!.size > 0) {
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position + 1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<LuckyDrawWin>()
    }

    fun setDataList(dataList: MutableList<LuckyDrawWin>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLuckyDrawWinBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]

        Glide.with(holder.itemView.context).load(item.giftImage).apply(RequestOptions().centerCrop()).into(holder.binding.imageLuckyDrawWinGift)
        holder.binding.textLuckyDrawWinGiftTitle.text = item.giftName
        holder.binding.textLuckyDrawWinGiftPrice.text = FormatUtil.getMoneyTypeFloat(item.giftPrice.toString())

        holder.binding.textLuckyDrawWinNickname.text = item.memberTotal?.nickname
        Glide.with(holder.itemView.context).load(Const.CDN_URL + "profile/${item.memberTotal!!.userKey}/index.html").apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(PplusCommonUtil.getDefaultProfile(position)).error(PplusCommonUtil.getDefaultProfile(position))).into(holder.binding.imageLuckyDrawWinFlag)
        Glide.with(holder.itemView.context).load(Uri.parse("file:///android_asset/flags/${item.memberTotal!!.nation!!.uppercase()}.png")).into(holder.binding.imageLuckyDrawWinFlag)

        val winNumbers = item.winNumber!!.split("|")
        when (winNumbers.size) {
            1 -> {

                holder.binding.textLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textLuckyDrawWinNumberSecond.visibility = View.GONE
                holder.binding.textLuckyDrawWinNumberThird.visibility = View.GONE

                holder.binding.textLuckyDrawWinNumberFirst.text = winNumbers[0]
            }

            2 -> {
                holder.binding.textLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textLuckyDrawWinNumberSecond.visibility = View.GONE
                holder.binding.textLuckyDrawWinNumberThird.visibility = View.VISIBLE

                holder.binding.textLuckyDrawWinNumberFirst.text = winNumbers[0]
                holder.binding.textLuckyDrawWinNumberThird.text = winNumbers[1]
            }

            3 -> {
                holder.binding.textLuckyDrawWinNumberFirst.visibility = View.VISIBLE
                holder.binding.textLuckyDrawWinNumberSecond.visibility = View.VISIBLE
                holder.binding.textLuckyDrawWinNumberThird.visibility = View.VISIBLE

                holder.binding.textLuckyDrawWinNumberFirst.text = winNumbers[0]
                holder.binding.textLuckyDrawWinNumberSecond.text = winNumbers[1]
                holder.binding.textLuckyDrawWinNumberThird.text = winNumbers[2]
            }
        }

        if (StringUtils.isNotEmpty(item.impression)) {
            holder.binding.textLuckyDrawWinImpression.visibility = View.VISIBLE
            holder.binding.textLuckyDrawWinImpression.text = item.impression
        } else {
            holder.binding.textLuckyDrawWinImpression.visibility = View.GONE
        }

        holder.binding.textLuckyDrawWinReplyCount.text = FormatUtil.getMoneyType(item.replyCount.toString())

        holder.binding.textLuckyDrawWinReplyCount.setOnClickListener {
            val intent = Intent(holder.itemView.context, LuckyDrawWinReplyActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher?.launch(intent)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(holder.absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLuckyDrawWinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}