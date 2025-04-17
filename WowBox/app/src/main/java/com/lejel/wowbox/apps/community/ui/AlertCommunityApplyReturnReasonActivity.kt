package com.lejel.wowbox.apps.community.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.CommunityApply
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertCommunityApplyReturnReasonBinding

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
