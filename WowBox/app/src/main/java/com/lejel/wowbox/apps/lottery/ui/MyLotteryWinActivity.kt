package com.lejel.wowbox.apps.lottery.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.lottery.data.LotteryWinLoadingView
import com.lejel.wowbox.apps.lottery.data.MyLotteryWinAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Lottery
import com.lejel.wowbox.core.network.model.dto.LotteryWin
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityMyLotteryWinBinding
import com.lejel.wowbox.databinding.ItemLottoBinding
import retrofit2.Call

class MyLotteryWinActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMyLotteryWinBinding

    override fun getLayoutView(): View {
        binding = ActivityMyLotteryWinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mLottery: Lottery? = null
    private var mTotalCount = 0
    var mAdapter: MyLotteryWinAdapter? = null
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mLockListView = false
    private var mPaging = 1
    override fun initializeView(savedInstanceState: Bundle?) {
        mLottery = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Lottery::class.java)
        mAdapter = MyLotteryWinAdapter()
        binding.recyclerMyLotteryWin.adapter = mAdapter
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerMyLotteryWin.layoutManager = mLayoutManager

        binding.recyclerMyLotteryWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        getMyLotteryWinList(mPaging)
                    }
                }
            }
        })

        mAdapter!!.listener = object : MyLotteryWinAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                if (item.grade!! < 4) {
                    val loading = LotteryWinLoadingView()
                    loading.isCancelable = false
                    try {
                        loading.show(supportFragmentManager, "")
                    } catch (e: Exception) {
                        LogUtil.e(LOG_TAG, e.toString())
                    }

                    val handler = Handler(Looper.myLooper()!!)
                    handler.postDelayed({
                        try {
                            loading.dismiss()
                        } catch (e: Exception) {
                            LogUtil.e(LOG_TAG, e.toString())
                        }

                        val intent = Intent(this@MyLotteryWinActivity, AlertLotteryWinActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        winLauncher.launch(intent)
                    }, 5500)
                } else {
                    val intent = Intent(this@MyLotteryWinActivity, AlertLotteryWinActivity::class.java)
                    intent.putExtra(Const.DATA, item)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    winLauncher.launch(intent)
                }
            }
        }
        binding.textMyLotteryWinTitle.text = PplusCommonUtil.fromHtml(getString(R.string.format_lotto_win_number, (mLottery!!.lotteryRound!!).toString()))
        getLotteryRoundList()
        getLotteryByRound(mLottery!!.lotteryRound!!)
    }

    private fun getRemainCount() {
        val params = HashMap<String, String>()
        params["lotteryRound"] = mLottery!!.lotteryRound.toString()
        ApiBuilder.create().getLotteryWinRemainCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                binding.textLotteryWinTotalReceive.setText(R.string.msg_total_receive_lotto_ticket)
                if (response?.result != null && response.result!! > 0) {
                    binding.textLotteryWinTotalReceive.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
                    binding.textLotteryWinTotalReceiveDesc.visibility = View.VISIBLE
                    binding.textLotteryWinTotalReceive.setOnClickListener {
                        totalReceive()
                    }
                } else {
                    binding.textLotteryWinTotalReceive.setBackgroundResource(R.drawable.bg_dcdcdc_radius_27)
                    binding.textLotteryWinTotalReceiveDesc.visibility = View.GONE
                    binding.textLotteryWinTotalReceive.setOnClickListener {}
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
            }
        }).build().call()
    }

    private fun totalReceive() {
        val params = HashMap<String, String>()
        params["lotteryRound"] = mLottery!!.lotteryRound.toString()
        showProgress("")
        ApiBuilder.create().totalReceiveLotteryWin(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                setEvent("lottery_receiveAll")
                hideProgress()
                showAlert(R.string.msg_complete_receive_lotto)
                getLotteryByRound(mLottery!!.lotteryRound!!)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getLotteryRoundList() {
        ApiBuilder.create().getLotteryRoundList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Lottery>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Lottery>>>?, response: NewResultResponse<ListResultResponse<Lottery>>?) {
                if (response?.result != null && response.result!!.list != null && response.result!!.list!!.isNotEmpty()) {
                    val roundList = response.result!!.list!!
                    binding.textMyLotteryWinTitle.setOnClickListener {
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
                                binding.textMyLotteryWinTitle.text = PplusCommonUtil.fromHtml(getString(R.string.format_lotto_win_number, round.lotteryRound.toString()))
                                getLotteryByRound(round.lotteryRound!!)
                            }
                        }).builder().show(this@MyLotteryWinActivity)
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
                    binding.layoutYLotteryWinNumber.removeAllViews()
                    val numberList = arrayListOf<Int>()
                    numberList.add(mLottery!!.no1!!)
                    numberList.add(mLottery!!.no2!!)
                    numberList.add(mLottery!!.no3!!)
                    numberList.add(mLottery!!.no4!!)
                    numberList.add(mLottery!!.no5!!)
                    numberList.add(mLottery!!.no6!!)

                    for (number in numberList) {
                        val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this@MyLotteryWinActivity), LinearLayout(this@MyLotteryWinActivity), false)
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
                        binding.layoutYLotteryWinNumber.addView(lottoBinding.root)
                    }

                    val plusText = TextView(this@MyLotteryWinActivity)
                    plusText.setTextColor(ResourceUtil.getColor(this@MyLotteryWinActivity, R.color.color_dcdcdc))
                    plusText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_36pt).toFloat())
                    plusText.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_24), 0)
                    plusText.text = "+"
                    binding.layoutYLotteryWinNumber.addView(plusText)

                    val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this@MyLotteryWinActivity), LinearLayout(this@MyLotteryWinActivity), false)
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
                    binding.layoutYLotteryWinNumber.addView(lottoBinding.root)

                    mPaging = 1
                    getMyLotteryWinList(mPaging)

                }

            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getMyLotteryWinList(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getMyLotteryWinList(mLottery!!.lotteryRound!!, params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LotteryWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LotteryWin>>>?, response: NewResultResponse<ListResultResponse<LotteryWin>>?) {
                hideProgress()
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if(mTotalCount > 0){
                            getRemainCount()
                        }else{
                            binding.textLotteryWinTotalReceive.setBackgroundResource(R.drawable.bg_303030_radius_26)
                            binding.textLotteryWinTotalReceive.setText(R.string.msg_not_win)
                            binding.textLotteryWinTotalReceiveDesc.visibility = View.GONE
                            binding.textLotteryWinTotalReceive.setOnClickListener {}
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
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
        getMyLotteryWinList(mPaging)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_winning_history), ToolbarOption.ToolbarMenu.LEFT)
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