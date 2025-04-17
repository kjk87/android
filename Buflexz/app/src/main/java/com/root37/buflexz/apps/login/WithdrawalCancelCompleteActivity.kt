package com.root37.buflexz.apps.login

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityWithdrawalCancelCompleteBinding

class WithdrawalCancelCompleteActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityWithdrawalCancelCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityWithdrawalCancelCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.textWithdrawalCancelCompleteConfirm.setOnClickListener {
            PplusCommonUtil.logOutAndRestart()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_withdrawal_cancel), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        PplusCommonUtil.logOutAndRestart()
                    }

                    else -> {}
                }
            }
        }
    }
}