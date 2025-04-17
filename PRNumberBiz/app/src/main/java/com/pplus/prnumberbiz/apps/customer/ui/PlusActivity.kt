package com.pplus.prnumberbiz.apps.customer.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity

class PlusActivity : BaseActivity() {
    override fun getPID(): String {
        return "Main_menu_plus list"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_plus
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.plus_container, PlusFragment.newInstance(), PlusFragment::class.java.simpleName)
        ft.commitNow()

    }
}
