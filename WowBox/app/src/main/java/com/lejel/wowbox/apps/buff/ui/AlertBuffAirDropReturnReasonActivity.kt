package com.lejel.wowbox.apps.buff.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.AirDrop
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertBuffAirDropReturnReasonBinding

class AlertBuffAirDropReturnReasonActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertBuffAirDropReturnReasonBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBuffAirDropReturnReasonBinding.inflate(layoutInflater)
        return binding.root
    }

    private lateinit var mAirDrop: AirDrop
    override fun initializeView(savedInstanceState: Bundle?) {
        mAirDrop = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, AirDrop::class.java)!!

        binding.textAlertBuffAirDropReturnReason.text = mAirDrop.comment
        binding.textAlertBuffAirDropReturnConfirm.setOnClickListener {
            finish()
        }

    }
}
