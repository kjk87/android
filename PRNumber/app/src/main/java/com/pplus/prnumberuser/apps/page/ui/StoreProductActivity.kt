package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.databinding.ActivityStoreProductBinding

class StoreProductActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityStoreProductBinding

    override fun getLayoutView(): View {
        binding = ActivityStoreProductBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPage: Page? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mPage = intent.getParcelableExtra(Const.PAGE)
        val ft = supportFragmentManager.beginTransaction()
        if(mPage!!.storeType == "online"){
            ft.replace(R.id.store_product_container, StoreProductShipFragment.newInstance(mPage!!), StoreProductShipFragment::class.java.simpleName)
        }else{
            ft.replace(R.id.store_product_container, StoreProductTicketFragment.newInstance(mPage!!), StoreProductTicketFragment::class.java.simpleName)
        }
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
