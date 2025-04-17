package com.pplus.prnumberuser.apps.product.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.AppMainActivity
import com.pplus.prnumberuser.databinding.ActivityShipTypePayCompleteBinding

class ShipTypePayCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityShipTypePayCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityShipTypePayCompleteBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun initializeView(savedInstanceState: Bundle?) {


        binding.textShipTypePayCompleteConfirm.setOnClickListener {
            goMain()
        }
    }

    private fun goMain(){
        val intent = Intent(this, AppMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }


    override fun onBackPressed() {

    }
}
