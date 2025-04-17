package com.pplus.prnumberuser.apps.menu.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertOrderPayBinding

class AlertOrderPayActivity : BaseActivity() {

    private lateinit var binding: ActivityAlertOrderPayBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertOrderPayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val key = intent.getStringExtra(Const.KEY)
        when(key){
            Const.ORDER->{
                binding.textAlertOrderPayMsg.setText(R.string.msg_alert_order_pay_desc)
            }
            Const.TICKET->{
                binding.textAlertOrderPayMsg.text = PplusCommonUtil.fromHtml(getString(R.string.html_ticket_ticket_pay_caution))
            }
        }


        binding.imageAlertOrderPayClose.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.textAlertOrderPayOrder.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

}