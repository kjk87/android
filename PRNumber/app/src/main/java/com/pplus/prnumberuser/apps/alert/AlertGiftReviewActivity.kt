package com.pplus.prnumberuser.apps.alert

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityAlertGiftReviewBinding
import com.pplus.utils.part.format.FormatUtil

class AlertGiftReviewActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertGiftReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertGiftReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val point = intent.getIntExtra(Const.POINT, 0)
        binding.textAlertGiftReviewPoint.text = getString(R.string.format_saved2, FormatUtil.getMoneyType(point.toString()))

        binding.textAlertGiftReviewCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.textAlertGiftReviewReviewWrite.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}
