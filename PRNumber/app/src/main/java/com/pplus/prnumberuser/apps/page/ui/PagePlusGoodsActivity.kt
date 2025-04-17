//package com.pplus.prnumberuser.apps.page.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.PageGoodsFragment
//import com.pplus.prnumberuser.core.network.model.dto.Page
//
//class PagePlusGoodsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_page_plus_goods
//    }
//
//    private var mPage: Page? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPage = intent.getParcelableExtra(Const.PAGE)
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.page_plus_goods_container, PageGoodsFragment.newInstance(mPage!!), PageGoodsFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_plus_goods), ToolbarOption.ToolbarMenu.LEFT)
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
