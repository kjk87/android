package com.pplus.prnumberuser.apps.event.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityEventBinding

class EventActivity : BaseActivity() {
    override fun getPID(): String {
        return "Home_Lucky Time event"
    }

    private lateinit var binding: ActivityEventBinding
    override fun getLayoutView(): View {
        binding = ActivityEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        setEventFragment()
    }

    private fun setEventFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.event_container, EventFragment.newInstance(1), EventFragment::class.java.simpleName)
        ft.commit()
    }

}
