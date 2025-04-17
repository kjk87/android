package com.pplus.prnumberbiz.apps.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_prlink.*

class PRLinkActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_prlink
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        text_prlink_apply.setOnClickListener{
            //https://docs.google.com/forms/d/e/1FAIpQLSeMwaRqu-fPqg6oAeqSJp1bWkUxl6lLEwWE-_gDfdNZ6NKaHg/viewform
            PplusCommonUtil.openChromeWebView(this, "https://docs.google.com/forms/d/e/1FAIpQLSeMwaRqu-fPqg6oAeqSJp1bWkUxl6lLEwWE-_gDfdNZ6NKaHg/viewform")
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_apply_marketing), ToolbarOption.ToolbarMenu.LEFT)
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
