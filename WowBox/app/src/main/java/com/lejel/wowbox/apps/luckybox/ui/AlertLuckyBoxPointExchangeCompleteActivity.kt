package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.databinding.ActivityAlertLuckyBoxPointExchangeCompleteBinding
import com.pplus.utils.part.format.FormatUtil

class AlertLuckyBoxPointExchangeCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyBoxPointExchangeCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyBoxPointExchangeCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val point = intent.getIntExtra(Const.POINT, 0)


        binding.textAlertLuckyBoxPointExchangeCompletePoint.text = FormatUtil.getMoneyType(point.toString())

        binding.textAlertLuckyBoxPointExchangeCompleteConfirm.setOnClickListener {

            setResult(RESULT_OK)
            finish()
        }

        binding.textAlertLuckyBoxPointExchangeCompleteMovePointMall.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Const.KEY, Const.POINT_MALL)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }


}
