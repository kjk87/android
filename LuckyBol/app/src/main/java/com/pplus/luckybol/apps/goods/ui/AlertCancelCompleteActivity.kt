package com.pplus.luckybol.apps.goods.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertCancelCompleteBinding

class AlertCancelCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertCancelCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertCancelCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.imageAlertCancelCompleteClose.setOnClickListener {
            finish()
        }

        binding.imageAlertCancelCompleteConfirm.setOnClickListener {
            finish()
        }

    }

}
