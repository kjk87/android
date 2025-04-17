package com.pplus.luckybol.apps.alert

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertGoogleReviewBinding
import com.pplus.utils.part.pref.PreferenceUtil

class AlertGoogleReviewActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertGoogleReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertGoogleReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAlertGoogleReviewCancel.setOnClickListener {
            finish()
        }

        binding.textAlertGoogleReviewReviewWrite.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.GOOGEL_REVIEW, true)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            finish()
        }
    }

}
