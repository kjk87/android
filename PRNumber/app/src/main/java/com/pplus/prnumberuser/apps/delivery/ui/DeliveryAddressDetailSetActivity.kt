package com.pplus.prnumberuser.apps.delivery.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityDeliveryAddressDetailSetBinding

class DeliveryAddressDetailSetActivity : BaseActivity() {

    private lateinit var binding: ActivityDeliveryAddressDetailSetBinding

    override fun getLayoutView(): View {
        binding = ActivityDeliveryAddressDetailSetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

    }

}