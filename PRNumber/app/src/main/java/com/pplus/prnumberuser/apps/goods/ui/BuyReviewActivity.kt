//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.graphics.Typeface
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.product.ui.MyReviewFragment
//import kotlinx.android.synthetic.main.activity_buy_review.*
//
//class BuyReviewActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_buy_review
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        layout_buy_review_enable_tab.setOnClickListener {
//            layout_buy_review_enable_tab.isSelected = true
//            layout_buy_review_tab.isSelected = false
//
//            text_buy_review_enable_tab.typeface = Typeface.DEFAULT_BOLD
//            text_buy_review_tab.typeface = Typeface.DEFAULT
//
//            setReviewEnable()
//        }
//
//        layout_buy_review_tab.setOnClickListener {
//            layout_buy_review_enable_tab.isSelected = false
//            layout_buy_review_tab.isSelected = true
//
//            text_buy_review_enable_tab.typeface = Typeface.DEFAULT
//            text_buy_review_tab.typeface = Typeface.DEFAULT_BOLD
//
//            setMyReview()
//        }
//
//        layout_buy_review_enable_tab.isSelected = true
//        layout_buy_review_tab.isSelected = false
//
//        text_buy_review_enable_tab.typeface = Typeface.DEFAULT_BOLD
//        text_buy_review_tab.typeface = Typeface.DEFAULT
//
//        setMyReview()
//    }
//
//    private fun setReviewEnable(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_review_container, ReviewEnableFragment.newInstance(), ReviewEnableFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setMyReview(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.buy_review_container, MyReviewFragment.newInstance(), MyReviewFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_review), ToolbarOption.ToolbarMenu.LEFT)
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
//
//}
