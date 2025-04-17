package com.root37.buflexz.apps.luckyball.ui

import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.GiftCardPurchase
import com.root37.buflexz.databinding.ActivityAlertLuckyBallLackBinding

class AlertLuckyBallLackActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyBallLackBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyBallLackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val lackBall = intent.getIntExtra(Const.DATA, 0)

        binding.textAlertLuckyBallLackBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(lackBall.toString()))
        binding.textAlertLuckyBallLackCancel.setOnClickListener {
            finish()
        }

        binding.textAlertLuckyBallLackEarnMethod.setOnClickListener {
            finish()
        }

    }
}
