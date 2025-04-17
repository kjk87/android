package com.pplus.luckybol.apps.card.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.card.data.InstallmentAdapter
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityInstallmentBinding

class InstallmentActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityInstallmentBinding

    override fun getLayoutView(): View {
        binding = ActivityInstallmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val adapter = InstallmentAdapter()
        binding.recyclerInstallment.layoutManager = LinearLayoutManager(this)
        binding.recyclerInstallment.adapter = adapter

        binding.textInstallmentRequest.setOnClickListener {
            val installment = adapter.mSelectData

            val data = Intent()
            data.putExtra(Const.INSTALLMENT, installment)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_installment), ToolbarOption.ToolbarMenu.RIGHT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
