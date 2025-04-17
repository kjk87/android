package com.pplus.prnumberuser.apps.menu.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityAlertVisitOrderBinding

class AlertVisitOrderActivity : BaseActivity() {

    private lateinit var binding: ActivityAlertVisitOrderBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertVisitOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        binding.imageAlertVisitOrder.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.textAlertVisitOrder.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

}