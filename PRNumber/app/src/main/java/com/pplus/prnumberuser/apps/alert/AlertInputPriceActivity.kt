package com.pplus.prnumberuser.apps.alert

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertInputPriceBinding
import com.pplus.utils.part.format.FormatUtil

class AlertInputPriceActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertInputPriceBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertInputPriceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val installment = intent.getStringExtra(Const.INSTALLMENT)
        val price = intent.getIntExtra(Const.PRICE, 0)

        binding.textAlertInputPricePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(price.toString())))
        if (installment == "00") {
            binding.textAlertInputPriceInstallment.text = "(${getString(R.string.word_one_pay)})"
        } else {
            binding.textAlertInputPriceInstallment.text = "(${getString(R.string.format_installment_period, installment?.toInt().toString())})"
        }
        binding.textAlertInputPriceConfirm.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.textAlertInputPriceCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}
