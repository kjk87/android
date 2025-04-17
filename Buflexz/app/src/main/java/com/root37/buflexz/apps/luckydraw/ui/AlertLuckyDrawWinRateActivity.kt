package com.root37.buflexz.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.dto.LuckyDrawNumber
import com.root37.buflexz.core.network.model.dto.LuckyDrawPurchase
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityAlertLuckyDrawJoinBinding
import com.root37.buflexz.databinding.ActivityAlertLuckyDrawWinRateBinding
import retrofit2.Call

class AlertLuckyDrawWinRateActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyDrawWinRateBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyDrawWinRateBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mLuckyDraw: LuckyDraw
    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!

        binding.textAlertLuckyDrawWinRate1Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_win_rate_title, "1"))
        binding.textAlertLuckyDrawWinRate1.text = mLuckyDraw.winRate1

        binding.textAlertLuckyDrawWinRate2Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_win_rate_title, "10"))
        binding.textAlertLuckyDrawWinRate2.text = mLuckyDraw.winRate2

        binding.textAlertLuckyDrawWinRate3Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_win_rate_title, "20"))
        binding.textAlertLuckyDrawWinRate3.text = mLuckyDraw.winRate3

        binding.textAlertLuckyDrawWinRateConfirm.setOnClickListener {
            finish()
        }

    }
}
