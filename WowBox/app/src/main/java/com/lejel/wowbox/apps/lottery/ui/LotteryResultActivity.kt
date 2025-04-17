package com.lejel.wowbox.apps.lottery.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.lottery.data.LotteryWinHeaderAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Lottery
import com.lejel.wowbox.core.network.model.dto.LotteryWin
import com.lejel.wowbox.core.network.model.dto.LotteryWinCondition
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLotteryResultBinding
import com.lejel.wowbox.databinding.ItemLottoBinding
import retrofit2.Call

class LotteryResultActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLotteryResultBinding

    override fun getLayoutView(): View {
        binding = ActivityLotteryResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mLottery: Lottery? = null
    private var mTotalCount = 0
    var mAdapter: LotteryWinHeaderAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
//    private var mLockListView = false
    private var mPaging = 1
    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = LotteryWinHeaderAdapter()
        binding.recyclerLotteryResultWin.adapter = mAdapter
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerLotteryResultWin.layoutManager = mLayoutManager

        mAdapter!!.listener = object : LotteryWinHeaderAdapter.OnItemClickListener{
            override fun clickMyResult() {
                val intent = Intent(this@LotteryResultActivity, MyLotteryWinActivity::class.java)
                intent.putExtra(Const.DATA, mLottery)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

//        binding.recyclerLotteryResultWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//
//                visibleItemCount = mLayoutManager.childCount
//                totalItemCount = mLayoutManager.itemCount
//                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        getTotalLotteryWinList(mPaging)
//                    }
//                }
//            }
//        })

        getLottery()
    }

    private fun getLottery() {
        showProgress("")
        ApiBuilder.create().getLottery().setCallback(object : PplusCallback<NewResultResponse<Lottery>> {
            override fun onResponse(call: Call<NewResultResponse<Lottery>>?, response: NewResultResponse<Lottery>?) {
                hideProgress()
                if (response?.result != null) {

                    getLotteryRoundList()
                    getLotteryByRound(response.result!!.lotteryRound!! -1)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
                hideProgress()
            }
        }).build().call()
    }


    private fun getLotteryRoundList() {
        ApiBuilder.create().getLotteryRoundList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Lottery>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Lottery>>>?, response: NewResultResponse<ListResultResponse<Lottery>>?) {
                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    val roundList = response.result!!.list!!
                    binding.textLotteryResultTitle.setOnClickListener {
                        val contentList = arrayListOf<String>()
                        for (round in roundList) {
                            contentList.add(getString(R.string.format_lotto_win_number, round.lotteryRound.toString()))
                        }
                        val builder = AlertBuilder.Builder()
                        builder.setContents(contentList)
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                val round = roundList[event_alert.value - 1]
                                binding.textLotteryResultTitle.text = PplusCommonUtil.fromHtml(getString(R.string.format_lotto_win_number, round.lotteryRound.toString()))
                                getLotteryByRound(round.lotteryRound!!)

                            }
                        }).builder().show(this@LotteryResultActivity)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Lottery>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Lottery>>?) {

            }
        }).build().call()
    }

    private fun getLotteryByRound(lotteryRound: Int) {
        showProgress("")
        ApiBuilder.create().getLotteryByLotteryRound(lotteryRound).setCallback(object : PplusCallback<NewResultResponse<Lottery>> {
            override fun onResponse(call: Call<NewResultResponse<Lottery>>?, response: NewResultResponse<Lottery>?) {
                hideProgress()
                if (response?.result != null) {
                    mLottery = response.result
                    if(mLottery!!.no1 == null){
                        getLotteryByRound(mLottery!!.lotteryRound!! -1)
                        return
                    }
                    binding.textLotteryResultTitle.text = PplusCommonUtil.fromHtml(getString(R.string.format_lotto_win_number, mLottery!!.lotteryRound.toString()))
                    mAdapter!!.mLottery = mLottery
                    binding.layoutLottoResultNumber.removeAllViews()
                    val numberList = arrayListOf<Int>()
                    numberList.add(mLottery!!.no1!!)
                    numberList.add(mLottery!!.no2!!)
                    numberList.add(mLottery!!.no3!!)
                    numberList.add(mLottery!!.no4!!)
                    numberList.add(mLottery!!.no5!!)
                    numberList.add(mLottery!!.no6!!)

                    for (number in numberList) {
                        val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this@LotteryResultActivity), LinearLayout(this@LotteryResultActivity), false)
                        lottoBinding.textLottoNumber.text = number.toString()
                        lottoBinding.textLottoNumber.layoutParams.width = resources.getDimensionPixelSize(R.dimen.width_80)
                        lottoBinding.textLottoNumber.layoutParams.height = resources.getDimensionPixelSize(R.dimen.width_80)
                        if (number in 1..10) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
                        } else if (number in 11..20) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
                        } else if (number in 21..30) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
                        } else if (number in 31..40) {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
                        } else {
                            lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
                        }
                        (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.width_6)
                        binding.layoutLottoResultNumber.addView(lottoBinding.root)
                    }

                    val plusText = TextView(this@LotteryResultActivity)
                    plusText.setTextColor(ResourceUtil.getColor(this@LotteryResultActivity, R.color.color_dcdcdc))
                    plusText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_36pt).toFloat())
                    plusText.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_24), 0)
                    plusText.text = "+"
                    binding.layoutLottoResultNumber.addView(plusText)

                    val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this@LotteryResultActivity), LinearLayout(this@LotteryResultActivity), false)
                    lottoBinding.textLottoNumber.text = mLottery!!.bonusNo.toString()
                    lottoBinding.textLottoNumber.layoutParams.width = resources.getDimensionPixelSize(R.dimen.width_80)
                    lottoBinding.textLottoNumber.layoutParams.height = resources.getDimensionPixelSize(R.dimen.width_80)
                    if (mLottery!!.bonusNo in 1..10) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ffc046)
                    } else if (mLottery!!.bonusNo in 11..20) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_457eef)
                    } else if (mLottery!!.bonusNo in 21..30) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ff4e4e)
                    } else if (mLottery!!.bonusNo in 31..40) {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_ad7aff)
                    } else {
                        lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_circle_5ecb69)
                    }
                    binding.layoutLottoResultNumber.addView(lottoBinding.root)

                    getLotteryWinCondition()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getLotteryWinCondition() {
        showProgress("")
        ApiBuilder.create().getLotteryWinCondition(mLottery!!.lotteryRound!!).setCallback(object : PplusCallback<NewResultResponse<LotteryWinCondition>> {
            override fun onResponse(call: Call<NewResultResponse<LotteryWinCondition>>?, response: NewResultResponse<LotteryWinCondition>?) {
                hideProgress()
                if (response?.result != null) {
                    mAdapter!!.mLotteryWinCondition = response.result
                    mPaging = 1
                    getTotalLotteryWinList(mPaging)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<LotteryWinCondition>>?, t: Throwable?, response: NewResultResponse<LotteryWinCondition>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getTotalLotteryWinList(page: Int) {
        val params = HashMap<String, String>()
//        params["paging[page]"] = page.toString()
//        params["paging[limit]"] = "20"
        showProgress("")
//        mLockListView = true
        ApiBuilder.create().getTotalLotteryWinList(mLottery!!.lotteryRound!!, params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LotteryWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LotteryWin>>>?, response: NewResultResponse<ListResultResponse<LotteryWin>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()
//                        mTotalCount = response.result!!.total!!
                    }

//                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.setDataList(dataList as MutableList<LotteryWin>)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LotteryWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LotteryWin>>?) {
                hideProgress()
            }
        }).build().call()
    }

    val winLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mPaging = 1
        getTotalLotteryWinList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}