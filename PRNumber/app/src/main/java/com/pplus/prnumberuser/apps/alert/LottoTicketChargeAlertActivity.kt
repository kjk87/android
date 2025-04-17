package com.pplus.prnumberuser.apps.alert

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
import com.pplus.prnumberuser.databinding.ActivityLottoTicketChargeAlertBinding

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
            val intent = Intent(this, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }


    override fun onPause() {
        super.onPause()
    }
}
