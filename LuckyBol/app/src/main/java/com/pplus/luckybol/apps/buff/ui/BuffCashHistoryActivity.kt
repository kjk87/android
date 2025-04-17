package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.buff.data.BuffLogAdapter
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Buff
import com.pplus.luckybol.core.network.model.dto.BuffDividedBolLog
import com.pplus.luckybol.core.network.model.dto.BuffMember
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.ActivityBuffCashHistoryBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call

class BuffCashHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "BuffCashHistoryActivity"
    }

    private lateinit var binding: ActivityBuffCashHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffCashHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage = 1
    private var mTotalCount = 0
    private var mLockListView = true
    private var mSort = "seqNo,desc"
    private var mAdapter: BuffLogAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mIsLast = false
    private var mBuff: Buff? = null
    private var mType = "point"

    override fun initializeView(savedInstanceState: Bundle?) {
        mType = intent.getStringExtra(Const.TYPE)!!

        binding.textBuffCashHistorySortPast.setOnClickListener {
            mSort = "seqNo,asc"
            binding.textBuffCashHistorySortPast.isSelected = true
            binding.textBuffCashHistorySortRecent.isSelected = false

            mPage = 0
            listCall(mPage)
        }

        binding.textBuffCashHistorySortRecent.setOnClickListener {
            mSort = "seqNo,desc"
            binding.textBuffCashHistorySortPast.isSelected = false
            binding.textBuffCashHistorySortRecent.isSelected = true

            mPage = 0
            listCall(mPage)
        }

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerBuffCashHistory.layoutManager = mLayoutManager
        mAdapter = BuffLogAdapter()
        binding.recyclerBuffCashHistory.adapter = mAdapter //        recycler_point_history.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_20))

        binding.recyclerBuffCashHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mSort = "seqNo,desc"
        binding.textBuffCashHistorySortPast.isSelected = false
        binding.textBuffCashHistorySortRecent.isSelected = true

        when(mType){
            "point"->{
                setTitle(getString(R.string.word_buff_cash_history))
                binding.textBuffCashHistoryTotalCashTitle.text = getString(R.string.word_total_buff_cash)
                binding.textBuffCashHistoryTop3Title.text = getString(R.string.word_divided_cash_top3)
            }
            "bol"->{
                setTitle(getString(R.string.word_buff_bol_history))
                binding.textBuffCashHistoryTotalCashTitle.text = getString(R.string.word_total_buff_bol)
                binding.textBuffCashHistoryTop3Title.text = getString(R.string.word_divided_bol_top3)
            }
        }

        getMyBuff()
    }

    private fun getMyBuff() {
        showProgress("")
        ApiBuilder.create().getBuffMember().setCallback(object : PplusCallback<NewResultResponse<BuffMember>> {
            override fun onResponse(call: Call<NewResultResponse<BuffMember>>?,
                                    response: NewResultResponse<BuffMember>?) {

                hideProgress()
                if (response?.data != null && response.data!!.buff != null) {
                    mBuff = response.data!!.buff!!
                    when(mType){
                        "point"->{
                            binding.textBuffCashHistoryTotalCash.text = FormatUtil.getMoneyTypeFloat(mBuff!!.totalDividedPoint.toString())
                            binding.textBuffCashHistoryMyCash.text = FormatUtil.getMoneyTypeFloat(response.data!!.receivedPoint.toString())
                        }
                        "bol"->{
                            binding.textBuffCashHistoryTotalCash.text = FormatUtil.getMoneyTypeFloat(mBuff!!.totalDividedBol.toString())
                            binding.textBuffCashHistoryMyCash.text = FormatUtil.getMoneyTypeFloat(response.data!!.receivedBol.toString())
                        }
                    }


//                    getTop3(mBuff!!.seqNo!!)
                    mPage = 0
                    listCall(mPage)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<BuffMember>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<BuffMember>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun  getTop3(buffSeqNo:Long) {
        val params = HashMap<String, String>()
        params["buffSeqNo"] = buffSeqNo.toString()
        params["size"] = "3"
        params["sort"] = "dividedPoint,desc"
        params["includeMe"] = "true"
        ApiBuilder.create().getBuffMemberList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffMember>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffMember>>?) {


                if(response?.data != null && response.data!!.content!!.isNotEmpty()){


                    binding.layoutBuffCashTop3.visibility = View.VISIBLE

                    when(mType){
                        "point"->{
                            val top1 = response.data!!.content!![0]

                            Glide.with(this@BuffCashHistoryActivity).load(top1.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop1Profile)
                            binding.textBuffCashTop1Name.text = top1.member!!.nickname
                            binding.textBuffCashTop1Cash.text = FormatUtil.getMoneyTypeFloat(top1.dividedPoint.toString())

                            if(response.data!!.content!!.size > 1){
                                val top2 = response.data!!.content!![1]
                                Glide.with(this@BuffCashHistoryActivity).load(top2.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop2Profile)
                                binding.textBuffCashTop2Name.text = top2.member!!.nickname
                                binding.textBuffCashTop2Cash.text = FormatUtil.getMoneyTypeFloat(top2.dividedPoint.toString())
                            }

                            if(response.data!!.content!!.size > 2){
                                val top3 = response.data!!.content!![2]
                                Glide.with(this@BuffCashHistoryActivity).load(top3.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop3Profile)
                                binding.textBuffCashTop3Name.text = top3.member!!.nickname
                                binding.textBuffCashTop3Cash.text = FormatUtil.getMoneyTypeFloat(top3.dividedPoint.toString())
                            }
                        }
                        "bol"->{
                            val top1 = response.data!!.content!![0]

                            Glide.with(this@BuffCashHistoryActivity).load(top1.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop1Profile)
                            binding.textBuffCashTop1Name.text = top1.member!!.nickname
                            binding.textBuffCashTop1Cash.text = FormatUtil.getMoneyTypeFloat(top1.dividedBol.toString())

                            if(response.data!!.content!!.size > 1){
                                val top2 = response.data!!.content!![1]
                                Glide.with(this@BuffCashHistoryActivity).load(top2.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop2Profile)
                                binding.textBuffCashTop2Name.text = top2.member!!.nickname
                                binding.textBuffCashTop2Cash.text = FormatUtil.getMoneyTypeFloat(top2.dividedBol.toString())
                            }

                            if(response.data!!.content!!.size > 2){
                                val top3 = response.data!!.content!![2]
                                Glide.with(this@BuffCashHistoryActivity).load(top3.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageBuffCashTop3Profile)
                                binding.textBuffCashTop3Name.text = top3.member!!.nickname
                                binding.textBuffCashTop3Cash.text = FormatUtil.getMoneyTypeFloat(top3.dividedBol.toString())
                            }
                        }
                    }


                }else{
                    binding.layoutBuffCashTop3.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffMember>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffMember>>?) {

            }
        }).build().call()

    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        params["buffSeqNo"] = mBuff!!.seqNo.toString()
        params["moneyType"] = "point"
        params["sort"] = mSort
        params["page"] = page.toString()
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getBuffLogList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuffDividedBolLog>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuffDividedBolLog>>>?,
                                    response: NewResultResponse<SubResultResponse<BuffDividedBolLog>>?) {
                hideProgress()
                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            binding.textBuffCashtHistoryNotExist.visibility = View.VISIBLE
                        } else {
                            binding.textBuffCashtHistoryNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuffDividedBolLog>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<BuffDividedBolLog>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buff_cash_history), ToolbarOption.ToolbarMenu.LEFT) //        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_free_charge_station))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {

                }
            }
        }
    }
}
