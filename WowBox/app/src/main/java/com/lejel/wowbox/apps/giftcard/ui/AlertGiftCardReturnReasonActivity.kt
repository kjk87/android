package com.lejel.wowbox.apps.giftcard.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.GiftCardPurchase
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertGiftCardReturnReasonBinding

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
