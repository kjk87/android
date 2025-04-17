package com.lejel.wowbox.apps.lottery.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.main.ui.MainLotteryFragment
import com.lejel.wowbox.databinding.ActivityLotteryBinding

class LotteryActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLotteryBinding

    override fun getLayoutView(): View {
        binding = ActivityLotteryBinding.inflate(layoutInflater)
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
        ft.replace(R.id.container_lottery, MainLotteryFragment.newInstance(), MainLotteryFragment::class.java.simpleName)
        ft.commit()
    }


    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lotto_en), ToolbarOption.ToolbarMenu.LEFT)
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