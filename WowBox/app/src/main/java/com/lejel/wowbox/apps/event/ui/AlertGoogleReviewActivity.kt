package com.lejel.wowbox.apps.event.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityAlertGoogleReviewBinding
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

        binding.textAlertGoogleReviewWrite.setOnClickListener {
            PreferenceUtil.getDefaultPreference(this).put(Const.GOOGEL_REVIEW, true)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            finish()
        }
    }

}
