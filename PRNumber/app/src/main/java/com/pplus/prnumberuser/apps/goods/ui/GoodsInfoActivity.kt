//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.setting.ui.InquiryActivity
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import kotlinx.android.synthetic.main.activity_goods_info.*
//
//class GoodsInfoActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_goods_info
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val key = intent.getStringExtra(Const.KEY)
//
//        when (key) {
//            Const.REFUND -> {
//                webview_goods_info.loadUrl(getString(R.string.msg_goods_refund_url)+"?timestamp=" + System.currentTimeMillis())
//                text_goods_info_report.visibility = View.GONE
//            }
//            Const.WARNING -> {
//                webview_goods_info.loadUrl(getString(R.string.msg_goods_buy_warning_url)+"?timestamp=" + System.currentTimeMillis())
//                text_goods_info_report.visibility = View.VISIBLE
//                text_goods_info_report.setOnClickListener {
//                    val goods = intent.getParcelableExtra<Goods>(Const.GOODS)
//                    val intent = Intent(this, InquiryActivity::class.java)
//                    intent.putExtra(Const.GOODS, goods)
//                    startActivity(intent)
//                }
//            }
//        }
//
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val key = intent.getStringExtra(Const.KEY)
//
//        val toolbarOption = ToolbarOption(this)
//        when (key) {
//            Const.REFUND -> {
//                toolbarOption.initializeDefaultToolbar(getString(R.string.word_refund_exchange_info), ToolbarOption.ToolbarMenu.RIGHT)
//            }
//            Const.WARNING -> {
//                toolbarOption.initializeDefaultToolbar(getString(R.string.word_buy_warning), ToolbarOption.ToolbarMenu.RIGHT)
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
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//
//}
