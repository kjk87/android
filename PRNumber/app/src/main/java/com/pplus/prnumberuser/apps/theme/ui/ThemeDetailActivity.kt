//package com.pplus.prnumberuser.apps.theme.ui
//
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.ThemeCategory
//
//class ThemeDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        val theme = intent.getParcelableExtra<ThemeCategory>(Const.DATA)
//        return "Main_thema_${theme!!.seqNo}"
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_theme_detail
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val themeCategory = intent.getParcelableExtra<ThemeCategory>(Const.DATA)
//
//        setTitle(themeCategory!!.name)
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.container_theme_detail, ThemePageFragment.newInstance(themeCategory), ThemePageFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_theme_place), ToolbarOption.ToolbarMenu.LEFT)
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
