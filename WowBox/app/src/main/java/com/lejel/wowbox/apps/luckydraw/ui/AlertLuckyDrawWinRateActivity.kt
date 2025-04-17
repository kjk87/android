package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertLuckyDrawWinRateBinding

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
