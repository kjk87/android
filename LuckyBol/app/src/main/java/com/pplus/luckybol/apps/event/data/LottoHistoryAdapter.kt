package com.pplus.luckybol.apps.event.data

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.event.ui.LuckyLottoDetailActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ItemLottoHistoryBinding
import com.pplus.luckybol.databinding.ItemSelectedLottoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


/**
 * Created by imac on 2018. 1. 8..
 */
class LottoHistoryAdapter() : RecyclerView.Adapter<LottoHistoryAdapter.ViewHolder>() {

    var mDataList: MutableList<Event>? = null
    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int, isOpen: Boolean)
    }

    init {
        this.mDataList = ArrayList()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Event {

        return mDataList!!.get(position)
    }

    fun getDataList(): MutableList<Event>? {

        return mDataList
    }

    fun add(data: Event) {

        if (mDataList == null) {
            mDataList = ArrayList<Event>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Event>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Event>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Event) {
        if(position != -1 && mDataList!!.size > 0){
            mDataList!!.removeAt(position)
            mDataList!!.add(position, data)
            notifyItemChanged(position+1)
        }

    }

    fun clear() {
        notifyItemRangeRemoved(0, mDataList!!.size)
        mDataList = ArrayList<Event>()
    }

    fun setDataList(dataList: MutableList<Event>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(binding:ItemLottoHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageLottoHistory
        val text_gift_title = binding.textLottoHistoryGiftTitle
        val layout_win_number = binding.layoutLottoHistoryWinNumber
        val text_win_announce = binding.textLottoHistoryWinAnnounce
        val layout_winner_title = binding.layoutLottoWinWinnerTitle
        val text_winner_desc = binding.textLottoHistoryWinnerDesc
        val recylcer_winner = binding.recyclerLottoHistoryWinner
        val layout_detail = binding.layoutLottoHistoryDetail

        init {


        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mDataList!![position]
        Glide.with(holder.itemView.context).load(item.bannerImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(holder.image)
        holder.text_gift_title.text = item.title

        if(item.lottoWinNumberList != null){
            val list = item.lottoWinNumberList!!
            holder.layout_win_number.removeAllViews()
            for (i in 0 until list.size) {
                val selectedLottoBinding = ItemSelectedLottoBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                selectedLottoBinding.textSelectedLottoNumber.text = list[i].lottoNumber.toString()

                if (list[i].lottoNumber in 1..10) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_f2c443)
                } else if (list[i].lottoNumber in 11..20) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_85c5f1)
                } else if (list[i].lottoNumber in 21..30) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_e4807b)
                } else if (list[i].lottoNumber in 31..40) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_a689ee)
                } else {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.bg_circle_57d281)
                }

                holder.layout_win_number.addView(selectedLottoBinding.root)
                if (i < list.size - 1) {
                    (selectedLottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
                }
            }
        }

        holder.text_win_announce.setOnClickListener {
            if(StringUtils.isNotEmpty(item.winAnnounceUrl)){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.winAnnounceUrl))
                holder.itemView.context.startActivity(intent)
            }
        }
        holder.layout_detail.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(holder.itemView.context as FragmentActivity, null)) {
                return@setOnClickListener
            }
            val intent = Intent(holder.itemView.context, LuckyLottoDetailActivity::class.java)
            intent.putExtra(Const.DATA, item)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            holder.itemView.context.startActivity(intent)
        }

        holder.layout_winner_title.visibility = View.GONE

        val adapter = LottoWinnerAdapter()
        holder.recylcer_winner.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recylcer_winner.adapter = adapter

        val params = HashMap<String, String>()
        params["eventSeqNo"] = item.no.toString()
        params["page"] = "0"
        params["size"] = "2"
        params["sort"] = "id,desc"
        ApiBuilder.create().getLottoWinnerList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventWinJpa>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<EventWinJpa>>>?,
                                    response: NewResultResponse<SubResultResponse<EventWinJpa>>?) {

                if (response?.data != null) {
                    val totalCount = response.data!!.totalElements!!
                    if(totalCount > 0){
                        holder.layout_winner_title.visibility = View.VISIBLE
                        if(response.data!!.totalElements!! > 1){
                            holder.text_winner_desc.visibility = View.VISIBLE
                            holder.text_winner_desc.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_lucky_lotto_winner_desc, response.data!!.totalElements.toString()))
                        }else{
                            holder.text_winner_desc.text = holder.itemView.context.getString(R.string.msg_lucky_lotto_gift_desc)
                        }
                    }

                    adapter.setDataList(response.data!!.content!! as MutableList<EventWinJpa>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<EventWinJpa>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<EventWinJpa>>?) {
            }

        }).build().call()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLottoHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}