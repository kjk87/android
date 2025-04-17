package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.MainEventReviewFragment
import com.pplus.luckybol.databinding.ActivityEventReviewBinding

class EventReviewActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityEventReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setEventFragment()
    }

    private fun setEventFragment() {
        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.event_review_container, MainEventReviewFragment.newInstance(), MainEventReviewFragment::class.java.simpleName)
        ft.replace(R.id.event_review_container, EventWinFragment.newInstance(), MainEventReviewFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_impression), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    finish()
                }
                else -> {}
            }
        }
    }
}
