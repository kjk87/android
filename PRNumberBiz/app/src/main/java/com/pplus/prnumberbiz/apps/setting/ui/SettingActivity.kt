package com.pplus.prnumberbiz.apps.setting.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.main.ui.SettingFragment

class SettingActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_menu_setting"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_setting
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.setting_container, SettingFragment.newInstance(), SettingFragment::class.java.simpleName)
        ft.commitNow()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
