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
import com.lejel.wowbox.databinding.ActivityPlayGuideBinding

class PlayGuideActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlayGuideBinding

    override fun getLayoutView(): View {
        binding = ActivityPlayGuideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textPlayGuideBefore.setOnClickListener {
            if(binding.pagerPlayGuide.currentItem != 0){
                binding.pagerPlayGuide.setCurrentItem(binding.pagerPlayGuide.currentItem -1, true)
            }
        }

        binding.textPlayGuideNext.setOnClickListener {
            if(binding.pagerPlayGuide.currentItem < 3){
                binding.pagerPlayGuide.setCurrentItem(binding.pagerPlayGuide.currentItem + 1, true)
            }else{
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.textPlayGuideBefore.visibility = View.INVISIBLE

        val adapter = PlayGuideAdapter()
        binding.pagerPlayGuide.adapter = adapter
        binding.dotsIndicatorPlayGuide.attachTo(binding.pagerPlayGuide)
        binding.pagerPlayGuide.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    binding.textPlayGuideBefore.visibility = View.INVISIBLE
                }else{
                    binding.textPlayGuideBefore.visibility = View.VISIBLE
                }
                if(position < adapter.itemCount - 1){
                    binding.textPlayGuideNext.setText(R.string.word_next2)
                }else{
                    binding.textPlayGuideNext.setText(R.string.word_confirm)
                }

            }
        })

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_play_guide_title), ToolbarOption.ToolbarMenu.LEFT)
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