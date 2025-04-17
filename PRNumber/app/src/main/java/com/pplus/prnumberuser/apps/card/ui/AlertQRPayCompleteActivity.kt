package com.pplus.prnumberuser.apps.card.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.FTLink
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertQrPayCompleteBinding
import com.pplus.utils.part.format.FormatUtil

class AlertQRPayCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertQrPayCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertQrPayCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ftLink = intent.getParcelableExtra<FTLink>(Const.DATA)

        binding.textAlertQrPayCompletePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(ftLink!!.order_req_amt)))
        binding.textAlertQrPayCompleteConfirm.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}
