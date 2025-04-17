package com.pplus.prnumberbiz.apps.goods.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sale_price_info_alert.*

class SalePriceInfoAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_price_info_alert
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        layout_sale_price_info_confirm.setOnClickListener {
            finish()
        }

    }

}
