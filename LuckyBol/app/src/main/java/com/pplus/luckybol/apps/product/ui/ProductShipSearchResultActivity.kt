package com.pplus.luckybol.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.model.dto.CategoryFirst
import com.pplus.luckybol.databinding.ActivityProductShipSearchBinding

class ProductShipSearchResultActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityProductShipSearchBinding

    override fun getLayoutView(): View {
        binding = ActivityProductShipSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    var mSearch = ""

    override fun initializeView(savedInstanceState: Bundle?) {
        mSearch = intent.getStringExtra(Const.DATA)!!
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.product_ship_search_container, ProductShipListFragment.newInstance(CategoryFirst()), ProductShipListFragment::class.java.simpleName)
        ft.commit()

    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_search_result), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
