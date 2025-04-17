package com.root37.buflexz.apps.community.ui

import android.os.Bundle
import android.view.View
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.CommunityApply
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityAlertCommunityApplyReturnReasonBinding

class AlertCommunityApplyReturnReasonActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertCommunityApplyReturnReasonBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertCommunityApplyReturnReasonBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mCommunityApply: CommunityApply
    override fun initializeView(savedInstanceState: Bundle?) {
        mCommunityApply = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, CommunityApply::class.java)!!

        binding.textAlertCommunityApplyReturnReason.text = mCommunityApply.reason
        binding.textAlertCommunityApplyReturnReasonConfirm.setOnClickListener {
            finish()
        }

    }
}
