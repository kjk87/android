package com.pplus.prnumberbiz.apps.post.ui

import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.pages.ui.PostFragment
import com.pplus.prnumberbiz.core.code.common.EnumData

class PostActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String? {

        return ""
    }

    override fun getLayoutRes(): Int {

        return R.layout.activity_my_config
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.my_config_container, PostFragment.newInstance(), PostFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_store_news), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_reg_post))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val intent = Intent(this@PostActivity, PostRegActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    startActivityForResult(intent, Const.REQ_REG)
                }
            }
        }
    }
}
