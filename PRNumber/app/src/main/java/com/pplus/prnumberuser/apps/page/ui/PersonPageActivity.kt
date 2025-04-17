//package com.pplus.prnumberuser.apps.page.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.Category
//
//class PersonPageActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Main_page_person"
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_theme_detail
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.container_theme_detail, PersonPageFragment.newInstance(), PersonPageFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_personal_pro), ToolbarOption.ToolbarMenu.LEFT)
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
