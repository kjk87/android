package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertLuckyBoxExchangeBolBinding

class AlertLuckyBoxExchangeBolActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyBoxExchangeBolBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyBoxExchangeBolBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val luckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)!!

        binding.textAlertLuckyBoxExchangeBolAmount.text = getString(R.string.format_lucky_box_bol_back_desc1, luckyBoxPurchaseItem.refundBol.toString())
        binding.textAlertLuckyBoxExchangeBolCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.textAlertLuckyBoxExchangeBolConfirm.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.DATA, luckyBoxPurchaseItem)
            setResult(RESULT_OK, data)
            finish()

        }

    }


}
