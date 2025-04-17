package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.BankSelectAdapter
import kotlinx.android.synthetic.main.activity_bank_select.*

class BankSelectActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_bank_select
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        recycler_bank_select.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        val bankList = resources.getStringArray(R.array.bank).toMutableList()
        val adapter = BankSelectAdapter(this)
        adapter.setDataList(bankList)
        recycler_bank_select.adapter = adapter

        adapter.setOnItemClickListener(object : BankSelectAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val bank = bankList[position]
                val data = Intent()
                data.putExtra(Const.DATA, bank)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_bank), ToolbarOption.ToolbarMenu.RIGHT)
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
