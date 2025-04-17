package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityAlertFirstBenefitCompleteBinding

class AlertFirstBenefitCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertFirstBenefitCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertFirstBenefitCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        binding.textAlertFirstBenefitCompleteConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}
