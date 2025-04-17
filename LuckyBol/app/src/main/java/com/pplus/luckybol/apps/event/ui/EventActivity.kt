package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityEventBinding

class EventActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Home_Lucky Time event"
    }

    private lateinit var binding: ActivityEventBinding

    override fun getLayoutView(): View {
        binding = ActivityEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val group = intent.getIntExtra(Const.GROUP, 1)

        when(group){
            1->{
                setTitle(getString(R.string.word_daily_12))
                setEventFragment(group)
            }
            2->{
                setTitle(getString(R.string.word_sponsor_event))
                setEventFragment(group)
            }
            3->{
                setTitle(getString(R.string.word_play_a))
                setPlayAFragment()
            }
        }
    }

    private fun setEventFragment(group:Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.event_container, EventByGroupFragment.newInstance(group), EventFragment::class.java.simpleName)
        ft.commit()
    }

    private fun setPlayAFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.event_container, PlayGroupAFragment.newInstance(), EventFragment::class.java.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_charge_station), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
