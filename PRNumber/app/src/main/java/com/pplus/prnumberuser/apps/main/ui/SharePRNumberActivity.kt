package com.pplus.prnumberuser.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivitySharePrnumberBinding

class SharePRNumberActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivitySharePrnumberBinding

    override fun getLayoutView(): View {
        binding = ActivitySharePrnumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.textSharePrnumber.setOnClickListener {
            share()
        }
    }

    private fun share() {
//        val recommendPoint = FormatUtil.getMoneyType(CountryConfigManager.getInstance().config.properties!!.recommendBol.toString())
//        val recommendKey = LoginInfoManager.getInstance().user.recommendKey

        var url = getString(R.string.msg_biz_url)
        if(Const.API_URL.startsWith("https://stage")){
            url = getString(R.string.msg_stage_biz_url)
        }

        val text = "${getString(R.string.msg_biz_share_description)}\n${url}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_share_prnumber), ToolbarOption.ToolbarMenu.LEFT)
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
