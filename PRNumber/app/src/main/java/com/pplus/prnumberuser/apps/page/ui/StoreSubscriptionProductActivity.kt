package com.pplus.prnumberuser.apps.page.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.product.ui.StoreMoneyProductFragment
import com.pplus.prnumberuser.apps.product.ui.StoreSubscriptionProductFragment
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.databinding.ActivityStoreProductBinding

class StoreSubscriptionProductActivity : BaseActivity(), ImplToolbar {
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
        val type = intent.getStringExtra(Const.TYPE)
        val ft = supportFragmentManager.beginTransaction()
        when(type){
            Const.SUBSCRIPTION->{
                ft.replace(R.id.store_product_container, StoreSubscriptionProductFragment.newInstance(mPage!!), StoreProductTicketFragment::class.java.simpleName)
            }
            Const.PREPAYMENT->{
                ft.replace(R.id.store_product_container, StoreMoneyProductFragment.newInstance(mPage!!), StoreMoneyProductFragment::class.java.simpleName)
            }
        }

        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        val type = intent.getStringExtra(Const.TYPE)
        when(type){
            Const.SUBSCRIPTION->{
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_subscription), ToolbarOption.ToolbarMenu.LEFT)
            }
            Const.PREPAYMENT->{
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_money_product), ToolbarOption.ToolbarMenu.LEFT)
            }
        }
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
