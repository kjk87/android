package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityMyLuckyDrawHistoryBinding

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
        val tab = intent.getIntExtra(Const.TAB, 0)
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

        when(tab){
            0->{
                setTitle(getString(R.string.word_my_join_history))
                purchase()
            }
            1->{
                setTitle(getString(R.string.word_my_win_history))
                win()
            }
        }


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
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_my_join_history), ToolbarOption.ToolbarMenu.LEFT)
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