//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import kotlinx.android.synthetic.main.activity_goods_order_terms.*
//
//class GoodsOrderTermsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_goods_order_terms
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        onNewIntent(intent)
//
//    }
//
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if (intent != null) {
//            val key = intent.getStringExtra(Const.KEY)
//            when (key) {
//                Const.TERMS1 -> {
//                    webview_goods_order_terms.loadUrl(getString(R.string.msg_buy_terms1) + "?timestamp=" + System.currentTimeMillis())
//                }
//                Const.TERMS2 -> {
//                    webview_goods_order_terms.loadUrl(getString(R.string.msg_buy_terms2) + "?timestamp=" + System.currentTimeMillis())
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        val key = intent.getStringExtra(Const.KEY)
//        when (key) {
//            Const.TERMS1 -> {
//                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_goods_buy_agree_title1), ToolbarOption.ToolbarMenu.LEFT)
//            }
//            Const.TERMS2 -> {
//                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_goods_buy_agree_title2), ToolbarOption.ToolbarMenu.LEFT)
//            }
//        }
//
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
