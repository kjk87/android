package com.pplus.prnumberuser.apps.bol.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityCashChangeLackAlertBinding

class CashChangeLackAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding:ActivityCashChangeLackAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityCashChangeLackAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.layoutCashChangeLackAlertAdpopcorn.setOnClickListener {
//            adPopCorn()
        }

        binding.textCashChangeLackConfirm.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
            }
        })
    }
}
