package com.pplus.prnumberbiz.apps.cash.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cash_charge_alert.*

class CashChargeAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return "Main_menu_cash_charge"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_cash_charge_alert
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        image_cash_charge_alert_close.setOnClickListener {
            finish()
        }

        layout_cash_charge_inquiry.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0263151234")
            startActivity(intent)
        }
    }

}
