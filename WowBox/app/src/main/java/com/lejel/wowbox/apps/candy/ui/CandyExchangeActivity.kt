package com.lejel.wowbox.apps.candy.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityCandyExchangeBinding

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