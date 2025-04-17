package com.lejel.wowbox.apps.luckyball.ui

import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityLuckyBallExchangeCompleteBinding

class LuckyBallExchangeCompleteActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBallExchangeCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBallExchangeCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val exchangeBall = intent.getIntExtra(Const.BALL, 0)

        binding.textLuckyBallExchangeCompletePoint.text = FormatUtil.getMoneyType((exchangeBall*1000).toString())
        binding.textLuckyBallExchangeCompleteBall.text = FormatUtil.getMoneyType(exchangeBall.toString())
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_exchange_luckyball), ToolbarOption.ToolbarMenu.LEFT)
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