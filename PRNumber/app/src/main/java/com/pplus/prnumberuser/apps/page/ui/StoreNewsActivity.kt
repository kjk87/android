//package com.pplus.prnumberuser.apps.page.ui
//
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.databinding.ActivityStoreNewsBinding
//
//class StoreNewsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    private lateinit var binding: ActivityStoreNewsBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityStoreNewsBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    private var mPage: Page? = null
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mPage = intent.getParcelableExtra(Const.PAGE)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.store_news_container, StoreNewsFragment.newInstance(mPage!!), StoreNewsFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_news), ToolbarOption.ToolbarMenu.LEFT)
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
