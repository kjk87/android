package com.lejel.wowbox.apps.event.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.data.PlayGuideAdapter
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyBallGuideBinding
import com.lejel.wowbox.databinding.ActivityPlayGuideBinding

class LuckyBallGuideActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityLuckyBallGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBallGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textLuckyBallGuideConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lucky_ball2), ToolbarOption.ToolbarMenu.LEFT)
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