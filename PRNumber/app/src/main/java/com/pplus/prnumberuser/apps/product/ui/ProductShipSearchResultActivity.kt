package com.pplus.prnumberuser.apps.product.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.main.ui.ProductShipListFragment
import com.pplus.prnumberuser.core.network.model.dto.CategoryFirst
import com.pplus.prnumberuser.databinding.ActivityProductShipSearchBinding

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
