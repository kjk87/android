package com.lejel.wowbox.apps.lottery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ItemLottoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.core.network.model.dto.LotteryWin
import com.lejel.wowbox.databinding.ActivityAlertLotteryWinBinding
import retrofit2.Call

class AlertLotteryWinActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLotteryWinBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLotteryWinBinding.inflate(layoutInflater)
        return binding.root
    }

    var mLotteryWin: LotteryWin? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mLotteryWin = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LotteryWin::class.java)!!

        binding.textAlertLotteryWinConfirm.setOnClickListener {
            val params = HashMap<String, String>()
            params["lotteryRound"] = mLotteryWin!!.lotteryRound.toString()
            showProgress("")
            ApiBuilder.create().receiveLotteryWin(mLotteryWin!!.seqNo!!, params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    setEvent("lottery_receive")
                    hideProgress()
                    showAlert(R.string.msg_complete_receive_win)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

        binding.textAlertLotteryWinTitle.text = getString(R.string.format_cong_win, mLotteryWin!!.grade.toString())
        when(mLotteryWin!!.giftType){
            "point"->{
                binding.textAlertLotteryWinGift.text = getString(R.string.format_lotto_win_point, FormatUtil.getMoneyType(mLotteryWin!!.money.toString()))
            }
            "cash"->{
                binding.textAlertLotteryWinGift.text = getString(R.string.format_lotto_win_cash, FormatUtil.getMoneyType(mLotteryWin!!.money.toString()))
            }
            "lotto"->{
                binding.textAlertLotteryWinGift.text = getString(R.string.format_lotto_win_lotto, FormatUtil.getMoneyType(mLotteryWin!!.money.toString()))
            }
        }

        binding.layoutAlertLotteryWinNumber.removeAllViews()

        val numberList = arrayListOf<Int>()
        numberList.add(mLotteryWin!!.no1!!)
        numberList.add(mLotteryWin!!.no2!!)
        numberList.add(mLotteryWin!!.no3!!)
        numberList.add(mLotteryWin!!.no4!!)
        numberList.add(mLotteryWin!!.no5!!)
        numberList.add(mLotteryWin!!.no6!!)

        val isWinList = arrayListOf<Boolean>()
        isWinList.add(mLotteryWin!!.winNo1!!)
        isWinList.add(mLotteryWin!!.winNo2!!)
        isWinList.add(mLotteryWin!!.winNo3!!)
        isWinList.add(mLotteryWin!!.winNo4!!)
        isWinList.add(mLotteryWin!!.winNo5!!)
        isWinList.add(mLotteryWin!!.winNo6!!)

        for (j in 0 until numberList.size) {
            val lottoNumber = numberList[j]
            val lottoBinding = ItemLottoBinding.inflate(LayoutInflater.from(this), LinearLayout(this), false)
            lottoBinding.textLottoNumber.text = lottoNumber.toString()

            lottoBinding.textLottoNumber.layoutParams.width = resources.getDimensionPixelSize(R.dimen.width_120)
            lottoBinding.textLottoNumber.layoutParams.height = resources.getDimensionPixelSize(R.dimen.width_120)

            if (lottoNumber in 1..10) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_lottery_number_1)
            } else if (lottoNumber in 11..20) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_lottery_number_2)
            } else if (lottoNumber in 21..30) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_lottery_number_3)
            } else if (lottoNumber in 31..40) {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_lottery_number_4)
            } else {
                lottoBinding.textLottoNumber.setBackgroundResource(R.drawable.bg_lottery_number_5)
            }



            val isAccord = isWinList[j]

            if(mLotteryWin!!.grade == 2 && !isAccord){
                lottoBinding.textLottoNumber.isSelected = mLotteryWin!!.winAdd != null && mLotteryWin!!.winAdd!!
            }else{
                lottoBinding.textLottoNumber.isSelected = isAccord
            }

            binding.layoutAlertLotteryWinNumber.addView(lottoBinding.root)
            if (j < numberList.size - 1) {
                (lottoBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.height_6)
            }
        }
    }


}
