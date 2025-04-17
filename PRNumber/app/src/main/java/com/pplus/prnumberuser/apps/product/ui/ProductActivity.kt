package com.pplus.prnumberuser.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.MainShipTypeFragment
import com.pplus.prnumberuser.databinding.ActivityProductBinding

class ProductActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityProductBinding

    override fun getLayoutView(): View {
        binding = ActivityProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.product_container, MainShipTypeFragment.newInstance(), MainShipTypeFragment::class.java.simpleName)

        ft.commit()

    }

}
