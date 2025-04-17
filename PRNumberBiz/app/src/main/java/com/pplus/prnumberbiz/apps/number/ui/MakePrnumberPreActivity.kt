package com.pplus.prnumberbiz.apps.number.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_make_prnumber_pre.*

class MakePrnumberPreActivity : BaseActivity(){
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_make_prnumber_pre
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        text_make_prnumber_pre_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_make_prnumber_title))

        image_make_prnumber_pre_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        text_make_prnumber_pre_confirm.setOnClickListener {
            val intent = Intent(this, MakePRNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_SET_PAGE -> {
                setResult(resultCode)
                finish()
            }
        }
    }
}
