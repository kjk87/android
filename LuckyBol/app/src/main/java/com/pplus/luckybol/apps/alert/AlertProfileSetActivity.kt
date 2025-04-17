package com.pplus.luckybol.apps.alert

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.setting.ui.ProfileConfigActivity
import com.pplus.luckybol.databinding.ActivityAlertProfileSetBinding
import com.pplus.utils.part.pref.PreferenceUtil

class AlertProfileSetActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertProfileSetBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertProfileSetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.imageProfileSet.setOnClickListener {
            val intent = Intent(this, ProfileConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }

        binding.textAlertProfileSetClose.setOnClickListener {
            finish()
        }

        binding.textAlertProfileSetDoNotAgain.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.GUIDE_PROFILE_SET, false)
            finish()
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }
}
