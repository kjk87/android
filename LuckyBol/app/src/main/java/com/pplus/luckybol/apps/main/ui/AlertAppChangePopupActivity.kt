package com.pplus.luckybol.apps.main.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertMainPopupBinding
import com.pplus.utils.part.pref.PreferenceUtil

class AlertAppChangePopupActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertMainPopupBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertMainPopupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAlertMainPopupClose.setOnClickListener {
            finish()
        }

        binding.textAlertMainPopupDoNotAgain.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.APP_CHANGE_POPUP, false)
            finish()
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }

}
