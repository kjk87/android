package com.pplus.luckybol.apps.alert

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertBolLackBinding

class AlertBolLackActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertBolLackBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBolLackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAlertBolLackConfirm.setOnClickListener {
            finish()
        }
    }


    override fun onPause() {
        super.onPause()
    }
}
