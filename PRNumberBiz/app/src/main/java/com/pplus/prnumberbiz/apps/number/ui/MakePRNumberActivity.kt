package com.pplus.prnumberbiz.apps.number.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity

class MakePRNumberActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_make_prnumber
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.make_prnumer_container, MakeNumberFragment.newInstance(), MakeNumberFragment::class.java.simpleName)
        ft.commit()
    }

}
