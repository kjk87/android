package com.lejel.wowbox.apps.luckydraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.community.ui.CommunityApplyActivity
import com.lejel.wowbox.apps.invite.ui.InviteActivity
import com.lejel.wowbox.apps.luckyball.ui.LuckyBallExchangeActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawGuideBinding

class LuckyDrawGuideActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textLuckyDrawGuideExchangeBuff.setOnClickListener {

        }

        binding.textLuckyDrawGuideExchangePoint.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val intent = Intent(this, LuckyBallExchangeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textLuckyDrawGuideExchangeInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val intent = Intent(this, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textLuckyDrawGuideExchangeTelegram.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val intent = Intent(this, CommunityApplyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lucky_draw_guide_en), ToolbarOption.ToolbarMenu.LEFT)
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