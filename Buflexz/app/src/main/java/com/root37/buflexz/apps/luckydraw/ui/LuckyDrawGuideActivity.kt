package com.root37.buflexz.apps.luckydraw.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.community.ui.CommunityApplyActivity
import com.root37.buflexz.apps.invite.ui.InviteActivity
import com.root37.buflexz.apps.luckyball.ui.LuckyBallExchangeActivity
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLuckyDrawGuideBinding

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