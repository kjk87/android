package com.pplus.prnumberuser.apps.signin.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.databinding.ActivityFindIdBinding

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
