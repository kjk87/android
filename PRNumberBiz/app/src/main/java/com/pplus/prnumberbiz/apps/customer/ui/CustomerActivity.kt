package com.pplus.prnumberbiz.apps.customer.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.ui.MainCustomerFragment

class CustomerActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_customer
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.customer_container, MainCustomerFragment.newInstance(), MainCustomerFragment::class.java.simpleName)
        ft.commitNow()

    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_customer_config), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
