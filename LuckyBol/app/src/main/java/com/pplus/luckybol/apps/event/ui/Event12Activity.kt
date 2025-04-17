package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityPrNumberEventBinding

class Event12Activity : BaseActivity() {
    override fun getPID(): String {
        return "Home_12 o'clock event"
    }

    private lateinit var binding: ActivityPrNumberEventBinding

    override fun getLayoutView(): View {
        binding = ActivityPrNumberEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setEventFragment()
    }

    private fun setEventFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.pr_number_event_container, Event12Fragment.newInstance(), Event12Fragment::class.java.simpleName)
        ft.commit()
    }

}
