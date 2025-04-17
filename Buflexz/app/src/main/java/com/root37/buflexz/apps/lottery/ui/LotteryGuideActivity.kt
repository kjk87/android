package com.root37.buflexz.apps.lottery.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.giftcard.ui.GiftCardBrandActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Lottery
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLotteryGuideBinding
import retrofit2.Call

class LotteryGuideActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLotteryGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityLotteryGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textLotteryGuideUrl.setOnClickListener {
            PplusCommonUtil.openChromeWebView(this, "https://m.dhlottery.co.kr/gameResult.do?method=byWin")
        }

        binding.textLotteryGuideQna1Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna1_title))
        binding.textLotteryGuideQna1Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna1_desc))
        binding.textLotteryGuideQna2Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna2_title))
        binding.textLotteryGuideQna2Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna2_desc))
        binding.textLotteryGuideQna3Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna3_title))
        binding.textLotteryGuideQna3Contents.text = PplusCommonUtil.fromHtml(getString(R.string.html_lottery_qna3_desc))

        binding.textLotteryGuideGiftShop.setOnClickListener {
            val intent = Intent(this, GiftCardBrandActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        getLottery()
    }

    private fun getLottery() {
        showProgress("")
        ApiBuilder.create().getLottery().setCallback(object : PplusCallback<NewResultResponse<Lottery>> {
            override fun onResponse(call: Call<NewResultResponse<Lottery>>?, response: NewResultResponse<Lottery>?) {
                hideProgress()
                if(response?.result != null){
                    val lottery = response.result!!

                    if(lottery.firstType == "point"){
                        binding.textLotteryGuide1stPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.firstMoney.toString()))
                    }else{
                        binding.textLotteryGuide1stPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.firstMoney.toString()))
                    }

                    if(lottery.secondType == "point"){
                        binding.textLotteryGuide2ndPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.secondMoney.toString()))
                    }else{
                        binding.textLotteryGuide2ndPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.secondMoney.toString()))
                    }

                    if(lottery.thirdType == "point"){
                        binding.textLotteryGuide3rdPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.thirdMoney.toString()))
                    }else{
                        binding.textLotteryGuide3rdPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.thirdMoney.toString()))
                    }

                    if(lottery.forthType == "point"){
                        binding.textLotteryGuide4thPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.forthMoney.toString()))
                    }else{
                        binding.textLotteryGuide4thPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.forthMoney.toString()))
                    }

                    if(lottery.fifthType == "point"){
                        binding.textLotteryGuide5thPrice.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(lottery.fifthMoney.toString()))
                    }else{
                        binding.textLotteryGuide5thPrice.text = getString(R.string.format_lottery_unit, FormatUtil.getMoneyType(lottery.fifthMoney.toString()))
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Lottery>>?, t: Throwable?, response: NewResultResponse<Lottery>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_use_method), ToolbarOption.ToolbarMenu.LEFT)
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