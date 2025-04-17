package com.lejel.wowbox.apps.luckybox.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityLuckyBoxWinHistoryBinding

class LuckyBoxWinHistoryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxWinHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxWinHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        win()
    }

    private fun win(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container_lucky_box_container, LuckyBoxWinFragment.newInstance(), LuckyBoxWinFragment::class.java.simpleName)
        ft.commit()
    }


    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}