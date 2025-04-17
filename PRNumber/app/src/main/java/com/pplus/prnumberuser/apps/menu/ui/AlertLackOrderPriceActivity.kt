package com.pplus.prnumberuser.apps.menu.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertLackOrderPriceBinding
import com.pplus.utils.part.format.FormatUtil

class AlertLackOrderPriceActivity : BaseActivity() {

    private lateinit var binding: ActivityAlertLackOrderPriceBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLackOrderPriceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val lackPrice = intent.getIntExtra(Const.PRICE, 0)

        binding.textAlertLackOrderPriceDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_lack_order_price_desc, FormatUtil.getMoneyType(lackPrice.toString())))

        binding.textAlertLackOrderPriceConfirm.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.textAlertLackOrderPriceAddMenu.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}