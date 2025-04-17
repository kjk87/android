package com.root37.buflexz.apps.giftcard.ui

import android.os.Bundle
import android.view.View
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.model.dto.GiftCardPurchase
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityAlertGiftCardReturnReasonBinding

class AlertGiftCardReturnReasonActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertGiftCardReturnReasonBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertGiftCardReturnReasonBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mGiftCardPurchase: GiftCardPurchase
    override fun initializeView(savedInstanceState: Bundle?) {
        mGiftCardPurchase = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, GiftCardPurchase::class.java)!!

        binding.textAlertGiftCardReturnReason.text = mGiftCardPurchase.comment
        binding.textAlertGiftCardReturnReasonConfirm.setOnClickListener {
            finish()
        }

    }
}
