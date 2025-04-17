package com.root37.buflexz.apps.join

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.databinding.ActivityJoinCompleteBinding

class JoinCompleteActivity : BaseActivity() {

    private lateinit var binding: ActivityJoinCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityJoinCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        binding.textJoinCompleteStart.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}