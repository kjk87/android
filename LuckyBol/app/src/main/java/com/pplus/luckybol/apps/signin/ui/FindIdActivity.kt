package com.pplus.luckybol.apps.signin.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.databinding.ActivityFindIdBinding

class FindIdActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityFindIdBinding

    override fun getLayoutView(): View {
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        findId()
    }

    fun findId() {

        val fragment = FindIdFragment.newInstance()

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.find_id_container, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    fun findIdResult(user: User) {

        val fragment = FindIDResultFragment.newInstance(user)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.find_id_container, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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
