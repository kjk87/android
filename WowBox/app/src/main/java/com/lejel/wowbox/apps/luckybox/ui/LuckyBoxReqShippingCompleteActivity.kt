package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxDeliveryPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBoxReqShippingCompleteBinding

class LuckyBoxReqShippingCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivityLuckyBoxReqShippingCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxReqShippingCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setEvent("wowbox_completePayShipping")
        val luckyBoxDeliveryPurchase = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxDeliveryPurchase::class.java)!!

        binding.textLuckyBoxReqShippingCompleteShippingPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(luckyBoxDeliveryPurchase.price.toString()))

        if(luckyBoxDeliveryPurchase.usePoint != null && luckyBoxDeliveryPurchase.usePoint!! > 0){
            binding.textLuckyBoxReqShippingCompleteUsePoint.text = getString(R.string.format_point_unit, "-${FormatUtil.getMoneyTypeFloat(luckyBoxDeliveryPurchase.usePoint.toString())}")
        }else{
            binding.textLuckyBoxReqShippingCompleteUsePoint.text = getString(R.string.format_point_unit, "0")
        }


        binding.textLuckyBoxReqShippingCompletePgPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyTypeFloat(luckyBoxDeliveryPurchase.pgPrice.toString()))

        binding.textLuckyBoxReqShippingCompleteContainer.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.textLuckyBoxReqShippingCompleteMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}