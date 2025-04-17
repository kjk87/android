package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityPurchaseHistoryBinding

class PurchaseHistoryActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_number_buyhistory"
    }

    private lateinit var binding: ActivityPurchaseHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityPurchaseHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        setPurchaseProductShippingHistory()
    }


    private fun setPurchaseProductShippingHistory(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.purchase_history_container, PurchaseProductShippingHistoryFragment.newInstance(), PurchaseProductShippingHistoryFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
