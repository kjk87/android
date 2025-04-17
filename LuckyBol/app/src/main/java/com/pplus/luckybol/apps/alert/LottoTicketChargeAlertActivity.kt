package com.pplus.luckybol.apps.alert

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityLottoTicketChargeAlertBinding

class LottoTicketChargeAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLottoTicketChargeAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoTicketChargeAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textLottoTicketChargeCancel.setOnClickListener {
            finish()
        }

        binding.textLottoTicketChargeConfirm.setOnClickListener {
            PplusCommonUtil.share(this)
        }

    }


    override fun onPause() {
        super.onPause()
    }
}
