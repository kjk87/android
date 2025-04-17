package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityAlertPageCashBackCompleteBinding
import com.pplus.utils.part.format.FormatUtil

class AlertPageCashBackCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertPageCashBackCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertPageCashBackCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val cash = intent.getIntExtra(Const.CASH, 0)


        binding.textAlertPageCashBachCompleteCash.text = getString(R.string.format_cash_unit4, FormatUtil.getMoneyType(cash.toString()))


        binding.textAlertPageCashBachCompleteConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}
