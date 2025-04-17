package com.pplus.prnumberbiz.apps.nfc.ui

import android.app.Activity
import android.os.Bundle
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.model.dto.LpngRes
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_alert_nfcpay_complete.*

class AlertNFCPayCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_alert_nfcpay_complete
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val lpng = intent.getParcelableExtra<LpngRes>(Const.LPNG)

        text_alert_nfcpay_complete_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(lpng.order_req_amt)))
        text_alert_nfcpay_complete_confirm.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}
