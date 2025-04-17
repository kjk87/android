package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.databinding.ActivityOnlinePageBinding

class NumberGroupPageActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }


    private lateinit var binding: ActivityOnlinePageBinding

    override fun getLayoutView(): View {
        binding = ActivityOnlinePageBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun initializeView(savedInstanceState: Bundle?) {

        val virtualNumberManage = intent.getParcelableExtra<VirtualNumberManage>(Const.DATA)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.online_page_container, NumberGroupPageFragment.newInstance(virtualNumberManage!!), NumberGroupPageFragment::class.java.simpleName)
        ft.commit()
    }


    override fun getToolbarOption(): ToolbarOption {
        val virtualNumberManage = intent.getParcelableExtra<VirtualNumberManage>(Const.DATA)
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(virtualNumberManage!!.groupName, ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
