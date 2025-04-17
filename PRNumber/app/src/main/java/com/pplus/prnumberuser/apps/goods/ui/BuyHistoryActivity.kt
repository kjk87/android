//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.graphics.Typeface
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_buy_history.*
//
//class BuyHistoryActivity : BaseActivity(), ImplToolbar {
//
//    override fun getPID(): String {
//        return "Main_number_buyhistory"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_buy_history
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        layout_buy_history_visit_tab.setOnClickListener {
//            layout_buy_history_visit_tab.isSelected = true
//            layout_buy_history_shipping_tab.isSelected = false
//            layout_buy_history_goods_tab.isSelected = false
//
//
//            text_buy_history_visit_tab.typeface = Typeface.DEFAULT_BOLD
//            text_buy_history_shipping_tab.typeface = Typeface.DEFAULT
//            text_buy_history_goods_tab.typeface = Typeface.DEFAULT
//
//            setVisitHistory()
//        }
//
//        layout_buy_history_shipping_tab.setOnClickListener {
//            layout_buy_history_visit_tab.isSelected = false
//            layout_buy_history_shipping_tab.isSelected = true
//            layout_buy_history_goods_tab.isSelected = false
//
//            text_buy_history_visit_tab.typeface = Typeface.DEFAULT
//            text_buy_history_shipping_tab.typeface = Typeface.DEFAULT_BOLD
//            text_buy_history_goods_tab.typeface = Typeface.DEFAULT
//            setBuyShippingHistory()
//        }
//
//        layout_buy_history_goods_tab.setOnClickListener {
//            layout_buy_history_visit_tab.isSelected = false
//            layout_buy_history_shipping_tab.isSelected = false
//            layout_buy_history_goods_tab.isSelected = true
//
//            text_buy_history_visit_tab.typeface = Typeface.DEFAULT
//            text_buy_history_shipping_tab.typeface = Typeface.DEFAULT
//            text_buy_history_goods_tab.typeface = Typeface.DEFAULT_BOLD
//            setBuyHistory()
//        }
//
//        layout_buy_history_shipping_tab.isSelected = true
//        layout_buy_history_visit_tab.isSelected = false
//        layout_buy_history_goods_tab.isSelected = false
//
//        text_buy_history_shipping_tab.typeface = Typeface.DEFAULT_BOLD
//        text_buy_history_visit_tab.typeface = Typeface.DEFAULT
//        text_buy_history_goods_tab.typeface = Typeface.DEFAULT
//
//        setBuyShippingHistory()
//    }
//
//    private fun setVisitHistory(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_history_container, VisitHistoryFragment.newInstance(), VisitHistoryFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setBuyShippingHistory(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_history_container, BuyShippingHistoryFragment.newInstance(), BuyShippingHistoryFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setBuyHistory(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_history_container, BuyHistoryFragment.newInstance(), BuyHistoryFragment::class.java.simpleName)
//        ft.commit()
//    }
//
////    private fun setMenuHistory(){
////        val ft = supportFragmentManager.beginTransaction()
////        ft.replace(R.id.buy_history_container, OrderHistoryFragment.newInstance(), OrderHistoryFragment::class.java.simpleName)
////        ft.commit()
////    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_history), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
