package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityBuffGuideBinding

class BuffGuideActivity : BaseActivity() {
    private lateinit var binding: ActivityBuffGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.imageBuffGuideBack.setOnClickListener {
            onBackPressed()
        }
    }
}