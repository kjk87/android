package com.pplus.prnumberbiz.apps.keyword.ui

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity

class KeywordActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_keyword
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setKeywordFragment()
    }

    private fun setKeywordFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.keyword_container, KeywordFragment.newInstance(), KeywordFragment::class.java.simpleName)
        ft.commitNow()
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
