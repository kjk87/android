package com.pplus.prnumberuser.apps.product.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.menu.ui.OrderPurchaseHistoryFragment
import com.pplus.prnumberuser.apps.menu.ui.TicketPurchaseHistoryFragment
import com.pplus.prnumberuser.databinding.ActivityPurchaseHistoryBinding

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

//        binding.layoutOrderPurchaseHistoryTab.setOnClickListener {
//            setOrderPurchaseHistory()
//        }
//
//        binding.layoutPurchaseHistoryShippingTab.setOnClickListener {
//            setPurchaseProductShippingHistory()
//        }

        val key = intent.getStringExtra(Const.KEY)
        when(key){
            Const.ORDER->{
                setOrderPurchaseHistory()
            }
            Const.TICKET->{
                setTicketPurchaseHistory()
            }
        }


    }

    private fun setOrderPurchaseHistory(){

        binding.layoutOrderPurchaseHistoryTab.isSelected = true
        binding.layoutPurchaseHistoryShippingTab.isSelected = false
        binding.textPurchaseHistoryTicketTab.typeface = Typeface.DEFAULT_BOLD
        binding.textPurchaseHistoryShippingTab.typeface = Typeface.DEFAULT

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.purchase_history_container, OrderPurchaseHistoryFragment.newInstance(), OrderPurchaseHistoryFragment::class.java.simpleName)
        ft.commit()
    }

    private fun setTicketPurchaseHistory(){

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.purchase_history_container, TicketPurchaseHistoryFragment.newInstance(), TicketPurchaseHistoryFragment::class.java.simpleName)
        ft.commit()
    }

    private fun setPurchaseProductShippingHistory(){

        binding.layoutOrderPurchaseHistoryTab.isSelected = false
        binding.layoutPurchaseHistoryShippingTab.isSelected = true
        binding.textPurchaseHistoryTicketTab.typeface = Typeface.DEFAULT
        binding.textPurchaseHistoryShippingTab.typeface = Typeface.DEFAULT_BOLD

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.purchase_history_container, PurchaseProductShippingHistoryFragment.newInstance(), PurchaseProductShippingHistoryFragment::class.java.simpleName)
        ft.commit()
    }


//    private fun setMenuHistory(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_history_container, OrderHistoryFragment.newInstance(), OrderHistoryFragment::class.java.simpleName)
//        ft.commit()
//    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_order_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
