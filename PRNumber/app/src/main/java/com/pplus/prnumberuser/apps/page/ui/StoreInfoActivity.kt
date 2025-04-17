package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.databinding.ActivityStoreInfoBinding

class StoreInfoActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityStoreInfoBinding

    override fun getLayoutView(): View {
        binding = ActivityStoreInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage: Page? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.store_info_container, StoreInfoFragment.newInstance(mPage!!), StoreInfoFragment::class.java.simpleName)
        ft.commit()
    }

}
