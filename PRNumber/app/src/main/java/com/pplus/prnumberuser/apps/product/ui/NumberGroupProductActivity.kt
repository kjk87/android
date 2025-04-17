package com.pplus.prnumberuser.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.VirtualNumberManage
import com.pplus.prnumberuser.databinding.ActivityStoreProductBinding

class NumberGroupProductActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityStoreProductBinding

    override fun getLayoutView(): View {
        binding = ActivityStoreProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val virtualNumberManage = intent.getParcelableExtra<VirtualNumberManage>(Const.DATA)
        val ft = supportFragmentManager.beginTransaction()
        if(virtualNumberManage!!.productType == "delivery"){
            ft.replace(R.id.store_product_container, NumberGroupProductShipFragment.newInstance(virtualNumberManage), NumberGroupProductShipFragment::class.java.simpleName)
        }else{
            ft.replace(R.id.store_product_container, NumberGroupProductTicketFragment.newInstance(virtualNumberManage), NumberGroupProductTicketFragment::class.java.simpleName)
        }
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {
        val virtualNumberManage = intent.getParcelableExtra<VirtualNumberManage>(Const.DATA)
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(virtualNumberManage!!.groupName, ToolbarOption.ToolbarMenu.LEFT)
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
