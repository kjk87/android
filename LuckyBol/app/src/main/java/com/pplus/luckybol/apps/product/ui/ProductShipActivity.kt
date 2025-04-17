package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.MainShipTypeFragment
import com.pplus.luckybol.databinding.ActivityPlayBinding

class ProductShipActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlayBinding

    override fun getLayoutView(): View {
        binding = ActivityPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.play_container, MainShipTypeFragment.newInstance(), MainShipTypeFragment::class.java.simpleName)
        ft.commit()

    }

}
