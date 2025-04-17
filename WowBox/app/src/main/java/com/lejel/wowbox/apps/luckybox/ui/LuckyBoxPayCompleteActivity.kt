package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxPayCompleteBinding
import com.pplus.utils.part.format.FormatUtil

class LuckyBoxPayCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivityLuckyBoxPayCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxPayCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val luckyBoxPurchase = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchase::class.java)!!
        setEvent("wowbox_buyComplete")
//        binding.textLuckyBoxPayCompleteUsePoint.text = getString(R.string.format_cash_unit, "-${FormatUtil.getMoneyTypeFloat(luckyBoxPurchase.usePoint.toString())}")
        binding.textLuckyBoxPayCompletePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(luckyBoxPurchase.pgPrice.toString()))
        binding.textLuckyBoxPayCompleteTitle.text = luckyBoxPurchase.title
        binding.textLuckyBoxPayCompleteCount.text = getString(R.string.format_count_unit, luckyBoxPurchase.quantity.toString())

        binding.textLuckyBoxPayCompleteContainer.setOnClickListener {
            val intent = Intent(this, LuckyBoxContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        }

        binding.textLuckyBoxPayCompleteConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}