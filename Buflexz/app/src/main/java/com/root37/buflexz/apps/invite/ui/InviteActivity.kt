package com.root37.buflexz.apps.invite.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityInviteBinding

class InviteActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityInviteBinding

    override fun getLayoutView(): View {
        binding = ActivityInviteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val key = LoginInfoManager.getInstance().member!!.userKey
        binding.textInviteRecommendKey.text = key
        binding.imageInviteRecommendKeyCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText(getString(R.string.word_recommend_code), key)
            clipboard.setPrimaryClip(clip)
            ToastUtil.show(this, R.string.msg_copied_clipboard)
        }

        binding.textInviteHistory.setOnClickListener {
            val intent = Intent(this, InviteHistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textInvite.setOnClickListener {
            share()
        }
    }

    private fun share() {
        val recommendKey = LoginInfoManager.getInstance().member!!.userKey
        val text = "${getString(R.string.format_invite_description, recommendKey)}\n${getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().member!!.userKey)}" //        val text = "${getString(R.string.msg_invite_desc)}\n${getString(R.string.msg_invite_url)}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
        startActivity(chooserIntent)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_invite_friend), ToolbarOption.ToolbarMenu.LEFT)
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