package com.lejel.wowbox.apps.event.ui

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.data.GetLuckyBallGuideAdapter
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityGetLuckyBallGuideBinding

class GetLuckyBallGuideActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGetLuckyBallGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityGetLuckyBallGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textGetLuckyBallGuideBefore.setOnClickListener {
            if(binding.pagerGetLuckyBallGuide.currentItem != 0){
                binding.pagerGetLuckyBallGuide.setCurrentItem(binding.pagerGetLuckyBallGuide.currentItem -1, true)
            }
        }

        binding.textGetLuckyBallGuideNext.setOnClickListener {
            if(binding.pagerGetLuckyBallGuide.currentItem < 3){
                binding.pagerGetLuckyBallGuide.setCurrentItem(binding.pagerGetLuckyBallGuide.currentItem + 1, true)
            }else{
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.textGetLuckyBallGuideBefore.visibility = View.INVISIBLE

        val adapter = GetLuckyBallGuideAdapter()
        binding.pagerGetLuckyBallGuide.adapter = adapter
        binding.dotsIndicatorGetLuckyBallGuide.attachTo(binding.pagerGetLuckyBallGuide)
        binding.pagerGetLuckyBallGuide.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    binding.textGetLuckyBallGuideBefore.visibility = View.INVISIBLE
                }else{
                    binding.textGetLuckyBallGuideBefore.visibility = View.VISIBLE
                }
                if(position < adapter.itemCount - 1){
                    binding.textGetLuckyBallGuideNext.setText(R.string.word_next2)
                }else{
                    binding.textGetLuckyBallGuideNext.setText(R.string.word_confirm)
                }

            }
        })

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_get_lucky_ball_title), ToolbarOption.ToolbarMenu.LEFT)
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