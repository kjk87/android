package com.pplus.prnumberbiz.apps.number.ui

import android.app.Activity
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.util.PplusNumberUtil
import kotlinx.android.synthetic.main.activity_make_prnumber_confirm.*

class MakePrnumberConfirmActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_make_prnumber_confirm
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val number = intent.getStringExtra(Const.DATA)

        text_make_prnumber_confirm_number.text = PplusNumberUtil.getOnlyNumber(number)

        text_make_prnumber_confirm_cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        text_make_prnumber_confirm.setOnClickListener {

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
