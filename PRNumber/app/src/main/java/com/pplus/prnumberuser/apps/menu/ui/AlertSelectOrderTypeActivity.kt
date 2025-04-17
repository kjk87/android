package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityAlertSelectOrderTypeBinding

class AlertSelectOrderTypeActivity : BaseActivity() {

    private lateinit var binding: ActivityAlertSelectOrderTypeBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertSelectOrderTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        var type = 2

        binding.imageAlertSelectOrderTypeClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.layoutAlertSelectOrderTypeDelivery.setOnClickListener {
            binding.layoutAlertSelectOrderTypeDelivery.isSelected = true
            binding.layoutAlertSelectOrderTypePackage.isSelected = false
            type = 2
        }

        binding.layoutAlertSelectOrderTypePackage.setOnClickListener {
            binding.layoutAlertSelectOrderTypeDelivery.isSelected = false
            binding.layoutAlertSelectOrderTypePackage.isSelected = true
            type = 5
        }

        binding.textAlertSelectOrderTypeOrder.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.TYPE, type)
            setResult(RESULT_OK, data)
            finish()
        }

        binding.layoutAlertSelectOrderTypeDelivery.isSelected = true
        binding.layoutAlertSelectOrderTypePackage.isSelected = false
        type = 2
    }

}