package com.pplus.prnumberbiz.apps.sale.ui

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sale_order_process.*

class SaleOrderHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_history
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val key = intent.getIntExtra(Const.KEY, 1)
        when (key) {
            1 -> {
                setSaleOrderHistoryFragment()
            }
            else->{
                setSaleDeliveryHistoryFragment(key)
            }
        }
    }

    private fun setSaleOrderHistoryFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_history_container, SaleOrderHistoryFragment.newInstance(), SaleOrderHistoryFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun setSaleDeliveryHistoryFragment(key:Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_history_container, SaleDeliveryHistoryFragment.newInstance(key), SaleDeliveryHistoryFragment::class.java.simpleName)
        ft.commitNow()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history), ToolbarOption.ToolbarMenu.LEFT)
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
