package com.pplus.prnumberuser.apps.product.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAlertProductNoShowBinding

class AlertProductNoShowActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertProductNoShowBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertProductNoShowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val type = intent.getStringExtra(Const.TYPE)


        when(type){
            "detail"->{
                binding.textAlertProductNoShowDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_alert_product_no_show_desc1))
            }
            "buy"->{
                binding.textAlertProductNoShowDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_alert_product_no_show_desc2))
            }
        }



        binding.textAlertProductNoShowCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.textAlertProductNoShowConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}
