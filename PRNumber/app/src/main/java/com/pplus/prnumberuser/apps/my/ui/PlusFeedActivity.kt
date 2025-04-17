//package com.pplus.prnumberuser.apps.my.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.ui.MainFeedFragment
//
//class PlusFeedActivity : BaseActivity(), ImplToolbar {
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_plus_feed
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val ft = supportFragmentManager.beginTransaction()
////        ft.replace(R.id.plus_feed_container, MainGoodsPlusFragment.newInstance(), MainGoodsPlusFragment::class.java.simpleName)
//        ft.replace(R.id.plus_feed_container, MainFeedFragment.newInstance(), MainFeedFragment::class.java.simpleName)
//        ft.commit()
//
//
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_plus_news), ToolbarOption.ToolbarMenu.LEFT)
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
