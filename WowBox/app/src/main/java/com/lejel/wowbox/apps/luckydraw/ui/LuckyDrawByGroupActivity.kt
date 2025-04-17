package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGroup
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawByGroupBinding

class LuckyDrawByGroupActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawByGroupBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawByGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String {
        return ""
    }

    private lateinit var mLuckyDrawGroup: LuckyDrawGroup
    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDrawGroup = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDrawGroup::class.java)!!

        Glide.with(this).load(mLuckyDrawGroup.image).apply(RequestOptions().centerCrop()).into(binding.imageLuckyDrawByGroup)

        binding.textLuckyDrawByGroupTitle.text = mLuckyDrawGroup.title

        binding.layoutLuckyDrawByGroupActiveTab.setOnClickListener {
            binding.layoutLuckyDrawByGroupActiveTab.isSelected = true
            binding.layoutLuckyDrawByGroupEndTab.isSelected = false
            activeTab()
        }

        binding.layoutLuckyDrawByGroupEndTab.setOnClickListener {
            binding.layoutLuckyDrawByGroupActiveTab.isSelected = false
            binding.layoutLuckyDrawByGroupEndTab.isSelected = true
            endTab()
        }

        binding.layoutLuckyDrawByGroupActiveTab.isSelected = true
        binding.layoutLuckyDrawByGroupEndTab.isSelected = false
        activeTab()
    }

    private fun activeTab() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.lucky_draw_by_group_container, LuckyDrawByGroupFragment.newInstance(mLuckyDrawGroup), LuckyDrawByGroupFragment::class.java.simpleName)
        ft.commit()
    }

    private fun endTab(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.lucky_draw_by_group_container, LuckyDrawCompleteByGroupFragment.newInstance(mLuckyDrawGroup), LuckyDrawCompleteByGroupFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
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