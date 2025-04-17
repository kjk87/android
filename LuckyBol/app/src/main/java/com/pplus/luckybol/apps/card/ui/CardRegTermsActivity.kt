package com.pplus.luckybol.apps.card.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityGoodsOrderTermsBinding

class CardRegTermsActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGoodsOrderTermsBinding

    override fun getLayoutView(): View {
        binding = ActivityGoodsOrderTermsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onNewIntent(intent)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val key = intent.getStringExtra(Const.KEY)
            when (key) {
                Const.TERMS1 -> {
                    binding.webviewGoodsOrderTerms.loadUrl(getString(R.string.msg_card_reg_terms1_url))
                }
                Const.TERMS2 -> {
                    binding.webviewGoodsOrderTerms.loadUrl(getString(R.string.msg_card_reg_terms2_url))
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        val key = intent.getStringExtra(Const.KEY)
        when (key) {
            Const.TERMS1 -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_card_reg_terms1), ToolbarOption.ToolbarMenu.LEFT)
            }
            Const.TERMS2 -> {
                toolbarOption.initializeDefaultToolbar(getString(R.string.msg_card_reg_terms2), ToolbarOption.ToolbarMenu.LEFT)
            }
        }

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
