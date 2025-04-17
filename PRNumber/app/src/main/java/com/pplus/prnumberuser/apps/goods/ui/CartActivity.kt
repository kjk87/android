//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//
//class CartActivity : BaseActivity(), ImplToolbar {
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
//        val pageSeqNo = intent.getLongExtra(Const.PAGE_SEQ_NO, 0)
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.review_container, PageCartFragment.newInstance(pageSeqNo), PageCartFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cart), ToolbarOption.ToolbarMenu.LEFT)
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
