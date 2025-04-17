package com.lejel.wowbox.apps.withdraw.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertWithdrawReturnReasonBinding

class AlertWithdrawReturnReasonActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertWithdrawReturnReasonBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertWithdrawReturnReasonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val withdraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Withdraw::class.java)!!

        binding.textAlertWithdrawReturnReason.text = withdraw.comment
        binding.textAlertWithdrawReturnReasonConfirm.setOnClickListener {
            finish()
        }

    }
}
