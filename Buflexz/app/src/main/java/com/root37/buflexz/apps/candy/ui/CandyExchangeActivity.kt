package com.root37.buflexz.apps.candy.ui

import android.os.Bundle
import android.view.View
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.databinding.ActivityCandyExchangeBinding

class CandyExchangeActivity : BaseActivity() {

    private lateinit var binding: ActivityCandyExchangeBinding

    override fun getLayoutView(): View {
        binding = ActivityCandyExchangeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

    }
}