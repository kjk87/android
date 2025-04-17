package com.lejel.wowbox.apps.luckybox.ui

import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.databinding.ActivityLuckyBoxContainerBinding

class LuckyBoxContainerActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityLuckyBoxContainerBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyBoxContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val tab = intent.getIntExtra(Const.TAB, 0)

        binding.textLuckyBoxContainerNotOpen.setOnClickListener {

            notOpen()
        }

        binding.textLuckyBoxContainerOpen.setOnClickListener {

            open()
        }

        when (tab) {
            0 -> {
                notOpen()
            }
            1 -> {
                open()
            }
        }


    }

    fun notOpen() {
        binding.textLuckyBoxContainerNotOpen.isSelected = true
        binding.textLuckyBoxContainerOpen.isSelected = false

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container_lucky_box_container, LuckyBoxNotOpenFragment.newInstance(), LuckyBoxNotOpenFragment::class.java.simpleName)
        ft.commit()
    }

    fun open() {
        binding.textLuckyBoxContainerNotOpen.isSelected = false
        binding.textLuckyBoxContainerOpen.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container_lucky_box_container, LuckyBoxOpenFragment.newInstance(), LuckyBoxOpenFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lucky_box_container), ToolbarOption.ToolbarMenu.LEFT)
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