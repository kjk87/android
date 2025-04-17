//package com.pplus.prnumberuser.apps.main.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//
//class MainPRActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_hot_deal
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.hot_deal_container, MainPRFragment.newInstance(), MainPRFragment::class.java.simpleName)
//        ft.commit()
//    }
//
////    override fun getToolbarOption(): ToolbarOption {
////
////        val toolbarOption = ToolbarOption(this)
////        toolbarOption.initializeDefaultToolbar(getString(R.string.word_hot_deal_goods), ToolbarOption.ToolbarMenu.LEFT)
////        return toolbarOption
////    }
////
////    override fun getOnToolbarClickListener(): OnToolbarListener {
////
////        return OnToolbarListener { v, toolbarMenu, tag ->
////            when (toolbarMenu) {
////                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
////                    onBackPressed()
////                }
////            }
////        }
////    }
//}
