package com.pplus.prnumberbiz.apps.customer.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.ui.MainCustomerFragment

class ContactCustomerActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_contact_customer
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.contact_customer_container, CustomerFragment.newInstance(), CustomerFragment::class.java.simpleName)
        ft.commitNow()

    }

}
