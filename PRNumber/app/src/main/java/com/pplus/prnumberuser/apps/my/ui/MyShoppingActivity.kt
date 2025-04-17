//package com.pplus.prnumberuser.apps.my.ui
//
//import android.graphics.Typeface
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyHistoryFragment
//import com.pplus.prnumberuser.apps.goods.ui.CartFragment
//import kotlinx.android.synthetic.main.activity_my_shopping.*
//
//class MyShoppingActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_my_shopping
//    }
//
//    companion object {
//        val CART = 0
//        val BUY = 1
//        val LIKE = 2
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val tab = intent.getIntExtra(Const.TAB, CART)
//
//
//        layout_my_shopping_cart.setOnClickListener {
//            setSelect(layout_my_shopping_cart, layout_my_shopping_buy, layout_my_shopping_wish)
//            setBold(text_my_shopping_cart, text_my_shopping_buy, text_my_shopping_wish)
//            setCartFragment()
//        }
//
//        layout_my_shopping_buy.setOnClickListener {
//            setSelect(layout_my_shopping_buy, layout_my_shopping_cart, layout_my_shopping_wish)
//            setBold(text_my_shopping_buy, text_my_shopping_cart, text_my_shopping_wish)
//            setBuyHistoryFragment()
//        }
//
//        layout_my_shopping_wish.setOnClickListener {
//            setSelect(layout_my_shopping_wish, layout_my_shopping_cart, layout_my_shopping_buy)
//            setBold(text_my_shopping_wish, text_my_shopping_cart, text_my_shopping_buy)
//        }
//
//        when (tab) {
//            CART -> {
//                setSelect(layout_my_shopping_cart, layout_my_shopping_buy, layout_my_shopping_wish)
//                setBold(text_my_shopping_cart, text_my_shopping_buy, text_my_shopping_wish)
//                setCartFragment()
//            }
//            BUY -> {
//                setSelect(layout_my_shopping_buy, layout_my_shopping_cart, layout_my_shopping_wish)
//                setBold(text_my_shopping_buy, text_my_shopping_cart, text_my_shopping_wish)
//                setBuyHistoryFragment()
//            }
//            LIKE -> {
//                setSelect(layout_my_shopping_wish, layout_my_shopping_cart, layout_my_shopping_buy)
//                setBold(text_my_shopping_wish, text_my_shopping_cart, text_my_shopping_buy)
//            }
//        }
//    }
//
//    private fun setSelect(view1: View, view2: View, view3: View) {
//        view1.isSelected = true
//        view2.isSelected = false
//        view3.isSelected = false
//    }
//
//    private fun setBold(view1: TextView, view2: TextView, view3: TextView) {
//
//        view1.typeface = Typeface.DEFAULT_BOLD
//        view2.typeface = Typeface.DEFAULT
//        view3.typeface = Typeface.DEFAULT
//    }
//
//    private fun setCartFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        ft.replace(R.id.my_shopping_container, CartFragment.newInstance(), CartFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    private fun setBuyHistoryFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        ft.replace(R.id.my_shopping_container, BuyHistoryFragment.newInstance(), BuyHistoryFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_shopping), ToolbarOption.ToolbarMenu.LEFT)
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
