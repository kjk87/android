//package com.pplus.prnumberbiz.apps.marketing.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberbiz.apps.customer.ui.ContactConfigActivity
//import com.pplus.prnumberbiz.apps.goods.ui.SelectGoodsActivity
//import com.pplus.prnumberbiz.apps.pages.ui.PageSetActivity
//import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
//import com.pplus.prnumberbiz.apps.sms.SmsSendActivity
//import kotlinx.android.synthetic.main.activity_marketing.*
//
//class MarketingActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_marketing
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        layout_marketing_kakao.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(this, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, SelectGoodsActivity::class.java)
//            intent.putExtra(Const.KAKAO, true)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_marketing_sms.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(this, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, ContactConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_marketing_push.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(this, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, PushSendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_marketing), ToolbarOption.ToolbarMenu.LEFT)
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
