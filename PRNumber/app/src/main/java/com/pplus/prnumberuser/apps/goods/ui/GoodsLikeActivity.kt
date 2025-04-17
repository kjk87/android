//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.graphics.Typeface
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_review.*
//
//class GoodsLikeActivity : BaseActivity(), ImplToolbar {
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_review
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        layout_goods_like_shipping_tab.setOnClickListener {
//            layout_goods_like_shipping_tab.isSelected = true
//            layout_goods_like_offline_tab.isSelected = false
//
//            text_goods_like_shipping_tab.typeface = Typeface.DEFAULT_BOLD
//            text_goods_like_offline_tab.typeface = Typeface.DEFAULT
//            setShipping()
//        }
//
//        layout_goods_like_offline_tab.setOnClickListener {
//            layout_goods_like_shipping_tab.isSelected = false
//            layout_goods_like_offline_tab.isSelected = true
//
//            text_goods_like_shipping_tab.typeface = Typeface.DEFAULT
//            text_goods_like_offline_tab.typeface = Typeface.DEFAULT_BOLD
//            setOffline()
//        }
//
//        layout_goods_like_shipping_tab.isSelected = true
//        layout_goods_like_offline_tab.isSelected = false
//
//        text_goods_like_shipping_tab.typeface = Typeface.DEFAULT_BOLD
//        text_goods_like_offline_tab.typeface = Typeface.DEFAULT
//
//        setShipping()
//    }
//
//    private fun setOffline(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.review_container, GoodsLikeFragment.newInstance(), GoodsLikeFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setShipping(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.review_container, GoodsLikeShippingFragment.newInstance(), GoodsLikeShippingFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_wish_goods), ToolbarOption.ToolbarMenu.LEFT)
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
