package com.lejel.wowbox.apps.buff.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityBuffGuerrillaMiningBinding

class BuffGuerrillaMiningActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityBuffGuerrillaMiningBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffGuerrillaMiningBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.layoutBuffGuerrillaTelegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BuffCoin_Official_chat")))
        }

        binding.layoutBuffGuerrillaDiscord.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/channels/1194087825486909460/1194543366025781378")))
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_mining4_desc1), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}