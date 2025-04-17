package com.pplus.luckybol.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.setting.ui.ProfileConfigActivity
import com.pplus.luckybol.databinding.ActivityAlertProfileBinding
import com.pplus.utils.part.format.FormatUtil

class AlertProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityAlertProfileBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val profileReward = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties.profileReward.toString())
        binding.textAlertProfileTitle.text = getString(R.string.format_alert_profile_title, profileReward)

        binding.textAlertProfileCancel.setOnClickListener {
            finish()
        }

        binding.textAlertProfileConfirm.setOnClickListener {
            val intent = Intent(this, ProfileConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            setProfileLauncher.launch(intent)
        }
    }

    val setProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setResult(RESULT_OK)
        finish()
    }
}