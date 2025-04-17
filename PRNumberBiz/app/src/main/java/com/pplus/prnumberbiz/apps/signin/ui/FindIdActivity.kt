package com.pplus.prnumberbiz.apps.signin.ui

import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.network.model.dto.User

class FindIdActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_find_id
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
            }
        }
    }
}
