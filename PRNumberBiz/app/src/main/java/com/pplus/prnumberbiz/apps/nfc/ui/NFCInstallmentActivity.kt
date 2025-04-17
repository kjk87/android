package com.pplus.prnumberbiz.apps.nfc.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.nfc.data.NFCInstallmentAdapter
import kotlinx.android.synthetic.main.activity_nfcinstallment.*

class NFCInstallmentActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_nfcinstallment
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val adapter = NFCInstallmentAdapter()
        recycler_nfcinstallment.layoutManager = LinearLayoutManager(this)
        recycler_nfcinstallment.adapter = adapter

        text_nfcinstallment_request.setOnClickListener {
            val installment = adapter.mSelectData

            val data = Intent()
            data.putExtra(Const.INSTALLMENT, installment)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_pay_nfc), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
