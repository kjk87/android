//package com.pplus.prnumberuser.apps.mobilegift.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//
//class MobileGiftActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_mobile_gift
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.mobile_gift_container, MainGiftishowFragment.newInstance(), MainGiftishowFragment::class.java.simpleName)
//        ft.commit()
//    }
//
////    override fun getToolbarOption(): ToolbarOption {
////
////        val toolbarOption = ToolbarOption(this)
////        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_shop), ToolbarOption.ToolbarMenu.LEFT)
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
