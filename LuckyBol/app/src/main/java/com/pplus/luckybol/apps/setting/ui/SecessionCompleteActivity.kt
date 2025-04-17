package com.pplus.luckybol.apps.setting.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.logOutAndRestart
import com.pplus.luckybol.databinding.ActivitySecessionBinding

/**
 * 회원탈퇴
 */
class SecessionCompleteActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivitySecessionBinding

    override fun getLayoutView(): View {
        binding = ActivitySecessionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.secession_container, SecessionResultFragment.newInstance(), SecessionResultFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_complete_secession), ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarMenu.RIGHT, getString(R.string.word_confirm))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarMenu.RIGHT -> if (tag == 1) {
                        logOutAndRestart()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onBackPressed() {
        logOutAndRestart()
    }
}