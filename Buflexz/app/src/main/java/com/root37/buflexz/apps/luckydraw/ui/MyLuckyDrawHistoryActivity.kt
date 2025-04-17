package com.root37.buflexz.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.databinding.ActivityMyLuckyDrawHistoryBinding

class MyLuckyDrawHistoryActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMyLuckyDrawHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityMyLuckyDrawHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.imageMyLuckyDrawHistoryPurchase.setOnClickListener {
            binding.layoutMyLuckyDrawHistoryPurchaseTab.visibility = View.VISIBLE
            binding.layoutMyLuckyDrawHistoryWinTab.visibility = View.GONE
            purchase()
        }

        binding.imageMyLuckyDrawHistoryWin.setOnClickListener {
            binding.layoutMyLuckyDrawHistoryPurchaseTab.visibility = View.GONE
            binding.layoutMyLuckyDrawHistoryWinTab.visibility = View.VISIBLE
            win()
        }

        binding.layoutMyLuckyDrawHistoryPurchaseTab.visibility = View.VISIBLE
        binding.layoutMyLuckyDrawHistoryWinTab.visibility = View.GONE
        purchase()
    }

    private fun purchase() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.my_lucky_draw_history_container, MyLuckyDrawPurchaseFragment.newInstance(), MyLuckyDrawPurchaseFragment::class.java.simpleName)
        ft.commit()
    }
    private fun win() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.my_lucky_draw_history_container, MyLuckyDrawWinFragment.newInstance(), MyLuckyDrawWinFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lucky_draw_join_history_en), ToolbarOption.ToolbarMenu.LEFT)
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