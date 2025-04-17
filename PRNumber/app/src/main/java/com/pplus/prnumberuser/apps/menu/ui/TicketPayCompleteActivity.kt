package com.pplus.prnumberuser.apps.menu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
import com.pplus.prnumberuser.databinding.ActivityOrderPayCompleteBinding
import com.pplus.prnumberuser.databinding.ActivityTicketPayCompleteBinding

class TicketPayCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityTicketPayCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityTicketPayCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {


        binding.textTicketPayCompleteHistory.setOnClickListener {
            val intent = Intent(this, AppMainActivity::class.java)
            intent.putExtra(Const.KEY, Const.TICKET_HISTORY)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textTicketPayCompleteMain.setOnClickListener {
            val intent = Intent(this, AppMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }


    override fun onBackPressed() {

    }
}
