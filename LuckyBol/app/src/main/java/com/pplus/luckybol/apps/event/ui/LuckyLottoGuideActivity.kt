package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityLuckyLottoGuideBinding

class LuckyLottoGuideActivity : BaseActivity() {

    private lateinit var binding: ActivityLuckyLottoGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyLottoGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.imageLuckyLottoGuideBack.setOnClickListener {
            onBackPressed()
        }
    }

}