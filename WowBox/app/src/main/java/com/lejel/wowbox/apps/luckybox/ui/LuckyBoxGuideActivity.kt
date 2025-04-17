package com.lejel.wowbox.apps.luckybox.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityLuckyBoxGuideBinding

class LuckyBoxGuideActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLuckyBoxGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
    }
}